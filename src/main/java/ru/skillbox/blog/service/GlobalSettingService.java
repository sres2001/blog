package ru.skillbox.blog.service;

public interface GlobalSettingService {
    boolean getBoolean(String code);

    default boolean isPostPremoderation() {
        return getBoolean("POST_PREMODERATION");
    }

    default boolean isStatisticsPublic() {
        return getBoolean("STATISTICS_IS_PUBLIC");
    }
}
