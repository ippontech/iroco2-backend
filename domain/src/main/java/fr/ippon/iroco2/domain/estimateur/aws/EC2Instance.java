package fr.ippon.iroco2.domain.estimateur.aws;

import fr.ippon.iroco2.domain.estimateur.model.cpu.CPUType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class EC2Instance {
    public static final double TDP_TO_POWER_CONSUMPTION_RATIO = 0.5;

    private final String name;
    private final BigDecimal memory;
    private final int vCPUs;
    private final CPUType cpuType;

    public static EC2Instance load(String name, BigDecimal memory, int vCPUs, CPUType cpuType) {
        return new EC2Instance(name, memory, vCPUs, cpuType);
    }
}
