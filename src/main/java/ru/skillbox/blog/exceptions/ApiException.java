package ru.skillbox.blog.exceptions;

import org.springframework.http.HttpStatus;

import java.util.Map;

public class ApiException extends RuntimeException {
    private static final long serialVersionUID = 0L;

    private final HttpStatus httpStatus;
    private final Map<String, String> errors;

    public ApiException(HttpStatus httpStatus, Map<String, String> errors) {
        this.httpStatus = httpStatus;
        this.errors = errors;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public Map<String, String> getErrors() {
        return errors;
    }

    @Override
    public String toString() {
        return "[ApiException super=" + super.toString() +
                ", httpStatus=" + httpStatus +
                ", errors=" + errors +
                ']';
    }
}
