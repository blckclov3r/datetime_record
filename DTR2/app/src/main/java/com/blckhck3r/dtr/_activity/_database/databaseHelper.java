package com.blckhck3r.dtr._activity._database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import com.blckhck3r.dtr._activity._misc.Course;
import com.blckhck3r.dtr._activity._misc.Trainee;
import com.blckhck3r.dtr._activity._misc.baseKey;
import com.blckhck3r.dtr._activity._misc.dbLog;

import java.util.ArrayList;

/**
 * Created by Abrenica, Aljun
 */

public class databaseHelper extends SQLiteOpenHelper {

    public static final String DATA_LOG = "CREATE TABLE IF NOT EXISTS " + baseKey.LOG_TABLE + " ("
            + baseKey._ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
            + baseKey.LOG_MESSAGE + " TEXT NOT NULL,"
            + baseKey.LOG_TIMESTAMP + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP"
            + ");";
    private static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + baseKey.TABLENAME + " ("
            + baseKey._ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " //0
            + baseKey.NAME + " TEXT ,"
            + baseKey.COURSE + " TEXT ,"
            + baseKey.EMAIL + " TEXT ,"
            + baseKey.CONTACT + " BIGINT ,"
            + baseKey.ADDRESS + " TEXT ,"
            + baseKey.TIMESTAMP + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP,"
            + baseKey.TIMEREMAINING + " INTEGER ,"  //7
            + baseKey.TSCHED + " TEXT ,"            //8
            + baseKey.TIME_START + " INTEGER ,"     //9
            + baseKey.TIME_END + " INTEGER ,"       //10
            + baseKey.START_MINUTE + " INTEGER ,"   //11
            + baseKey.END_MINUTE + " INTEGER , "    //12
            + baseKey.S_CONDITION + " TEXT , "      //13
            + baseKey.E_CONDITION + " TEXT ,"        //14
            + baseKey.TIMEREMAINING_MINUTE+" INTEGER "        //15
            + ");";
    private static final String CREATE_COURSE = "CREATE TABLE IF NOT EXISTS " + baseKey.COURSE_TABLE + " ("
            + baseKey._ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
            + baseKey.TIMELIMIT + " INTEGER NOT NULL,"
            + baseKey.COURSENAME + " TEXT NOT NULL,"
            + baseKey.COURSEDESCRIPTION + " TEXT NOT NULL,"
            + baseKey.REGISTRATIONLIMIT + " INTEGER NOT NULL,"
            + baseKey.DAYSCHEDULE + " TEXT NOT NULL,"
            + baseKey.TIME_START + " INTEGER NOT NULL,"
            + baseKey.TIME_END + " INTEGER NOT NULL,"
            + baseKey.START_MINUTE + " INTEGER NOT NULL,"
            + baseKey.END_MINUTE + " INTEGER NOT NULL, "
            + baseKey.S_CONDITION + " TEXT NOT NULL, "
            + baseKey.E_CONDITION + " TEXT NOT NULL"
            + ");";


    public databaseHelper(Context context) {
        super(context, baseKey.DBNAME, null, baseKey.VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE);
        sqLiteDatabase.execSQL(CREATE_COURSE);
        sqLiteDatabase.execSQL(DATA_LOG);
    }
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + baseKey.TABLENAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + baseKey.COURSE_TABLE);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + baseKey.LOG_TABLE);
        onCreate(sqLiteDatabase);
    }
    public void deleteAllLog() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE " + baseKey.LOG_TABLE);
        onCreate(db);

    }
    public boolean addLog(dbLog log) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        db.beginTransaction();
        try{
            cv.put(baseKey.LOG_MESSAGE, log.getMessage());
            long result = db.insert(baseKey.LOG_TABLE, null, cv);
            db.setTransactionSuccessful();
            if (result == -1) {
                return false;
            } else {
                return true;
            }
        }catch (Exception e){
            e.printStackTrace();
            db.close();
            return false;
        }finally {
            db.endTransaction();
            db.close();
        }

    }
    public Cursor getLogData() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM '" + baseKey.LOG_TABLE + "'";
        Cursor data = db.rawQuery(query, null);
        return data;
    }


    public int delCourseData(String search) {
        SQLiteDatabase db = this.getWritableDatabase();
        return  db.delete(baseKey.COURSE_TABLE, baseKey.COURSENAME + "=?", new String[]{search});
    }

    public int countCourse(String course) {
        int counter = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        db.beginTransaction();
        try{
            String sql = "SELECT COUNT(*) FROM " + baseKey.TABLENAME + " WHERE " + baseKey.COURSE + " = '" + course + "'";
            Cursor cursor = db.rawQuery(sql, null);
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                counter = cursor.getInt(0);
            }
            db.setTransactionSuccessful();
        }catch (SQLiteException e){
            e.printStackTrace();
            db.close();
        }finally {
            db.endTransaction();
            db.close();
        }
        return counter;
    }

    public ArrayList<String> getAllCourse() {
        ArrayList<String> list = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            String selectQuery = "SELECT * FROM " + baseKey.COURSE_TABLE;
            Cursor cursor = db.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                do {
                    String courseName = cursor.getString(cursor.getColumnIndex(baseKey.COURSENAME));
                    list.add(courseName);
                } while (cursor.moveToNext());
            }
            db.setTransactionSuccessful();
        } catch (SQLiteException e) {
            e.printStackTrace();
            db.close();
        }
        return list;
    }
    public boolean addCourse(Course course) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        db.beginTransaction();
        try {
            cv.put(baseKey.TIMELIMIT, course.getTimeLimit());
            cv.put(baseKey.COURSENAME, course.getAddCourse());
            cv.put(baseKey.COURSEDESCRIPTION, course.getAddDescription());
            cv.put(baseKey.REGISTRATIONLIMIT, course.getRegLimit());
            cv.put(baseKey.DAYSCHEDULE, course.getDay());
            cv.put(baseKey.TIME_START, course.getIn());
            cv.put(baseKey.TIME_END, course.getOut());
            cv.put(baseKey.START_MINUTE, course.getSmin());
            cv.put(baseKey.END_MINUTE, course.getEmin());
            cv.put(baseKey.S_CONDITION, course.getT1());
            cv.put(baseKey.E_CONDITION, course.getT2());
            long result = db.insert(baseKey.COURSE_TABLE, null, cv);
            db.setTransactionSuccessful();
            if (result > 0) {
                return true;
            } else {
                return false;
            }
        } catch (SQLiteException e) {
            e.printStackTrace();
            db.close();
            return false;
        } finally {
            db.endTransaction();
            db.close();
        }
    }

    public Cursor getCourseData() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + baseKey.COURSE_TABLE + " WHERE _id ORDER BY _id DESC";
        return db.rawQuery(query, null);
    }

    public Cursor getCourse(){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + baseKey.COURSE_TABLE;
        return db.rawQuery(query, null);
    }

    public Cursor getCourseValue(String courseName) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + baseKey.COURSE_TABLE + " WHERE " + baseKey.COURSENAME + " = '" + courseName + "'";
        return db.rawQuery(query, null);
    }

    public Cursor retrieveCourse(String searchCourse) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {baseKey._ID, baseKey.TIMELIMIT, baseKey.COURSENAME};
        Cursor cursor = null;
        if (searchCourse != null && searchCourse.length() > 0) {
            String sql = "SELECT * FROM " + baseKey.COURSE_TABLE + " WHERE " + baseKey.COURSENAME + " LIKE '" + searchCourse + "%'";
            cursor = db.rawQuery(sql, null);
            return cursor;
        }
        cursor = db.query(baseKey.COURSE_TABLE, columns, baseKey._ID, null, null, null, baseKey.COURSENAME + " DESC");
        return cursor;
    }
    public int updateTime(int search, Trainee trainee) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        int row = 0;
        db.beginTransaction();
        try{
            cv.put(baseKey.TIMEREMAINING, trainee.getTimeremaining());
            cv.put(baseKey.TIMEREMAINING_MINUTE,trainee.getTotal_time_minute());
            row =  db.update(baseKey.TABLENAME, cv, baseKey._ID + "=?", new String[]{String.valueOf(search)});
            db.setTransactionSuccessful();
        }catch (SQLiteException e){
            e.printStackTrace();
            db.close();
        }finally {
            db.endTransaction();
            db.close();
        }
        return row;
    }
    public boolean addData(Trainee trainee) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        db.beginTransaction();
        try {
            cv.put(baseKey.NAME, trainee.getName());
            cv.put(baseKey.COURSE, trainee.getCourse());
            cv.put(baseKey.EMAIL, trainee.getEmail());
            cv.put(baseKey.CONTACT, trainee.getContact());
            cv.put(baseKey.ADDRESS, trainee.getAddress());
            cv.put(baseKey.TIMEREMAINING, trainee.getTimeremaining());
            cv.put(baseKey.TSCHED, trainee.gettSched());
            cv.put(baseKey.TIME_START, trainee.getTimestart());
            cv.put(baseKey.TIME_END, trainee.getTimeend());
            cv.put(baseKey.START_MINUTE,trainee.getMinutestart());
            cv.put(baseKey.END_MINUTE,trainee.getMinuteend());
            cv.put(baseKey.S_CONDITION,trainee.getSt());
            cv.put(baseKey.E_CONDITION,trainee.getEt());
            long result = db.insert(baseKey.TABLENAME, null, cv);
            db.setTransactionSuccessful();
            if (result > 0) {
                return true;
            } else {
                return false;
            }
        } catch (SQLiteException e) {
            e.printStackTrace();
            return false;
        } finally {
            db.endTransaction();
            db.close();
        }
    }

    public Cursor getData() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM '" + baseKey.TABLENAME + "' WHERE _id ORDER BY _id DESC";
        Cursor data = db.rawQuery(query, null);
        return data;
    }
    public Cursor getDataId(String name) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM '" + baseKey.TABLENAME + "' WHERE name = '" + name + "'";
        return db.rawQuery(query, null);
    }

    public Cursor getCourseId(String course){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM "+baseKey.COURSE_TABLE+" WHERE "+baseKey.COURSENAME+" = '"+course+"'";
        return db.rawQuery(query,null);
    }

    public boolean editCourse(int search, Course course) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        db.beginTransaction();
        try {
            cv.put(baseKey.TIMELIMIT, course.getTimeLimit());
            cv.put(baseKey.COURSENAME, course.getAddCourse());
            cv.put(baseKey.COURSEDESCRIPTION, course.getAddDescription());
            cv.put(baseKey.REGISTRATIONLIMIT, course.getRegLimit());
            cv.put(baseKey.DAYSCHEDULE,course.getDay());
            cv.put(baseKey.TIME_START,course.getIn());
            cv.put(baseKey.TIME_END,course.getOut());
            cv.put(baseKey.START_MINUTE,course.getSmin());
            cv.put(baseKey.END_MINUTE,course.getEmin());
            cv.put(baseKey.S_CONDITION,course.getT1());
            cv.put(baseKey.E_CONDITION,course.getT2());
            long result = db.update(baseKey.COURSE_TABLE, cv, baseKey._ID + "=?", new String[]{String.valueOf(search)});
            db.setTransactionSuccessful();
            if (result > 0) {
                return true;
            } else {
                return false;
            }
        } catch (SQLiteException e) {
            e.printStackTrace();
            db.close();
            return false;
        } finally {
            db.endTransaction();
            db.close();
        }
    }
    public Cursor getEnrolled(String course) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + baseKey.TABLENAME + " WHERE " + baseKey.COURSE + " = '" + course + "'";
        Cursor data = db.rawQuery(query, null);
        return data;
    }
    public boolean updateData(int search, Trainee trainee) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        db.beginTransaction();
        try {
            cv.put(baseKey.NAME, trainee.getName());
            cv.put(baseKey.COURSE, trainee.getCourse());
            cv.put(baseKey.EMAIL, trainee.getEmail());
            cv.put(baseKey.CONTACT, trainee.getContact());
            cv.put(baseKey.ADDRESS, trainee.getAddress());
            cv.put(baseKey.TIMEREMAINING, trainee.getTimeremaining());
            cv.put(baseKey.TIME_START, trainee.getTimestart());
            cv.put(baseKey.TIME_END, trainee.getTimeend());
            cv.put(baseKey.START_MINUTE,trainee.getMinutestart());
            cv.put(baseKey.END_MINUTE,trainee.getMinuteend());
            cv.put(baseKey.S_CONDITION,trainee.getSt());
            cv.put(baseKey.E_CONDITION,trainee.getEt());
            long result = db.update(baseKey.TABLENAME, cv, baseKey._ID + "=?", new String[]{String.valueOf(search)});
            db.setTransactionSuccessful();
            if (result > 0) {
                return true;
            } else {
                return false;
            }
        } catch (SQLiteException e) {
            e.printStackTrace();
            db.close();
            return false;
        } finally {
            db.endTransaction();
            db.close();
        }
    }

    public boolean updateDataCourse(String search,Trainee trainee) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        db.beginTransaction();
        try {
            cv.put(baseKey.COURSE,trainee.getCourse());
            cv.put(baseKey.TSCHED,trainee.gettSched());
            cv.put(baseKey.TIME_START, trainee.getTimestart());
            cv.put(baseKey.TIME_END, trainee.getTimeend());
            cv.put(baseKey.START_MINUTE,trainee.getMinutestart());
            cv.put(baseKey.END_MINUTE,trainee.getMinuteend());
            cv.put(baseKey.S_CONDITION,trainee.getSt());
            cv.put(baseKey.E_CONDITION,trainee.getEt());
            long result = db.update(baseKey.TABLENAME, cv, baseKey.COURSE + "=?", new String[]{String.valueOf(search)});
            db.setTransactionSuccessful();
            if (result > 0) {
                return true;
            } else {
                return false;
            }
        } catch (SQLiteException e) {
            e.printStackTrace();
            return false;
        } finally {
            db.endTransaction();
            db.close();
        }
    }


    public int deleteData(String search) {
        SQLiteDatabase db = this.getWritableDatabase();
        return  db.delete(baseKey.TABLENAME, baseKey.NAME + "=?", new String[]{search});
    }
    public Cursor retrieve(String searchTerm) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {baseKey._ID, baseKey.NAME};
        Cursor cursor = null;
        if (searchTerm != null && searchTerm.length() > 0) {
            String sql = "SELECT * FROM " + baseKey.TABLENAME + " WHERE " + baseKey.NAME + " LIKE '" + searchTerm + "%'";
            cursor = db.rawQuery(sql, null);
            return cursor;
        }
        cursor = db.query(baseKey.TABLENAME, columns, baseKey._ID, null, null, null, "name DESC");
        return cursor;
    }
    public Cursor checkCourse(String s) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + baseKey.TABLENAME + " WHERE " + baseKey.COURSE + " = '" + s + "'";
        Cursor data = db.rawQuery(query, null);
        return data;
    }
}
