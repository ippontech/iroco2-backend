package fr.ippon.iroco2.calculateur.presentation.reponse;

import java.util.List;
import java.util.UUID;

public record InfrastructureResponse(UUID id, String name, CloudServiceProviderResponse cloudServiceProvider, UUID defaultRegion,
                                     List<ComponentResponse> components) {

}
