package fr.ippon.iroco2.legacy.access.infrastructure.primary;

import org.springframework.security.core.AuthenticationException;

public class IrocoAuthenticationException extends AuthenticationException {

    public IrocoAuthenticationException(String message) {
        super(message);
    }
}
