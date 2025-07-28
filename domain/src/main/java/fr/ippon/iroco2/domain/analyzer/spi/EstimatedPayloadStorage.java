package fr.ippon.iroco2.domain.analyzer.spi;

import fr.ippon.iroco2.domain.commons.model.EstimatedPayload;

public interface EstimatedPayloadStorage {
    void save(EstimatedPayload estimatedPayload);
}
