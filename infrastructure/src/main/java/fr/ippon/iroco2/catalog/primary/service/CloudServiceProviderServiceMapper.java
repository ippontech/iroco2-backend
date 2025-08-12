package fr.ippon.iroco2.catalog.primary.service;

import fr.ippon.iroco2.domain.calculator.model.CloudServiceProviderService;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface CloudServiceProviderServiceMapper {
    CloudServiceProviderServiceMapper SERVICE_AND_RESPONSE_MAPPER = Mappers.getMapper(CloudServiceProviderServiceMapper.class);

    List<CloudServiceProviderServiceResponse> toResponse(List<CloudServiceProviderService> domains);

    CloudServiceProviderServiceResponse toResponse(CloudServiceProviderService domain);
}
