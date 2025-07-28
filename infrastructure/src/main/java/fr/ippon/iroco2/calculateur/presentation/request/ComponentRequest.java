package fr.ippon.iroco2.calculateur.presentation.request;

import fr.ippon.iroco2.domain.calculateur.model.CloudServiceProviderService;
import fr.ippon.iroco2.domain.calculateur.model.Component;
import fr.ippon.iroco2.domain.calculateur.model.ConfiguredSetting;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

public record ComponentRequest(
        UUID id,
        @NotNull UUID infrastructureID,
        @NotBlank String name,
        @NotNull UUID regionID,
        @NotNull UUID serviceID,
        @NotEmpty List<ConfigurationValueRequest> configurationValues
) {

    public Component createToDomain() {
        var values = mapConfiguredSettings();
        var service = new CloudServiceProviderService();
        service.setId(serviceID);
        return Component.create(infrastructureID, name, regionID, service, values);
    }

    public Component updateToDomain() {
        var values = mapConfiguredSettings();
        var service = new CloudServiceProviderService();
        service.setId(serviceID);
        return Component.load(id, infrastructureID, name, null, regionID, service, values);
    }

    private List<ConfiguredSetting> mapConfiguredSettings() {
        return configurationValues.stream()
                .map(value -> new ConfiguredSetting(value.configurationSettingId(), null, value.value()))
                .toList();
    }
}
