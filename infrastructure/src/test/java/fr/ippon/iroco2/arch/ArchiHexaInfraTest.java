package fr.ippon.iroco2.arch;

import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import fr.ippon.iroco2.domain.commons.DomainService;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

@AnalyzeClasses(packages = "fr.ippon.iroco2")
public class ArchiHexaInfraTest {
    @ArchTest
    ArchRule infra_should_not_depend_on_classes_annoted_with_domain =
            noClasses().that().resideOutsideOfPackage("fr.ippon.iroco2.domain..")
                    .should().accessClassesThat().areAnnotatedWith(DomainService.class);
}
