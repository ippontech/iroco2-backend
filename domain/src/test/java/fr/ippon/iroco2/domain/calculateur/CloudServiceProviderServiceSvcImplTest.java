package fr.ippon.iroco2.domain.calculateur;

import fr.ippon.iroco2.domain.calculateur.model.CloudServiceProviderService;
import fr.ippon.iroco2.domain.calculateur.spi.CloudServiceProviderServiceStorage;
import fr.ippon.iroco2.domain.commons.exception.NotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static java.util.UUID.randomUUID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CloudServiceProviderServiceSvcImplTest {
    @Mock
    private CloudServiceProviderServiceStorage storage;
    @InjectMocks
    private CloudServiceProviderServiceSvcImpl svc;

    @Test
    void findAllByCsp_should_call_storage() {
        //given
        UUID cspId = randomUUID();
        List<CloudServiceProviderService> list = List.of(mock(CloudServiceProviderService.class));
        when(storage.findAllByCsp(cspId)).thenReturn(list);
        //when
        var result = svc.findAllByCsp(cspId);
        //then
        assertThat(result).isEqualTo(list);
    }

    @Test
    void findAll_should_call_storage() {
        //given
        List<CloudServiceProviderService> list = List.of(mock(CloudServiceProviderService.class));
        when(storage.findAll()).thenReturn(list);
        //when
        var result = svc.findAll();
        //then
        assertThat(result).isEqualTo(list);
    }

    @Test
    void findServiceById_should_throw_exception_if_not_found_service() {
        //given
        UUID serviceId = randomUUID();
        //when
        var exception = catchThrowable(() -> svc.findServiceById(serviceId));
        //then
        assertThat(exception)
                .isInstanceOf(NotFoundException.class)
                .hasMessage("The service with id %s does not exist".formatted(serviceId));
    }

    @Test
    void findServiceById_should_call_storage() {
        //given
        UUID serviceId = randomUUID();
        var service = mock(CloudServiceProviderService.class);
        when(storage.findById(serviceId)).thenReturn(Optional.of(service));
        //when
        var result = svc.findServiceById(serviceId);
        //then
        assertThat(result).isEqualTo(service);
    }
}
