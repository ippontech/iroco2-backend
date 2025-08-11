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
package fr.ippon.iroco2.access.jwt;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ScannerJwtGeneratorTest {

    private final String issuer = "issuer";
    private final String audience = "audience";
    private final String testEmail = "user@example.com";
    private final String testAwsAccountId = "123456789012";
    private final long testExpiration = 3600L;
    private ScannerJwtGenerator scannerJwtGenerator;
    @Mock
    private AwsKeyManagementService awsKeyManagementService;
    @Captor
    private ArgumentCaptor<String> unsignedTokenCaptor;

    @BeforeEach
    void setupSecurityContext() {
        Authentication auth = new UsernamePasswordAuthenticationToken(testEmail, "password");
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(auth);
        SecurityContextHolder.setContext(context);

        scannerJwtGenerator = new ScannerJwtGenerator(awsKeyManagementService, issuer, audience);
    }

    @Test
    void generateJwt_returns_valid_three_part_token() throws Exception {
        // Arrange
        byte[] mockSignature = "mocked-signature".getBytes(StandardCharsets.UTF_8);
        when(awsKeyManagementService.sign(anyString())).thenReturn(mockSignature);

        // Act
        String jwt = scannerJwtGenerator.generate(testAwsAccountId, testExpiration);

        // Assert
        String[] parts = jwt.split("\\.");
        assertThat(parts).hasSize(3);
    }

    @Test
    void generateJwt_contains_valid_header() throws Exception {
        // Arrange
        byte[] mockSignature = "signature".getBytes();
        when(awsKeyManagementService.sign(anyString())).thenReturn(mockSignature);

        // Act
        String jwt = scannerJwtGenerator.generate(testAwsAccountId, testExpiration);
        String header = new String(Base64.getUrlDecoder().decode(jwt.split("\\.")[0]));

        // Assert
        assertThat(header).contains("\"alg\":\"RS256\"", "\"typ\":\"JWT\"");
    }

    @Test
    void generateJwt_contains_valid_payload() throws Exception {
        // Arrange
        byte[] mockSignature = "signature".getBytes();
        when(awsKeyManagementService.sign(anyString())).thenReturn(mockSignature);

        // Act
        String jwt = scannerJwtGenerator.generate(testAwsAccountId, testExpiration);
        String payload = new String(Base64.getUrlDecoder().decode(jwt.split("\\.")[1]));

        // Assert
        assertThat(payload)
                .contains("\"iss\":\"" + issuer + "\"")
                .contains("\"aud\":\"" + audience + "\"")
                .contains("\"sub\":\"" + testEmail + "\"")
                .contains("\"aws_account_id\":\"" + testAwsAccountId + "\"");
    }

    @Test
    void generateJwt_signs_correct_unsigned_token() throws Exception {
        // Arrange
        byte[] mockSignature = "signature".getBytes();
        when(awsKeyManagementService.sign(anyString())).thenReturn(mockSignature);

        // Act
        scannerJwtGenerator.generate(testAwsAccountId, testExpiration);

        // Assert
        verify(awsKeyManagementService).sign(unsignedTokenCaptor.capture());
        String unsignedToken = unsignedTokenCaptor.getValue();
        String[] parts = unsignedToken.split("\\.");
        assertThat(parts).hasSize(2);
    }

    @Test
    void generateJwt_throws_when_no_authentication() {
        // Arrange
        SecurityContextHolder.clearContext();

        // Act & Assert
        assertThrows(NullPointerException.class, () -> scannerJwtGenerator.generate(testAwsAccountId, testExpiration));
    }

    @Test
    void generateJwt_signature_is_base64_url_encoded() throws Exception {
        // Arrange
        byte[] mockSignature = "signature+with/special~chars".getBytes();
        String expectedEncoded = Base64.getUrlEncoder().withoutPadding().encodeToString(mockSignature);
        when(awsKeyManagementService.sign(anyString())).thenReturn(mockSignature);

        // Act
        String jwt = scannerJwtGenerator.generate(testAwsAccountId, testExpiration);
        String signaturePart = jwt.split("\\.")[2];

        // Assert
        assertThat(signaturePart).isEqualTo(expectedEncoded);
    }
}
