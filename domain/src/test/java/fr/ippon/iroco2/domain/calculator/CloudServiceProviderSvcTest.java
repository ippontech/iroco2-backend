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
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CloudServiceProviderSvcTest {
    @Mock
    private CloudServiceProviderStorage cloudServiceProviderStorage;

    @InjectMocks
    private CloudServiceProviderSvcImpl cloudServiceProviderSvc;

    @Test
    void findById_shouldReturnCloudServiceProvider_whenEntityExists() {
        // Arrange
        UUID validUUID = UUID.randomUUID();
        CloudServiceProvider provider = new CloudServiceProvider();
        Mockito.when(cloudServiceProviderStorage.findById(validUUID)).thenReturn(Optional.of(provider));

        // Act
        var result = cloudServiceProviderSvc.findById(validUUID);

        // Assert
        Assertions.assertThat(result).isNotNull();
        Mockito.verify(cloudServiceProviderStorage, times(1)).findById(validUUID);
    }

    @Test
    void findById_shouldThrowNotFoundException_whenEntityNotFound() {
        // Arrange
        UUID validUUID = UUID.randomUUID();
        when(cloudServiceProviderStorage.findById(validUUID)).thenReturn(Optional.empty());

        // Act
        var error = Assertions.catchThrowable(() -> cloudServiceProviderSvc.findById(validUUID));

        // Assert
        Assertions.assertThat(error).isInstanceOf(NotFoundException.class);
        Mockito.verify(cloudServiceProviderStorage, times(1)).findById(validUUID);
    }
}