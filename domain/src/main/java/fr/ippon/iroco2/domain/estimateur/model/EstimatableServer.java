package fr.ippon.iroco2.domain.estimateur.model;

import fr.ippon.iroco2.domain.estimateur.model.cpu.CPUConfig;

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