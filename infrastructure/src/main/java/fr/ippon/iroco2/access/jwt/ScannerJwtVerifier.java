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

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ScannerJwtVerifier {

    private final AwsKeyManagementService awsKeyManagementService;
    private final String issuer;
    private final String audience;

    public ScannerJwtVerifier(
            AwsKeyManagementService awsKeyManagementService,
            @Value("${jwt.issuer}") String issuer,
            @Value("${jwt.audience}") String audience
    ) {
        this.awsKeyManagementService = awsKeyManagementService;
        this.issuer = issuer;
        this.audience = audience;
    }

    public boolean verify(final String token) {
        try {
            Algorithm algorithm = Algorithm.RSA256(awsKeyManagementService.getPublicKey(), null);
            JWT.require(algorithm)
                    .acceptLeeway(200)
                    .withClaimPresence("exp")
                    .withClaimPresence("sub")
                    .withClaimPresence("aws_account_id")
                    .withIssuer(issuer)
                    .withAudience(audience)
                    .build()
                    .verify(token);
            return true;
        } catch (Exception e) {
            log.error("Token verification failed: {}", e.getMessage());
            return false;
        }
    }
}
