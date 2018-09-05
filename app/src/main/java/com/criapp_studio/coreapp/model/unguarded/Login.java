package com.criapp_studio.coreapp.model.unguarded;

/**
 * Created by Galeen on 5.1.2017 г..
 */
public class Login {
    private User user;
    private int userId;
    private String sessionId;

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

}
