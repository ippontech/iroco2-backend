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
package fr.ippon.iroco2.scanner.persistence;

import fr.ippon.iroco2.common.persistance.entity.EstimatedPayloadEntity;
import fr.ippon.iroco2.domain.commons.model.EstimatedPayload;
import fr.ippon.iroco2.domain.scanner.model.Scan;
import fr.ippon.iroco2.domain.scanner.spi.ScanStorage;
import fr.ippon.iroco2.scanner.persistence.repository.ScannerRepository;
import fr.ippon.iroco2.scanner.persistence.repository.entity.ScanEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ScannerStorageAdapter implements ScanStorage {

    private final ScannerRepository scannerRepository;

    @Override
    public UUID save(Scan scan) {
        ScanEntity entity = fromDomain(scan);
        ScanEntity saved = scannerRepository.save(entity);
        return saved.getId();
    }

    @Override
    public List<Scan> findByOwner(String owner) {
        return scannerRepository.findByOwnerOrderByCreationDateAsc(owner).stream().map(ScanEntity::toDomain).toList();
    }

    @Override
    public void delete(UUID id) {
        scannerRepository.deleteById(id);
    }

    @Override
    public Optional<Scan> findById(UUID id) {
        return scannerRepository.findById(id).map(ScanEntity::toDomain);
    }

    private ScanEntity fromDomain(Scan scan) {
        ScanEntity scanEntity = new ScanEntity();
        scanEntity.setId(scan.getId());
        scanEntity.setOwner(scan.getOwner());
        scanEntity.setStatus(scan.getStatus());
        scanEntity.setAwsAccountId(scan.getAwsAccountId());
        scanEntity.setCreationDate(scan.getCreationDate());
        scanEntity.setPayloads(scan.getPayloads().stream().map(this::fromDomain).toList());
        return scanEntity;
    }

    private EstimatedPayloadEntity fromDomain(EstimatedPayload payload) {
        EstimatedPayloadEntity estimatedPayloadEntity = new EstimatedPayloadEntity();
        estimatedPayloadEntity.setId(payload.id());
        estimatedPayloadEntity.setScan(new ScanEntity());
        estimatedPayloadEntity.getScan().setId(payload.reportId());
        estimatedPayloadEntity.setCarbonGramFootprint(payload.carbonGramFootprint());
        estimatedPayloadEntity.setName(payload.name());
        return estimatedPayloadEntity;
    }
}
