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
