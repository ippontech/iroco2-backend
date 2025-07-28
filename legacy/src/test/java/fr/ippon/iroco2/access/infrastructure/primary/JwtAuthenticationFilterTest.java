package fr.ippon.iroco2.access.infrastructure.primary;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import fr.ippon.iroco2.access.infrastructure.primary.utils.TestSecurityUtils;
import fr.ippon.iroco2.common.presentation.security.CustomPrincipal;
import fr.ippon.iroco2.config.TestContainersPostgresqlConfig;
import fr.ippon.iroco2.legacy.access.infrastructure.primary.JwtAuthenticationFilter;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

class JwtAuthenticationFilterTest extends TestContainersPostgresqlConfig {

    public static final String PUBLIC_URL = "/api/public/v2/catalog/services";
    public static final String PRIVATE_URL = "/api/cloud-service-providers";

    @Autowired
    private TestSecurityUtils testSecurityUtils;

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
    void public_api_should_return_200_and_must_not_create_user_security_context() throws Exception {
        mockMvc.perform(get(PUBLIC_URL)).andExpect(status().isOk());
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Assertions.assertThat(authentication).isNull();
    }

    @Test
    void secured_api_should_return_200_and_create_user_security_context() throws Exception {
        String token = testSecurityUtils.buildDefaultJWT();
        mockMvc.perform(get(PRIVATE_URL).header("Authorization", "Bearer " + token)).andExpect(status().isOk());

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Assertions.assertThat(authentication).isNotNull();
        Assertions.assertThat(authentication.getPrincipal()).isInstanceOf(CustomPrincipal.class);
        Assertions.assertThat(authentication.getAuthorities().stream().findFirst()).isPresent();
        Assertions.assertThat(((CustomPrincipal) authentication.getPrincipal()).getName()).isEqualTo("max@guinguin.fr");
    }

    @Test
    void bad_role_api_should_return_401_and_must_not_create_user_security_context() throws Exception {
        String token = testSecurityUtils.buildJWTWithRole("BAD_ROLE");
        mockMvc
            .perform(get(PRIVATE_URL).header("Authorization", "Bearer " + token))
            .andExpect(status().isUnauthorized());
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Assertions.assertThat(authentication).isNull();
    }

    @Test
    void no_authorization_header_api_should_return_401_and_must_not_create_user_security_context() throws Exception {
        var result = mockMvc.perform(get(PRIVATE_URL));

        result
            .andExpect(status().is(HttpStatus.UNAUTHORIZED.value()))
            .andExpect(jsonPath("$.message").value("[SECURITY] - Authorization header is blank [value = 'null']"));
    }

    @Test
    void token_who_does_not_start_with_bearer_should_return_401_and_must_not_create_user_security_context()
        throws Exception {
        String token = "BAD TOKEN";
        mockMvc.perform(get(PRIVATE_URL).header("Authorization", token)).andExpect(status().isUnauthorized());
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Assertions.assertThat(authentication).isNull();
    }
}
