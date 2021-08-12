package ru.skillbox.blog.dto.mapper;

import java.util.Map;

public class BaseResponseDto {

    private boolean result;
    private Map<String, String> errors;

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public Map<String, String> getErrors() {
        return errors;
    }

    public void setErrors(Map<String, String> errors) {
        this.errors = errors;
    }
}
