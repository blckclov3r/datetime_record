package com.blckhck3r.dtr._activity._misc;


/**
 * Created by Abrenica, Aljun
 */
public class Course {
    int course_Id;
    int timeLimit =0;
    String addCourse;
    String addDescription;
    int regLimit = 0;
    String day;
    int in = 0;
    int out = 0;
    int smin = 0;
    int emin = 0;
    String t1 = "";
    String t2 = "";


    public Course(int timeLimit, String addCourse, String addDescription,
                  int regLimit, String day, int in, int out, int smin,
                  int emin, String t1, String t2) {
        this.timeLimit = timeLimit;
        this.addCourse = addCourse;
        this.addDescription = addDescription;
        this.regLimit = regLimit;
        this.day = day;
        this.in = in;
        this.out = out;
        this.smin = smin;
        this.emin = emin;
        this.t1 = t1;
        this.t2 = t2;
    }

    public String getT1() {

        return t1;
    }

    public void setT1(String t1) {
        this.t1 = t1;
    }

    public String getT2() {
        return t2;
    }

    public void setT2(String t2) {
        this.t2 = t2;
    }

    public int getSmin() {
        return smin;
    }

    public void setSmin(int smin) {
        this.smin = smin;
    }

    public int getEmin() {
        return emin;
    }

    public void setEmin(int emin) {
        this.emin = emin;
    }

    public Course(int timeLimit, String addCourse, String addDescription, int regLimit, String day, int in, int out) {
        this.timeLimit = timeLimit;
        this.addCourse = addCourse;
        this.addDescription = addDescription;
        this.regLimit = regLimit;
        this.day = day;
        this.in = in;
        this.out = out;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public int getIn() {
        return in;
    }

    public void setIn(int in) {
        this.in = in;
    }

    public int getOut() {
        return out;
    }

    public void setOut(int out) {
        this.out = out;
    }


    public int getCourse_Id() {
        return course_Id;
    }

    public void setCourse_Id(int course_Id) {
        this.course_Id = course_Id;
    }

    public int getTimeLimit() {
        return timeLimit;
    }

    public void setTimeLimit(int timeLimit) {
        this.timeLimit = timeLimit;
    }

    public String getAddCourse() {
        return addCourse;
    }

    public void setAddCourse(String addCourse) {
        this.addCourse = addCourse;
    }

    public String getAddDescription() {
        return addDescription;
    }

    public void setAddDescription(String addDescription) {
        this.addDescription = addDescription;
    }

    public int getRegLimit() {
        return regLimit;
    }

    public void setRegLimit(int regLimit) {
        this.regLimit = regLimit;
    }

    public Course(String addCourse) {
        this.addCourse = addCourse;
    }

    public Course(int timeLimit, String addCourse, String addDescription, int regLimit) {
        this.timeLimit = timeLimit;
        this.addCourse = addCourse;
        this.addDescription = addDescription;
        this.regLimit = regLimit;
    }

    public Course(){}
}
