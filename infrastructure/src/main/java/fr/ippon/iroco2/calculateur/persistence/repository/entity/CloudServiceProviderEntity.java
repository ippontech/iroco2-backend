package fr.ippon.iroco2.calculateur.persistence.repository.entity;

import fr.ippon.iroco2.domain.calculateur.model.CloudServiceProvider;
import fr.ippon.iroco2.domain.calculateur.model.CloudServiceProviderRegion;
import fr.ippon.iroco2.domain.calculateur.model.CloudServiceProviderService;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor

@Entity
@Table(name = "cloud_service_provider")
public class CloudServiceProviderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column
    private UUID id;

    @Column(unique = true)
    private String name;

    @OneToMany(mappedBy = "cloudServiceProvider")
    private Set<CloudServiceProviderRegionEntity> cloudServiceProviderRegions = new HashSet<>();

    @OneToMany(mappedBy = "cloudServiceProvider")
    private List<CloudServiceProviderServiceEntity> cloudServiceProviderServices = new ArrayList<>();

    public CloudServiceProvider toDomain() {
        var provider = new CloudServiceProvider(this.id);
        provider.setName(this.name);

        Set<CloudServiceProviderRegion> regions = cloudServiceProviderRegions.stream().map(CloudServiceProviderRegionEntity::toDomain).collect(Collectors.toSet());

        provider.setCloudServiceProviderRegions(regions);

        List<CloudServiceProviderService> services = cloudServiceProviderServices.stream().map(CloudServiceProviderServiceEntity::toDomain).toList();

        provider.setCloudServiceProviderServices(services);
        return provider;
    }
}
