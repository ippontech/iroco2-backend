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
package fr.ippon.iroco2.domain.calculator;

import fr.ippon.iroco2.domain.calculator.spi.ServiceInstanceStorage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ServiceInstanceSvcImplTest {
    @Mock
    private ServiceInstanceStorage storage;
    @InjectMocks
    private ServiceInstanceSvcImpl service;

    @Test
    void should_get_empty_list_if_not_found() {
        var result = service.getNamesOfCompatibleInstancesForService(null);
        assertThat(result).isEmpty();
    }

    @Test
    void should_return_service_instance_for_given_service() {
        var serviceName = "a service";
        var ec2Name = "ec2 name";
        when(storage.findAllNamesByServiceShortName(serviceName)).thenReturn(List.of(ec2Name));

        var result = service.getNamesOfCompatibleInstancesForService(serviceName);

        assertThat(result).hasSize(1);
        assertThat(result.getFirst()).isEqualTo(ec2Name);
    }
}