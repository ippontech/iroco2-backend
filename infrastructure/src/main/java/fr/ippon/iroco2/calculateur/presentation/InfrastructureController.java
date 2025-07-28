package fr.ippon.iroco2.calculateur.presentation;

import fr.ippon.iroco2.calculateur.presentation.mapper.ComponentMapper;
import fr.ippon.iroco2.calculateur.presentation.reponse.CarbonFootprintResponse;
import fr.ippon.iroco2.calculateur.presentation.reponse.InfrastructureResponse;
import fr.ippon.iroco2.calculateur.presentation.reponse.RegionCarbonFootprintResponse;
import fr.ippon.iroco2.calculateur.presentation.request.InfrastructureRequest;
import fr.ippon.iroco2.common.presentation.security.IsMember;
import fr.ippon.iroco2.domain.calculateur.api.EstimationSvc;
import fr.ippon.iroco2.domain.calculateur.api.InfrastructureSvc;
import fr.ippon.iroco2.domain.calculateur.model.Infrastructure;
import fr.ippon.iroco2.domain.commons.exception.FunctionalException;
import fr.ippon.iroco2.domain.commons.spi.SessionProvider;
import jakarta.validation.Valid;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v2/infrastructures")
@RequiredArgsConstructor
public class InfrastructureController {

    private static final ComponentMapper COMPONENT_MAPPER = Mappers.getMapper(ComponentMapper.class);
    private final InfrastructureSvc infrastructureService;
    private final EstimationSvc estimationSvc;
    private final SessionProvider sessionProvider;

    @PostMapping
    @IsMember
    @ResponseStatus(HttpStatus.CREATED)
    public void save(@RequestBody @Valid InfrastructureRequest request) {
        String owner = sessionProvider.getConnectedUserEmail();
        Infrastructure infrastructure = request.createToDomain(owner);
        infrastructureService.save(infrastructure);
    }

    @IsMember
    @GetMapping("/{id}")
    public ResponseEntity<InfrastructureResponse> findById(@PathVariable UUID id) {
        var infrastructure = infrastructureService.findById(id);
        InfrastructureResponse response = COMPONENT_MAPPER.toResponse(infrastructure);
        return ResponseEntity.ok(response);
    }

    @IsMember
    @GetMapping("/{id}/carbon-footprint")
    public ResponseEntity<List<CarbonFootprintResponse>> estimateCarbonFootprint(@PathVariable UUID id)
            throws FunctionalException {
        var carbonFootprintByComponent = estimationSvc.estimateCarbonFootprintByInfrastructureId(id);

        List<CarbonFootprintResponse> responses = new ArrayList<>();
        carbonFootprintByComponent.forEach(
                (component, co2Gr) -> responses.add(CarbonFootprintResponse.createFrom(component, co2Gr))
        );

        return ResponseEntity.ok(responses);
    }

    @IsMember
    @GetMapping("/{id}/byregion-carbon-footprint")
    public ResponseEntity<List<RegionCarbonFootprintResponse>> estimateCarbonFootprintForAllRegions(
            @PathVariable UUID id
    ) throws FunctionalException {
        var carbonFootprintByComponent = estimationSvc.estimateCarbonFootprintByInfrastructureIdForAllRegions(id);

        List<RegionCarbonFootprintResponse> responses = new ArrayList<>();
        carbonFootprintByComponent.forEach(
                (region, co2Gr) -> responses.add(RegionCarbonFootprintResponse.createFrom(region, co2Gr))
        );

        return ResponseEntity.ok(responses);
    }

    @IsMember
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable UUID id) {
        infrastructureService.delete(id);
    }

    @IsMember
    @GetMapping
    public ResponseEntity<List<InfrastructureResponse>> getInfrastructures() {
        var infrastructures = infrastructureService.findAll();
        List<InfrastructureResponse> responses = infrastructures.stream().map(COMPONENT_MAPPER::toResponse).toList();
        return ResponseEntity.ok(responses);
    }
}
