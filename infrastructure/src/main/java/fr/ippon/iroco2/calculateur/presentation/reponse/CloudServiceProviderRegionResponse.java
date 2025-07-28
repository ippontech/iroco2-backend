package fr.ippon.iroco2.calculateur.presentation.reponse;

import java.util.UUID;

public record CloudServiceProviderRegionResponse(UUID id, UUID csp, String name, String area, String shortname) {
}
