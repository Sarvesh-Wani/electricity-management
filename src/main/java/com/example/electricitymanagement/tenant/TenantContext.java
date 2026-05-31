package com.example.electricitymanagement.tenant;

public class TenantContext {

    // InheritableThreadLocal so @Async child threads also inherit the tenant
    private static final InheritableThreadLocal<String> CURRENT_TENANT
            = new InheritableThreadLocal<>();

    private TenantContext() {}   // no instances — pure static utility

    public static void setTenant(String tenantId) {
        CURRENT_TENANT.set(tenantId);
    }

    public static String getTenant() {
        return CURRENT_TENANT.get();  // returns null if not set
    }

    public static void clear() {
        CURRENT_TENANT.remove();   // remove() not set(null) — prevents memory leaks
    }
}
