package com.blckhck3r.dtr._activity._activity.main_activity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;
import com.blckhck3r.dtr.R;
import com.blckhck3r.dtr._activity._fragments.About_Fragment;
import com.blckhck3r.dtr._activity._fragments.Add_Course;
import com.blckhck3r.dtr._activity._fragments.Add_Fragment;
import com.blckhck3r.dtr._activity._fragments.Course_Fragment;
import com.blckhck3r.dtr._activity._fragments.Home_Fragment;
import com.blckhck3r.dtr._activity._fragments.Info_Fragment;
import java.io.File;

import cn.pedant.SweetAlert.SweetAlertDialog;
import es.dmoral.toasty.Toasty;
import spencerstudios.com.bungeelib.Bungee;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    public static Activity fa;
    boolean doubleBackToExitPressedOnce = false;
    private Fragment mFragment = null;
    private long mLastClickTime = 0;
    private FragmentManager mFragmentManager;

    private static final String TAG = "MainActivity";
    private static final String COMMON_TAG = "mAppLog";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fa = this;
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);
        mFragmentManager = getSupportFragmentManager();
        mFragment = new Home_Fragment();
        if (savedInstanceState == null) {
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    mFragmentManager.beginTransaction().setAllowOptimization(true)
                            .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                            .replace(R.id.fragment_container, mFragment)
                            .commit();
                }
            });
            navigationView.setCheckedItem(R.id.nav_home);
        }
        Log.d(COMMON_TAG, TAG + " onCreate count: " + mFragmentManager.getBackStackEntryCount());
    }


    public void Toast(final String s) {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                Toasty.info(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public void onBackPressed() {
        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (doubleBackToExitPressedOnce) {
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
    }


    @Override
    protected void onStart() {
        super.onStart();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

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
        Log.d(COMMON_TAG, TAG + " count: " + mFragmentManager.getBackStackEntryCount());
        switch (id) {
            case R.id.nav_add: {
                if (mFragment != null) {
                    for (int i = 0; i < mFragmentManager.getBackStackEntryCount(); i++) {
                        mFragmentManager.popBackStackImmediate();
                    }
                    mFragment = new Add_Fragment();
                    if (getSupportActionBar() != null) {
                        MainActivity.this.setTitle("C:/> Insert");
                    }
                    mFragmentManager
                            .beginTransaction()
                            .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                            .replace(R.id.fragment_container, mFragment)
                            .commit();
                }
            }
            break;

            case R.id.nav_about:
                if (mFragment != null) {
                    mFragment = new About_Fragment();
                    if (getSupportActionBar() != null) {
                        MainActivity.this.setTitle("C:/> About");
                    }
                    mFragmentManager
                            .beginTransaction()
                            .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                            .replace(R.id.fragment_container, mFragment)
                            .commit();
                }
                break;
            case R.id.nav_home:
                if (mFragment != null) {
                    mFragment = new Home_Fragment();
                    if (getSupportActionBar() != null) {
                        MainActivity.this.setTitle("C:/> Home");
                    }
                    mFragmentManager
                            .beginTransaction()
                            .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                            .replace(R.id.fragment_container, mFragment)
                            .commit();
                }
                break;

            case R.id.nav_tList: {
                if (mFragment != null) {
                    mFragment = new Info_Fragment();
                    if (getSupportActionBar() != null) {
                        MainActivity.this.setTitle("C:/> Trainees");
                    }
                    mFragmentManager
                            .beginTransaction()
                            .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                            .replace(R.id.fragment_container, mFragment)
                            .commit();
                }

            }
            break;

            case R.id.nav_addCourse: {
                if (mFragment != null) {
                    mFragment = new Add_Course();
                    if (getSupportActionBar() != null) {
                        MainActivity.this.setTitle("C:/> Create");
                    }
                    mFragmentManager
                            .beginTransaction()
                            .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                            .replace(R.id.fragment_container, mFragment)
                            .commit();
                }
            }
            break;

            case R.id.nav_courseList:
                if (mFragment != null) {
                    mFragment = new Course_Fragment();
                    if (getSupportActionBar() != null) {
                        MainActivity.this.setTitle("C:/> Course");
                    }
                    mFragmentManager
                            .beginTransaction()
                            .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                            .replace(R.id.fragment_container, mFragment)
                            .commit();
                }
                break;

            case R.id.nav_log_: {
                Intent intentLog = new Intent(MainActivity.this, Log_Activity.class);
                startActivity(intentLog);
                Bungee.slideUp(MainActivity.this);
            }
            break;

            case R.id.nav_send: {
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        ApplicationInfo app = getApplicationContext().getApplicationInfo();
                        String filePath = app.sourceDir;
                        Intent intent = new Intent(Intent.ACTION_SEND);
                        intent.setType("*/*");
                        intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(filePath)));
                        startActivity(Intent.createChooser(intent, "Share app via"));
                    }
                });
            }
            Toast("Share it via?");
            break;

        }

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
                Intent browse = new Intent(Intent.ACTION_VIEW, Uri.parse("github.com/blckclov3r"));
                startActivity(browse);
            }
        });
    }
}
