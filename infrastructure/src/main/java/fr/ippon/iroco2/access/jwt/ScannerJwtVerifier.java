package fr.ippon.iroco2.access.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import fr.ippon.iroco2.access.aws_kms.AwsKeyManagementService;
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
