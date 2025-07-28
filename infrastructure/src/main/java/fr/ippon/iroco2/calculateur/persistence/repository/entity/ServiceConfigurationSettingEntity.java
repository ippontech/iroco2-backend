package fr.ippon.iroco2.calculateur.persistence.repository.entity;

import fr.ippon.iroco2.domain.calculateur.model.ServiceConfigurationSetting;
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
@Table(name = "service_configuration_setting")
public class ServiceConfigurationSettingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column
    private UUID id;

    @JoinColumn(name = "service_id")
    @ManyToOne
    private CloudServiceProviderServiceEntity cloudServiceProviderService;

    @JoinColumn(name = "configuration_setting_id")
    @ManyToOne
    private ConfigurationSettingEntity configurationSetting;

    @Column(name = "default_value")
    private String defaultValue;

    public ServiceConfigurationSetting toDomain() {
        return new ServiceConfigurationSetting(this.id, this.configurationSetting.toDomain(), this.defaultValue);
    }
}
