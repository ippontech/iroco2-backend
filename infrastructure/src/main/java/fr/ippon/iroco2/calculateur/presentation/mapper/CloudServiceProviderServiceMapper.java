package fr.ippon.iroco2.calculateur.presentation.mapper;

import fr.ippon.iroco2.domain.calculateur.model.CloudServiceProviderService;
import fr.ippon.iroco2.calculateur.presentation.reponse.CloudServiceProviderServiceResponse;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public abstract class CloudServiceProviderServiceMapper {

    public List<CloudServiceProviderServiceResponse> toResponse(List<CloudServiceProviderService> domains) {
        return domains.stream().map(this::toResponse).toList();
    }

    public abstract CloudServiceProviderServiceResponse toResponse(CloudServiceProviderService domain);

}
