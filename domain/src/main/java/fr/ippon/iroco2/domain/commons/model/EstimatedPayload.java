package fr.ippon.iroco2.domain.commons.model;

import java.util.UUID;

public record EstimatedPayload(
        UUID id,
        UUID reportId,
        int carbonGramFootprint,
        String name) {
    public static EstimatedPayload create(Payload payload, int carbonGramFootprint) {
        return new EstimatedPayload(UUID.randomUUID(), payload.reportId(), carbonGramFootprint, payload.name());
    }
}
