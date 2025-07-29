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
package fr.ippon.iroco2.legacy.common.application;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

public class DurationUtils {

    private DurationUtils() {
        //private constructor for static class

    }

    public static Duration ofMonths(long numberOfMonth) {
        long durationInSecond = ChronoUnit.MONTHS.getDuration().toSeconds() * numberOfMonth;
        return Duration.ofSeconds(durationInSecond);
    }

    public static Duration ofWeeks(long numberOfWeek) {
        long durationInSecond = ChronoUnit.WEEKS.getDuration().toSeconds() * numberOfWeek;
        return Duration.ofSeconds(durationInSecond);
    }
}
