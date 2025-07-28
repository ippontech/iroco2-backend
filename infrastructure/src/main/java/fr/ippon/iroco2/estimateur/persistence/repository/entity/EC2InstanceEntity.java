package fr.ippon.iroco2.estimateur.persistence.repository.entity;

import fr.ippon.iroco2.domain.estimateur.aws.EC2Instance;
import fr.ippon.iroco2.domain.estimateur.model.cpu.CPUType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor

@Entity
@Table(name = "EC2INSTANCE")
public class EC2InstanceEntity {

    @Id
    @Column(name = "Id")
    private int id;

    @Column(name = "Name")
    private String name;

    @Column(name = "Memory")
    private BigDecimal memory;

    @Column(name = "vCPUs")
    private int vCPUs;

    @Column(name = "cpuType")
    private String cpuType;

    @Column(name = "GPU")
    private Integer gpu;

    @Column(name = "gpuType")
    private String gpuType;

    public EC2Instance toDomain() {
        return EC2Instance.load(name, memory, vCPUs, CPUType.valueOf(cpuType));
    }
}
