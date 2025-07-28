package fr.ippon.iroco2.analyzer.presentation.mapper;

import fr.ippon.iroco2.domain.commons.model.EstimatedPayload;
import fr.ippon.iroco2.domain.analyzer.model.Analysis;
import fr.ippon.iroco2.analyzer.presentation.response.CreatedAnalysisResponse;
import fr.ippon.iroco2.common.presentation.response.EstimatedPayloadResponse;
import fr.ippon.iroco2.analyzer.presentation.response.AnalysisDetailResponse;
import fr.ippon.iroco2.analyzer.presentation.response.AnalysisListElementResponse;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class AnalysisMapper {
    public List<AnalysisListElementResponse> toResponse(List<Analysis> analyses) {
        return analyses.stream().map(this::toResponse).toList();
    }

    protected AnalysisListElementResponse toResponse(Analysis analysis) {
        int co2Gr = analysis.getPayloads().stream().mapToInt(EstimatedPayload::carbonGramFootprint).sum();
        return new AnalysisListElementResponse(analysis.getId(), analysis.getStatus(), analysis.getCreationDate(), co2Gr);
    }

    public CreatedAnalysisResponse toResponse(UUID analysisId, String presignedUrl) {
        return new CreatedAnalysisResponse(analysisId.toString(), presignedUrl);
    }

    public AnalysisDetailResponse toDetailResponse(Analysis analysis) {
        return new AnalysisDetailResponse(analysis.getId(), analysis.getStatus(), analysis.getPayloads().stream().map(this::toResponse).toList());
    }

    private EstimatedPayloadResponse toResponse(EstimatedPayload estimatedPayload) {
        return new EstimatedPayloadResponse(estimatedPayload.carbonGramFootprint(), estimatedPayload.name());
    }
}
