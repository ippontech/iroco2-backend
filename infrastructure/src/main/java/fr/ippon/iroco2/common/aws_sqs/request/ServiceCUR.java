package fr.ippon.iroco2.common.aws_sqs.request;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import fr.ippon.iroco2.domain.calculateur.model.emu.AWSDataCenter;
import fr.ippon.iroco2.domain.commons.model.Payload;
import fr.ippon.iroco2.domain.commons.model.PayloadConfiguration;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.Duration;
import java.util.HashMap;
import java.util.UUID;

@Getter
@RequiredArgsConstructor

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "serviceTypeCUR", visible = true)
@JsonSubTypes({@JsonSubTypes.Type(value = ServiceS3CUR.class, name = "S3"), @JsonSubTypes.Type(value = ServiceEC2CUR.class, name = "EC2"),})
public abstract class ServiceCUR {

    private final Duration durationOfServiceOperation;
    private final String awsDataCenter;
    private final String correlationId;
    private final ServiceTypeCUR serviceTypeCUR;
    private final Long numberOfMessageExpected;

    public final Payload toDomain() {
        return new Payload(UUID.fromString(correlationId), serviceTypeCUR.name(), AWSDataCenter.fromValue(awsDataCenter).getAssociatedCountryIsoCode(), durationOfServiceOperation, Math.toIntExact(numberOfMessageExpected), toConfiguredValuesDomain());
    }

    protected abstract HashMap<PayloadConfiguration, String> toConfiguredValuesDomain();
}
