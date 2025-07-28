package fr.ippon.iroco2.domain.commons.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class Report {
    protected UUID id;
    protected String owner;
    protected ReportStatus status;
    protected LocalDateTime creationDate;
    protected List<EstimatedPayload> payloads;
}
