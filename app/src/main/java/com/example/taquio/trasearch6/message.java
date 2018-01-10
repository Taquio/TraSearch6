package com.example.taquio.trasearch6;

/**
 * Created by taquio on 1/6/18.
 */

public class message {

    private String content,userName;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public message(){}

    public message(String content, String userName) {
        this.content = content;
        this.userName = userName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}
