package fr.ippon.iroco2.calculateur.persistence;

import fr.ippon.iroco2.domain.calculateur.model.CloudServiceProviderRegion;
import fr.ippon.iroco2.domain.calculateur.spi.CloudServiceProviderRegionStorage;
import fr.ippon.iroco2.calculateur.persistence.repository.CloudServiceProviderRegionRepository;
import fr.ippon.iroco2.calculateur.persistence.repository.entity.CloudServiceProviderRegionEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class CloudServiceProviderRegionStorageAdapter implements CloudServiceProviderRegionStorage {
    private final CloudServiceProviderRegionRepository cloudServiceProviderRegionRepository;

    @Override
    public List<CloudServiceProviderRegion> findAllByCsp(UUID cspId) {
        return cloudServiceProviderRegionRepository.findAllByCsp(cspId).stream().map(CloudServiceProviderRegionEntity::toDomain).toList();
    }

    @Override
    public List<CloudServiceProviderRegion> findAll() {
        return cloudServiceProviderRegionRepository.findAll().stream().map(CloudServiceProviderRegionEntity::toDomain).toList();
    }

    @Override
    public Optional<CloudServiceProviderRegion> findById(UUID defaultRegion) {
        return cloudServiceProviderRegionRepository.findById(defaultRegion).map(CloudServiceProviderRegionEntity::toDomain);
    }
}
