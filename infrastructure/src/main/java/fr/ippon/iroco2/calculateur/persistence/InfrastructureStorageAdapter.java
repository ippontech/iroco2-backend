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
package fr.ippon.iroco2.calculateur.persistence;

import fr.ippon.iroco2.domain.calculateur.model.Infrastructure;
import fr.ippon.iroco2.domain.calculateur.spi.InfrastructureStorage;
import fr.ippon.iroco2.calculateur.persistence.repository.InfrastructureRepository;
import fr.ippon.iroco2.calculateur.persistence.repository.entity.InfrastructureEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class InfrastructureStorageAdapter implements InfrastructureStorage {
    private final InfrastructureRepository infrastructureRepository;

    @Override
    public void save(Infrastructure infrastructure) {
        InfrastructureEntity entity = InfrastructureEntity.fromDomain(infrastructure);
        infrastructureRepository.save(entity);
    }

    @Override
    public List<Infrastructure> findAllByOwner(String email) {
        return infrastructureRepository.findAllByOwner(email).stream().map(InfrastructureEntity::toDomain).toList();
    }

    @Override
    public Optional<Infrastructure> findById(UUID id) {
        return infrastructureRepository.findById(id).map(InfrastructureEntity::toDomain);
    }

    @Override
    public void delete(Infrastructure infrastructure) {
        infrastructureRepository.deleteById(infrastructure.id());
    }
}
