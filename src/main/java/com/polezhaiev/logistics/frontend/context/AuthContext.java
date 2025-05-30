package com.polezhaiev.logistics.frontend.context;

import org.springframework.stereotype.Component;

@Component
public class AuthContext {
    private String accessToken;
    private String email;
    private String role;
    private String userId;

    public void setAccessToken(String token) {
        this.accessToken = token;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void clear() {
        accessToken = null;
        email = null;
        role = null;
        userId = null;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }

    public void setUserId(String id) {
        this.userId = id;
    }

    public String getUserId() {
        return userId;
    }
}
