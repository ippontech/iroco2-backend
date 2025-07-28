package fr.ippon.iroco2.domain.calculateur;

import fr.ippon.iroco2.domain.calculateur.api.InfrastructureSvc;
import fr.ippon.iroco2.domain.calculateur.model.CloudServiceProviderRegion;
import fr.ippon.iroco2.domain.calculateur.model.CloudServiceProviderService;
import fr.ippon.iroco2.domain.calculateur.model.Component;
import fr.ippon.iroco2.domain.calculateur.model.ConfiguredSetting;
import fr.ippon.iroco2.domain.calculateur.model.Infrastructure;
import fr.ippon.iroco2.domain.calculateur.spi.CloudServiceProviderRegionStorage;
import fr.ippon.iroco2.domain.calculateur.spi.CloudServiceProviderServiceStorage;
import fr.ippon.iroco2.domain.calculateur.spi.ComponentStorage;
import fr.ippon.iroco2.domain.calculateur.spi.ConfigurationSettingStorage;
import fr.ippon.iroco2.domain.commons.exception.NotFoundException;
import fr.ippon.iroco2.domain.commons.svc.DateProvider;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static java.util.UUID.randomUUID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ComponentSvcImplTest {
    @Mock
    private InfrastructureSvc infrastructureSvc;
    @Mock
    private CloudServiceProviderRegionStorage regionStorage;
    @Mock
    private CloudServiceProviderServiceStorage serviceStorage;
    @Mock
    private DateProvider dateProvider;
    @Mock
    private ComponentStorage componentStorage;
    @Mock
    private ConfigurationSettingStorage configurationSettingStorage;

    @InjectMocks
    @Spy
    private ComponentSvcImpl componentSvc;

    private static Component givenComponent(UUID infraId, UUID regionId, UUID serviceId) {
        Component component = mock(Component.class);
        when(component.getInfrastructureID()).thenReturn(infraId);
        when(component.getRegionID()).thenReturn(regionId);
        CloudServiceProviderService componentService = mock(CloudServiceProviderService.class);
        when(componentService.getId()).thenReturn(serviceId);
        when(component.getService()).thenReturn(componentService);
        return component;
    }

    @Test
    void save_should_throw_exception_if_infra_not_found() {
        // GIVEN
        Component componentWithUnknownInfra = mock(Component.class);
        UUID unknownInfra = randomUUID();
        when(componentWithUnknownInfra.getInfrastructureID()).thenReturn(unknownInfra);

        // WHEN
        var exception = catchThrowable(() -> componentSvc.save(componentWithUnknownInfra));

        // THEN
        assertThat(exception)
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Infrastructure not found (%s)".formatted(unknownInfra));
    }

    @Test
    void save_should_throw_exception_if_region_of_component_not_found() {
        // GIVEN
        UUID knownInfraId = givenInfra();

        UUID regionId = randomUUID();
        Component component = mock(Component.class);
        when(component.getInfrastructureID()).thenReturn(knownInfraId);
        when(component.getRegionID()).thenReturn(regionId);

        // WHEN
        var exception = catchThrowable(() -> componentSvc.save(component));

        // THEN
        assertThat(exception)
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Region not found (%s)".formatted(regionId));
    }

    private UUID givenInfra() {
        UUID knownInfraId = randomUUID();
        Infrastructure infra = mock(Infrastructure.class);
        when(infrastructureSvc.findById(knownInfraId)).thenReturn(infra);
        return knownInfraId;
    }

    @Test
    void save_should_throw_exception_if_no_region_component_and_not_found_infra_region() {
        // GIVEN
        UUID knownInfraId = randomUUID();
        UUID infraRegionId = randomUUID();
        Infrastructure infra = mock(Infrastructure.class);
        when(infrastructureSvc.findById(knownInfraId)).thenReturn(infra);
        when(infra.defaultRegionId()).thenReturn(infraRegionId);

        Component componentWithUnknownRegion = mock(Component.class);
        when(componentWithUnknownRegion.getInfrastructureID()).thenReturn(knownInfraId);
        when(componentWithUnknownRegion.getRegionID()).thenReturn(null).thenReturn(infraRegionId);

        // WHEN
        var exception = catchThrowable(() -> componentSvc.save(componentWithUnknownRegion));

        // THEN
        verify(componentWithUnknownRegion).setRegionID(infraRegionId);
        assertThat(exception)
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Region not found (%s)".formatted(infraRegionId));

    }

    @Test
    void save_should_throw_exception_if_not_found_service_component() {
        // GIVEN
        UUID knownInfraId = randomUUID();
        when(infrastructureSvc.findById(knownInfraId)).thenReturn(mock(Infrastructure.class));

        UUID regionId = randomUUID();
        when(regionStorage.findById(regionId)).thenReturn(Optional.of(mock(CloudServiceProviderRegion.class)));

        UUID serviceId = randomUUID();
        CloudServiceProviderService service = mock(CloudServiceProviderService.class);
        when(service.getId()).thenReturn(serviceId);

        Component componentWithUnknownRegion = Component.create(knownInfraId, null, regionId, service, null);

        // WHEN
        var exception = catchThrowable(() -> componentSvc.save(componentWithUnknownRegion));

        // THEN
        assertThat(exception)
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Service not found (%s)".formatted(serviceId));

    }

    @Test
    void save_should_replace_no_component_region_by_infra_region() {
        // GIVEN
        UUID infraId = randomUUID();
        UUID infraRegionId = randomUUID();
        Infrastructure infra = mock(Infrastructure.class);
        when(infra.defaultRegionId()).thenReturn(infraRegionId);
        when(infrastructureSvc.findById(infraId)).thenReturn(infra);

        when(regionStorage.findById(infraRegionId)).thenReturn(Optional.of(mock(CloudServiceProviderRegion.class)));

        UUID serviceId = randomUUID();
        CloudServiceProviderService service = givenCloudServiceProviderService(serviceId);
        when(service.getId()).thenReturn(serviceId);

        Component component = mock(Component.class);
        when(component.getInfrastructureID()).thenReturn(infraId);
        when(component.getRegionID()).thenReturn(null).thenReturn(infraRegionId);
        when(component.getService()).thenReturn(service);

        // WHEN
        componentSvc.save(component);

        // THEN
        verify(component).setRegionID(infraRegionId);
    }

    @Test
    void save_should_reload_service_in_component() {
        // GIVEN
        UUID infraId = givenInfra();

        UUID regionId = randomUUID();
        when(regionStorage.findById(regionId)).thenReturn(Optional.of(mock(CloudServiceProviderRegion.class)));

        UUID serviceId = randomUUID();
        CloudServiceProviderService storageService = givenCloudServiceProviderService(serviceId);

        Component component = givenComponent(infraId, regionId, serviceId);

        // WHEN
        componentSvc.save(component);

        // THEN
        verify(component).setService(storageService);
    }

    private CloudServiceProviderService givenCloudServiceProviderService(UUID serviceId) {
        CloudServiceProviderService storageService = mock(CloudServiceProviderService.class);
        when(serviceStorage.findById(serviceId)).thenReturn(Optional.of(storageService));
        return storageService;
    }

    @Test
    void save_should_throw_exception_if_not_found_config() {
        // GIVEN
        UUID infraId = givenInfra();

        UUID regionId = randomUUID();
        when(regionStorage.findById(regionId)).thenReturn(Optional.of(mock(CloudServiceProviderRegion.class)));

        UUID serviceId = randomUUID();
        givenCloudServiceProviderService(serviceId);

        Component component = givenComponent(infraId, regionId, serviceId);

        ConfiguredSetting conf = mock(ConfiguredSetting.class);
        UUID confId = randomUUID();
        when(conf.configurationSettingId()).thenReturn(confId);
        when(component.getConfigurationValues()).thenReturn(List.of(conf));

        // WHEN
        var exception = catchThrowable(() -> componentSvc.save(component));

        // THEN
        verify(configurationSettingStorage).findById(confId);
        assertThat(exception)
                .isInstanceOf(NotFoundException.class)
                .hasMessage("ConfigurationSetting not found (%s)".formatted(confId));
    }

    @Test
    void save_should_update_last_modification_date() {
        // GIVEN
        UUID infraId = givenInfra();

        UUID regionId = randomUUID();
        when(regionStorage.findById(regionId)).thenReturn(Optional.of(mock(CloudServiceProviderRegion.class)));

        UUID serviceId = randomUUID();
        givenCloudServiceProviderService(serviceId);

        Component component = givenComponent(infraId, regionId, serviceId);

        LocalDateTime now = givenDateProvider();

        // WHEN
        componentSvc.save(component);

        // THEN
        verify(component).setLastModificationDate(now);
    }

    @Test
    void save_should_save_in_storage() {
        // GIVEN
        UUID infraId = givenInfra();

        UUID regionId = randomUUID();
        when(regionStorage.findById(regionId)).thenReturn(Optional.of(mock(CloudServiceProviderRegion.class)));

        UUID serviceId = randomUUID();
        givenCloudServiceProviderService(serviceId);

        Component component = givenComponent(infraId, regionId, serviceId);

        givenDateProvider();

        // WHEN
        componentSvc.save(component);

        // THEN
        verify(componentStorage).save(component);
    }

    @Test
    void delete_should_throw_exception_if_not_found_component() {
        // GIVEN
        UUID componentId = randomUUID();

        // WHEN
        var exception = catchThrowable(() -> componentSvc.delete(componentId));

        // THEN
        assertThat(exception)
                .isInstanceOf(NotFoundException.class)
                .hasMessage("The component with ID '%s' is not found".formatted(componentId));
    }

    @Test
    void delete_should_call_delete_storage() {
        // GIVEN
        UUID componentId = randomUUID();
        Component component = mock(Component.class);
        when(componentStorage.findById(componentId)).thenReturn(Optional.of(component));

        // WHEN
        componentSvc.delete(componentId);

        // THEN
        verify(componentStorage).delete(component);
    }

    @Test
    void find_all_should_call_find_all_storage() {
        // GIVEN
        UUID infraId = randomUUID();

        // WHEN
        componentSvc.findAllByInfrastructureID(infraId);

        // THEN
        verify(componentStorage).findByInfrastructureId(infraId);
    }

    @Test
    void update_should_throw_exception_if_not_found_component() {
        // GIVEN
        UUID componentId = randomUUID();
        Component component = mock(Component.class);
        when(component.getId()).thenReturn(componentId);

        // WHEN
        var exception = catchThrowable(() -> componentSvc.update(component));

        // THEN
        assertThat(exception)
                .isInstanceOf(NotFoundException.class)
                .hasMessage("The component with ID '%s' is not found".formatted(componentId));
    }

    @Test
    void update_should_call_save() {
        // GIVEN
        UUID infraId = givenInfra();

        UUID regionId = randomUUID();
        when(regionStorage.findById(regionId)).thenReturn(Optional.of(mock(CloudServiceProviderRegion.class)));

        UUID serviceId = randomUUID();
        givenCloudServiceProviderService(serviceId);

        Component component = givenComponent(infraId, regionId, serviceId);

        UUID componentId = randomUUID();
        when(component.getId()).thenReturn(componentId);
        when(componentStorage.findById(componentId)).thenReturn(Optional.of(component));

        givenDateProvider();

        // WHEN
        componentSvc.update(component);

        // THEN
        verify(componentSvc).save(component);
    }

    private LocalDateTime givenDateProvider() {
        LocalDateTime now = LocalDateTime.now();
        when(dateProvider.now()).thenReturn(now);
        return now;
    }
}
