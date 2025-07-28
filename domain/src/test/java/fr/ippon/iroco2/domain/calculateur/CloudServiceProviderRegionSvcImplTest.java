package fr.ippon.iroco2.domain.calculateur;

import fr.ippon.iroco2.domain.calculateur.model.CloudServiceProviderRegion;
import fr.ippon.iroco2.domain.calculateur.spi.CloudServiceProviderRegionStorage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static java.util.UUID.randomUUID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CloudServiceProviderRegionSvcImplTest {
    @Mock
    private CloudServiceProviderRegionStorage storage;
    @InjectMocks
    private CloudServiceProviderRegionSvcImpl svc;

    @Test
    void find_should_call_storage() {
        //given
        UUID id = randomUUID();
        var listRegions = List.of(mock(CloudServiceProviderRegion.class));
        when(storage.findAllByCsp(id)).thenReturn(listRegions);
        //when
        var result = svc.findAllByCsp(id);
        //then
        assertThat(result).isEqualTo(listRegions);
    }
}
