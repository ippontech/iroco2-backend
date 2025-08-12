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
package fr.ippon.iroco2.domain.commons;

import fr.ippon.iroco2.domain.commons.exception.FunctionalException;
import fr.ippon.iroco2.domain.commons.exception.NotFoundException;
import fr.ippon.iroco2.domain.commons.model.AReport;
import fr.ippon.iroco2.domain.commons.model.EstimatedPayload;
import fr.ippon.iroco2.domain.commons.model.Payload;
import fr.ippon.iroco2.domain.commons.secondary.ReportStorage;
import fr.ippon.iroco2.domain.estimator.CarbonEstimator;

import java.util.UUID;

public abstract class AbstractElementSvc<R extends AReport<R>, E extends NotFoundException> implements ElementSvc<R, E> {
    @Override
    public final void addEstimation(Payload payload) throws FunctionalException {
        R report = getStorage()
                .findById(payload.reportId())
                .orElseThrow(() -> createException(payload.reportId()));

        String countryIsoCode = payload.countryIsoCode();
        var carbonGramFootprint = getCarbonEstimator().estimatePayload(countryIsoCode, payload);
        var estimatedPayload = EstimatedPayload.create(payload, carbonGramFootprint);

        R updatedReport = report.addPayload(estimatedPayload, payload.expectedPayloads());
        getStorage().save(updatedReport);
    }

    protected abstract E createException(UUID reportId);

    protected abstract ReportStorage<R> getStorage();

    protected abstract CarbonEstimator getCarbonEstimator();
}
