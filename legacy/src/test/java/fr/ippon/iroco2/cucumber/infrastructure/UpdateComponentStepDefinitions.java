package fr.ippon.iroco2.cucumber.infrastructure;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.ippon.iroco2.cucumber.common.SharedMockMvcResult;
import fr.ippon.iroco2.calculateur.presentation.request.ComponentRequest;
import fr.ippon.iroco2.calculateur.presentation.request.ConfigurationValueRequest;
import fr.ippon.iroco2.calculateur.persistence.repository.entity.ComponentEntity;
import fr.ippon.iroco2.calculateur.persistence.repository.ComponentRepository;
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
import java.util.Optional;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;

@SpringBootTest
@Transactional
public class UpdateComponentStepDefinitions {

    @Autowired
    private ObjectMapper objectMapper;


    @Autowired
    private SharedComponentState sharedComponentState;

    @Autowired
    private SharedMockMvcResult mockMvcResult;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ComponentRepository componentRepository;

    @Given("{string} has the component on this infrastructure with the ID {string}")
    public void hasTheComponentWithTheFollowingDetails(String ignored, String componentID) {
        sharedComponentState.componentId(UUID.fromString(componentID));
    }

    @SneakyThrows
    @When("{string} changes the name of the component to {string}")
    public void changesTheNameOfItsComponentWithIdTo(String ignored, String newName) {
        updateComponent(newName, null, null);
    }

    @SneakyThrows
    @When("{string} changes the region of the component to the region with ID {string}")
    public void changesTheRegionOfTheComponentToTheRegionWithID(String ignore, String newRegionID) {
        updateComponent(null, newRegionID, null);
    }

    @SneakyThrows
    @When("{string} tries to change the name of a non-existent component to {string}")
    public void triesToChangeTheNameOfANonExistentComponentTo(String ignore, String newName) {
        updateComponent(newName, null, "37fb80f0-0483-4347-9ec2-f7155b6ac5bd");
    }

    @Then("the component has now the name {string}")
    public void theComponentHasNowTheName(String expectedName) {
        Optional<ComponentEntity> componentEntity = componentRepository.findById(sharedComponentState.componentId());
        Assertions.assertThat(componentEntity).isPresent();
        Assertions.assertThat(componentEntity.get().getName()).isEqualTo(expectedName);
    }

    @Then("the component has now the region {string}")
    public void theComponentHasNowTheRegion(String expectedRegion) {
        Optional<ComponentEntity> componentEntity = componentRepository.findById(sharedComponentState.componentId());
        Assertions.assertThat(componentEntity).isPresent();
        Assertions.assertThat(componentEntity.get().getCspRegion().getName()).isEqualTo(expectedRegion);
    }

    private void updateComponent(String newName, String newRegionID, String nonExistentComponentID) throws Exception {
        List<ComponentEntity> components = componentRepository.findAll();
        ComponentEntity component = components.getFirst();

        ComponentRequest request = new ComponentRequest(
                StringUtils.isNotBlank(nonExistentComponentID) ? UUID.fromString(nonExistentComponentID) : component.getId(),
                component.getInfrastructure().getId(),
                StringUtils.isNotBlank(newName) ? newName : component.getName(),
                StringUtils.isNotBlank(newRegionID) ? UUID.fromString(newRegionID) : component.getCspRegion().getId(),
                component.getService().getId(),
                component.getConfiguredSettings().stream()
                        .map(
                                setting -> new ConfigurationValueRequest(
                                        setting.getValue(),
                                        setting.getConfigurationSetting().getId()
                                )
                        ).toList()
        );

        String json = objectMapper.writeValueAsString(request);

        var result = mockMvc.perform(patch("/api/v2/components").contentType(MediaType.APPLICATION_JSON).content(json));

        mockMvcResult.result(result.andDo(MockMvcResultHandlers.print()));
    }
}
