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
package fr.ippon.iroco2.domain.calculator;

import fr.ippon.iroco2.domain.calculator.model.CloudServiceProvider;
import fr.ippon.iroco2.domain.calculator.primary.CloudServiceProviderSvc;
import fr.ippon.iroco2.domain.calculator.secondary.CloudServiceProviderStorage;
import fr.ippon.iroco2.domain.commons.DomainService;
import fr.ippon.iroco2.domain.commons.exception.NotFoundException;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;

@DomainService
@RequiredArgsConstructor
public class CloudServiceProviderSvcImpl implements CloudServiceProviderSvc {

    private final CloudServiceProviderStorage cloudServiceProviderStorage;

    @Override
    public CloudServiceProvider findById(UUID uuid) {
        return cloudServiceProviderStorage.findById(uuid).orElseThrow(() -> new NotFoundException("CSP not found"));
    }

    @Override
    public List<CloudServiceProvider> findAll() {
        return cloudServiceProviderStorage.findAll();
    }
}
