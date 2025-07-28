package fr.ippon.iroco2.domain.calculateur.model;

import fr.ippon.iroco2.domain.calculateur.model.emu.SettingName;

import java.util.UUID;

public record ConfiguredSetting(
        UUID configurationSettingId,
        SettingName configurationSettingName,
        String value) {
}
