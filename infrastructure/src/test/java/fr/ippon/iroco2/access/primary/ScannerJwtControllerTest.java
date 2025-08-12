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
package fr.ippon.iroco2.access.primary;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.ippon.iroco2.access.jwt.ScannerJwtGenerator;
import fr.ippon.iroco2.access.jwt.ScannerJwtVerifier;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = ScannerJwtController.class,
        excludeFilters = @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                value = {JwtAuthenticationFilter.class, ScannerAuthenticationFilter.class}
        )
)
@WithMockUser
class ScannerJwtControllerTest {
    @Autowired
    public ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ScannerJwtGenerator scannerJwtGenerator;
    @MockBean
    private ScannerJwtVerifier scannerJwtVerifier;

    @Test
    void generate_should_return_jwt_token() throws Exception {
        // GIVEN
        var request = new GenerateTokenRequest("123456789012", 3600);
        when(scannerJwtGenerator.generate("123456789012", 3600)).thenReturn("jwt-token");

        // WHEN
        var result = mockMvc.perform(
                post("/api/v1/token/generate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .with(csrf())
        );

        // THEN
        result.andExpect(status().isOk())
                .andExpect(content().string("jwt-token"));
    }

    @Test
    void verify_should_return_true_if_verify_ok() throws Exception {
        //GIVEN
        var apiKey = "api-key";
        when(scannerJwtVerifier.verify(apiKey)).thenReturn(TRUE);

        //WHEN
        var result = mockMvc.perform(get("/api/v1/token/verify").queryParam("apiKey", apiKey));

        //THEN
        result.andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    void verify_should_return_false_if_verify_ko() throws Exception {
        //GIVEN
        var apiKey = "api-key";
        when(scannerJwtVerifier.verify(apiKey)).thenReturn(FALSE);

        //WHEN
        var result = mockMvc.perform(get("/api/v1/token/verify").queryParam("apiKey", apiKey));

        //THEN
        result.andExpect(status().isOk())
                .andExpect(content().string("false"));
    }
}
