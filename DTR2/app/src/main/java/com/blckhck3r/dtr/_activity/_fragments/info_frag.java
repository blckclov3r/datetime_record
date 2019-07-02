package com.blckhck3r.dtr._activity._fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.blckhck3r.dtr.R;
import com.blckhck3r.dtr._activity._activity.info_activity.InfoActivity;
import com.blckhck3r.dtr._activity._activity.time_activity.TraineeTimeList;
import com.blckhck3r.dtr._activity._activity.update_delete_activity.UpdateInfoActivity;
import com.blckhck3r.dtr._activity._database.databaseHelper;
import com.blckhck3r.dtr._activity._misc.Trainee;
import com.blckhck3r.dtr._activity._misc.dbLog;

import org.michaelbel.bottomsheet.BottomSheet;
import org.michaelbel.bottomsheet.Utils;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;
import es.dmoral.toasty.Toasty;
import spencerstudios.com.bungeelib.Bungee;

/**
 * Created by Abrenica, Aljun
 */

public class info_frag extends Fragment {
    databaseHelper dbHelper;
    ListView listView;
    ArrayList<Trainee> listData;
    CustomAdapter adapter;
    SearchView sv;
    private long mLastClickTime = 0;
    static String name = "";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.info_frag,container,false);
        dbHelper = new databaseHelper(getActivity());
        listData = new ArrayList<>();
        listData.clear();
        listView = (ListView) view.findViewById(R.id.listView);
        sv = (SearchView) view.findViewById(R.id.sv);
        sv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sv.setIconified(false);
            }
        });

        populateView();
        return view;
    }
    int tList = 0;
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listData.clear();
        getSearchName("");
        if(tList == 0){
            Toast("No data found");
        }else{
            new Handler().post(new Runnable() {
                @Override
                public void run() {
//                    new SweetAlertDialog(getActivity(), SweetAlertDialog.SUCCESS_TYPE)
//                            .setTitleText("Trainee Section")
//                            .setContentText("Tips\nLong press the items to show up the dialog" +
//                                    "\nSingle click to show trainee information")
//                            .show();
                    Toasty.info(getActivity(),"Tips: Long press the items to show up the dialog, " +
                            "Single click to show trainee information",Toast.LENGTH_SHORT).show();
                }
            });

        }
    }

    @Override
    public void onStart() {
        super.onStart();
        getSearchName("");
        listData.clear();
        populateView();
        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                getSearchName(newText);
//                Toast.makeText(getActivity(), "You are searching for . . .", Toast.LENGTH_SHORT).show();
                return false;
            }
        });
    }

    public void getSearchName(String searchTerm) {
        listData.clear();
        Trainee trainee;
        Cursor c = dbHelper.retrieve(searchTerm);
        int id;
        String name = "";
        if(c.moveToFirst()){
            do{
                id = c.getInt(0);
                name = c.getString(1);
                trainee = new Trainee();
                trainee.setTrainee_id(id);
                trainee.setName(name);
                listData.add(trainee);
            }while(c.moveToNext());
        }
        c.close();
        dbHelper.close();
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }


    public void populateView() {

        Cursor cursor = dbHelper.getData();
        if (cursor.moveToFirst()) {
            do {
                name = cursor.getString(1);
                listData.add(new Trainee(name));
                   } while (cursor.moveToNext());
               }
        cursor.close();
        dbHelper.close();
        adapter = new CustomAdapter(getActivity(), R.layout.custom_activity, listData);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();




    }


    private class CustomAdapter extends BaseAdapter {
        ArrayList<Trainee> textList;
        private Context context;
        private int layout;

        public CustomAdapter(Context context, int layout, ArrayList<Trainee> textList) {
            this.context = context;
            this.layout = layout;
            this.textList = textList;
        }


        @Override
        public int getCount() {
            tList = textList.size();
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
            if (row == null) {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                row = inflater.inflate(layout, null);
                holder.name = row.findViewById(R.id.txtName);

                row.setTag(holder);
            } else {
                holder = (CustomAdapter.ViewHolder) row.getTag();
            }
            final Trainee trainee = textList.get(i);
            holder.name.setText(trainee.getName());


            row.setOnClickListener(new AdapterView.OnClickListener() {
                int idList = -1;
                String nameList = "";
                String courseList = "";
                String emailList = "";
                String contactList = "";
                String addressList = "";
                String timestampList = "";
                int timeremaining = 0;

                String sched = "";
                int hStart = 0;
                int hEnd = 0;
                int mStart = 0;
                int mEnd = 0;
                String t1 = "";
                String t2 = "";

                int main_time_minute = 0;

                @Override
                public void onClick(View view) {
                    if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                        return;
                    }
                    mLastClickTime = SystemClock.elapsedRealtime();
                    Cursor cursor = dbHelper.getDataId(trainee.getName());
                    if (cursor.moveToFirst()) {
                        do {
                            idList = cursor.getInt(0);
                            nameList = cursor.getString(1);
                            courseList = cursor.getString(2);
                            emailList = cursor.getString(3);
                            contactList = cursor.getString(4);
                            addressList = cursor.getString(5);
                            timestampList = cursor.getString(6);
                            timeremaining = cursor.getInt(7);
                            sched = cursor.getString(8);
                            hStart = cursor.getInt(9);
                            hEnd = cursor.getInt(10);
                            mStart = cursor.getInt(11);
                            mEnd = cursor.getInt(12);
                            t1 = cursor.getString(13);
                            t2 = cursor.getString(14);
                             main_time_minute  = cursor.getInt(15);

                            Intent intent = new Intent(getActivity(), InfoActivity.class);
                            if(main_time_minute >=60){
                                main_time_minute = main_time_minute -60;
                            }
                            intent.putExtra("_id", idList);
                            intent.putExtra("name", nameList);
                            intent.putExtra("course", courseList);
                            intent.putExtra("email", emailList);
                            intent.putExtra("contact", contactList);
                            intent.putExtra("address", addressList);
                            intent.putExtra("timestamp", timestampList);
                            intent.putExtra("timeremaining", timeremaining);

                            intent.putExtra("day_sched",sched);
                            intent.putExtra("time_start",hStart);
                            intent.putExtra("time_end",hEnd);
                            intent.putExtra("start_minute",mStart);
                            intent.putExtra("end_minute",mEnd);
                            intent.putExtra("s_condition",t1);
                            intent.putExtra("e_condition",t2);
                            intent.putExtra("timeremaining_minute",main_time_minute);
                            startActivity(intent);
                            Bungee.slideRight(getActivity());
                        } while (cursor.moveToNext());
                    }

                    cursor.close();
                    dbHelper.close();
//

                }
            });

            row.setOnLongClickListener(new AdapterView.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    BottomSheet.Builder builder = new BottomSheet.Builder(getActivity());
                    builder.setTitle("C:/> Name: "+trainee.getName()+"");
                    builder.setContentType(BottomSheet.GRID);
                    builder.setTitleTextColorRes(R.color.colorPrimaryDark);
                    builder.setFullWidth(true);
                    builder.setCellHeight(Utils.dp(getActivity(), 48));
                    builder.setDividers(true);
                    builder.setTitleMultiline(true);
                    builder.setWindowDimming(80);
                    builder.setItemTextColor(getResources().getColor(R.color.colorPrimaryDark));
                    builder.setIconColor(getResources().getColor(R.color.white));
                    builder.setBackgroundColor(getResources().getColor(R.color.white));
                    builder.setMenu(R.menu.bottomsheet, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            int id = which;
                            switch (id){
                                case 0: {
                                    int idList = -1;
                                    String nameList = "";
                                    String courseList = "";
                                    String emailList = "";
                                    String contactList = "";
                                    String addressList = "";
                                    String timestampList = "";
                                    int timeremaining = 0;
                                    int mStart = 0;
                                    int mEnd = 0;
                                    String t1 = "";
                                    String t2 = "";
                                    int startTime = 0;
                                    int endTime = 0;
                                    int main_time_minute = 0;
                                    Cursor cursor = dbHelper.getDataId(trainee.getName());
                                    if (cursor.moveToFirst()) {
                                        do {
                                            idList = cursor.getInt(0);
                                            nameList = cursor.getString(1);
                                            courseList = cursor.getString(2);
                                            emailList = cursor.getString(3);
                                            contactList = cursor.getString(4);
                                            addressList = cursor.getString(5);
                                            timestampList = cursor.getString(6);
                                            timeremaining = cursor.getInt(7);
                                            startTime = cursor.getInt(9);
                                            endTime = cursor.getInt(10);
                                            mStart = cursor.getInt(11);
                                            mEnd = cursor.getInt(12);
                                            t1 = cursor.getString(13);
                                            t2 = cursor.getString(14);
                                            main_time_minute = cursor.getInt(15);
                                            Intent intent = new Intent(getActivity(), TraineeTimeList.class);
                                            if (main_time_minute >= 60) {
                                                main_time_minute = main_time_minute - 60;
                                            }
                                            intent.putExtra("_id", idList);
                                            intent.putExtra("name", nameList);
                                            intent.putExtra("course", courseList);
                                            intent.putExtra("email", emailList);
                                            intent.putExtra("contact", contactList);
                                            intent.putExtra("address", addressList);
                                            intent.putExtra("timestamp", timestampList);
                                            intent.putExtra("timeremaining", timeremaining);
                                            intent.putExtra("time_start", startTime);
                                            intent.putExtra("time_end", endTime);
                                            intent.putExtra("start_minute", mStart);
                                            intent.putExtra("end_minute", mEnd);
                                            intent.putExtra("s_condition", t1);
                                            intent.putExtra("e_condition", t2);
                                            intent.putExtra("timeremaining_minute", main_time_minute);
                                            startActivity(intent);
                                            Bungee.slideUp(getActivity());
                                        } while (cursor.moveToNext());
                                    }
                                    cursor.close();
                                    dbHelper.close();
                                } break;
                                case 1: {
                                    int idList = -1;
                                    String nameList = "";
                                    String courseList = "";
                                    String emailList = "";
                                    String contactList = "";
                                    String addressList = "";
                                    String timestampList = "";
                                    int timeremaining = 0;
                                    String sched = "";
                                    int hStart = 0;
                                    int hEnd = 0;
                                    int mStart = 0;
                                    int mEnd = 0;
                                    String t1 = "";
                                    String t2 = "";
                                    Cursor cursor = dbHelper.getDataId(trainee.getName());
                                    if (cursor.moveToFirst()) {
                                        do {
                                            idList = cursor.getInt(0);
                                            nameList = cursor.getString(1);
                                            courseList = cursor.getString(2);
                                            emailList = cursor.getString(3);
                                            contactList = cursor.getString(4);
                                            addressList = cursor.getString(5);
                                            timestampList = cursor.getString(6);
                                            timeremaining = cursor.getInt(7);
                                            sched = cursor.getString(8);
                                            hStart = cursor.getInt(9);
                                            hEnd = cursor.getInt(10);
                                            mStart = cursor.getInt(11);
                                            mEnd = cursor.getInt(12);
                                            t1 = cursor.getString(13);
                                            t2 = cursor.getString(14);
                                            Intent intent = new Intent(getActivity(), UpdateInfoActivity.class);
                                            intent.putExtra("_id", idList);
                                            intent.putExtra("name", nameList);
                                            intent.putExtra("course", courseList);
                                            intent.putExtra("email", emailList);
                                            intent.putExtra("contact", contactList);
                                            intent.putExtra("address", addressList);
                                            intent.putExtra("timestamp", timestampList);
                                            intent.putExtra("timeremaining", timeremaining);
                                            intent.putExtra("day_sched", sched);
                                            intent.putExtra("time_start", hStart);
                                            intent.putExtra("time_end", hEnd);
                                            intent.putExtra("start_minute", mStart);
                                            intent.putExtra("end_minute", mEnd);
                                            intent.putExtra("s_condition", t1);
                                            intent.putExtra("e_condition", t2);
                                            startActivity(intent);
                                            Bungee.slideUp(getActivity());
                                        } while (cursor.moveToNext());
                                    }
                                    cursor.close();
                                    dbHelper.close();
                                } break;
                                case 2: {
                                    if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                                        return;
                                    }
                                    mLastClickTime = SystemClock.elapsedRealtime();
                                    final String tName = trainee.getName();
                                    new SweetAlertDialog(getActivity(), SweetAlertDialog.CUSTOM_IMAGE_TYPE)
                                            .setCustomImage(R.drawable.delete)
                                            .setTitleText("Delete")
                                            .setContentText("Do you want to delete this trainee?, name: " + trainee.getName())
                                            .setConfirmText(" Yes ")
                                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                @Override
                                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                    int result = dbHelper.deleteData(trainee.getName());
                                                    sweetAlertDialog.dismiss();
                                                    if (result > 0) {
                                                        Toasty.success(getActivity(),"Successfully deleted",Toast.LENGTH_SHORT).show();
                                                        getSearchName("");
                                                        listData.clear();
                                                        populateView();
                                                        CustomAdapter adapter = new CustomAdapter(getActivity(), R.layout.custom_activity, listData);
                                                        listView.setAdapter(adapter);
                                                        adapter.notifyDataSetChanged();
                                                        new SweetAlertDialog(getActivity(), SweetAlertDialog.SUCCESS_TYPE)
                                                                .setTitleText("Delete")
                                                                .setContentText("Trainee successfully deleted")
                                                                .show();
                                                        boolean zxcv = dbHelper.addLog(new dbLog("Trainee successfully deleted, name: " + tName));
                                                        dbHelper.close();
//                                                Toast.makeText(getActivity(), "Trainee Successfully deleted", Toast.LENGTH_SHORT).show();
                                                    } else {
                                                        new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE)
                                                                .setTitleText("Oops...")
                                                                .setContentText("Something went wrong!")
                                                                .show();
                                                        boolean zxcv = dbHelper.addLog(new dbLog("Trainee not delete, name: " + tName));
                                                        dbHelper.close();
//                                                Toast.makeText(getActivity(), "Trainee not delete", Toast.LENGTH_SHORT).show();
                                                    }
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
                                    break;
                            }
                        }
                    });
                    builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            dbHelper.close();
                            return;
                        }
                    });
                    builder.show();
                    return true;
                }
            });

            return row;
        }
        public  class ViewHolder {
            TextView name;

        }
    }

    public void Toast(final String s){
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                Toasty.info(getActivity(), s, Toast.LENGTH_SHORT).show();
            }
        });

    }

}
