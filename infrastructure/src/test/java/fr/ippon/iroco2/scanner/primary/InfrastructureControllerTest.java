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
package fr.ippon.iroco2.scanner.primary;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.ippon.iroco2.access.primary.JwtAuthenticationFilter;
import fr.ippon.iroco2.access.primary.ScannerAuthenticationFilter;
import fr.ippon.iroco2.calculator.infrastructure.primary.InfrastructureController;
import fr.ippon.iroco2.calculator.infrastructure.primary.InfrastructureRequest;
import fr.ippon.iroco2.domain.calculator.model.CloudServiceProviderRegion;
import fr.ippon.iroco2.domain.calculator.model.CloudServiceProviderService;
import fr.ippon.iroco2.domain.calculator.model.Component;
import fr.ippon.iroco2.domain.calculator.model.Infrastructure;
import fr.ippon.iroco2.domain.calculator.primary.EstimationSvc;
import fr.ippon.iroco2.domain.calculator.primary.InfrastructureSvc;
import fr.ippon.iroco2.domain.commons.secondary.SessionProvider;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = InfrastructureController.class,
        excludeFilters = @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                value = {JwtAuthenticationFilter.class, ScannerAuthenticationFilter.class}
        )
)
@WithMockUser
class InfrastructureControllerTest {
    @Captor
    ArgumentCaptor<Infrastructure> captor;

    @MockBean
    private InfrastructureSvc infrastructureSvc;
    @MockBean
    private EstimationSvc estimationSvc;
    @MockBean
    private SessionProvider sessionProvider;

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void save_should_call_save_service_with_connected_user() throws Exception {
        //given
        UUID cloudServiceProviderId = UUID.randomUUID();
        UUID regionId = UUID.randomUUID();
        InfrastructureRequest infra = new InfrastructureRequest("infra1", cloudServiceProviderId, regionId);
        when(sessionProvider.getConnectedUserEmail()).thenReturn("connected user");

        //when
        ResultActions perform = mockMvc.perform(
                post("/api/v2/infrastructures").content(objectMapper.writeValueAsString(infra))
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON));

        //then
        perform.andExpect(status().isCreated());
        verify(infrastructureSvc).save(captor.capture());
        Infrastructure savedInfra = captor.getValue();
        assertThat(savedInfra.name()).isEqualTo("infra1");
        assertThat(savedInfra.defaultRegionId()).isEqualTo(regionId);
        assertThat(savedInfra.cloudServiceProvider().getId()).isEqualTo(cloudServiceProviderId);
    }

    @Test
    void findById_should_return_given_infra() throws Exception {
        //given
        UUID infraId = UUID.randomUUID();
        Infrastructure infrastructure = mock(Infrastructure.class);
        when(infrastructure.id()).thenReturn(infraId);
        when(infrastructureSvc.findById(infraId)).thenReturn(infrastructure);

        //when
        ResultActions perform = mockMvc.perform(get("/api/v2/infrastructures/" + infraId));

        //then
        perform.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(infraId.toString()));
    }

    @Test
    void estimateCarbonFootprint_should_return_estimation_for_each_component() throws Exception {
        //given
        UUID infraId = UUID.randomUUID();
        Component comp1 = mock(Component.class);
        when(comp1.getName()).thenReturn("comp1");
        when(comp1.getService()).thenReturn(mock(CloudServiceProviderService.class));
        Component comp2 = mock(Component.class);
        when(comp2.getName()).thenReturn("comp2");
        when(comp2.getService()).thenReturn(mock(CloudServiceProviderService.class));
        when(estimationSvc.estimateCarbonFootprintByInfrastructureId(infraId))
                .thenReturn(Map.of(comp1, 1, comp2, 2));

        //when
        ResultActions perform = mockMvc.perform(get("/api/v2/infrastructures/" + infraId + "/carbon-footprint"));

        //then
        perform.andExpect(status().isOk())
                .andExpect(jsonPath("$.[?(@.componentName == 'comp1')].co2Gr").value(1))
                .andExpect(jsonPath("$.[?(@.componentName == 'comp2')].co2Gr").value(2));
    }

    @Test
    void estimateCarbonFootprintForAllRegions_should_return_estimation_for_each_region() throws Exception {
        //given
        UUID infraId = UUID.randomUUID();
        CloudServiceProviderRegion region1 = mock(CloudServiceProviderRegion.class);
        when(region1.getName()).thenReturn("reg1");
        CloudServiceProviderRegion region2 = mock(CloudServiceProviderRegion.class);
        when(region2.getName()).thenReturn("reg2");
        when(estimationSvc.estimateCarbonFootprintByInfrastructureIdForAllRegions(infraId))
                .thenReturn(Map.of(region1, 1, region2, 2));

        //when
        ResultActions perform = mockMvc.perform(get("/api/v2/infrastructures/" + infraId + "/byregion-carbon-footprint"));

        //then
        perform.andExpect(status().isOk())
                .andExpect(jsonPath("$.[?(@.regionName == 'reg1')].co2Gr").value(1))
                .andExpect(jsonPath("$.[?(@.regionName == 'reg2')].co2Gr").value(2));
    }

    @Test
    void delele_should_call_delete_service() throws Exception {
        //given
        UUID infraId = UUID.randomUUID();
        //when
        ResultActions perform = mockMvc.perform(delete("/api/v2/infrastructures/{id}", infraId).with(csrf()));
        //then
        perform.andExpect(status().isOk());
        verify(infrastructureSvc).delete(infraId);
    }

    @Test
    void getInfrastructures_should_return_all_infra() throws Exception {
        //given
        Infrastructure infra1 = mock(Infrastructure.class);
        when(infra1.name()).thenReturn("infra1");
        Infrastructure infra2 = mock(Infrastructure.class);
        when(infra2.name()).thenReturn("infra2");
        when(infrastructureSvc.findAll()).thenReturn(List.of(infra1, infra2));

        //when
        ResultActions perform = mockMvc.perform(get("/api/v2/infrastructures"));

        //then
        perform.andExpect(status().isOk())
                .andExpect(jsonPath("$..name", hasSize(2)))
                .andExpect(jsonPath("$..name", hasItem("infra1")))
                .andExpect(jsonPath("$..name", hasItem("infra2")));
    }
}
