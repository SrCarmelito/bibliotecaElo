package com.bibliotecaelo.rules;

import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Table;
import org.hibernate.envers.RevisionEntity;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;

@AnalyzeClasses(packages = "com.bibliotecaelo")
public class TestRules {

    @ArchTest
    static ArchRule entidadesDomainDevemSerAuditadas =
            classes().that().areAnnotatedWith(Table.class)
                    .should()
                    .beAnnotatedWith(EntityListeners.class)
                    .orShould().beAnnotatedWith(RevisionEntity.class);

}
