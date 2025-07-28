package fr.ippon.iroco2.domain.calculateur;

import fr.ippon.iroco2.domain.calculateur.api.CloudServiceProviderSvc;
import fr.ippon.iroco2.domain.calculateur.model.CloudServiceProvider;
import fr.ippon.iroco2.domain.calculateur.spi.CloudServiceProviderStorage;
import fr.ippon.iroco2.domain.commons.DomainService;
import fr.ippon.iroco2.domain.commons.exception.NotFoundException;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;

@DomainService
@RequiredArgsConstructor
public class CloudServiceProviderSvcImpl implements CloudServiceProviderSvc {

    private final CloudServiceProviderStorage cloudServiceProviderStorage;

    @Override
    public CloudServiceProvider findById(UUID uuid) {
        return cloudServiceProviderStorage.findById(uuid).orElseThrow(() -> new NotFoundException("CSP not found"));
    }

    @Override
    public List<CloudServiceProvider> findAll() {
        return cloudServiceProviderStorage.findAll();
    }
}
