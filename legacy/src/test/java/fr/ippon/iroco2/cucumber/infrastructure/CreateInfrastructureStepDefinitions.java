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
package fr.ippon.iroco2.cucumber.infrastructure;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.ippon.iroco2.calculateur.persistence.repository.CloudServiceProviderRepository;
import fr.ippon.iroco2.calculateur.persistence.repository.entity.CloudServiceProviderEntity;
import fr.ippon.iroco2.calculateur.persistence.repository.entity.CloudServiceProviderRegionEntity;
import fr.ippon.iroco2.calculateur.persistence.repository.CloudServiceProviderRegionRepository;
import fr.ippon.iroco2.domain.commons.exception.NotFoundException;
import fr.ippon.iroco2.config.SecurityTestConfig;
import fr.ippon.iroco2.cucumber.common.SharedMockMvcResult;
import fr.ippon.iroco2.calculateur.presentation.request.InfrastructureRequest;
import fr.ippon.iroco2.calculateur.persistence.repository.entity.InfrastructureEntity;
import fr.ippon.iroco2.calculateur.persistence.repository.InfrastructureRepository;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.assertj.core.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
public class CreateInfrastructureStepDefinitions {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private InfrastructureRepository infrastructureRepository;

    @Autowired
    private CloudServiceProviderRepository cloudServiceProviderRepository;

    @Autowired
    private CloudServiceProviderRegionRepository cloudServiceProviderRegionRepository;

    @Autowired
    private SharedInfrastructureState sharedInfrastructureState;

    @Autowired
    private SharedMockMvcResult mockMvcResult;

    @Given("{string} is logged in as a member")
    public void isAnotherMember(String user) {
        SecurityTestConfig.setupAuthentication(user.toLowerCase() + "@ippon.fr", "ROLE_MEMBER");
    }

    @When("{string} creates a new infrastructure with the following details:")
    @SneakyThrows
    public void createANewInfrastructureWithTheFollowingDetails(String ignored, DataTable dataTable) {
        List<Map<String, String>> data = dataTable.asMaps(String.class, String.class);
        Map<String, String> row = data.getFirst();

        InfrastructureRequest infrastructureRequest = new InfrastructureRequest(
                row.get("name"),
                StringUtils.isBlank(row.get("cloudServiceProvider"))
                        ? null
                        : UUID.fromString(row.get("cloudServiceProvider")),
                StringUtils.isBlank(row.get("defaultCSPRegion")) ? null : UUID.fromString(row.get("defaultCSPRegion"))
        );

        String json = objectMapper.writeValueAsString(infrastructureRequest);

        mockMvcResult.result(
                mockMvc
                        .perform(post("/api/v2/infrastructures").contentType(MediaType.APPLICATION_JSON).content(json))
                        .andDo(MockMvcResultHandlers.print())
        ); // To test
    }

    @SneakyThrows
    @Then("The infrastructure is created for user {string}")
    public void theInfrastructureIsCreated(String user) {
        // HTTP Status
        mockMvcResult.result().andExpect(status().isCreated());
    }

    @SneakyThrows
    @Then("The infrastructure is not created")
    public void theInfrastructureIsNotCreated() {
        long totalInfrastructures = infrastructureRepository.count();
        Assertions.assertThat(totalInfrastructures).isOne();
    }

    @SneakyThrows
    @And("{string} sees the id of the infrastructure")
    public void theResponseShouldContainTheDetailsOfTheCreatedInfrastructureWithAnId(String ignored) {
        mockMvcResult.result().andDo(print()).andExpect(jsonPath("$.id").exists());
    }

    @Given("{string} is not a member")
    public void isNotAMember(String user) {
        SecurityTestConfig.setupAuthentication(user.toLowerCase() + "@ippon.fr", "ROLE_NOT_MEMBER");
    }

    @When("{string} accesses the previously created infrastructure")
    @SneakyThrows
    public void accessesTheInfrastructureWithId(String ignored) {
        mockMvcResult.result(
                mockMvc.perform(get("/api/v2/infrastructures/" + sharedInfrastructureState.infrastructureId()))
        );
    }

    @Given("{string} has an infrastructure with the following details:")
    public void hasAnInfrastructureWithTheFollowingDetails(String user, DataTable dataTable) {
        List<Map<String, String>> data = dataTable.asMaps(String.class, String.class);
        for (Map<String, String> row : data) {
            UUID cloudServiceProvider = StringUtils.isBlank(row.get("cloudServiceProvider"))
                    ? null
                    : UUID.fromString(row.get("cloudServiceProvider"));
            UUID defaultRegion = StringUtils.isBlank(row.get("defaultCSPRegion"))
                    ? null
                    : UUID.fromString(row.get("defaultCSPRegion"));
            if (Objects.nonNull(defaultRegion) && Objects.nonNull(cloudServiceProvider)) {
                CloudServiceProviderEntity cloudServiceProviderEntity = cloudServiceProviderRepository
                        .findById(cloudServiceProvider)
                        .orElseThrow(() -> new NotFoundException("CSP not found"));
                CloudServiceProviderRegionEntity cloudServiceProviderRegionEntity = cloudServiceProviderRegionRepository
                        .findById(defaultRegion)
                        .orElseThrow(() -> new NotFoundException("CSP Region not found"));

                InfrastructureEntity infrastructure = new InfrastructureEntity();
                infrastructure.setName(row.get("name"));
                infrastructure.setCloudServiceProvider(cloudServiceProviderEntity);
                infrastructure.setDefaultRegion(cloudServiceProviderRegionEntity);
                infrastructure.setOwner(user.toLowerCase() + "@ippon.fr");

                InfrastructureEntity infra = infrastructureRepository.save(infrastructure);
                UUID newInfrastructureId = infra.getId();
                sharedInfrastructureState.infrastructureId(
                        newInfrastructureId
                );
            }
        }
    }

    @When("{string} accesses all infrastructures")
    @SneakyThrows
    public void accessesAllInfrastructures(String ignored) {
        mockMvcResult.result(mockMvc.perform(get("/api/v2/infrastructures")));
    }

    @Then("{string} sees {int} infrastructures")
    @SneakyThrows
    public void seesAllInfrastructures(String ignored, int numberOfInfrastructures) {
        mockMvcResult
                .result()
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(numberOfInfrastructures));
    }

    @And("{string} doesn't see the infrastructure anymore")
    public void doesnTSeeTheInfrastructureAnymore(String ignored) {
        Optional<InfrastructureEntity> infra = infrastructureRepository.findById(
                sharedInfrastructureState.infrastructureId()
        );
        Assertions.assertThat(infra).isNotPresent();
    }

    @When("{string} delete the previously created infrastructure")
    @SneakyThrows
    public void deleteThePreviouslyCreatedInfrastructure(String ignored) {
        mockMvcResult.result(
                mockMvc.perform(delete("/api/v2/infrastructures/" + sharedInfrastructureState.infrastructureId()))
        );
    }
}
