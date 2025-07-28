package fr.ippon.iroco2.domain.calculateur;

import fr.ippon.iroco2.domain.calculateur.api.CloudServiceProviderRegionSvc;
import fr.ippon.iroco2.domain.calculateur.model.CloudServiceProviderRegion;
import fr.ippon.iroco2.domain.calculateur.spi.CloudServiceProviderRegionStorage;
import fr.ippon.iroco2.domain.commons.DomainService;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;

@DomainService
@RequiredArgsConstructor
public class CloudServiceProviderRegionSvcImpl implements CloudServiceProviderRegionSvc {

    private final CloudServiceProviderRegionStorage cloudServiceProviderRegionStorage;

    @Override
    public List<CloudServiceProviderRegion> findAllByCsp(UUID cspId) {
        return cloudServiceProviderRegionStorage.findAllByCsp(cspId);
    }
}
