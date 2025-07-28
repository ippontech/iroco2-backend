package fr.ippon.iroco2.calculateur.persistence.repository.entity;

import fr.ippon.iroco2.domain.calculateur.model.ConfiguredSetting;
import fr.ippon.iroco2.domain.calculateur.model.emu.SettingName;
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
@Table(name = "configured_setting")
public class ConfiguredSettingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "component_id", nullable = false)
    private ComponentEntity component;

    @ManyToOne
    @JoinColumn(name = "configuration_setting_id", nullable = false)
    private ConfigurationSettingEntity configurationSetting;

    @Column
    private String value;

    public ConfiguredSetting toDomain() {
        return new ConfiguredSetting(configurationSetting.getId(), SettingName.valueOf(configurationSetting.getName()), value);
    }
}
