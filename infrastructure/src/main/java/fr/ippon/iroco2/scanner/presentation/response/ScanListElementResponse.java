package fr.ippon.iroco2.scanner.presentation.response;

import fr.ippon.iroco2.domain.commons.model.ReportStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public record ScanListElementResponse(UUID id, ReportStatus status, LocalDateTime dateCreation, int co2Gr) {
}
