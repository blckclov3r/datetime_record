package com.blckhck3r.dtr._activity._activity.main_activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.blckhck3r.dtr.R;
import com.blckhck3r.dtr._activity._database.DatabaseHelper;
import com.blckhck3r.dtr._activity._misc.DbLog;
import java.util.ArrayList;
import cn.pedant.SweetAlert.SweetAlertDialog;
import es.dmoral.toasty.Toasty;
import spencerstudios.com.bungeelib.Bungee;


public class Log_Activity extends AppCompatActivity {

    ListView lv;
    ArrayList<DbLog> listData;
    CustomAdapter adapter;
    DatabaseHelper dbHelper;
    int text_list = 0;
    private long mLastClickTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_);
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
        Log_Activity.this.setTitle("C:/> Log | Record");
        dbHelper = new DatabaseHelper(getApplicationContext());
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);

        lv = (ListView) findViewById(R.id.lv);
        listData = new ArrayList<>();

        listData.clear();
        populateView();

        if (text_list == 0) {
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    Toast("No data found");
                }
            });
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        dbHelper.close();
    }




    @Override
    protected void onStart() {
        super.onStart();
        MainActivity.fa.finish();
        listData.clear();
        populateView();
    }

    public void Toast(final String toast) {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                Toasty.info(getApplicationContext(), toast, Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.delete_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.deleteLog) {
            sweetPopup();
        }
        listData.clear();
        populateView();
        return super.onOptionsItemSelected(item);
    }

    public void sweetPopup() {
        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
            return;
        }
        mLastClickTime = SystemClock.elapsedRealtime();
        new SweetAlertDialog(this, SweetAlertDialog.CUSTOM_IMAGE_TYPE)
                .setCustomImage(R.drawable.delete)
                .setTitleText("Are you sure?")
                .setContentText("Won't be able to recover the log file!")
                .setConfirmText(" Yes ")
                .setCancelText(" No ")
                .showCancelButton(true)
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.cancel();
                    }
                })
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(final SweetAlertDialog sDialog) {
                        if (text_list == 0) {
                            new Handler().post(new Runnable() {
                                @Override
                                public void run() {
                                    if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                                        return;
                                    }
                                    mLastClickTime = SystemClock.elapsedRealtime();
                                    Toasty.error(getApplicationContext(), "Log is already empty", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                            });
                        } else {
                            sDialog.setTitleText("Deleted!")
                                    .setContentText("Log file has been deleted!")
                                    .setConfirmText("OK")
                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                                            sDialog.dismissWithAnimation();
                                        }
                                    })
                                    .setCancelClickListener(null)
                                    .showCancelButton(false)
                                    .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);

                            new Handler().post(new Runnable() {
                                @Override
                                public void run() {
                                    Toasty.success(getApplicationContext(), "Log successfully cleared", Toast.LENGTH_SHORT).show();
                                    dbHelper.deleteAllLog();
                                    listData.clear();
                                    populateView();
                                    dbHelper.close();
                                }
                            });
                        }
                    }
                })
                .show();
    }

    public void popupDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.drawable.ic_delete_forever_black);
        builder.setTitle("CLEAR LOG");
        builder.setMessage("Are you sure to permanent clear all log? ").setCancelable(false).setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dbHelper.deleteAllLog();
                        listData.clear();
                        populateView();
                        dbHelper.close();
                        Toast.makeText(getApplicationContext(), "Log Cleared", Toast.LENGTH_SHORT).show();
                    }
                }).setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int n) {
                dialog.dismiss();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }


    public void populateView() {
        Cursor cursor = dbHelper.getLogData();
        if (cursor.moveToFirst()) {
            do {
                String name = cursor.getString(1);
                String time = cursor.getString(2);
                listData.add(new DbLog(name, time));

            } while (cursor.moveToNext());
        }
        adapter = new CustomAdapter(getApplicationContext(), R.layout.log_custom_activity, listData);
        lv.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        cursor.close();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(Log_Activity.this, MainActivity.class);
        startActivity(intent);
        Bungee.slideDown(Log_Activity.this);
        finish();
    }

    private class CustomAdapter extends BaseAdapter {
        ArrayList<DbLog> textList;
        private Context context;
        private int layout;

        public CustomAdapter(Context context, int layout, ArrayList<DbLog> textList) {
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
            if (row == null) {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                row = inflater.inflate(layout, null);
                holder.name = row.findViewById(R.id.txtName);
                holder.timestamp = row.findViewById(R.id.txtTime);
                row.setTag(holder);
            } else {
                holder = (CustomAdapter.ViewHolder) row.getTag();
            }
            final DbLog log = textList.get(i);
            holder.name.setText(log.getMessage());
            holder.timestamp.setText(log.getTimestamp());
            return row;

        }

        public class ViewHolder {
            TextView name;
            TextView timestamp;
        }

    }
}
