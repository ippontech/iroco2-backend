package fr.ippon.iroco2.domain.calculateur.spi;

import fr.ippon.iroco2.domain.calculateur.model.ConfigurationSetting;

import java.util.Optional;
import java.util.UUID;

public interface ConfigurationSettingStorage {
    Optional<ConfigurationSetting> findById(UUID uuid);
}
