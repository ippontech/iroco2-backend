package fr.ippon.iroco2.analyzer.aws_sqs;

import fr.ippon.iroco2.common.aws_sqs.request.ServiceCUR;
import fr.ippon.iroco2.domain.analyzer.api.AnalyzerSvc;
import fr.ippon.iroco2.domain.commons.exception.FunctionalException;
import io.awspring.cloud.sqs.annotation.SqsListener;
import io.awspring.cloud.sqs.annotation.SqsListenerAcknowledgementMode;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AnalyzerSqsConsumer {

    private final AnalyzerSvc analyzerSvc;

    @SqsListener(
            value = "${aws.sqs.analyzer.queue.name}",
            acknowledgementMode = SqsListenerAcknowledgementMode.ON_SUCCESS
    )
    public void consumeMessages(Message<ServiceCUR> message) throws FunctionalException {
        var payload = message.getPayload().toDomain();
        analyzerSvc.addEstimation(payload);
    }
}
