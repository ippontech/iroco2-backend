package fr.ippon.iroco2.scanner.aws_sqs;

import fr.ippon.iroco2.KmsMockConfig;
import fr.ippon.iroco2.S3MockConfig;
import fr.ippon.iroco2.common.aws_sqs.request.ServiceCUR;
import fr.ippon.iroco2.common.aws_sqs.request.ServiceEC2CUR;
import fr.ippon.iroco2.common.aws_sqs.request.ServiceTypeCUR;
import fr.ippon.iroco2.domain.calculateur.model.emu.AWSDataCenter;
import fr.ippon.iroco2.domain.scanner.model.Scan;
import fr.ippon.iroco2.domain.scanner.spi.ScanStorage;
import fr.ippon.iroco2.estimateur.persistence.repository.GlobalEnergyMixRepository;
import fr.ippon.iroco2.estimateur.persistence.repository.entity.GlobalEnergyMixEntity;
import fr.ippon.iroco2.scanner.persistence.repository.ScannerRepository;
import fr.ippon.iroco2.scanner.persistence.repository.entity.ScanEntity;
import io.awspring.cloud.sqs.operations.SqsTemplate;
import org.assertj.core.api.Assertions;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.shaded.org.awaitility.Awaitility;
import org.testcontainers.utility.DockerImageName;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;


@Testcontainers
@SpringBootTest
@Import({ S3MockConfig.class, KmsMockConfig.class })
class ScannerSqsConsumerTest {

    @Container
    private static final LocalStackContainer localStackContainer = new LocalStackContainer(
            DockerImageName.parse("localstack/localstack")
    ).withServices(LocalStackContainer.Service.SQS);

    @Autowired
    private ScannerRepository scannerRepository;

    @SpyBean
    private ScanStorage scanStorage;

    @Autowired
    private GlobalEnergyMixRepository globalEnergyMixRepository;

    @Autowired
    private SqsTemplate sqsTemplate;

    @Value("${aws.sqs.scanner.queue.name}")
    private String queueName;

    @DynamicPropertySource
    static void registerProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.cloud.aws.credentials.access-key", localStackContainer::getAccessKey);
        registry.add("spring.cloud.aws.credentials.secret-key", localStackContainer::getSecretKey);

        registry.add(
                "spring.cloud.aws.sqs.endpoint",
                () -> localStackContainer.getEndpointOverride(LocalStackContainer.Service.SQS).toString()
        );
        registry.add("spring.cloud.aws.sqs.region", localStackContainer::getRegion);
        registry.add("spring.cloud.aws.sqs.enabled", () -> "true");
    }

    private static @NotNull ServiceCUR createEC2CUR(UUID uuid, AWSDataCenter dataCenter) {
        String ec2Type = "t3a.nano";
        Duration duration = Duration.of(5, ChronoUnit.HOURS);
        String correlationId = uuid.toString();
        ServiceTypeCUR serviceTypeCUR = ServiceTypeCUR.EC2;
        Long numberOfMessageExpected = 1L;

        return new ServiceEC2CUR(
                duration,
                dataCenter.getAbbreviation(),
                correlationId,
                serviceTypeCUR,
                numberOfMessageExpected,
                ec2Type
        );
    }

    @Test
    void receivedMessageFromQueue() {
        // GIVEN
        UUID scanId = UUID.randomUUID();
        AWSDataCenter dataCenter = AWSDataCenter.AFRICA_CAPE_TOWN;
        var serviceCUR = createEC2CUR(scanId, dataCenter);

        givenExistingScan(scanId);
        givenExistingGlobalEnergyMix(dataCenter);

        // WHEN
        sqsTemplate.send(to -> to.queue(queueName).payload(serviceCUR));

        // THEN
        Awaitility.await()
                .atMost(5, TimeUnit.SECONDS)
                .pollInterval(100, TimeUnit.MILLISECONDS)
                .untilAsserted(() -> scanHasBeenSaved(scanId));
    }

    private void scanHasBeenSaved(UUID scanId) {
        ArgumentCaptor<Scan> captor = ArgumentCaptor.forClass(Scan.class);
        Mockito.verify(scanStorage).save(captor.capture());
        Assertions.assertThat(captor.getValue()).isNotNull();
        Assertions.assertThat(captor.getValue().getId()).isEqualTo(scanId);
    }

    private void givenExistingGlobalEnergyMix(AWSDataCenter dataCenter) {
        Optional<GlobalEnergyMixEntity> existingMix = globalEnergyMixRepository
                .findByIsoCode(dataCenter.getAssociatedCountryIsoCode());

        if (existingMix.isEmpty()) {
            GlobalEnergyMixEntity mixEntity = new GlobalEnergyMixEntity();
            mixEntity.setIsoCode(dataCenter.getAssociatedCountryIsoCode());
            globalEnergyMixRepository.save(mixEntity);
        }
    }

    private void givenExistingScan(UUID scanId) {
        ScanEntity scanEntity = new ScanEntity();
        scanEntity.setId(scanId);
        scannerRepository.save(scanEntity);
    }

}