package com.blckhck3r.dtr._activity._fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
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
import com.blckhck3r.dtr._activity._activity.time_activity.TraineeTimeList;
import com.blckhck3r.dtr._activity._database.DatabaseHelper;
import com.blckhck3r.dtr._activity._misc.Trainee;

import java.util.ArrayList;

/**
 * Created by admin on 6/4/2018.
 */

public class Update_Fragment extends Fragment {
    DatabaseHelper dbHelper;
    ListView listView;
    ArrayList<Trainee> listData;
    CustomAdapter adapter;
    SearchView sv;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.update_frag,container,false);
        dbHelper = new DatabaseHelper(getActivity());
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

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listData.clear();
        getSearchName("");
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
                Toast.makeText(getActivity(), "You are searching for . . .", Toast.LENGTH_SHORT).show();
                return false;
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        listData.clear();
        getSearchName("");
    }

    @Override
    public void onPause() {
        super.onPause();
        listData.clear();
        getSearchName("");
    }

    public void getSearchName(String searchTerm) {
        listData.clear();
        Trainee trainee;
        Cursor c = dbHelper.retrieve(searchTerm);
        int id;
        String name;
        while (c.moveToNext()) {
            id = c.getInt(0);
            name = c.getString(1);
            trainee = new Trainee();
            trainee.setTrainee_id(id);
            trainee.setName(name);
            listData.add(trainee);
        }
        dbHelper.close();
        listView.setAdapter(adapter);

    }
    public void populateView(){
        Cursor cursor = dbHelper.getData();
        if(cursor.moveToFirst()){
            do{
                String name = cursor.getString(1);
                String timestamp = cursor.getString(6);
                listData.add(new Trainee(name,timestamp));

            }while(cursor.moveToNext());
        }
        adapter = new CustomAdapter(getActivity(),R.layout.custom_activity,listData);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }


    private  class CustomAdapter extends BaseAdapter {
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

            row.setOnClickListener(new AdapterView.OnClickListener() {
                int idList = -1;
                String nameList = "";
                String courseList = "";
                String emailList = "";
                String contactList="";
                String addressList="";
                String timestampList="";
                int timeremaining = 0;
                @Override
                public void onClick(View view) {
                    Cursor cursor = dbHelper.getDataId(trainee.getName());
                    while (cursor.moveToNext()){

                        idList = cursor.getInt(0);
                        nameList = cursor.getString(1);
                        courseList = cursor.getString(2);
                        emailList = cursor.getString(3);
                        contactList = cursor.getString(4);
                        addressList = cursor.getString(5);
                        timestampList = cursor.getString(6);
                        timeremaining = cursor.getInt(7);

                        Intent intent= new Intent(getActivity(),TraineeTimeList.class);
                        intent.putExtra("_id",idList);
                        intent.putExtra("name",nameList);
                        intent.putExtra("course",courseList);
                        intent.putExtra("email",emailList);
                        intent.putExtra("contact",contactList);
                        intent.putExtra("address",addressList);
                        intent.putExtra("timestamp",timestampList);
                        intent.putExtra("timeremaining",timeremaining);
                        startActivity(intent);
                    }



                }
            });

            row.setOnLongClickListener(new AdapterView.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {


                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setIcon(R.drawable.ic_delete);
                    builder.setTitle("Trainee Name: "+trainee.getName());
                    builder.setMessage("Are you sure to delete this trainee?");
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            int result = dbHelper.deleteData(trainee.getName());
                            getSearchName("");
                            listData.clear();
                            populateView();
                            CustomAdapter adapter = new CustomAdapter(getActivity(),R.layout.custom_activity,listData);
                            listView.setAdapter(adapter);
                            dbHelper.close();
                            if(result>0){
                                Toast.makeText(getActivity(), "Trainee Successfully deleted", Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(getActivity(), "Trainee not delete", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
//                            Toast.makeText(getActivity(),"Delete Cancel",Toast.LENGTH_SHORT).show();
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

        public class ViewHolder{
            TextView name;
        }
    }
}
