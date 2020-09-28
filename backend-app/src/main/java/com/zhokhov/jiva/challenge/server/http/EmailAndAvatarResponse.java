package com.zhokhov.jiva.challenge.server.http;

import com.fasterxml.jackson.annotation.JsonProperty;

public class EmailAndAvatarResponse {

    private String email;

    @JsonProperty("avatar_url")
    private String avatarUrl;

    public EmailAndAvatarResponse() {
    }

    public EmailAndAvatarResponse(String email, String avatarUrl) {
        setEmail(email);
        setAvatarUrl(avatarUrl);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

}
