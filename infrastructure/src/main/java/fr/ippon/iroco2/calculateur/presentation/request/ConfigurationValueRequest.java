package fr.ippon.iroco2.calculateur.presentation.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;


public record ConfigurationValueRequest(@NotBlank String value,
                                        @JsonProperty("id") @NotNull UUID configurationSettingId) {
}
