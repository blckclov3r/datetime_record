package com.blckhck3r.dtr._activity._activity.main_activity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.blckhck3r.dtr.R;
import com.blckhck3r.dtr._activity._database.databaseHelper;
import com.blckhck3r.dtr._activity._fragments.about_fragment;
import com.blckhck3r.dtr._activity._fragments.add_course;
import com.blckhck3r.dtr._activity._fragments.add_fragment;
import com.blckhck3r.dtr._activity._fragments.course_frag;
import com.blckhck3r.dtr._activity._fragments.home_fragment;
import com.blckhck3r.dtr._activity._fragments.info_frag;

import java.io.File;

import cn.pedant.SweetAlert.SweetAlertDialog;
import es.dmoral.toasty.Toasty;
import spencerstudios.com.bungeelib.Bungee;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    public static Activity fa;
    databaseHelper dbHelper;
    SQLiteDatabase db;
//    MediaPlayer mymusic;
    boolean doubleBackToExitPressedOnce = false;
    private long mLastClickTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbHelper = new databaseHelper(this);
        db = dbHelper.getWritableDatabase();
        fa = this;
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);

        if (savedInstanceState == null) {
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    getSupportFragmentManager().beginTransaction().setAllowOptimization(true)
                            .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                            .replace(R.id.fragment_container, new home_fragment())
                            .commit();
                }
            });
            navigationView.setCheckedItem(R.id.nav_home);
        }
//        setMymusic();
    }


    public void Toast(final String s) {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                Toasty.info(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
            }
        });
    }

//    public void setMymusic() {
//        new Handler().post(new Runnable() {
//            @Override
//            public void run() {
//                mymusic = MediaPlayer.create(MainActivity.this, R.raw.chipper);
//                mymusic.start();
//                mymusic.setLooping(true);
//            }
//        });
//    }

    @Override
    public void onBackPressed() {
        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (doubleBackToExitPressedOnce) {
//            mymusic.release();
            super.onBackPressed();
            Bungee.diagonal(MainActivity.this);
        }
        this.doubleBackToExitPressedOnce = true;
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                Toasty.info(MainActivity.this, "Click back again, to exit", Toast.LENGTH_SHORT).show();
            }
        });
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    drawer.closeDrawer(GravityCompat.START);
                }
            });
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        dbHelper.close();
//        mymusic.release();
    }


    @Override
    protected void onStart() {
        super.onStart();
        dbHelper.close();
    }


    public void sweet_popup() {
        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
            return;
        }
        mLastClickTime = SystemClock.elapsedRealtime();
        new SweetAlertDialog(this, SweetAlertDialog.CUSTOM_IMAGE_TYPE)
                .setTitleText("Are you sure?")
                .setCustomImage(R.drawable.logout)
                .setContentText("Do you want to exit this app?")
                .setConfirmText(" Yes ")
                .setCancelText(" No ")
                .showCancelButton(true)
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.cancel();
                        return;
                    }
                })
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismissWithAnimation();
                        Toasty.info(getApplicationContext(), "" +
                                "This program created by: Aljun Abrenica " +
                                "\nSource code: bitbucket.com/blckhck3r", Toast.LENGTH_LONG).show();
                        finish();
                        Bungee.diagonal(MainActivity.this);
                    }
                })
                .show();
    }


//    public void popupDialog(){
//        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setIcon(R.drawable.ic_power);
//        builder.setTitle("EXIT");
//        builder.setMessage("Do you want to close this application? ").setCancelable(false).setPositiveButton("Yes",
//                new DialogInterface.OnClickListener(){
//                    public void onClick(DialogInterface dialog,int id){
//                        new Handler().post(new Runnable() {
//                            @Override
//                            public void run() {
//                                Toasty.normal(getApplicationContext(),"" +
//                                        "This program created by: Aljun Abrenica " +
//                                        "\nSource code: bitbucket.com/blckhck3r",Toast.LENGTH_LONG).show();
//                                finish();
//                            }
//                        });
//                    }
//                }).setNegativeButton("No",new DialogInterface.OnClickListener(){
//            public void onClick(DialogInterface dialog,int n){
//                dialog.dismiss();
//            }
//        });
//        AlertDialog alert = builder.create();
//        alert.show();
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.exit_app) {
//            popupDialog();
            sweet_popup();
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

//        if (id == R.id.nav_camera) {
//            // Handle the camera action
//        } else if (id == R.id.nav_gallery) {
//
//        } else if (id == R.id.nav_slideshow) {
//
//        } else if (id == R.id.nav_manage) {
//
//        } else if (id == R.id.nav_share) {
//
//        } else if (id == R.id.nav_send) {
//
//        }
        switch (id) {
            case R.id.nav_add: {
                if (getSupportActionBar() != null) {
                    MainActivity.this.setTitle("C:/> Insert");
                }
                getSupportFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                        .replace(R.id.fragment_container, new add_fragment())
                        .commit();
            }
            break;

            case R.id.nav_about:

                if (getSupportActionBar() != null) {
                    MainActivity.this.setTitle("C:/> About");
                }

                getSupportFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                        .replace(R.id.fragment_container, new about_fragment())
                        .commit();

                break;
            case R.id.nav_home:

                if (getSupportActionBar() != null) {
                    MainActivity.this.setTitle("C:/> Home");
                }
                getSupportFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                        .replace(R.id.fragment_container, new home_fragment())
                        .commit();

                break;

//            case R.id.nav_traineeList:
//                new Handler().post(new Runnable() {
//                    @Override
//                    public void run() {
//                        finish();
//                        Intent intent = new Intent(MainActivity.this, AllTraineeList.class);
//                        startActivity(intent);
//                    }
//                });
//                break;

            case R.id.nav_tList: {

                if (getSupportActionBar() != null) {
                    MainActivity.this.setTitle("C:/> Trainees");
                }
                getSupportFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                        .replace(R.id.fragment_container, new info_frag())
                        .commit();
            }
            break;

            case R.id.nav_addCourse: {

                if (getSupportActionBar() != null) {
                    MainActivity.this.setTitle("C:/> Create");
                }
                getSupportFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                        .replace(R.id.fragment_container, new add_course())
                        .commit();
            }
            break;

            case R.id.nav_courseList:

                if (getSupportActionBar() != null) {
                    MainActivity.this.setTitle("C:/> Course");
                }
                getSupportFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                        .replace(R.id.fragment_container, new course_frag())
                        .commit();

                break;

            case R.id.nav_log_: {
                getSupportFragmentManager().beginTransaction().remove(new home_fragment()).commit();
                Intent intentLog = new Intent(MainActivity.this, Log_Activity.class);
                startActivity(intentLog);
                Bungee.slideUp(MainActivity.this);
            }
            break;

            case R.id.nav_send:{
//                mymusic.release();
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        ApplicationInfo app = getApplicationContext().getApplicationInfo();
                        String filePath = app.sourceDir;
                        Intent intent = new Intent(Intent.ACTION_SEND);
                        intent.setType("*/*");
                        intent.putExtra(Intent.EXTRA_STREAM,Uri.fromFile(new File(filePath)));
                        startActivity(Intent.createChooser(intent,"Share app via"));
                    }
                });
//                setMymusic();
            }
            Toast("Share it via?");
            break;

        }
//        if(id == R.id.nav_add){
//            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new add_fragment()).commit();
//        }else if(id==R.id.nav_view){
//            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new view_fragment()).commit();
//        }else if(id==R.id.nav_about){
//            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new about_fragment()).commit();
//        }else if(id==R.id.nav_home){
//            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new home_fragment()).commit();
//        }


//        drawer.closeDrawer(GravityCompat.START);
        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        new Handler().post(new Runnable() {
            public void run() {
                drawer.closeDrawer(GravityCompat.START);
            }
        });
        return true;
    }


    public void mindweb_qr(View view) {
        view.findViewById(R.id.btn_mindweb).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browse = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/mindwebesolutions"));
                startActivity(browse);
            }
        });
    }


    public void facebook(View view) {
        view.findViewById(R.id.fb).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toasty.info(getApplicationContext(), "facebook.com/blckclov3r", Toast.LENGTH_LONG).show();
                Intent browse = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/blckclov3r"));
                startActivity(browse);
            }
        });
    }

    public void messenger(View view) {
        view.findViewById(R.id.meseeenger).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browse = new Intent(Intent.ACTION_VIEW, Uri.parse("http://m.me/blckclov3r"));
                startActivity(browse);
            }
        });
    }

    public void git(View view) {
        view.findViewById(R.id.github).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toasty.info(getApplicationContext(), "Sooooon :D", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
