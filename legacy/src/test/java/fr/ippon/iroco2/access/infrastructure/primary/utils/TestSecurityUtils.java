package fr.ippon.iroco2.access.infrastructure.primary.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import fr.ippon.iroco2.legacy.access.domain.SecurityRole;
import java.security.KeyFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class TestSecurityUtils {

    @Value("${clerk.public.key}")
    private String clerkPublicKey;

    @Value("${clerk.secret.key}")
    private String clerkSecretKey;

    public DecodedJWT getDecodedJWT(boolean algoIsNone) {
        Algorithm algorithm = Algorithm.none();
        if (!algoIsNone) {
            final RSAPublicKey rsaPublicKey = this.getPublicKeyFromString(clerkPublicKey);
            final RSAPrivateKey rsaPrivateKey = this.getPrivateKeyFromString(clerkSecretKey);
            algorithm = Algorithm.RSA256(rsaPublicKey, rsaPrivateKey);
        }

        String token = JWT.create()
            .withIssuer("issuer-test")
            .withAudience("audience-test")
            .withClaim("email", "max@guinguin.fr")
            .withClaim("role", SecurityRole.MEMBER.name())
            .withIssuedAt(new Date())
            .withExpiresAt(new Date(System.currentTimeMillis() + 500000L))
            .withJWTId(UUID.randomUUID().toString())
            .sign(algorithm);

        JWTVerifier verifier = JWT.require(algorithm).build();
        return verifier.verify(token);
    }

    public String buildDefaultJWT() {
        return getJWTCreatorBuilder().sign(defaultAlgorithm());
    }

    public String buildJWTWithAudience(String audience) {
        var builder = getJWTCreatorBuilder();
        builder.withAudience(audience);
        return builder.sign(defaultAlgorithm());
    }

    public String buildJWTWithIssuer(String issuer) {
        var builder = getJWTCreatorBuilder();
        builder.withIssuer(issuer);
        return builder.sign(defaultAlgorithm());
    }

    public String buildJWTWithExp(Instant instant) {
        var builder = getJWTCreatorBuilder();
        builder.withExpiresAt(instant);
        return builder.sign(defaultAlgorithm());
    }

    public String buildJWTWithEmail(String email) {
        var builder = getJWTCreatorBuilder();
        builder.withClaim("email", email);
        return builder.sign(defaultAlgorithm());
    }

    public String buildJWTWithRole(String role) {
        var builder = getJWTCreatorBuilder();
        builder.withClaim("role", role);
        return builder.sign(defaultAlgorithm());
    }

    public Algorithm defaultAlgorithm() {
        final RSAPublicKey rsaPublicKey = this.getPublicKeyFromString(clerkPublicKey);
        final RSAPrivateKey rsaPrivateKey = this.getPrivateKeyFromString(clerkSecretKey);
        return Algorithm.RSA256(rsaPublicKey, rsaPrivateKey);
    }

    public JWTCreator.Builder getJWTCreatorBuilder() {
        return JWT.create()
            .withIssuer("issuer-test")
            .withAudience("audience-test")
            .withClaim("email", "max@guinguin.fr")
            .withClaim("role", "MEMBER")
            .withIssuedAt(new Date())
            .withExpiresAt(new Date(System.currentTimeMillis() + 500000L))
            .withJWTId(UUID.randomUUID().toString());
    }

    @SneakyThrows
    public RSAPublicKey getPublicKeyFromString(String publicKey) {
        byte[] encoded = Base64.getDecoder().decode(publicKey);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(encoded);
        return (RSAPublicKey) keyFactory.generatePublic(keySpec);
    }

    @SneakyThrows
    public RSAPrivateKey getPrivateKeyFromString(String privateKey) {
        byte[] encoded = Base64.getDecoder().decode(privateKey);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(encoded);
        return (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
    }

    public String buildScannerToken() {
        return JWT.create()
            .withIssuer("issuer-test")
            .withAudience("audience-test")
            .withSubject("subject-test")
            .withIssuedAt(Instant.now())
            .withExpiresAt(Instant.now().plusSeconds(60))
            .withClaim("aws_account_id", "123456789012")
            .sign(defaultAlgorithm());
    }
}
