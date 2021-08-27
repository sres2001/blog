package ru.skillbox.blog.service;

public interface MailService {

    void sendPasswordRestoreEmail(String email, String code);
}
