package com.example.taquio.trasearch6.Models;

/**
 * Created by User on 6/30/2017.
 */

public class UserSettings {

    private User user;

    public UserSettings(User user) {
        this.user = user;
    }

    public UserSettings() {

    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "UserSettings{" +
                "user=" + user +
                '}';
    }
}
