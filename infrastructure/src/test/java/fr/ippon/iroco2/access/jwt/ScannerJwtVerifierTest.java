package fr.ippon.iroco2.access.jwt;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import fr.ippon.iroco2.access.aws_kms.AwsKeyManagementService;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.util.Date;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ScannerJwtVerifierTest {

    private ScannerJwtVerifier scannerJwtVerifier;

    @Mock
    private AwsKeyManagementService awsKeyManagementService;

    private final String issuer = "testIssuer";
    private final String audience = "testAudience";

    private RSAPrivateKey privateKey;
    private RSAPrivateKey anotherPrivateKey;

    @BeforeEach
    void setUp() throws NoSuchAlgorithmException, InvalidKeySpecException {
        KeyPair keyPair = generateRSAKeyPair();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        privateKey = (RSAPrivateKey) keyPair.getPrivate();

        KeyPair anotherKeyPair = generateRSAKeyPair();
        anotherPrivateKey = (RSAPrivateKey) anotherKeyPair.getPrivate();

        when(awsKeyManagementService.getPublicKey()).thenReturn(publicKey);

        scannerJwtVerifier = new ScannerJwtVerifier(awsKeyManagementService, issuer, audience);
    }

    private KeyPair generateRSAKeyPair() throws NoSuchAlgorithmException {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(4096);
        return keyGen.generateKeyPair();
    }

    @Test
    void verify_validToken_returns_true() {
        String token = createValidToken();
        boolean result = scannerJwtVerifier.verify(token);
        assertThat(result).isTrue();
    }

    @Test
    void verify_invalidIssuer_returns_false() {
        String token = createTokenWithIssuer("invalidIssuer");
        boolean result = scannerJwtVerifier.verify(token);
        assertThat(result).isFalse();
    }

    @Test
    void verify_invalidAudience_returns_false() {
        String token = createTokenWithAudience("invalidAudience");
        boolean result = scannerJwtVerifier.verify(token);
        assertThat(result).isFalse();
    }

    @Test
    void verify_missingSub_returns_false() {
        String token = createTokenWithoutSub();
        boolean result = scannerJwtVerifier.verify(token);
        assertThat(result).isFalse();
    }

    @Test
    void verify_missingExp_returns_false() {
        String token = createTokenWithoutExp();
        boolean result = scannerJwtVerifier.verify(token);
        assertThat(result).isFalse();
    }

    @Test
    void verify_expiredToken_returns_false() {
        String token = createExpiredToken();
        boolean result = scannerJwtVerifier.verify(token);
        assertThat(result).isFalse();
    }

    @Test
    void verify_expiredButWithinLeeway_returns_true() {
        String token = createTokenExpiredWithinLeeway();
        boolean result = scannerJwtVerifier.verify(token);
        assertThat(result).isTrue();
    }

    @Test
    void verify_invalidSignature_returns_false() {
        String token = createTokenWithInvalidSignature();
        boolean result = scannerJwtVerifier.verify(token);
        assertThat(result).isFalse();
    }

    @Test
    void verify_missingAwsAccountId_returns_false() {
        String token = createTokenWithoutAwsAccountId();
        boolean result = scannerJwtVerifier.verify(token);
        assertThat(result).isFalse();
    }

    @Test
    void verify_malformedToken_returns_false() {
        boolean result = scannerJwtVerifier.verify("invalid.token");
        assertThat(result).isFalse();
    }

    private String createValidToken() {
        Algorithm algorithm = Algorithm.RSA256(null, privateKey);
        return JWT.create()
            .withIssuer(issuer)
            .withAudience(audience)
            .withSubject("testSub")
            .withClaim("aws_account_id", "123456789012")
            .withExpiresAt(new Date(System.currentTimeMillis() + 3600 * 1000))
            .sign(algorithm);
    }

    private String createTokenWithIssuer(String issuer) {
        Algorithm algorithm = Algorithm.RSA256(null, privateKey);
        return JWT.create()
            .withIssuer(issuer)
            .withAudience(audience)
            .withSubject("testSub")
            .withClaim("aws_account_id", "123456789012")
            .withExpiresAt(new Date(System.currentTimeMillis() + 3600 * 1000))
            .sign(algorithm);
    }

    private String createTokenWithAudience(String audience) {
        Algorithm algorithm = Algorithm.RSA256(null, privateKey);
        return JWT.create()
            .withIssuer(issuer)
            .withAudience(audience)
            .withSubject("testSub")
            .withClaim("aws_account_id", "123456789012")
            .withExpiresAt(new Date(System.currentTimeMillis() + 3600 * 1000))
            .sign(algorithm);
    }

    private String createTokenWithoutSub() {
        Algorithm algorithm = Algorithm.RSA256(null, privateKey);
        return JWT.create()
            .withIssuer(issuer)
            .withAudience(audience)
            .withClaim("aws_account_id", "123456789012")
            .withExpiresAt(new Date(System.currentTimeMillis() + 3600 * 1000))
            .sign(algorithm);
    }

    private String createTokenWithoutExp() {
        Algorithm algorithm = Algorithm.RSA256(null, privateKey);
        return JWT.create()
            .withIssuer(issuer)
            .withAudience(audience)
            .withClaim("aws_account_id", "123456789012")
            .withSubject("testSub")
            .sign(algorithm);
    }

    private String createExpiredToken() {
        Algorithm algorithm = Algorithm.RSA256(null, privateKey);
        return JWT.create()
            .withIssuer(issuer)
            .withAudience(audience)
            .withClaim("aws_account_id", "123456789012")
            .withSubject("testSub")
            .withExpiresAt(new Date(System.currentTimeMillis() - 300 * 1000))
            .sign(algorithm);
    }

    private String createTokenExpiredWithinLeeway() {
        Algorithm algorithm = Algorithm.RSA256(null, privateKey);
        return JWT.create()
            .withIssuer(issuer)
            .withAudience(audience)
            .withClaim("aws_account_id", "123456789012")
            .withSubject("testSub")
            .withExpiresAt(new Date(System.currentTimeMillis() - 150 * 1000))
            .sign(algorithm);
    }

    private String createTokenWithInvalidSignature() {
        Algorithm algorithm = Algorithm.RSA256(null, anotherPrivateKey);
        return JWT.create()
            .withIssuer(issuer)
            .withAudience(audience)
            .withClaim("aws_account_id", "123456789012")
            .withSubject("testSub")
            .withExpiresAt(new Date(System.currentTimeMillis() + 3600 * 1000))
            .sign(algorithm);
    }

    private String createTokenWithoutAwsAccountId() {
        Algorithm algorithm = Algorithm.RSA256(null, privateKey);
        return JWT.create()
            .withIssuer(issuer)
            .withAudience(audience)
            .withSubject("testSub")
            .withExpiresAt(new Date(System.currentTimeMillis() + 3600 * 1000))
            .sign(algorithm);
    }
}
