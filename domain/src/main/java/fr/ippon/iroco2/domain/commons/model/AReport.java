package fr.ippon.iroco2.domain.commons.model;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public abstract class AReport<R extends Report> extends Report {
    protected AReport(UUID id, String owner, ReportStatus status, LocalDateTime creationDate, List<EstimatedPayload> payloads) {
        super(id, owner, status, creationDate, payloads);
    }

    public abstract R addPayload(EstimatedPayload estimatedPayload, int expectedPayloads);
}
