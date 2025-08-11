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
package fr.ippon.iroco2.catalog.primary;

import fr.ippon.iroco2.catalog.primary.service.CloudServiceProviderServiceResponse;
import fr.ippon.iroco2.domain.calculator.api.CloudServiceProviderServiceSvc;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

import static fr.ippon.iroco2.catalog.primary.service.CloudServiceProviderServiceMapper.SERVICE_AND_RESPONSE_MAPPER;

@RestController
@RequestMapping("/api/public/v2/catalog")
@RequiredArgsConstructor
public class CatalogController {
    private final CloudServiceProviderServiceSvc cloudServiceProviderServiceSvc;

    @GetMapping("/services")
    public ResponseEntity<List<CloudServiceProviderServiceResponse>> getAllServices() {
        var catalog = cloudServiceProviderServiceSvc.findAll();
        return ResponseEntity.ok(SERVICE_AND_RESPONSE_MAPPER.toResponse(catalog));
    }

    @GetMapping("/services/{serviceId}")
    public ResponseEntity<CloudServiceProviderServiceResponse> getServiceById(@PathVariable UUID serviceId) {
        var service = cloudServiceProviderServiceSvc.findServiceById(serviceId);
        return ResponseEntity.ok(SERVICE_AND_RESPONSE_MAPPER.toResponse(service));
    }
}
