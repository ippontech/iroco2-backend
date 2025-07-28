package fr.ippon.iroco2.cucumber.infrastructure;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import fr.ippon.iroco2.cucumber.common.SharedMockMvcResult;
import fr.ippon.iroco2.calculateur.persistence.repository.ComponentRepository;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.util.UUID;
import lombok.SneakyThrows;
import org.assertj.core.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class DeleteComponentStepDefinitions {

    @Autowired
    private ComponentRepository componentRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SharedComponentState sharedComponentState;

    @Autowired
    private SharedMockMvcResult mockMvcResult;

    @When("{string} delete the component with id {string}")
    @SneakyThrows
    public void deleteComponent(String ignore, String componentID) {
        sharedComponentState.componentId(UUID.fromString(componentID));
        mockMvcResult.result(mockMvc.perform(delete("/api/v2/components/" + sharedComponentState.componentId())));
    }

    @Then("{string} does not see the component anymore")
    public void doesNotSeeTheComponentAnymore(String ignore) {
        Assertions.assertThat(componentRepository.findById(sharedComponentState.componentId())).isEmpty();
    }

    @Then("{string} receives a client error")
    @SneakyThrows
    public void receivesAClientError(String ignore) {
        mockMvcResult.result().andExpect(status().is4xxClientError());
    }
}
