package fr.ippon.iroco2.common.infrastructure.primary;

import fr.ippon.iroco2.domain.commons.exception.FunctionalException;
import fr.ippon.iroco2.domain.commons.exception.NotFoundException;
import fr.ippon.iroco2.common.presentation.error.ErrorApi;
import fr.ippon.iroco2.common.presentation.error.GlobalExceptionHandler;
import fr.ippon.iroco2.legacy.access.infrastructure.primary.IrocoAuthenticationException;
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