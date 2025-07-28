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
