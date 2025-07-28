package fr.ippon.iroco2.domain.calculateur.api;

import fr.ippon.iroco2.domain.calculateur.model.CloudServiceProviderRegion;
import fr.ippon.iroco2.domain.calculateur.model.Component;
import fr.ippon.iroco2.domain.commons.exception.FunctionalException;

import java.util.Map;
import java.util.UUID;

public interface EstimationSvc {
    Map<Component, Integer> estimateCarbonFootprintByInfrastructureId(UUID infrastructureId)
            throws FunctionalException;

    Map<CloudServiceProviderRegion, Integer> estimateCarbonFootprintByInfrastructureIdForAllRegions(UUID infrastructureId)
            throws FunctionalException;
}
