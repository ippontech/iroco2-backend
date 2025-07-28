package fr.ippon.iroco2.domain.calculateur.exception;

import fr.ippon.iroco2.domain.commons.exception.NotFoundException;
import lombok.Getter;

import java.util.UUID;

@Getter
public class InfrastructureNotFound extends NotFoundException {

    public InfrastructureNotFound(UUID infrastructureId) {
        super("L'infrastructure d'id '%s' n'existe pas".formatted(infrastructureId));
    }
}
