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

import fr.ippon.iroco2.domain.calculateur.model.ConfigurationSetting;
import fr.ippon.iroco2.domain.calculateur.spi.ConfigurationSettingStorage;
import fr.ippon.iroco2.calculateur.persistence.repository.ConfigurationSettingRepository;
import fr.ippon.iroco2.calculateur.persistence.repository.entity.ConfigurationSettingEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class ConfigurationSettingStorageAdapter implements ConfigurationSettingStorage {
    private final ConfigurationSettingRepository configurationSettingRepository;

    @Override
    public Optional<ConfigurationSetting> findById(UUID uuid) {
        return configurationSettingRepository.findById(uuid).map(ConfigurationSettingEntity::toDomain);
    }
}
