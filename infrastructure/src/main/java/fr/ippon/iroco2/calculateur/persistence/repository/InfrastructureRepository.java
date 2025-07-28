package fr.ippon.iroco2.calculateur.persistence.repository;

import fr.ippon.iroco2.calculateur.persistence.repository.entity.InfrastructureEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface InfrastructureRepository extends JpaRepository<InfrastructureEntity, UUID> {

    List<InfrastructureEntity> findAllByOwner(String owner);

}
