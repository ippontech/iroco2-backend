package fr.ippon.iroco2.legacy.instance_type.infrastructure.persistence;

import fr.ippon.iroco2.legacy.instance_type.infrastructure.persistence.entity.ServiceInstanceModel;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceInstanceRepository extends JpaRepository<ServiceInstanceModel, Integer> {
    List<ServiceInstanceModel> findById_ServiceShortName(String shortname);
}
