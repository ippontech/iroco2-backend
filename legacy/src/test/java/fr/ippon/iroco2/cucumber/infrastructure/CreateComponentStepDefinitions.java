package fr.ippon.iroco2.cucumber.infrastructure;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.ippon.iroco2.cucumber.common.SharedMockMvcResult;
import fr.ippon.iroco2.calculateur.presentation.request.ComponentRequest;
import fr.ippon.iroco2.calculateur.presentation.request.ConfigurationValueRequest;
import fr.ippon.iroco2.calculateur.persistence.repository.entity.ComponentEntity;
import fr.ippon.iroco2.calculateur.persistence.repository.ComponentRepository;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.SneakyThrows;
import org.assertj.core.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@Transactional
public class CreateComponentStepDefinitions {

    @Autowired
    SharedMockMvcResult mockMvcResult;
    @Autowired
    private SharedInfrastructureState sharedInfrastructureState;
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ComponentRepository componentRepository;

    @SneakyThrows
    @Then("the component is saved")
    public void theComponentIsSavedSuccessfully() {
        // DB
        List<ComponentEntity> components = componentRepository.findByInfrastructureId(sharedInfrastructureState.infrastructureId());
        Assertions.assertThat(components).hasSize(1);
    }

    @SneakyThrows
    @When("{string} creates the component for the service of id {string} with the following configuration:")
    public void createsTheComponentForTheServiceOfIdWithTheFollowingConfiguration(
            String ignored,
            String serviceID,
            DataTable dataTable
    ) {
        Map<String, String> data = dataTable.asMap(String.class, String.class);
        String name = data.get("name");
        String configurationValuesJson = data.get("configurationValues");
        List<ConfigurationValueRequest> configurationValues = objectMapper.readValue(
                configurationValuesJson,
                new TypeReference<>() {
                }
        );

        ComponentRequest componentRequest = new ComponentRequest(
                null,
                sharedInfrastructureState.infrastructureId(),
                name,
                UUID.fromString("123e4567-e89b-12d3-a456-426614174001"),
                UUID.fromString(serviceID),
                configurationValues
        );

        String json = objectMapper.writeValueAsString(componentRequest);

        mockMvcResult.result(
                mockMvc
                        .perform(post("/api/v2/components").contentType(MediaType.APPLICATION_JSON).content(json))
                        .andDo(MockMvcResultHandlers.print())
        );

    }

    @And("the component is not saved")
    public void theComponentIsNotSaved() {
        List<ComponentEntity> componentEntities = componentRepository.findByInfrastructureId(sharedInfrastructureState.infrastructureId());
        Assertions.assertThat(componentEntities).isEmpty();
    }
}
