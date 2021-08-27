package ru.skillbox.blog.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.skillbox.blog.dto.SettingsDto;
import ru.skillbox.blog.model.GlobalSetting;
import ru.skillbox.blog.repository.GlobalSettingRepository;
import ru.skillbox.blog.service.GlobalSettingService;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class GlobalSettingServiceImpl implements GlobalSettingService {

    private static final String YES = "YES";
    private static final String NO = "NO";

    private final GlobalSettingRepository repository;

    public GlobalSettingServiceImpl(GlobalSettingRepository repository) {
        this.repository = repository;
    }

    public boolean getBoolean(String code) {
        return repository.findByCode(code)
                .map(setting -> YES.equals(setting.getValue()))
                .orElse(false);
    }

    @Override
    public SettingsDto getSettings() {
        Map<String, Boolean> settingList = repository.findByCodeIn(Set.of(
                        MULTIUSER_MODE, POST_PREMODERATION, STATISTICS_IS_PUBLIC))
                .stream()
                .collect(Collectors.toMap(GlobalSetting::getCode, entity -> YES.equals(entity.getValue())));

        SettingsDto dto = new SettingsDto();
        dto.setMultiuserMode(settingList.get(MULTIUSER_MODE));
        dto.setPostPremoderation(settingList.get(POST_PREMODERATION));
        dto.setStatisticsIsPublic(settingList.get(STATISTICS_IS_PUBLIC));
        return dto;
    }

    @Transactional
    @Override
    public void updateSettings(SettingsDto dto) {
        repository.updateByCode(MULTIUSER_MODE, dto.isMultiuserMode() ? YES : NO);
        repository.updateByCode(POST_PREMODERATION, dto.isPostPremoderation() ? YES : NO);
        repository.updateByCode(STATISTICS_IS_PUBLIC, dto.isStatisticsIsPublic() ? YES : NO);
    }
}
