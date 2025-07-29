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

import fr.ippon.iroco2.domain.calculateur.model.Infrastructure;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
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

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor

@Entity
@Table(name = "infrastructure")
public class InfrastructureEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column
    private UUID id;

    @Column
    private String name;

    @JoinColumn(name = "cloud_service_provider")
    @ManyToOne
    private CloudServiceProviderEntity cloudServiceProvider;

    @Column
    private String owner;

    @JoinColumn(name = "default_region")
    @ManyToOne
    private CloudServiceProviderRegionEntity defaultRegion;

    @OneToMany(mappedBy = "infrastructure", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<ComponentEntity> components = new ArrayList<>();

    public static InfrastructureEntity fromDomain(Infrastructure infrastructure) {
        var entity = new InfrastructureEntity();
        entity.setId(infrastructure.id());
        entity.setName(infrastructure.name());
        entity.setCloudServiceProvider(new CloudServiceProviderEntity());
        entity.getCloudServiceProvider().setId(infrastructure.cloudServiceProvider().getId());

        entity.setDefaultRegion(new CloudServiceProviderRegionEntity());
        entity.getDefaultRegion().setId(infrastructure.defaultRegionId());

        entity.setOwner(infrastructure.owner());
        entity.getComponents().clear();
        return entity;
    }

    public Infrastructure toDomain() {
        return new Infrastructure(id, name, cloudServiceProvider.toDomain(), owner, defaultRegion.getId(), components.stream().map(ComponentEntity::toDomain).toList());
    }
}
