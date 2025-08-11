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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Base64;

@Component
public class ScannerJwtGenerator {

    private final ObjectMapper objectMapper;
    private final AwsKeyManagementService awsKeyManagementService;
    private final String issuer;
    private final String audience;

    public ScannerJwtGenerator(
            AwsKeyManagementService awsKeyManagementService,
            @Value("${jwt.issuer}") String issuer,
            @Value("${jwt.audience}") String audience
    ) {
        this.objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
        this.awsKeyManagementService = awsKeyManagementService;
        this.issuer = issuer;
        this.audience = audience;
    }

    public String generate(final String awsAccountId, final long expireInSeconds) throws JsonProcessingException {
        final JwtTokenHeader header = createJwtTokenHeader();
        final JwtTokenPayload payload = createJwtTokenPayload(expireInSeconds, awsAccountId);

        final String unsignedToken = String.format(
                "%s.%s",
                encodeBase64Url(objectMapper.writeValueAsBytes(header)),
                encodeBase64Url(objectMapper.writeValueAsBytes(payload))
        );
        final byte[] signature = awsKeyManagementService.sign(unsignedToken);
        final String encodedSignature = encodeBase64Url(signature);

        return String.format("%s.%s", unsignedToken, encodedSignature);
    }

    private JwtTokenHeader createJwtTokenHeader() {
        return new JwtTokenHeader("RS256", "JWT");
    }

    private JwtTokenPayload createJwtTokenPayload(final long expireInSeconds, final String awsAccountId) {
        final String email = getAuthenticatedUserEmail();
        return new JwtTokenPayload(expireInSeconds, issuer, audience, email, awsAccountId);
    }

    private String getAuthenticatedUserEmail() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    private String encodeBase64Url(final byte[] object) {
        return Base64.getUrlEncoder().withoutPadding().encodeToString(object);
    }
}
