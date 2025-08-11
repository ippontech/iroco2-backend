package fr.ippon.iroco2.catalog.primary.provider;

import fr.ippon.iroco2.domain.calculator.model.CloudServiceProvider;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface CloudServiceProviderMapper {
    CloudServiceProviderMapper PROVIDER_AND_RESPONSE_MAPPER = Mappers.getMapper(CloudServiceProviderMapper.class);

    List<CloudServiceProviderResponse> toResponse(List<CloudServiceProvider> domains);

    CloudServiceProviderResponse toResponse(CloudServiceProvider domains);
}
