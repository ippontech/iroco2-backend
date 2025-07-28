package fr.ippon.iroco2.estimateur.persistence.repository;

import fr.ippon.iroco2.estimateur.persistence.repository.entity.EC2InstanceEntity;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EC2InstanceRepository extends CrudRepository<EC2InstanceEntity, Integer>, JpaSpecificationExecutor<EC2InstanceEntity> {
    Optional<EC2InstanceEntity> findByName(String name);
}
