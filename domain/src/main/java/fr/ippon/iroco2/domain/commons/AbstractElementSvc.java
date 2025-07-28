package fr.ippon.iroco2.domain.commons;

import fr.ippon.iroco2.domain.commons.exception.FunctionalException;
import fr.ippon.iroco2.domain.commons.exception.NotFoundException;
import fr.ippon.iroco2.domain.commons.model.AReport;
import fr.ippon.iroco2.domain.commons.model.EstimatedPayload;
import fr.ippon.iroco2.domain.commons.model.Payload;
import fr.ippon.iroco2.domain.commons.spi.ReportStorage;
import fr.ippon.iroco2.domain.estimateur.CarbonEstimator;

import java.util.UUID;

public abstract class AbstractElementSvc<R extends AReport<R>, E extends NotFoundException> implements ElementSvc<R, E> {
    @Override
    public final void addEstimation(Payload payload) throws FunctionalException {
        R report = getStorage()
                .findById(payload.reportId())
                .orElseThrow(() -> createException(payload.reportId()));

        String countryIsoCode = payload.countryIsoCode();
        var carbonGramFootprint = getCarbonEstimator().estimatePayload(countryIsoCode, payload);
        var estimatedPayload = EstimatedPayload.create(payload, carbonGramFootprint);

        R updatedReport = report.addPayload(estimatedPayload, payload.expectedPayloads());
        getStorage().save(updatedReport);
    }

    protected abstract E createException(UUID reportId);

    protected abstract ReportStorage<R> getStorage();

    protected abstract CarbonEstimator getCarbonEstimator();
}
