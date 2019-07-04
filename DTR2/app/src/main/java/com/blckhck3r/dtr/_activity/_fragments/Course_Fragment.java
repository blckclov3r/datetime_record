package com.blckhck3r.dtr._activity._fragments;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.blckhck3r.dtr.R;
import com.blckhck3r.dtr._activity._activity.course_enrolled.Course_Enrolled;
import com.blckhck3r.dtr._activity._activity.course_update.EditCourse_Activity;
import com.blckhck3r.dtr._activity._activity.info_activity.CourseInfoActivity;
import com.blckhck3r.dtr._activity._database.DatabaseHelper;
import com.blckhck3r.dtr._activity._misc.Course;
import com.blckhck3r.dtr._activity._misc.DbLog;

import org.michaelbel.bottomsheet.BottomSheet;
import org.michaelbel.bottomsheet.Utils;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;
import es.dmoral.toasty.Toasty;
import spencerstudios.com.bungeelib.Bungee;

/**
 * Created by Abrenica, Aljun
 */

public class Course_Fragment extends Fragment {
    DatabaseHelper dbHelper;
    ListView lv;
    ArrayList<Course> listData;
    CustomAdapter adapter;
    SearchView sv;
    String myCourse;
    int cList = 0;
    private long mLastClickTime = 0;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.edit_frag,container,false);
        lv = (ListView) view.findViewById(R.id.updateListView);
        dbHelper = new DatabaseHelper(getActivity());
        listData = new ArrayList<>();
        listData.clear();
        sv = (SearchView) view.findViewById(R.id.sv);
        sv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sv.setIconified(false);
            }
        });
        populateView();
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listData.clear();
        getSearchName("");
        if(cList == 0){
            Toast("No data found");
        }else{
            new Handler().post(new Runnable() {
                @Override
                public void run() {
//                    new SweetAlertDialog(getActivity(), SweetAlertDialog.SUCCESS_TYPE)
//                            .setTitleText("Course Section")
//                            .setContentText("Tips\nLong press the items to show up the dialog" +
//                                    "\nSingle click to show course information")
//                            .show();
                    Toasty.info(getActivity(),"Tips: Long press the items to show up the dialog, " +
                            "Single click to show course information",Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        getSearchName("");
        listData.clear();
        populateView();
        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                getSearchName(newText);
//                Toast.makeText(getActivity(), "You are searching for . . .", Toast.LENGTH_SHORT).show();
                return false;
            }
        });
    }

    public void getSearchName(String searchCourse) {
        listData.clear();
        Course course;
        Cursor c = dbHelper.retrieveCourse(searchCourse);
        int id;
        String name="";
        if(c.moveToFirst()){
            do{
                id = c.getInt(0);
                name = c.getString(2);
                course = new Course();
                course.setCourse_Id(id);
                course.setAddCourse(name);
                listData.add(course);
            }while(c.moveToNext());
        }
        c.close();
        dbHelper.close();
        lv.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    public void populateView(){
        Cursor cursor = dbHelper.getCourseData();
        if(cursor.moveToFirst()){
            do{
                String course = cursor.getString(2);
                listData.add(new Course(course));

            }while(cursor.moveToNext());
        }
        cursor.close();
        dbHelper.close();
        adapter = new CustomAdapter(getActivity(),R.layout.custom_activity,listData);
        lv.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private class CustomAdapter extends BaseAdapter{
        private Context context;
        private int layout;
        ArrayList<Course> courseList;

        public CustomAdapter(Context context, int layout, ArrayList<Course> courseList) {
            this.context = context;
            this.layout = layout;
            this.courseList = courseList;
        }

        @Override
        public int getCount() {
            cList = courseList.size();
            return courseList.size();
        }

        @Override
        public Object getItem(int i) {
            return i;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View row = view;
            CustomAdapter.ViewHolder holder = new CustomAdapter.ViewHolder();
            if(row == null){
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                row = inflater.inflate(layout,null);
                holder.name = row.findViewById(R.id.txtName);
                row.setTag(holder);
            }else{
                holder = (CustomAdapter.ViewHolder)row.getTag();
            }

            final Course course = courseList.get(i);
            holder.name.setText(course.getAddCourse());

            row.setOnClickListener(new AdapterView.OnClickListener(){
                int idList = -1;
                String courseName = "";
                String courseDesc = "";
                int timeLimit = 0;
                int course_limit = 0;

                String day = "";
                int startHour = 0;
                int endHour = 0;
                int startMinute =0;
                int endMinute = 0;
                String t_start = "";
                String  t_end = "";

                @Override
                public void onClick(View view) {
                    Cursor cursor = dbHelper.getCourseId(course.getAddCourse());
                    if(cursor.moveToFirst()){
                        do{
                            idList = cursor.getInt(0);
                            timeLimit = cursor.getInt(1);
                            courseName = cursor.getString(2);
                            courseDesc = cursor.getString(3);
                            course_limit = cursor.getInt(4);

                            day = cursor.getString(5);
                            startHour = cursor.getInt(6);
                            endHour = cursor.getInt(7);
                            startMinute = cursor.getInt(8);
                            endMinute = cursor.getInt(9);
                            t_start = cursor.getString(10);
                            t_end = cursor.getString(11);

                            Intent intent = new Intent(getActivity(), CourseInfoActivity.class);
                            intent.putExtra("_id",idList);
                            intent.putExtra("Add_Course",courseName);
                            intent.putExtra("add_description",courseDesc);
                            intent.putExtra("time_limit",timeLimit);
                            intent.putExtra("course_limit",course_limit);

                            intent.putExtra("day_sched",day);
                            intent.putExtra("time_start",startHour);
                            intent.putExtra("time_end",endHour);
                            intent.putExtra("start_minute",startMinute);
                            intent.putExtra("end_minute",endMinute);
                            intent.putExtra("s_condition",t_start);
                            intent.putExtra("e_condition",t_end);
                            startActivity(intent);
                            Bungee.slideRight(getActivity());
                        }while (cursor.moveToNext());
                    }
                    cursor.close();
                }
            });

            row.setOnLongClickListener(new AdapterView.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {

                    BottomSheet.Builder builder = new BottomSheet.Builder(getActivity());
                    builder.setTitle("C:/> Course: "+course.getAddCourse()+"");
                    builder.setContentType(BottomSheet.GRID);
                    builder.setTitleTextColorRes(R.color.colorPrimaryDark);
                    builder.setFullWidth(true);
                    builder.setCellHeight(Utils.dp(getActivity(), 48));

                    builder.setDividers(true);
                    builder.setTitleMultiline(true);
                    builder.setWindowDimming(80);
//                    builder.setDarkTheme(false);
                    builder.setItemTextColor(getResources().getColor(R.color.colorPrimaryDark));
                    builder.setIconColor(getResources().getColor(R.color.white));
                    builder.setBackgroundColor(getResources().getColor(R.color.white));
                    builder.setMenu(R.menu.course_bottomsheet, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            int id = which;
                            switch (id){
                                case 0: {
                                    int idList = -1;
                                    String courseName = "";
                                    String courseDesc = "";
                                    int timeLimit = 0;
                                    int course_limit = 0;

                                    String schedule = "";
                                    int start_hour = 0;
                                    int end_hour = 0;
                                    int start_minute = 0;
                                    int end_minute = 0;
                                    String t1 = "";
                                    String t2 = "";
                                    Cursor cursor = dbHelper.getCourseId(course.getAddCourse());
                                    if (cursor.moveToFirst()) {
                                        do {
                                            idList = cursor.getInt(0);
                                            timeLimit = cursor.getInt(1);
                                            courseName = cursor.getString(2);
                                            courseDesc = cursor.getString(3);
                                            course_limit = cursor.getInt(4);

                                            schedule = cursor.getString(5);
                                            start_hour = cursor.getInt(6);
                                            end_hour = cursor.getInt(7);
                                            start_minute = cursor.getInt(8);
                                            end_minute = cursor.getInt(9);
                                            t1 = cursor.getString(10);
                                            t2 = cursor.getString(11);

                                            Intent intent = new Intent(getActivity(), EditCourse_Activity.class);
                                            intent.putExtra("_id", idList);
                                            intent.putExtra("Add_Course", courseName);
                                            intent.putExtra("add_description", courseDesc);
                                            intent.putExtra("time_limit", timeLimit);
                                            intent.putExtra("course_limit", course_limit);

                                            intent.putExtra("day_sched", schedule);
                                            intent.putExtra("time_start", start_hour);
                                            intent.putExtra("time_end", end_hour);
                                            intent.putExtra("start_minute", start_minute);
                                            intent.putExtra("end_minute", end_minute);
                                            intent.putExtra("s_condition", t1);
                                            intent.putExtra("e_condition", t2);
                                            startActivity(intent);
                                            Bungee.slideUp(getActivity());
                                        } while (cursor.moveToNext());
                                    }
                                }break;
                                case 1: {
                                    int idList = -1;
                                    String courseName = "";
                                    String courseDesc = "";
                                    int timeLimit = 0;
                                    int course_limit = 0;

                                    Cursor cursor = dbHelper.getCourseId(course.getAddCourse());
                                    if (cursor.moveToFirst()) {
                                        do {
                                            idList = cursor.getInt(0);
                                            timeLimit = cursor.getInt(1);
                                            courseName = cursor.getString(2);
                                            courseDesc = cursor.getString(3);
                                            course_limit = cursor.getInt(4);

                                            Intent intent = new Intent(getActivity(), Course_Enrolled.class);
                                            intent.putExtra("_id", idList);
                                            intent.putExtra("Add_Course", courseName);
                                            intent.putExtra("add_description", courseDesc);
                                            intent.putExtra("time_limit", timeLimit);
                                            intent.putExtra("course_limit", course_limit);
                                            startActivity(intent);
                                            Bungee.slideUp(getActivity());
                                        } while (cursor.moveToNext());
                                    }
                                    cursor.close();
                                }break;
                                case 2:{
                                    if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                                        return;
                                    }
                                    mLastClickTime = SystemClock.elapsedRealtime();
                                    final Cursor cursor = dbHelper.checkCourse(String.valueOf(course.getAddCourse()));
                                    if(cursor.moveToFirst()){
                                        do{
                                            new Handler().post(new Runnable() {
                                                @Override
                                                public void run() {
                                                    Toasty.error(getActivity(),"Unable to delete, there are some trainee enrolled in this course",Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                            return;
                                        }while (cursor.moveToNext());
                                    }
                                    cursor.close();
                                    dbHelper.close();
                                    myCourse = course.getAddCourse();
                                    new SweetAlertDialog(getActivity(), SweetAlertDialog.CUSTOM_IMAGE_TYPE)
                                            .setCustomImage(R.drawable.delete)
                                            .setTitleText("Are you sure to delete this course?")
                                            .setContentText("Won't be able to recover the data, course name: "+course.getAddCourse())
                                            .setConfirmText(" Yes ")
                                            .setCancelText(" No ")
                                            .showCancelButton(true)
                                            .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                @Override
                                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                    sweetAlertDialog.cancel();
                                                }
                                            })
                                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                @Override
                                                public void onClick(SweetAlertDialog sDialog) {
                                                    new Handler().post(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            int result = dbHelper.delCourseData(course.getAddCourse());
                                                            getSearchName("");
                                                            listData.clear();
                                                            populateView();
                                                            CustomAdapter adapter = new CustomAdapter(getActivity(),R.layout.custom_activity,listData);
                                                            lv.setAdapter(adapter);
                                                            dbHelper.close();
                                                            if(result>0){
                                                                boolean x = dbHelper.addLog(new DbLog("Course successfully deleted, course title: "+String.valueOf(myCourse)));
                                                                dbHelper.close();
                                                                Toasty.success(getActivity(), "Course Successfully deleted", Toast.LENGTH_SHORT).show();
                                                            }else{
                                                                new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE)
                                                                        .setTitleText("Oops...")
                                                                        .setContentText("Something went wrong!")
                                                                        .show();
                                                                boolean x = dbHelper.addLog(new DbLog("Course not deleted, course title: "+String.valueOf(myCourse)));
                                                                dbHelper.close();
                                                                Toasty.error(getActivity(), "Course not delete", Toast.LENGTH_SHORT).show();
                                                            }
                                                        }
                                                    });
                                                    sDialog
                                                            .setTitleText("Deleted!")
                                                            .setCancelText(null)
                                                            .showCancelButton(false)
                                                            .setContentText("Course successfully deleted")
                                                            .setConfirmText("OK")
                                                            .setConfirmClickListener(null)
                                                            .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                                                }
                                            })
                                            .show();
                                }break;
                            }
                        }
                    });
                    builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            dbHelper.close();
                            return;
                        }
                    });

                    builder.show();
                    return true;
                }
            });

            return row;
        }

        public class ViewHolder{
            TextView name;
        }
    }
    public void Toast(final String s){
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                Toasty.info(getActivity(),s,Toast.LENGTH_LONG).show();
            }
        });

    }

}
