package fr.ippon.iroco2.domain.calculateur.api;

import fr.ippon.iroco2.domain.calculateur.model.Infrastructure;

import java.util.List;
import java.util.UUID;

public interface InfrastructureSvc {
    void save(Infrastructure infrastructure);

    List<Infrastructure> findAll();

    Infrastructure findById(UUID id);

    void delete(UUID id);
}
