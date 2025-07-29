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

import java.math.BigDecimal;
import java.math.RoundingMode;

public class BigDecimalUtils {

    private static final BigDecimal ONE_HUNDRED = new BigDecimal("100");

    private BigDecimalUtils() {
        // private constructor for static class
    }

    public static BigDecimal toPercentageOf(BigDecimal value, BigDecimal total) {
        return value.divide(total, 4, RoundingMode.HALF_UP).multiply(ONE_HUNDRED);
    }

    public static BigDecimal percentOf(BigDecimal percentage, BigDecimal total) {
        return percentage.multiply(total).divide(ONE_HUNDRED, 2, RoundingMode.HALF_UP);
    }
}
