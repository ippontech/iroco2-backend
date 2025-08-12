package fr.ippon.iroco2.domain.calculator.primary;

import java.util.List;

public interface ServiceInstanceSvc {
    List<String> getNamesOfCompatibleInstancesForService(String serviceShortName);
}
