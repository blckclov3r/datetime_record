package com.blckhck3r.dtr._activity._activity.time_activity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
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
import com.blckhck3r.dtr._activity._activity.update_delete_activity.UpdateInfoActivity;
import com.blckhck3r.dtr._activity._database.databaseHelper;
import com.blckhck3r.dtr._activity._misc.Trainee;
import com.blckhck3r.dtr._activity._misc.dbLog;
import com.fujiyuu75.sequent.Animation;
import com.fujiyuu75.sequent.Sequent;

import cn.pedant.SweetAlert.SweetAlertDialog;
import es.dmoral.toasty.Toasty;
import in.codeshuffle.typewriterview.TypeWriterView;
import spencerstudios.com.bungeelib.Bungee;

public class TraineeTimeList extends AppCompatActivity {
    static int startTime =0;
    static int endTime =0;
    static int elapseTime = 0;
    static int totalTime = 0;
    static int timeremaining = 0;
    static int newTime = 0;
    static int startMinute = 0;
    static int endMinute = 0;
    static int temp_minute = 0;
    static int test = 0;
    static int temp_test = 0;
    TextView infoName, infoId, infoCourse, tRemaining, tStatus,time_remaining_minute;
    EditText traineeStart, traineeEnd;
    TextView thistime;
    Button submitBtn;
    String myStatus = "";
    EditText tStartMinute, tEndMinute;
    Spinner t1, t2;
    String[] timeCondition = {"AM", "PM"};
    databaseHelper dbHelper;
    int t_main_remaining =0;
    static int start_m = 0;
    int timeremaing=0;
    static int elapse = 0;
    int temporary = 0;
    String name = "";
    TypeWriterView typeWriterView;
    private long mLastClickTime =0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trainee_time_list);
        dbHelper = new databaseHelper(this);
        TraineeTimeList.this.setTitle("Time Update");
        typeWriterView= (TypeWriterView) findViewById(R.id.title);
        infoName = (TextView) findViewById(R.id.infoName);
        infoId = (TextView) findViewById(R.id.infoId);
        infoCourse = (TextView) findViewById(R.id.infoCourse);
        tRemaining = (TextView) findViewById(R.id.timeRemaining);
        tStatus = (TextView) findViewById(R.id.timeStatus);
        submitBtn = (Button) findViewById(R.id.submitBtn);
        time_remaining_minute = (TextView) findViewById(R.id.time_remaining_minute);
        traineeStart = (EditText) findViewById(R.id.traineeStart);
        traineeEnd = (EditText) findViewById(R.id.traineeEnd);
        thistime = (TextView) findViewById(R.id.thisTime);
        t1 = (Spinner) findViewById(R.id.t1);
        t2 = (Spinner) findViewById(R.id.t2);
        tStartMinute = (EditText) findViewById(R.id.tStartMinute);
        tEndMinute = (EditText) findViewById(R.id.tEndMinute);
        thistime.setText("");
        tRemaining.setText("");
        Intent intent = getIntent();
        int Id = intent.getIntExtra("_id", -1);
        timeremaing = intent.getIntExtra("timeremaining", 0);
        int start = intent.getIntExtra("time_start", 0);
        int end = intent.getIntExtra("time_end", 0);
        String selectedId = String.valueOf(Id);
        String selectedName = intent.getStringExtra("name");
        String selectedCourse = intent.getStringExtra("course");
        String t_s = intent.getStringExtra("s_condition");
        String t_e = intent.getStringExtra("e_condition");
        start_m = intent.getIntExtra("start_minute",0);
        int end_m = intent.getIntExtra("end_minute",0);
        t_main_remaining = intent.getIntExtra("timeremaining_minute",0);
        time_remaining_minute.setText(String.valueOf(t_main_remaining));
        tStartMinute.setText(String.valueOf(start_m));
        tEndMinute.setText(String.valueOf(end_m));
        traineeStart.setText(String.valueOf(start));
        traineeEnd.setText(String.valueOf(end));
        int x = Integer.parseInt(String.valueOf(timeremaing));
        newTime = x;
        if (x == 0) {
            Toast("Task Complete");
            tStatus.setText("Complete");
            start_m = 0;
            time_remaining_minute.setText("00");
            tRemaining.setText("00");
            tStatus.setTextColor(Color.rgb(0, 80, 0));
            traineeStart.setEnabled(false);
            traineeEnd.setEnabled(false);
            submitBtn.setEnabled(false);
            submitBtn.setClickable(false);
            submitBtn.setTextColor(Color.GRAY);
//            submitBtn.setBackgroundColor(Color.GRAY);
        } else {
            Toast("Incomplete");
            tStatus.setText("Incomplete");
            tStatus.setTextColor(Color.rgb(80, 0, 0));
        }

        infoId.setText(selectedId);
        infoName.setText(selectedName);
        infoCourse.setText(selectedCourse);
        tRemaining.setText(String.valueOf(newTime));
        myStatus = tStatus.getText().toString();

        int test_s = 0;
        int test_e = 0;
        if(t_s.equals("AM")){
            test_s = 0;
        }else{
            test_s = 1;
        }

        if(t_e.equals("AM")){
            test_e = 0;
        }else{
            test_e = 1;
        }

        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, timeCondition);
        t1.setAdapter(adapter1);
        t1.setSelection(test_s);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, timeCondition);
        t2.setAdapter(adapter2);
        t2.setSelection(test_e);
        setSubmitBtn();
    }

    @Override
    protected void onStart() {
        super.onStart();
        startTime =0;
        endTime =0;
        elapseTime = 0;
        totalTime = 0;
        timeremaining = 0;
        newTime = 0;
        startMinute = 0;
        endMinute = 0;
        temp_minute = 0;
        test = 0;
        temp_test = 0;

        new Handler().post(new Runnable() {
            @Override
            public void run() {
                typeWriterView.animateText("Time Update");
                typeWriterView.setDelay(50);
                LinearLayout layoutBtn = (LinearLayout) findViewById(R.id.buttonArea);
                Sequent.origin(layoutBtn).anim(TraineeTimeList.this, Animation.FADE_IN_LEFT).
                        delay(300).start();
                LinearLayout layout = (LinearLayout) findViewById(R.id.helloworld);
                Sequent.origin(layout).anim(TraineeTimeList.this, Animation.BOUNCE_IN).
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

    public void setSubmitBtn() {
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();

                String regexNum = "\\d+";
                final String id = infoId.getText().toString();
                name = infoName.getText().toString();
                String x = traineeStart.getText().toString().trim();
                String y = traineeEnd.getText().toString().trim();
                final String tIn = t1.getSelectedItem().toString();
                final String tOut = t2.getSelectedItem().toString();
                String timeInMinute = tStartMinute.getText().toString();
                String timeOutMinute = tEndMinute.getText().toString();


                if (TextUtils.isEmpty(x)) {
                    traineeStart.setText("00");
                    Toasty.error(getApplicationContext(),"Timein hour should not empty",Toast.LENGTH_SHORT).show();
                    return;
                } else if (TextUtils.isEmpty(y)) {
                    traineeEnd.setText("00");
                    Toasty.error(getApplicationContext(),"Timeout hour should not empty",Toast.LENGTH_SHORT).show();
                    return;
                } else if (traineeStart.length() >= 3 || traineeEnd.length() >= 3 || tStartMinute.length() >=3 || tEndMinute.length()>=3) {
                    Toasty.error(getApplicationContext(),"Timein length should not equal or more than 3",Toast.LENGTH_SHORT).show();
                    Toast("Time length must not equal or more than 3");
                    return;
                }

                if(x.equals("0") || x.equals("00")){
                    traineeStart.setText("00");
                    Toasty.error(getApplicationContext(),"Timein hour should not equal to zero",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(y.equals("0") || y.equals("00")){
                    traineeEnd.setText("00");
                    Toasty.error(getApplicationContext(),"Timeout hour should not equal to zero",Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!(x.matches(regexNum))) {
                    traineeStart.setError("Positive integer only, special characters or string are not tolerated");
                    return;
                }

                if (!(y.matches(regexNum))) {
                    traineeEnd.setError("Positive integer only, special characters or string are not tolerated");
                    return;
                }

                if (timeInMinute.length() == 0 || timeOutMinute.length() == 0) {
                    tStartMinute.setText("00");
                    tEndMinute.setText("00");
                    Toasty.success(getApplicationContext(),"Time minute automatic set to zero",Toast.LENGTH_SHORT).show();
                    return;
                }

                startTime = Integer.parseInt(x);
                endTime = Integer.parseInt(y);
                startMinute = Integer.parseInt(timeInMinute);
                endMinute = Integer.parseInt(timeOutMinute);

                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        new SweetAlertDialog(TraineeTimeList.this, SweetAlertDialog.CUSTOM_IMAGE_TYPE)
                                .setCustomImage(R.drawable.notepad_xml)
                                .setTitleText("Time Update")
                                .setContentText("Do you want to update the time of this trainee?")
                                .setConfirmText(" Yes ")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(final SweetAlertDialog sDialog) {
                                        sDialog.dismiss();
                                        submitBtn.setTextColor(Color.GRAY);
                                        submitBtn.setClickable(false);
                                        submitBtn.setEnabled(false);
                                        new Handler().post(new Runnable() {
                                            @Override
                                            public void run() {
                                                sDialog.dismiss();
                                            }
                                        });
                                        if (startMinute > 60) {
                                            startMinute = 0;
                                            Toast("Time-in minute should not greater than 60");
                                            return;
                                        }else if(startMinute == 60){
                                            startMinute = 0;
                                            tStartMinute.setText(String.valueOf(startMinute));
                                            startTime +=1;
                                            traineeStart.setText(String.valueOf(startTime));
                                        }
                                        if (endMinute > 60) {
                                            endMinute = 0;
                                            Toast("Timeout miute should not greater than 60");
                                            return;
                                        }else if(endMinute == 60){
                                            endMinute = 0;
                                            tEndMinute.setText(String.valueOf(endMinute));
                                            endTime +=1;
                                            traineeEnd.setText(String.valueOf(endTime));
                                        }

                                        temporary = startMinute + endMinute;
                                        if (tIn == "AM" && tOut == "AM") {
                                            if (startTime == endTime) {
                                                if (temporary >= 60) {
                                                    temp_minute = temporary-60;
                                                }
                                                temp_minute = temporary;
                                                elapseTime = ((12+endTime )-( startTime));
                                            }
                                            if (startTime > endTime) {
                                                if (temporary >= 60) {
                                                    temp_minute = temporary-60;
                                                }
                                                temp_minute = temporary;
                                                elapseTime = (12+endTime )-( startTime);
                                            }
                                            if(startTime < endTime && endTime != 12){
                                                if (temporary >= 60) {
                                                    temp_minute = temporary-60;
                                                }
                                                temp_minute = temporary;
                                                elapseTime = (endTime - startTime);
                                            }
                                            if(startTime < endTime && endTime == 12){
                                                if (temporary >= 60) {
                                                    temp_minute = temporary-60;
                                                }
                                                temp_minute = temporary;
                                                elapseTime = (12+endTime )-( startTime);
                                            }
                                        }

                                        else if (tIn == "PM" && tOut == "AM") {
                                            if(startTime == endTime){
                                                if (temporary >= 60) {
                                                    temp_minute = temporary - 60;
                                                }
                                                temp_minute = temporary;
                                                elapseTime = ((12+endTime )-( startTime));
                                            }
                                            if(startTime > endTime && startTime == 12){
                                                if (temporary >= 60) {
                                                    temp_minute = temporary - 60;
                                                }
                                                temp_minute = temporary;
                                                elapseTime = ((24+endTime )-( startTime));
                                            }
                                            if(startTime < endTime && endTime == 12){
                                                if (temporary >= 60) {
                                                    temp_minute = temporary - 60;
                                                }else{
                                                    temp_minute = temporary;
                                                    elapseTime = ((endTime )- (startTime));
                                                }
                                            }
                                            if (startTime < endTime && endTime !=12) {
                                                if (temporary >= 60) {
                                                    temp_minute = temporary - 60;
                                                }
                                                temp_minute = temporary;
                                                elapseTime = ((12+endTime )- (startTime));
                                            }

                                            if (startTime > endTime && startTime !=12) {
                                                if (temporary >= 60) {
                                                    elapseTime = ((12 + endTime) - (startTime));
                                                    temp_minute = temporary - 60;
                                                }
                                                temp_minute = temporary;
                                                elapseTime = ((12 + endTime) - (startTime));
                                            }
                                        }
                                        else if (tIn == "AM" && tOut == "PM") {
                                            if(startTime == endTime){
                                                if (temporary >= 60) {
                                                    temp_minute = temporary - 60;
                                                }
                                                temp_minute = temporary;
                                                elapseTime = ((12+endTime )-( startTime));
                                            }
                                            if(startTime > endTime && startTime == 12){
                                                if (temporary >= 60) {
                                                    temp_minute = temporary - 60;
                                                }
                                                temp_minute = temporary;
                                                elapseTime = ((24+endTime )-( startTime));
                                            }
                                            if(startTime < endTime && endTime == 12){
                                                if (temporary >= 60) {
                                                    temp_minute = temporary - 60;
                                                }else{
                                                    temp_minute = temporary;
                                                    elapseTime = ((endTime )- (startTime));
                                                }
                                            }
                                            if (startTime < endTime && endTime !=12) {
                                                if (temporary >= 60) {
                                                    temp_minute = temporary - 60;
                                                }
                                                temp_minute = temporary;
                                                elapseTime = ((12+endTime )- (startTime));
                                            }

                                            if (startTime > endTime && startTime !=12) {
                                                if (temporary >= 60) {
                                                    elapseTime = ((12 + endTime) - (startTime));
                                                    temp_minute = temporary - 60;
                                                }
                                                temp_minute = temporary;
                                                elapseTime = ((12 + endTime) - (startTime));
                                            }
                                        }else if(tIn == "PM" && tOut == "PM"){
                                            if (startTime == endTime) {
                                                if (temporary >= 60) {
                                                    temp_minute = temporary-60;
                                                }
                                                temp_minute = temporary;
                                                elapseTime = ((12+endTime )-( startTime));
                                            }
                                            if (startTime > endTime) {
                                                if (temporary >= 60) {
                                                    temp_minute = temporary-60;
                                                }
                                                temp_minute = temporary;
                                                elapseTime = (12+endTime )-( startTime);
                                            }
                                            if(startTime < endTime && endTime != 12){
                                                if (temporary >= 60) {
                                                    temp_minute = temporary-60;
                                                }
                                                temp_minute = temporary;
                                                elapseTime = (endTime - startTime);
                                            }
                                            if(startTime < endTime && endTime == 12){
                                                if (temporary >= 60) {
                                                    temp_minute = temporary-60;
                                                }
                                                temp_minute = temporary;
                                                elapseTime = (12+endTime )-( startTime);
                                            }
                                        }
                                        Cursor cursor = dbHelper.getDataId(String.valueOf(name));
                                        if (cursor.moveToFirst()) {
                                            do {
                                                timeremaining = cursor.getInt(7);
                                                test = cursor.getInt(15);
                                                if (timeremaining <= 0 || timeremaining <= -1) {
                                                    Toast("Trainee task complete");
                                                    timeremaining = -1;
                                                    break;
                                                }
                                            } while (cursor.moveToNext());
                                        }
                                        totalTime = timeremaining;
                                        cursor.close();
                                        dbHelper.close();
                                        int copyTotalTime = totalTime;
                                        elapse = elapseTime;
                                        timeremaining = timeremaining - elapse;
                                        totalTime = timeremaining;
                                        if ( totalTime <= 0 ||  timeremaining <=0) {
                                            temp_minute = 0;
                                            temp_test = 0;
                                            totalTime = 0;
                                            start_m =0;
                                            temporary = 0;
                                            tStatus.setText("Complete");
                                            tStatus.setTextColor(Color.rgb(0, 80, 0));
                                            traineeStart.setEnabled(false);
                                            traineeEnd.setEnabled(false);
                                            submitBtn.setClickable(false);
                                            submitBtn.setEnabled(false);
                                        }
                                        if(test > 60){
                                            test = test-60;
                                            elapse+=1;
                                            totalTime-=1;
                                        }else if(test == 60){
                                            test = 0;
                                            elapse+=1;
                                            totalTime-=1;
                                        }

                                        temp_minute = temp_minute + test;
                                        int update_temp_min = temp_minute;

                                        if(totalTime<=0){
                                            update_temp_min = 0;
                                        }
                                        if(update_temp_min >= 60 ){
                                            update_temp_min = update_temp_min - 60;
                                            if(update_temp_min<=0){
                                                update_temp_min = 0;
                                            }
                                        }

                                        int result = dbHelper.updateTime(Integer.parseInt(id), new Trainee(totalTime,update_temp_min));
                                        time_remaining_minute.setText(String.valueOf(update_temp_min));
                                        if (result > 0) {
                                            new SweetAlertDialog(TraineeTimeList.this, SweetAlertDialog.SUCCESS_TYPE)
                                                    .setTitleText("Success!")
                                                    .setContentText(name+" time successfully updated")
                                                    .show();
                                            Toasty.success(getApplicationContext(),name+" time sucessfully updated, elapse time: " +
                                                    elapse+" hr. : "+temporary+" min.",Toast.LENGTH_LONG).show();
                                            tRemaining.setTextColor(Color.rgb(0, 80, 0));
                                            tRemaining.setText(String.valueOf(totalTime));
                                            thistime.setTextColor(Color.rgb(80, 0, 0));
                                            thistime.setText(String.valueOf(newTime));

                                            if(totalTime == 0 || totalTime <=0){
                                                boolean log = dbHelper.addLog(new dbLog("Trainee time update, name: " + name + ", status: Complete!"));
                                            }else{
                                                boolean log = dbHelper.addLog(new dbLog("Trainee time update, name: " + name + ", elapse time: " + elapseTime + " hr: "+temporary+" min" +
                                                        ", time remaining: " + timeremaining + " hr., status: " + String.valueOf(myStatus)));
                                            }
                                            dbHelper.close();
                                        } else {
                                            Toast("Data not updated");
                                            submitBtn.setClickable(true);
                                            submitBtn.setEnabled(true);
                                        }

                                        time_remaining_minute.setText(String.valueOf(update_temp_min));
                                        thistime.setText(String.valueOf(copyTotalTime));
                                        startTime =0;
                                        endTime =0;
                                        elapseTime = 0;
                                        totalTime = 0;
                                        timeremaining = 0;
                                        newTime = 0;
                                        startMinute = 0;
                                        endMinute = 0;
                                        temp_minute = 0;
                                        test = 0;
                                        temp_test = 0;
                                        submitBtn.setTextColor(Color.GRAY);
                                        submitBtn.setClickable(false);
                                        submitBtn.setEnabled(false);
                                    }
                                })
                                .setCancelText(" No ")
                                .showCancelButton(true)
                                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        sDialog.cancel();
                                    }
                                })
                                .show();
                    }
                });//eof handler
            }
        });
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
                            sDialog.dismiss();
                        }
                    })
                    .show();
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        dbHelper.close();
        typeWriterView.setWithMusic(false);
        typeWriterView.removeAnimation();
        Bungee.slideDown(TraineeTimeList.this);
        finish();
    }

    public void Toast(final String s) {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                Toasty.info(getApplicationContext(), s, Toast.LENGTH_LONG).show();
            }
        });
    }
}
