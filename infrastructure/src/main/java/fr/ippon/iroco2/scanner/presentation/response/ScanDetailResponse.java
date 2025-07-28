package fr.ippon.iroco2.scanner.presentation.response;

import fr.ippon.iroco2.common.presentation.response.EstimatedPayloadResponse;
import fr.ippon.iroco2.domain.commons.model.ReportStatus;

import java.util.List;
import java.util.UUID;

public record ScanDetailResponse(UUID id, ReportStatus status, List<EstimatedPayloadResponse> payloads) {
}
