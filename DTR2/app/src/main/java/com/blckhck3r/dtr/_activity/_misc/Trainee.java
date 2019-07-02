package com.blckhck3r.dtr._activity._misc;


/**
 * Created by Abrenica, Aljun
 */

public class Trainee {

    private int trainee_id;
    private String name;
    private String course;
    private String email;
    private String contact;
    private String address;
    private String timestamp;
    private int timeremaining;
    private String tSched;
    private int timestart = 0;
    private int timeend = 0;

    private int minutestart =0;
    private int minuteend =0;
    private String st ="";
    private String et="";
    //id,sched,timein,timeout,s_min,e_min,t1,t2


    public Trainee(String course, String tSched, int timestart, int timeend, int minutestart, int minuteend, String st, String et) {
        this.course = course;
        this.tSched = tSched;
        this.timestart = timestart;
        this.timeend = timeend;
        this.minutestart = minutestart;
        this.minuteend = minuteend;
        this.st = st;
        this.et = et;
    }

    public Trainee(String name, String course, String email, String contact, String address, int timeremaining, int timestart, int timeend, int minutestart, int minuteend, String st, String et) {
        this.name = name;
        this.course = course;
        this.email = email;
        this.contact = contact;
        this.address = address;
        this.timeremaining = timeremaining;
        this.timestart = timestart;
        this.timeend = timeend;
        this.minutestart = minutestart;
        this.minuteend = minuteend;
        this.st = st;
        this.et = et;
    }

    public Trainee(String name, String course, String email, String contact, String address, String timestamp, int timeremaining, String tSched, int timestart, int timeend, int minutestart, int minuteend, String st, String et, int total_time_minute) {
        this.name = name;
        this.course = course;
        this.email = email;
        this.contact = contact;
        this.address = address;
        this.timestamp = timestamp;
        this.timeremaining = timeremaining;
        this.tSched = tSched;
        this.timestart = timestart;
        this.timeend = timeend;
        this.minutestart = minutestart;
        this.minuteend = minuteend;
        this.st = st;
        this.et = et;
        this.total_time_minute = total_time_minute;
    }

    int total_time_minute = 0;

    public int getTotal_time_minute() {
        return total_time_minute;
    }

    public void setTotal_time_minute(int total_time_minute) {
        this.total_time_minute = total_time_minute;
    }

    public Trainee(int timeremaining, int total_time_minute) {

        this.timeremaining = timeremaining;
        this.total_time_minute = total_time_minute;
    }

    public int getMinutestart() {
        return minutestart;
    }

    public void setMinutestart(int minutestart) {
        this.minutestart = minutestart;
    }

    public int getMinuteend() {
        return minuteend;
    }

    public void setMinuteend(int minuteend) {
        this.minuteend = minuteend;
    }

    public String getSt() {
        return st;
    }

    public void setSt(String st) {
        this.st = st;
    }

    public String getEt() {
        return et;
    }

    public void setEt(String et) {
        this.et = et;
    }

    public Trainee(String name, String course, String email, String contact,
                   String address, int timeremaining, String tSched,
                   int timestart, int timeend, int minutestart, int minuteend,
                   String st, String et) {
        this.name = name;
        this.course = course;
        this.email = email;
        this.contact = contact;
        this.address = address;
        this.timeremaining = timeremaining;
        this.tSched = tSched;
        this.timestart = timestart;
        this.timeend = timeend;

        this.minutestart = minutestart;
        this.minuteend = minuteend;
        this.st = st;
        this.et = et;
    }


    public Trainee(String name, String course, String email, String contact, String address, int timeremaining, String tSched, int timestart, int timeend) {
        this.name = name;
        this.course = course;
        this.email = email;
        this.contact = contact;
        this.address = address;
        this.timeremaining = timeremaining;
        this.tSched = tSched;
        this.timestart = timestart;
        this.timeend = timeend;
    }



    public String gettSched() {
        return tSched;
    }

    public void settSched(String tSched) {
        this.tSched = tSched;
    }

    public Trainee(String name, String course, String email, String contact, String address, int timeremaining, int timestart, int timeend) {
        this.name = name;
        this.course = course;
        this.email = email;
        this.contact = contact;
        this.address = address;
        this.timeremaining = timeremaining;
        this.timestart = timestart;
        this.timeend = timeend;
    }

    public int getTimestart() {
        return timestart;
    }

    public void setTimestart(int timestart) {
        this.timestart = timestart;
    }

    public int getTimeend() {
        return timeend;
    }

    public void setTimeend(int timeend) {
        this.timeend = timeend;
    }

    public Trainee(String name, String course, String email, String contact, String address, int timeremaining) {
        this.name = name;
        this.course = course;
        this.email = email;
        this.contact = contact;
        this.address = address;
        this.timeremaining = timeremaining;
    }

    public Trainee(int timeremaining) {
        this.timeremaining = timeremaining;
    }

    public int getTimeremaining() {

        return timeremaining;
    }

    public void setTimeremaining(int timeremaining) {
        this.timeremaining = timeremaining;
    }
    public Trainee(int id,String name, String timestamp) {
        this.name = name;
        this.timestamp = timestamp;
    }

    public Trainee(String name, String timestamp) {
        this.name = name;
        this.timestamp = timestamp;
    }

    public String getTimestamp() {

        return timestamp;
    }
    public Trainee(String name) {
        this.name = name;
    }
    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public Trainee(){}

    public Trainee(int trainee_id, String name, String course, String email, String contact, String address) {
        this.trainee_id = trainee_id;
        this.name = name;
        this.course = course;
        this.email = email;
        this.contact = contact;
        this.address = address;
    }

    public Trainee(String name, String course, String email, String contact, String address) {
        this.name = name;
        this.course = course;
        this.email = email;
        this.contact = contact;
        this.address = address;
    }



    public int getTrainee_id() {
        return trainee_id;
    }

    public void setTrainee_id(int trainee_id) {
        this.trainee_id = trainee_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
