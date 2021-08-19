package ru.skillbox.blog.service.impl;

import java.util.UUID;

public class RandomUtils {

    public static String generatePasswordRestoreHash() {
        return generateRandomHash();
    }

    public static String generateFilePath() {
        return generateRandomHash();
    }

    private static String generateRandomHash() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    public static String generateCaptchaSecret() {
        return UUID.randomUUID().toString();
    }
}
