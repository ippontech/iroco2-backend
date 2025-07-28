package fr.ippon.iroco2.calculateur.presentation.mapper;

import fr.ippon.iroco2.domain.calculateur.model.Component;
import fr.ippon.iroco2.domain.calculateur.model.Infrastructure;
import fr.ippon.iroco2.calculateur.presentation.reponse.ComponentResponse;
import fr.ippon.iroco2.calculateur.presentation.reponse.InfrastructureResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper
public abstract class ComponentMapper {

    @Mapping(target = "defaultRegion", source = "defaultRegionId")
    public abstract InfrastructureResponse toResponse(Infrastructure infrastructure);

    public List<ComponentResponse> toResponse(List<Component> domains) {
        return domains.stream().map(this::toResponse).toList();
    }

    @Mapping(target = "serviceID", source = "service.id")
    protected abstract ComponentResponse toResponse(Component domain);
}
