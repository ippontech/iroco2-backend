package fr.ippon.iroco2.common.aws_sqs.request;

import fr.ippon.iroco2.domain.commons.model.PayloadConfiguration;
import lombok.Getter;

import java.time.Duration;
import java.util.HashMap;

@Getter
public class ServiceEC2CUR extends ServiceCUR {

    private final String ec2Type;

    public ServiceEC2CUR( Duration durationOfServiceOperation, String awsDataCenter, String correlationId, ServiceTypeCUR serviceTypeCUR, Long numberOfMessageExpected, String ec2Type) {
        super(durationOfServiceOperation, awsDataCenter, correlationId, serviceTypeCUR, numberOfMessageExpected);
        this.ec2Type = ec2Type;
    }

    @Override
    protected HashMap<PayloadConfiguration, String> toConfiguredValuesDomain() {
        HashMap<PayloadConfiguration, String> config = new HashMap<>();
        config.put(PayloadConfiguration.INSTANCE_TYPE, ec2Type);
        return config;
    }
}
