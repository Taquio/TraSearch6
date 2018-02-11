package com.example.taquio.trasearch6;

/**
 * Created by taquio on 2/11/18.
 */

public class Users {

    private String Name,Email,Image;

    public Users(){

    }

    public Users(String name, String email, String image) {
        Name = name;
        Email = email;
        Image = image;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }
}
