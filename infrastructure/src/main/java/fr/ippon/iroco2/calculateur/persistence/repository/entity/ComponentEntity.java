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
package fr.ippon.iroco2.calculateur.persistence.repository.entity;

import fr.ippon.iroco2.domain.calculateur.model.Component;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor

@Entity
@Table(name = "component")
public class ComponentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column
    private UUID id;

    @Column
    private String name;

    @Column(name = "last_modification_date")
    private LocalDateTime lastModificationDate;

    @JoinColumn(name = "csp_region")
    @ManyToOne
    private CloudServiceProviderRegionEntity cspRegion;

    @JoinColumn(name = "service_id")
    @ManyToOne
    private CloudServiceProviderServiceEntity service;

    @OneToMany(mappedBy = "component", cascade = {CascadeType.MERGE, CascadeType.REMOVE, CascadeType.PERSIST}, orphanRemoval = true)
    private List<ConfiguredSettingEntity> configuredSettings = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "infrastructure_id", nullable = false)
    private InfrastructureEntity infrastructure;

    public Component toDomain() {
        var values = configuredSettings.stream().map(ConfiguredSettingEntity::toDomain).toList();
        return Component.load(id, infrastructure.getId(), name, lastModificationDate, cspRegion.getId(), service.toDomain(), values);
    }
}
