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
package fr.ippon.iroco2.common.aws.cur;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import fr.ippon.iroco2.domain.calculator.model.emu.AWSDataCenter;
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
