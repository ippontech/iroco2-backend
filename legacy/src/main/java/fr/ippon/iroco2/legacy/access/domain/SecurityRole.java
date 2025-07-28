package fr.ippon.iroco2.legacy.access.domain;

import lombok.Getter;

@Getter
public enum SecurityRole {
    ADMIN("ROLE_ADMIN"),
    MEMBER("ROLE_MEMBER");

    private final String authority;

    SecurityRole(String authority) {
        this.authority = authority;
    }
}
