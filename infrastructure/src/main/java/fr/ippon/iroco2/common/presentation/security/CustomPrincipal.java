package fr.ippon.iroco2.common.presentation.security;

import java.security.Principal;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class CustomPrincipal implements Principal {

    @Getter
    private String userId;

    private String email;

    @Override
    public String getName() {
        return this.email;
    }
}
