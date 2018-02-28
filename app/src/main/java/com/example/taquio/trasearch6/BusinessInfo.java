package com.example.taquio.trasearch6;

/**
 * Created by Del Mar on 2/24/2018.
 */

public class BusinessInfo {

    String bsnEmail;
    String bsnBusinessName;
    String bsnLocation;
    String bsnMobile;
    String bsnPhone;
    String imagePermit;
    String image;
    String image_thumb;
    String deviceToken;
    String userId;
    String userType;
    boolean isVerify;

    public BusinessInfo(String bsnEmail, String bsnBusinessName, String bsnLocation, String bsnMobile, String bsnPhone, String imagePermit, String image, String image_thumb, String deviceToken, String userId, String userType, boolean isVerify) {
        this.bsnEmail = bsnEmail;
        this.bsnBusinessName = bsnBusinessName;
        this.bsnLocation = bsnLocation;
        this.bsnMobile = bsnMobile;
        this.bsnPhone = bsnPhone;
        this.imagePermit = imagePermit;
        this.image = image;
        this.image_thumb = image_thumb;
        this.deviceToken = deviceToken;
        this.userId = userId;
        this.userType = userType;
        this.isVerify = isVerify;
    }

    public String getBsnEmail() {
        return bsnEmail;
    }

    public void setBsnEmail(String bsnEmail) {
        this.bsnEmail = bsnEmail;
    }

    public String getBsnBusinessName() {
        return bsnBusinessName;
    }

    public void setBsnBusinessName(String bsnBusinessName) {
        this.bsnBusinessName = bsnBusinessName;
    }

    public String getBsnLocation() {
        return bsnLocation;
    }

    public void setBsnLocation(String bsnLocation) {
        this.bsnLocation = bsnLocation;
    }

    public String getBsnMobile() {
        return bsnMobile;
    }

    public void setBsnMobile(String bsnMobile) {
        this.bsnMobile = bsnMobile;
    }

    public String getBsnPhone() {
        return bsnPhone;
    }

    public void setBsnPhone(String bsnPhone) {
        this.bsnPhone = bsnPhone;
    }

    public String getImagePermit() {
        return imagePermit;
    }

    public void setImagePermit(String imagePermit) {
        this.imagePermit = imagePermit;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getImage_thumb() {
        return image_thumb;
    }

    public void setImage_thumb(String image_thumb) {
        this.image_thumb = image_thumb;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public boolean isVerify() {
        return isVerify;
    }

    public void setVerify(boolean verify) {
        isVerify = verify;
    }
}
