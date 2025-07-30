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
package fr.ippon.iroco2.arch;

import com.tngtech.archunit.core.domain.properties.HasName;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

@AnalyzeClasses(packages = "fr.ippon.iroco2")
public class ArchiHexaInfraTest {
    @ArchTest
    ArchRule infra_should_not_access_on_classes_annoted_with_domain =
            noClasses().that().resideOutsideOfPackage("fr.ippon.iroco2.domain..")
                    .should().accessClassesThat()
                    .areAnnotatedWith("fr.ippon.iroco2.domain.commons.DomainService");
    @ArchTest
    ArchRule infra_should_not_access_on_domain_or_svc_classes =
            noClasses().that().resideOutsideOfPackage("fr.ippon.iroco2.domain..")
                    .and().doNotHaveSimpleName("DomainConnector")
                    .should().dependOnClassesThat()
                    .resideInAnyPackage(
                            "fr.ippon.iroco2.domain.*",
                            "fr.ippon.iroco2.domain.*.svc"
                    );
    @ArchTest
    ArchRule infra_adapter_should_implement_domain_spi_interfaces =
            classes().that().haveNameMatching(".*Adapter")
                    .and().doNotHaveSimpleName("EC2InstanceStorageAdapter") // TODO avoid exception
                    .should().implement(HasName.AndFullName.Predicates.fullNameMatching("fr\\.ippon\\.iroco2\\.domain\\.[a-z]+\\.spi\\..*"));
    @ArchTest
    ArchRule infra_controller_should_call_domain_api_interfaces =
            classes().that().haveNameMatching(".*Controller")
                    .and().doNotHaveSimpleName("ScannerJwtController") // used to get token
                    .should().accessClassesThat().haveNameMatching("fr\\.ippon\\.iroco2\\.domain\\.[a-z]+\\.api\\..*");
}
