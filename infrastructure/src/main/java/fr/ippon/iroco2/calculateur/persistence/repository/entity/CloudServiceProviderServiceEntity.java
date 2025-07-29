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

import fr.ippon.iroco2.domain.calculateur.model.CloudServiceProviderService;
import fr.ippon.iroco2.domain.calculateur.model.emu.Availability;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor

@Entity
@Table(name = "cloud_service_provider_service")
public class CloudServiceProviderServiceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column
    private UUID id;

    @Column
    private String name;

    @Column
    private String description;

    @Column(name = "availability", nullable = false)
    @Enumerated(EnumType.STRING)
    private Availability availability;

    @Column(name = "short_name")
    private String shortname;

    @Column(name = "levers", columnDefinition = "varchar[]")
    @JdbcTypeCode(SqlTypes.ARRAY)
    private List<String> levers;

    @Column(name = "limitations", columnDefinition = "varchar[]")
    @JdbcTypeCode(SqlTypes.ARRAY)
    private List<String> limitations;

    @JoinColumn(name = "csp")
    @ManyToOne
    private CloudServiceProviderEntity cloudServiceProvider;

    public CloudServiceProviderService toDomain() {
        var service = new CloudServiceProviderService();
        service.setId(this.id);
        service.setName(this.name);
        service.setDescription(this.description);
        service.setAvailability(this.availability);
        service.setShortname(this.shortname);
        service.setLevers(this.levers.stream().toList());
        service.setLimitations(this.limitations.stream().toList());
        return service;
    }
}
