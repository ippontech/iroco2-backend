/*
 * Copyright 2025 Ippon Technologies
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package fr.ippon.iroco2.legacy.instance_type.domain;

import fr.ippon.iroco2.domain.estimateur.model.cpu.GPUType;
import fr.ippon.iroco2.estimateur.persistence.repository.EC2InstanceRepository;
import fr.ippon.iroco2.estimateur.persistence.repository.entity.EC2InstanceEntity;
import fr.ippon.iroco2.legacy.instance_type.domain.model.EC2InstanceType;
import fr.ippon.iroco2.legacy.instance_type.infrastructure.persistence.entity.ServiceInstanceModel;
import fr.ippon.iroco2.legacy.instance_type.infrastructure.persistence.ServiceInstanceRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class EC2InstanceTypeService {

    private final EC2InstanceRepository ec2InstanceRepository;
    private final ServiceInstanceRepository serviceInstanceRepository;

    public EC2InstanceTypeService(
            EC2InstanceRepository ec2InstanceRepository,
            ServiceInstanceRepository serviceInstanceRepository
    ) {
        this.ec2InstanceRepository = ec2InstanceRepository;
        this.serviceInstanceRepository = serviceInstanceRepository;
    }

    public EC2InstanceType findEC2InstanceType(String instanceName) {
        EC2InstanceEntity ec2InstanceEntity = ec2InstanceRepository.findByName(instanceName).orElse(null);
        GPUType gpuType = null;
        if (Objects.nonNull(ec2InstanceEntity.getGpuType())) {
            gpuType = GPUType.valueOf(ec2InstanceEntity.getGpuType());
        }
        return new EC2InstanceType(
                ec2InstanceEntity.getName(),
                ec2InstanceEntity.getVCPUs(),
                ec2InstanceEntity.getMemory(),
                ec2InstanceEntity.getCpuType(),
                Optional.ofNullable(ec2InstanceEntity.getGpu()),
                Optional.ofNullable(gpuType)
        );
    }

    public List<EC2InstanceEntity> getCompatibleInstancesForService(String serviceShortName) {
        return serviceInstanceRepository
                .findById_ServiceShortName(serviceShortName)
                .stream()
                .map(ServiceInstanceModel::instanceModel)
                .toList();
    }

}
