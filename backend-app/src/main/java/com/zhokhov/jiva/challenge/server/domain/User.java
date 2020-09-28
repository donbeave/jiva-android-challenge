package com.zhokhov.jiva.challenge.server.domain;

public class User {

    private final String id;
    private final String email;
    private final String password;
    private String avatarPath;

    public User(String id, String email, String password, String avatarPath) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.avatarPath = avatarPath;
    }

    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getAvatarPath() {
        return avatarPath;
    }

    public void setAvatarPath(String avatarPath) {
        this.avatarPath = avatarPath;
    }

}
