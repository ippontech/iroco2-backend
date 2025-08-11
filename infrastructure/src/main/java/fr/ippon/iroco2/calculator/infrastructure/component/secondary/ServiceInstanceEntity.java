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
package fr.ippon.iroco2.calculator.infrastructure.component.secondary;

import fr.ippon.iroco2.common.secondary.EC2InstanceEntity;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Accessors(fluent = true)
@Setter
@Getter
@Entity
@Table(name = "COMPATIBLE_INSTANCES_FOR_SERVICE")
public class ServiceInstanceEntity {
    @OneToOne
    @JoinColumn(name = "INSTANCE_TYPE_NAME", referencedColumnName = "name")
    EC2InstanceEntity instanceModel;

    @EmbeddedId
    private CompatibleInstancesCompositeID id;
}
