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
package fr.ippon.iroco2.access.primary;

import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.util.Arrays;
import java.util.List;

import static fr.ippon.iroco2.access.primary.SecurityRole.ADMIN;

@Component
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final ClerkHelper clerkHelper;
    private final HandlerExceptionResolver handlerExceptionResolver;
    private final boolean authActivate;

    public JwtAuthenticationFilter(ClerkHelper clerkHelper, HandlerExceptionResolver handlerExceptionResolver, @Value("${iroco2.authentication.activate}") boolean authActivate) {
        this.clerkHelper = clerkHelper;
        this.handlerExceptionResolver = handlerExceptionResolver;
        this.authActivate = authActivate;
    }

    private static void shouldHaveBearer(String header) {
        if (!header.startsWith("Bearer ")) {
            throw new IrocoAuthenticationException("[SECURITY] - Authorization header doesn't start with 'Bearer ' [value = '%s']".formatted(header));
        }
    }

    private static void shouldNotBeEmpty(String header) {
        if (StringUtils.isBlank(header)) {
            throw new IrocoAuthenticationException("[SECURITY] - Authorization header is blank [value = '%s']".formatted(header));
        }
    }

    private static void addAuthDemoInContext() {
        CustomPrincipal principal = new CustomPrincipal("demo", "demo@ippon.fr");
        List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(ADMIN.getAuthority()));
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(principal, null, authorities);
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    @SneakyThrows
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) {
        String requestHeaderAuth = request.getHeader("Authorization");
        try {
            shouldNotBeEmpty(requestHeaderAuth);
            shouldHaveBearer(requestHeaderAuth);

            log.debug("authentication activated? {}", authActivate);
            if (authActivate) {
                addClerkAuthDataFromTokenInContext(requestHeaderAuth);
            } else {
                addAuthDemoInContext();
            }

            filterChain.doFilter(request, response);
        } catch (IrocoAuthenticationException ex) {
            log.error(ex.getMessage());
            handlerExceptionResolver.resolveException(request, response, null, ex);
        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return new OrRequestMatcher(
                new AntPathRequestMatcher("/api/public/**"),
                new AntPathRequestMatcher("/actuator/health"),
                new AntPathRequestMatcher("/actuator/info"),
                new AntPathRequestMatcher("/api/scanner", "POST")
        ).matches(request);
    }

    private void addClerkAuthDataFromTokenInContext(String requestHeaderAuth) throws IrocoAuthenticationException {
        final String token = requestHeaderAuth.substring(7);
        DecodedJWT decodedJWT = clerkHelper.getVerifiedDecodedJWT(token);
        clerkHelper.checkHeader(decodedJWT);

        String userId = decodedJWT.getSubject();
        String email = decodedJWT.getClaim("email").asString();
        String roleInJWT = decodedJWT.getClaim("role").asString().toUpperCase();

        SecurityRole role = Arrays.stream(SecurityRole.values())
                .filter(r -> r.name().equals(roleInJWT))
                .findFirst()
                .orElseThrow(
                        () -> {
                            var msg = "[SECURITY] - Role '%s' invalid. Valid roles: %s".formatted(roleInJWT, Arrays.toString(SecurityRole.values()));
                            return new IrocoAuthenticationException(msg);
                        }
                );

        List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(role.getAuthority()));

        CustomPrincipal principal = new CustomPrincipal(userId, email);
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                principal,
                null,
                authorities
        );
        SecurityContextHolder.getContext().setAuthentication(auth);
    }
}
