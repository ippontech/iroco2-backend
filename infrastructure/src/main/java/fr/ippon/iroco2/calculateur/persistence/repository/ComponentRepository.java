package fr.ippon.iroco2.calculateur.persistence.repository;

import fr.ippon.iroco2.calculateur.persistence.repository.entity.ComponentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ComponentRepository extends JpaRepository<ComponentEntity, UUID> {

    List<ComponentEntity> findByInfrastructureId(UUID infrastructureId);
}
