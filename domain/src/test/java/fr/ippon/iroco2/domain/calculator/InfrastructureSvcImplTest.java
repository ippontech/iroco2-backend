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

import fr.ippon.iroco2.domain.calculator.exception.InfrastructureNotFound;
import fr.ippon.iroco2.domain.calculator.model.Infrastructure;
import fr.ippon.iroco2.domain.calculator.secondary.InfrastructureStorage;
import fr.ippon.iroco2.domain.commons.exception.UnauthorizedActionException;
import fr.ippon.iroco2.domain.commons.secondary.SessionProvider;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static java.util.UUID.randomUUID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class InfrastructureSvcImplTest {
    @Mock
    private SessionProvider sessionProvider;
    @Mock
    private InfrastructureStorage infrastructureStorage;
    @InjectMocks
    @Spy
    private InfrastructureSvcImpl infrastructureSvc;

    @Test
    void findById_should_throw_exception_if_infra_not_found() {
        // GIVEN
        UUID infraId = randomUUID();

        // WHEN
        var exception = catchThrowable(() -> infrastructureSvc.findById(infraId));

        // THEN
        assertThat(exception)
                .isInstanceOf(InfrastructureNotFound.class)
                .hasMessage("L'infrastructure d'id '%s' n'existe pas".formatted(infraId));
    }

    @Test
    void findById_should_throw_exception_if_not_owner() {
        // GIVEN
        UUID infraId = randomUUID();
        Infrastructure infra = mock(Infrastructure.class);
        when(infrastructureStorage.findById(infraId)).thenReturn(Optional.of(infra));
        when(infra.owner()).thenReturn("owner");
        when(sessionProvider.getConnectedUserEmail()).thenReturn("not owner");

        // WHEN
        var exception = catchThrowable(() -> infrastructureSvc.findById(infraId));

        // THEN
        assertThat(exception)
                .isInstanceOf(UnauthorizedActionException.class)
                .hasMessage("You don't have the right to access this infrastructure");
    }

    @Test
    void findById_should_return_found_infra() {
        // GIVEN
        UUID infraId = randomUUID();
        Infrastructure infra = mock(Infrastructure.class);
        when(infrastructureStorage.findById(infraId)).thenReturn(Optional.of(infra));
        String owner = "owner";
        when(infra.owner()).thenReturn(owner);
        when(sessionProvider.getConnectedUserEmail()).thenReturn(owner);

        // WHEN
        var result = infrastructureSvc.findById(infraId);

        // THEN
        assertThat(result).isEqualTo(infra);
    }

    @Test
    void findAll_should_call_find_all_for_connected_user() {
        // GIVEN
        String connectedUser = "connected user";
        when(sessionProvider.getConnectedUserEmail()).thenReturn(connectedUser);

        // WHEN
        infrastructureSvc.findAll();

        // THEN
        verify(infrastructureStorage).findAllByOwner(connectedUser);
    }

    @Test
    void save_should_call_save_storage() {
        // GIVEN
        Infrastructure infra = mock(Infrastructure.class);

        // WHEN
        infrastructureSvc.save(infra);

        // THEN
        verify(infrastructureStorage).save(infra);
    }

    @Test
    void delete_should_call_delete_storage() {
        // GIVEN
        UUID infraId = randomUUID();
        Infrastructure infra = mock(Infrastructure.class);
        doReturn(infra).when(infrastructureSvc).findById(infraId);

        // WHEN
        infrastructureSvc.delete(infraId);

        // THEN
        verify(infrastructureStorage).delete(infra);
    }
}
