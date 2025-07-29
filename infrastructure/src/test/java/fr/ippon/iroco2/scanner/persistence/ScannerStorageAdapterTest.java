/*
 * Copyright 2025 Ippon Technologies
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package fr.ippon.iroco2.scanner.persistence;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import fr.ippon.iroco2.common.persistance.entity.EstimatedPayloadEntity;
import fr.ippon.iroco2.domain.commons.model.EstimatedPayload;
import fr.ippon.iroco2.domain.scanner.model.Scan;
import fr.ippon.iroco2.scanner.persistence.repository.ScannerRepository;
import fr.ippon.iroco2.scanner.persistence.repository.entity.ScanEntity;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ScannerStorageAdapterTest {

    @Mock
    private ScannerRepository scannerRepository;

    @InjectMocks
    private ScannerStorageAdapter scannerStorageAdapter;

    private Scan scan;
    private ScanEntity scanEntity;

    @BeforeEach
    void setUp() {
        scan = Scan.create("validUser", LocalDateTime.now(), "123456789012");

        scanEntity = new ScanEntity();
        scanEntity.setId(scan.getId());
        scanEntity.setPayloads(
            scan
                .getPayloads()
                .stream()
                .map(p -> {
                    try {
                        return invokeFromDomain(scannerStorageAdapter, p);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                })
                .toList()
        );
        scanEntity.setOwner(scan.getOwner());
        scanEntity.setAwsAccountId(scan.getAwsAccountId());
        scanEntity.setStatus(scan.getStatus());
        scanEntity.setPayloads(new ArrayList<>());
    }

    @Test
    void save_should_persist_and_return_uuid() {
        // GIVEN
        when(scannerRepository.save(any(ScanEntity.class))).thenReturn(scanEntity);

        // WHEN
        UUID result = scannerStorageAdapter.save(scan);

        // THEN
        assertThat(result).isEqualTo(scan.getId());
        verify(scannerRepository, times(1)).save(any(ScanEntity.class));
    }

    @Test
    void findByOwner_should_return_list_of_scans() {
        // GIVEN
        when(scannerRepository.findByOwnerOrderByCreationDateAsc(anyString())).thenReturn(List.of(scanEntity));

        // WHEN
        var result = scannerStorageAdapter.findByOwner("validUser");

        // THEN
        assertThat(result).isNotEmpty();
        assertThat(result.getFirst().getId()).isEqualTo(scan.getId());
        assertThat(result.getFirst().getStatus()).isEqualTo(scan.getStatus());
        assertThat(result.getFirst().getAwsAccountId()).isEqualTo(scan.getAwsAccountId());
        assertThat(result.getFirst().getPayloads()).isEqualTo(scan.getPayloads());
        verify(scannerRepository, times(1)).findByOwnerOrderByCreationDateAsc(anyString());
    }

    @Test
    void findById_should_return_scan() {
        // GIVEN
        when(scannerRepository.findById(any(UUID.class))).thenReturn(java.util.Optional.of(scanEntity));

        // WHEN
        var result = scannerStorageAdapter.findById(scan.getId());

        // THEN
        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(scan.getId());
        assertThat(result.get().getOwner()).isEqualTo(scan.getOwner());
        assertThat(result.get().getAwsAccountId()).isEqualTo(scan.getAwsAccountId());
        assertThat(result.get().getPayloads()).isEqualTo(scan.getPayloads());
        verify(scannerRepository, times(1)).findById(any(UUID.class));
    }

    @Test
    void fromDomain_should_map_scan_to_scanEntity() throws Exception {
        // WHEN
        ScanEntity result = invokeFromDomain(scannerStorageAdapter, scan);

        // THEN
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(scan.getId());
        assertThat(result.getOwner()).isEqualTo(scan.getOwner());
        assertThat(result.getAwsAccountId()).isEqualTo(scan.getAwsAccountId());
        assertThat(result.getStatus()).isEqualTo(scan.getStatus());
    }

    @Test
    void delete_should_remove_scan_by_id() {
        // GIVEN
        UUID scanId = UUID.randomUUID();

        // WHEN
        scannerStorageAdapter.delete(scanId);

        // THEN
        verify(scannerRepository, times(1)).deleteById(scanId);
    }

    @Test
    void findById_should_find_scan_id_if_exists() {
        // GIVEN
        when(scannerRepository.findById(scanEntity.getId())).thenReturn(Optional.of(scanEntity));

        // WHEN
        Optional<Scan> optionalScan = scannerStorageAdapter.findById(scanEntity.getId());

        // THEN
        assertThat(optionalScan.isEmpty()).isFalse();
        verify(scannerRepository, times(1)).findById(scanEntity.getId());
    }

    @Test
    void findById_should_be_empty_optional_if_not_found() {
        // GIVEN
        when(scannerRepository.findById(scanEntity.getId())).thenReturn(Optional.empty());

        // WHEN
        Optional<Scan> optionalScan = scannerStorageAdapter.findById(scanEntity.getId());

        // THEN
        assertThat(optionalScan.isEmpty()).isTrue();
        verify(scannerRepository, times(1)).findById(scanEntity.getId());
    }

    private ScanEntity invokeFromDomain(ScannerStorageAdapter adapter, Scan scan) throws Exception {
        var method = ScannerStorageAdapter.class.getDeclaredMethod("fromDomain", Scan.class);
        method.setAccessible(true);
        return (ScanEntity) method.invoke(adapter, scan);
    }

    private EstimatedPayloadEntity invokeFromDomain(ScannerStorageAdapter adapter, EstimatedPayload payload)
        throws Exception {
        var method = ScannerStorageAdapter.class.getDeclaredMethod("fromDomain", EstimatedPayload.class);
        method.setAccessible(true);
        return (EstimatedPayloadEntity) method.invoke(adapter, payload);
    }
}
