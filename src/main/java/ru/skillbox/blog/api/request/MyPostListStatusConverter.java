package ru.skillbox.blog.api.request;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class MyPostListStatusConverter implements Converter<String, MyPostListStatus> {
    @Override
    public MyPostListStatus convert(String s) {
        return MyPostListStatus.valueOf(s.toUpperCase(Locale.ROOT));
    }
}
