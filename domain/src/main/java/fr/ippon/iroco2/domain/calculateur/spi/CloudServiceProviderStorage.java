package fr.ippon.iroco2.domain.calculateur.spi;

import fr.ippon.iroco2.domain.calculateur.model.CloudServiceProvider;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CloudServiceProviderStorage {
    List<CloudServiceProvider> findAll();

    Optional<CloudServiceProvider> findById(UUID uuid);
}
