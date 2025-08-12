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

import fr.ippon.iroco2.domain.calculator.model.CloudServiceProvider;
import fr.ippon.iroco2.domain.calculator.secondary.CloudServiceProviderStorage;
import fr.ippon.iroco2.domain.commons.exception.NotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CloudServiceProviderSvcImplTest {
    @Mock
    private CloudServiceProviderStorage storage;
    @InjectMocks
    private CloudServiceProviderSvcImpl svc;

    @Test
    void findall_should_call_storage() {
        //given
        var listCSP = List.of(mock(CloudServiceProvider.class));
        when(storage.findAll()).thenReturn(listCSP);
        //when
        var result = svc.findAll();
        //then
        assertThat(result).isEqualTo(listCSP);
    }

    @Test
    void findById_should_throw_exception_if_not_found() {
        //given
        UUID id = UUID.randomUUID();
        //when
        var exception = catchThrowable(() -> svc.findById(id));
        //then
        assertThat(exception)
                .isInstanceOf(NotFoundException.class)
                .hasMessage("CSP not found");
    }

    @Test
    void findById_should_call_storage() {
        //given
        UUID id = UUID.randomUUID();
        var csp = mock(CloudServiceProvider.class);
        when(storage.findById(id)).thenReturn(Optional.of(csp));
        //when
        var result = svc.findById(id);
        //then
        assertThat(result).isEqualTo(csp);
    }
}
