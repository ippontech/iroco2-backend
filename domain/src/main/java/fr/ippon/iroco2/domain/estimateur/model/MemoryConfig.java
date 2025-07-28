package fr.ippon.iroco2.domain.estimateur.model;

public record MemoryConfig(double megaBytes) {
    public static final MemoryConfig ZERO_MB = new MemoryConfig(0d);

    private static final int MB_TO_GB = 1_000;
    private static final int MB_TO_TB = MB_TO_GB * MB_TO_GB;

    public double asGigaBytes() {
        return megaBytes / MB_TO_GB;
    }

    public double asTeraBytes() {
        return megaBytes / MB_TO_TB;
    }
}