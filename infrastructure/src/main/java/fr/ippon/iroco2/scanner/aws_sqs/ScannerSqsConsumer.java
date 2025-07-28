package fr.ippon.iroco2.scanner.aws_sqs;

import fr.ippon.iroco2.common.aws_sqs.request.ServiceCUR;
import fr.ippon.iroco2.domain.commons.exception.FunctionalException;
import fr.ippon.iroco2.domain.scanner.api.ScannerSvc;
import io.awspring.cloud.sqs.annotation.SqsListener;
import io.awspring.cloud.sqs.annotation.SqsListenerAcknowledgementMode;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ScannerSqsConsumer {

    private final ScannerSvc scannerSvc;

    @SqsListener(
            value = "${aws.sqs.scanner.queue.name}",
            acknowledgementMode = SqsListenerAcknowledgementMode.ON_SUCCESS
    )
    public void consumeMessages(Message<ServiceCUR> message) throws FunctionalException {
        var payload = message.getPayload().toDomain();
        scannerSvc.addEstimation(payload);
    }
}
