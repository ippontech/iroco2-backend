package fr.ippon.iroco2.domain.calculateur.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public record Infrastructure(
        UUID id,
        String name,
        CloudServiceProvider cloudServiceProvider,
        String owner,
        UUID defaultRegionId,
        List<Component> components) {

    public static Infrastructure create(String name, UUID cloudServiceProviderId, UUID defaultRegionId, String owner) {
        final CloudServiceProvider cloudServiceProvider = new CloudServiceProvider(cloudServiceProviderId);
        return new Infrastructure(UUID.randomUUID(), name, cloudServiceProvider, owner, defaultRegionId, new ArrayList<>());
    }
}
