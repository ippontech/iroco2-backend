package fr.ippon.iroco2.domain.commons.svc;

import fr.ippon.iroco2.domain.commons.DomainService;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@DomainService
@RequiredArgsConstructor
public class DateProvider {
    public LocalDateTime now() {
        return LocalDateTime.now();
    }
}
