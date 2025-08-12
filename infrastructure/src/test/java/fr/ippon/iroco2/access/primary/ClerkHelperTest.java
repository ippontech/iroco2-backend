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

import com.auth0.jwt.interfaces.DecodedJWT;
import fr.ippon.iroco2.common.TestSecurityUtils;
import fr.ippon.iroco2.config.TestAwsConfig;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.EnabledIf;

import java.security.spec.InvalidKeySpecException;
import java.time.Instant;
import java.util.Base64;
import java.util.stream.Stream;

@SpringBootTest
@Import(TestAwsConfig.class)
@EnabledIf(expression = "#{environment['clerk.secret.key'] != ''}", loadContext = true)
class ClerkHelperTest {
    @Autowired
    ClerkHelper clerkHelper;
    @Autowired
    TestSecurityUtils testSecurityUtils;
    @Value("${clerk.public.key}")
    private String clerkPublicKey;

    static Stream<Arguments> bad_audience_data() {
        return Stream.of(Arguments.of("bad-audience"), Arguments.of(""), Arguments.of("   "));
    }

    static Stream<Arguments> bad_issuer_data() {
        return Stream.of(Arguments.of("bad-issuer"), Arguments.of(""), Arguments.of("   "));
    }

    @Test
    void valid_public_key_and_footer_should_not_throw_exception() {
        Assertions.assertThatCode(() -> clerkHelper.getClerkPublicKey(clerkPublicKey)).doesNotThrowAnyException();
    }

    @Test
    void not_base64_public_key_should_throw_illegalArgumentException() {
        Assertions.assertThatCode(() -> clerkHelper.getClerkPublicKey("bad public key ééééééé")).isInstanceOf(
                IllegalArgumentException.class
        );
    }

    @Test
    void bad_base64_public_key_should_throw_invalidKeySpecException() {
        Assertions.assertThatCode(
                () -> clerkHelper.getClerkPublicKey(Base64.getEncoder().encodeToString("bad key".getBytes()))
        ).isInstanceOf(InvalidKeySpecException.class);
    }

    @Test
    void no_none_value_in_alg_claim_header_should_not_throw_exception() {
        DecodedJWT decodedJWT = testSecurityUtils.getDecodedJWT(false);
        Assertions.assertThatCode(() -> clerkHelper.checkHeader(decodedJWT)).doesNotThrowAnyException();
    }

    @Test
    void none_value_in_alg_claim_header_should_not_throw_exception() {
        DecodedJWT decodedJWT = testSecurityUtils.getDecodedJWT(true);
        Assertions.assertThatCode(() -> clerkHelper.checkHeader(decodedJWT)).isInstanceOf(
                IrocoAuthenticationException.class
        );
    }

    @ParameterizedTest
    @MethodSource("bad_audience_data")
    void bad_audience_claim_should_throw_custom_security_exception(String audience) {
        String token = testSecurityUtils.buildJWTWithAudience(audience);
        Assertions.assertThatCode(() -> clerkHelper.getVerifiedDecodedJWT(token)).isInstanceOf(
                IrocoAuthenticationException.class
        );
    }

    @Test
    void null_audience_claim_should_throw_custom_security_exception() {
        String token = testSecurityUtils.buildJWTWithAudience(null);
        Assertions.assertThatCode(() -> clerkHelper.getVerifiedDecodedJWT(token)).isInstanceOf(
                IrocoAuthenticationException.class
        );
    }

    @Test
    void correct_audience_claim_should_not_throw_exception() {
        String token = testSecurityUtils.buildJWTWithAudience("audience-test");
        Assertions.assertThatCode(() -> clerkHelper.getVerifiedDecodedJWT(token)).doesNotThrowAnyException();
    }

    @ParameterizedTest
    @MethodSource("bad_issuer_data")
    void bad_issuer_claim_should_throw_custom_security_exception(String issuer) {
        String token = testSecurityUtils.buildJWTWithIssuer(issuer);
        Assertions.assertThatCode(() -> clerkHelper.getVerifiedDecodedJWT(token)).isInstanceOf(
                IrocoAuthenticationException.class
        );
    }

    @Test
    void null_issuer_claim_should_throw_custom_security_exception() {
        String token = testSecurityUtils.buildJWTWithIssuer(null);
        Assertions.assertThatCode(() -> clerkHelper.getVerifiedDecodedJWT(token)).isInstanceOf(
                IrocoAuthenticationException.class
        );
    }

    @Test
    void correct_issuer_claim_should_not_throw_exception() {
        String token = testSecurityUtils.buildJWTWithIssuer("issuer-test");
        Assertions.assertThatCode(() -> clerkHelper.getVerifiedDecodedJWT(token)).doesNotThrowAnyException();
    }

    @Test
    void valid_exp_claim_should_not_throw_exception() {
        String token = testSecurityUtils.buildJWTWithExp(Instant.MAX);
        Assertions.assertThatCode(() -> clerkHelper.getVerifiedDecodedJWT(token)).doesNotThrowAnyException();
    }

    @Test
    void correct_email_claim_should_not_throw_exception() {
        String token = testSecurityUtils.buildJWTWithEmail("max@guinguin.fr");
        Assertions.assertThatCode(() -> clerkHelper.getVerifiedDecodedJWT(token)).doesNotThrowAnyException();
    }

    @Test
    void correct_role_claim_should_not_throw_exception() {
        String token = testSecurityUtils.buildJWTWithRole(SecurityRole.MEMBER.name());
        Assertions.assertThatCode(() -> clerkHelper.getVerifiedDecodedJWT(token)).doesNotThrowAnyException();
    }
}
