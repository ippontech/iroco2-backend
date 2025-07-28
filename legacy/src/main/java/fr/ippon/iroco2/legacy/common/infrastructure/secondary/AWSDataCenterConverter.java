package fr.ippon.iroco2.legacy.common.infrastructure.secondary;

import fr.ippon.iroco2.domain.calculateur.model.emu.AWSDataCenter;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.apache.commons.lang3.StringUtils;

@Converter(autoApply = true)
public class AWSDataCenterConverter implements AttributeConverter<AWSDataCenter, String> {

    @Override
    public String convertToDatabaseColumn(AWSDataCenter attribute) {
        return attribute == null ? null : attribute.getName();
    }

    @Override
    public AWSDataCenter convertToEntityAttribute(String dbData) {
        return StringUtils.isNotBlank(dbData) ? AWSDataCenter.findByName(dbData) : null;
    }
}
