package fr.ippon.iroco2.calculateur.presentation.reponse;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public record ConfiguredSettingResponse(@JsonProperty("id") UUID configurationSettingId, String value) {
}
