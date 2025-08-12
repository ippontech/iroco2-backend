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
package fr.ippon.iroco2.domain.estimator.model;

import fr.ippon.iroco2.domain.estimator.model.cpu.CPUConfig;

import java.time.Duration;

public record EstimatableServer(CPUConfig cpu, MemoryConfig ram, MemoryConfig disk, Duration duration) {

    public static final double SSD_POWER_CONSUMPTION_IN_W_PER_TB = 1.52;
    public static final double RAM_POWER_CONSUMPTION_IN_W_PER_GB = (double) 3 / 8;
    public static final int MINIMAL_NUMBER_OF_REPLICATIONS_FOR_S3_STANDARD = 3;
    public static final int MINUTES_PER_HOUR = 60;

    private double getWattPower() {
        var cpuPower = cpu.getPower();
        var ramPower = ram.asGigaBytes() * RAM_POWER_CONSUMPTION_IN_W_PER_GB;
        var diskPower = disk.asTeraBytes() * SSD_POWER_CONSUMPTION_IN_W_PER_TB * MINIMAL_NUMBER_OF_REPLICATIONS_FOR_S3_STANDARD;
        return cpuPower + ramPower + diskPower;
    }

    private double getHours() {
        return (double) duration.toMinutes() / MINUTES_PER_HOUR;
    }

    public double getWattHoursConsumption() {
        return getHours() * getWattPower();
    }

}