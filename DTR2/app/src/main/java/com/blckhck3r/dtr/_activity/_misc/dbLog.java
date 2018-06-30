package com.blckhck3r.dtr._activity._misc;

/**
 * Created by admin on 6/10/2018.
 */

public class dbLog {
    private int _id;
    private String message;
    private String timestamp;

    public dbLog(String message, String timestamp) {
        this.message = message;
        this.timestamp = timestamp;
    }

    public dbLog(String message) {
        this.message = message;
    }

    public int get_id() {

        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
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
