package fr.ippon.iroco2.domain.estimateur.model.cpu;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class RealCPUConfig implements CPUConfig {
    private final CPUType cpu;
    private final int vCpuNumber;
    private final double usageRatio;


    @Override
    public double getPower() {
        return cpu.getOneCorePowerInWatt() * vCpuNumber * usageRatio;
    }

}
