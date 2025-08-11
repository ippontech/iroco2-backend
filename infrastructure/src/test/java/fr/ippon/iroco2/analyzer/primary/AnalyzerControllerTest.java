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
package fr.ippon.iroco2.analyzer.primary;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.c4_soft.springaddons.security.oauth2.test.annotations.WithMockAuthentication;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.ippon.iroco2.analyzer.secondary.AnalysisEntity;
import fr.ippon.iroco2.analyzer.secondary.AnalysisRepository;
import fr.ippon.iroco2.common.secondary.EstimatedPayloadEntity;
import fr.ippon.iroco2.config.KmsMockConfig;
import fr.ippon.iroco2.domain.commons.model.ReportStatus;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Testcontainers
@SpringBootTest
@AutoConfigureMockMvc
@WithMockAuthentication(authorities = {"ROLE_MEMBER"}, name = AnalyzerControllerTest.USER_EMAIL)
@Transactional
@Import(KmsMockConfig.class)
class AnalyzerControllerTest {

    public static final int DEFAULT_TEST_CARBON_GRAM_FOOTPRINT = 1569874;
    public static final String USER_EMAIL = "user@email.com";
    public static final String DEFAULT_TEST_PAYLOAD_NAME = "serviceName EC2";

    @Container
    private static final LocalStackContainer localStackContainer = new LocalStackContainer(
            DockerImageName.parse("localstack/localstack")
    ).withServices(LocalStackContainer.Service.S3);

    @Autowired
    public ObjectMapper objectMapper;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AnalysisRepository analysisRepository;

    @DynamicPropertySource
    static void registerProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.cloud.aws.credentials.access-key", localStackContainer::getAccessKey);
        registry.add("spring.cloud.aws.credentials.secret-key", localStackContainer::getSecretKey);

        registry.add(
                "spring.cloud.aws.s3.endpoint",
                () -> localStackContainer.getEndpointOverride(LocalStackContainer.Service.S3).toString()
        );
        registry.add("spring.cloud.aws.s3.region", localStackContainer::getRegion);
        registry.add("spring.cloud.aws.s3.enabled", () -> "true");
    }

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    private void assertThatUrlReturnedIsWellFormated(ResultActions result)
            throws UnsupportedEncodingException, JsonProcessingException {
        String resultAsString = result.andReturn().getResponse().getContentAsString();

        CreatedAnalysisResponse createdAnalysisResponse = objectMapper.readValue(
                resultAsString,
                CreatedAnalysisResponse.class
        );

        String pattern = localStackContainer.getEndpointOverride(LocalStackContainer.Service.S3).toString() + ".*";

        assertThat(createdAnalysisResponse.presignedUrl()).matches(pattern);
    }

    private List<AnalysisListElementResponse> readJSonResponse(ResultActions result)
            throws UnsupportedEncodingException, JsonProcessingException {
        String contentAsString = result.andReturn().getResponse().getContentAsString();
        return objectMapper.readValue(contentAsString, new TypeReference<>() {
        });
    }

    @Test
    void should_return_presigned_url() throws Exception {
        // WHEN
        var result = mockMvc.perform(MockMvcRequestBuilders.get("/api/analysis/presigned-url/csv"));

        // THEN
        result.andExpect(status().isOk()).andReturn();
        assertThatUrlReturnedIsWellFormated(result);
        assertAnalysisCreatedForUser();
    }

    private void assertAnalysisCreatedForUser() {
        assertThat(analysisRepository.findByOwnerOrderByCreationDateAsc(USER_EMAIL)).hasSize(1);
    }

    @Test
    void should_return_listWithAnalysissOfConnectedUser() throws Exception {
        // GIVEN
        EstimatedPayloadEntity payloadEntity = givenExistingAnalysisWithPayload(USER_EMAIL);
        var analysis = payloadEntity.getAnalysis();

        // WHEN
        var result = mockMvc.perform(MockMvcRequestBuilders.get("/api/analysis"));

        // THEN
        result
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(analysis.getId().toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].status").value(analysis.getStatus().toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].co2Gr").value(DEFAULT_TEST_CARBON_GRAM_FOOTPRINT));
    }

    @Test
    void should_return_emptyListIfConnectedUserHasNoAnalysis() throws Exception {
        // WHEN
        var result = mockMvc.perform(MockMvcRequestBuilders.get("/api/analysis"));

        // THEN
        result.andExpect(status().isOk()).andReturn();

        var parsedResult = readJSonResponse(result);
        assertThat(parsedResult).isEmpty();
    }

    @Test
    void should_return_empty_list_when_single_analysis_is_deleted() throws Exception {
        // GIVEN
        EstimatedPayloadEntity payloadEntity = givenExistingAnalysisWithPayload(USER_EMAIL);
        var analysis = payloadEntity.getAnalysis();

        // WHEN
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/analysis/" + analysis.getId()));

        // THEN
        var result = mockMvc.perform(MockMvcRequestBuilders.get("/api/analysis"));
        result.andExpect(status().isOk()).andReturn();

        var parsedResult = readJSonResponse(result);
        assertThat(parsedResult).isEmpty();
    }

    @Test
    void should_return_403_when_connectedUserIsNotOwner() throws Exception {
        // GIVEN
        EstimatedPayloadEntity payloadEntity = givenExistingAnalysisWithPayload("not the owner");
        var analysisEntity = payloadEntity.getAnalysis();

        // WHEN
        var result = mockMvc.perform(MockMvcRequestBuilders.get("/api/analysis/" + analysisEntity.getId().toString()));

        // THEN
        result.andExpect(status().isForbidden()).andReturn();
    }

    @Test
    void should_return_analysisDetails_when_connectedUserIsOwner() throws Exception {
        // GIVEN
        EstimatedPayloadEntity payloadEntity = givenExistingAnalysisWithPayload(USER_EMAIL);
        var analysisEntity = payloadEntity.getAnalysis();

        // WHEN
        var result = mockMvc.perform(MockMvcRequestBuilders.get("/api/analysis/" + analysisEntity.getId().toString()));

        // THEN
        result
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(analysisEntity.getId().toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(analysisEntity.getStatus().toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payloads").isArray())
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.payloads[0].carbonGramFootprint").value(
                                payloadEntity.getCarbonGramFootprint()
                        )
                )
                .andExpect(MockMvcResultMatchers.jsonPath("$.payloads[0].name").value(DEFAULT_TEST_PAYLOAD_NAME));
    }

    private @NotNull EstimatedPayloadEntity givenExistingAnalysisWithPayload(String owner) {
        AnalysisEntity entity = new AnalysisEntity();
        entity.setId(UUID.randomUUID());
        entity.setStatus(ReportStatus.IN_PROGRESS);
        entity.setOwner(owner);
        entity.setCreationDate(LocalDateTime.now());

        EstimatedPayloadEntity payload = new EstimatedPayloadEntity();
        payload.setId(UUID.randomUUID());
        payload.setAnalysis(entity);
        payload.setCarbonGramFootprint(DEFAULT_TEST_CARBON_GRAM_FOOTPRINT);
        payload.setName(DEFAULT_TEST_PAYLOAD_NAME);

        entity.getPayloads().add(payload);
        analysisRepository.save(entity);
        return payload;
    }
}
