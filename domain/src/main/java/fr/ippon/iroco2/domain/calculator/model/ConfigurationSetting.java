package fr.ippon.iroco2.domain.calculator.model;

import fr.ippon.iroco2.domain.calculator.model.emu.SettingName;

import java.util.UUID;

public record ConfigurationSetting(
        UUID id,
        SettingName name) {
}
