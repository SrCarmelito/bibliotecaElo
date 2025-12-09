package com.bibliotecaelo.utils;

import java.sql.SQLException;
import java.sql.Statement;

import liquibase.change.custom.CustomTaskChange;
import liquibase.database.Database;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.CustomChangeException;
import liquibase.exception.DatabaseException;
import liquibase.exception.ValidationErrors;
import liquibase.resource.ResourceAccessor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
public class DomainAuditColumns implements CustomTaskChange {

    private String tableName;
    private String databaseType;

    protected String addColumn(String columnName, boolean isString) {
        boolean isH2 = getDatabaseType().contains("H2");

        StringBuilder sql = new StringBuilder();

        sql.append("ALTER TABLE ");

        if(isH2) {
            sql.append("biblioteca.");
        }

        sql.append(tableName)
               .append(" ADD COLUMN ")
               .append(columnName);

        if(isString) {
            sql.append(" varchar(60) ");
        } else {
            sql.append(" timestamp ");
        }

        sql.append("NOT NULL;");

        return sql.toString();
    }

    @Override
    public void execute(Database database) throws CustomChangeException {
        JdbcConnection dbConnection = (JdbcConnection) database.getConnection();

        setDatabaseType(database.getDatabaseProductName());

        Statement statement;
        try {
            statement = dbConnection.createStatement();

            statement.execute(addColumn("datacriacao", false));
            statement.execute(addColumn("usuariocriacao", true));
            statement.execute(addColumn("dataalteracao", false));
            statement.execute(addColumn("usuarioalteracao", true));

        } catch (SQLException | DatabaseException e) {
            try {
                dbConnection.rollback();
            } catch (DatabaseException databaseException) {
                log.error(databaseException.getMessage());
            }
        }
    }

    @Override public String getConfirmationMessage() {
        return "Colunas de auditoria criadas para a tabela ".concat(tableName);
    }

    @Override public void setUp() {}

    @Override public void setFileOpener(ResourceAccessor resourceAccessor) {}

    @Override public ValidationErrors validate(Database database) {
        return null;
    }
}
