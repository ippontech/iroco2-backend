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
package fr.ippon.iroco2.calculateur.presentation;

import fr.ippon.iroco2.domain.calculateur.api.CloudServiceProviderRegionSvc;
import fr.ippon.iroco2.domain.calculateur.api.CloudServiceProviderServiceSvc;
import fr.ippon.iroco2.domain.calculateur.api.CloudServiceProviderSvc;
import fr.ippon.iroco2.calculateur.presentation.mapper.CloudServiceProviderMapper;
import fr.ippon.iroco2.calculateur.presentation.mapper.CloudServiceProviderRegionMapper;
import fr.ippon.iroco2.calculateur.presentation.mapper.CloudServiceProviderServiceMapper;
import fr.ippon.iroco2.calculateur.presentation.reponse.CloudServiceProviderRegionResponse;
import fr.ippon.iroco2.calculateur.presentation.reponse.CloudServiceProviderResponse;
import fr.ippon.iroco2.calculateur.presentation.reponse.CloudServiceProviderServiceResponse;
import fr.ippon.iroco2.common.presentation.security.IsMember;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/cloud-service-providers")
@RequiredArgsConstructor
public class CloudServiceProviderController {

    public static final CloudServiceProviderRegionMapper REGION_MAPPER = Mappers.getMapper(CloudServiceProviderRegionMapper.class);
    public static final CloudServiceProviderMapper PROVIDER_MAPPER = Mappers.getMapper(CloudServiceProviderMapper.class);

    private final CloudServiceProviderRegionSvc cloudServiceProviderRegionSvc;
    private final CloudServiceProviderSvc cloudServiceProviderSvc;
    private final CloudServiceProviderServiceSvc cloudServiceProviderServiceSvc;

    @GetMapping("/{cspId}/regions")
    @IsMember
    public ResponseEntity<List<CloudServiceProviderRegionResponse>> findAllRegionsByCsp(@PathVariable UUID cspId) {
        var cloudServiceProviderRegions = cloudServiceProviderRegionSvc.findAllByCsp(cspId);
        return ResponseEntity.ok(REGION_MAPPER.toResponse(cloudServiceProviderRegions));
    }

    @GetMapping
    @IsMember
    public ResponseEntity<List<CloudServiceProviderResponse>> findAllCsp() {
        var serviceProviders = cloudServiceProviderSvc.findAll();
        return ResponseEntity.ok(PROVIDER_MAPPER.toResponse(serviceProviders));
    }

    @GetMapping("/{cspId}/services")
    @IsMember
    public ResponseEntity<List<CloudServiceProviderServiceResponse>> findAllServicesByCsp(@PathVariable UUID cspId) {
        var cloudServiceProviderServices = cloudServiceProviderServiceSvc.findAllByCsp(cspId);
        return ResponseEntity.ok(Mappers.getMapper(CloudServiceProviderServiceMapper.class).toResponse(cloudServiceProviderServices));
    }

}
