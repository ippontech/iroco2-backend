package fr.ippon.iroco2.config;

import fr.ippon.iroco2.LegacyTestConfig;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest
@ActiveProfiles(value = "test-containers-postgres")
@Testcontainers
@Import(LegacyTestConfig.class)
public abstract class TestContainersPostgresqlConfig {
}
