package com.api.jobo.JoboApi.config.security;

import com.google.common.collect.Sets;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

import static com.api.jobo.JoboApi.config.security.AppUserPermission.*;

public enum AppRolesEnum {

    ROLE_ADMIN(Sets.newHashSet(USER_READ, USER_WRITE, USER_DELETE, USER_UPDATE, JOB_READ, JOB_UPDATE, JOB_DELETE, JOB_WRITE,SERVICE_READ,SERVICE_DELETE,SERVICE_UPDATE,SERVICE_WRITE,REVIEW_READ,REVIEW_WRITE,REVIEW_DELETE,REVIEW_READ)),
    ROLE_ADMIN_TRAINEE(Sets.newHashSet(USER_READ, USER_UPDATE, USER_DELETE, USER_WRITE, JOB_READ, JOB_UPDATE,SERVICE_READ,SERVICE_UPDATE,REVIEW_READ)),
    ROLE_SERVICE_PROVIDER(Sets.newHashSet(USER_UPDATE, USER_READ, USER_DELETE, USER_WRITE, JOB_READ, JOB_UPDATE,SERVICE_READ,REVIEW_READ,REVIEW_UPDATE,REVIEW_WRITE)),
    ROLE_CLIENT(Sets.newHashSet(USER_UPDATE, USER_READ, USER_DELETE, USER_WRITE, JOB_READ, JOB_UPDATE,SERVICE_READ,JOB_WRITE,REVIEW_READ,REVIEW_UPDATE,REVIEW_WRITE));


    private final Set<AppUserPermission> permissions;

    AppRolesEnum(Set<AppUserPermission> permissions) {
        this.permissions = permissions;
    }

    public Set<AppUserPermission> getPermissions() {
        return permissions;
    }

    public Set<SimpleGrantedAuthority> getGrantedAuthorities() {
        Set<SimpleGrantedAuthority> permissions = getPermissions().stream().map(permission -> new SimpleGrantedAuthority(permission.getPermission())).collect(Collectors.toSet());

        permissions.add(new SimpleGrantedAuthority(this.name()));
        return permissions;
    }
}
