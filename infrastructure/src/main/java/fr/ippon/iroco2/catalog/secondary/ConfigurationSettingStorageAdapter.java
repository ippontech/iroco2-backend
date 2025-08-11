package fr.ippon.iroco2.catalog.secondary;

import fr.ippon.iroco2.domain.calculator.spi.ConfigurationSettingStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class ConfigurationSettingStorageAdapter implements ConfigurationSettingStorage {
    private final ConfigurationSettingRepository configurationSettingRepository;

    @Override
    public boolean existsById(UUID uuid) {
        return configurationSettingRepository.existsById(uuid);
    }
}
