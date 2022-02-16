package asc.foods.user;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import org.junit.jupiter.api.Test;

class ArchTest {

    @Test
    void servicesAndRepositoriesShouldNotDependOnWebLayer() {
        JavaClasses importedClasses = new ClassFileImporter()
            .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
            .importPackages("asc.foods.user");

        noClasses()
            .that()
            .resideInAnyPackage("asc.foods.user.service..")
            .or()
            .resideInAnyPackage("asc.foods.user.repository..")
            .should()
            .dependOnClassesThat()
            .resideInAnyPackage("..asc.foods.user.web..")
            .because("Services and repositories should not depend on web layer")
            .check(importedClasses);
    }
}
