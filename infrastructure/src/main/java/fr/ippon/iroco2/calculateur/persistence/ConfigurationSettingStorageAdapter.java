package fr.ippon.iroco2.calculateur.persistence;

import fr.ippon.iroco2.domain.calculateur.model.ConfigurationSetting;
import fr.ippon.iroco2.domain.calculateur.spi.ConfigurationSettingStorage;
import fr.ippon.iroco2.calculateur.persistence.repository.ConfigurationSettingRepository;
import fr.ippon.iroco2.calculateur.persistence.repository.entity.ConfigurationSettingEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class ConfigurationSettingStorageAdapter implements ConfigurationSettingStorage {
    private final ConfigurationSettingRepository configurationSettingRepository;

    @Override
    public Optional<ConfigurationSetting> findById(UUID uuid) {
        return configurationSettingRepository.findById(uuid).map(ConfigurationSettingEntity::toDomain);
    }
}
