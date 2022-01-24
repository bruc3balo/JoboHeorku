package com.api.jobo.JoboApi.config.security;

public enum AppUserPermission {

    //user
    USER_READ("user:read"),
    USER_WRITE("user:write"),
    USER_DELETE("user:delete"),
    USER_UPDATE("user:update"),

    //product
    JOB_READ("job:read"),
    JOB_WRITE("job:write"),
    JOB_DELETE("job:delete"),
    JOB_UPDATE("job:update"),

    //services
    SERVICE_READ("service:read"),
    SERVICE_WRITE("service:write"),
    SERVICE_DELETE("service:delete"),
    SERVICE_UPDATE("service:update"),

    //Review
    REVIEW_READ("review:read"),
    REVIEW_WRITE("review:write"),
    REVIEW_DELETE("review:delete"),
    REVIEW_UPDATE("review:update");

    private final String permission;

    AppUserPermission(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }
}
