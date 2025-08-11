package fr.ippon.iroco2.access.primary;

import com.fasterxml.jackson.annotation.JsonProperty;

public record GenerateTokenRequest(
        @JsonProperty("aws_account_id") String awsAccountId,
        @JsonProperty("expire_in_seconds") long expireInSeconds
) {
}
