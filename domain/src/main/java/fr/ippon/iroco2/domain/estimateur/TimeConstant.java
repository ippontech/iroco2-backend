package fr.ippon.iroco2.domain.estimateur;

public final class TimeConstant {
    public static final int MS_IN_ONE_DAY = 1000 * 60 * 60 * 24;
    public static final double AVERAGE_DAYS_PER_MONTH = 30.44;
    public static final double MS_IN_ONE_MONTH = MS_IN_ONE_DAY * AVERAGE_DAYS_PER_MONTH;

    private TimeConstant() {
    }
}
