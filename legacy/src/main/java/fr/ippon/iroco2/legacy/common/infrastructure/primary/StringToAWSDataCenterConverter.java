package fr.ippon.iroco2.legacy.common.infrastructure.primary;

import fr.ippon.iroco2.domain.calculateur.model.emu.AWSDataCenter;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToAWSDataCenterConverter implements Converter<String, AWSDataCenter> {

    @Override
    public AWSDataCenter convert(String source) {
        return AWSDataCenter.findByName(source);
    }
}
