package fr.ippon.iroco2.access.aws_kms;

import static org.assertj.core.api.Assertions.assertThat;

import fr.ippon.iroco2.KmsMockConfig;
import fr.ippon.iroco2.S3MockConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@EnableConfigurationProperties(KmsProperties.class)
@TestPropertySource(
    properties = {
        "spring.cloud.aws.kms.region=us-east-1",
        "spring.cloud.aws.kms.endpoint=http://localhost:4566",
        "spring.cloud.aws.kms.profile=iroco-dev",
    }
)
@Import({ S3MockConfig.class, KmsMockConfig.class })
class KmsPropertiesTest {

    @Autowired
    private KmsProperties kmsProperties;

    @Test
    void should_bind_region() {
        assertThat(kmsProperties.getRegion()).isEqualTo("us-east-1");
    }

    @Test
    void should_bind_endpoint() {
        assertThat(kmsProperties.getEndpoint()).hasToString("http://localhost:4566");
    }

    @Test
    void should_bind_profile() {
        assertThat(kmsProperties.getProfile()).isEqualTo("iroco-dev");
    }
}
