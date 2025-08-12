package fr.ippon.iroco2.arch_rule;

import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

import static com.tngtech.archunit.core.domain.properties.HasName.AndFullName.Predicates.fullNameMatching;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;

@AnalyzeClasses(packages = "fr.ippon.iroco2")
public class ArchRulesOnSecondaryTest {
    public static final String REPOSITORY_ANNOTATION = "org.springframework.stereotype.Repository";
    public static final String SECONDARY_PACKAGE = "..secondary..";

    @ArchTest
    ArchRule adapter_should_implement_domain_secondary_interfaces =
            classes().that().haveNameMatching(".*Adapter")
                    .and().doNotHaveSimpleName("EC2InstanceStorageAdapter") // TODO avoid exception
                    .should().implement(fullNameMatching("fr\\.ippon\\.iroco2\\.domain\\.[a-z]+\\.secondary\\..*"));

    @ArchTest
    ArchRule adapter_should_be_annotated_with_repository =
            classes().that().haveNameMatching(".*Adapter")
                    .and().doNotHaveSimpleName("SessionProviderAdapter")
                    .should().beAnnotatedWith(REPOSITORY_ANNOTATION);

    @ArchTest
    ArchRule repository_should_be_in_secondary_package =
            classes().that().areAnnotatedWith(REPOSITORY_ANNOTATION)
                    .or().haveNameMatching(".*Repository")
                    .should().resideInAPackage(SECONDARY_PACKAGE);

    @ArchTest
    ArchRule entity_should_be_in_secondary_package =
            classes().that().areAnnotatedWith("jakarta.persistence.Entity")
                    .should().resideInAPackage(SECONDARY_PACKAGE);
}
