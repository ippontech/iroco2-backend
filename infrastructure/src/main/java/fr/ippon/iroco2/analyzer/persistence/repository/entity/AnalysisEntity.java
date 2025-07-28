package fr.ippon.iroco2.analyzer.persistence.repository.entity;

import fr.ippon.iroco2.common.persistance.entity.EstimatedPayloadEntity;
import fr.ippon.iroco2.common.persistance.entity.ReportEntity;
import fr.ippon.iroco2.domain.commons.model.EstimatedPayload;
import fr.ippon.iroco2.domain.analyzer.model.Analysis;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor

@Entity
@Table(name = "analysis")
public class AnalysisEntity extends ReportEntity {


    @OneToMany(mappedBy = "analysis", fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REMOVE})
    protected List<EstimatedPayloadEntity> payloads = new ArrayList<>();

    public Analysis toDomain() {
        var domainPayloads = payloads.stream().map(p -> new EstimatedPayload(p.getId(), id, p.getCarbonGramFootprint(), p.getName())).toList();
        return Analysis.load(id, owner, status, creationDate, domainPayloads);
    }
}
