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
package fr.ippon.iroco2.infrastructure;

import com.c4_soft.springaddons.security.oauth2.test.annotations.WithMockAuthentication;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.ippon.iroco2.calculateur.presentation.reponse.CarbonFootprintResponse;
import fr.ippon.iroco2.calculateur.presentation.reponse.RegionCarbonFootprintResponse;
import fr.ippon.iroco2.config.TestContainersPostgresqlConfig;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;
import java.util.UUID;

@AutoConfigureMockMvc(addFilters = false)
@WithMockAuthentication(authorities = {"ROLE_MEMBER"}, name = "charles@ippon.fr")
class EstimateCarbonFootTest extends TestContainersPostgresqlConfig {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void estimateCarbonFootprint() throws Exception {
        String infrastructureId = "ebda6c8b-3c28-4046-8f91-ac112a7e24c2";
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v2/infrastructures/" + infrastructureId + "/carbon-footprint");

        var result = this.mockMvc.perform(requestBuilder);

        // Mapper le JSON en liste d'objets
        String json = result.andReturn().getResponse().getContentAsString();
        List<CarbonFootprintResponse> responses = new ObjectMapper().readValue(json, new TypeReference<>() {
        });
        var myEC2 = new CarbonFootprintResponse("MyEC2", "Amazon Elastic Compute Cloud (EC2)", 871);
        Assertions.assertThat(responses).containsExactly(myEC2);
    }

    @Test
    void estimateCarbonFootprintByRegions() throws Exception {
        String infrastructureId = "ebda6c8b-3c28-4046-8f91-ac112a7e24c2";
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v2/infrastructures/" + infrastructureId + "/byregion-carbon-footprint");

        var result = this.mockMvc.perform(requestBuilder);

        result.andExpect(MockMvcResultMatchers.status().isOk());

        // Mapper le JSON en liste d'objets
        String json = result.andReturn().getResponse().getContentAsString();
        List<RegionCarbonFootprintResponse> responses = new ObjectMapper().readValue(json, new TypeReference<>() {
        });
        var paris = new RegionCarbonFootprintResponse(UUID.fromString("123e4567-e89b-12d3-a456-426614174001"), "Europe (Paris)", 871);
        var virginia = new RegionCarbonFootprintResponse(UUID.fromString("027311d5-9892-41f1-9ad1-8e45e6f0d374"), "US East (N. Virginia)", 4711);
        Assertions.assertThat(responses).containsExactlyInAnyOrder(paris, virginia);
    }
}