package fr.ippon.iroco2.legacy.access.infrastructure.primary;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import fr.ippon.iroco2.access.infrastructure.primary.utils.TestSecurityUtils;
import fr.ippon.iroco2.access.jwt.ScannerJwtVerifier;
import fr.ippon.iroco2.common.presentation.security.CustomPrincipal;
import fr.ippon.iroco2.config.TestContainersPostgresqlConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.HandlerExceptionResolver;

class ScannerAuthenticationFilterTest extends TestContainersPostgresqlConfig {

    public static final String PUBLIC_URL = "/api/public/v2/catalog/services";
    public static final String PRIVATE_URL = "/api/scanner";

    @Autowired
    private TestSecurityUtils testSecurityUtils;

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @Mock
    private ScannerJwtVerifier scannerJwtVerifier;

    @Autowired
    private HandlerExceptionResolver handlerExceptionResolver;

    @BeforeEach
    void beforeEach() {
        ScannerAuthenticationFilter scannerAuthenticationFilter = new ScannerAuthenticationFilter(
            scannerJwtVerifier,
            handlerExceptionResolver
        );
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
            .apply(SecurityMockMvcConfigurers.springSecurity(scannerAuthenticationFilter))
            .build();
    }

    @Test
    void public_api_should_return_200_and_must_not_create_user_security_context() throws Exception {
        mockMvc.perform(get(PUBLIC_URL)).andExpect(status().isOk());
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assertThat(authentication).isNull();
    }

    @Test
    void scanner_api_should_return_200_and_must_create_user_security_context() throws Exception {
        String token = testSecurityUtils.buildScannerToken();
        when(scannerJwtVerifier.verify(token)).thenReturn(true);
        mockMvc
            .perform(
                post(PRIVATE_URL)
                    .header("Authorization", "Bearer " + token)
            )
            .andExpect(status().isCreated());

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assertThat(authentication).isNotNull();
        assertThat(authentication.getPrincipal()).isInstanceOf(CustomPrincipal.class);
        assertThat(((CustomPrincipal) authentication.getPrincipal()))
            .usingRecursiveComparison()
            .isEqualTo(new CustomPrincipal("123456789012", "subject-test"));
    }

    @Test
    void no_authorization_header_api_should_return_401_and_must_not_create_user_security_context() throws Exception {
        var result = mockMvc.perform(post(PRIVATE_URL));

        result
            .andExpect(status().is(HttpStatus.UNAUTHORIZED.value()))
            .andExpect(jsonPath("$.message").value("[SECURITY] - Invalid authorization header [value = null]"));
    }

    @Test
    void token_who_does_not_start_with_bearer_should_return_401_and_must_not_create_user_security_context()
        throws Exception {
        String token = "BAD TOKEN";
        mockMvc.perform(post(PRIVATE_URL).header("Authorization", token)).andExpect(status().isUnauthorized());
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assertThat(authentication).isNull();
    }
}
