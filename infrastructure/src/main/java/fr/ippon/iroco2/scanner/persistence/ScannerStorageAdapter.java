package fr.ippon.iroco2.scanner.persistence;

import fr.ippon.iroco2.common.persistance.entity.EstimatedPayloadEntity;
import fr.ippon.iroco2.domain.commons.model.EstimatedPayload;
import fr.ippon.iroco2.domain.scanner.model.Scan;
import fr.ippon.iroco2.domain.scanner.spi.ScanStorage;
import fr.ippon.iroco2.scanner.persistence.repository.ScannerRepository;
import fr.ippon.iroco2.scanner.persistence.repository.entity.ScanEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ScannerStorageAdapter implements ScanStorage {

    private final ScannerRepository scannerRepository;

    @Override
    public UUID save(Scan scan) {
        ScanEntity entity = fromDomain(scan);
        ScanEntity saved = scannerRepository.save(entity);
        return saved.getId();
    }

    @Override
    public List<Scan> findByOwner(String owner) {
        return scannerRepository.findByOwnerOrderByCreationDateAsc(owner).stream().map(ScanEntity::toDomain).toList();
    }

    @Override
    public void delete(UUID id) {
        scannerRepository.deleteById(id);
    }

    @Override
    public Optional<Scan> findById(UUID id) {
        return scannerRepository.findById(id).map(ScanEntity::toDomain);
    }

    private ScanEntity fromDomain(Scan scan) {
        ScanEntity scanEntity = new ScanEntity();
        scanEntity.setId(scan.getId());
        scanEntity.setOwner(scan.getOwner());
        scanEntity.setStatus(scan.getStatus());
        scanEntity.setAwsAccountId(scan.getAwsAccountId());
        scanEntity.setCreationDate(scan.getCreationDate());
        scanEntity.setPayloads(scan.getPayloads().stream().map(this::fromDomain).toList());
        return scanEntity;
    }

    private EstimatedPayloadEntity fromDomain(EstimatedPayload payload) {
        EstimatedPayloadEntity estimatedPayloadEntity = new EstimatedPayloadEntity();
        estimatedPayloadEntity.setId(payload.id());
        estimatedPayloadEntity.setScan(new ScanEntity());
        estimatedPayloadEntity.getScan().setId(payload.reportId());
        estimatedPayloadEntity.setCarbonGramFootprint(payload.carbonGramFootprint());
        estimatedPayloadEntity.setName(payload.name());
        return estimatedPayloadEntity;
    }
}
