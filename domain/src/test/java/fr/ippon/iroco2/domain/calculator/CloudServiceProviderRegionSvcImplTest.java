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

import fr.ippon.iroco2.domain.calculator.model.CloudServiceProviderRegion;
import fr.ippon.iroco2.domain.calculator.spi.CloudServiceProviderRegionStorage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static java.util.UUID.randomUUID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CloudServiceProviderRegionSvcImplTest {
    @Mock
    private CloudServiceProviderRegionStorage storage;
    @InjectMocks
    private CloudServiceProviderRegionSvcImpl svc;

    @Test
    void find_should_call_storage() {
        //given
        UUID id = randomUUID();
        var listRegions = List.of(mock(CloudServiceProviderRegion.class));
        when(storage.findAllByCsp(id)).thenReturn(listRegions);
        //when
        var result = svc.findAllByCsp(id);
        //then
        assertThat(result).isEqualTo(listRegions);
    }
}
