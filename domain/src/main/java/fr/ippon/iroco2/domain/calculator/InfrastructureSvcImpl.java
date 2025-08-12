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
import fr.ippon.iroco2.domain.calculator.primary.InfrastructureSvc;
import fr.ippon.iroco2.domain.calculator.secondary.InfrastructureStorage;
import fr.ippon.iroco2.domain.commons.DomainService;
import fr.ippon.iroco2.domain.commons.exception.UnauthorizedActionException;
import fr.ippon.iroco2.domain.commons.secondary.SessionProvider;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;

@DomainService
@RequiredArgsConstructor
public class InfrastructureSvcImpl implements InfrastructureSvc {

    private final InfrastructureStorage infrastructureStorage;
    private final SessionProvider sessionProvider;

    @Override
    public void save(Infrastructure infrastructure) {
        infrastructureStorage.save(infrastructure);
    }

    @Override
    public List<Infrastructure> findAll() {
        final String email = sessionProvider.getConnectedUserEmail();
        return infrastructureStorage.findAllByOwner(email);
    }

    @Override
    public Infrastructure findById(UUID id) {
        final String email = sessionProvider.getConnectedUserEmail();
        Infrastructure infrastructure = infrastructureStorage
                .findById(id)
                .orElseThrow(() -> new InfrastructureNotFound(id));

        if (!infrastructure.owner().equals(email)) throw new UnauthorizedActionException(
                "You don't have the right to access this infrastructure"
        );

        return infrastructure;
    }

    @Override

    public void delete(UUID id) {
        infrastructureStorage.delete(this.findById(id));
    }
}
