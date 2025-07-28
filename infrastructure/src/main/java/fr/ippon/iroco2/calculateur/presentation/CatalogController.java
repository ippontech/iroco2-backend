package fr.ippon.iroco2.calculateur.presentation;

import fr.ippon.iroco2.calculateur.presentation.mapper.CloudServiceProviderServiceMapper;
import fr.ippon.iroco2.calculateur.presentation.reponse.CloudServiceProviderServiceResponse;
import fr.ippon.iroco2.domain.calculateur.api.CloudServiceProviderServiceSvc;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/public/v2/catalog")
@RequiredArgsConstructor
public class CatalogController {
    public static final CloudServiceProviderServiceMapper SERVICE_MAPPER = Mappers.getMapper(CloudServiceProviderServiceMapper.class);

    private final CloudServiceProviderServiceSvc cloudServiceProviderServiceSvc;

    @GetMapping("/services")
    public ResponseEntity<List<CloudServiceProviderServiceResponse>> getAllServices() {
        var catalog = cloudServiceProviderServiceSvc.findAll();
        return ResponseEntity.ok(SERVICE_MAPPER.toResponse(catalog));
    }

    @GetMapping("/services/{serviceId}")
    public ResponseEntity<CloudServiceProviderServiceResponse> getServiceById(@PathVariable UUID serviceId) {
        var service = cloudServiceProviderServiceSvc.findServiceById(serviceId);
        return ResponseEntity.ok(SERVICE_MAPPER.toResponse(service));
    }
}
