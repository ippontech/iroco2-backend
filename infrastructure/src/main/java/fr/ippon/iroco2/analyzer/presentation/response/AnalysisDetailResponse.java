package fr.ippon.iroco2.analyzer.presentation.response;

import fr.ippon.iroco2.common.presentation.response.EstimatedPayloadResponse;
import fr.ippon.iroco2.domain.commons.model.ReportStatus;

import java.util.List;
import java.util.UUID;

public record AnalysisDetailResponse(UUID id, ReportStatus status, List<EstimatedPayloadResponse> payloads) {
}
