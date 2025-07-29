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
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.nio.charset.StandardCharsets;
import java.security.interfaces.RSAPublicKey;
import java.util.Base64;
import java.util.function.Consumer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.services.kms.KmsClient;
import software.amazon.awssdk.services.kms.model.SignRequest;
import software.amazon.awssdk.services.kms.model.SignResponse;
import software.amazon.awssdk.services.kms.model.SigningAlgorithmSpec;

@ExtendWith(MockitoExtension.class)
class AwsKeyManagementServiceTest {

    private AwsKeyManagementService awsKeyManagementService;

    @Mock
    private KmsClient kmsClient;

    private final String keyARN = "dummy-key-arn";

    private final String publicKey =
        "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAu1SU1LfVLPHCozMxH2Mo" +
        "4lgOEePzNm0tRgeLezV6ffAt0gunVTLw7onLRnrq0/IzW7yWR7QkrmBL7jTKEn5u" +
        "+qKhbwKfBstIs+bMY2Zkp18gnTxKLxoS2tFczGkPLPgizskuemMghRniWaoLcyeh" +
        "kd3qqGElvW/VDL5AaWTg0nLVkjRo9z+40RQzuVaE8AkAFmxZzow3x+VJYKdjykkJ" +
        "0iT9wCS0DRTXu269V264Vf/3jvredZiKRkgwlL9xNAwxXFg0x/XFw005UWVRIkdJ" +
        "C9xajd4i7T+UGcuL3Sl0Cb7K1De6o7QkF8yJp+h/6tZz0i1D9Q0Z6qnD3Ynp5UO3" +
        "gwIDAQAB";

    @Captor
    private ArgumentCaptor<Consumer<SignRequest.Builder>> signRequestCaptor;

    @BeforeEach
    void setUp() {
        awsKeyManagementService = new AwsKeyManagementService(kmsClient, publicKey, keyARN);
    }

    @Test
    void sign_returns_signature_of_the_message() {
        // Arrange
        String message = "test-message";
        String dummySignature = "dummy-signature";
        SdkBytes signatureBytes = SdkBytes.fromByteArray(dummySignature.getBytes());
        SignResponse signResponse = SignResponse.builder().signature(signatureBytes).build();

        when(kmsClient.sign(any(Consumer.class))).thenReturn(signResponse);

        // Act
        byte[] result = awsKeyManagementService.sign(message);

        // Assert
        verify(kmsClient).sign(signRequestCaptor.capture());
        SignRequest.Builder builder = SignRequest.builder();
        signRequestCaptor.getValue().accept(builder);
        SignRequest request = builder.build();

        assertThat(result).isEqualTo(dummySignature.getBytes());
        assertThat(request.keyId()).isEqualTo(keyARN);
        assertThat(request.signingAlgorithm()).isEqualTo(SigningAlgorithmSpec.RSASSA_PKCS1_V1_5_SHA_256);
        assertThat(request.message().asByteArray()).isEqualTo(message.getBytes(StandardCharsets.UTF_8));
    }

    @Test
    void getPublicKey_returns_the_public_key_of_the_corresponding_key_id() throws Exception {
        // Arrange
        byte[] validPublicKeyDer = Base64.getDecoder().decode(publicKey.getBytes());

        // Act
        RSAPublicKey result = awsKeyManagementService.getPublicKey();

        // Assert
        assertThat(result.getEncoded()).isEqualTo(validPublicKeyDer);
    }
}
