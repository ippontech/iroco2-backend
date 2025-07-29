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
package fr.ippon.iroco2.domain.calculateur;

import fr.ippon.iroco2.domain.calculateur.api.CloudServiceProviderServiceSvc;
import fr.ippon.iroco2.domain.calculateur.model.CloudServiceProviderService;
import fr.ippon.iroco2.domain.calculateur.spi.CloudServiceProviderServiceStorage;
import fr.ippon.iroco2.domain.commons.DomainService;
import fr.ippon.iroco2.domain.commons.exception.NotFoundException;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;

@DomainService
@RequiredArgsConstructor
public class CloudServiceProviderServiceSvcImpl implements CloudServiceProviderServiceSvc {
    private final CloudServiceProviderServiceStorage cloudServiceProviderServiceStorage;

    @Override
    public List<CloudServiceProviderService> findAllByCsp(UUID cspId) {
        return cloudServiceProviderServiceStorage.findAllByCsp(cspId);
    }

    @Override
    public List<CloudServiceProviderService> findAll() {
        return cloudServiceProviderServiceStorage.findAll();
    }

    @Override
    public CloudServiceProviderService findServiceById(UUID serviceId) {
        return cloudServiceProviderServiceStorage.findById(serviceId).orElseThrow(() -> new NotFoundException("The service with id %s does not exist".formatted(serviceId)));
    }
}