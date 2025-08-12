package fr.ippon.iroco2.domain.calculator;

import fr.ippon.iroco2.domain.calculator.primary.ServiceInstanceSvc;
import fr.ippon.iroco2.domain.calculator.secondary.ServiceInstanceStorage;
import fr.ippon.iroco2.domain.commons.DomainService;

import java.util.List;

@DomainService
public class ServiceInstanceSvcImpl implements ServiceInstanceSvc {
    private final ServiceInstanceStorage storage;

    public ServiceInstanceSvcImpl(ServiceInstanceStorage storage) {
        this.storage = storage;
    }

    @Override
    public List<String> getNamesOfCompatibleInstancesForService(String serviceShortName) {
        return storage.findAllNamesByServiceShortName(serviceShortName);
    }
}
