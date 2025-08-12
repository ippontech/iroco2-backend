/*
 * Copyright 2025 Ippon Technologies
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package fr.ippon.iroco2.domain.estimator;

import fr.ippon.iroco2.domain.calculator.model.Component;
import fr.ippon.iroco2.domain.calculator.model.ConfiguredSetting;
import fr.ippon.iroco2.domain.calculator.model.emu.SettingName;
import fr.ippon.iroco2.domain.commons.DomainService;
import fr.ippon.iroco2.domain.commons.exception.FunctionalException;
import fr.ippon.iroco2.domain.commons.model.Payload;
import fr.ippon.iroco2.domain.estimator.aws.EC2Instance;
import fr.ippon.iroco2.domain.estimator.aws.EC2InstanceStorage;
import fr.ippon.iroco2.domain.estimator.exception.GlobalEnergyMixNotFound;
import fr.ippon.iroco2.domain.estimator.model.EstimatableServer;
import fr.ippon.iroco2.domain.estimator.model.MemoryConfig;
import fr.ippon.iroco2.domain.estimator.model.cpu.CPUArchitectureType;
import fr.ippon.iroco2.domain.estimator.model.cpu.CPUConfig;
import fr.ippon.iroco2.domain.estimator.model.cpu.CPUType;
import fr.ippon.iroco2.domain.estimator.model.cpu.RealCPUConfig;
import fr.ippon.iroco2.domain.estimator.model.cpu.ServerlessCPUConfig;
import fr.ippon.iroco2.domain.estimator.secondary.GlobalEnergyMixStorage;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.Optional;
import java.util.function.Function;

import static fr.ippon.iroco2.domain.calculator.model.emu.SettingName.MEMORY_IN_MEGA_BYTE;
import static fr.ippon.iroco2.domain.calculator.model.emu.SettingName.PROCESSOR_ARCHITECTURE;
import static fr.ippon.iroco2.domain.calculator.model.emu.SettingName.STORAGE_IN_MEGA_BYTE;
import static fr.ippon.iroco2.domain.commons.model.PayloadConfiguration.INSTANCE_TYPE;
import static fr.ippon.iroco2.domain.commons.model.PayloadConfiguration.S3_STORAGE;
import static fr.ippon.iroco2.domain.estimator.TimeConstant.AVERAGE_DAYS_PER_MONTH;
import static fr.ippon.iroco2.domain.estimator.TimeConstant.MS_IN_ONE_DAY;
import static fr.ippon.iroco2.domain.estimator.TimeConstant.MS_IN_ONE_MONTH;
import static fr.ippon.iroco2.domain.estimator.aws.EC2Instance.TDP_TO_POWER_CONSUMPTION_RATIO;
import static fr.ippon.iroco2.domain.estimator.model.MemoryConfig.ZERO_MB;
import static java.lang.Integer.parseInt;
import static java.math.RoundingMode.UP;

@DomainService
@RequiredArgsConstructor
public class CarbonEstimator {
    private static final BigDecimal NUMBER_OF_MEMORY_FOR_ONE_LAMBDA_VCPU_IN_MO = BigDecimal.valueOf(1769);
    private final GlobalEnergyMixStorage globalEnergyMixStorage;
    private final EC2InstanceStorage ec2InstanceStorage;

    private static Duration computeDuration(Component component) {
        double percentageUptime = 1;
        for (ConfiguredSetting configurationValue : component.getConfigurationValues()) {
            switch (configurationValue.configurationSettingName()) {
                case INSTANCE_NUMBER, VOLUME_NUMBER, MONTHLY_INVOCATION_COUNT ->
                        percentageUptime *= parseInt(configurationValue.value());
                case DAYS_ON_PER_MONTH ->
                        percentageUptime *= parseInt(configurationValue.value()) / AVERAGE_DAYS_PER_MONTH;
                case DAILY_USAGE_COUNT ->
                        percentageUptime *= parseInt(configurationValue.value()) * AVERAGE_DAYS_PER_MONTH;
                case AVERAGE_EXEC_TIME_IN_MS ->
                        percentageUptime *= parseInt(configurationValue.value()) / MS_IN_ONE_MONTH;
                case DAILY_RUNNING_TIME_IN_MS ->
                        percentageUptime *= (double) parseInt(configurationValue.value()) / MS_IN_ONE_DAY;
                default -> { // not an UpTime parameter
                }
            }
        }
        return Duration.ofMillis((long) (percentageUptime * MS_IN_ONE_MONTH));
    }

    private static MemoryConfig findMemoryConfig(Component component, SettingName settingName) {
        return Optional.ofNullable(component.getValue(settingName))
                .map(Double::parseDouble)
                .map(MemoryConfig::new)
                .orElse(ZERO_MB);
    }

    private static CPUConfig findCPU(Component component) {
        return Optional.ofNullable(component.getValue(PROCESSOR_ARCHITECTURE))
                .map(CPUArchitectureType::valueOf)
                .map(cpuArchitectureType
                        -> buildRealCPUConfig(cpuArchitectureType.getDefaultCPUType(), getVCpuNumber(component), 1))
                .orElse(new ServerlessCPUConfig());
    }

    private static int getVCpuNumber(Component component) {
        return component.isLambda() ? BigDecimal.valueOf(component.getMemoryInMegaByte()).divide(NUMBER_OF_MEMORY_FOR_ONE_LAMBDA_VCPU_IN_MO, 0, UP).intValue() : 1;
    }

    private static Function<EC2Instance, EstimatableServer> getEc2InstanceEstimatableServerFunction(MemoryConfig disk, Duration duration) {
        return ec2 -> new EstimatableServer(
                buildRealCPUConfig(ec2.getCpuType(), ec2.getVCPUs(), TDP_TO_POWER_CONSUMPTION_RATIO),
                new MemoryConfig(ec2.getMemory().doubleValue()),
                disk,
                duration);
    }

    private static CPUConfig buildRealCPUConfig(CPUType type, int nbVCPU, double ratio) {
        return new RealCPUConfig(type, nbVCPU, ratio);
    }

    private EstimatableServer getEstimatableServer(String instanceType, MemoryConfig disk, Duration duration, CPUConfig cpu, MemoryConfig ram) {
        return Optional.ofNullable(instanceType)
                .flatMap(ec2InstanceStorage::findByName)
                .map(getEc2InstanceEstimatableServerFunction(disk, duration))
                .orElse(new EstimatableServer(cpu, ram, disk, duration));
    }

    public int estimateComponent(String countryIsoCode, Component component) throws FunctionalException {
        CPUConfig cpu = findCPU(component);
        MemoryConfig ram = findMemoryConfig(component, MEMORY_IN_MEGA_BYTE);
        MemoryConfig disk = findMemoryConfig(component, STORAGE_IN_MEGA_BYTE);
        Duration duration = computeDuration(component);
        var estimatableServer = getEstimatableServer(component.getValue(SettingName.INSTANCE_TYPE), disk, duration, cpu, ram);
        return estimateServer(countryIsoCode, estimatableServer);
    }

    public int estimatePayload(String countryIsoCode, Payload payload) throws FunctionalException {
        CPUConfig cpu = new ServerlessCPUConfig();
        MemoryConfig disk = Optional.ofNullable(payload.getValue(S3_STORAGE)).map(Double::parseDouble).map(MemoryConfig::new).orElse(ZERO_MB);
        Duration duration = payload.durationOfServiceOperation();
        var estimatableServer = getEstimatableServer(payload.getValue(INSTANCE_TYPE), disk, duration, cpu, ZERO_MB);
        return estimateServer(countryIsoCode, estimatableServer);
    }

    int estimateServer(String countryIsoCode, EstimatableServer estimatableServer) throws FunctionalException {
        var countryDataOpt = globalEnergyMixStorage.findByIsoCode(countryIsoCode)
                .orElseThrow(() -> new GlobalEnergyMixNotFound("Country with iso code %s not found, returning 0 by default".formatted(countryIsoCode)));
        var countryEnergyMixCoef = countryDataOpt.carbonGramByWhWeightedMix();
        return (int) (estimatableServer.getWattHoursConsumption() * countryEnergyMixCoef);
    }
}
