package fr.ippon.iroco2.domain.calculateur.model;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class CloudServiceProviderRegion {
    private UUID id;
    private String name;
    private String area;
    private String shortname;
    private UUID cloudServiceProviderId;
}
