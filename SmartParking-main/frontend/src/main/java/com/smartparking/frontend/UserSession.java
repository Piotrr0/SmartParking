package com.smartparking.frontend;

public class UserSession {

    private static UserSession instance;

    private Long userId;
    private String username;

    private UserSession(Long userId, String username) {
        this.userId = userId;
        this.username = username;
    }

    public static void setSession(Long userId, String username) {
        instance = new UserSession(userId, username);
    }
    public static UserSession getInstance() {
        return instance;
    }
    public static void cleanUserSession() {
        instance = null;
    }
    public Long getUserId() {
        return userId;
    }
    public String getUsername() {
        return username;
    }
}