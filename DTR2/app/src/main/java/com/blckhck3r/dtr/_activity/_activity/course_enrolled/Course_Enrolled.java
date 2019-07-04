package com.blckhck3r.dtr._activity._activity.course_enrolled;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.blckhck3r.dtr.R;
import com.blckhck3r.dtr._activity._database.DatabaseHelper;
import com.blckhck3r.dtr._activity._misc.Trainee;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;
import es.dmoral.toasty.Toasty;
import spencerstudios.com.bungeelib.Bungee;

public class Course_Enrolled extends AppCompatActivity {

    ListView lv;
    DatabaseHelper dbHelper;
    ArrayList<Trainee> listData;
    SearchView sv;
    CustomAdapter adapter;
    TextView courseName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_enrolled);
        lv = (ListView) findViewById(R.id.lv);
        sv = (SearchView) findViewById(R.id.sv);
        Course_Enrolled.this.setTitle("C:/> Course Enrollee");
        courseName = (TextView) findViewById(R.id.courseName);
        dbHelper = new DatabaseHelper(getApplicationContext());
        listData = new ArrayList<>();
        populateView();
    }


    int text_list = 0;
    @Override
    protected void onStart() {
        super.onStart();
        listData.clear();
        populateView();
        if(text_list == 0){
            new SweetAlertDialog(this, SweetAlertDialog.CUSTOM_IMAGE_TYPE)
                    .setTitleText("No data found!")
                    .setContentText("There is no enrollee in this course")
                    .setCustomImage(R.drawable.ic_mindweb)
                    .show();
        }else{
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    Toasty.info(getApplicationContext(),"Number of trainees: "+text_list,Toast.LENGTH_SHORT).show();
                }
            });
        }

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
                    .setContentText("This application is for project purposes only \n© MindWeb IT eSolutions\n" +
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

    public void Toast(final String s){
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                Toasty.normal(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void populateView(){
        Intent intent = getIntent();
        int Id = intent.getIntExtra("_id",-1);
        String course = intent.getStringExtra("Add_Course");
        String courseDesc = intent.getStringExtra("add_description");
        int HrLimit = intent.getIntExtra("time_limit",0);
        int traineeLimit = intent.getIntExtra("course_limit",0);

        courseName.setText(course);

        Cursor cursor = dbHelper.getEnrolled(course);
        if(cursor.moveToFirst()){
            do{
                String name = cursor.getString(1);
                String time = cursor.getString(6);
                listData.add(new Trainee(name,time));

            }while(cursor.moveToNext());
        }
        cursor.close();
        adapter = new CustomAdapter(this,R.layout.custom_activity,listData);
        lv.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    public class CustomAdapter extends BaseAdapter {
        private Context context;
        private int layout;
        ArrayList<Trainee> textList;
        public CustomAdapter(Context context, int layout, ArrayList<Trainee> textList) {
            this.context = context;
            this.layout = layout;
            this.textList = textList;
        }

        @Override
        public int getCount() {
            text_list = textList.size();
            return textList.size();
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
                row =inflater.inflate(layout,null);
                holder.name = row.findViewById(R.id.txtName);
                row.setTag(holder);
            }else{
                holder = (CustomAdapter.ViewHolder) row.getTag();
            }
            final Trainee trainee = textList.get(i);
            holder.name.setText(trainee.getName());

            return row;
        }

        public class ViewHolder{
            TextView name;

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        dbHelper.close();
        Bungee.slideDown(Course_Enrolled.this);
        finish();
    }
}