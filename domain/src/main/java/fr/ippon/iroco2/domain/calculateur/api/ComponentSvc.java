package fr.ippon.iroco2.domain.calculateur.api;


import fr.ippon.iroco2.domain.calculateur.model.Component;

import java.util.List;
import java.util.UUID;

public interface ComponentSvc {
    void save(Component component);

    void delete(UUID componentId);

    List<Component> findAllByInfrastructureID(UUID infrastructureId);

    void update(Component component);
}
