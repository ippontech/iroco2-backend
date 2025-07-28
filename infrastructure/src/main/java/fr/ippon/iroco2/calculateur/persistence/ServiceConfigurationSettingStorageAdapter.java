package fr.ippon.iroco2.calculateur.persistence;

import fr.ippon.iroco2.domain.calculateur.model.ServiceConfigurationSetting;
import fr.ippon.iroco2.domain.calculateur.spi.ServiceConfigurationSettingStorage;
import fr.ippon.iroco2.calculateur.persistence.repository.ServiceConfigurationSettingRepository;
import fr.ippon.iroco2.calculateur.persistence.repository.entity.ServiceConfigurationSettingEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class ServiceConfigurationSettingStorageAdapter implements ServiceConfigurationSettingStorage {
    private final ServiceConfigurationSettingRepository serviceConfigurationSettingRepository;
    @Override
    public List<ServiceConfigurationSetting> findAllByServiceId(UUID uuid) {
        return serviceConfigurationSettingRepository.findAllByCloudServiceProviderServiceId(uuid).stream()
                .map(ServiceConfigurationSettingEntity::toDomain).toList();
    }
}
