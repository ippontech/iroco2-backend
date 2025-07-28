package fr.ippon.iroco2.domain.commons.exception;

public abstract class FunctionalException extends Exception {

    protected FunctionalException(String message) {
        super(message);
    }
}
