package com.blckhck3r.dtr._activity._activity.update_delete_activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
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
import com.blckhck3r.dtr._activity._activity.main_activity.SplashScreen;
import com.blckhck3r.dtr._activity._activity.time_activity.TraineeTimeList;
import com.blckhck3r.dtr._activity._database.databaseHelper;
import com.blckhck3r.dtr._activity._misc.Trainee;
import com.blckhck3r.dtr._activity._misc.dbLog;
import com.fujiyuu75.sequent.Animation;
import com.fujiyuu75.sequent.Sequent;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;
import es.dmoral.toasty.Toasty;
import in.codeshuffle.typewriterview.TypeWriterView;
import spencerstudios.com.bungeelib.Bungee;

public class UpdateInfoActivity extends AppCompatActivity {
    static int counterBtn = 0;
    static int tLimit = 0, tCompare = 0;
    static int traineeTime = 0;
    EditText infoUpdateName, infoUpdateAddress, infoUpdateNumber, infoUpdateEmail;
    TextView infoUpdateId;
    SearchableSpinner infoUpdateCourse;
    Button infoUpdateBtn;
    EditText trainee_time;
    //    resetBtn;
//    String[] course = {"Empty", "Android", "C#", ".NET", "Wordpress", "Java"};
    ArrayList<String> courseList;
    ArrayAdapter<String> courseAdapter;
    databaseHelper dbHelper;
    ArrayList<Trainee> listData;
    int s_hour = 0;
    int e_hour = 0;
    int s_minute = 0;
    int e_minute = 0;
    String st = "";
    String et = "";
     TypeWriterView typeWriterView;
    private long mLastClickTime = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_info);
        dbHelper = new databaseHelper(this);
        UpdateInfoActivity.this.setTitle("Trainee Update");
        typeWriterView= (TypeWriterView) findViewById(R.id.typeWriterView);
        listData = new ArrayList<>();
        infoUpdateName = (EditText) findViewById(R.id.infoUpdateName);
        infoUpdateAddress = (EditText) findViewById(R.id.infoUpdateAddress);
        infoUpdateCourse = (SearchableSpinner) findViewById(R.id.infoUpdateCourse);
        infoUpdateCourse.setTitle("Course");
        infoUpdateCourse.setPositiveButton("Ok");
        infoUpdateNumber = (EditText) findViewById(R.id.infoUpdateNumber);
        infoUpdateEmail = (EditText) findViewById(R.id.infoUpdateEmail);
        infoUpdateId = (TextView) findViewById(R.id.infoUpdateId);
        infoUpdateBtn = (Button) findViewById(R.id.infoUpdateBtn);
        trainee_time = (EditText) findViewById(R.id.trainee_time);

        Intent intent = getIntent();
        int Id = intent.getIntExtra("_id", -1);
        int tR = intent.getIntExtra("timeremaining", 0);
        int start = intent.getIntExtra("time_start", 0);
        int end = intent.getIntExtra("time_end", 0);

        String selectedId = String.valueOf(Id);
        String selectedName = intent.getStringExtra("name");
        String selectedCourse = intent.getStringExtra("course");
        String selectedEmail = intent.getStringExtra("email");
        String selectedContact = intent.getStringExtra("contact");
        String selectedAddress = intent.getStringExtra("address");
        String selectedTimestamp = intent.getStringExtra("timestamp");
        String timeremaining = String.valueOf(tR);
        courseList = new ArrayList<>();
        courseList = dbHelper.getAllCourse();
        courseAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, courseList);
        infoUpdateCourse.setAdapter(courseAdapter);
        courseAdapter.notifyDataSetChanged();
        infoUpdateId.setText(selectedId);
        infoUpdateName.setText(selectedName);
        infoUpdateAddress.setText(selectedAddress);
        infoUpdateEmail.setText(selectedEmail);
        infoUpdateNumber.setText(selectedContact);
        trainee_time.setText(timeremaining);

        for (int i = 0; i < courseList.size(); i++) {
            if (courseList.get(i).equals(selectedCourse)) {
                infoUpdateCourse.setSelection(i);
                break;
            }
        }

        setInfoUpdateBtn();
        infoUpdateId.setVisibility(View.INVISIBLE);

        infoUpdateName.setEnabled(false);
        infoUpdateName.setClickable(false);

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
    public void setInfoUpdateBtn() {
        infoUpdateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();

                final String id = infoUpdateId.getText().toString();
                final String name = infoUpdateName.getText().toString();
                final String course = infoUpdateCourse.getSelectedItem().toString();
                final String email = infoUpdateEmail.getText().toString();
                final String number = infoUpdateNumber.getText().toString();
                final String address = infoUpdateAddress.getText().toString().trim();
                final String tremaining = trainee_time.getText().toString().trim();

                String regexString = "[a-zA-Z ]+";
                String regexNum = "\\d+";

                if (TextUtils.isEmpty(name)) {
                    infoUpdateName.setError("Name is required");
                    return;
                }
                if (!(name.matches(regexString))) {
                    infoUpdateName.setError("Name must not contain any special character or number");
                    return;
                }
                if (name.length() <= 4) {
                    infoUpdateName.setError("The name you entered is too low");
                    return;
                }
                if (name.length() >= 30) {
                    infoUpdateName.setError("The name you entered is too long");
                    return;
                }
                if (course.equals("")) {
                    Toast.makeText(getApplicationContext(), "Please pick one course", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(email)) {
                    infoUpdateEmail.setError("Email address is required");
                    return;
                }

                if (email.length() >= 30) {
                    infoUpdateEmail.setError("Email is too long");
                }

                if (TextUtils.isEmpty(number)) {
                    infoUpdateNumber.setError("Contact Number is required");
                    return;
                }

                if (!number.matches(regexNum)) {
                    infoUpdateNumber.setError("Contact number must not contain any special character or string");
                    return;
                }
                if (number.length() >= 15) {
                    infoUpdateNumber.setError("Contact Number is too long");
                    return;
                }


                if (TextUtils.isEmpty(address)) {
                    infoUpdateAddress.setError("Address is required");
                    return;
                }
                if (address.length() >= 999) {
                    infoUpdateName.setError("Address is too long");
                    return;
                }


                final Cursor cursorCourse = dbHelper.getCourseValue(course);
                if (cursorCourse.moveToFirst()) {
                    do {
                        traineeTime = cursorCourse.getInt(1);
                        tCompare = cursorCourse.getInt(4);
                    } while (cursorCourse.moveToNext());
                }
                cursorCourse.close();
                dbHelper.close();


                final Cursor cursorTime = dbHelper.getCourseId(course);
                if(cursorTime.moveToFirst()){
                    do{
                        s_hour = cursorTime.getInt(6);
                        e_hour = cursorTime.getInt(7);
                        s_minute = cursorTime.getInt(8);
                        e_minute = cursorTime.getInt(9);
                        st = cursorTime.getString(10);
                        et = cursorTime.getString(11);
                    }while (cursorTime.moveToNext());
                }
                cursorTime.close();

                if (Integer.parseInt(tremaining) > traineeTime) {
                    new SweetAlertDialog(UpdateInfoActivity.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Oops... Something went wrong")
                            .setContentText("The course time is update lately, please check the course time remaining in the course section")
                            .show();
                    trainee_time.setError("The total time of training you entered is greater than in particular course");
                    Toasty.error(UpdateInfoActivity.this,"There something wrong :(",Toast.LENGTH_SHORT).show();
                    return;
                }

                if (Integer.parseInt(tremaining) <= 0) {
                    trainee_time.setError("The total time of training must not equal to zero or any negative integer");
                    return;
                }

                if (tremaining.length() == 0) {
                    trainee_time.setError("The total time of training is required");
                    return;
                }

                tLimit = dbHelper.countCourse(course);
                if (tLimit >= tCompare) {
                    counterBtn++;
                    if (counterBtn >= 3) {
                        Toast("Solution:  \n" +
                                "1.) Edit " + course + " course located in course section\n" +
                                "2.) Extend the maximum enrollee \n" +
                                "3.) Goodluck :D hehe");
                        return;
                    }
                    Toasty.normal(getApplicationContext(), "Data not updated, the course you selected is " +
                            "\n\t\t\t\t\t\t\t@ maximum limit", Toast.LENGTH_SHORT).show();
                    dbHelper.close();
                    return;
                }
                dbHelper.close();
                new SweetAlertDialog(UpdateInfoActivity.this, SweetAlertDialog.CUSTOM_IMAGE_TYPE)
                        .setCustomImage(R.drawable._do_xml)
                        .setTitleText("Are you sure?")
                        .setContentText("Do you want to update this trainee? | time remaining remain the same in the text field," +
                                "unless you want to change it")
                        .setConfirmText(" Yes ")
                        .setCancelText(" No ")
                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.cancel();
                            }
                        })
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {

                                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                                    return;
                                }
                                mLastClickTime = SystemClock.elapsedRealtime();

                                boolean result = dbHelper.updateData(Integer.parseInt(id), new Trainee(name, course, email, number, address,
                                        Integer.parseInt(tremaining),s_hour,e_hour,s_minute,e_minute,st,et));
                                if (result) {
                                    Toasty.success(getApplicationContext(),"Trainee successfully updated",Toast.LENGTH_SHORT).show();
                                    sweetAlertDialog.setTitleText("Success!")
                                            .setContentText("Trainee successfully updated!")
                                            .setConfirmText("OK")
                                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                @Override
                                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                    sweetAlertDialog.dismiss();
                                                    infoUpdateBtn.setClickable(false);
                                                    infoUpdateBtn.setEnabled(false);
                                                }
                                            })
                                            .setCancelClickListener(null)
                                            .showCancelButton(false)
                                            .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
//                                    Toast.makeText(UpdateInfoActivity.this, "Trainee Successfully Updated", Toast.LENGTH_SHORT).show();
                                    boolean zcxv = dbHelper.addLog(new dbLog("Trainee successfully updated, name: " + name + "" +
                                            ",email: " + email + ", contact number: " + number + ", address: " + address + " time remaining: " + tremaining));
                                    infoUpdateBtn.setClickable(false);
                                    infoUpdateBtn.setEnabled(false);
                                    infoUpdateBtn.setTextColor(Color.GRAY);
                                    traineeTime = 0;
                                    tCompare = 0;
                                    counterBtn = 0;
                                    tLimit = 0;
                                } else {
                                    sweetAlertDialog.dismiss();
                                    Toasty.normal(UpdateInfoActivity.this, "Trainee not update", Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .show();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        counterBtn = 0;

        new Handler().post(new Runnable() {
            @Override
            public void run() {
                typeWriterView.animateText("Trainee Update");
                LinearLayout helloworld = (LinearLayout) findViewById(R.id.helloworld);
                LinearLayout layout = (LinearLayout) findViewById(R.id.buttonArea);
                Sequent.origin(layout).anim(UpdateInfoActivity.this, Animation.FADE_IN_LEFT).
                        delay(300).start();
                Sequent.origin(helloworld).anim(UpdateInfoActivity.this, Animation.BOUNCE_IN).
                        delay(500).start();
            }
        });
        typeWriterView.setWithMusic(false);

    }

    public void Toast(final String s) {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                Toasty.normal(getApplicationContext(), s, Toast.LENGTH_LONG).show();
            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();
        typeWriterView.setWithMusic(false);
        typeWriterView.removeAnimation();

    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        typeWriterView.setWithMusic(false);
        typeWriterView.removeAnimation();
        dbHelper.close();
        Bungee.slideDown(UpdateInfoActivity.this);
        finish();
    }
}
