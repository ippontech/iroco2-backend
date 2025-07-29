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
package fr.ippon.iroco2.scanner.presentation.mapper;

import fr.ippon.iroco2.common.presentation.response.EstimatedPayloadResponse;
import fr.ippon.iroco2.domain.commons.model.EstimatedPayload;
import fr.ippon.iroco2.domain.scanner.model.Scan;
import fr.ippon.iroco2.scanner.presentation.response.ScanDetailResponse;
import fr.ippon.iroco2.scanner.presentation.response.ScanListElementResponse;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ScanMapper {
    public List<ScanListElementResponse> toResponse(List<Scan> scans) {
        return scans.stream().map(this::toResponse).toList();
    }

    protected ScanListElementResponse toResponse(Scan scan) {
        int co2Gr = scan.getPayloads().stream().mapToInt(EstimatedPayload::carbonGramFootprint).sum();
        return new ScanListElementResponse(scan.getId(), scan.getStatus(), scan.getCreationDate(), co2Gr);
    }

    public ScanDetailResponse toDetailResponse(Scan scan) {
        return new ScanDetailResponse(scan.getId(), scan.getStatus(), scan.getPayloads().stream().map(this::toResponse).toList());
    }

    private EstimatedPayloadResponse toResponse(EstimatedPayload estimatedPayload) {
        return new EstimatedPayloadResponse(estimatedPayload.carbonGramFootprint(), estimatedPayload.name());
    }
}
