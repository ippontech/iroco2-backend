/*
 * Copyright 2025 Ippon Technologies
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package fr.ippon.iroco2.domain.calculateur;

import fr.ippon.iroco2.domain.calculateur.api.EstimationSvc;
import fr.ippon.iroco2.domain.calculateur.api.InfrastructureSvc;
import fr.ippon.iroco2.domain.calculateur.model.CloudServiceProviderRegion;
import fr.ippon.iroco2.domain.calculateur.model.Component;
import fr.ippon.iroco2.domain.calculateur.model.Infrastructure;
import fr.ippon.iroco2.domain.calculateur.model.emu.AWSDataCenter;
import fr.ippon.iroco2.domain.calculateur.spi.CloudServiceProviderRegionStorage;
import fr.ippon.iroco2.domain.commons.DomainService;
import fr.ippon.iroco2.domain.commons.exception.FunctionalException;
import fr.ippon.iroco2.domain.commons.exception.NotFoundException;
import fr.ippon.iroco2.domain.estimateur.CarbonEstimator;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@DomainService
@RequiredArgsConstructor
public class EstimationSvcImpl implements EstimationSvc {
    private final CloudServiceProviderRegionStorage cloudServiceProviderRegionStorage;
    private final InfrastructureSvc infrastructureSvc;
    private final CarbonEstimator carbonEstimator;

    private String mapToIsoCode(UUID regionID) {
        CloudServiceProviderRegion region = cloudServiceProviderRegionStorage.findById(regionID).orElseThrow(() -> new NotFoundException("Region not found"));
        return AWSDataCenter.findByName(region.getName()).getAssociatedCountryIsoCode();
    }

    private int estimateCarbonGramFootprint(Component component, UUID regionID) throws FunctionalException {
        String countryIsoCode = mapToIsoCode(regionID);
        return carbonEstimator.estimateComponent(countryIsoCode, component);
    }

    @Override
    public Map<Component, Integer> estimateCarbonFootprintByInfrastructureId(UUID infrastructureId) throws FunctionalException {
        Infrastructure infrastructure = infrastructureSvc.findById(infrastructureId);
        Map<Component, Integer> list = new HashMap<>();
        for (Component component : infrastructure.components()) {
            var estimation = estimateCarbonGramFootprint(component, component.getRegionID());
            list.put(component, estimation);
        }
        return list;
    }

    @Override
    public Map<CloudServiceProviderRegion, Integer> estimateCarbonFootprintByInfrastructureIdForAllRegions(UUID infrastructureId) throws FunctionalException {
        List<CloudServiceProviderRegion> allRegions = cloudServiceProviderRegionStorage.findAll();
        Infrastructure infrastructure = infrastructureSvc.findById(infrastructureId);

        Map<CloudServiceProviderRegion, Integer> map = new HashMap<>();
        for (CloudServiceProviderRegion region : allRegions) {
            var regionEstimation = 0;
            for (Component c : infrastructure.components()) {
                regionEstimation += estimateCarbonGramFootprint(c, region.getId());
            }
            map.put(region, regionEstimation);
        }

        return map;
    }
}
