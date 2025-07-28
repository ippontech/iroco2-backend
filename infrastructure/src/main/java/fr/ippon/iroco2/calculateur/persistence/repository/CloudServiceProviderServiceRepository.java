package fr.ippon.iroco2.calculateur.persistence.repository;

import fr.ippon.iroco2.calculateur.persistence.repository.entity.CloudServiceProviderServiceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CloudServiceProviderServiceRepository extends JpaRepository<CloudServiceProviderServiceEntity, UUID> {


    @Query(value = """
                select csps from CloudServiceProviderServiceEntity as csps
                where  csps.cloudServiceProvider.id = :cspId
            """)
    List<CloudServiceProviderServiceEntity> findAllByCsp(UUID cspId);

}
