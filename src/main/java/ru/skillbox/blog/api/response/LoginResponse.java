package ru.skillbox.blog.api.response;

import com.fasterxml.jackson.annotation.JsonInclude;

public class LoginResponse {

    private boolean result;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private UserProfileResponse user;

    public LoginResponse(boolean result) {
        this.result = result;
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public UserProfileResponse getUser() {
        return user;
    }

    public void setUser(UserProfileResponse user) {
        this.user = user;
    }
}
