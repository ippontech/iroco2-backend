package fr.ippon.iroco2.calculateur.persistence.repository;

import fr.ippon.iroco2.calculateur.persistence.repository.entity.ServiceConfigurationSettingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ServiceConfigurationSettingRepository extends JpaRepository<ServiceConfigurationSettingEntity, UUID> {

    List<ServiceConfigurationSettingEntity> findAllByCloudServiceProviderServiceId(UUID cloudServiceProviderServiceId);

}
