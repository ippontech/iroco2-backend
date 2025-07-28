package fr.ippon.iroco2.calculateur.persistence;

import fr.ippon.iroco2.domain.calculateur.model.Infrastructure;
import fr.ippon.iroco2.domain.calculateur.spi.InfrastructureStorage;
import fr.ippon.iroco2.calculateur.persistence.repository.InfrastructureRepository;
import fr.ippon.iroco2.calculateur.persistence.repository.entity.InfrastructureEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class InfrastructureStorageAdapter implements InfrastructureStorage {
    private final InfrastructureRepository infrastructureRepository;

    @Override
    public void save(Infrastructure infrastructure) {
        InfrastructureEntity entity = InfrastructureEntity.fromDomain(infrastructure);
        infrastructureRepository.save(entity);
    }

    @Override
    public List<Infrastructure> findAllByOwner(String email) {
        return infrastructureRepository.findAllByOwner(email).stream().map(InfrastructureEntity::toDomain).toList();
    }

    @Override
    public Optional<Infrastructure> findById(UUID id) {
        return infrastructureRepository.findById(id).map(InfrastructureEntity::toDomain);
    }

    @Override
    public void delete(Infrastructure infrastructure) {
        infrastructureRepository.deleteById(infrastructure.id());
    }
}
