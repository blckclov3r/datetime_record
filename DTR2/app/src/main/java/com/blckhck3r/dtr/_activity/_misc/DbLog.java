package com.blckhck3r.dtr._activity._misc;

/**
 * Created by admin on 6/10/2018.
 */

public class DbLog {
    private int log_id;
    private String message;
    private String timestamp;

    public DbLog(String message, String timestamp) {
        this.message = message;
        this.timestamp = timestamp;
    }

    public DbLog(String message) {
        this.message = message;
    }

    public int getLog_id() {

        return log_id;
    }

    public void setLog_id(int log_id) {
        this.log_id = log_id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
