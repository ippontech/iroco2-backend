package fr.ippon.iroco2.calculateur.presentation.reponse;

import fr.ippon.iroco2.domain.calculateur.model.CloudServiceProviderRegion;

import java.util.UUID;

public record RegionCarbonFootprintResponse(UUID regionID, String regionName, int co2Gr) {

    public static RegionCarbonFootprintResponse createFrom(CloudServiceProviderRegion region, Integer co2Gr) {
        return new RegionCarbonFootprintResponse(region.getId(), region.getName(), co2Gr);
    }
}
