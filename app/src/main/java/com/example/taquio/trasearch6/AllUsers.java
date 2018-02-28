package com.example.taquio.trasearch6;

/**
 * Created by Taquio on 2/25/2018.
 */

public class AllUsers {

    public String userType;

    public AllUsers(){}

    public AllUsers(String userType) {
        this.userType = userType;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }
}
