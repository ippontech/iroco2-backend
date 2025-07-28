package fr.ippon.iroco2.estimateur.persistence;

import fr.ippon.iroco2.domain.estimateur.aws.EC2Instance;
import fr.ippon.iroco2.domain.estimateur.aws.EC2InstanceStorage;
import fr.ippon.iroco2.estimateur.persistence.repository.EC2InstanceRepository;
import fr.ippon.iroco2.estimateur.persistence.repository.entity.EC2InstanceEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class EC2InstanceStorageAdapter implements EC2InstanceStorage {
    private final EC2InstanceRepository ec2InstanceRepository;

    @Override
    public Optional<EC2Instance> findByName(String name) {
        return ec2InstanceRepository.findByName(name).map(EC2InstanceEntity::toDomain);
    }
}
