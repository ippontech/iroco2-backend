package fr.ippon.iroco2.domain.scanner.model;

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
public class Scan extends AReport<Scan> {

    private final String awsAccountId;

    private Scan(UUID id, String owner, String awsAccountId, ReportStatus status, LocalDateTime creationDate, List<EstimatedPayload> payloads) {
        super(id, owner, status, creationDate, payloads);
        this.awsAccountId = awsAccountId;
    }

    public static Scan create(String owner, LocalDateTime creationDate, String awsAccountId) {
        return new Scan(UUID.randomUUID(), owner, awsAccountId, ReportStatus.CREATED, creationDate, new ArrayList<>());
    }

    public static Scan load(UUID id, String owner, ReportStatus status, LocalDateTime creationDate, List<EstimatedPayload> domainPayloads, String awsAccountId) {
        return new Scan(id, owner, awsAccountId, status, creationDate, domainPayloads);
    }

    public Scan addPayload(EstimatedPayload estimatedPayload, int expectedPayloads) {
        var newPayloads = new ArrayList<>(payloads);
        newPayloads.add(estimatedPayload);

        boolean isLoadingEnded = newPayloads.size() == expectedPayloads;
        return Scan.load(id, owner, isLoadingEnded ? ReportStatus.SUCCESS : ReportStatus.IN_PROGRESS, creationDate, newPayloads, awsAccountId);
    }
}
