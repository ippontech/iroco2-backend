package fr.ippon.iroco2.calculator.infrastructure.component.secondary;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServiceInstanceRepository extends JpaRepository<ServiceInstanceEntity, Integer> {
    List<ServiceInstanceEntity> findById_ServiceShortName(String shortname);
}
