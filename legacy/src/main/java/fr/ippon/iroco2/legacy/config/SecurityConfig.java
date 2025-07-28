package fr.ippon.iroco2.legacy.config;

import fr.ippon.iroco2.legacy.access.infrastructure.primary.JwtAuthenticationFilter;
import fr.ippon.iroco2.legacy.access.infrastructure.primary.ScannerAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final ScannerAuthenticationFilter scannerAuthenticationFilter;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(
        ScannerAuthenticationFilter scannerAuthenticationFilter,
        JwtAuthenticationFilter jwtAuthenticationFilter
    ) {
        this.scannerAuthenticationFilter = scannerAuthenticationFilter;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .addFilterBefore(scannerAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
            .authorizeHttpRequests(
                authorize ->
                    authorize
                        .requestMatchers("/api/public/**", "/actuator/health", "/actuator/info")
                        .permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/scanner")
                        .authenticated()
                        .requestMatchers("/api/**")
                        .hasAnyRole("ADMIN", "MEMBER")
                        .requestMatchers("/actuator/**")
                        .hasRole("ADMIN")
                        .anyRequest()
                        .authenticated()
            )
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .exceptionHandling(Customizer.withDefaults());

        return http.build();
    }
}
