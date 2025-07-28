package fr.ippon.iroco2.domain.estimateur.aws;

import java.util.Optional;

public interface EC2InstanceStorage {
    Optional<EC2Instance> findByName(String name);
}
