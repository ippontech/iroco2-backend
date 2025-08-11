package fr.ippon.iroco2.domain.calculator.spi;

import java.util.UUID;

public interface ConfigurationSettingStorage {
    boolean existsById(UUID uuid);
}
