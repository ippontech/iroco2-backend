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
package fr.ippon.iroco2.analyzer.presentation;

import fr.ippon.iroco2.common.presentation.security.IsMember;
import fr.ippon.iroco2.domain.analyzer.api.AnalyzerSvc;
import fr.ippon.iroco2.domain.analyzer.exception.AnalysisNotFoundException;
import fr.ippon.iroco2.analyzer.aws_s3.BucketStorage;
import fr.ippon.iroco2.analyzer.presentation.mapper.AnalysisMapper;
import fr.ippon.iroco2.analyzer.presentation.response.AnalysisDetailResponse;
import fr.ippon.iroco2.analyzer.presentation.response.AnalysisListElementResponse;
import fr.ippon.iroco2.analyzer.presentation.response.CreatedAnalysisResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/analysis")
@RequiredArgsConstructor
public class AnalyzerController {

    private final BucketStorage bucketStorage;
    private final AnalyzerSvc analyzerSvc;
    public final AnalysisMapper analysisMapper;

    @IsMember
    @GetMapping("/{id}")
    public ResponseEntity<AnalysisDetailResponse> findById(@PathVariable UUID id) throws AnalysisNotFoundException {
        var analysis = analyzerSvc.findById(id);
        var response = analysisMapper.toDetailResponse(analysis);
        return ResponseEntity.ok(response);
    }

    @IsMember
    @GetMapping
    public ResponseEntity<List<AnalysisListElementResponse>> findAll() {
        var analyses = analyzerSvc.findAll();
        var responses = analysisMapper.toResponse(analyses);
        return ResponseEntity.ok(responses);
    }

    @IsMember
    @GetMapping("/presigned-url/{fileFormat}")
    public ResponseEntity<CreatedAnalysisResponse> getPresignedUrl(@PathVariable String fileFormat) {
        var analysisId = analyzerSvc.create();
        String presignedUrl = bucketStorage.createPresignedUrl(analysisId + "." + fileFormat);
        var result = analysisMapper.toResponse(analysisId, presignedUrl);
        return ResponseEntity.ok(result);
    }

    @IsMember
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable UUID id) {
        analyzerSvc.delete(id);
    }
}
