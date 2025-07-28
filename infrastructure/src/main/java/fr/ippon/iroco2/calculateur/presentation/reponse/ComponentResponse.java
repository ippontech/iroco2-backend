package fr.ippon.iroco2.calculateur.presentation.reponse;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record ComponentResponse(
    UUID id,
    String name,
    LocalDateTime lastModificationDate,
    UUID regionID,
    UUID serviceID,
    List<ConfiguredSettingResponse> configurationValues
) {}
