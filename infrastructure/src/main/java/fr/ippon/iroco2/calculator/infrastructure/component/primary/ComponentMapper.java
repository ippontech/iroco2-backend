package fr.ippon.iroco2.calculator.infrastructure.component.primary;

import fr.ippon.iroco2.calculator.infrastructure.primary.InfrastructureResponse;
import fr.ippon.iroco2.domain.calculator.model.Component;
import fr.ippon.iroco2.domain.calculator.model.Infrastructure;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface ComponentMapper {
    ComponentMapper COMPONENT_AND_RESPONSE_MAPPER = Mappers.getMapper(ComponentMapper.class);

    @Mapping(target = "defaultRegion", source = "defaultRegionId")
    InfrastructureResponse toResponse(Infrastructure infrastructure);

    List<ComponentResponse> toResponse(List<Component> domains);

    @Mapping(target = "serviceID", source = "service.id")
    ComponentResponse toResponse(Component domain);
}
