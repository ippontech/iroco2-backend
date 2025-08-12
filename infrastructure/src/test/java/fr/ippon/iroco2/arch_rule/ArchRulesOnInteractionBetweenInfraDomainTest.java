package fr.ippon.iroco2.arch_rule;

import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

@AnalyzeClasses(packages = "fr.ippon.iroco2")
public class ArchRulesOnInteractionBetweenInfraDomainTest {
    public static final String DOMAIN_PACKAGE = "fr.ippon.iroco2.domain..";

    @ArchTest
    ArchRule infra_should_not_access_on_classes_annotated_with_domain =
            noClasses().that().resideOutsideOfPackage(DOMAIN_PACKAGE)
                    .should().accessClassesThat()
                    .areAnnotatedWith("fr.ippon.iroco2.domain.commons.DomainService");

    @ArchTest
    ArchRule infra_should_not_access_on_domain_or_svc_classes =
            noClasses().that().resideOutsideOfPackage(DOMAIN_PACKAGE)
                    .and().doNotHaveSimpleName("DomainConnector")
                    .should().dependOnClassesThat()
                    .resideInAnyPackage(
                            "fr.ippon.iroco2.domain.*",
                            "fr.ippon.iroco2.domain.*.svc"
                    );
}
