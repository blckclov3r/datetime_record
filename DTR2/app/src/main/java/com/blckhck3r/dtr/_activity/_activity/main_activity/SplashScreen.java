package com.blckhck3r.dtr._activity._activity.main_activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.widget.LinearLayout;

import com.blckhck3r.dtr.R;
import com.fujiyuu75.sequent.Animation;
import com.fujiyuu75.sequent.Sequent;

import in.codeshuffle.typewriterview.TypeWriterView;
import spencerstudios.com.bungeelib.Bungee;

public class SplashScreen extends AppCompatActivity {
    public static Activity sp;
    LinearLayout layout;
    LinearLayout sec;
    LinearLayout logo;
    LinearLayout links;
    private Intent intent;
    private TypeWriterView typeWriterView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash_screen);
        intent = new Intent(getApplicationContext(), MainActivity.class);
        typeWriterView = (TypeWriterView) findViewById(R.id.typeWriterView);
        layout = (LinearLayout) findViewById(R.id.helloworld);
        sec = (LinearLayout) findViewById(R.id.sec);
        logo = (LinearLayout) findViewById(R.id.aljunlogo);
        links = (LinearLayout) findViewById(R.id.aljun_links);
        typeWriterView.animateText("MindWeb");
        typeWriterView.setDelay(50);
        typeWriterView.setWithMusic(true);


        Sequent.origin(sec).anim(SplashScreen.this, Animation.FADE_IN_LEFT).duration(555).
                delay(800).start();
        Sequent.origin(logo).anim(SplashScreen.this, Animation.BOUNCE_IN).duration(1000).
                delay(1400).start();
        Sequent.origin(layout).anim(SplashScreen.this, Animation.FADE_IN_UP).duration(1000).
                delay(2400).start();
        Sequent.origin(links).anim(SplashScreen.this, Animation.FADE_IN_RIGHT).duration(1000).
                delay(2100).start();


        new Handler().post(new Runnable() {
            @Override
            public void run() {
                final Thread myThread = new Thread() {
                    @Override
                    public void run() {
                        try {
                            sleep(2700 * 2);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } finally {
                            startActivity(intent);
                            Bungee.diagonal(SplashScreen.this);
                            finish();

                        }
                    }
                };
                myThread.start();
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        typeWriterView.setWithMusic(false);
        typeWriterView.removeAnimation();
        finish();
    }
}