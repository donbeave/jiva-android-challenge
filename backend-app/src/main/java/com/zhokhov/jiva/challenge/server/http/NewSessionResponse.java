package com.zhokhov.jiva.challenge.server.http;

import com.fasterxml.jackson.annotation.JsonProperty;

public class NewSessionResponse {

    @JsonProperty("userid")
    private String userId;
    private String token;

    public NewSessionResponse() {
    }

    public NewSessionResponse(String userId, String token) {
        setuserId(userId);
        setToken(token);
    }

    public String getuserId() {
        return userId;
    }

    public void setuserId(String userId) {
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

}
