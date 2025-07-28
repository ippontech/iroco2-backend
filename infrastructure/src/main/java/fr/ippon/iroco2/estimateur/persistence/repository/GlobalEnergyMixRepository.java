package fr.ippon.iroco2.estimateur.persistence.repository;

import fr.ippon.iroco2.estimateur.persistence.repository.entity.GlobalEnergyMixEntity;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GlobalEnergyMixRepository extends CrudRepository<GlobalEnergyMixEntity, Integer>, JpaSpecificationExecutor<GlobalEnergyMixEntity> {
    Optional<GlobalEnergyMixEntity> findByIsoCode(String isoCode);
}
