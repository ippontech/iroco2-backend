package fr.ippon.iroco2.domain.calculateur.api;

import fr.ippon.iroco2.domain.calculateur.model.CloudServiceProviderRegion;

import java.util.List;
import java.util.UUID;

public interface CloudServiceProviderRegionSvc {
    List<CloudServiceProviderRegion> findAllByCsp(UUID cspId);
}
