package fr.ippon.iroco2.domain.scanner;

import fr.ippon.iroco2.domain.commons.exception.FunctionalException;
import fr.ippon.iroco2.domain.commons.exception.UnauthorizedActionException;
import fr.ippon.iroco2.domain.commons.model.Payload;
import fr.ippon.iroco2.domain.commons.model.ReportStatus;
import fr.ippon.iroco2.domain.commons.spi.SessionProvider;
import fr.ippon.iroco2.domain.commons.svc.DateProvider;
import fr.ippon.iroco2.domain.estimateur.CarbonEstimator;
import fr.ippon.iroco2.domain.scanner.exception.ScanNotFoundException;
import fr.ippon.iroco2.domain.scanner.model.Scan;
import fr.ippon.iroco2.domain.scanner.spi.ScanStorage;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;

import static fr.ippon.iroco2.domain.commons.model.ReportStatus.IN_PROGRESS;
import static fr.ippon.iroco2.domain.commons.model.ReportStatus.SUCCESS;
import static java.util.UUID.randomUUID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentCaptor.forClass;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ScannerSvcTest {
    private static final String AWS_ACCOUNT_ID = "123456789012";
    private static final String OWNER_EMAIL = "validUser";
    private static final LocalDateTime NOW = LocalDateTime.of(2024, 4, 1, 12, 0);

    @Mock
    private ScanStorage scanStorage;
    @Mock
    private SessionProvider sessionProvider;
    @Mock
    private DateProvider dateProvider;
    @Mock
    private CarbonEstimator carbonEstimator;
    @InjectMocks
    private ScannerSvcImpl scannerSvc;

    private static Payload createPayload(UUID scan, int expectedPayloads) {
        return new Payload(scan, "countryCode", "name", Duration.ZERO, expectedPayloads, new HashMap<>());
    }

    private static Scan createSavedScan(String currentUser) {
        return Scan.load(
                randomUUID(),
                currentUser,
                ReportStatus.CREATED,
                LocalDateTime.now(),
                new ArrayList<>(),
                AWS_ACCOUNT_ID
        );
    }

    @Test
    void create_should_save_a_scan() {
        // GIVEN
        when(sessionProvider.getConnectedAwsAccountId()).thenReturn(AWS_ACCOUNT_ID);
        when(sessionProvider.getConnectedUserEmail()).thenReturn(OWNER_EMAIL);
        when(dateProvider.now()).thenReturn(NOW);

        // WHEN
        scannerSvc.create();

        // THEN
        ArgumentCaptor<Scan> scanCaptor = forClass(Scan.class);
        verify(scanStorage).save(scanCaptor.capture());

        Scan savedScan = scanCaptor.getValue();
        assertThat(savedScan).isNotNull();
        assertThat(savedScan.getOwner()).isEqualTo(OWNER_EMAIL);
        assertThat(savedScan.getAwsAccountId()).isEqualTo(AWS_ACCOUNT_ID);
        assertThat(savedScan.getCreationDate()).isEqualTo(NOW);
    }

    @Test
    void create_should_generate_a_valid_uuid() {
        // GIVEN
        when(sessionProvider.getConnectedAwsAccountId()).thenReturn(AWS_ACCOUNT_ID);
        when(sessionProvider.getConnectedUserEmail()).thenReturn(OWNER_EMAIL);
        when(dateProvider.now()).thenReturn(NOW);
        UUID expectedUuid = randomUUID();
        when(scanStorage.save(any())).thenReturn(expectedUuid);

        // WHEN
        UUID uuid = scannerSvc.create();

        // THEN
        assertThat(uuid).isEqualTo(expectedUuid);

        ArgumentCaptor<Scan> scanCaptor = forClass(Scan.class);
        verify(scanStorage, times(1)).save(scanCaptor.capture());

        Scan savedScan = scanCaptor.getValue();
        assertThat(savedScan).isNotNull();
        assertThat(savedScan.getOwner()).isEqualTo(OWNER_EMAIL);
        assertThat(savedScan.getAwsAccountId()).isEqualTo(AWS_ACCOUNT_ID);
        assertThat(savedScan.getCreationDate()).isEqualTo(NOW);
    }

    @Test
    void delete_should_delete_a_scan_when_scan_exist() {
        // GIVEN
        UUID scanId = randomUUID();
        Scan existingScan = createSavedScan("current-user");
        when(scanStorage.findById(scanId)).thenReturn(Optional.of(existingScan));

        // WHEN
        scannerSvc.delete(scanId);

        // THEN
        verify(scanStorage).findById(scanId);
        verify(scanStorage).delete(existingScan.getId());
    }

    @Test
    void delete_should_throw_not_found_exception_when_scan_does_not_exist() {
        // GIVEN
        UUID scanId = randomUUID();
        when(scanStorage.findById(scanId)).thenReturn(Optional.empty());

        // WHEN
        Throwable error = catchThrowable(() -> scannerSvc.delete(scanId));

        // THEN
        assertThat(error)
                .isInstanceOf(ScanNotFoundException.class)
                .hasMessage("The scan with ID '%s' is not found".formatted(scanId));
        verify(scanStorage).findById(scanId);
        verify(scanStorage, never()).delete(any());
    }

    @Test
    void findById_should_return_scan_when_it_exists() {
        // GIVEN
        UUID scanId = randomUUID();
        var scan = Scan.load(scanId, OWNER_EMAIL, IN_PROGRESS, NOW, new ArrayList<>(), AWS_ACCOUNT_ID);
        when(scanStorage.findById(scanId)).thenReturn(java.util.Optional.of(scan));
        when(sessionProvider.getConnectedUserEmail()).thenReturn(OWNER_EMAIL);

        // WHEN
        var result = scannerSvc.findById(scanId);

        // THEN
        assertThat(result).isEqualTo(scan);
    }

    @Test
    void findById_should_throw_exception_if_not_owner() {
        //given
        UUID id = randomUUID();
        Scan scan = mock(Scan.class);
        when(scan.getOwner()).thenReturn("owner");
        when(scanStorage.findById(id)).thenReturn(Optional.of(scan));

        String connectedUser = "not owner";
        when(sessionProvider.getConnectedUserEmail()).thenReturn(connectedUser);

        //when
        var exception = catchThrowable(() -> scannerSvc.findById(id));

        //then
        assertThat(exception)
                .isInstanceOf(UnauthorizedActionException.class)
                .hasMessage("%s n'est pas autorisé à accéder à cette ressource".formatted(connectedUser));
    }

    @Test
    void findById_should_throw_exception_when_scan_does_not_exist() {
        // GIVEN
        UUID scanId = randomUUID();
        when(scanStorage.findById(scanId)).thenReturn(java.util.Optional.empty());

        // WHEN
        Assertions.assertThatThrownBy(() -> scannerSvc.findById(scanId))
                .isInstanceOf(ScanNotFoundException.class)
                .hasMessage("The scan with ID '%s' is not found".formatted(scanId));
    }

    @Test
    void findAll_should_filter_scans_by_owner() {
        // GIVEN
        String owner = "validUser";
        when(sessionProvider.getConnectedUserEmail()).thenReturn(owner);
        var savedScan = new ArrayList<Scan>();
        when(scanStorage.findByOwner(owner)).thenReturn(savedScan);

        // WHEN
        var scans = scannerSvc.findAll();

        // THEN
        assertThat(scans).isEqualTo(savedScan);
    }

    @Test
    void addEstimation_should_throw_when_missing_scan() {
        // GIVEN
        var payload = createPayload(randomUUID(), 0);
        when(scanStorage.findById(payload.reportId())).thenReturn(Optional.empty());

        // WHEN
        var error = catchThrowable(() -> scannerSvc.addEstimation(payload));

        // THEN
        assertThat(error)
                .isInstanceOf(ScanNotFoundException.class)
                .hasMessage("The scan with ID '%s' is not found".formatted(payload.reportId()));
    }

    @Test
    void scanStatus_isEqualTo_inProcess_afterFirstPayloadAdding() throws FunctionalException {
        // GIVEN
        var scan = givenExistingScan();
        var payload = createPayload(scan.getId(), 0);

        // WHEN
        scannerSvc.addEstimation(payload);

        // THEN
        var savedScan = getSavedScan();
        assertThat(savedScan.getStatus()).isEqualTo(IN_PROGRESS);
    }

    @Test
    void scanStatus_isEqualTo_success_afterLastPayloadAdding() throws FunctionalException {
        // GIVEN
        var scan = givenExistingScan();
        var payload = createPayload(scan.getId(), scan.getPayloads().size() + 1);

        // WHEN
        scannerSvc.addEstimation(payload);

        // THEN
        var savedScan = getSavedScan();
        assertThat(savedScan.getStatus()).isEqualTo(SUCCESS);
        assertThat(savedScan.getPayloads()).hasSize(payload.expectedPayloads());
    }

    @Test
    void addEstimation_should_call_carbonEstimator() throws FunctionalException {
        //given
        Payload payload = mock(Payload.class);
        when(payload.countryIsoCode()).thenReturn("isocode");
        when(scanStorage.findById(any())).thenReturn(Optional.of(mock(Scan.class)));

        //when
        scannerSvc.addEstimation(payload);

        //then
        verify(carbonEstimator).estimatePayload("isocode", payload);
    }

    private Scan givenExistingScan() {
        var scan = createSavedScan(OWNER_EMAIL);
        when(scanStorage.findById(scan.getId())).thenReturn(Optional.of(scan));
        return scan;
    }

    private Scan getSavedScan() {
        ArgumentCaptor<Scan> captor = forClass(Scan.class);
        verify(scanStorage).save(captor.capture());
        return captor.getValue();
    }
}

