package fr.ippon.iroco2.catalog.primary.setting;

import fr.ippon.iroco2.domain.calculator.model.ServiceConfigurationSetting;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface ServiceConfigurationSettingMapper {
    ServiceConfigurationSettingMapper SETTING_AND_RESPONSE_MAPPER = Mappers.getMapper(ServiceConfigurationSettingMapper.class);

    List<ServiceConfigurationSettingResponse> toResponse(List<ServiceConfigurationSetting> domains);

    ServiceConfigurationSettingResponse toResponse(ServiceConfigurationSetting domains);
}
