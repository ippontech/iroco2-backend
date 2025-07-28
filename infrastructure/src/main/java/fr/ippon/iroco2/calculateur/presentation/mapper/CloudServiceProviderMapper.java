package fr.ippon.iroco2.calculateur.presentation.mapper;

import fr.ippon.iroco2.domain.calculateur.model.CloudServiceProvider;
import fr.ippon.iroco2.calculateur.presentation.reponse.CloudServiceProviderResponse;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public abstract class CloudServiceProviderMapper {

    public List<CloudServiceProviderResponse> toResponse(List<CloudServiceProvider> domains) {
        return domains.stream().map(this::toResponse).toList();
    }

    protected abstract CloudServiceProviderResponse toResponse(CloudServiceProvider domains);

}
