package fr.ippon.iroco2.scanner.presentation;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.ippon.iroco2.KmsMockConfig;
import fr.ippon.iroco2.common.persistance.entity.EstimatedPayloadEntity;
import fr.ippon.iroco2.domain.commons.exception.NotFoundException;
import fr.ippon.iroco2.domain.commons.model.ReportStatus;
import fr.ippon.iroco2.domain.scanner.api.ScannerSvc;
import fr.ippon.iroco2.scanner.persistence.repository.ScannerRepository;
import fr.ippon.iroco2.scanner.persistence.repository.entity.ScanEntity;
import fr.ippon.iroco2.scanner.presentation.response.ScanListElementResponse;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import software.amazon.awssdk.annotations.NotNull;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Testcontainers
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false) // Désactive les filtres de sécurité
@Transactional
@Import({KmsMockConfig.class, TestSecurityConfig.class})
class ScannerControllerTest {

    public static final String USER_EMAIL = "user@email.com";
    public static final int DEFAULT_TEST_CARBON_GRAM_FOOTPRINT = 1569874;
    public static final String DEFAULT_TEST_PAYLOAD_NAME = "serviceName EC2";
    public static final String AWS_ACCOUNT_ID = "0123456789012";
    @Container
    private static final LocalStackContainer localStackContainer = new LocalStackContainer(
            DockerImageName.parse("localstack/localstack")
    ).withServices(LocalStackContainer.Service.S3);
    private final UUID scanId = UUID.randomUUID();
    @Autowired
    public ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ScannerSvc scannerSvc;
    private String validToken;
    @Autowired
    private ScannerRepository scannerRepository;
    @Autowired
    private WebApplicationContext webApplicationContext;

    @DynamicPropertySource
    static void registerProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.cloud.aws.credentials.access-key", localStackContainer::getAccessKey);
        registry.add("spring.cloud.aws.credentials.secret-key", localStackContainer::getSecretKey);

        registry.add(
                "spring.cloud.aws.s3.endpoint",
                () -> localStackContainer.getEndpointOverride(LocalStackContainer.Service.S3).toString()
        );
        registry.add("spring.cloud.aws.s3.region", localStackContainer::getRegion);
        registry.add("spring.cloud.aws.s3.enabled", () -> "true");
    }

    @BeforeEach
    void setUp() {
        validToken = JWT.create()
                .withClaim("aws_account_id", "123456789012")
                .withSubject("testUser")
                .withIssuedAt(new Date())
                .sign(Algorithm.none());
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    void should_return_201_and_uuid_when_valid_request() throws Exception {
        // GIVEN
        when(scannerSvc.create()).thenReturn(scanId);

        // WHEN & THEN
        mockMvc
                .perform(MockMvcRequestBuilders.post("/api/scanner").header("Authorization", "Bearer " + validToken))
                .andExpect(status().isCreated())
                .andExpect(content().string("\"" + scanId + "\""));
    }

    @Test
    void should_return_list_with_scans_when_connected_user_has_scans() throws Exception {
        // GIVEN
        EstimatedPayloadEntity payloadEntity = givenExistingScanWithPayload();
        var scan = payloadEntity.getScan().toDomain();

        // WHEN
        when(scannerSvc.findAll()).thenReturn(List.of(scan));
        var result = mockMvc.perform(MockMvcRequestBuilders.get("/api/scanner"));

        // THEN
        result
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(scan.getId().toString()))
                .andExpect(jsonPath("$[0].status").value(scan.getStatus().toString()))
                .andExpect(jsonPath("$[0].co2Gr").value(DEFAULT_TEST_CARBON_GRAM_FOOTPRINT))
                .andReturn();
    }

    @Test
    void should_return_empty_list_when_connected_user_has_no_scans() throws Exception {
        // WHEN
        var result = mockMvc.perform(MockMvcRequestBuilders.get("/api/scanner"));

        // THEN
        result.andExpect(status().isOk()).andReturn();

        var parsedResult = readJSonResponse(result);
        assertThat(parsedResult).isEmpty();
    }

    private List<ScanListElementResponse> readJSonResponse(ResultActions result) throws Exception {
        return objectMapper.readValue(result.andReturn().getResponse().getContentAsString(), new TypeReference<>() {
        });
    }

    @Test
    void should_delete_scan_by_id() throws Exception {
        // GIVEN
        EstimatedPayloadEntity payloadEntity = givenExistingScanWithPayload();
        var scan = payloadEntity.getScan();

        // WHEN
        var result = mockMvc.perform(delete("/api/scanner/" + scan.getId()));

        // THEN
        result.andExpect(status().isOk());
    }

    @Test
    void should_throw_an_exception_if_scan_does_not_exist() throws Exception {
        // GIVEN
        UUID scanId = UUID.randomUUID();
        doThrow(new NotFoundException("Not found")).when(scannerSvc).delete(scanId);

        // WHEN
        var result = mockMvc.perform(delete("/api/scanner/" + scanId));

        // THEN
        result.andExpect(status().isNotFound());
    }

    @Test
    void should_return_scan_when_connected_user_is_owner() throws Exception {
        // GIVEN
        EstimatedPayloadEntity payloadEntity = givenExistingScanWithPayload();
        var scan = payloadEntity.getScan().toDomain();

        // WHEN
        when(scannerSvc.findById(scan.getId())).thenReturn(scan);
        var result = mockMvc.perform(MockMvcRequestBuilders.get("/api/scanner/" + scan.getId().toString()));

        // THEN
        result
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(scan.getId().toString()))
                .andExpect(jsonPath("$.status").value(scan.getStatus().toString()))
                .andExpect(jsonPath("$.payloads").isArray())
                .andExpect(jsonPath("$.payloads[0].carbonGramFootprint").value(payloadEntity.getCarbonGramFootprint()))
                .andExpect(jsonPath("$.payloads[0].name").value(payloadEntity.getName()));
    }

    private @NotNull EstimatedPayloadEntity givenExistingScanWithPayload() {
        ScanEntity entity = new ScanEntity();
        entity.setId(UUID.randomUUID());
        entity.setStatus(ReportStatus.IN_PROGRESS);
        entity.setOwner(ScannerControllerTest.USER_EMAIL);
        entity.setAwsAccountId(ScannerControllerTest.AWS_ACCOUNT_ID);
        entity.setCreationDate(LocalDateTime.now());
        entity.setPayloads(new ArrayList<>());

        EstimatedPayloadEntity payload = new EstimatedPayloadEntity();
        payload.setId(UUID.randomUUID());
        payload.setScan(entity);
        payload.setCarbonGramFootprint(DEFAULT_TEST_CARBON_GRAM_FOOTPRINT);
        payload.setName(DEFAULT_TEST_PAYLOAD_NAME);

        entity.getPayloads().add(payload);
        scannerRepository.save(entity);
        return payload;
    }
}
