package fr.ippon.iroco2.calculateur.persistence;

import fr.ippon.iroco2.domain.calculateur.model.CloudServiceProvider;
import fr.ippon.iroco2.domain.calculateur.spi.CloudServiceProviderStorage;
import fr.ippon.iroco2.calculateur.persistence.repository.CloudServiceProviderRepository;
import fr.ippon.iroco2.calculateur.persistence.repository.entity.CloudServiceProviderEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class CloudServiceProviderStorageAdapter implements CloudServiceProviderStorage {

    private final CloudServiceProviderRepository cloudServiceProviderRepository;

    @Override
    public List<CloudServiceProvider> findAll() {
        return cloudServiceProviderRepository.findAll().stream().map(CloudServiceProviderEntity::toDomain).toList();
    }

    @Override
    public Optional<CloudServiceProvider> findById(UUID uuid) {
        return cloudServiceProviderRepository.findById(uuid).map(CloudServiceProviderEntity::toDomain);
    }
}
