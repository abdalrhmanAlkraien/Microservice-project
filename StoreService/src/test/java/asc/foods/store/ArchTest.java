package asc.foods.store;

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
            .importPackages("asc.foods.store");

        noClasses()
            .that()
            .resideInAnyPackage("asc.foods.store.service..")
            .or()
            .resideInAnyPackage("asc.foods.store.repository..")
            .should()
            .dependOnClassesThat()
            .resideInAnyPackage("..asc.foods.store.web..")
            .because("Services and repositories should not depend on web layer")
            .check(importedClasses);
    }
}
