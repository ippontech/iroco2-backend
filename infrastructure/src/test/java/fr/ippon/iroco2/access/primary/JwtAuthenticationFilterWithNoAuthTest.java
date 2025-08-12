package fr.ippon.iroco2.access.primary;

import fr.ippon.iroco2.config.TestContainersPostgresqlConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("no-auth")
class JwtAuthenticationFilterWithNoAuthTest extends TestContainersPostgresqlConfig {
    private static final String PUBLIC_URL = "/api/public/v2/catalog/services";
    private static final String PRIVATE_URL = "/api/cloud-service-providers";

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @BeforeEach
    void beforeEach() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(SecurityMockMvcConfigurers.springSecurity(jwtAuthenticationFilter))
                .build();
    }

    @Test
    void no_auth_should_access_public_url() throws Exception {
        mockMvc.perform(get(PUBLIC_URL)).andExpect(status().isOk());
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assertThat(authentication).isNull();
    }

    @Test
    void no_auth_should_access_private_url_with_demo_user() throws Exception {
        //when
        ResultActions resultActions = mockMvc.perform(get(PRIVATE_URL).header("Authorization", "Bearer "));
        //then
        resultActions.andExpect(status().isOk());
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assertThat(authentication).isNotNull();
        CustomPrincipal principal = (CustomPrincipal) authentication.getPrincipal();
        assertThat(principal.getUserId()).isEqualTo("demo");
        assertThat(principal.getName()).isEqualTo("demo@ippon.fr");
        assertThat(authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority)).containsExactly("ROLE_ADMIN");
    }
}
