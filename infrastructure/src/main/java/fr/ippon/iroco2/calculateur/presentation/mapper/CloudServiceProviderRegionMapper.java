package fr.ippon.iroco2.calculateur.presentation.mapper;

import fr.ippon.iroco2.domain.calculateur.model.CloudServiceProviderRegion;
import fr.ippon.iroco2.calculateur.presentation.reponse.CloudServiceProviderRegionResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper
public abstract class CloudServiceProviderRegionMapper {

    public List<CloudServiceProviderRegionResponse> toResponse(List<CloudServiceProviderRegion> domains) {
        return domains.stream().map(this::toResponse).toList();
    }

    @Mapping(target = "csp", source = "domains.cloudServiceProviderId")
    protected abstract CloudServiceProviderRegionResponse toResponse(CloudServiceProviderRegion domains);

}
