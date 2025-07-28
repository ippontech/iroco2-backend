package fr.ippon.iroco2.cucumber.catalog;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import fr.ippon.iroco2.cucumber.common.SharedMockMvcResult;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class CatalogStepsDefinitions {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SharedMockMvcResult mockMvcResult;

    @SneakyThrows
    @When("{string} requests all services from catalog")
    public void requestsAllServicesFromCatalog(String ignore) {
        mockMvcResult.result(
            mockMvc.perform(
                MockMvcRequestBuilders.get("/api/public/v2/catalog/services").accept(MediaType.APPLICATION_JSON)
            )
        );
    }

    @SneakyThrows
    @Then("{string} should receive a list of all the services of all the providers")
    public void shouldReceiveAListOfAllTheServicesOfAllTheProviders(String ignore) {
        mockMvcResult
            .result()
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(jsonPath("$.length()").value(1));
    }

    @SneakyThrows
    @When("{string} requests the description of the service {string}")
    public void requestsTheDescriptionOfTheService(String ignore, String serviceId) {
        mockMvcResult.result(
            mockMvc.perform(
                MockMvcRequestBuilders.get("/api/public/v2/catalog/services/%s".formatted(serviceId)).accept(
                    MediaType.APPLICATION_JSON
                )
            )
        );
    }

    @SneakyThrows
    @Then("{string} should receive the description of the service {string}")
    public void shouldReceiveTheDescriptionOfTheService(String ignore, String serviceId) {
        mockMvcResult
            .result()
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(content().json("{id: %s}".formatted(serviceId)));
    }

    @SneakyThrows
    @Then("{string} is informed that the ressource does not exist")
    public void isInformedThatTheRessourceDoesNotExist(String ignore) {
        mockMvcResult.result().andExpect(MockMvcResultMatchers.status().isNotFound());
    }
}
