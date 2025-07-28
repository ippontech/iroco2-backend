package fr.ippon.iroco2.domain.scanner.api;

import fr.ippon.iroco2.domain.commons.ElementSvc;
import fr.ippon.iroco2.domain.scanner.exception.ScanNotFoundException;
import fr.ippon.iroco2.domain.scanner.model.Scan;

public interface ScannerSvc extends ElementSvc<Scan, ScanNotFoundException> {
}
