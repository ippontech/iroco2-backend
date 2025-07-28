package fr.ippon.iroco2;

import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import software.amazon.awssdk.services.kms.KmsClient;

@TestConfiguration
public class KmsMockConfig {

    @Bean
    public KmsClient kmsClient() {
        return Mockito.mock(KmsClient.class);
    }
}
