package fr.ippon.iroco2.calculateur.presentation.request;

import fr.ippon.iroco2.domain.calculateur.model.Infrastructure;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record InfrastructureRequest(
        @NotBlank
        @Size(min = 2, max = 255)
        String name,
        @NotNull
        UUID cloudServiceProvider,
        @NotNull
        UUID defaultRegion) {

    public Infrastructure createToDomain(String owner) {
        return Infrastructure.create(name, cloudServiceProvider, defaultRegion, owner);
    }
}
