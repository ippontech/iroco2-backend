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

import fr.ippon.iroco2.analyzer.persistence.repository.AnalysisRepository;
import fr.ippon.iroco2.analyzer.persistence.repository.entity.AnalysisEntity;
import fr.ippon.iroco2.common.TestLocalStackWithSQS;
import fr.ippon.iroco2.common.aws_sqs.request.ServiceCUR;
import fr.ippon.iroco2.common.aws_sqs.request.ServiceEC2CUR;
import fr.ippon.iroco2.common.aws_sqs.request.ServiceTypeCUR;
import fr.ippon.iroco2.domain.analyzer.model.Analysis;
import fr.ippon.iroco2.domain.analyzer.spi.AnalysisStorage;
import fr.ippon.iroco2.domain.calculateur.model.emu.AWSDataCenter;
import fr.ippon.iroco2.estimateur.persistence.repository.GlobalEnergyMixRepository;
import fr.ippon.iroco2.estimateur.persistence.repository.entity.GlobalEnergyMixEntity;
import org.assertj.core.api.Assertions;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.testcontainers.shaded.org.awaitility.Awaitility;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

class AnalyzerSqsConsumerTest extends TestLocalStackWithSQS {
    @Autowired
    private AnalysisRepository analysisRepository;

    @SpyBean
    private AnalysisStorage analysisStorage;

    @Autowired
    private GlobalEnergyMixRepository globalEnergyMixRepository;

    @Value("${aws.sqs.analyzer.queue.name}")
    private String queueName;

    private static @NotNull ServiceCUR createEC2CUR(UUID uuid, AWSDataCenter dataCenter) {
        String ec2Type = "t3a.nano";
        Duration duration = Duration.of(5, ChronoUnit.HOURS);
        String correlationId = uuid.toString();
        ServiceTypeCUR serviceTypeCUR = ServiceTypeCUR.EC2;
        Long numberOfMessageExpected = 1L;

        return new ServiceEC2CUR(
                duration,
                dataCenter.getAbbreviation(),
                correlationId,
                serviceTypeCUR,
                numberOfMessageExpected,
                ec2Type
        );
    }

    @Test
    void receivedMessageFromQueue() {
        // GIVEN
        UUID analysisId = UUID.randomUUID();
        AWSDataCenter dataCenter = AWSDataCenter.AFRICA_CAPE_TOWN;
        var serviceCUR = createEC2CUR(analysisId, dataCenter);

        givenExistingAnalysis(analysisId);
        givenExistingGlobalEnergyMix(dataCenter);

        // WHEN
        sqsTemplate.send(to -> to.queue(queueName).payload(serviceCUR));

        // THEN
        Awaitility.await()
                .atMost(5, TimeUnit.SECONDS)
                .pollInterval(100, TimeUnit.MILLISECONDS)
                .untilAsserted(() -> analysisHasBeenSaved(analysisId));
    }

    private void analysisHasBeenSaved(UUID analysisId) {
        ArgumentCaptor<Analysis> captor = ArgumentCaptor.forClass(Analysis.class);
        Mockito.verify(analysisStorage).save(captor.capture());
        Assertions.assertThat(captor.getValue()).isNotNull();
        Assertions.assertThat(captor.getValue().getId()).isEqualTo(analysisId);
    }

    private void givenExistingGlobalEnergyMix(AWSDataCenter dataCenter) {
        Optional<GlobalEnergyMixEntity> existingMix = globalEnergyMixRepository
                .findByIsoCode(dataCenter.getAssociatedCountryIsoCode());

        if (existingMix.isEmpty()) {
            GlobalEnergyMixEntity mixEntity = new GlobalEnergyMixEntity();
            mixEntity.setIsoCode(dataCenter.getAssociatedCountryIsoCode());
            globalEnergyMixRepository.save(mixEntity);
        }
    }

    private void givenExistingAnalysis(UUID analysisId) {
        AnalysisEntity analysisEntity = new AnalysisEntity();
        analysisEntity.setId(analysisId);
        analysisRepository.save(analysisEntity);
    }
}
