package fr.ippon.iroco2.legacy.instance_type.infrastructure.persistence.entity;

import fr.ippon.iroco2.estimateur.persistence.repository.entity.EC2InstanceEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Accessors(fluent = true)
@Setter
@Getter
@Entity
@Table(name = "COMPATIBLE_INSTANCES_FOR_SERVICE")
public class ServiceInstanceModel {

    @EmbeddedId
    private CompatibleInstancesCompositeID id;

    @OneToOne
    @JoinColumn(name = "INSTANCE_TYPE_NAME", referencedColumnName = "name")
    EC2InstanceEntity instanceModel;
}
