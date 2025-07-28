package fr.ippon.iroco2.domain.calculateur.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class CloudServiceProvider {
    private UUID id;
    private String name;
    private Set<CloudServiceProviderRegion> cloudServiceProviderRegions = new HashSet<>();
    private List<CloudServiceProviderService> cloudServiceProviderServices = new ArrayList<>();

    public CloudServiceProvider(UUID cloudServiceProviderId) {
        id = cloudServiceProviderId;
    }
}
