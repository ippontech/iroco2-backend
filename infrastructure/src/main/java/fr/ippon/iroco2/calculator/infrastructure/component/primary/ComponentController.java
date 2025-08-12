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
package fr.ippon.iroco2.calculator.infrastructure.component.primary;

import fr.ippon.iroco2.common.primary.security.IsMember;
import fr.ippon.iroco2.domain.calculator.model.Component;
import fr.ippon.iroco2.domain.calculator.primary.ComponentSvc;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

import static fr.ippon.iroco2.calculator.infrastructure.component.primary.ComponentMapper.COMPONENT_AND_RESPONSE_MAPPER;

@RestController
@RequestMapping("/api/v2/components")
@RequiredArgsConstructor
public class ComponentController {
    private final ComponentSvc componentSvc;

    @PostMapping
    @IsMember
    @ResponseStatus(HttpStatus.CREATED)
    public void save(@RequestBody @Valid ComponentRequest componentRequest) {
        Component component = componentRequest.createToDomain();
        componentSvc.save(component);
    }

    @PatchMapping
    @IsMember
    @ResponseStatus(HttpStatus.OK)
    public void update(@RequestBody @Valid ComponentRequest componentRequest) {
        Component component = componentRequest.updateToDomain();
        componentSvc.update(component);
    }

    @IsMember
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable UUID id) {
        componentSvc.delete(id);
    }

    @GetMapping
    @IsMember
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<ComponentResponse>> findAllByInfrastructureID(@RequestParam("infrastructureId") @Valid @NotNull UUID infrastructureId) {
        var components = componentSvc.findAllByInfrastructureID(infrastructureId);
        List<ComponentResponse> response = COMPONENT_AND_RESPONSE_MAPPER.toResponse(components);
        return ResponseEntity.ok().body(response);
    }
}
