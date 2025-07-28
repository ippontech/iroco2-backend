package fr.ippon.iroco2.access.presentation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.c4_soft.springaddons.security.oauth2.test.annotations.WithMockAuthentication;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.ippon.iroco2.S3MockConfig;
import fr.ippon.iroco2.access.presentation.request.GenerateTokenRequest;
import java.util.regex.Pattern;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.kms.KmsClient;
import software.amazon.awssdk.services.kms.model.CreateKeyRequest;
import software.amazon.awssdk.services.kms.model.KeySpec;
import software.amazon.awssdk.services.kms.model.KeyUsageType;

@Testcontainers
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@WithMockAuthentication(authorities = { "ROLE_MEMBER" }, name = ScannerJwtControllerTest.USER_EMAIL)
@Import(S3MockConfig.class)
class ScannerJwtControllerTest {

    public static final String USER_EMAIL = "user@email.com";
    private static final String JWT_PATTERN = "^[A-Za-z0-9-_=]+\\.[A-Za-z0-9-_=]+\\.?[A-Za-z0-9-_.+/=]*$";
    private static final Pattern jwtPattern = Pattern.compile(JWT_PATTERN);

    @Container
    private static final LocalStackContainer localStackContainer = new LocalStackContainer(
        DockerImageName.parse("localstack/localstack")
    ).withServices(LocalStackContainer.Service.KMS);

    @Autowired
    public ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @DynamicPropertySource
    static void registerProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.cloud.aws.credentials.access-key", localStackContainer::getAccessKey);
        registry.add("spring.cloud.aws.credentials.secret-key", localStackContainer::getSecretKey);
        registry.add(
            "spring.cloud.aws.kms.endpoint",
            () -> localStackContainer.getEndpointOverride(LocalStackContainer.Service.KMS).toString()
        );
        registry.add("spring.cloud.aws.kms.region", localStackContainer::getRegion);
        registry.add("spring.cloud.aws.kms.enabled", () -> "true");

        KmsClient kmsClient = createKmsClient();
        registry.add("aws.kms.key-arn", () -> createKmsKey(kmsClient));
    }

    @Test
    void should_return_jwt_token() throws Exception {
        // GIVEN
        var request = new GenerateTokenRequest("123456789012", 3600);

        // WHEN
        var result = mockMvc.perform(
            MockMvcRequestBuilders.post("/api/v1/token/generate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        );

        // THEN
        final String responseContent = result.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        assertThat(responseContent).as("Response should be a valid JWT format").isNotEmpty().matches(jwtPattern);
    }

    private static KmsClient createKmsClient() {
        return KmsClient.builder()
            .endpointOverride(localStackContainer.getEndpointOverride(LocalStackContainer.Service.KMS))
            .credentialsProvider(
                StaticCredentialsProvider.create(
                    AwsBasicCredentials.create(localStackContainer.getAccessKey(), localStackContainer.getSecretKey())
                )
            )
            .region(Region.of(localStackContainer.getRegion()))
            .build();
    }

    private static String createKmsKey(KmsClient kmsClient) {
        return kmsClient
            .createKey(CreateKeyRequest.builder().keySpec(KeySpec.RSA_4096).keyUsage(KeyUsageType.SIGN_VERIFY).build())
            .keyMetadata()
            .keyId();
    }
}
