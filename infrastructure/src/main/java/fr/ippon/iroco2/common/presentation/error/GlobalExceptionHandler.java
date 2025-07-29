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
package fr.ippon.iroco2.common.presentation.error;

import fr.ippon.iroco2.domain.commons.exception.FunctionalException;
import fr.ippon.iroco2.domain.commons.exception.NotFoundException;
import fr.ippon.iroco2.domain.commons.exception.UnauthorizedActionException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
public class GlobalExceptionHandler {

    private static ErrorApi createErrorApi(Exception exception) {
        return new ErrorApi(exception.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorApi defaultCatcher(Exception exception) {
        return createErrorApi(exception);
    }

    @ExceptionHandler({MethodArgumentNotValidException.class, HttpMessageNotReadableException.class, FunctionalException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorApi badRequest(Exception exception) {
        return createErrorApi(exception);
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorApi notFound(NotFoundException exception) {
        return createErrorApi(exception);
    }

    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorApi unauthorized(AuthenticationException exception) {
        return createErrorApi(exception);
    }

    @ExceptionHandler({AccessDeniedException.class, UnauthorizedActionException.class})
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorApi forbidden(Exception exception) {
        return createErrorApi(exception);
    }

}
