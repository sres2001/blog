package ru.skillbox.blog.service.impl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class RandomUtilsTest {

    @Test
    @DisplayName("генерируем хэш для востановления пароля")
    public void testGeneratePasswordRestoreHash() {
        String hash = RandomUtils.generatePasswordRestoreHash();
        System.out.println("hash = " + hash);
        assertTrue(hash.matches("^[0-9a-f]{32}$"));
    }

    @Test
    @DisplayName("генерируем случайный путь для загружаемых изображений")
    public void testGenerateFilePath() {
        String path = RandomUtils.generateFilePath();
        System.out.println("path = " + path);
        assertTrue(path.matches("^[0-9a-f]{32}$"));
    }

    @Test
    @DisplayName("генерируем секрет для капчи")
    public void testGenerateCaptchaSecret() {
        String secret = RandomUtils.generateCaptchaSecret();
        System.out.println("secret = " + secret);
        assertTrue(secret.matches("^[0-9a-f-]{36}$"));
    }

}