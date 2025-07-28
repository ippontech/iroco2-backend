package fr.ippon.iroco2.access.infrastructure.primary;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import fr.ippon.iroco2.access.infrastructure.primary.utils.TestSecurityUtils;
import fr.ippon.iroco2.config.TestContainersPostgresqlConfig;
import fr.ippon.iroco2.legacy.access.domain.SecurityRole;
import fr.ippon.iroco2.legacy.access.infrastructure.primary.JwtAuthenticationFilter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

class PreAuthorizeTest extends TestContainersPostgresqlConfig {

    @Autowired
    TestSecurityUtils testSecurityUtils;

    @Autowired
    WebApplicationContext context;

    MockMvc mockMvc;

    @Autowired
    JwtAuthenticationFilter jwtAuthenticationFilter;

    @BeforeEach
    void beforeEach() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
            .apply(SecurityMockMvcConfigurers.springSecurity(jwtAuthenticationFilter))
            .build();
    }

    @Test
    void admin_pre_authorize_should_return_200_when_user_has_admin_role() throws Exception {
        String token = testSecurityUtils.buildJWTWithRole(SecurityRole.ADMIN.name());
        mockMvc
            .perform(get("/api/fakes/admin-api").header("Authorization", "Bearer " + token))
            .andExpect(status().isOk());
    }

    @Test
    void admin_member_pre_authorize_should_return_200_when_user_has_admin_role() throws Exception {
        String token = testSecurityUtils.buildJWTWithRole(SecurityRole.ADMIN.name());
        mockMvc
            .perform(get("/api/fakes/member-api").header("Authorization", "Bearer " + token))
            .andExpect(status().isOk());
    }

    @Test
    void member_pre_authorize_should_return_200_when_user_has_member_role() throws Exception {
        String token = testSecurityUtils.buildJWTWithRole(SecurityRole.MEMBER.name());
        mockMvc
            .perform(get("/api/fakes/member-api").header("Authorization", "Bearer " + token))
            .andExpect(status().isOk());
    }

    @Test
    void admin_pre_authorize_should_return_403_when_user_has_member_role() throws Exception {
        String token = testSecurityUtils.buildJWTWithRole(SecurityRole.MEMBER.name());
        mockMvc
            .perform(get("/api/fakes/admin-api").header("Authorization", "Bearer " + token))
            .andExpect(status().isForbidden());
    }
}
