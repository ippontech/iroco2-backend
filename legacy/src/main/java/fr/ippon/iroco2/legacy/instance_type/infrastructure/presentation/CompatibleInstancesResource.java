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
package fr.ippon.iroco2.legacy.instance_type.infrastructure.presentation;

import fr.ippon.iroco2.common.presentation.security.IsMember;
import fr.ippon.iroco2.legacy.instance_type.domain.EC2InstanceTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/awsInstanceType")
@RequiredArgsConstructor
public class CompatibleInstancesResource {

    private final EC2InstanceTypeService ec2InstanceTypeService;

    @IsMember
    @GetMapping
    public ResponseEntity<List<InstanceTypeDTO>> getCompatibleInstancesForService(@RequestParam("serviceShortName") String serviceShortName) {
        List<InstanceTypeDTO> list = ec2InstanceTypeService.getCompatibleInstancesForService(serviceShortName)
                .stream().map(instance -> new InstanceTypeDTO(instance.getName())).toList();
        return ResponseEntity.ok().body(list);
    }
}
