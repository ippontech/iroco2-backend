package fr.ippon.iroco2.legacy.access.infrastructure.primary;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import fr.ippon.iroco2.access.jwt.ScannerJwtVerifier;
import fr.ippon.iroco2.common.presentation.security.CustomPrincipal;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

@Slf4j
@Component
public class ScannerAuthenticationFilter extends OncePerRequestFilter {

    private final ScannerJwtVerifier scannerJwtVerifier;
    private final HandlerExceptionResolver handlerExceptionResolver;

    public ScannerAuthenticationFilter(
        ScannerJwtVerifier scannerJwtVerifier,
        @Qualifier("handlerExceptionResolver") HandlerExceptionResolver handlerExceptionResolver
    ) {
        this.scannerJwtVerifier = scannerJwtVerifier;
        this.handlerExceptionResolver = handlerExceptionResolver;
    }

    @SneakyThrows
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) {
        String authHeader = request.getHeader("Authorization");
        try {
            if (!isValidHeader(authHeader)) {
                throw new IrocoAuthenticationException(
                    "[SECURITY] - Invalid authorization header [value = %s]".formatted(authHeader)
                );
            }

            String token = authHeader.substring(7);

            if (!scannerJwtVerifier.verify(token)) {
                throw new IrocoAuthenticationException("[SECURITY] - Token verification failed");
            }

            setupAuthentication(token);
            filterChain.doFilter(request, response);
        } catch (IrocoAuthenticationException ex) {
            log.error("Authentication error: {}", ex.getMessage());
            handlerExceptionResolver.resolveException(request, response, null, ex);
        }
    }

    private boolean isValidHeader(String authHeader) {
        return StringUtils.isNotBlank(authHeader) && authHeader.startsWith("Bearer ");
    }

    private void setupAuthentication(String token) {
        DecodedJWT decodedJWT = JWT.decode(token);
        CustomPrincipal principal = new CustomPrincipal(
            decodedJWT.getClaim("aws_account_id").asString(),
            decodedJWT.getSubject()
        );

        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(principal, null, null);

        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return !new AntPathRequestMatcher("/api/scanner", "POST").matches(request);
    }
}
