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
