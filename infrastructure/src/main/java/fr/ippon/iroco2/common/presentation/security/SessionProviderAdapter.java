package fr.ippon.iroco2.common.presentation.security;

import fr.ippon.iroco2.domain.commons.spi.SessionProvider;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SessionProviderAdapter implements SessionProvider {

    public String getConnectedAwsAccountId() {
        return ((CustomPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUserId();
    }

    public String getConnectedUserEmail() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
}
