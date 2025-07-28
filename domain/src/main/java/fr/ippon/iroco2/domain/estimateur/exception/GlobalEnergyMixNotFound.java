package fr.ippon.iroco2.domain.estimateur.exception;


import fr.ippon.iroco2.domain.commons.exception.FunctionalException;

public class GlobalEnergyMixNotFound extends FunctionalException {
    public GlobalEnergyMixNotFound(String message) {
        super(message);
    }
}
