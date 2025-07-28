package fr.ippon.iroco2.legacy.instance_type.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode
@Embeddable
public class CompatibleInstancesCompositeID implements Serializable {

    @Column(name = "SERVICE_SHORT_NAME", insertable = false, updatable = false)
    private String serviceShortName;

    @Column(name = "INSTANCE_TYPE_NAME", insertable = false, updatable = false)
    private String instanceTypeName;
}
