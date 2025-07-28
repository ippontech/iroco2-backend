package fr.ippon.iroco2.scanner.persistence.repository.entity;

import fr.ippon.iroco2.common.persistance.entity.EstimatedPayloadEntity;
import fr.ippon.iroco2.common.persistance.entity.ReportEntity;
import fr.ippon.iroco2.domain.commons.model.EstimatedPayload;
import fr.ippon.iroco2.domain.scanner.model.Scan;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor

@Entity
@Table(name = "scan")
public class ScanEntity extends ReportEntity {

    @OneToMany(mappedBy = "scan", fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REMOVE})
    protected List<EstimatedPayloadEntity> payloads;
    @Column(name = "aws_account_id")
    private String awsAccountId;

    public Scan toDomain() {
        var domainPayloads = payloads.stream().map(p -> new EstimatedPayload(p.getId(), id, p.getCarbonGramFootprint(), p.getName())).toList();
        return Scan.load(id, owner, status, creationDate, domainPayloads, awsAccountId);
    }
}
