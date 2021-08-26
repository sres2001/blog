package ru.skillbox.blog.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.skillbox.blog.model.GlobalSetting;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface GlobalSettingRepository extends CrudRepository<GlobalSetting, Integer> {

    Optional<GlobalSetting> findByCode(String code);

    List<GlobalSetting> findByCodeIn(Set<String> codes);

    @Modifying
    @Query("update GlobalSetting s set s.value = :value where s.code = :code")
    void updateByCode(String code, String value);
}
