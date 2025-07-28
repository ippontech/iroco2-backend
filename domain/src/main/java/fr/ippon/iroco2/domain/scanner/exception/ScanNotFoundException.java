package fr.ippon.iroco2.domain.scanner.exception;

import fr.ippon.iroco2.domain.commons.exception.NotFoundException;

import java.util.UUID;

public class ScanNotFoundException extends NotFoundException {
    public ScanNotFoundException(UUID scanId) {
        super("The scan with ID '%s' is not found".formatted(scanId));
    }
}
