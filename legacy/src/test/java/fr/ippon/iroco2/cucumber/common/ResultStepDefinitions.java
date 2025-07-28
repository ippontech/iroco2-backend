package fr.ippon.iroco2.cucumber.common;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import fr.ippon.iroco2.cucumber.infrastructure.SharedInfrastructureState;
import io.cucumber.java.en.Then;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;

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
