package fr.ippon.iroco2.calculateur.persistence.repository.entity;

import fr.ippon.iroco2.domain.calculateur.model.CloudServiceProviderRegion;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
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
@Table(name = "cloud_service_provider_region")
public class CloudServiceProviderRegionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column
    private UUID id;

    @Column
    private String name;

    @Column
    private String area;

    @Column
    private String shortname;

    @JoinColumn(name = "csp")
    @ManyToOne
    private CloudServiceProviderEntity cloudServiceProvider;

    public CloudServiceProviderRegion toDomain() {
        var region = new CloudServiceProviderRegion();
        region.setId(this.id);
        region.setName(this.name);
        region.setArea(this.area);
        region.setShortname(this.shortname);
        region.setCloudServiceProviderId(this.cloudServiceProvider.getId());
        return region;
    }
}
