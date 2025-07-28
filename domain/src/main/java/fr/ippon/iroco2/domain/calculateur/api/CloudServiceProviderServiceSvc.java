package fr.ippon.iroco2.domain.calculateur.api;

import fr.ippon.iroco2.domain.calculateur.model.CloudServiceProviderService;

import java.util.List;
import java.util.UUID;

public interface CloudServiceProviderServiceSvc {
    List<CloudServiceProviderService> findAllByCsp(UUID cspId);

    List<CloudServiceProviderService> findAll();

    CloudServiceProviderService findServiceById(UUID serviceId);
}