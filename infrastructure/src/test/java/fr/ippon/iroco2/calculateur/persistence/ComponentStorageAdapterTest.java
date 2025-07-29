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
import fr.ippon.iroco2.calculateur.persistence.repository.entity.CloudServiceProviderRegionEntity;
import fr.ippon.iroco2.calculateur.persistence.repository.entity.CloudServiceProviderServiceEntity;
import fr.ippon.iroco2.calculateur.persistence.repository.entity.ComponentEntity;
import fr.ippon.iroco2.calculateur.persistence.repository.entity.ConfigurationSettingEntity;
import fr.ippon.iroco2.calculateur.persistence.repository.entity.ConfiguredSettingEntity;
import fr.ippon.iroco2.calculateur.persistence.repository.entity.InfrastructureEntity;
import fr.ippon.iroco2.domain.calculateur.model.CloudServiceProviderService;
import fr.ippon.iroco2.domain.calculateur.model.Component;
import fr.ippon.iroco2.domain.calculateur.model.ConfiguredSetting;
import fr.ippon.iroco2.domain.calculateur.model.emu.SettingName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ComponentStorageAdapterTest {
    @Mock
    private ComponentRepository componentRepository;
    @Mock
    private CloudServiceProviderRegionRepository cloudServiceProviderRegionRepository;
    @Mock
    private CloudServiceProviderServiceRepository cloudServiceProviderServiceRepository;
    @Mock
    private InfrastructureRepository infrastructureRepository;
    @InjectMocks
    private ComponentStorageAdapter adapter;
    @Captor
    private ArgumentCaptor<ComponentEntity> componentEntityArgumentCaptor;

    @Test
    void save_should_save_converted_component() {
        //given
        UUID id = UUID.randomUUID();
        UUID infrastructureID = UUID.randomUUID();
        String name = "name of component";
        LocalDateTime lastModificationDate = LocalDateTime.now();
        UUID regionID = UUID.randomUUID();
        CloudServiceProviderService service = new CloudServiceProviderService();
        UUID serviceId = UUID.randomUUID();
        service.setId(serviceId);
        UUID configurationSettingId = UUID.randomUUID();
        SettingName configurationSettingName = SettingName.PROCESSOR_ARCHITECTURE;
        String value = "value of cs";
        ConfiguredSetting configuredSetting = new ConfiguredSetting(configurationSettingId, configurationSettingName, value);
        List<ConfiguredSetting> values = List.of(configuredSetting);
        Component component = Component.load(id, infrastructureID, name, lastModificationDate, regionID, service, values);

        CloudServiceProviderRegionEntity cspre = mock(CloudServiceProviderRegionEntity.class);
        when(cloudServiceProviderRegionRepository.findById(regionID)).thenReturn(Optional.of(cspre));
        CloudServiceProviderServiceEntity cspse = mock(CloudServiceProviderServiceEntity.class);
        when(cloudServiceProviderServiceRepository.findById(serviceId)).thenReturn(Optional.of(cspse));
        InfrastructureEntity ie = mock(InfrastructureEntity.class);
        when(infrastructureRepository.findById(infrastructureID)).thenReturn(Optional.of(ie));

        //when
        adapter.save(component);

        //then
        verify(componentRepository).save(componentEntityArgumentCaptor.capture());
        ComponentEntity entity = componentEntityArgumentCaptor.getValue();
        assertThat(entity).isNotNull();
        assertThat(entity.getId()).isEqualTo(id);
        assertThat(entity.getName()).isEqualTo(name);
        assertThat(entity.getLastModificationDate()).isEqualTo(lastModificationDate);
        assertThat(entity.getCspRegion()).isEqualTo(cspre);
        assertThat(entity.getService()).isEqualTo(cspse);
        assertThat(entity.getInfrastructure()).isEqualTo(ie);
        assertThat(entity.getConfiguredSettings()).hasSize(1);
        ConfiguredSettingEntity cse = entity.getConfiguredSettings().getFirst();
        assertThat(cse).isNotNull();
        assertThat(cse.getId()).isNotNull();
        assertThat(cse.getComponent()).isEqualTo(entity);
        assertThat(cse.getValue()).isEqualTo(value);
        ConfigurationSettingEntity configurationSetting = cse.getConfigurationSetting();
        assertThat(configurationSetting).isNotNull();
        assertThat(configurationSetting.getId()).isEqualTo(configurationSettingId);
    }

    @Test
    void save_should_save_null_region_service_infra_if_not_found() {
        //given
        CloudServiceProviderService service = new CloudServiceProviderService();
        Component component = Component.load(null, null, null, null, null, service, List.of());

        //when
        adapter.save(component);

        //then
        verify(componentRepository).save(componentEntityArgumentCaptor.capture());
        ComponentEntity entity = componentEntityArgumentCaptor.getValue();
        assertThat(entity.getCspRegion()).isNull();
        assertThat(entity.getService()).isNull();
        assertThat(entity.getInfrastructure()).isNull();
    }
}
