package ru.skillbox.blog.service.impl;

import org.springframework.stereotype.Service;
import ru.skillbox.blog.repository.GlobalSettingRepository;
import ru.skillbox.blog.service.GlobalSettingService;

@Service
public class GlobalSettingServiceImpl implements GlobalSettingService {

    private final GlobalSettingRepository repository;

    public GlobalSettingServiceImpl(GlobalSettingRepository repository) {
        this.repository = repository;
    }

    @Override
    public boolean getBoolean(String code) {
        return repository.findByCode(code)
                .map(setting -> "YES".equals(setting.getValue()))
                .orElse(false);
    }
}
