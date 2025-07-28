package fr.ippon.iroco2.domain.calculateur;

import fr.ippon.iroco2.domain.calculateur.api.InfrastructureSvc;
import fr.ippon.iroco2.domain.calculateur.exception.InfrastructureNotFound;
import fr.ippon.iroco2.domain.calculateur.model.CloudServiceProviderRegion;
import fr.ippon.iroco2.domain.calculateur.model.Component;
import fr.ippon.iroco2.domain.calculateur.model.Infrastructure;
import fr.ippon.iroco2.domain.calculateur.spi.CloudServiceProviderRegionStorage;
import fr.ippon.iroco2.domain.commons.exception.FunctionalException;
import fr.ippon.iroco2.domain.commons.exception.NotFoundException;
import fr.ippon.iroco2.domain.estimateur.CarbonEstimator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static java.util.UUID.randomUUID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EstimationSvcTest {
    @Mock
    private CloudServiceProviderRegionStorage cloudServiceProviderRegionStorage;
    @Mock
    private InfrastructureSvc infrastructureSvc;
    @Mock
    private CarbonEstimator carbonEstimator;
    @InjectMocks
    private EstimationSvcImpl estimationSvc;

    private Component givenComponent(int i, boolean withIsrael) throws FunctionalException {
        var component = Component.load(
                null,
                null,
                String.valueOf(i),
                null,
                null,
                null,
                List.of());
        when(carbonEstimator.estimateComponent("DEU", component)).thenReturn(i);
        if (withIsrael)
            when(carbonEstimator.estimateComponent("ISR", component)).thenReturn(i * 10);
        return component;
    }

    @Test
    void estimate_should_throw_exception_if_infrastructure_not_found() {
        // GIVEN
        UUID unknownInfraId = randomUUID();

        // WHEN
        when(infrastructureSvc.findById(any())).thenThrow(new InfrastructureNotFound(unknownInfraId));
        var error = catchThrowable(() -> estimationSvc.estimateCarbonFootprintByInfrastructureId(unknownInfraId));

        // THEN
        assertThat(error)
                .isInstanceOf(InfrastructureNotFound.class)
                .hasMessage("L'infrastructure d'id '%s' n'existe pas".formatted(unknownInfraId));
    }

    private Infrastructure givenInfra() {
        UUID infraId = randomUUID();
        Infrastructure infra = new Infrastructure(
                infraId,
                null,
                null,
                null,
                null,
                new ArrayList<>());
        when(infrastructureSvc.findById(infraId)).thenReturn(infra);
        return infra;
    }

    @Test
    void estimate_should_throw_exception_if_region_not_found() {
        // GIVEN
        Infrastructure infra = givenInfra();
        UUID infraId = infra.id();
        infra.components().add(Component.load(
                null,
                null,
                null,
                null,
                null,
                null,
                null));

        // WHEN
        var error = catchThrowable(() -> estimationSvc.estimateCarbonFootprintByInfrastructureId(infraId));

        // THEN
        assertThat(error)
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Region not found");
    }

    @Test
    void estimate_should_return_a_map_with_components_as_key_and_its_estimation() throws FunctionalException {
        // GIVEN
        Infrastructure infra = givenInfra();
        UUID infraId = infra.id();
        for (int i = 0; i < 5; i++) {
            infra.components().add(givenComponent(i, false));
        }
        var cloudServiceProviderRegion = new CloudServiceProviderRegion();
        cloudServiceProviderRegion.setName("Europe (Frankfurt)");
        when(cloudServiceProviderRegionStorage.findById(any()))
                .thenReturn(Optional.of(cloudServiceProviderRegion));

        // WHEN
        var result = estimationSvc.estimateCarbonFootprintByInfrastructureId(infraId);

        // THEN
        assertThat(result).hasSize(5);
        assertThat(result.keySet().stream().map(Component::getName)).containsExactlyInAnyOrderElementsOf(List.of("0", "1", "2", "3", "4"));
        result.forEach((key, value) -> assertThat(Integer.parseInt(key.getName())).isEqualTo(value));
    }

    @Test
    void estimate_all_region_should_return_empty_map_if_no_region() throws FunctionalException {
        // GIVEN
        Infrastructure infra = givenInfra();
        UUID infraId = infra.id();

        // WHEN
        var result = estimationSvc.estimateCarbonFootprintByInfrastructureIdForAllRegions(infraId);

        // THEN
        assertThat(result).isEmpty();
    }

    private CloudServiceProviderRegion givenCloudServiceProviderRegion(String name) {
        var cloudServiceProviderRegion = new CloudServiceProviderRegion();
        UUID regionId = randomUUID();
        cloudServiceProviderRegion.setId(regionId);
        cloudServiceProviderRegion.setName(name);
        when(cloudServiceProviderRegionStorage.findById(regionId))
                .thenReturn(Optional.of(cloudServiceProviderRegion));
        return cloudServiceProviderRegion;
    }

    @Test
    void estimate_all_region_should_return_a_map_with_estimation_of_all_components_group_by_region()
            throws FunctionalException {
        // GIVEN
        Infrastructure infra = givenInfra();
        UUID infraId = infra.id();
        CloudServiceProviderRegion europe = givenCloudServiceProviderRegion("Europe (Frankfurt)");
        CloudServiceProviderRegion israel = givenCloudServiceProviderRegion("Israel (Tel Aviv)");
        when(cloudServiceProviderRegionStorage.findAll()).thenReturn(List.of(europe, israel));

        for (int i = 1; i < 3; i++) {
            infra.components().add(givenComponent(i, true));
        }

        // WHEN
        var result = estimationSvc.estimateCarbonFootprintByInfrastructureIdForAllRegions(infraId);

        // THEN
        assertThat(result).hasSize(2);
        assertThat(result.keySet().stream().map(CloudServiceProviderRegion::getName))
                .containsExactlyInAnyOrderElementsOf(List.of("Europe (Frankfurt)", "Israel (Tel Aviv)"));
        result.forEach(this::checkEstimationByRegion);
    }

    private void checkEstimationByRegion(CloudServiceProviderRegion region, Integer estimation) {
        switch (region.getName()) {
            case "Europe (Frankfurt)" -> assertThat(estimation).isEqualTo(3);
            case "Israel (Tel Aviv)" -> assertThat(estimation).isEqualTo(30);
            default -> fail("Region not found: " + region.getName());
        }
    }
}