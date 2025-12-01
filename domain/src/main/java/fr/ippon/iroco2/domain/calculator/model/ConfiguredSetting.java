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
package fr.ippon.iroco2.domain.calculator.model;

import fr.ippon.iroco2.domain.calculator.model.emu.SettingName;

import java.util.OptionalDouble;
import java.util.UUID;

import static fr.ippon.iroco2.domain.estimator.TimeConstant.AVERAGE_DAYS_PER_MONTH;
import static fr.ippon.iroco2.domain.estimator.TimeConstant.MS_IN_ONE_DAY;
import static fr.ippon.iroco2.domain.estimator.TimeConstant.MS_IN_ONE_MONTH;
import static java.lang.Double.parseDouble;

public record ConfiguredSetting(
        UUID configurationSettingId,
        SettingName configurationSettingName,
        String value) {

    /**
     * Computes the average uptime ratio for this setting according to its type and value.
     *
     * @return an optional containing the uptime ratio corresponding to this setting.
     * Empty if the setting is irrelevant to the uptime ration
     */
    public OptionalDouble computeAverageUptimeRatio() {
        if (!configurationSettingName.isUptimeParameter()) {
            return OptionalDouble.empty();
        }

        final double value = parseDouble(this.value);
        final double averageUptimeRatio = switch (configurationSettingName) {
            case INSTANCE_NUMBER, VOLUME_NUMBER, MONTHLY_INVOCATION_COUNT -> value;
            case DAYS_ON_PER_MONTH -> value / AVERAGE_DAYS_PER_MONTH;
            case DAILY_USAGE_COUNT -> value * AVERAGE_DAYS_PER_MONTH;
            case AVERAGE_EXEC_TIME_IN_MS -> value / MS_IN_ONE_MONTH;
            case DAILY_RUNNING_TIME_IN_MS -> value / MS_IN_ONE_DAY;
            default ->
                    throw new IllegalStateException("Unexpected configuration setting: '%s'".formatted(configurationSettingName));
        };
        return OptionalDouble.of(averageUptimeRatio);
    }
}
