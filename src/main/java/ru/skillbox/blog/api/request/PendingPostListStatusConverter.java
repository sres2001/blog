package ru.skillbox.blog.api.request;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class PendingPostListStatusConverter implements Converter<String, ModeratorPostListStatus> {
    @Override
    public ModeratorPostListStatus convert(String s) {
        return ModeratorPostListStatus.valueOf(s.toUpperCase(Locale.ROOT));
    }
}
