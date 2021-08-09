package ru.skillbox.blog.api.response;

public class LogoutResponse {

    private boolean result;

    public LogoutResponse(boolean result) {
        this.result = result;
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }
}
