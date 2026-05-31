package com.example.electricitymanagement.tenant;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.engine.jdbc.connections.spi.MultiTenantConnectionProvider;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@Slf4j
@Component
@RequiredArgsConstructor
public class SchemaMultiTenantConnectionProvider
        implements MultiTenantConnectionProvider {

    private final DataSource dataSource;

    @Override
    public Connection getAnyConnection() throws SQLException {
        return dataSource.getConnection();
    }

    @Override
    public void releaseAnyConnection(Connection connection) throws SQLException {
        connection.close();
    }

    @Override
    public Connection getConnection(Object tenantId) throws SQLException {
        String tenantIdentifier = tenantId.toString();
        Connection connection = dataSource.getConnection();
        System.out.println("Resolver called");

        try {
            // This single line is the entire magic of schema-based multi-tenancy.
            // SET search_path tells PostgreSQL: for this connection, when I say
            // SELECT * FROM customers — look in 'reliance' schema, not public.
            connection.createStatement()
                    .execute("SET search_path = " + tenantIdentifier);
            log.debug("search_path set to: {}", tenantIdentifier);
        } catch (SQLException e) {
            connection.close();
            throw e;
        }
        return connection;
    }

    @Override
    public void releaseConnection(Object tenantId,
                                  Connection connection) throws SQLException {
        String tenantIdentifier = tenantId.toString();
        try {
            // ALWAYS reset before returning to the pool.
            // The next request borrowing this connection must start clean.
            connection.createStatement()
                    .execute("SET search_path = public");
        } catch (SQLException e) {
            connection.close();
            return;
        }
        connection.close();
    }

    @Override
    public boolean supportsAggressiveRelease() {
        return false;
    }

    @Override
    public boolean isUnwrappableAs(Class unwrapType) {
        return false;
    }

    @Override
    public <T> T unwrap(Class<T> unwrapType) {
        return null;
    }
}
