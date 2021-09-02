package ru.skillbox.blog.service;

public interface MailService {

    void sendPasswordRestoreEmail(String applicationUrl, String email, String code);
}
