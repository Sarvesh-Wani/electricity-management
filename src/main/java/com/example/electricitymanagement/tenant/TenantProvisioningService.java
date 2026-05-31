package com.example.electricitymanagement.tenant;

//
//import liquibase.Contexts;
//import liquibase.LabelExpression;
//import liquibase.Liquibase;
//import liquibase.database.Database;
//import liquibase.database.DatabaseFactory;
//import liquibase.database.jvm.JdbcConnection;
//import liquibase.resource.ClassLoaderResourceAccessor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.Statement;

@Slf4j
@Service
@RequiredArgsConstructor
public class TenantProvisioningService {

    private final DataSource dataSource;

    public void provisionTenant(String tenantId) {
        // Hard validation — tenantId goes directly into SQL (CREATE SCHEMA).
        // Only lowercase letters, digits, underscores. Max 63 chars (PostgreSQL limit).
        // This prevents SQL injection via schema name.
        if (tenantId == null || !tenantId.matches("^[a-z0-9_]{1,63}$")) {
            throw new IllegalArgumentException(
                    "Invalid tenant ID: " + tenantId);
        }

        log.info("Provisioning tenant schema: {}", tenantId);

        try (Connection connection = dataSource.getConnection()) {

            // Step 1 — Create the schema
            // IF NOT EXISTS makes this safe to call again if registration is retried
            try (Statement stmt = connection.createStatement()) {
                stmt.execute("CREATE SCHEMA IF NOT EXISTS " + tenantId);
            }
            log.info("Schema created: {}", tenantId);

            // Step 2 — Run Liquibase migrations inside the new schema
            // This creates customer, complaint, meter, bill tables etc.
//            runLiquibase(connection, tenantId);

            log.info("Tenant provisioned: {}", tenantId);

        } catch (Exception e) {
            log.error("Failed to provision tenant: {}", tenantId, e);
            throw new RuntimeException("Tenant provisioning failed: " + tenantId, e);
        }
    }
//
//    private void runLiquibase(Connection connection, String tenantId) throws Exception {
//        Database database = DatabaseFactory.getInstance()
//                .findCorrectDatabaseImplementation(new JdbcConnection(connection));
//
//        /*
//         * These two lines tell Liquibase which schema to create tables in.
//         * Without them it would create tables in public by mistake.
//         */
//        database.setDefaultSchemaName(tenantId);
//        database.setLiquibaseSchemaName(tenantId);
//
//        Liquibase liquibase = new Liquibase(
//                "db/tenant/changelog/master.xml",   // our tenant table definitions
//                new ClassLoaderResourceAccessor(),
//                database
//        );
//
//        liquibase.update(new Contexts(), new LabelExpression());
//        log.info("Liquibase migrations complete for schema: {}", tenantId);
//    }
}