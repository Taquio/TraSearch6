package com.example.taquio.trasearch6.Models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Edward on 28/02/2018.
 */

public class Report implements Parcelable{

    private String message_report;
    private String photo_path;

    public Report(String message_report, String photo_path) {
        this.message_report = message_report;
        this.photo_path = photo_path;
    }

    @Override
    public String toString() {
        return "Report{" +
                "message_report='" + message_report + '\'' +
                ", photo_path='" + photo_path + '\'' +
                '}';
    }

    protected Report(Parcel in) {
        message_report = in.readString();
        photo_path = in.readString();
    }

    public static final Creator<Report> CREATOR = new Creator<Report>() {
        @Override
        public Report createFromParcel(Parcel in) {
            return new Report(in);
        }

        @Override
        public Report[] newArray(int size) {
            return new Report[size];
        }
    };

    public String getMessage_report() {
        return message_report;
    }

    public void setMessage_report(String message_report) {
        this.message_report = message_report;
    }

    public String getPhoto_path() {
        return photo_path;
    }

    public void setPhoto_path(String photo_path) {
        this.photo_path = photo_path;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(message_report);
        dest.writeString(photo_path);
    }
}


