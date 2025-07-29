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
