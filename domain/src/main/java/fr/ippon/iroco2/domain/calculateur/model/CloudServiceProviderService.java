package fr.ippon.iroco2.domain.calculateur.model;

import fr.ippon.iroco2.domain.calculateur.model.emu.Availability;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class CloudServiceProviderService {
    private UUID id;
    private String name;
    private String description;
    private Availability availability;
    private String shortname;
    private List<String> levers;
    private List<String> limitations;
}
