package fr.ippon.iroco2.domain.calculateur;

import fr.ippon.iroco2.domain.calculateur.model.CloudServiceProvider;
import fr.ippon.iroco2.domain.calculateur.spi.CloudServiceProviderStorage;
import fr.ippon.iroco2.domain.commons.exception.NotFoundException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CloudServiceProviderSvcTest {
    @Mock
    private CloudServiceProviderStorage cloudServiceProviderStorage;

    @InjectMocks
    private CloudServiceProviderSvcImpl cloudServiceProviderSvc;

    @Test
    void findById_shouldReturnCloudServiceProvider_whenEntityExists() {
        // Arrange
        UUID validUUID = UUID.randomUUID();
        CloudServiceProvider provider = new CloudServiceProvider();
        Mockito.when(cloudServiceProviderStorage.findById(validUUID)).thenReturn(Optional.of(provider));

        // Act
        var result = cloudServiceProviderSvc.findById(validUUID);

        // Assert
        Assertions.assertThat(result).isNotNull();
        Mockito.verify(cloudServiceProviderStorage, times(1)).findById(validUUID);
    }

    @Test
    void findById_shouldThrowNotFoundException_whenEntityNotFound() {
        // Arrange
        UUID validUUID = UUID.randomUUID();
        when(cloudServiceProviderStorage.findById(validUUID)).thenReturn(Optional.empty());

        // Act
        var error = Assertions.catchThrowable(() -> cloudServiceProviderSvc.findById(validUUID));

        // Assert
        Assertions.assertThat(error).isInstanceOf(NotFoundException.class);
        Mockito.verify(cloudServiceProviderStorage, times(1)).findById(validUUID);
    }
}