package fr.ippon.iroco2.calculateur.presentation.mapper;

import fr.ippon.iroco2.domain.calculateur.model.ServiceConfigurationSetting;
import fr.ippon.iroco2.calculateur.presentation.reponse.ServiceConfigurationSettingResponse;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public abstract class ServiceConfigurationSettingMapper {

    public List<ServiceConfigurationSettingResponse> toResponse(List<ServiceConfigurationSetting> domains) {
        return domains.stream().map(this::toResponse).toList();
    }

    protected abstract ServiceConfigurationSettingResponse toResponse(ServiceConfigurationSetting domains);

}
