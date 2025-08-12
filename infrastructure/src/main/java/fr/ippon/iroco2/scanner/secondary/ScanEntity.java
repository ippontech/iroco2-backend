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
package fr.ippon.iroco2.scanner.secondary;

import fr.ippon.iroco2.common.secondary.EstimatedPayloadEntity;
import fr.ippon.iroco2.common.secondary.ReportEntity;
import fr.ippon.iroco2.domain.commons.model.EstimatedPayload;
import fr.ippon.iroco2.domain.scanner.model.Scan;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor

@Entity
@Table(name = "scan")
public class ScanEntity extends ReportEntity {

    @OneToMany(mappedBy = "scan", fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REMOVE})
    protected List<EstimatedPayloadEntity> payloads;
    @Column(name = "aws_account_id")
    private String awsAccountId;

    public Scan toDomain() {
        var domainPayloads = payloads.stream().map(p -> new EstimatedPayload(p.getId(), id, p.getCarbonGramFootprint(), p.getName())).toList();
        return Scan.load(id, owner, status, creationDate, domainPayloads, awsAccountId);
    }
}
