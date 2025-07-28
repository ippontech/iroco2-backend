package fr.ippon.iroco2.domain.calculateur.spi;

import fr.ippon.iroco2.domain.calculateur.model.ServiceConfigurationSetting;

import java.util.List;
import java.util.UUID;

public interface ServiceConfigurationSettingStorage {
    List<ServiceConfigurationSetting> findAllByServiceId(UUID uuid);
}
