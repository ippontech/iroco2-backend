package fr.ippon.iroco2.domain.calculateur.model;

import java.util.UUID;

public record ServiceConfigurationSetting(
        UUID id,
        ConfigurationSetting configurationSetting,
        String defaultValue) {
}
