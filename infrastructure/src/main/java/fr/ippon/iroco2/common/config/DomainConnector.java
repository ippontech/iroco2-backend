package fr.ippon.iroco2.common.config;

import fr.ippon.iroco2.domain.commons.DomainService;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

@Configuration
@ComponentScan(
        basePackages = "fr.ippon.iroco2.domain",
        includeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = DomainService.class)
)
public class DomainConnector {
}
