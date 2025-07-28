package fr.ippon.iroco2.cucumber.catalog;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import fr.ippon.iroco2.cucumber.common.SharedMockMvcResult;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class CloudServiceProviderStepsDefinitions {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SharedMockMvcResult resultActions;

    @When("{string} requests all Cloud Service Providers")
    public void bobRequestsAllCloudServiceProviders(String ignore) throws Exception {
        resultActions.result(
            mockMvc
                .perform(MockMvcRequestBuilders.get("/api/cloud-service-providers").accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
        );
    }

    @When("{string} requests all Regions for the Cloud Service Provider with ID {string}")
    public void bobRequestsAllRegionsForCloudServiceProvider(String ignore, String cspId) throws Exception {
        resultActions.result(
            mockMvc
                .perform(
                    MockMvcRequestBuilders.get("/api/cloud-service-providers/" + cspId + "/regions").accept(
                        MediaType.APPLICATION_JSON
                    )
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
        );
    }

    @When("{string} requests all Services for the Cloud Service Provider with ID {string}")
    public void bobRequestsAllServicesForCloudServiceProvider(String ignore, String cspId) throws Exception {
        resultActions.result(
            mockMvc
                .perform(
                    MockMvcRequestBuilders.get("/api/cloud-service-providers/" + cspId + "/services").accept(
                        MediaType.APPLICATION_JSON
                    )
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
        );
    }

    @Then("{string} should receive a list of Cloud Service Providers")
    public void bobShouldReceiveAListOfCloudServiceProviders(String ignore) throws Exception {
        resultActions.result().andExpect(status().isOk()).andExpect(jsonPath("$.length()").value(1));
    }

    @Then("{string} should receive a list of Regions for that Cloud Service Provider")
    public void bobShouldReceiveAListOfRegionsForThatCloudServiceProvider(String ignore) throws Exception {
        resultActions.result().andExpect(status().isOk()).andExpect(jsonPath("$.length()").value(2));
    }

    @Then("{string} should receive a list of Services for that Cloud Service Provider")
    public void bobShouldReceiveAListOfServicesForThatCloudServiceProvider(String ignore) throws Exception {
        resultActions.result().andExpect(status().isOk()).andExpect(jsonPath("$.length()").value(1));
    }
}
