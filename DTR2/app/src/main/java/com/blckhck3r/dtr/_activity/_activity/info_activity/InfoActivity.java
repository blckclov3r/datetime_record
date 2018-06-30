package com.blckhck3r.dtr._activity._activity.info_activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.blckhck3r.dtr.R;
import com.blckhck3r.dtr._activity._activity.main_activity.MainActivity;
import com.fujiyuu75.sequent.Animation;
import com.fujiyuu75.sequent.Sequent;
import cn.pedant.SweetAlert.SweetAlertDialog;
import in.codeshuffle.typewriterview.TypeWriterView;
import spencerstudios.com.bungeelib.Bungee;


public class InfoActivity extends AppCompatActivity {
    TextView infoName,infoId,infoEmail,infoNumber,infoAddress,
            infoTimestamp,infoCourse,traineeTime,traineeStatus;
    TextView schedule,sH,mH,t1;
    TextView eH,eM,t2,timeremaining_minute;
    TypeWriterView typeWriterView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        InfoActivity.this.setTitle("C:/> Information");
        typeWriterView=(TypeWriterView)findViewById(R.id.title);
        final LinearLayout layout = (LinearLayout) findViewById(R.id.status);
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                typeWriterView.animateText("Trainee Info.");
                typeWriterView.setDelay(50);
            }
        });
        typeWriterView.removeAnimation();
        typeWriterView.setWithMusic(false);
        Sequent.origin(layout).anim(InfoActivity.this, Animation.BOUNCE_IN).delay(200).
                start();
        infoName = (TextView) findViewById(R.id.infoName);
        infoId = (TextView) findViewById(R.id.infoId);
        infoEmail = (TextView) findViewById(R.id.infoEmail);
        infoNumber = (TextView) findViewById(R.id.infoNumber);
        infoAddress = (TextView) findViewById(R.id.infoAddress);
        infoTimestamp = (TextView) findViewById(R.id.infoTimestamp);
        infoCourse = (TextView) findViewById(R.id.infoCourse);
        traineeTime = (TextView) findViewById(R.id.traineeTime);
        traineeStatus = (TextView) findViewById(R.id.traineeStatus);
        timeremaining_minute = (TextView) findViewById(R.id.timeremaining_minute);
        schedule = (TextView) findViewById(R.id.schedule);
        sH = (TextView) findViewById(R.id.sH);
        mH = (TextView) findViewById(R.id.mH);
        t1 = (TextView) findViewById(R.id.t1);
        eH = (TextView) findViewById(R.id.eH);
        eM = (TextView) findViewById(R.id.eM);
        t2 = (TextView) findViewById(R.id.t2);
    }
    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = getIntent();
        int Id = intent.getIntExtra("_id",-1);
        int timeremaing = intent.getIntExtra("timeremaining",0);
        String selectedId = String.valueOf(Id);
        String selectedName = intent.getStringExtra("name");
        String selectedCourse = intent.getStringExtra("course");
        String selectedEmail = intent.getStringExtra("email");
        String selectedContact = intent.getStringExtra("contact");
        String selectedAddress = intent.getStringExtra("address");
        String selectedTimestamp = intent.getStringExtra("timestamp");
        String selectedTraineeStatus =  String.valueOf(timeremaing);
        String sched = intent.getStringExtra("day_sched");
        int startHour = intent.getIntExtra("time_start",0);
        int endHour = intent.getIntExtra("time_end",0);
        int startMinute = intent.getIntExtra("start_minute",0);
        int endMinute = intent.getIntExtra("end_minute",0);
        String s_condition = intent.getStringExtra("s_condition");
        String e_condition = intent.getStringExtra("e_condition");
        int m_main = intent.getIntExtra("timeremaining_minute",0);
        int x = Integer.parseInt(String.valueOf(timeremaing));
        if(x == 0){
            traineeStatus.setText("Complete");
            new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
                    .setTitleText("Task Complete")
                    .setContentText("Complete!")
                    .show();
//            Toast.makeText(getApplicationContext(),"Task Complete",Toast.LENGTH_SHORT).show();
            traineeStatus.setTextColor(Color.rgb(0,80,0));
        }else{
            traineeStatus.setText("Incomplete");
//            Toast.makeText(getApplicationContext(),"Incomplete",Toast.LENGTH_SHORT).show();
            traineeStatus.setTextColor(Color.rgb(80,0,0));
        }
        schedule.setText(sched);
        sH.setText(String.valueOf(startHour));
        mH.setText(String.valueOf(startMinute));
        eH.setText(String.valueOf(endHour));
        eM.setText(String.valueOf(endMinute));
        t1.setText(s_condition);
        t2.setText(e_condition);
        infoId.setText(selectedId);
        infoName.setText(selectedName);
        infoCourse.setText(selectedCourse);
        infoEmail.setText(selectedEmail);
        infoNumber.setText(selectedContact);
        infoAddress.setText(selectedAddress);
        infoTimestamp.setText(selectedTimestamp);
        traineeTime.setText(selectedTraineeStatus);
        timeremaining_minute.setText(String.valueOf(m_main));
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
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        typeWriterView.removeAnimation();
        typeWriterView.setWithMusic(false);
        Bungee.slideLeft(InfoActivity.this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        typeWriterView.removeAnimation();
        typeWriterView.setWithMusic(false);
    }
}
