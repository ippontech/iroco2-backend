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
package fr.ippon.iroco2.access.aws_kms;

import static org.assertj.core.api.Assertions.assertThat;

import fr.ippon.iroco2.KmsMockConfig;
import fr.ippon.iroco2.S3MockConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@EnableConfigurationProperties(KmsProperties.class)
@TestPropertySource(
    properties = {
        "spring.cloud.aws.kms.region=us-east-1",
        "spring.cloud.aws.kms.endpoint=http://localhost:4566",
        "spring.cloud.aws.kms.profile=iroco-dev",
    }
)
@Import({ S3MockConfig.class, KmsMockConfig.class })
class KmsPropertiesTest {

    @Autowired
    private KmsProperties kmsProperties;

    @Test
    void should_bind_region() {
        assertThat(kmsProperties.getRegion()).isEqualTo("us-east-1");
    }

    @Test
    void should_bind_endpoint() {
        assertThat(kmsProperties.getEndpoint()).hasToString("http://localhost:4566");
    }

    @Test
    void should_bind_profile() {
        assertThat(kmsProperties.getProfile()).isEqualTo("iroco-dev");
    }
}
