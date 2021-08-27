package ru.skillbox.blog.service;

import ru.skillbox.blog.dto.SettingsDto;

public interface GlobalSettingService {

    String MULTIUSER_MODE = "MULTIUSER_MODE";
    String POST_PREMODERATION = "POST_PREMODERATION";
    String STATISTICS_IS_PUBLIC = "STATISTICS_IS_PUBLIC";

    boolean getBoolean(String code);

    default boolean isMultiuserMode() {
        return getBoolean(MULTIUSER_MODE);
    }

    default boolean isPostPremoderation() {
        return getBoolean(POST_PREMODERATION);
    }

    default boolean isStatisticsPublic() {
        return getBoolean(STATISTICS_IS_PUBLIC);
    }

    SettingsDto getSettings();

    void updateSettings(SettingsDto dto);
}
