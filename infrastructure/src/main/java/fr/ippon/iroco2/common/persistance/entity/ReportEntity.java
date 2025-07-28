package fr.ippon.iroco2.common.persistance.entity;

import fr.ippon.iroco2.domain.commons.model.ReportStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class ReportEntity {

    @Id
    protected UUID id;

    @Column(name = "owner")
    protected String owner;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    protected ReportStatus status;

    @Column(name = "creation_date")
    protected LocalDateTime creationDate;
}
