package ru.skillbox.blog.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class BlogInformation {

    @Value("${blog.title}")
    private String title;

    @Value("${blog.subtitle}")
    private String subtitle;

    @Value("${blog.phone}")
    private String phone;

    @Value("${blog.email}")
    private String email;

    @Value("${blog.copyright}")
    private String copyright;

    @Value("${blog.copyrightFrom}")
    private String copyrightFrom;

    public String getTitle() {
        return title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public String getCopyright() {
        return copyright;
    }

    public String getCopyrightFrom() {
        return copyrightFrom;
    }
}
