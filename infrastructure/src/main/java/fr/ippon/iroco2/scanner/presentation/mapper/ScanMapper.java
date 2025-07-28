package fr.ippon.iroco2.scanner.presentation.mapper;

import fr.ippon.iroco2.common.presentation.response.EstimatedPayloadResponse;
import fr.ippon.iroco2.domain.commons.model.EstimatedPayload;
import fr.ippon.iroco2.domain.scanner.model.Scan;
import fr.ippon.iroco2.scanner.presentation.response.ScanDetailResponse;
import fr.ippon.iroco2.scanner.presentation.response.ScanListElementResponse;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ScanMapper {
    public List<ScanListElementResponse> toResponse(List<Scan> scans) {
        return scans.stream().map(this::toResponse).toList();
    }

    protected ScanListElementResponse toResponse(Scan scan) {
        int co2Gr = scan.getPayloads().stream().mapToInt(EstimatedPayload::carbonGramFootprint).sum();
        return new ScanListElementResponse(scan.getId(), scan.getStatus(), scan.getCreationDate(), co2Gr);
    }

    public ScanDetailResponse toDetailResponse(Scan scan) {
        return new ScanDetailResponse(scan.getId(), scan.getStatus(), scan.getPayloads().stream().map(this::toResponse).toList());
    }

    private EstimatedPayloadResponse toResponse(EstimatedPayload estimatedPayload) {
        return new EstimatedPayloadResponse(estimatedPayload.carbonGramFootprint(), estimatedPayload.name());
    }
}
