package ru.skillbox.blog.security;

public enum UserPermission {
    POST_WRITE("post:write"),
    POST_MODERATE("post:moderate"),
    SETTINGS_WRITE("settings:write");

    private final String permission;

    UserPermission(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }
}
