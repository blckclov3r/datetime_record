package com.blckhck3r.dtr._activity._fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.blckhck3r.dtr.R;
import com.blckhck3r.dtr._activity._database.databaseHelper;
import com.blckhck3r.dtr._activity._misc.Trainee;
import com.blckhck3r.dtr._activity._misc.dbLog;
import com.fujiyuu75.sequent.Animation;
import com.fujiyuu75.sequent.Sequent;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;
import es.dmoral.toasty.Toasty;
import in.codeshuffle.typewriterview.TypeWriterView;

public class add_fragment extends Fragment {
    public static int traineeTime = 0;
    public static int traineeLimit = 0;
    public static int compareLimit = 0;
    static int timein = 0;
    static int timeout = 0;
    static int minutein = 0;
    static int minuteout = 0;
    //    String[] course = {"Empty", "Android", "C#", ".NET", "Wordpress", "Java"};
    ArrayAdapter<String> courseAdapter;
    SearchableSpinner spinnerCourse;
    EditText tName, tEmail, tContact, tAddress;
    databaseHelper dbHelper;
    Button insBtn, btnReset;
    String schedDay = "";
    String st = "";
    String et = "";
    TypeWriterView typeWriterView;
    private long mLastClickTime = 0;

    public add_fragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.add_fragment, container, false);
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                LinearLayout layoutBtn = (LinearLayout) view.findViewById(R.id.buttonArea);
                Sequent.origin(layoutBtn).anim(getActivity(), Animation.FADE_IN_LEFT).
                        delay(300).start();
                LinearLayout layout = (LinearLayout) view.findViewById(R.id.helloworld);
                Sequent.origin(layout).anim(getActivity(), Animation.BOUNCE_IN).
                        delay(400).start();
            }
        });
        return view;
    }

    public void setSpinner() {
        ArrayList<String> courseList = dbHelper.getAllCourse();
        if (courseList.isEmpty() || courseList.equals("")) {
            courseList.add("");
            new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("Empty")
                    .setContentText("Course list is empty\nCreate course first before to proceed in the registration form")
                    .setConfirmText(" Ok ")
                    .show();
            Toasty.warning(getActivity(), "Course mandatory", Toast.LENGTH_SHORT).show();
        }
        courseAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, courseList);
        spinnerCourse.setAdapter(courseAdapter);
        courseAdapter.notifyDataSetChanged();
    }

    @Override
    public void onStart() {
        super.onStart();
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                typeWriterView.animateText("Registration Form");
                typeWriterView.setDelay(50);
            }
        });
        typeWriterView.setWithMusic(false);
    }

    @Override
    public void onPause() {
        super.onPause();
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                typeWriterView.setWithMusic(false);
                typeWriterView.removeAnimation();
            }
        });
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        typeWriterView = (TypeWriterView) view.findViewById(R.id.typeWriterView);
        spinnerCourse = (SearchableSpinner) view.findViewById(R.id.spinnerCourse);
        spinnerCourse.setTitle("Course");
        spinnerCourse.setPositiveButton("Ok");
        tName = (EditText) view.findViewById(R.id.tName);
        tEmail = (EditText) view.findViewById(R.id.tEmail);
        tContact = (EditText) view.findViewById(R.id.tContact);
        tAddress = (EditText) view.findViewById(R.id.tAddress);
        insBtn = (Button) view.findViewById(R.id.insBtn);
        btnReset = (Button) view.findViewById(R.id.btnReset);
        dbHelper = new databaseHelper(getActivity());
        setSpinner();

        view.findViewById(R.id.btnReset).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();

                new SweetAlertDialog(getActivity(), SweetAlertDialog.CUSTOM_IMAGE_TYPE)
                        .setTitleText("Reset")
                        .setCustomImage(R.drawable.reload)
                        .setContentText("Are you sure to reset all fields?")
                        .setCancelText(" No ")
                        .setConfirmText(" Yes ")
                        .showCancelButton(true)
                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.cancel();
                                return;
                            }
                        })
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.dismiss();
                                traineeTime = 0;
                                traineeLimit = 0;
                                compareLimit = 0;
                                new Handler().post(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toasty.info(getActivity(), "All field has been cleared", Toast.LENGTH_SHORT).show();
                                    }
                                });
                                new SweetAlertDialog(getActivity(), SweetAlertDialog.SUCCESS_TYPE)
                                        .setTitleText("Reset")
                                        .setContentText("All field has been reset")
                                        .show();
                                setClear();
                            }
                        }).show();
//                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//                builder.setTitle("Reset");
//                builder.setIcon(R.drawable.ic_loop);
//                builder.setMessage("Are you sure to reset all fields?");
//                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        traineeTime = 0;
//                        traineeLimit = 0;
//                        compareLimit = 0;
//                        setClear();
////                        tName.setEnabled(true);
////                        tAddress.setEnabled(true);
////                        tContact.setEnabled(true);
////                        tEmail.setEnabled(true);
////                        spinnerCourse.setEnabled(true);
////                        insBtn.setEnabled(true);
////                        insBtn.setClickable(true);
////                        insBtn.setTextColor(Color.WHITE);
////                        insBtn.setBackgroundColor(Color.rgb(47, 79, 79));
////                        Toast("Reset");
//                    }
//                });
//                builder.setNegativeButton("Dismiss", new DialogInterface.OnClickListener(){
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        dialogInterface.dismiss();
//                    }
//                });
//                builder.show();

            }
        });

        view.findViewById(R.id.insBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();

                final String name = tName.getText().toString().trim();
                final String course = spinnerCourse.getSelectedItem().toString().trim();
                final String email = tEmail.getText().toString().trim();
                final String contact = tContact.getText().toString().trim();
                final String address = tAddress.getText().toString().trim();
                String regexString = "[a-zA-Z ]+";
                String regexNum = "\\d+";
                String regex = "^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";


                final Cursor cursor = dbHelper.getDataId(tName.getText().toString());
                if (cursor.moveToFirst()) {
                    do {
                        tName.setError("The name you entered is already exists");
                        Toasty.error(getActivity(), "The name you entered is already exists", Toast.LENGTH_SHORT).show();
                        return;
                    } while (cursor.moveToNext());
                }
                cursor.close();
                dbHelper.close();

                if (TextUtils.isEmpty(name)) {
                    tName.setError("Name is required");
                    Toasty.error(getActivity(), "Name mandatory", Toast.LENGTH_SHORT).show();
                    return;
                } else if (!(name.matches(regexString))) {
                    tName.setError("Name must not contain any special character or number");
                    Toasty.error(getActivity(), "Name must not contain any special character or number", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (name.length() <= 4 && name.length() != 0) {
                    Toasty.error(getActivity(), "The name you entered is too low", Toast.LENGTH_SHORT).show();
                    tName.setError("The name you entered is too low");
                    return;
                } else if (name.length() >= 30) {
                    Toasty.error(getActivity(), "The name you entered is too long", Toast.LENGTH_SHORT).show();
                    tName.setError("The name you entered is too long");
                    return;
                }

                if (course.length() == 0 || course.equals("")) {
                    Toasty.error(getActivity(), "Course mandatory", Toast.LENGTH_SHORT).show();
                    Toast("Course is required");
                    return;
                }

                if (tEmail.length() >= 30) {
                    Toasty.error(getActivity(), "The email address you entered is too long", Toast.LENGTH_SHORT).show();
                    tEmail.setError("The email address you entered is too long");
                    return;
                } else if (TextUtils.isEmpty(email)) {
                    Toasty.error(getActivity(), "Email address is mandatory", Toast.LENGTH_SHORT).show();
                    tEmail.setError("Email address is required");
                    return;
                } else if (!(email.matches(regex))) {
                    Toasty.error(getActivity(), "The email address you entered is invalid", Toast.LENGTH_SHORT).show();
                    tEmail.setError("The email you entered is invalid");
                    return;
                }

                if (tContact.length() >= 15) {
                    Toasty.error(getActivity(), "The contact number you entered is too long", Toast.LENGTH_SHORT).show();
                    tContact.setError("The Contact Number you entered is too long");
                    return;
                } else if (!contact.matches(regexNum)) {
                    Toasty.error(getActivity(), "Contact number must not contain negative sign or any special character", Toast.LENGTH_SHORT).show();
                    tContact.setError("Contact number must not contain negative sign or any special character");
                    return;
                } else if (TextUtils.isEmpty(contact)) {
                    Toasty.error(getActivity(), "Contact number mandatory", Toast.LENGTH_SHORT).show();
                    tContact.setError("Contact Number is required");
                    return;
                }
                if (TextUtils.isEmpty(address)) {
                    Toasty.error(getActivity(), "Address mandatory", Toast.LENGTH_SHORT).show();
                    tAddress.setError("Address is required");
                    return;
                } else if (address.length() >= 399) {
                    Toasty.error(getActivity(), "The address you entered is too long", Toast.LENGTH_SHORT).show();
                    tAddress.setError("Address characters is too long");
                    return;
                }
                final Cursor cursorCourse = dbHelper.getCourseValue(course);
                if (cursorCourse.moveToFirst()) {
                    do {
                        traineeTime = cursorCourse.getInt(1);
                        compareLimit = cursorCourse.getInt(4);
                        schedDay = cursorCourse.getString(5);
                        timein = cursorCourse.getInt(6);
                        timeout = cursorCourse.getInt(7);

                        minutein = cursorCourse.getInt(8);
                        minuteout = cursorCourse.getInt(9);
                        st = cursorCourse.getString(10);
                        et = cursorCourse.getString(11);

                    } while (cursorCourse.moveToNext());
                }
                cursorCourse.close();
                dbHelper.close();
                traineeLimit = dbHelper.countCourse(course);
                if (traineeLimit >= compareLimit) {
                    new Handler().post(new Runnable() {
                        @Override
                        public void run() {
                            Toasty.error(getActivity(), "The course you selected is already full", Toast.LENGTH_SHORT).show();
                        }
                    });
                    return;
                }
                final String finalSchedDay = schedDay;
                final int in = timein;
                final int out = timeout;
                final int remaining = traineeTime;
                final int inminute = minutein;
                final int outmiute = minuteout;
                final String s_time = st;
                final String e_time = et;

                new SweetAlertDialog(getActivity(), SweetAlertDialog.CUSTOM_IMAGE_TYPE)
                        .setTitleText("Are you sure?")
                        .setContentText("Do you want to insert this new trainee?")
                        .setCustomImage(R.drawable.add_database)
                        .setConfirmText(" Yes ")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(final SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismiss();
                                insBtn.setClickable(false);
                                insBtn.setEnabled(false);
                                boolean result = dbHelper.addData(new Trainee(name, course, email, contact, address, remaining,
                                        finalSchedDay, in, out, inminute, outmiute, s_time, e_time
                                ));
                                if (result) {
                                    Toasty.success(getActivity(), "Trainee successfully added!", Toast.LENGTH_SHORT, true).show();
                                    new SweetAlertDialog(getActivity(), SweetAlertDialog.SUCCESS_TYPE)
                                            .setTitleText("Success!")
                                            .setContentText("New trainee, successfully created!")
                                            .show();
                                    boolean x = dbHelper.addLog(new dbLog("Add trainee, name: " + name + ", course: " + course));
                                    dbHelper.close();
                                    traineeTime = 0;
                                    compareLimit = 0;
                                    traineeLimit = 0;
                                    setClear();
                                } else {
                                    sweetAlertDialog.dismiss();
                                    new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE)
                                            .setTitleText("Oops...")
                                            .setContentText("Something went wrong!")
                                            .show();
                                    boolean x = dbHelper.addLog(new dbLog("Trainee not inserted"));
                                    dbHelper.close();
                                    Toast("Data not inserted");
                                }

                            }
                        })
                        .setCancelText(" No ")
                        .showCancelButton(true)
                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.cancel();
                                return;
                            }
                        })
                        .show();
                insBtn.setClickable(true);
                insBtn.setEnabled(true);
            }
        });
    }

    public void setClear() {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                insBtn.setClickable(true);
                insBtn.setEnabled(true);
                tName.setText("");
                tAddress.setText("");
                tContact.setText("");
                tEmail.setText("");
                spinnerCourse.setSelection(0);
                dbHelper.close();
            }
        });
    }

    public void Toast(final String s) {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                Toasty.info(getActivity(), s, Toast.LENGTH_SHORT).show();
            }
        });
    }


}
