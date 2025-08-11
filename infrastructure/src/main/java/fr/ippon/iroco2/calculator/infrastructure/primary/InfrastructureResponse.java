package fr.ippon.iroco2.calculator.infrastructure.primary;

import fr.ippon.iroco2.calculator.infrastructure.component.primary.ComponentResponse;
import fr.ippon.iroco2.catalog.primary.provider.CloudServiceProviderResponse;

import java.util.List;
import java.util.UUID;

public record InfrastructureResponse(UUID id, String name, CloudServiceProviderResponse cloudServiceProvider,
                                     UUID defaultRegion,
                                     List<ComponentResponse> components) {

}
