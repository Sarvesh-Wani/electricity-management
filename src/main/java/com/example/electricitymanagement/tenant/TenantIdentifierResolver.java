package com.example.electricitymanagement.tenant;

import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.springframework.stereotype.Component;

@Component
public class TenantIdentifierResolver implements CurrentTenantIdentifierResolver {

    public static final String DEFAULT_SCHEMA = "public";

    @Override
    public String resolveCurrentTenantIdentifier() {
        System.out.println("Resolver called");

        String tenant = TenantContext.getTenant();
        // If no tenant set (Aniruddha's staff, or login endpoint)
        // return "public" — queries hit the public schema
        return (tenant != null && !tenant.isBlank()) ? tenant : DEFAULT_SCHEMA;
    }

    @Override
    public boolean validateExistingCurrentSessions() {
        return true;  // reject stale sessions from a different tenant
    }
}
