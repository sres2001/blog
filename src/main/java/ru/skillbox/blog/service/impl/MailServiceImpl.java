package ru.skillbox.blog.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import ru.skillbox.blog.exceptions.ApiException;
import ru.skillbox.blog.service.MailService;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;

@Service
public class MailServiceImpl implements MailService {

    private final JavaMailSender mailSender;
    private final String blogTitle;
    private final String blogEmail;

    public MailServiceImpl(
            JavaMailSender mailSender,
            @Value("${blog.title}") String blogTitle,
            @Value("${blog.email}") String blogEmail
    ) {
        this.mailSender = mailSender;
        this.blogTitle = blogTitle;
        this.blogEmail = blogEmail;
    }

    @Override
    public void sendPasswordRestoreEmail(String applicationUrl, String email, String code) {

        String url = applicationUrl + "/login/change-password/" + code;
        String htmlMessage = "Мы получили запрос на изменение вашего пароля в нашей системе. " +
                "Если это были вы, то перейдите по ссылке<br/>\n" +
                "<a href=\"" + url + "\">Установить пароль</a>";
        String textMessage = "Мы получили запрос на изменение вашего пароля в нашей системе. " +
                "Если это были вы, то перейдите по ссылке\n" +
                url;

        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            helper.setFrom(blogEmail, blogTitle);
            helper.setTo(email);
            helper.setSubject(blogTitle + ": Восстановление пароля");
            helper.setText(textMessage, htmlMessage);
            mailSender.send(mimeMessage);
        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }
}
