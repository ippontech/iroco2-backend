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
package fr.ippon.iroco2.domain.arch;

import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

@AnalyzeClasses(packages = "fr.ippon.iroco2")
public class ArchiHexaDomainTest {
    @ArchTest
    ArchRule classes_spi_package_should_have_interfaces_only =
            classes().that().resideInAPackage("..secondary").should().beInterfaces();
    @ArchTest
    ArchRule classes_api_package_should_have_interfaces_only =
            classes().that().resideInAPackage("..primary").should().beInterfaces();
    @ArchTest
    ArchRule domain_should_not_have_adapter =
            noClasses().should().haveNameMatching(".*Adapter");
}
