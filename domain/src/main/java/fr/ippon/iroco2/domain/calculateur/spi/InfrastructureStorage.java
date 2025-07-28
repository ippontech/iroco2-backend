package fr.ippon.iroco2.domain.calculateur.spi;

import fr.ippon.iroco2.domain.calculateur.model.Infrastructure;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface InfrastructureStorage {
    void save(Infrastructure infrastructure);

    List<Infrastructure> findAllByOwner(String email);

    Optional<Infrastructure> findById(UUID id);

    void delete(Infrastructure infrastructure);
}
