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
package fr.ippon.iroco2.calculateur.persistence;

import fr.ippon.iroco2.calculateur.persistence.repository.CloudServiceProviderRegionRepository;
import fr.ippon.iroco2.calculateur.persistence.repository.CloudServiceProviderServiceRepository;
import fr.ippon.iroco2.calculateur.persistence.repository.ComponentRepository;
import fr.ippon.iroco2.calculateur.persistence.repository.InfrastructureRepository;
import fr.ippon.iroco2.calculateur.persistence.repository.entity.ComponentEntity;
import fr.ippon.iroco2.calculateur.persistence.repository.entity.ConfigurationSettingEntity;
import fr.ippon.iroco2.calculateur.persistence.repository.entity.ConfiguredSettingEntity;
import fr.ippon.iroco2.domain.calculateur.model.Component;
import fr.ippon.iroco2.domain.calculateur.model.ConfiguredSetting;
import fr.ippon.iroco2.domain.calculateur.spi.ComponentStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class ComponentStorageAdapter implements ComponentStorage {

    private final ComponentRepository componentRepository;
    private final CloudServiceProviderRegionRepository cloudServiceProviderRegionRepository;
    private final CloudServiceProviderServiceRepository cloudServiceProviderServiceRepository;
    private final InfrastructureRepository infrastructureRepository;

    private static ConfiguredSettingEntity fromDomain(ConfiguredSetting configuredSetting) {
        var setting = new ConfiguredSettingEntity();
        setting.setId(UUID.randomUUID());
        setting.setConfigurationSetting(fromDomain(configuredSetting.configurationSettingId()));
        setting.setValue(configuredSetting.value());
        return setting;
    }

    private static ConfigurationSettingEntity fromDomain(UUID settingId) {
        ConfigurationSettingEntity configurationSetting = new ConfigurationSettingEntity();
        configurationSetting.setId(settingId);
        return configurationSetting;
    }

    @Override
    public void save(Component component) {
        ComponentEntity entity = fromDomain(component);
        componentRepository.save(entity);
    }

    @Override
    public Optional<Component> findById(UUID componentId) {
        return componentRepository.findById(componentId).map(ComponentEntity::toDomain);
    }

    @Override
    public void delete(Component component) {
        componentRepository.deleteById(component.getId());
    }

    @Override
    public List<Component> findByInfrastructureId(UUID infrastructureId) {
        return componentRepository.findByInfrastructureId(infrastructureId).stream().map(ComponentEntity::toDomain).toList();
    }

    public ComponentEntity fromDomain(Component component) {
        var entity = new ComponentEntity();
        entity.setId(component.getId());
        entity.setName(component.getName());
        entity.setLastModificationDate(component.getLastModificationDate());

        entity.setCspRegion(cloudServiceProviderRegionRepository.findById(component.getRegionID()).orElse(null));

        entity.setService(cloudServiceProviderServiceRepository.findById(component.getService().getId()).orElse(null));

        entity.setInfrastructure(infrastructureRepository.findById(component.getInfrastructureID()).orElse(null));

        List<ConfiguredSettingEntity> list = new ArrayList<>();
        for (ConfiguredSetting configuredSetting : component.getConfigurationValues()) {
            var setting = fromDomain(configuredSetting);
            setting.setComponent(entity);
            list.add(setting);
        }
        entity.setConfiguredSettings(list);

        return entity;
    }
}
