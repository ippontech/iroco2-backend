package fr.ippon.iroco2.cucumber.common;

import io.cucumber.spring.ScenarioScope;
import jakarta.inject.Singleton;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.ResultActions;

@Component
@ScenarioScope //is reset after each scenario
@Accessors(chain = true, fluent = true)
@Getter
@Setter
@Singleton
public class SharedMockMvcResult {

    private ResultActions result;
}
