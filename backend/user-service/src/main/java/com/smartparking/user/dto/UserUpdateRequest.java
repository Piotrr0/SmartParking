package com.smartparking.user.dto;

public class UserUpdateRequest {
    private String username;
    private String email;
    private String newPassword;

    public UserUpdateRequest() {
    }

    public UserUpdateRequest(String username, String email, String newPassword) {
        this.username = username;
        this.email = email;
        this.newPassword = newPassword;
    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getNewPassword() {
        return newPassword;
    }
    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
