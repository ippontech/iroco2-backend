package fr.ippon.iroco2.catalog.primary.provider;

import fr.ippon.iroco2.catalog.primary.region.CloudServiceProviderRegionResponse;
import fr.ippon.iroco2.catalog.primary.service.CloudServiceProviderServiceResponse;
import fr.ippon.iroco2.common.primary.security.IsMember;
import fr.ippon.iroco2.domain.calculator.api.CloudServiceProviderRegionSvc;
import fr.ippon.iroco2.domain.calculator.api.CloudServiceProviderServiceSvc;
import fr.ippon.iroco2.domain.calculator.api.CloudServiceProviderSvc;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

import static fr.ippon.iroco2.catalog.primary.provider.CloudServiceProviderMapper.PROVIDER_AND_RESPONSE_MAPPER;
import static fr.ippon.iroco2.catalog.primary.region.CloudServiceProviderRegionMapper.REGION_AND_RESPONSE_MAPPER;
import static fr.ippon.iroco2.catalog.primary.service.CloudServiceProviderServiceMapper.SERVICE_AND_RESPONSE_MAPPER;

@RestController
@RequestMapping("/api/cloud-service-providers")
@RequiredArgsConstructor
public class CloudServiceProviderController {

    private final CloudServiceProviderRegionSvc cloudServiceProviderRegionSvc;
    private final CloudServiceProviderSvc cloudServiceProviderSvc;
    private final CloudServiceProviderServiceSvc cloudServiceProviderServiceSvc;

    @GetMapping("/{cspId}/regions")
    @IsMember
    public ResponseEntity<List<CloudServiceProviderRegionResponse>> findAllRegionsByCsp(@PathVariable UUID cspId) {
        var cloudServiceProviderRegions = cloudServiceProviderRegionSvc.findAllByCsp(cspId);
        return ResponseEntity.ok(REGION_AND_RESPONSE_MAPPER.toResponse(cloudServiceProviderRegions));
    }

    @GetMapping
    @IsMember
    public ResponseEntity<List<CloudServiceProviderResponse>> findAllCsp() {
        var serviceProviders = cloudServiceProviderSvc.findAll();
        return ResponseEntity.ok(PROVIDER_AND_RESPONSE_MAPPER.toResponse(serviceProviders));
    }

    @GetMapping("/{cspId}/services")
    @IsMember
    public ResponseEntity<List<CloudServiceProviderServiceResponse>> findAllServicesByCsp(@PathVariable UUID cspId) {
        var cloudServiceProviderServices = cloudServiceProviderServiceSvc.findAllByCsp(cspId);
        return ResponseEntity.ok(SERVICE_AND_RESPONSE_MAPPER.toResponse(cloudServiceProviderServices));
    }

}
