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
            classes().that().resideInAPackage("..spi").should().beInterfaces();
    @ArchTest
    ArchRule classes_api_package_should_have_interfaces_only =
            classes().that().resideInAPackage("..api").should().beInterfaces();
    @ArchTest
    ArchRule domain_should_not_have_adapter =
            noClasses().should().haveNameMatching(".*Adapteu?r");
}
