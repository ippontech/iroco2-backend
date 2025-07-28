package fr.ippon.iroco2.domain.estimateur.model.cpu;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CPUType {
    EPYC(CPUArchitectureType.X_86, (double) 350 / 56),
    XEON(CPUArchitectureType.X_86, (double) 270 / 64),
    GRAVITON(CPUArchitectureType.ARM, (double) 100 / 64),
    ETSY(CPUArchitectureType.X_86, 2.1);

    private final CPUArchitectureType cpuArchitectureType;
    private final double oneCorePowerInWatt;
}
