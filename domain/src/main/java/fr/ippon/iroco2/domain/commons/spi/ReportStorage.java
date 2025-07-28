package fr.ippon.iroco2.domain.commons.spi;

import fr.ippon.iroco2.domain.commons.model.AReport;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReportStorage<R extends AReport<R>> {
    void delete(UUID id);

    List<R> findByOwner(String owner);

    Optional<R> findById(UUID id);

    UUID save(R report);
}
