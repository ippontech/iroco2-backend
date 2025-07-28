package fr.ippon.iroco2.calculateur.presentation.reponse;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ServiceConfigurationSettingResponse {


    private UUID id;
    private ConfigurationSettingResponse configurationSetting;
    private String defaultValue;

}
