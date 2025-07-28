package fr.ippon.iroco2.domain.commons.exception;


public class UnauthorizedActionException extends RuntimeException {

    public UnauthorizedActionException(String message) {
        super(message);
    }
}
