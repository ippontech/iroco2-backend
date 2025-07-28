package fr.ippon.iroco2.cucumber.infrastructure;

import io.cucumber.spring.ScenarioScope;
import jakarta.inject.Singleton;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@ScenarioScope //is reset after each scenario
@Accessors(chain = true, fluent = true)
@Getter
@Setter
@Singleton
public class SharedComponentState {

    private UUID componentId;
}
