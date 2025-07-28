package fr.ippon.iroco2.access.jwt;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import fr.ippon.iroco2.access.aws_kms.AwsKeyManagementService;
import java.util.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

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
