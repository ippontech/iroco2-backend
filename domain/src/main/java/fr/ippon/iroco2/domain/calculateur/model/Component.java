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
package fr.ippon.iroco2.domain.calculateur.model;

import fr.ippon.iroco2.domain.calculateur.model.emu.SettingName;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static fr.ippon.iroco2.domain.calculateur.model.emu.SettingName.MEMORY_IN_MEGA_BYTE;
import static java.util.Optional.ofNullable;

@Getter
@Setter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Component {
    public static final String LAMBDA_SERVICE_SHORT_NAME = "Lambda";

    private UUID id;
    private UUID infrastructureID;
    private String name;
    private LocalDateTime lastModificationDate;
    private UUID regionID;
    private CloudServiceProviderService service;
    private List<ConfiguredSetting> configurationValues;

    public static Component create(UUID infrastructureID, String name, UUID regionID, CloudServiceProviderService service, List<ConfiguredSetting> values) {
        return new Component(UUID.randomUUID(), infrastructureID, name, LocalDateTime.now(), regionID, service, values);
    }

    public static Component load(UUID id, UUID infrastructureID, String name, LocalDateTime lastModificationDate, UUID regionID, CloudServiceProviderService service, List<ConfiguredSetting> values) {
        return new Component(id, infrastructureID, name, lastModificationDate, regionID, service, values);
    }

    public double getMemoryInMegaByte() {
        return ofNullable(getValue(MEMORY_IN_MEGA_BYTE))
                .map(Double::parseDouble)
                .orElse(0d);
    }

    public boolean isLambda() {
        return LAMBDA_SERVICE_SHORT_NAME.equals(service.getShortname());
    }

    public String getValue(SettingName settingName) {
        return configurationValues == null ? null :
                configurationValues.stream()
                        .filter(cs -> settingName == cs.configurationSettingName())
                        .findFirst()
                        .map(ConfiguredSetting::value)
                        .orElse(null);
    }
}
