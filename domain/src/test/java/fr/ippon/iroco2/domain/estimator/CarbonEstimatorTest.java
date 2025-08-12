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

import fr.ippon.iroco2.domain.calculator.model.CloudServiceProviderService;
import fr.ippon.iroco2.domain.calculator.model.Component;
import fr.ippon.iroco2.domain.calculator.model.ConfiguredSetting;
import fr.ippon.iroco2.domain.calculator.model.emu.SettingName;
import fr.ippon.iroco2.domain.commons.exception.FunctionalException;
import fr.ippon.iroco2.domain.commons.model.Payload;
import fr.ippon.iroco2.domain.estimator.aws.EC2Instance;
import fr.ippon.iroco2.domain.estimator.aws.EC2InstanceStorage;
import fr.ippon.iroco2.domain.estimator.exception.GlobalEnergyMixNotFound;
import fr.ippon.iroco2.domain.estimator.model.EstimatableServer;
import fr.ippon.iroco2.domain.estimator.model.MemoryConfig;
import fr.ippon.iroco2.domain.estimator.model.cpu.RealCPUConfig;
import fr.ippon.iroco2.domain.estimator.model.cpu.ServerlessCPUConfig;
import fr.ippon.iroco2.domain.estimator.model.energy_mix.GlobalEnergyMix;
import fr.ippon.iroco2.domain.estimator.secondary.GlobalEnergyMixStorage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static fr.ippon.iroco2.domain.calculator.model.Component.LAMBDA_SERVICE_SHORT_NAME;
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
import static fr.ippon.iroco2.domain.estimator.model.cpu.CPUType.EPYC;
import static fr.ippon.iroco2.domain.estimator.model.cpu.CPUType.XEON;
import static java.math.BigDecimal.ZERO;
import static java.math.RoundingMode.UP;
import static java.util.Optional.empty;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CarbonEstimatorTest {
    @Mock
    private GlobalEnergyMixStorage globalEnergyMixStorage;
    @Mock
    private EC2InstanceStorage ec2InstanceStorage;

    @InjectMocks
    @Spy
    private CarbonEstimator carbonEstimator;

    private static EstimatableServer createEstimatableServer() {
        RealCPUConfig cpu = new RealCPUConfig(EPYC, 1, 1);
        return new EstimatableServer(cpu, new MemoryConfig(100), new MemoryConfig(15_000), Duration.ofHours(36));
    }

    private static double getPercentageUptime(SettingName settingName) {
        double percentageUptime;
        switch (settingName) {
            case INSTANCE_NUMBER, VOLUME_NUMBER, MONTHLY_INVOCATION_COUNT -> percentageUptime = 5d;
            case DAYS_ON_PER_MONTH -> percentageUptime = 5d / AVERAGE_DAYS_PER_MONTH;
            case DAILY_USAGE_COUNT -> percentageUptime = 5d * AVERAGE_DAYS_PER_MONTH;
            case AVERAGE_EXEC_TIME_IN_MS -> percentageUptime = 5d / MS_IN_ONE_MONTH;
            case DAILY_RUNNING_TIME_IN_MS -> percentageUptime = 5d / MS_IN_ONE_DAY;
            default -> percentageUptime = 1d;
        }
        return percentageUptime;
    }

    @Test
    void estimate_server_should_throwException_when_GlobalEnergyMixNotFound() {
        // GIVEN
        String isoCode = "isoCode";
        when(globalEnergyMixStorage.findByIsoCode(isoCode)).thenReturn(empty());

        // WHEN
        var error = catchThrowable(() -> carbonEstimator.estimateServer(isoCode, createEstimatableServer()));

        // THEN
        assertThat(error).isInstanceOf(GlobalEnergyMixNotFound.class)
                .hasMessage("Country with iso code %s not found, returning 0 by default".formatted(isoCode));
    }

    @Test
    void estimate_server_should_computeCO2() throws FunctionalException {
        // GIVEN
        var globalEnergyMix = givenExistingGlobaEnergy();
        EstimatableServer estimatableServer = createEstimatableServer();

        // WHEN
        var result = carbonEstimator.estimateServer(globalEnergyMix.getIsoCode(), estimatableServer);

        // THEN
        double expectedEstimation = estimatableServer.getWattHoursConsumption() * globalEnergyMix.carbonGramByWhWeightedMix();
        assertThat(result).isEqualTo((int) expectedEstimation);
    }

    @Test
    void estimate_payload_should_estimate_with_default_if_no_instance_type() throws FunctionalException {
        // GIVEN
        var globalEnergyMix = givenExistingGlobaEnergy();
        Duration duration = Duration.ofHours(10);
        Payload payload = new Payload(null, null, null, duration, 0, null);
        String isoCode = globalEnergyMix.getIsoCode();

        // WHEN
        carbonEstimator.estimatePayload(isoCode, payload);

        // THEN
        ArgumentCaptor<EstimatableServer> captor = ArgumentCaptor.forClass(EstimatableServer.class);
        verify(carbonEstimator).estimateServer(any(), captor.capture());
        EstimatableServer estimatableServer = captor.getValue();
        assertThat(estimatableServer.disk()).isEqualTo(ZERO_MB);
        assertThat(estimatableServer.ram()).isEqualTo(ZERO_MB);
        assertThat(estimatableServer.duration()).isEqualTo(duration);
        assertThat(estimatableServer.cpu()).isInstanceOf(ServerlessCPUConfig.class);
    }

    @Test
    void estimate_payload_should_estimate_with_given_data() throws FunctionalException {
        // GIVEN
        var globalEnergyMix = givenExistingGlobaEnergy();
        Duration duration = Duration.ofHours(10);
        var confWithInstance = Map.of(INSTANCE_TYPE, "666", S3_STORAGE, "1000");
        Payload payload = new Payload(null, null, null, duration, 0, confWithInstance);
        when(ec2InstanceStorage.findByName("666")).thenReturn(Optional.of(EC2Instance.load("666", ZERO, 5, EPYC)));

        // WHEN
        carbonEstimator.estimatePayload(globalEnergyMix.getIsoCode(), payload);

        // THEN
        verify(ec2InstanceStorage).findByName("666");
        ArgumentCaptor<EstimatableServer> captor = ArgumentCaptor.forClass(EstimatableServer.class);
        verify(carbonEstimator).estimateServer(any(), captor.capture());
        EstimatableServer estimatableServer = captor.getValue();
        assertThat(estimatableServer.cpu())
                .isInstanceOf(RealCPUConfig.class)
                .matches(cpuConfig -> cpuConfig.getPower() == EPYC.getOneCorePowerInWatt() * 5 * TDP_TO_POWER_CONSUMPTION_RATIO);
        assertThat(estimatableServer.disk().asGigaBytes()).isEqualTo(1d);
        assertThat(estimatableServer.duration()).isEqualTo(duration);
        assertThat(estimatableServer.ram().asGigaBytes()).isEqualTo(0d);
    }

    @Test
    void estimate_component_should_estimate_with_defaults_data() throws FunctionalException {
        // GIVEN
        var globalEnergyMix = givenExistingGlobaEnergy();
        var component = Component.create(null, null, null, null, List.of());

        // WHEN
        carbonEstimator.estimateComponent(globalEnergyMix.getIsoCode(), component);

        // THEN
        ArgumentCaptor<EstimatableServer> captor = ArgumentCaptor.forClass(EstimatableServer.class);
        verify(carbonEstimator).estimateServer(any(), captor.capture());
        EstimatableServer estimatableServer = captor.getValue();
        assertThat(estimatableServer.disk()).isEqualTo(ZERO_MB);
        assertThat(estimatableServer.ram()).isEqualTo(ZERO_MB);
        assertThat(estimatableServer.duration().toMillis()).isEqualTo((long) MS_IN_ONE_MONTH);
        assertThat(estimatableServer.cpu()).isInstanceOf(ServerlessCPUConfig.class);
    }

    @ParameterizedTest
    @EnumSource(SettingName.class)
    void estimate_component_should_estimate_with_given_data_not_lambda(SettingName settingName) throws FunctionalException {
        // GIVEN
        var globalEnergyMix = givenExistingGlobaEnergy();

        CloudServiceProviderService notLamda = mock(CloudServiceProviderService.class);
        ConfiguredSetting csProcessor = new ConfiguredSetting(null, PROCESSOR_ARCHITECTURE, "X_86");
        ConfiguredSetting csMemory = new ConfiguredSetting(null, MEMORY_IN_MEGA_BYTE, "1000000");
        ConfiguredSetting csStorage = new ConfiguredSetting(null, STORAGE_IN_MEGA_BYTE, "1000");
        ConfiguredSetting csVol = new ConfiguredSetting(null, settingName, "5");
        var settings = List.of(csProcessor, csMemory, csStorage, csVol);
        var component = Component.create(null, null, null, notLamda, settings);

        // WHEN
        carbonEstimator.estimateComponent(globalEnergyMix.getIsoCode(), component);

        // THEN
        ArgumentCaptor<EstimatableServer> captor = ArgumentCaptor.forClass(EstimatableServer.class);
        verify(carbonEstimator).estimateServer(any(), captor.capture());
        EstimatableServer estimatableServer = captor.getValue();
        assertThat(estimatableServer.cpu())
                .isInstanceOf(RealCPUConfig.class)
                .matches(cpuConfig -> cpuConfig.getPower() == XEON.getOneCorePowerInWatt() * 1d * 1d);
        assertThat(estimatableServer.disk().asGigaBytes()).isEqualTo(1d);
        assertThat(estimatableServer.ram().asTeraBytes()).isEqualTo(1d);
        double percentageUptime = getPercentageUptime(settingName);
        assertThat(estimatableServer.duration().toMillis()).isEqualTo((long) (MS_IN_ONE_MONTH * percentageUptime));
    }

    @ParameterizedTest
    @EnumSource(SettingName.class)
    void estimate_component_should_estimate_with_given_data_lambda(SettingName settingName) throws FunctionalException {
        // GIVEN
        var globalEnergyMix = givenExistingGlobaEnergy();

        CloudServiceProviderService lamda = mock(CloudServiceProviderService.class);
        when(lamda.getShortname()).thenReturn(LAMBDA_SERVICE_SHORT_NAME);
        ConfiguredSetting csProcessor = new ConfiguredSetting(null, PROCESSOR_ARCHITECTURE, "X_86");
        ConfiguredSetting csMemory = new ConfiguredSetting(null, MEMORY_IN_MEGA_BYTE, "1000000");
        ConfiguredSetting csStorage = new ConfiguredSetting(null, STORAGE_IN_MEGA_BYTE, "1000");
        ConfiguredSetting csVol = new ConfiguredSetting(null, settingName, "5");
        var settings = List.of(csProcessor, csMemory, csStorage, csVol);
        var component = Component.create(null, null, null, lamda, settings);

        // WHEN
        carbonEstimator.estimateComponent(globalEnergyMix.getIsoCode(), component);

        // THEN
        ArgumentCaptor<EstimatableServer> captor = ArgumentCaptor.forClass(EstimatableServer.class);
        verify(carbonEstimator).estimateServer(any(), captor.capture());
        EstimatableServer estimatableServer = captor.getValue();
        int vCpuNumber = BigDecimal.valueOf(1_000_000).divide(BigDecimal.valueOf(1_769), 0, UP).intValue();
        assertThat(estimatableServer.cpu())
                .isInstanceOf(RealCPUConfig.class)
                .matches(cpuConfig -> cpuConfig.getPower() == XEON.getOneCorePowerInWatt() * vCpuNumber * 1d);
        assertThat(estimatableServer.disk().asGigaBytes()).isEqualTo(1d);
        assertThat(estimatableServer.ram().asTeraBytes()).isEqualTo(1d);
        double percentageUptime = getPercentageUptime(settingName);
        assertThat(estimatableServer.duration().toMillis()).isEqualTo((long) (MS_IN_ONE_MONTH * percentageUptime));
    }

    private GlobalEnergyMix givenExistingGlobaEnergy() {
        GlobalEnergyMix globalEnergyMix = new GlobalEnergyMix();
        globalEnergyMix.setCountryName("name");
        globalEnergyMix.setIsoCode("isoCode");

        globalEnergyMix.setBiofuelTWh(BigDecimal.ONE);
        globalEnergyMix.setCarbonIntensity(BigDecimal.ONE);
        globalEnergyMix.setCoalTWh(BigDecimal.ONE);
        globalEnergyMix.setFossilTWh(BigDecimal.ONE);
        globalEnergyMix.setGasTWh(BigDecimal.ONE);
        globalEnergyMix.setHydroelectricityTWh(BigDecimal.ONE);
        globalEnergyMix.setLowCarbonTWh(BigDecimal.ONE);
        globalEnergyMix.setNuclearTWh(BigDecimal.ONE);
        globalEnergyMix.setOilTWh(BigDecimal.ONE);
        globalEnergyMix.setOtherRenewableTWh(BigDecimal.ONE);
        globalEnergyMix.setOtherRenewableExcBiofuelTWh(BigDecimal.ONE);
        globalEnergyMix.setPerCapitaWh(BigDecimal.ONE);
        globalEnergyMix.setRenewablesTWh(BigDecimal.ONE);
        globalEnergyMix.setSolarTWh(BigDecimal.ONE);
        globalEnergyMix.setWindTWh(BigDecimal.ONE);

        globalEnergyMix.setTotalTWh(BigDecimal.valueOf(15));

        when(globalEnergyMixStorage.findByIsoCode(globalEnergyMix.getIsoCode())).thenReturn(Optional.of(globalEnergyMix));
        return globalEnergyMix;
    }

}