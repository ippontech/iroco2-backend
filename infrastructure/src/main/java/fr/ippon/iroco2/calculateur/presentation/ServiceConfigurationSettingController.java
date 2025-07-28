package fr.ippon.iroco2.calculateur.presentation;

import fr.ippon.iroco2.domain.calculateur.api.ServiceConfigurationSettingSvc;
import fr.ippon.iroco2.calculateur.presentation.mapper.ServiceConfigurationSettingMapper;
import fr.ippon.iroco2.calculateur.presentation.reponse.ServiceConfigurationSettingResponse;
import fr.ippon.iroco2.common.presentation.security.IsMember;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/service-configuration-settings")
@RequiredArgsConstructor
public class ServiceConfigurationSettingController {

    public static final ServiceConfigurationSettingMapper SETTING_MAPPER = Mappers.getMapper(ServiceConfigurationSettingMapper.class);
    private final ServiceConfigurationSettingSvc configurationSettingSvc;

    @GetMapping("/{cloudServiceProviderServiceId}")
    @IsMember
    public ResponseEntity<List<ServiceConfigurationSettingResponse>> findAllByService(@PathVariable UUID cloudServiceProviderServiceId) {
        var configurationSetting = configurationSettingSvc.findAllByService(cloudServiceProviderServiceId);
        return ResponseEntity.ok(SETTING_MAPPER.toResponse(configurationSetting));
    }

}
