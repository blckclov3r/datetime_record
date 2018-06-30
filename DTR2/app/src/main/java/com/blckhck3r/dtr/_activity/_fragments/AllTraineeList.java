package com.blckhck3r.dtr._activity._fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.WindowManager;

import com.blckhck3r.dtr.R;
import com.blckhck3r.dtr._activity._activity.main_activity.Log_Activity;
import com.blckhck3r.dtr._activity._activity.main_activity.MainActivity;
import com.blckhck3r.dtr._activity._activity.main_activity.SectionPageAdapter;

public class AllTraineeList extends AppCompatActivity {

    private static final String TAG = "AllTraineeList";
    public ViewPager mViewPager;

    public AllTraineeList(){

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
        setContentView(R.layout.activity_all_trainee_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        AllTraineeList.this.setTitle("Trainee options");

    }

    @Override
    protected void onStart() {
        super.onStart();
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(mViewPager);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        tabLayout.setFillViewport(true);
        tabLayout.getMeasuredWidth();
        tabLayout.getTabMode();
        tabLayout.setupWithViewPager(mViewPager);
    }

    public void setupViewPager(ViewPager viewPager){
        SectionPageAdapter adapter = new SectionPageAdapter(getSupportFragmentManager());
        adapter.addFragment(new info_frag(),"Trainee List");
        adapter.addFragment(new course_frag() ,"Course List");
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        Intent intent = new Intent(AllTraineeList.this, MainActivity.class);
        startActivity(intent);

    }


}
