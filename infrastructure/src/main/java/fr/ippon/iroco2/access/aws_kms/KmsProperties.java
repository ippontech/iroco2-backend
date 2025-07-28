package fr.ippon.iroco2.access.aws_kms;

import io.awspring.cloud.autoconfigure.AwsClientProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = KmsProperties.PREFIX)
public class KmsProperties extends AwsClientProperties {

    public static final String PREFIX = "spring.cloud.aws.kms";

    private String profile;

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }
}
