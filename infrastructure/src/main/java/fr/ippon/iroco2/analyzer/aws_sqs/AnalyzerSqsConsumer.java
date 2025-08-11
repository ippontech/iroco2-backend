/*
 * Copyright 2025 Ippon Technologies
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package fr.ippon.iroco2.analyzer.aws_sqs;

import fr.ippon.iroco2.common.aws.cur.ServiceCUR;
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
