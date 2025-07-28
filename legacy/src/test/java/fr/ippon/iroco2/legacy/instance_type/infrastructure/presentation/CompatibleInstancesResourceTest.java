package fr.ippon.iroco2.legacy.instance_type.infrastructure.presentation;

import com.c4_soft.springaddons.security.oauth2.test.annotations.WithMockAuthentication;
import fr.ippon.iroco2.config.TestContainersPostgresqlConfig;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc(addFilters = false)
@WithMockAuthentication(authorities = {"ROLE_MEMBER"})
class CompatibleInstancesResourceTest extends TestContainersPostgresqlConfig {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void should_get_all_and_only_ec2_instance() throws Exception {
        mockMvc.perform(get("/api/awsInstanceType").queryParam("serviceShortName", "EC2"))
                .andExpect(status().isOk()).andExpect(jsonPath("$.length()", CoreMatchers.is(650)));
    }
}