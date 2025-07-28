package fr.ippon.iroco2.domain.calculateur;

import fr.ippon.iroco2.domain.calculateur.api.ServiceConfigurationSettingSvc;
import fr.ippon.iroco2.domain.calculateur.model.ServiceConfigurationSetting;
import fr.ippon.iroco2.domain.calculateur.spi.ServiceConfigurationSettingStorage;
import fr.ippon.iroco2.domain.commons.DomainService;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;

@DomainService
@RequiredArgsConstructor
public class ServiceConfigurationSettingSvcIpml implements ServiceConfigurationSettingSvc {

    private final ServiceConfigurationSettingStorage serviceConfigurationSettingStorage;

    @Override
    public List<ServiceConfigurationSetting> findAllByService(UUID cloudServiceProviderService) {
        return serviceConfigurationSettingStorage.findAllByServiceId(cloudServiceProviderService);
    }
}