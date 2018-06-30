package com.blckhck3r.dtr._activity._activity.course_update;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.blckhck3r.dtr.R;
import com.blckhck3r.dtr._activity._database.databaseHelper;
import com.blckhck3r.dtr._activity._misc.Course;
import com.blckhck3r.dtr._activity._misc.Trainee;
import com.blckhck3r.dtr._activity._misc.dbLog;
import com.fujiyuu75.sequent.Animation;
import com.fujiyuu75.sequent.Sequent;

import cn.pedant.SweetAlert.SweetAlertDialog;
import es.dmoral.toasty.Toasty;
import in.codeshuffle.typewriterview.TypeWriterView;
import spencerstudios.com.bungeelib.Bungee;

public class edit_course_activity extends AppCompatActivity {
    static int Id, HrLimit, traineeLimit;
    static String courseName, courseDescription;
    EditText course, hrCourse, maxEnrollee, courseDesc;
    TextView courseId;
    Button updateCourse;
    databaseHelper dbHelper;
    String[] timeCondtion = {"AM", "PM"};
    Spinner t1, t2;
    String e_condition;
    String s_condition;
    EditText sched, start_hour, start_minute, end_hour, end_minute;
    String traineeCourse;
    TypeWriterView typeWriterView;
    private long mLastClickTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_course_activity);
        dbHelper = new databaseHelper(this);
        edit_course_activity.this.setTitle("Course Update | Edit");
        typeWriterView = (TypeWriterView) findViewById(R.id.title);
        course = (EditText) findViewById(R.id.course);
        hrCourse = (EditText) findViewById(R.id.hrCourse);
        maxEnrollee = (EditText) findViewById(R.id.maxEnrollee);
        courseDesc = (EditText) findViewById(R.id.courseDesc);
        updateCourse = (Button) findViewById(R.id.updateCourse);
        courseId = (TextView) findViewById(R.id.courseId);
        sched = (EditText) findViewById(R.id.sched);
        start_hour = (EditText) findViewById(R.id.start_hour);
        start_minute = (EditText) findViewById(R.id.start_minute);
        end_hour = (EditText) findViewById(R.id.end_hour);
        end_minute = (EditText) findViewById(R.id.end_minute);
        t1 = (Spinner) findViewById(R.id.t1);
        t2 = (Spinner) findViewById(R.id.t2);

        Intent intent = getIntent();
        Id = intent.getIntExtra("_id", -1);
        courseName = intent.getStringExtra("add_course");
        courseDescription = intent.getStringExtra("add_description");
        HrLimit = intent.getIntExtra("time_limit", 0);
        traineeLimit = intent.getIntExtra("course_limit", 0);
        String schedule = intent.getStringExtra("day_sched");
        int t_start = intent.getIntExtra("time_start", 0);
        int t_end = intent.getIntExtra("time_end", 0);
        int s_minute = intent.getIntExtra("start_minute", 0);
        int e_minute = intent.getIntExtra("end_minute", 0);
        s_condition = intent.getStringExtra("s_condition");
        e_condition = intent.getStringExtra("e_condition");

        sched.setText(schedule);
        start_hour.setText(String.valueOf(t_start));
        end_hour.setText(String.valueOf(t_end));
        start_minute.setText(String.valueOf(s_minute));
        end_minute.setText(String.valueOf(e_minute));
        course.setText(courseName);
        hrCourse.setText(String.valueOf(HrLimit));
        maxEnrollee.setText(String.valueOf(traineeLimit));
        courseDesc.setText(courseDescription);
        courseId.setText(String.valueOf(Id));
        setSpinner();
        updateBtn();

        course.setEnabled(false);
        course.setClickable(false);
        course.setFocusable(false);
    }

    @Override
    protected void onStart() {
        super.onStart();
        dbHelper.close();

//        new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
//                .setTitleText("Warning")
//                .setContentText("Don't try to edit the course who's already exists in the list view, Goodluck :D")
//                .setConfirmText(" Ok ")
//                .show();
//        course.setClickable(false);
//        course.setEnabled(false);
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                typeWriterView.animateText("Course Update");
                typeWriterView.setDelay(50);
                LinearLayout layoutBtn = (LinearLayout) findViewById(R.id.buttonArea);
                Sequent.origin(layoutBtn).anim(edit_course_activity.this, Animation.FADE_IN_LEFT).
                        delay(300).start();
                LinearLayout layout = (LinearLayout) findViewById(R.id.helloworld);
                Sequent.origin(layout).anim(edit_course_activity.this, Animation.BOUNCE_IN).
                        delay(500).start();
            }
        });
        typeWriterView.setWithMusic(false);
    }

    @Override
    protected void onPause() {
        super.onPause();
        typeWriterView.setWithMusic(false);
        typeWriterView.removeAnimation();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.about, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.aboutBtn) {
            new SweetAlertDialog(this, SweetAlertDialog.CUSTOM_IMAGE_TYPE)
                    .setTitleText("About")
                    .setCustomImage(R.drawable.ic_mindweb)
                    .setContentText("This application is for training purposes only \n© MindWeb IT eSolutions\n" +
                            "All Right Reserved 2018")
                    .setConfirmText(" Ok ")
                    .showCancelButton(false)
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            sDialog.dismissWithAnimation();
                        }
                    })
                    .show();
        }
        return super.onOptionsItemSelected(item);
    }

    public void updateBtn() {
        updateCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();

                traineeCourse = courseName;
                final String cou = course.getText().toString();
                final String hrCou = hrCourse.getText().toString();
                final String maxEn = maxEnrollee.getText().toString();
                final String couDes = courseDesc.getText().toString();
                final String couId = courseId.getText().toString();
                final String schedule = sched.getText().toString();
                String s_hour = start_hour.getText().toString();
                String e_hour = end_hour.getText().toString();
                String s_minute = start_minute.getText().toString();
                String e_minute = end_minute.getText().toString();
                final String s_con = t1.getSelectedItem().toString();
                final String e_con = t2.getSelectedItem().toString();
                final int startHour = Integer.parseInt(s_hour);
                final int endHour = Integer.parseInt(e_hour);
                final int startMin = Integer.parseInt(s_minute);
                final int endMin = Integer.parseInt(e_minute);


                new SweetAlertDialog(edit_course_activity.this, SweetAlertDialog.CUSTOM_IMAGE_TYPE)
                        .setTitleText("Are you sure?")
                        .setCustomImage(R.drawable._do_xml)
                        .setContentText("Do you want to update this course?")
                        .setConfirmText(" Yes ")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.dismissWithAnimation();
                                new SweetAlertDialog(edit_course_activity.this, SweetAlertDialog.SUCCESS_TYPE)
                                        .setTitleText("Success!")
                                        .setContentText("Course and Trainee, successfully updated")
                                        .show();
                                new Handler().post(new Runnable() {
                                    @Override
                                    public void run() {
                                        boolean traineeEdit = dbHelper.updateDataCourse(courseName, new Trainee(cou, schedule, startHour, endHour, startMin, endMin, s_con, e_con));
                                        boolean resultCourse = dbHelper.editCourse(Integer.parseInt(couId), new Course(Integer.parseInt(hrCou), cou, couDes,
                                                Integer.parseInt(maxEn), schedule, startHour, endHour, startMin, endMin, s_con, e_con));
                                        boolean zxcv = dbHelper.addLog(new dbLog("Course successfully updated, course title: " + courseName));
                                        if (traineeEdit && resultCourse && zxcv) {
                                            Toasty.success(getApplicationContext(), "Course and Trainee successfully updated", Toast.LENGTH_SHORT).show();
                                        }else if(resultCourse && zxcv){
                                            Toasty.success(getApplicationContext(), "Course successfully updated", Toast.LENGTH_SHORT).show();
                                        }
                                        traineeCourse = "";
                                        updateCourse.setEnabled(false);
                                        updateCourse.setClickable(false);
                                        updateCourse.setTextColor(Color.GRAY);
                                    }
                                });
                            }
                        })
                        .setCancelText(" No ")
                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.cancel();
                                updateCourse.setEnabled(true);
                                updateCourse.setClickable(true);
                                return;
                            }
                        })
                        .show();

            }
        });
    }

    public void Toast(final String s) {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                Toasty.info(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void setSpinner() {
        int test_s = 0;
        int test_e = 0;
        if (s_condition.equals("AM")) {
            test_s = 0;
        } else {
            test_s = 1;
        }
        if (e_condition.equals("AM")) {
            test_e = 0;
        } else {
            test_e = 1;
        }
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, timeCondtion);
        t1.setAdapter(adapter1);
        t1.setSelection(test_s);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, timeCondtion);
        t2.setAdapter(adapter2);
        t2.setSelection(test_e);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        typeWriterView.setWithMusic(false);
        typeWriterView.removeAnimation();
        dbHelper.close();
        Bungee.slideDown(edit_course_activity.this);
        finish();
    }
}