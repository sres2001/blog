package ru.skillbox.blog.api.response;

public class AuthCheckResponse {

    private boolean result;

    public AuthCheckResponse(boolean result) {
        this.result = result;
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }
}
