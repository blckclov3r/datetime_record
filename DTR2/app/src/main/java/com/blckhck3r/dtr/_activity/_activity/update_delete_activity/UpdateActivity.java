package com.blckhck3r.dtr._activity._activity.update_delete_activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
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
import com.blckhck3r.dtr._activity._activity.main_activity.MainActivity;
import com.blckhck3r.dtr._activity._database.databaseHelper;
import com.blckhck3r.dtr._activity._misc.Trainee;

import java.util.ArrayList;

/* Program © Acinerba Nujla Aljun Abrenica*/

public class UpdateActivity extends AppCompatActivity {
    databaseHelper dbHelper;
    ListView updateListView;
    ArrayList<Trainee> listData;
    SearchView sv;
    CustomAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        UpdateActivity.this.setTitle("DELETE / UPDATE");
        dbHelper = new databaseHelper(this);
        sv = (SearchView) findViewById(R.id.sv);

        updateListView = (ListView) findViewById(R.id.updateListView);
        listData = new ArrayList<>();
        listData.clear();
        getSearchName("");
    }

    @Override
    protected void onStart() {
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
                Toast.makeText(getApplicationContext(), "You are searching for . . .", Toast.LENGTH_SHORT).show();
                return false;
            }
        });
    }

    public void getSearchName(String searchTerm) {
        listData.clear();
        Trainee trainee;
        Cursor c = dbHelper.retrieve(searchTerm);
        int id;
        String name;
        if (c.moveToFirst()) {
            do {
                id = c.getInt(0);
                name = c.getString(1);
                trainee = new Trainee();
                trainee.setTrainee_id(id);
                trainee.setName(name);
                listData.add(trainee);
            } while (c.moveToNext());
        }

        dbHelper.close();
        updateListView.setAdapter(adapter);

    }

    public void populateView() {
        Cursor cursor = dbHelper.getData();
        if (cursor.moveToFirst()) {
            do {
                String name = cursor.getString(1);
                String time = cursor.getString(6);
                listData.add(new Trainee(name, time));

            } while (cursor.moveToNext());
        }
        adapter = new CustomAdapter(this, R.layout.custom_activity, listData);
        adapter.notifyDataSetChanged();
        updateListView.setAdapter(adapter);
        cursor.close();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        dbHelper.close();
        finish();
        Intent intent = new Intent(UpdateActivity.this, MainActivity.class);
        startActivity(intent);

    }

    public class CustomAdapter extends BaseAdapter {
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
            CustomAdapter.ViewHolder holder = new ViewHolder();
            if (row == null) {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                row = inflater.inflate(layout, null);
                holder.name = row.findViewById(R.id.txtName);
                row.setTag(holder);
            } else {
                holder = (ViewHolder) row.getTag();
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

                @Override
                public void onClick(View view) {
                    Cursor cursor = dbHelper.getDataId(trainee.getName());
                    if(cursor.moveToFirst()){
                        do{

                            idList = cursor.getInt(0);
                            nameList = cursor.getString(1);
                            courseList = cursor.getString(2);
                            emailList = cursor.getString(3);
                            contactList = cursor.getString(4);
                            addressList = cursor.getString(5);
                            timestampList = cursor.getString(6);

                            Intent intent = new Intent(getApplicationContext(), UpdateInfoActivity.class);
                            intent.putExtra("_id", idList);
                            intent.putExtra("name", nameList);
                            intent.putExtra("course", courseList);
                            intent.putExtra("email", emailList);
                            intent.putExtra("contact", contactList);
                            intent.putExtra("address", addressList);
                            intent.putExtra("timestamp", timestampList);
                            startActivity(intent);
                        }while(cursor.moveToNext());
                    }
                    cursor.close();

//                    if(idList==-1){
//                        Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
//                    }else{
//                        Toast.makeText(getApplicationContext(), "ID: "+idList+" NAME: "+nameList, Toast.LENGTH_SHORT).show();
//                    }

                }
            });

            row.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {


                    AlertDialog.Builder builder = new AlertDialog.Builder(UpdateActivity.this);
                    builder.setIcon(R.drawable.ic_delete);
                    builder.setTitle("Trainee Name: " + trainee.getName());
                    builder.setMessage("Are you sure to delete this trainee?");
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            int result = dbHelper.deleteData(trainee.getName());
                            getSearchName("");
                            listData.clear();
                            populateView();
                            CustomAdapter adapter = new CustomAdapter(getApplicationContext(), R.layout.custom_activity, listData);
                            updateListView.setAdapter(adapter);
                            dbHelper.close();
                            if (result > 0) {
                                Toast.makeText(UpdateActivity.this, "Trainee Successfully deleted", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(UpdateActivity.this, "Trainee not delete", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
//                            Toast.makeText(getApplicationContext(),"Delete Cancel",Toast.LENGTH_SHORT).show();
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

        public class ViewHolder {
            TextView name;

        }
    }
}
