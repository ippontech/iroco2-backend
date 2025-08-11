package fr.ippon.iroco2.calculator.infrastructure.component.secondary;

import fr.ippon.iroco2.common.secondary.EC2InstanceEntity;
import fr.ippon.iroco2.domain.calculator.spi.ServiceInstanceStorage;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ServiceInstanceStorageAdapter implements ServiceInstanceStorage {
    private final ServiceInstanceRepository repository;

    public ServiceInstanceStorageAdapter(ServiceInstanceRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<String> findAllNamesByServiceShortName(String shortName) {
        return repository.findById_ServiceShortName(shortName)
                .stream()
                .map(ServiceInstanceEntity::instanceModel)
                .map(EC2InstanceEntity::getName)
                .toList();
    }
}
