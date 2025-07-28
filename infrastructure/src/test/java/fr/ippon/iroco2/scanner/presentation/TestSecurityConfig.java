package fr.ippon.iroco2.scanner.presentation;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@TestConfiguration
public class TestSecurityConfig {

    @Bean
    public SecurityFilterChain testSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(
                        authorizeRequests ->
                                authorizeRequests
                                        .requestMatchers("/api/public/**", "/actuator/health", "/actuator/info").permitAll()
                                        .requestMatchers("/api/**").hasAnyRole("ADMIN", "MEMBER")
                                        .requestMatchers("/actuator/**").hasRole("ADMIN")
                                        .anyRequest()
                                        .authenticated()
                )
                // Désactiver le filtre JWT en test
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(Customizer.withDefaults());

        return http.build();
    }
}
