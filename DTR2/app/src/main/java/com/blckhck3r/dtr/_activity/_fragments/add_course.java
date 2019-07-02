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
import android.widget.Spinner;
import android.widget.Toast;

import com.blckhck3r.dtr.R;
import com.blckhck3r.dtr._activity._database.databaseHelper;
import com.blckhck3r.dtr._activity._misc.Course;
import com.blckhck3r.dtr._activity._misc.dbLog;
import com.fujiyuu75.sequent.Animation;
import com.fujiyuu75.sequent.Sequent;

import cn.pedant.SweetAlert.SweetAlertDialog;
import es.dmoral.toasty.Toasty;
import in.codeshuffle.typewriterview.TypeWriterView;

public class add_course extends Fragment {
    static int sHour = 0;
    static int eHour = 0;
    static int sMinute = 0;
    static int eMinute = 0;
    static int temp_minute = 0;
    EditText addLimit;
    EditText addCourse;
    EditText addDescription;
    EditText traineeLimit;
    EditText startMinute;
    EditText endMinute;
    databaseHelper dbHelper;
    Spinner t1, t2;
    EditText day;
    String[] timeCondition = {"AM", "PM"};
    EditText timeIn, timeOut;
    Button courseBtn;
    TypeWriterView typeWriterView;
    private long mLastClickTime = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_add_course, container, false);
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

    public void Toast(final String s) {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                Toasty.normal(getActivity(), s, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                typeWriterView.animateText("Create Course");
                typeWriterView.setDelay(50);
            }
        });
        typeWriterView.setWithMusic(false);
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dbHelper = new databaseHelper(getActivity());
        addLimit = (EditText) view.findViewById(R.id.addLimit);
        addCourse = (EditText) view.findViewById(R.id.addCourse);
        addDescription = (EditText) view.findViewById(R.id.addDescription);
        traineeLimit = (EditText) view.findViewById(R.id.traineeLimit);
        day = (EditText) view.findViewById(R.id.day);
        timeIn = (EditText) view.findViewById(R.id.timeIn);
        timeOut = (EditText) view.findViewById(R.id.timeOut);
        startMinute = (EditText) view.findViewById(R.id.startMinute);
        endMinute = (EditText) view.findViewById(R.id.endMinute);
        courseBtn = (Button) view.findViewById(R.id.courseBtn);
        t1 = (Spinner) view.findViewById(R.id.t1);
        t2 = (Spinner) view.findViewById(R.id.t2);
        typeWriterView = (TypeWriterView) view.findViewById(R.id.typeWriterView);
        setSpinner();


        view.findViewById(R.id.courseBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();

                final String limit = addLimit.getText().toString().trim();
                final String course = addCourse.getText().toString();
                final String description = addDescription.getText().toString();
                final String regLimit = traineeLimit.getText().toString().trim();
                final String daySched = day.getText().toString();
                final String tIn = timeIn.getText().toString();
                final String tOut = timeOut.getText().toString();
                String tInMinute = startMinute.getText().toString();
                String tOutMinute = endMinute.getText().toString();
                final String s_spinner = t1.getSelectedItem().toString();
                final String e_spinner = t2.getSelectedItem().toString();
                String regexNum = "\\d+";

                if (TextUtils.isEmpty(limit)) {
                    addLimit.setError("Total hours of Training is required");
                    return;
                } else if (!(limit.matches(regexNum))) {
                    addLimit.setError("Total hours of training must not contain any special character");
                    return;
                } else if (limit.length() >= 5) {
                    addLimit.setError("Time limit must not exceed to the length of 5 or more than");
                    return;
                }
                if (TextUtils.isEmpty(course)) {
                    addCourse.setError("Course name is required");
                    return;
                }

                Cursor cursor = dbHelper.getCourseId(addCourse.getText().toString());
                if (cursor.moveToFirst()) {
                    do {
                        addCourse.setError("The course name you entered is already exists");
                        Toasty.error(getActivity(), "The course title you entered is already exist in our database",
                                Toast.LENGTH_SHORT).show();
                        return;
                    } while (cursor.moveToNext());
                }
                cursor.close();
                dbHelper.close();

                if (TextUtils.isEmpty(regLimit)) {
                    traineeLimit.setError("Registration limit is required");
                    return;
                } else if (!(regLimit.matches(regexNum))) {
                    traineeLimit.setError("Registration limit must not contain any special character");
                    return;
                } else if (regLimit.length() >= 4) {
                    traineeLimit.setError("The length of registration limit must not exceed the length of 3 or more than");
                    return;
                }
                if (tInMinute.length() == 0 || tOutMinute.length() == 0) {
                    startMinute.setText("00");
                    endMinute.setText("00");
                    return;
                }

                sHour = Integer.parseInt(tIn);
                eHour = Integer.parseInt(tOut);
                sMinute = Integer.parseInt(tInMinute);
                eMinute = Integer.parseInt(tOutMinute);

                if (TextUtils.isEmpty(tIn)) {
                    timeIn.setText("00");
                    timeIn.setError("Timeout mandatory");
                    timeIn.setError("Don't leave empty this field");
                    return;
                }
                if (TextUtils.isEmpty(tOut)) {
                    timeOut.setText("00");
                    timeOut.setError("Timeout mandatory");
                    timeOut.setError("Don't leave empty this field");
                    return;
                }

                if (tIn.equals("00") || tOut.equals("00")) {
                    timeOut.setError("Timein mandatory");
                    timeIn.setError("Timeout mandatory");
                    Toasty.error(getActivity(), "Dont leave this field empty, pick a number not greater than in 12", Toast.LENGTH_SHORT).show();
                    timeOut.setText("00");
                    timeIn.setText("00");
                    return;
                }

                if (sHour > 12) {
                    timeIn.setError("Timein hour should not greater than in 12");
                    Toasty.error(getActivity(), "Timein hour should not greater than in 12 hour clock", Toast.LENGTH_SHORT, true).show();
                    return;
                } else if (sHour <= 0) {
                    timeIn.setError("Timein hour should not equal to zero or any negative integer");
                    Toasty.error(getActivity(), "Timein is invalid", Toast.LENGTH_SHORT, true).show();
                }
                if (eHour > 12) {
                    timeOut.setError("Timeout hour should not greater than in 12");
                    Toasty.error(getActivity(), "Timeout hour should not greater than in 12 hour clock", Toast.LENGTH_SHORT, true).show();
                    return;
                } else if (eHour <= 0) {
                    timeOut.setError("Timeout should not equal to zero or any negative integer");
                    Toasty.error(getActivity(), "Timeout is invalid", Toast.LENGTH_SHORT, true).show();
                    return;
                }
                if (sMinute > 60) {
                    Toasty.error(getActivity(), "Timein minute should not greater than 60", Toast.LENGTH_SHORT, true).show();
                    return;
                } else if (sMinute == 60) {
                    temp_minute = (sMinute / 60);
                    sHour += temp_minute;
                    sMinute = 0;
                }
                if (eMinute > 60) {
                    Toasty.error(getActivity(), "Timeout minute should not greater than 60", Toast.LENGTH_SHORT, true).show();
                    return;
                } else if (eMinute == 60) {
                    temp_minute = (eMinute / 60);
                    eHour += temp_minute;
                    eMinute = 0;
                }

                if (TextUtils.isEmpty(description)) {
                    addDescription.setError("Description is required");
                    return;
                } else if (description.length() >= 299) {
                    Toasty.error(getActivity(), "Description must not exceed up to 299 characters", Toast.LENGTH_SHORT, true).show();
                    addDescription.setError("Description must not exceed up to 299 characters");
                    return;
                }


                new SweetAlertDialog(getActivity(), SweetAlertDialog.CUSTOM_IMAGE_TYPE)
                        .setTitleText("Are you sure?")
                        .setContentText("Do you want to create this course? ")
                        .setCustomImage(R.drawable.add_database)
                        .setConfirmText(" Yes ")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(final SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismiss();
                                courseBtn.setEnabled(false);
                                courseBtn.setClickable(false);

                                boolean x = dbHelper.addCourse(new Course(Integer.parseInt(limit), course, description,
                                        Integer.parseInt(regLimit), daySched, Integer.parseInt(String.valueOf(sHour)), eHour,
                                        sMinute, eMinute, s_spinner, e_spinner));
                                if (x) {
                                    Toasty.success(getActivity(), "Course successfully created!", Toast.LENGTH_SHORT, true).show();
                                    new SweetAlertDialog(getActivity(), SweetAlertDialog.SUCCESS_TYPE)
                                            .setTitleText("Success")
                                            .setContentText("Course successfully created.")
                                            .show();
                                    boolean result = dbHelper.addLog(new dbLog("Course successfully created, course title: " + course));
                                    dbHelper.close();
                                    setClear();
                                } else {
                                    new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE)
                                            .setTitleText("Oops...")
                                            .setContentText("Something went wrong!")
                                            .show();
                                    boolean result = dbHelper.addLog(new dbLog("Course not added, something wrong in the database"));
                                    dbHelper.close();
                                }
                                eMinute = 0;
                                temp_minute = 0;
                                sMinute = 0;
                                sHour = 0;
                                eHour = 0;
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

                courseBtn.setEnabled(true);
                courseBtn.setClickable(true);

            }
        });

        //reset Button

        view.findViewById(R.id.resetBtn).setOnClickListener(new View.OnClickListener() {
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
                        .setConfirmText(" Yes ")
                        .setCancelText(" No ")
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
                            public void onClick(final SweetAlertDialog sDialog) {
                                new Handler().post(new Runnable() {
                                    @Override
                                    public void run() {
                                        sDialog.dismiss();
                                        Toasty.info(getActivity(), "All field has been cleared", Toast.LENGTH_SHORT).show();
                                    }
                                });
                                new SweetAlertDialog(getActivity(), SweetAlertDialog.SUCCESS_TYPE)
                                        .setTitleText("Successfully Reset")
                                        .setContentText("All field has been cleared")
                                        .show();
                                setClear();
                            }
                        }).show();

            }
        });
        //eof of reset Button
    }

    public void setClear() {
        courseBtn.setEnabled(true);
        courseBtn.setClickable(true);
        addLimit.setText("");
        addCourse.setText("");
        addDescription.setText("");
        day.setText("");
        traineeLimit.setText("");
        timeIn.setText("00");
        timeOut.setText("00");
        startMinute.setText("00");
        endMinute.setText("00");
        dbHelper.close();

    }

    public void setSpinner() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, timeCondition);
        t1.setAdapter(adapter);
        t2.setAdapter(adapter);
        day.setText("");
        t1.setSelection(0);
        t2.setSelection(1);
    }
}
