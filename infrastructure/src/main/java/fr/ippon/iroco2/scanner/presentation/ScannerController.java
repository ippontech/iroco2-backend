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
package fr.ippon.iroco2.scanner.presentation;

import fr.ippon.iroco2.common.presentation.security.IsMember;
import fr.ippon.iroco2.domain.scanner.api.ScannerSvc;
import fr.ippon.iroco2.domain.scanner.exception.ScanNotFoundException;
import fr.ippon.iroco2.scanner.presentation.mapper.ScanMapper;
import fr.ippon.iroco2.scanner.presentation.response.ScanDetailResponse;
import fr.ippon.iroco2.scanner.presentation.response.ScanListElementResponse;

import java.util.List;
import java.util.UUID;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/scanner")
@RequiredArgsConstructor
public class ScannerController {
    private final ScannerSvc scannerSvc;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<UUID> save() {
        UUID savedScan = scannerSvc.create();
        return ResponseEntity.status(HttpStatus.CREATED).body(savedScan);
    }

    @IsMember
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable UUID id) throws ScanNotFoundException {
        scannerSvc.delete(id);
    }

    @IsMember
    @GetMapping
    public ResponseEntity<List<ScanListElementResponse>> findAll() {
        var scans = scannerSvc.findAll();
        var responses = ScanMapper.toResponse(scans);
        return ResponseEntity.ok(responses);
    }

    @IsMember
    @GetMapping("/{id}")
    public ResponseEntity<ScanDetailResponse> findById(@PathVariable UUID id) throws ScanNotFoundException {
        var scan = scannerSvc.findById(id);
        var response = ScanMapper.toDetailResponse(scan);
        return ResponseEntity.ok(response);
    }
}
