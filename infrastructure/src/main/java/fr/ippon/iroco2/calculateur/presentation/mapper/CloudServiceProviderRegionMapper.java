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
package fr.ippon.iroco2.calculateur.presentation.mapper;

import fr.ippon.iroco2.domain.calculateur.model.CloudServiceProviderRegion;
import fr.ippon.iroco2.calculateur.presentation.reponse.CloudServiceProviderRegionResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper
public abstract class CloudServiceProviderRegionMapper {

    public List<CloudServiceProviderRegionResponse> toResponse(List<CloudServiceProviderRegion> domains) {
        return domains.stream().map(this::toResponse).toList();
    }

    @Mapping(target = "csp", source = "domains.cloudServiceProviderId")
    protected abstract CloudServiceProviderRegionResponse toResponse(CloudServiceProviderRegion domains);

}
