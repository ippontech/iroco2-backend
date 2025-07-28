package fr.ippon.iroco2.access.jwt;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

public record JwtTokenPayload(
    Instant iat,
    Instant exp,
    String iss,
    String aud,
    String sub,
    @JsonProperty(AWS_ACCOUNT_ID) String awsAccountId
) {
    private static final String AWS_ACCOUNT_ID = "aws_account_id";
    public JwtTokenPayload(long expireInSeconds, String issuer, String audience, String subject, String awsAccountId) {
        this(
            Instant.now().truncatedTo(ChronoUnit.SECONDS),
            Instant.now().plusSeconds(expireInSeconds).truncatedTo(ChronoUnit.SECONDS),
            issuer,
            audience,
            subject,
            awsAccountId
        );
    }
}
