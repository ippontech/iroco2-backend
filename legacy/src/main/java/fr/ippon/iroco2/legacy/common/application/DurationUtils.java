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
