package fr.ippon.iroco2.domain.analyzer.api;

import fr.ippon.iroco2.domain.analyzer.exception.AnalysisNotFoundException;
import fr.ippon.iroco2.domain.analyzer.model.Analysis;
import fr.ippon.iroco2.domain.commons.ElementSvc;

public interface AnalyzerSvc extends ElementSvc<Analysis, AnalysisNotFoundException> {
}
