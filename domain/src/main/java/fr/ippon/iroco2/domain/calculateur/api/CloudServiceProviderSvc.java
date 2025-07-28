package fr.ippon.iroco2.domain.calculateur.api;

import fr.ippon.iroco2.domain.calculateur.model.CloudServiceProvider;

import java.util.List;
import java.util.UUID;

public interface CloudServiceProviderSvc {
    CloudServiceProvider findById(UUID uuid);

    List<CloudServiceProvider> findAll();
}
