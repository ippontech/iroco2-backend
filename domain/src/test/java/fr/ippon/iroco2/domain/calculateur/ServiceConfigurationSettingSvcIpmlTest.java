package fr.ippon.iroco2.domain.calculateur;

import fr.ippon.iroco2.domain.calculateur.model.ServiceConfigurationSetting;
import fr.ippon.iroco2.domain.calculateur.spi.ServiceConfigurationSettingStorage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ServiceConfigurationSettingSvcIpmlTest {
    @Mock
    private ServiceConfigurationSettingStorage storage;
    @InjectMocks
    private ServiceConfigurationSettingSvcIpml svc;

    @Test
    void find_should_call_storage() {
        //given
        UUID id = UUID.randomUUID();
        var list = List.of(mock(ServiceConfigurationSetting.class));
        when(storage.findAllByServiceId(id)).thenReturn(list);
        //when
        var result = svc.findAllByService(id);
        //then
        assertThat(result).isEqualTo(list);
    }
}
