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
package fr.ippon.iroco2.infrastructure;

import fr.ippon.iroco2.estimateur.persistence.repository.EC2InstanceRepository;
import fr.ippon.iroco2.estimateur.persistence.repository.entity.EC2InstanceEntity;
import fr.ippon.iroco2.instance_type.domain.EC2InstanceTypeService;
import fr.ippon.iroco2.instance_type.domain.model.EC2InstanceType;
import fr.ippon.iroco2.instance_type.infrastructure.persistence.ServiceInstanceRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.Optional;


@ExtendWith(SpringExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class EC2InstanceTypeServiceTest {

    @Mock
    private EC2InstanceRepository EC2InstanceRepository;

    @Mock
    private ServiceInstanceRepository serviceInstanceRepository;

    private EC2InstanceTypeService EC2InstanceTypeService;

    @BeforeAll
    public void init() {
        EC2InstanceTypeService = new EC2InstanceTypeService(EC2InstanceRepository, serviceInstanceRepository);

        EC2InstanceEntity EC2InstanceEntity = new EC2InstanceEntity();
        EC2InstanceEntity.setVCPUs(2);
        EC2InstanceEntity.setMemory(BigDecimal.valueOf(0.5));
        EC2InstanceEntity.setCpuType("XEON");
        Mockito.when(EC2InstanceRepository.findByName("t4g.nano")).thenReturn(Optional.of(EC2InstanceEntity));
    }

    @Test
    void findInstanceTypeTest() {
        EC2InstanceType EC2InstanceTypeExpected = new EC2InstanceType("t4g.nano", 2, BigDecimal.valueOf(0.5), "XEON", null, null);
        EC2InstanceType EC2InstanceTypeResult = EC2InstanceTypeService.findEC2InstanceType(EC2InstanceTypeExpected.name());

        Assertions.assertNotNull(EC2InstanceTypeResult);
        Assertions.assertEquals(EC2InstanceTypeExpected.vCPUs(), EC2InstanceTypeResult.vCPUs());
        Assertions.assertEquals(EC2InstanceTypeExpected.memory(), EC2InstanceTypeResult.memory());
        Assertions.assertEquals(EC2InstanceTypeExpected.cpuType(), EC2InstanceTypeResult.cpuType());
    }
}