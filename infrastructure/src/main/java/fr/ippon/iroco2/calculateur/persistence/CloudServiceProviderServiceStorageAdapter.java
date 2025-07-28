package fr.ippon.iroco2.calculateur.persistence;

import fr.ippon.iroco2.domain.calculateur.model.CloudServiceProviderService;
import fr.ippon.iroco2.domain.calculateur.spi.CloudServiceProviderServiceStorage;
import fr.ippon.iroco2.calculateur.persistence.repository.CloudServiceProviderServiceRepository;
import fr.ippon.iroco2.calculateur.persistence.repository.entity.CloudServiceProviderServiceEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class CloudServiceProviderServiceStorageAdapter implements CloudServiceProviderServiceStorage {
    private final CloudServiceProviderServiceRepository cloudServiceProviderServiceRepository;

    @Override
    public List<CloudServiceProviderService> findAllByCsp(UUID cspId) {
        return cloudServiceProviderServiceRepository.findAllByCsp(cspId).stream().map(CloudServiceProviderServiceEntity::toDomain).toList();
    }

    @Override
    public Optional<CloudServiceProviderService> findById(UUID serviceId) {
        return cloudServiceProviderServiceRepository.findById(serviceId).map(CloudServiceProviderServiceEntity::toDomain);
    }

    @Override
    public List<CloudServiceProviderService> findAll() {
        return cloudServiceProviderServiceRepository.findAll().stream().map(CloudServiceProviderServiceEntity::toDomain).toList();
    }
}
