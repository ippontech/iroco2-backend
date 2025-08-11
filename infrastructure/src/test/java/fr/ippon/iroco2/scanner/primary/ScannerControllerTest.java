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
package fr.ippon.iroco2.scanner.primary;

import fr.ippon.iroco2.access.primary.JwtAuthenticationFilter;
import fr.ippon.iroco2.access.primary.ScannerAuthenticationFilter;
import fr.ippon.iroco2.common.secondary.EstimatedPayloadEntity;
import fr.ippon.iroco2.domain.commons.exception.NotFoundException;
import fr.ippon.iroco2.domain.commons.model.ReportStatus;
import fr.ippon.iroco2.domain.scanner.api.ScannerSvc;
import fr.ippon.iroco2.scanner.secondary.ScanEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import software.amazon.awssdk.annotations.NotNull;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = ScannerController.class,
        excludeFilters = @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                value = {JwtAuthenticationFilter.class, ScannerAuthenticationFilter.class}
        )
)
@WithMockUser
class ScannerControllerTest {
    private static final String USER_EMAIL = "user@email.com";
    private static final int DEFAULT_TEST_CARBON_GRAM_FOOTPRINT = 1569874;
    private static final String DEFAULT_TEST_PAYLOAD_NAME = "serviceName EC2";
    private static final String AWS_ACCOUNT_ID = "0123456789012";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ScannerSvc scannerSvc;

    @Test
    void save_should_return_201_and_uuid_when_valid_request() throws Exception {
        // GIVEN
        UUID scanId = UUID.randomUUID();
        when(scannerSvc.create()).thenReturn(scanId);

        // WHEN & THEN
        mockMvc
                .perform(post("/api/scanner").with(csrf()))
                .andExpect(status().isCreated())
                .andExpect(content().string("\"%s\"".formatted(scanId)));
    }

    @Test
    void findAll_should_return_list_with_scans_when_connected_user_has_scans() throws Exception {
        // GIVEN
        EstimatedPayloadEntity payloadEntity = givenExistingScanWithPayload();
        var scan = payloadEntity.getScan().toDomain();

        // WHEN
        when(scannerSvc.findAll()).thenReturn(List.of(scan));
        var result = mockMvc.perform(get("/api/scanner"));

        // THEN
        result
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").value(scan.getId().toString()))
                .andExpect(jsonPath("$[0].status").value(scan.getStatus().toString()))
                .andExpect(jsonPath("$[0].co2Gr").value(DEFAULT_TEST_CARBON_GRAM_FOOTPRINT));
    }

    @Test
    void findAll_should_return_empty_list_when_connected_user_has_no_scans() throws Exception {
        // WHEN
        var result = mockMvc.perform(get("/api/scanner"));

        // THEN
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void delete_should_delete_scan_by_id() throws Exception {
        // GIVEN
        EstimatedPayloadEntity payloadEntity = givenExistingScanWithPayload();
        var scan = payloadEntity.getScan();

        // WHEN
        var result = mockMvc.perform(delete("/api/scanner/" + scan.getId()).with(csrf()));

        // THEN
        verify(scannerSvc).delete(scan.getId());
        result.andExpect(status().isOk());
    }

    @Test
    void delete_should_throw_an_exception_if_scan_does_not_exist() throws Exception {
        // GIVEN
        UUID scanId = UUID.randomUUID();
        doThrow(new NotFoundException("Not found")).when(scannerSvc).delete(scanId);

        // WHEN
        var result = mockMvc.perform(delete("/api/scanner/" + scanId).with(csrf()));

        // THEN
        result.andExpect(status().isNotFound());
    }

    @Test
    void findById_should_return_scan_when_connected_user_is_owner() throws Exception {
        // GIVEN
        EstimatedPayloadEntity payloadEntity = givenExistingScanWithPayload();
        var scan = payloadEntity.getScan().toDomain();

        // WHEN
        when(scannerSvc.findById(scan.getId())).thenReturn(scan);
        var result = mockMvc.perform(get("/api/scanner/" + scan.getId()));

        // THEN
        result
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(scan.getId().toString()))
                .andExpect(jsonPath("$.status").value(scan.getStatus().toString()))
                .andExpect(jsonPath("$.payloads").isArray())
                .andExpect(jsonPath("$.payloads[0].carbonGramFootprint").value(payloadEntity.getCarbonGramFootprint()))
                .andExpect(jsonPath("$.payloads[0].name").value(payloadEntity.getName()));
    }

    private @NotNull EstimatedPayloadEntity givenExistingScanWithPayload() {
        ScanEntity entity = new ScanEntity();
        entity.setId(UUID.randomUUID());
        entity.setStatus(ReportStatus.IN_PROGRESS);
        entity.setOwner(ScannerControllerTest.USER_EMAIL);
        entity.setAwsAccountId(ScannerControllerTest.AWS_ACCOUNT_ID);
        entity.setCreationDate(LocalDateTime.now());
        entity.setPayloads(new ArrayList<>());

        EstimatedPayloadEntity payload = new EstimatedPayloadEntity();
        payload.setId(UUID.randomUUID());
        payload.setScan(entity);
        payload.setCarbonGramFootprint(DEFAULT_TEST_CARBON_GRAM_FOOTPRINT);
        payload.setName(DEFAULT_TEST_PAYLOAD_NAME);

        entity.getPayloads().add(payload);
        return payload;
    }
}
