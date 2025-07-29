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
package fr.ippon.iroco2.domain.analyzer.model;

import fr.ippon.iroco2.domain.commons.model.AReport;
import fr.ippon.iroco2.domain.commons.model.EstimatedPayload;
import fr.ippon.iroco2.domain.commons.model.ReportStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class Analysis extends AReport<Analysis> {

    private Analysis(UUID id, String owner, ReportStatus status, LocalDateTime creationDate, List<EstimatedPayload> payloads) {
        super(id, owner, status, creationDate, payloads);
    }

    public static Analysis create(String owner, LocalDateTime creationDate) {
        return new Analysis(UUID.randomUUID(), owner, ReportStatus.CREATED, creationDate, new ArrayList<>());
    }

    public static Analysis load(UUID id, String owner, ReportStatus status, LocalDateTime creationDate, List<EstimatedPayload> payloads) {
        return new Analysis(id, owner, status, creationDate, payloads);
    }

    public Analysis addPayload(EstimatedPayload estimatedPayload, int expectedPayloads) {
        var newPayloads = new ArrayList<>(payloads);
        newPayloads.add(estimatedPayload);

        boolean isLoadingEnded = newPayloads.size() == expectedPayloads;
        return Analysis.load(id, owner, isLoadingEnded ? ReportStatus.SUCCESS : ReportStatus.IN_PROGRESS, creationDate, newPayloads);
    }
}
