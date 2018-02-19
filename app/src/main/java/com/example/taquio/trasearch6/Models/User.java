package com.example.taquio.trasearch6.Models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by User on 6/26/2017.
 */

public class User implements Parcelable {


    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
    private String userID, Email, Name, Image,
            Image_thumb, PhoneNumber, UserName, device_token;
    private Long online;


    public User() {

    }

    protected User(Parcel in) {
        userID = in.readString();
        Email = in.readString();
        Name = in.readString();
        Image = in.readString();
        Image_thumb = in.readString();
        PhoneNumber = in.readString();
        UserName = in.readString();
        device_token = in.readString();
        if (in.readByte() == 0) {
            online = null;
        } else {
            online = in.readLong();
        }
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getImage_thumb() {
        return Image_thumb;
    }

    public void setImage_thumb(String image_thumb) {
        Image_thumb = image_thumb;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getDevice_token() {
        return device_token;
    }

    public void setDevice_token(String device_token) {
        this.device_token = device_token;
    }

    public Long getOnline() {
        return online;
    }

    public void setOnline(Long online) {
        this.online = online;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(userID);
        dest.writeString(Email);
        dest.writeString(Name);
        dest.writeString(Image);
        dest.writeString(Image_thumb);
        dest.writeString(PhoneNumber);
        dest.writeString(UserName);
        dest.writeString(device_token);
        if (online == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(online);
        }
    }
}
