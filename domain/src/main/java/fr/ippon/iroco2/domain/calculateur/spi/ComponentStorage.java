package fr.ippon.iroco2.domain.calculateur.spi;

import fr.ippon.iroco2.domain.calculateur.model.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ComponentStorage {
    void save(Component component);

    Optional<Component> findById(UUID componentId);

    void delete(Component component);

    List<Component> findByInfrastructureId(UUID infrastructureId);
}
