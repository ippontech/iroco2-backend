package fr.ippon.iroco2.domain.commons.model;

import java.time.Duration;
import java.util.Map;
import java.util.UUID;

public record Payload(
        UUID reportId,
        String name,
        String countryIsoCode,
        Duration durationOfServiceOperation,
        int expectedPayloads,
        Map<PayloadConfiguration, String> configuredValues) {
    public String getValue(PayloadConfiguration key) {
        return configuredValues == null ? null :
                configuredValues.entrySet().stream()
                        .filter(cv -> key == cv.getKey())
                        .findFirst()
                        .map(Map.Entry::getValue)
                        .orElse(null);
    }
}
