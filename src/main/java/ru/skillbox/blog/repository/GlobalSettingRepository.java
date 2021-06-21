package ru.skillbox.blog.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.skillbox.blog.model.GlobalSetting;

import java.util.Optional;

@Repository
public interface GlobalSettingRepository extends CrudRepository<GlobalSetting, Integer> {

    Optional<GlobalSetting> findByCode(String code);
}
