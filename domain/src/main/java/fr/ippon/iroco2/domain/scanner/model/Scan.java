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
package fr.ippon.iroco2.domain.scanner.model;

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
public class Scan extends AReport<Scan> {

    private final String awsAccountId;

    private Scan(UUID id, String owner, String awsAccountId, ReportStatus status, LocalDateTime creationDate, List<EstimatedPayload> payloads) {
        super(id, owner, status, creationDate, payloads);
        this.awsAccountId = awsAccountId;
    }

    public static Scan create(String owner, LocalDateTime creationDate, String awsAccountId) {
        return new Scan(UUID.randomUUID(), owner, awsAccountId, ReportStatus.CREATED, creationDate, new ArrayList<>());
    }

    public static Scan load(UUID id, String owner, ReportStatus status, LocalDateTime creationDate, List<EstimatedPayload> domainPayloads, String awsAccountId) {
        return new Scan(id, owner, awsAccountId, status, creationDate, domainPayloads);
    }

    public Scan addPayload(EstimatedPayload estimatedPayload, int expectedPayloads) {
        var newPayloads = new ArrayList<>(payloads);
        newPayloads.add(estimatedPayload);

        boolean isLoadingEnded = newPayloads.size() == expectedPayloads;
        return Scan.load(id, owner, isLoadingEnded ? ReportStatus.SUCCESS : ReportStatus.IN_PROGRESS, creationDate, newPayloads, awsAccountId);
    }
}
