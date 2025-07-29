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
package fr.ippon.iroco2.domain.analyzer;

import fr.ippon.iroco2.domain.analyzer.api.AnalyzerSvc;
import fr.ippon.iroco2.domain.analyzer.exception.AnalysisNotFoundException;
import fr.ippon.iroco2.domain.analyzer.model.Analysis;
import fr.ippon.iroco2.domain.analyzer.spi.AnalysisStorage;
import fr.ippon.iroco2.domain.commons.AbstractElementSvc;
import fr.ippon.iroco2.domain.commons.DomainService;
import fr.ippon.iroco2.domain.commons.exception.NotFoundException;
import fr.ippon.iroco2.domain.commons.exception.UnauthorizedActionException;
import fr.ippon.iroco2.domain.commons.spi.ReportStorage;
import fr.ippon.iroco2.domain.commons.spi.SessionProvider;
import fr.ippon.iroco2.domain.commons.svc.DateProvider;
import fr.ippon.iroco2.domain.estimateur.CarbonEstimator;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;

@DomainService
@RequiredArgsConstructor
public class AnalyzerSvcImpl extends AbstractElementSvc<Analysis, AnalysisNotFoundException> implements AnalyzerSvc {

    private final AnalysisStorage analysisStorage;
    private final SessionProvider sessionProvider;
    private final DateProvider dateProvider;
    private final CarbonEstimator carbonEstimator;

    @Override
    public UUID create() {
        String owner = sessionProvider.getConnectedUserEmail();
        var analysis = Analysis.create(owner, dateProvider.now());
        analysisStorage.save(analysis);
        return analysis.getId();
    }

    @Override
    public List<Analysis> findAll() {
        String owner = sessionProvider.getConnectedUserEmail();
        return analysisStorage.findByOwner(owner);
    }

    @Override
    public Analysis findById(UUID id) throws AnalysisNotFoundException {
        var analysis = analysisStorage.findById(id).orElseThrow(() -> new AnalysisNotFoundException(id));

        String connectedUser = sessionProvider.getConnectedUserEmail();
        if (analysis.getOwner().equals(connectedUser)) return analysis;
        else throw new UnauthorizedActionException(
                "%s n'est pas autorisé à accéder à cette ressource".formatted(connectedUser)
        );
    }

    @Override
    public void delete(UUID analysisId) {
        Analysis analysis = existsControl(analysisId);
        analysisStorage.delete(analysis.getId());
    }

    private Analysis existsControl(UUID analysisId) {
        return analysisStorage
                .findById(analysisId)
                .orElseThrow(() -> new NotFoundException("The analysis with ID '%s' is not found".formatted(analysisId)));
    }

    @Override
    protected AnalysisNotFoundException createException(UUID reportId) {
        return new AnalysisNotFoundException(reportId);
    }

    @Override
    protected ReportStorage<Analysis> getStorage() {
        return analysisStorage;
    }

    @Override
    protected CarbonEstimator getCarbonEstimator() {
        return carbonEstimator;
    }
}
