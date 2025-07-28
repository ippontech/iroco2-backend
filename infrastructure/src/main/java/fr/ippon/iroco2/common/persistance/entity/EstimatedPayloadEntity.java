package fr.ippon.iroco2.common.persistance.entity;

import fr.ippon.iroco2.analyzer.persistence.repository.entity.AnalysisEntity;
import fr.ippon.iroco2.scanner.persistence.repository.entity.ScanEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor

@Entity
@Table(name = "estimated_payload")
public class EstimatedPayloadEntity {
    @Id
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "analysis_id")
    private AnalysisEntity analysis;

    @ManyToOne
    @JoinColumn(name = "scan_id")
    private ScanEntity scan;


    @Column(name = "carbon_gram_footprint")
    private int carbonGramFootprint;

    private String name;
}
