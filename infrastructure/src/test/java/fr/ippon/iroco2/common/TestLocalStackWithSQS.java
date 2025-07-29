package fr.ippon.iroco2.common;

import fr.ippon.iroco2.KmsMockConfig;
import fr.ippon.iroco2.S3MockConfig;
import io.awspring.cloud.sqs.operations.SqsTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import static org.testcontainers.containers.localstack.LocalStackContainer.Service.SQS;

@Testcontainers
@SpringBootTest
@Import({S3MockConfig.class, KmsMockConfig.class})
public abstract class TestLocalStackWithSQS {
    @Container
    private static final LocalStackContainer LOCALSTACK =
            new LocalStackContainer(DockerImageName.parse("localstack/localstack"))
                    .withServices(SQS);

    @Autowired
    protected SqsTemplate sqsTemplate;

    @DynamicPropertySource
    private static void registerProperties(DynamicPropertyRegistry registry) {
        LOCALSTACK.start();
        registry.add("spring.cloud.aws.credentials.access-key", LOCALSTACK::getAccessKey);
        registry.add("spring.cloud.aws.credentials.secret-key", LOCALSTACK::getSecretKey);
        registry.add("spring.cloud.aws.sqs.endpoint", () -> LOCALSTACK.getEndpointOverride(SQS).toString());
        registry.add("spring.cloud.aws.sqs.region", LOCALSTACK::getRegion);
        registry.add("spring.cloud.aws.sqs.enabled", () -> "true");
    }
}
