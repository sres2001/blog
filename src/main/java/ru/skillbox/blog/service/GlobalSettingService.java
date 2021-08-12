package ru.skillbox.blog.service;

public interface GlobalSettingService {
    boolean getBoolean(String code);

    default boolean isPostPremoderation() {
        return getBoolean("POST_PREMODERATION");
    }
}
