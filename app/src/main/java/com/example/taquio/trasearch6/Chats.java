package com.example.taquio.trasearch6;

/**
 * Created by taquio on 2/16/18.
 */

public class Chats {

    private String message,type;
    private long time;
    private boolean seened;
    private String from;

    public Chats(){}

    public Chats(String message, String type, long time, boolean seened, String from) {
        this.message = message;
        this.type = type;
        this.time = time;
        this.seened = seened;
        this.from = from;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public boolean isSeened() {
        return seened;
    }

    public void setSeened(boolean seened) {
        this.seened = seened;
    }
}
