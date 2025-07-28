package fr.ippon.iroco2.calculateur.presentation.reponse;

import fr.ippon.iroco2.domain.calculateur.model.emu.Availability;

import java.util.List;
import java.util.UUID;

public record CloudServiceProviderServiceResponse(
        UUID id,
        String name,
        String description,
        String shortname,
        Availability availability,
        List<String> levers,
        List<String> limitations) {
}
