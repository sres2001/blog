package ru.skillbox.blog.api.response;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Map;

public class BaseResponse {

    private boolean result;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Map<String, String> errors;

    public BaseResponse() {
    }

    public static BaseResponse success() {
        return new BaseResponse(true);
    }

    public BaseResponse(boolean result) {
        this.result = result;
    }

    public BaseResponse(Map<String, String> errors) {
        this.errors = errors;
    }

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
