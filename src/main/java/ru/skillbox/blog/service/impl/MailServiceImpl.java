package ru.skillbox.blog.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;
import ru.skillbox.blog.service.MailService;

@Service
public class MailServiceImpl implements MailService {

    private final MailSender mailSender;
    private final String blogTitle;
    private final String blogEmail;

    public MailServiceImpl(
            MailSender mailSender,
            @Value("${blog.title}") String blogTitle,
            @Value("${blog.email}") String blogEmail
    ) {
        this.mailSender = mailSender;
        this.blogTitle = blogTitle;
        this.blogEmail = blogEmail;
    }

    @Override
    public void sendPasswordRestoreEmail(String email, String code) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(blogEmail);
        message.setTo(email);
        message.setSubject(blogTitle + ": Восстановление пароля");
        message.setText("Мы получили запрос на изменение вашего пароля в нашей системе. " +
                "Если это были вы, то перейдите по ссылке\n" +
                "/login/change-password/" + code);
        mailSender.send(message);
    }
}
