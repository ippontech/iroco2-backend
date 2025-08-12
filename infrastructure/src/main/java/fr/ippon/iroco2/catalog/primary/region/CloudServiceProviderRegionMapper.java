package fr.ippon.iroco2.catalog.primary.region;

import fr.ippon.iroco2.domain.calculator.model.CloudServiceProviderRegion;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface CloudServiceProviderRegionMapper {
    CloudServiceProviderRegionMapper REGION_AND_RESPONSE_MAPPER = Mappers.getMapper(CloudServiceProviderRegionMapper.class);

    List<CloudServiceProviderRegionResponse> toResponse(List<CloudServiceProviderRegion> domains);

    @Mapping(target = "csp", source = "domains.cloudServiceProviderId")
    CloudServiceProviderRegionResponse toResponse(CloudServiceProviderRegion domains);
}
