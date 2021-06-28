package ru.skillbox.blog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.skillbox.blog.model.CaptchaCode;

import java.util.Date;

@Repository
public interface CaptchaCodeRepository extends JpaRepository<CaptchaCode, Integer> {

    @Modifying
    @Query("delete from CaptchaCode c where c.time <= :threshold")
    void deleteOldCaptcha(Date threshold);
}
