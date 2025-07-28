package fr.ippon.iroco2.domain.analyzer.exception;

import fr.ippon.iroco2.domain.commons.exception.NotFoundException;

import java.util.UUID;

public class AnalysisNotFoundException extends NotFoundException {
    public AnalysisNotFoundException(UUID analysisId) {
        super("Analyse avec l'identifiant %s est introuvable".formatted(analysisId));
    }
}
