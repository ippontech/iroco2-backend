package fr.ippon.iroco2.cucumber;

import fr.ippon.iroco2.config.TestContainersPostgresqlConfig;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

@CucumberContextConfiguration
@AutoConfigureMockMvc(addFilters = false)
public class CucumberSpringConfiguration extends TestContainersPostgresqlConfig {

}
