package fr.ippon.iroco2.domain.commons;

import fr.ippon.iroco2.domain.commons.exception.FunctionalException;
import fr.ippon.iroco2.domain.commons.exception.NotFoundException;
import fr.ippon.iroco2.domain.commons.model.Payload;
import fr.ippon.iroco2.domain.commons.model.Report;

import java.util.List;
import java.util.UUID;

public interface ElementSvc<R extends Report, E extends NotFoundException> {
    UUID create();

    List<R> findAll();

    R findById(UUID id) throws E;

    void addEstimation(Payload payload) throws FunctionalException;

    void delete(UUID id);
}
