package fr.ippon.iroco2.domain.calculateur.model;

import fr.ippon.iroco2.domain.calculateur.model.emu.SettingName;

import java.util.UUID;

public record ConfigurationSetting(
        UUID id,
        SettingName name) {
}
