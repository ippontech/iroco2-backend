package fr.ippon.iroco2.calculateur.persistence.repository;

import fr.ippon.iroco2.calculateur.persistence.repository.entity.CloudServiceProviderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CloudServiceProviderRepository extends JpaRepository<CloudServiceProviderEntity, UUID> {

}
