package fr.ippon.iroco2.domain.calculateur.api;

import fr.ippon.iroco2.domain.calculateur.model.ServiceConfigurationSetting;

import java.util.List;
import java.util.UUID;

public interface ServiceConfigurationSettingSvc {
    List<ServiceConfigurationSetting> findAllByService(UUID cloudServiceProviderService);
}