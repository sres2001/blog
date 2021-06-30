package ru.skillbox.blog.config;

import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.TimeZone;

@Configuration
public class BlogConfiguration {

    @Bean
    public static BeanFactoryPostProcessor setTimeZoneUtc() {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        return factory -> {};
    }
}
