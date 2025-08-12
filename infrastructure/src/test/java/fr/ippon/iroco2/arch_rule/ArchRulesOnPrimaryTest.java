package fr.ippon.iroco2.arch_rule;

import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;

@AnalyzeClasses(packages = "fr.ippon.iroco2")
public class ArchRulesOnPrimaryTest {
    public static final String PRIMARY_PACKAGE = "..primary..";

    @ArchTest
    ArchRule infra_controller_should_call_domain_primary_interfaces =
            classes().that().haveNameMatching(".*Controller")
                    .and().doNotHaveSimpleName("ScannerJwtController") // used to get token
                    .should().accessClassesThat().haveNameMatching("fr\\.ippon\\.iroco2\\.domain\\.[a-z]+\\.primary\\..*");

    @ArchTest
    ArchRule controller_should_be_in_primary_package =
            classes().that().areAnnotatedWith("org.springframework.web.bind.annotation.RestController")
                    .should().resideInAPackage(PRIMARY_PACKAGE);

    @ArchTest
    ArchRule response_beans_should_be_in_primary_package =
            classes().that().haveNameMatching(".*Response")
                    .should().resideInAPackage(PRIMARY_PACKAGE);

    @ArchTest
    ArchRule request_beans_should_be_in_primary_package =
            classes().that().haveNameMatching(".*Request")
                    .should().resideInAPackage(PRIMARY_PACKAGE);
}
