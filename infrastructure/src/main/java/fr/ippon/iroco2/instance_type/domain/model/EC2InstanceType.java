package fr.ippon.iroco2.instance_type.domain.model;

import fr.ippon.iroco2.domain.estimateur.model.cpu.GPUType;

import java.math.BigDecimal;
import java.util.Optional;

public record EC2InstanceType(
        String name,
        int vCPUs,
        BigDecimal memory,
        String cpuType,
        Optional<Integer> gpus,
        Optional<GPUType> gpuType
) {
}
