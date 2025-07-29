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
package fr.ippon.iroco2.domain.estimateur;

public final class TimeConstant {
    public static final int MS_IN_ONE_DAY = 1000 * 60 * 60 * 24;
    public static final double AVERAGE_DAYS_PER_MONTH = 30.44;
    public static final double MS_IN_ONE_MONTH = MS_IN_ONE_DAY * AVERAGE_DAYS_PER_MONTH;

    private TimeConstant() {
    }
}
