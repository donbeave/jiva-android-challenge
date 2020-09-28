package com.zhokhov.jiva.challenge.server.http;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AvatarResponse {

    @JsonProperty("avatar_url")
    private String avatarUrl;

    public AvatarResponse() {
    }

    public AvatarResponse(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

}
