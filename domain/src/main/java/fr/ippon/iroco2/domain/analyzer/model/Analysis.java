package fr.ippon.iroco2.domain.analyzer.model;

import fr.ippon.iroco2.domain.commons.model.AReport;
import fr.ippon.iroco2.domain.commons.model.EstimatedPayload;
import fr.ippon.iroco2.domain.commons.model.ReportStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class Analysis extends AReport<Analysis> {

    private Analysis(UUID id, String owner, ReportStatus status, LocalDateTime creationDate, List<EstimatedPayload> payloads) {
        super(id, owner, status, creationDate, payloads);
    }

    public static Analysis create(String owner, LocalDateTime creationDate) {
        return new Analysis(UUID.randomUUID(), owner, ReportStatus.CREATED, creationDate, new ArrayList<>());
    }

    public static Analysis load(UUID id, String owner, ReportStatus status, LocalDateTime creationDate, List<EstimatedPayload> payloads) {
        return new Analysis(id, owner, status, creationDate, payloads);
    }

    public Analysis addPayload(EstimatedPayload estimatedPayload, int expectedPayloads) {
        var newPayloads = new ArrayList<>(payloads);
        newPayloads.add(estimatedPayload);

        boolean isLoadingEnded = newPayloads.size() == expectedPayloads;
        return Analysis.load(id, owner, isLoadingEnded ? ReportStatus.SUCCESS : ReportStatus.IN_PROGRESS, creationDate, newPayloads);
    }
}
