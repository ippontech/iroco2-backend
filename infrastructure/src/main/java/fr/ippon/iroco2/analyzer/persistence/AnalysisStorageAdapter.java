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
package fr.ippon.iroco2.analyzer.persistence;

import fr.ippon.iroco2.analyzer.persistence.repository.AnalysisRepository;
import fr.ippon.iroco2.analyzer.persistence.repository.entity.AnalysisEntity;
import fr.ippon.iroco2.common.persistance.entity.EstimatedPayloadEntity;
import fr.ippon.iroco2.domain.analyzer.model.Analysis;
import fr.ippon.iroco2.domain.analyzer.spi.AnalysisStorage;
import fr.ippon.iroco2.domain.commons.model.EstimatedPayload;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class AnalysisStorageAdapter implements AnalysisStorage {

    private final AnalysisRepository analysisRepository;

    @Override
    public UUID save(Analysis analysis) {
        AnalysisEntity entity = fromDomain(analysis);
        AnalysisEntity saved = analysisRepository.save(entity);
        return saved.getId();
    }

    @Override
    public List<Analysis> findByOwner(String owner) {
        return analysisRepository
                .findByOwnerOrderByCreationDateAsc(owner)
                .stream()
                .map(AnalysisEntity::toDomain)
                .toList();
    }

    @Override
    public Optional<Analysis> findById(UUID id) {
        return analysisRepository.findById(id).map(AnalysisEntity::toDomain);
    }

    @Override
    public void delete(UUID analysisId) {
        analysisRepository.deleteById(analysisId);
    }

    private AnalysisEntity fromDomain(Analysis analysis) {
        AnalysisEntity analysisEntity = new AnalysisEntity();
        analysisEntity.setId(analysis.getId());
        analysisEntity.setOwner(analysis.getOwner());
        analysisEntity.setStatus(analysis.getStatus());
        analysisEntity.setCreationDate(analysis.getCreationDate());
        analysisEntity.setPayloads(analysis.getPayloads().stream().map(this::fromDomain).toList());
        return analysisEntity;
    }

    private EstimatedPayloadEntity fromDomain(EstimatedPayload payload) {
        EstimatedPayloadEntity estimatedPayloadEntity = new EstimatedPayloadEntity();
        estimatedPayloadEntity.setId(payload.id());
        estimatedPayloadEntity.setAnalysis(new AnalysisEntity());
        estimatedPayloadEntity.getAnalysis().setId(payload.reportId());
        estimatedPayloadEntity.setCarbonGramFootprint(payload.carbonGramFootprint());
        estimatedPayloadEntity.setName(payload.name());
        return estimatedPayloadEntity;
    }
}
