package com.blckhck3r.dtr._activity._activity.info_activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blckhck3r.dtr.R;
import com.blckhck3r.dtr._activity._database.DatabaseHelper;
import com.fujiyuu75.sequent.Animation;
import com.fujiyuu75.sequent.Sequent;

import cn.pedant.SweetAlert.SweetAlertDialog;
import es.dmoral.toasty.Toasty;
import in.codeshuffle.typewriterview.TypeWriterView;
import spencerstudios.com.bungeelib.Bungee;

public class CourseInfoActivity extends AppCompatActivity {
    TextView courseName,courseDescription,courseHrLimit,courseRegLimit,courseId,enrolled;
    static int regNumber = 0;
    TextView dTraining,start_hour,start_minute,end_hour,end_minute,t_1,t_2;
    DatabaseHelper dbHelper;
    TypeWriterView typeWriterView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_info);
        dbHelper = new DatabaseHelper(getApplicationContext());
        CourseInfoActivity.this.setTitle("Course Information");
        typeWriterView= (TypeWriterView) findViewById(R.id.title);
        courseId = (TextView) findViewById(R.id.courseId);
        courseName = (TextView) findViewById(R.id.courseName);
        courseDescription = (TextView) findViewById(R.id.courseDescription);
        courseHrLimit = (TextView) findViewById(R.id.courseHrLimit);
        courseRegLimit = (TextView) findViewById(R.id.courseRegLimit);
        enrolled = (TextView) findViewById(R.id.enrolled);
        dTraining = (TextView) findViewById(R.id.dTraining);
        start_hour = (TextView) findViewById(R.id.start_hour);
        end_hour = (TextView) findViewById(R.id.end_hour);
        start_minute = (TextView) findViewById(R.id.start_minute);
        end_minute = (TextView) findViewById(R.id.end_minute);
        t_1 = (TextView) findViewById(R.id.t1);
        t_2 = (TextView) findViewById(R.id.t2);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = getIntent();
        int Id = intent.getIntExtra("_id",-1);
        String course = intent.getStringExtra("Add_Course");
        String courseDesc = intent.getStringExtra("add_description");
        int HrLimit = intent.getIntExtra("time_limit",0);
        int traineeLimit = intent.getIntExtra("course_limit",0);
        String day = intent.getStringExtra("day_sched");
        int startH = intent.getIntExtra("time_start",0);
        int endH = intent.getIntExtra("time_end",0);
        int minuteStart = intent.getIntExtra("start_minute",0);
        int minuteEnd = intent.getIntExtra("end_minute",0);
        String t1 = intent.getStringExtra("s_condition");
        String t2 = intent.getStringExtra("e_condition");
        regNumber = dbHelper.countCourse(course);
        dbHelper.close();
        courseHrLimit.setText(String.valueOf(HrLimit));
        courseName.setText(course);
        courseDescription.setText(courseDesc);
        courseId.setText(String.valueOf(Id));
        courseRegLimit.setText(String.valueOf(traineeLimit));
        enrolled.setText(String.valueOf(regNumber));
        dTraining.setText(String.valueOf(day));
        start_hour.setText(String.valueOf(startH));
        end_hour.setText(String.valueOf(endH));
        start_minute.setText(String.valueOf(minuteStart));
        end_minute.setText(String.valueOf(minuteEnd));
        t_1.setText(String.valueOf(t1));
        t_2.setText(String.valueOf(t2));
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                LinearLayout layout = (LinearLayout) findViewById(R.id.helloworld);
                Sequent.origin(layout).anim(CourseInfoActivity.this, Animation.BOUNCE_IN).
                        delay(500).start();
                typeWriterView.animateText("Course Info.");
                typeWriterView.setDelay(50);
            }
        });
        typeWriterView.setWithMusic(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.about, menu);
        return true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        typeWriterView.setWithMusic(false);
        typeWriterView.removeAnimation();
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        typeWriterView.setWithMusic(false);
        typeWriterView.removeAnimation();
        dbHelper.close();
        Bungee.slideLeft(CourseInfoActivity.this);
        finish();
    }

    public void Toast(final String s){
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                Toasty.normal(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
