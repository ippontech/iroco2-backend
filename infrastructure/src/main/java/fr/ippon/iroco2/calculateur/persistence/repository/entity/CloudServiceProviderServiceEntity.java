package fr.ippon.iroco2.calculateur.persistence.repository.entity;

import fr.ippon.iroco2.domain.calculateur.model.CloudServiceProviderService;
import fr.ippon.iroco2.domain.calculateur.model.emu.Availability;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor

@Entity
@Table(name = "cloud_service_provider_service")
public class CloudServiceProviderServiceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column
    private UUID id;

    @Column
    private String name;

    @Column
    private String description;

    @Column(name = "availability", nullable = false)
    @Enumerated(EnumType.STRING)
    private Availability availability;

    @Column(name = "short_name")
    private String shortname;

    @Column(name = "levers", columnDefinition = "varchar[]")
    @JdbcTypeCode(SqlTypes.ARRAY)
    private List<String> levers;

    @Column(name = "limitations", columnDefinition = "varchar[]")
    @JdbcTypeCode(SqlTypes.ARRAY)
    private List<String> limitations;

    @JoinColumn(name = "csp")
    @ManyToOne
    private CloudServiceProviderEntity cloudServiceProvider;

    public CloudServiceProviderService toDomain() {
        var service = new CloudServiceProviderService();
        service.setId(this.id);
        service.setName(this.name);
        service.setDescription(this.description);
        service.setAvailability(this.availability);
        service.setShortname(this.shortname);
        service.setLevers(this.levers.stream().toList());
        service.setLimitations(this.limitations.stream().toList());
        return service;
    }
}
