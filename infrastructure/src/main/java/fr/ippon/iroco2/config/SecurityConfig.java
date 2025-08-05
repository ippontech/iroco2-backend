/*
 * Copyright 2025 Ippon Technologies
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package fr.ippon.iroco2.config;

import fr.ippon.iroco2.access.presentation.JwtAuthenticationFilter;
import fr.ippon.iroco2.access.presentation.ScannerAuthenticationFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@Slf4j
public class SecurityConfig {
    @Autowired
    private ScannerAuthenticationFilter scannerAuthenticationFilter;
    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;
    @Value("${iroco2.authentication.activate}")
    private boolean authActivate;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        log.debug("authentication activated? {}", authActivate);
        return
                authActivate ?
                        http
                                .csrf(AbstractHttpConfigurer::disable)
                                .addFilterBefore(scannerAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                                .authorizeHttpRequests(authorize ->
                                        authorize
                                                .requestMatchers("/api/public/**", "/actuator/health", "/actuator/info").permitAll()
                                                .requestMatchers(HttpMethod.POST, "/api/scanner").authenticated()
                                                .requestMatchers("/api/**").hasAnyRole("ADMIN", "MEMBER")
                                                .requestMatchers("/actuator/**").hasRole("ADMIN")
                                                .anyRequest().authenticated()
                                )
                                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                                .exceptionHandling(withDefaults())
                                .build()
                        : http.authorizeHttpRequests(authorize -> authorize.anyRequest().permitAll())
                        .build();
    }
}
