package com.bibliotecaelo.utils;

import java.sql.Statement;

import liquibase.database.Database;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.CustomChangeException;
import liquibase.exception.DatabaseException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

class DomainAuditColumnsTest {

    @Test
    void addColumn() {
        DomainAuditColumns domainAuditColumns = new DomainAuditColumns();
        domainAuditColumns.setTableName("MINHA_TABELA");

        domainAuditColumns.setDatabaseType("Postgres");
        assertThat(domainAuditColumns.addColumn("MINHA_COLUNA", true))
                .isEqualTo("ALTER TABLE MINHA_TABELA ADD COLUMN MINHA_COLUNA varchar(60) NOT NULL;");

        domainAuditColumns.setDatabaseType("H2");
        assertThat(domainAuditColumns.addColumn("MINHA_COLUNA_2", false))
                .isEqualTo("ALTER TABLE biblioteca.MINHA_TABELA ADD COLUMN MINHA_COLUNA_2 timestamp NOT NULL;");
    }

    @Test
    void execute() throws CustomChangeException, DatabaseException {

        Database database = mock(Database.class);
        JdbcConnection databaseConnection = mock(JdbcConnection.class);
        Statement statement = mock(Statement.class);

        when(database.getConnection()).thenReturn(databaseConnection);
        when(database.getDatabaseProductName()).thenReturn("h2");
        when(databaseConnection.createStatement()).thenReturn(statement);

        DomainAuditColumns domainAuditColumns = new DomainAuditColumns();

        domainAuditColumns.execute(database);

        verify(database).getConnection();
        verify(database).getDatabaseProductName();
        verify(databaseConnection).createStatement();
        verifyNoMoreInteractions(database, databaseConnection);
    }
}