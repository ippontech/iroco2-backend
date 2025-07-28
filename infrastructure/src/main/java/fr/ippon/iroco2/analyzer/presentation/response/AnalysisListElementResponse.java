package fr.ippon.iroco2.analyzer.presentation.response;

import fr.ippon.iroco2.domain.commons.model.ReportStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public record AnalysisListElementResponse(UUID id,
                                          ReportStatus status,
                                          LocalDateTime dateCreation,
                                          int co2Gr) {
}
