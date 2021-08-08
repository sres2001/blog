package ru.skillbox.blog.security;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * Sergey Reshetnik<br/>
 * Date: 08.08.2021<br/>
 * Time: 11:00<br/>
 * Copyright 2021 Connective Games LLC. All rights reserved.
 */
public enum UserRole {
    AUTHOR(Set.of(UserPermission.POST_WRITE, UserPermission.USER_LOGOUT)),
    MODERATOR(Set.of(UserPermission.POST_MODERATE, UserPermission.USER_LOGOUT)),
    ADMIN(Set.of(UserPermission.SETTINGS_WRITE, UserPermission.USER_LOGOUT));

    private final Set<UserPermission> permissions;

    UserRole(Set<UserPermission> permissions) {
        this.permissions = permissions;
    }

    public Set<UserPermission> getPermissions() {
        return permissions;
    }

    public Set<SimpleGrantedAuthority> getGrantedAuthorities() {
        Set<SimpleGrantedAuthority> permissions = getPermissions().stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toSet());
        permissions.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
        return permissions;
    }
}
