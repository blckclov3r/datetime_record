package com.blckhck3r.dtr._activity._database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import com.blckhck3r.dtr._activity._misc.Course;
import com.blckhck3r.dtr._activity._misc.Trainee;
import com.blckhck3r.dtr._activity._misc.BaseKey;
import com.blckhck3r.dtr._activity._misc.DbLog;

import java.util.ArrayList;

/**
 * Created by Abrenica, Aljun
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATA_LOG = "CREATE TABLE IF NOT EXISTS " + BaseKey.LOG_TABLE + " ("
            + BaseKey._ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
            + BaseKey.LOG_MESSAGE + " TEXT NOT NULL,"
            + BaseKey.LOG_TIMESTAMP + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP"
            + ");";

    private static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + BaseKey.TABLENAME + " ("
            + BaseKey._ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " //0
            + BaseKey.NAME + " TEXT ,"
            + BaseKey.COURSE + " TEXT ,"
            + BaseKey.EMAIL + " TEXT ,"
            + BaseKey.CONTACT + " BIGINT ,"
            + BaseKey.ADDRESS + " TEXT ,"
            + BaseKey.TIMESTAMP + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP,"
            + BaseKey.TIMEREMAINING + " INTEGER ,"  //7
            + BaseKey.TSCHED + " TEXT ,"            //8
            + BaseKey.TIME_START + " INTEGER ,"     //9
            + BaseKey.TIME_END + " INTEGER ,"       //10
            + BaseKey.START_MINUTE + " INTEGER ,"   //11
            + BaseKey.END_MINUTE + " INTEGER , "    //12
            + BaseKey.S_CONDITION + " TEXT , "      //13
            + BaseKey.E_CONDITION + " TEXT ,"        //14
            + BaseKey.TIMEREMAINING_MINUTE + " INTEGER "        //15
            + ");";
    private static final String CREATE_COURSE = "CREATE TABLE IF NOT EXISTS " + BaseKey.COURSE_TABLE + " ("
            + BaseKey._ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
            + BaseKey.TIMELIMIT + " INTEGER NOT NULL,"
            + BaseKey.COURSENAME + " TEXT NOT NULL,"
            + BaseKey.COURSEDESCRIPTION + " TEXT NOT NULL,"
            + BaseKey.REGISTRATIONLIMIT + " INTEGER NOT NULL,"
            + BaseKey.DAYSCHEDULE + " TEXT NOT NULL,"
            + BaseKey.TIME_START + " INTEGER NOT NULL,"
            + BaseKey.TIME_END + " INTEGER NOT NULL,"
            + BaseKey.START_MINUTE + " INTEGER NOT NULL,"
            + BaseKey.END_MINUTE + " INTEGER NOT NULL, "
            + BaseKey.S_CONDITION + " TEXT NOT NULL, "
            + BaseKey.E_CONDITION + " TEXT NOT NULL"
            + ");";


    public DatabaseHelper(Context context) {
        super(context, BaseKey.DBNAME, null, BaseKey.VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE);
        sqLiteDatabase.execSQL(CREATE_COURSE);
        sqLiteDatabase.execSQL(DATA_LOG);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + BaseKey.TABLENAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + BaseKey.COURSE_TABLE);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + BaseKey.LOG_TABLE);
        onCreate(sqLiteDatabase);
    }

    public void deleteAllLog() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE " + BaseKey.LOG_TABLE);
        onCreate(db);

    }

    public boolean addLog(DbLog log) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        try {
            cv.put(BaseKey.LOG_MESSAGE, log.getMessage());
            long result = db.insert(BaseKey.LOG_TABLE, null, cv);
            if (result == -1) {
                return false;
            } else {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    public Cursor getLogData() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM '" + BaseKey.LOG_TABLE + "'";
        Cursor data = db.rawQuery(query, null);
        return data;
    }


    public int delCourseData(String search) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(BaseKey.COURSE_TABLE, BaseKey.COURSENAME + "=?", new String[]{search});
    }

    public int countCourse(String course) {
        int counter = 0;
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            String sql = "SELECT COUNT(*) FROM " + BaseKey.TABLENAME + " WHERE " + BaseKey.COURSE + " = '" + course + "'";
            Cursor cursor = db.rawQuery(sql, null);
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                counter = cursor.getInt(0);
            }
        } catch (SQLiteException e) {
            e.printStackTrace();
        }
        return counter;
    }

    public ArrayList<String> getAllCourse() {
        ArrayList<String> list = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            String selectQuery = "SELECT * FROM " + BaseKey.COURSE_TABLE;
            @SuppressLint("Recycle") Cursor cursor = db.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                do {
                    String courseName = cursor.getString(cursor.getColumnIndex(BaseKey.COURSENAME));
                    list.add(courseName);
                } while (cursor.moveToNext());
            }
        } catch (SQLiteException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean addCourse(Course course) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        try {
            cv.put(BaseKey.TIMELIMIT, course.getTimeLimit());
            cv.put(BaseKey.COURSENAME, course.getAddCourse());
            cv.put(BaseKey.COURSEDESCRIPTION, course.getAddDescription());
            cv.put(BaseKey.REGISTRATIONLIMIT, course.getRegLimit());
            cv.put(BaseKey.DAYSCHEDULE, course.getDay());
            cv.put(BaseKey.TIME_START, course.getIn());
            cv.put(BaseKey.TIME_END, course.getOut());
            cv.put(BaseKey.START_MINUTE, course.getSmin());
            cv.put(BaseKey.END_MINUTE, course.getEmin());
            cv.put(BaseKey.S_CONDITION, course.getT1());
            cv.put(BaseKey.E_CONDITION, course.getT2());
            long result = db.insert(BaseKey.COURSE_TABLE, null, cv);
            if (result > 0) {
                return true;
            } else {
                return false;
            }
        } catch (SQLiteException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Cursor getCourseData() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + BaseKey.COURSE_TABLE + " WHERE _id ORDER BY _id DESC";
        return db.rawQuery(query, null);
    }

    public Cursor getCourse() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + BaseKey.COURSE_TABLE;
        return db.rawQuery(query, null);
    }

    public Cursor getCourseValue(String courseName) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + BaseKey.COURSE_TABLE + " WHERE " + BaseKey.COURSENAME + " = '" + courseName + "'";
        return db.rawQuery(query, null);
    }

    public Cursor retrieveCourse(String searchCourse) {
        SQLiteDatabase db = this.getWritableDatabase();
        String[] columns = {BaseKey._ID, BaseKey.TIMELIMIT, BaseKey.COURSENAME};
        Cursor cursor = null;
        if (searchCourse != null && searchCourse.length() > 0) {
            String sql = "SELECT * FROM " + BaseKey.COURSE_TABLE + " WHERE " + BaseKey.COURSENAME + " LIKE '" + searchCourse + "%'";
            cursor = db.rawQuery(sql, null);
            return cursor;
        }
        cursor = db.query(BaseKey.COURSE_TABLE, columns, BaseKey._ID, null, null, null, BaseKey.COURSENAME + " DESC");
        return cursor;
    }

    public int updateTime(int search, Trainee trainee) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        int row = 0;
        try {
            cv.put(BaseKey.TIMEREMAINING, trainee.getTimeremaining());
            cv.put(BaseKey.TIMEREMAINING_MINUTE, trainee.getTotal_time_minute());
            row = db.update(BaseKey.TABLENAME, cv, BaseKey._ID + "=?", new String[]{String.valueOf(search)});
        } catch (SQLiteException e) {
            e.printStackTrace();
        }
        return row;
    }

    public boolean addData(Trainee trainee) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        try {
            cv.put(BaseKey.NAME, trainee.getName());
            cv.put(BaseKey.COURSE, trainee.getCourse());
            cv.put(BaseKey.EMAIL, trainee.getEmail());
            cv.put(BaseKey.CONTACT, trainee.getContact());
            cv.put(BaseKey.ADDRESS, trainee.getAddress());
            cv.put(BaseKey.TIMEREMAINING, trainee.getTimeremaining());
            cv.put(BaseKey.TSCHED, trainee.gettSched());
            cv.put(BaseKey.TIME_START, trainee.getTimestart());
            cv.put(BaseKey.TIME_END, trainee.getTimeend());
            cv.put(BaseKey.START_MINUTE, trainee.getMinutestart());
            cv.put(BaseKey.END_MINUTE, trainee.getMinuteend());
            cv.put(BaseKey.S_CONDITION, trainee.getSt());
            cv.put(BaseKey.E_CONDITION, trainee.getEt());
            long result = db.insert(BaseKey.TABLENAME, null, cv);
            if (result > 0) {
                return true;
            } else {
                return false;
            }
        } catch (SQLiteException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Cursor getData() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM '" + BaseKey.TABLENAME + "' WHERE _id ORDER BY _id DESC";
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    public Cursor getDataId(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM '" + BaseKey.TABLENAME + "' WHERE name = '" + name + "'";
        return db.rawQuery(query, null);
    }

    public Cursor getCourseId(String course) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + BaseKey.COURSE_TABLE + " WHERE " + BaseKey.COURSENAME + " = '" + course + "'";
        return db.rawQuery(query, null);
    }

    public boolean editCourse(int search, Course course) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        try {
            cv.put(BaseKey.TIMELIMIT, course.getTimeLimit());
            cv.put(BaseKey.COURSENAME, course.getAddCourse());
            cv.put(BaseKey.COURSEDESCRIPTION, course.getAddDescription());
            cv.put(BaseKey.REGISTRATIONLIMIT, course.getRegLimit());
            cv.put(BaseKey.DAYSCHEDULE, course.getDay());
            cv.put(BaseKey.TIME_START, course.getIn());
            cv.put(BaseKey.TIME_END, course.getOut());
            cv.put(BaseKey.START_MINUTE, course.getSmin());
            cv.put(BaseKey.END_MINUTE, course.getEmin());
            cv.put(BaseKey.S_CONDITION, course.getT1());
            cv.put(BaseKey.E_CONDITION, course.getT2());
            long result = db.update(BaseKey.COURSE_TABLE, cv, BaseKey._ID + "=?", new String[]{String.valueOf(search)});
            if (result > 0) {
                return true;
            } else {
                return false;
            }
        } catch (SQLiteException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Cursor getEnrolled(String course) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + BaseKey.TABLENAME + " WHERE " + BaseKey.COURSE + " = '" + course + "'";
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    public boolean updateData(int search, Trainee trainee) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        try {
            cv.put(BaseKey.NAME, trainee.getName());
            cv.put(BaseKey.COURSE, trainee.getCourse());
            cv.put(BaseKey.EMAIL, trainee.getEmail());
            cv.put(BaseKey.CONTACT, trainee.getContact());
            cv.put(BaseKey.ADDRESS, trainee.getAddress());
            cv.put(BaseKey.TIMEREMAINING, trainee.getTimeremaining());
            cv.put(BaseKey.TIME_START, trainee.getTimestart());
            cv.put(BaseKey.TIME_END, trainee.getTimeend());
            cv.put(BaseKey.START_MINUTE, trainee.getMinutestart());
            cv.put(BaseKey.END_MINUTE, trainee.getMinuteend());
            cv.put(BaseKey.S_CONDITION, trainee.getSt());
            cv.put(BaseKey.E_CONDITION, trainee.getEt());
            long result = db.update(BaseKey.TABLENAME, cv, BaseKey._ID + "=?", new String[]{String.valueOf(search)});
            if (result > 0) {
                return true;
            } else {
                return false;
            }
        } catch (SQLiteException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateDataCourse(String search, Trainee trainee) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        try {
            cv.put(BaseKey.COURSE, trainee.getCourse());
            cv.put(BaseKey.TSCHED, trainee.gettSched());
            cv.put(BaseKey.TIME_START, trainee.getTimestart());
            cv.put(BaseKey.TIME_END, trainee.getTimeend());
            cv.put(BaseKey.START_MINUTE, trainee.getMinutestart());
            cv.put(BaseKey.END_MINUTE, trainee.getMinuteend());
            cv.put(BaseKey.S_CONDITION, trainee.getSt());
            cv.put(BaseKey.E_CONDITION, trainee.getEt());
            long result = db.update(BaseKey.TABLENAME, cv, BaseKey.COURSE + "=?", new String[]{String.valueOf(search)});
            if (result > 0) {
                return true;
            } else {
                return false;
            }
        } catch (SQLiteException e) {
            e.printStackTrace();
            return false;
        }
    }


    public int deleteData(String search) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(BaseKey.TABLENAME, BaseKey.NAME + "=?", new String[]{search});
    }

    public Cursor retrieve(String searchTerm) {
        SQLiteDatabase db = this.getWritableDatabase();
        String[] columns = {BaseKey._ID, BaseKey.NAME};
        Cursor cursor = null;
        if (searchTerm != null && searchTerm.length() > 0) {
            String sql = "SELECT * FROM " + BaseKey.TABLENAME + " WHERE " + BaseKey.NAME + " LIKE '" + searchTerm + "%'";
            cursor = db.rawQuery(sql, null);
            return cursor;
        }
        cursor = db.query(BaseKey.TABLENAME, columns, BaseKey._ID, null, null, null, "name DESC");
        return cursor;
    }

    public Cursor checkCourse(String s) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + BaseKey.TABLENAME + " WHERE " + BaseKey.COURSE + " = '" + s + "'";
        Cursor data = db.rawQuery(query, null);
        return data;
    }
}
