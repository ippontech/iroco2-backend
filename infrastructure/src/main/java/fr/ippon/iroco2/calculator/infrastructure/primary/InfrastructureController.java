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
package fr.ippon.iroco2.calculator.infrastructure.primary;

import fr.ippon.iroco2.common.primary.security.IsMember;
import fr.ippon.iroco2.domain.calculator.model.Infrastructure;
import fr.ippon.iroco2.domain.calculator.primary.EstimationSvc;
import fr.ippon.iroco2.domain.calculator.primary.InfrastructureSvc;
import fr.ippon.iroco2.domain.commons.exception.FunctionalException;
import fr.ippon.iroco2.domain.commons.secondary.SessionProvider;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static fr.ippon.iroco2.calculator.infrastructure.component.primary.ComponentMapper.COMPONENT_AND_RESPONSE_MAPPER;

@RestController
@RequestMapping("/api/v2/infrastructures")
@RequiredArgsConstructor
public class InfrastructureController {
    private final InfrastructureSvc infrastructureService;
    private final EstimationSvc estimationSvc;
    private final SessionProvider sessionProvider;

    @PostMapping
    @IsMember
    @ResponseStatus(HttpStatus.CREATED)
    public void save(@RequestBody @Valid InfrastructureRequest request) {
        String owner = sessionProvider.getConnectedUserEmail();
        Infrastructure infrastructure = request.createToDomain(owner);
        infrastructureService.save(infrastructure);
    }

    @IsMember
    @GetMapping("/{id}")
    public ResponseEntity<InfrastructureResponse> findById(@PathVariable UUID id) {
        var infrastructure = infrastructureService.findById(id);
        InfrastructureResponse response = COMPONENT_AND_RESPONSE_MAPPER.toResponse(infrastructure);
        return ResponseEntity.ok(response);
    }

    @IsMember
    @GetMapping("/{id}/carbon-footprint")
    public ResponseEntity<List<CarbonFootprintResponse>> estimateCarbonFootprint(@PathVariable UUID id)
            throws FunctionalException {
        var carbonFootprintByComponent = estimationSvc.estimateCarbonFootprintByInfrastructureId(id);

        List<CarbonFootprintResponse> responses = new ArrayList<>();
        carbonFootprintByComponent.forEach(
                (component, co2Gr) -> responses.add(CarbonFootprintResponse.createFrom(component, co2Gr))
        );

        return ResponseEntity.ok(responses);
    }

    @IsMember
    @GetMapping("/{id}/byregion-carbon-footprint")
    public ResponseEntity<List<RegionCarbonFootprintResponse>> estimateCarbonFootprintForAllRegions(
            @PathVariable UUID id
    ) throws FunctionalException {
        var carbonFootprintByComponent = estimationSvc.estimateCarbonFootprintByInfrastructureIdForAllRegions(id);

        List<RegionCarbonFootprintResponse> responses = new ArrayList<>();
        carbonFootprintByComponent.forEach(
                (region, co2Gr) -> responses.add(RegionCarbonFootprintResponse.createFrom(region, co2Gr))
        );

        return ResponseEntity.ok(responses);
    }

    @IsMember
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable UUID id) {
        infrastructureService.delete(id);
    }

    @IsMember
    @GetMapping
    public ResponseEntity<List<InfrastructureResponse>> getInfrastructures() {
        var infrastructures = infrastructureService.findAll();
        List<InfrastructureResponse> responses = infrastructures.stream().map(COMPONENT_AND_RESPONSE_MAPPER::toResponse).toList();
        return ResponseEntity.ok(responses);
    }
}
