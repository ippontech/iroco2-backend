package fr.ippon.iroco2.domain.calculateur;

import fr.ippon.iroco2.domain.calculateur.api.CloudServiceProviderServiceSvc;
import fr.ippon.iroco2.domain.calculateur.model.CloudServiceProviderService;
import fr.ippon.iroco2.domain.calculateur.spi.CloudServiceProviderServiceStorage;
import fr.ippon.iroco2.domain.commons.DomainService;
import fr.ippon.iroco2.domain.commons.exception.NotFoundException;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;

@DomainService
@RequiredArgsConstructor
public class CloudServiceProviderServiceSvcImpl implements CloudServiceProviderServiceSvc {
    private final CloudServiceProviderServiceStorage cloudServiceProviderServiceStorage;

    @Override
    public List<CloudServiceProviderService> findAllByCsp(UUID cspId) {
        return cloudServiceProviderServiceStorage.findAllByCsp(cspId);
    }

    @Override
    public List<CloudServiceProviderService> findAll() {
        return cloudServiceProviderServiceStorage.findAll();
    }

    @Override
    public CloudServiceProviderService findServiceById(UUID serviceId) {
        return cloudServiceProviderServiceStorage.findById(serviceId).orElseThrow(() -> new NotFoundException("The service with id %s does not exist".formatted(serviceId)));
    }
}