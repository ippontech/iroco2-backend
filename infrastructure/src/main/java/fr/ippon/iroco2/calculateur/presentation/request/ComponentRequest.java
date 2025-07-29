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
package fr.ippon.iroco2.calculateur.presentation.request;

import fr.ippon.iroco2.domain.calculateur.model.CloudServiceProviderService;
import fr.ippon.iroco2.domain.calculateur.model.Component;
import fr.ippon.iroco2.domain.calculateur.model.ConfiguredSetting;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

public record ComponentRequest(
        UUID id,
        @NotNull UUID infrastructureID,
        @NotBlank String name,
        @NotNull UUID regionID,
        @NotNull UUID serviceID,
        @NotEmpty List<ConfigurationValueRequest> configurationValues
) {

    public Component createToDomain() {
        var values = mapConfiguredSettings();
        var service = new CloudServiceProviderService();
        service.setId(serviceID);
        return Component.create(infrastructureID, name, regionID, service, values);
    }

    public Component updateToDomain() {
        var values = mapConfiguredSettings();
        var service = new CloudServiceProviderService();
        service.setId(serviceID);
        return Component.load(id, infrastructureID, name, null, regionID, service, values);
    }

    private List<ConfiguredSetting> mapConfiguredSettings() {
        return configurationValues.stream()
                .map(value -> new ConfiguredSetting(value.configurationSettingId(), null, value.value()))
                .toList();
    }
}
