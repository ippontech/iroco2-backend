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
package fr.ippon.iroco2.domain.estimateur.model.cpu;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CPUType {
    EPYC(CPUArchitectureType.X_86, (double) 350 / 56),
    XEON(CPUArchitectureType.X_86, (double) 270 / 64),
    GRAVITON(CPUArchitectureType.ARM, (double) 100 / 64),
    ETSY(CPUArchitectureType.X_86, 2.1);

    private final CPUArchitectureType cpuArchitectureType;
    private final double oneCorePowerInWatt;
}
