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
package fr.ippon.iroco2.access.primary;

import fr.ippon.iroco2.common.primary.error.ErrorApi;
import fr.ippon.iroco2.common.primary.error.GlobalExceptionHandler;
import fr.ippon.iroco2.domain.commons.exception.FunctionalException;
import fr.ippon.iroco2.domain.commons.exception.NotFoundException;
import jakarta.validation.ValidationException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler globalExceptionHandler = new GlobalExceptionHandler();

    public static void isCorrect(ErrorApi errorApi, String message) {
        Assertions.assertThat(errorApi).hasFieldOrPropertyWithValue("message", message);
    }

    @Test
    void testDataIntegrityViolationException() {
        ValidationException exception = new ValidationException("Invalid data");

        var errorApi = globalExceptionHandler.badRequest(exception);

        isCorrect(errorApi, "Invalid data");
    }

    @Test
    void testFunctionalExceptionHandler() {
        FunctionalException exception = new FunctionalException("Business rule violation") {

        };

        var errorApi = globalExceptionHandler.badRequest(exception);

        isCorrect(errorApi, "Business rule violation");
    }

    @Test
    void testNotFoundExceptionHandler() {
        NotFoundException exception = new NotFoundException("Resource not found");

        var errorApi = globalExceptionHandler.notFound(exception);

        isCorrect(errorApi, "Resource not found");
    }

    @Test
    void testCustomSecurityHandler() {
        IrocoAuthenticationException exception = new IrocoAuthenticationException("Custom security issue");

        var result = globalExceptionHandler.unauthorized(exception);

        isCorrect(result, "Custom security issue");
    }

    @Test
    void testAccessDeniedHandler() {
        AccessDeniedException exception = new AccessDeniedException("Access denied");

        var result = globalExceptionHandler.forbidden(exception);

        isCorrect(result, "Access denied");
    }

    @Test
    void testAuthenticationSecurityHandler() {
        AuthenticationException exception = new IrocoAuthenticationException("Authentication failed");

        var result = globalExceptionHandler.unauthorized(exception);

        isCorrect(result, "Authentication failed");
    }
}