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

import io.awspring.cloud.autoconfigure.core.AwsClientBuilderConfigurer;
import io.awspring.cloud.autoconfigure.core.AwsClientCustomizer;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.services.kms.KmsClient;
import software.amazon.awssdk.services.kms.KmsClientBuilder;

@Configuration
@ConditionalOnClass(KmsClient.class)
@EnableConfigurationProperties(KmsProperties.class)
@ConditionalOnProperty(name = "spring.cloud.aws.kms.enabled", havingValue = "true", matchIfMissing = true)
public class KmsAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public KmsClient kmsClient(
        AwsClientBuilderConfigurer awsClientBuilderConfigurer,
        ObjectProvider<AwsClientCustomizer<KmsClientBuilder>> configurer,
        KmsProperties properties
    ) {
        KmsClientBuilder builder = KmsClient.builder();
        awsClientBuilderConfigurer.configure(builder, properties, configurer.getIfAvailable());
        if (properties.getProfile() != null) {
            AwsCredentialsProvider profileProvider = ProfileCredentialsProvider.create(properties.getProfile());
            builder.credentialsProvider(profileProvider);
        }

        return builder.build();
    }
}
