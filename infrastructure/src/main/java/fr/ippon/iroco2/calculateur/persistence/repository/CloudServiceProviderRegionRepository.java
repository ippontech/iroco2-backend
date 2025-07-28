package fr.ippon.iroco2.calculateur.persistence.repository;

import fr.ippon.iroco2.calculateur.persistence.repository.entity.CloudServiceProviderRegionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CloudServiceProviderRegionRepository extends JpaRepository<CloudServiceProviderRegionEntity, UUID> {

    @Query(value = """
                select cspr from CloudServiceProviderRegionEntity as cspr
                where  cspr.cloudServiceProvider.id = :cspId
            """)
    List<CloudServiceProviderRegionEntity> findAllByCsp(UUID cspId);

}
