package fr.ippon.iroco2.domain.calculateur.spi;

import fr.ippon.iroco2.domain.calculateur.model.CloudServiceProviderRegion;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CloudServiceProviderRegionStorage {
    List<CloudServiceProviderRegion> findAllByCsp(UUID cspId);

    List<CloudServiceProviderRegion> findAll();

    Optional<CloudServiceProviderRegion> findById(UUID defaultRegion);
}
