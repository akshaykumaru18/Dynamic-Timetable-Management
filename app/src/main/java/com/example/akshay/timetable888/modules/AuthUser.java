package com.example.akshay.timetable888.modules;

public class AuthUser {

    private String UserId;
    private String securityLevel;

    public AuthUser(String userId, String securityLevel) {
        UserId = userId;
        this.securityLevel = securityLevel;
    }

    public AuthUser() {

    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getSecurityLevel() {
        return securityLevel;
    }

    public void setSecurityLevel(String securityLevel) {
        this.securityLevel = securityLevel;
    }
}
