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
package fr.ippon.iroco2.common.presentation.security;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

@ExtendWith(MockitoExtension.class)
class SessionProviderAdapterTest {

    private SessionProviderAdapter sessionProvider;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @BeforeEach
    void setUp() {
        sessionProvider = new SessionProviderAdapter();
        SecurityContextHolder.setContext(securityContext);
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void getConnectedAwsAccountId_returns_awsAccountId_when_principal_is_CustomPrincipal() {
        // GIVEN
        CustomPrincipal principal = new CustomPrincipal("user123", "user@iroco.com");
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(principal);

        // WHEN
        String result = sessionProvider.getConnectedAwsAccountId();

        // THEN
        assertThat(result).isEqualTo("user123");
    }

    @Test
    void getConnectedAwsAccountId_throws_when_principal_is_wrong_type() {
        // GIVEN
        User wrongPrincipal = new User("user@iroco.com", "pass", List.of());
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(wrongPrincipal);

        // WHEN/THEN
        assertThrows(ClassCastException.class, () -> sessionProvider.getConnectedAwsAccountId());
    }

    @Test
    void getConnectedAwsAccountId_throws_when_no_authentication() {
        // GIVEN
        when(securityContext.getAuthentication()).thenReturn(null);

        // WHEN/THEN
        assertThrows(NullPointerException.class, () -> sessionProvider.getConnectedAwsAccountId());
    }

    @Test
    void getConnectedUserEmail_returns_email_from_authentication_name() {
        // GIVEN
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("user@iroco.com");

        // WHEN
        String result = sessionProvider.getConnectedUserEmail();

        // THEN
        assertThat(result).isEqualTo("user@iroco.com");
    }

    @Test
    void getConnectedUserEmail_throws_when_no_authentication() {
        // GIVEN
        when(securityContext.getAuthentication()).thenReturn(null);

        // WHEN/THEN
        assertThrows(NullPointerException.class, () -> sessionProvider.getConnectedUserEmail());
    }
}
