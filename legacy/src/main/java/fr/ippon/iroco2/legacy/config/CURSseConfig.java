package fr.ippon.iroco2.legacy.config;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Configuration
public class CURSseConfig {

    @Bean(name = "correlationIdCURSseEmitterMap")
    @Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
    public ConcurrentMap<String, SseEmitter> correlationIdCURSseEmitterMap() {
        return new ConcurrentHashMap<>();
    }
}
