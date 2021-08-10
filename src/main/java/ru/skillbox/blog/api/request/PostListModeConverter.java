package ru.skillbox.blog.api.request;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class PostListModeConverter implements Converter<String, PostListMode> {
    @Override
    public PostListMode convert(String s) {
        return PostListMode.valueOf(s.toUpperCase(Locale.ROOT));
    }
}
