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
package fr.ippon.iroco2.cucumber.common;

import fr.ippon.iroco2.cucumber.infrastructure.SharedInfrastructureState;
import io.cucumber.java.en.Then;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
public class ResultStepDefinitions {

    @Autowired
    private SharedMockMvcResult mockMvcResult;

    @Autowired
    private SharedInfrastructureState sharedInfrastructureState;

    //403
    @SneakyThrows
    @Then("{string} is informed that it is not allowed the infrastructure with the ID {string}")
    public void isInformedThatIsNotAllowed(String ignore, String infrastructureId) {
        String message = "You don't have the right to access this infrastructure";
        mockMvcResult.result().andExpect(status().is(HttpStatus.FORBIDDEN.value())).andExpect(jsonPath("$.message").value(message));
    }

    //401
    @SneakyThrows
    @Then("{string} is informed that the permission to {string} is not granted")
    public void theResponseShouldHaveAStatusForbidden(String ignored0, String ignored1) {
        mockMvcResult.result().andExpect(status().isForbidden());
    }

    //400
    @SneakyThrows
    @Then("{string} is informed that he performed a bad request")
    public void theResponseShouldHaveAStatusBADREQUEST(String ignored) {
        mockMvcResult.result().andExpect(status().isBadRequest());
    }

    //404
    @SneakyThrows
    @Then("{string} is informed that there are missing ressources")
    public void theResponseShouldHaveAStatusNOTFOUND(String ignored) {
        mockMvcResult.result().andExpect(status().isNotFound());
    }

    //204
    @Then("{string} sees that {string} was already deleted")
    @SneakyThrows
    public void seesThatWasAlreadyDeleted(String ignored0, String ignored1) {
        var result = mockMvcResult.result();
        String message = String.format("L'infrastructure d'id '%s' n'existe pas", sharedInfrastructureState.infrastructureId());
        result.andExpect(status().is(HttpStatus.NOT_FOUND.value())).andExpect(jsonPath("$.message").value(message));
    }

    //200
    @Then("{string} sees that {string} was successful")
    @SneakyThrows
    public void seesThatWasSuccessful(String ignored0, String ignored1) {
        mockMvcResult.result().andExpect(status().isOk());
    }

    //201
    @SneakyThrows
    @Then("{string} sees that {string} was created")
    public void seesThatWasCreated(String ignored0, String ignored1) {
        mockMvcResult.result().andExpect(status().isCreated());
    }
}
