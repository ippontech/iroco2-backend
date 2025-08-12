package fr.ippon.iroco2.domain.calculator.secondary;

import java.util.List;

public interface ServiceInstanceStorage {
    List<String> findAllNamesByServiceShortName(String shortName);
}
