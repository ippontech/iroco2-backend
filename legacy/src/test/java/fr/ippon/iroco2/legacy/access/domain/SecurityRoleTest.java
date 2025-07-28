package fr.ippon.iroco2.legacy.access.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class SecurityRoleTest {

    @ParameterizedTest
    @MethodSource("roleProvider")
    void securityRoles_maps_to_correct_authority(SecurityRole role, String expectedAuthority) {
        assertThat(role.getAuthority()).isEqualTo(expectedAuthority);
    }

    private static Stream<Arguments> roleProvider() {
        return Stream.of(
            Arguments.of(SecurityRole.ADMIN, "ROLE_ADMIN"),
            Arguments.of(SecurityRole.MEMBER, "ROLE_MEMBER")
        );
    }
}
