package fr.ippon.iroco2.common.aws_sqs.request;

import fr.ippon.iroco2.domain.commons.model.PayloadConfiguration;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.HashMap;

@Getter
public class ServiceS3CUR extends ServiceCUR {

    private final BigDecimal storageInMo;

    public ServiceS3CUR( Duration durationOfServiceOperation, String awsDataCenter, String correlationId, ServiceTypeCUR serviceTypeCUR, Long numberOfMessageExpected, BigDecimal storageInMo) {
        super(durationOfServiceOperation, awsDataCenter, correlationId, serviceTypeCUR, numberOfMessageExpected);
        this.storageInMo = storageInMo;
    }

    @Override
    protected HashMap<PayloadConfiguration, String> toConfiguredValuesDomain() {
        HashMap<PayloadConfiguration, String> config = new HashMap<>();
        config.put(PayloadConfiguration.S3_STORAGE, storageInMo.toString());
        return config;
    }
}
