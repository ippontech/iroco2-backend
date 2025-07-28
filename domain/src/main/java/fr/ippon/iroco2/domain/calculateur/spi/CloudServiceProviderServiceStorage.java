package fr.ippon.iroco2.domain.calculateur.spi;

import fr.ippon.iroco2.domain.calculateur.model.CloudServiceProviderService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CloudServiceProviderServiceStorage {
    List<CloudServiceProviderService> findAllByCsp(UUID cspId);

    Optional<CloudServiceProviderService> findById(UUID serviceId);

    List<CloudServiceProviderService> findAll();
}
