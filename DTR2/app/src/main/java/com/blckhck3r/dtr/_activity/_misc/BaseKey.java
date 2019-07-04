package com.blckhck3r.dtr._activity._misc;

import android.provider.BaseColumns;

/**
 * Created by Abrenica, Aljun
 */

public class BaseKey implements BaseColumns {
    //Trainee
    public static final int VERSION = 1;
    public static final String TABLENAME ="trainee";
    public static final String DBNAME = "mydb.db";
    public static final String NAME = "name";
    public static final String COURSE = "course";
    public static final String EMAIL = "email";
    public static final String CONTACT ="contact";
    public static final String ADDRESS ="address";
    public static final String TIME_START = "time_start";
    public static final String TIME_END ="time_end";
    public static final String TIMESTAMP="timestamp";
    public static final String TIMEREMAINING="timeremaining";
    public static final String TSCHED = "trainee_sched";
    public static final String TIMEREMAINING_MINUTE ="timeremaining_minute";


    //courses
    public static final String COURSE_TABLE ="course_tbl";
    public static final String TIMELIMIT = "time_limit";
    public static final String COURSENAME ="Add_Course";
    public static final String COURSEDESCRIPTION = "add_description";
    public static final String REGISTRATIONLIMIT = "course_limit";
    public static final String DAYSCHEDULE = "day_sched";
    public static final String START_MINUTE = "start_minute";
    public static final String END_MINUTE ="end_minute";
    public static final String S_CONDITION ="s_condition";
    public static final String E_CONDITION ="e_condition";


    //db log
    public static final String LOG_TABLE ="log_tbl";
    public static final String LOG_MESSAGE = "log_message";
    public static final String LOG_TIMESTAMP ="timestamp";


}
