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


import fr.ippon.iroco2.domain.calculateur.api.ComponentSvc;
import fr.ippon.iroco2.domain.calculateur.api.InfrastructureSvc;
import fr.ippon.iroco2.domain.calculateur.model.Component;
import fr.ippon.iroco2.domain.calculateur.model.ConfiguredSetting;
import fr.ippon.iroco2.domain.calculateur.model.Infrastructure;
import fr.ippon.iroco2.domain.calculateur.spi.CloudServiceProviderRegionStorage;
import fr.ippon.iroco2.domain.calculateur.spi.CloudServiceProviderServiceStorage;
import fr.ippon.iroco2.domain.calculateur.spi.ComponentStorage;
import fr.ippon.iroco2.domain.calculateur.spi.ConfigurationSettingStorage;
import fr.ippon.iroco2.domain.commons.DomainService;
import fr.ippon.iroco2.domain.commons.exception.NotFoundException;
import fr.ippon.iroco2.domain.commons.svc.DateProvider;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;

@DomainService
@RequiredArgsConstructor
public class ComponentSvcImpl implements ComponentSvc {

    private final InfrastructureSvc infrastructureSvc;
    private final CloudServiceProviderRegionStorage cloudServiceProviderRegionStorage;
    private final CloudServiceProviderServiceStorage cloudServiceProviderServiceStorage;
    private final ConfigurationSettingStorage configurationSettingStorage;
    private final ComponentStorage componentStorage;
    private final DateProvider dateProvider;

    @Override
    public void save(Component component) {
        Infrastructure infrastructure = infrastructureSvc.findById(component.getInfrastructureID());
        if (infrastructure == null)
            throw new NotFoundException("Infrastructure not found (%s)".formatted(component.getInfrastructureID()));

        if (component.getRegionID() == null) component.setRegionID(infrastructure.defaultRegionId());

        cloudServiceProviderRegionStorage.findById(component.getRegionID())
                .orElseThrow(() -> new NotFoundException("Region not found (%s)".formatted(component.getRegionID())));

        var service = cloudServiceProviderServiceStorage.findById(component.getService().getId())
                .orElseThrow(() -> new NotFoundException("Service not found (%s)".formatted(component.getService().getId())));

        component.setService(service);

        for (ConfiguredSetting configuration : component.getConfigurationValues()) {
            configurationSettingStorage.findById(configuration.configurationSettingId())
                    .orElseThrow(() -> new NotFoundException("ConfigurationSetting not found (%s)".formatted(configuration.configurationSettingId())));
        }

        component.setLastModificationDate(dateProvider.now());
        componentStorage.save(component);
    }

    @Override
    public void delete(UUID componentId) {
        Component component = existsControl(componentId);
        componentStorage.delete(component);
    }

    private Component existsControl(UUID componentId) {
        return componentStorage.findById(componentId).orElseThrow(() -> new NotFoundException("The component with ID '%s' is not found".formatted(componentId)));
    }

    public List<Component> findAllByInfrastructureID(UUID infrastructureId) {
        return componentStorage.findByInfrastructureId(infrastructureId);
    }

    @Override
    public void update(Component component) {
        existsControl(component.getId());
        save(component);
    }
}
