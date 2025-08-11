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
package fr.ippon.iroco2.access.primary;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.KeyFactory;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/**
 * Références pour les bonnes pratiques de sécurité des JWT :
 *
 * <p>1. <a href="https://www.pingidentity.com/fr/resources/blog/post/jwt-security-nobody-talks-about.html">Ping Identity - JWT Security</a>
 * - Un article détaillant les aspects de sécurité souvent négligés des JWT.</p>
 *
 * <p>2. <a href="https://curity.io/resources/learn/jwt-best-practices/">Curity - JWT Best Practices</a>
 * - Guide complet sur les meilleures pratiques pour utiliser les JWT en toute sécurité.</p>
 *
 * <p>3. <a href="https://owasp.org/www-project-web-security-testing-guide/latest/4-Web_Application_Security_Testing/06-Session_Management_Testing/10-Testing_JSON_Web_Tokens">OWASP - Testing JSON Web Tokens</a>
 * - Guide pour tester la sécurité des JSON Web Tokens dans les applications web.</p>
 */
@Component
@Slf4j
public class ClerkHelper {

    @Value("${clerk.public.key}")
    private String clerkPublicKey;

    @Value("${clerk.issuer}")
    private String clerkIssuer;

    @Value("${clerk.audience}")
    private String clerkAudience;

    public DecodedJWT getVerifiedDecodedJWT(String token) throws IrocoAuthenticationException {
        DecodedJWT decodedJWT;
        try {
            Algorithm algorithm = Algorithm.RSA256(getClerkPublicKey(clerkPublicKey), null);
            JWTVerifier verifier = JWT.require(algorithm)
                    // 1s leeway of validity for windows user
                    .acceptLeeway(200)
                    .withClaimPresence("exp")
                    .withClaimPresence("email")
                    .withClaimPresence("role")
                    .withIssuer(clerkIssuer)
                    .withAudience(clerkAudience)
                    .build();
            decodedJWT = verifier.verify(token);
        } catch (JWTVerificationException exception) {
            log.error(exception.getMessage(), exception);
            throw new IrocoAuthenticationException("[SECURITY] - The signature of the token is not valid");
        }
        return decodedJWT;
    }

    public void checkHeader(DecodedJWT decodedJWT) throws IrocoAuthenticationException {
        final String alg = decodedJWT.getHeaderClaim("alg").asString();
        if ("NONE".equalsIgnoreCase(alg)) {
            throw new IrocoAuthenticationException("[SECURITY] - The alg claim header is equals to 'NONE'");
        }
    }

    @SneakyThrows
    public RSAPublicKey getClerkPublicKey(String publicKey) {
        byte[] publicBytes = Base64.getDecoder().decode(publicKey.getBytes());
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return (RSAPublicKey) keyFactory.generatePublic(keySpec);
    }
}
