package fr.ippon.iroco2.domain.estimateur.model.cpu;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@Getter
@RequiredArgsConstructor
public enum CPUArchitectureType {
    X_86("x86"), ARM("ARM");

    private final String name;

    public static Optional<CPUArchitectureType> findByName(String name) {
        for (CPUArchitectureType cpuArchitectureType : values()) {
            if (cpuArchitectureType.name.equals(name)) {
                return Optional.of(cpuArchitectureType);
            }
        }
        return Optional.empty();
    }

    public CPUType getDefaultCPUType() {
        return switch (this) {
            case X_86 -> CPUType.XEON;
            case ARM -> CPUType.GRAVITON;
        };
    }
}
