package fr.ippon.iroco2.legacy.access.infrastructure.primary;

import com.auth0.jwt.interfaces.DecodedJWT;
import fr.ippon.iroco2.common.presentation.security.CustomPrincipal;
import fr.ippon.iroco2.legacy.access.domain.SecurityRole;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

@Component
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final ClerkHelper clerkHelper;
    private final HandlerExceptionResolver handlerExceptionResolver;

    public JwtAuthenticationFilter(
        ClerkHelper clerkHelper,
        @Qualifier("handlerExceptionResolver") HandlerExceptionResolver handlerExceptionResolver
    ) {
        this.clerkHelper = clerkHelper;
        this.handlerExceptionResolver = handlerExceptionResolver;
    }

    @SneakyThrows
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) {
        String requestHeader = request.getHeader("Authorization");
        try {
            if (StringUtils.isBlank(requestHeader)) {
                throw new IrocoAuthenticationException(
                    "[SECURITY] - Authorization header is blank [value = '%s']".formatted(requestHeader)
                );
            }

            if (!requestHeader.startsWith("Bearer ")) {
                throw new IrocoAuthenticationException(
                    "[SECURITY] - Authorization header doesn't start with 'Bearer ' [value = '%s']".formatted(
                            requestHeader
                        )
                );
            }

            final String token = requestHeader.substring(7);

            DecodedJWT decodedJWT = clerkHelper.getVerifiedDecodedJWT(token);
            clerkHelper.checkHeader(decodedJWT);

            this.initAuthentication(decodedJWT);

            filterChain.doFilter(request, response);
        } catch (IrocoAuthenticationException ex) {
            log.error(ex.getMessage());
            handleSecurityException(request, response, ex);
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

    private void initAuthentication(DecodedJWT decodedJWT) throws IrocoAuthenticationException {
        String userId = decodedJWT.getSubject();
        String email = decodedJWT.getClaim("email").asString();
        String roleInJWT = decodedJWT.getClaim("role").asString().toUpperCase();

        SecurityRole role = Arrays.stream(SecurityRole.values())
            .filter(r -> r.name().equals(roleInJWT))
            .findFirst()
            .orElseThrow(
                () ->
                    new IrocoAuthenticationException(
                        "[SECURITY] - Role '%s' invalid. Valid roles: %s".formatted(
                                roleInJWT,
                                Arrays.toString(SecurityRole.values())
                            )
                    )
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

    private void handleSecurityException(HttpServletRequest request, HttpServletResponse response, Exception ex) {
        handlerExceptionResolver.resolveException(request, response, null, ex);
    }
}
