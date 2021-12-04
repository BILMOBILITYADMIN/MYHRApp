package com.hrapps;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import Model.UserProfileDetail;

public class AddUserDetail extends BasicActivity {
    int user_detail_type = 0;
    LinearLayout exp_layout, cert_layout;
    TextView title;
    EditText cert_name, cert_institute, cert_date;
    EditText exp_title, exp_company, exp_link, exp_start, exp_end;
    ImageView done, cancel;
    DatePickerDialog dialog = null;
    Calendar myCalendar;
    String date_type = "";
    AlertDialog alertDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_user_detail);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.my_primary_dark));

        }

        exp_layout = (LinearLayout) findViewById(R.id.experience_layout);
        cert_layout = (LinearLayout) findViewById(R.id.certificate_layout);
        title = (TextView) findViewById(R.id.title);
        done = (ImageView) findViewById(R.id.done);
        cancel = (ImageView) findViewById(R.id.cancel);

        //cert layout
        cert_name = (EditText) findViewById(R.id.cert_name);
        cert_institute = (EditText) findViewById(R.id.cert_institute);
        cert_date = (EditText) findViewById(R.id.cert_date);

        //experience layout
        exp_title = (EditText) findViewById(R.id.exp_title);
        exp_company = (EditText) findViewById(R.id.exp_company);
        exp_link = (EditText) findViewById(R.id.exp_link);
        exp_start = (EditText) findViewById(R.id.exp_start);
        exp_end = (EditText) findViewById(R.id.exp_end);

        myCalendar = Calendar.getInstance();

        user_detail_type = getIntent().getExtras().getInt("Type");

        if (user_detail_type == 0) {
            title.setText("Add Experience");
            exp_layout.setVisibility(View.VISIBLE);
            exp_start.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!alertDialog.isShowing()) {
                        alertDialog.show();
                        date_type = "start";
                    }
                }
            });


            exp_end.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!alertDialog.isShowing()) {
                        alertDialog.show();
                        date_type = "end";
                    }
                }
            });


        } else {
            title.setText("Add Certification");
            cert_layout.setVisibility(View.VISIBLE);
            cert_date.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (!dialog.isShowing()) {
                        dialog.show();
                        date_type = "cert_date";
                    }
                }
            });
        }

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user_detail_type == 0) {

                    if (exp_title.length() == 0 || exp_company.length() == 0 || exp_end.length() == 0 || exp_start.length() == 0) {
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(exp_title.getWindowToken(), 0);
                        Snackbar.make(v, "Please fill the empty fields", Snackbar.LENGTH_LONG).show();

                    } else {
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(exp_title.getWindowToken(), 0);
                        UserProfileDetail expDetail = new UserProfileDetail(exp_title.getText().toString(), exp_company.getText().toString(), exp_start.getText().toString() + " to " + exp_end.getText().toString());
                        expDetail.setExp_start(exp_start.getText().toString());
                        expDetail.setExp_end(exp_end.getText().toString());
                        expDetail.setWeb_link(exp_link.getText().toString());
                        Intent data = new Intent();
                        data.putExtra("Detail", expDetail);
                        setResult(200, data);
                        finish();
                    }
                } else {

                    if (cert_name.length() == 0 || cert_institute.length() == 0 || cert_date.length() == 0) {
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(cert_name.getWindowToken(), 0);
                        Snackbar.make(v, "Please fill the empty fields", Snackbar.LENGTH_LONG).show();
                    } else {
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(cert_name.getWindowToken(), 0);
                        UserProfileDetail certDetails = new UserProfileDetail(cert_name.getText().toString(), cert_institute.getText().toString(), cert_date.getText().toString());
                        Intent data = new Intent();
                        data.putExtra("Detail", certDetails);
                        setResult(200, data);
                        finish();

                    }

                }
            }
        });

        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                String myFormat = "dd/MM/yy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                if (date_type.equalsIgnoreCase("start")) {
                    exp_start.setText(sdf.format(myCalendar.getTime()));
                } else if (date_type.equalsIgnoreCase("end")) {
                    exp_end.setText(sdf.format(myCalendar.getTime()));
                } else {
                    cert_date.setText(sdf.format(myCalendar.getTime()));
                }

            }
        };
        if (user_detail_type == 0) {
            final View dialogView = View.inflate(this, R.layout.dialog_date_picker, null);
            alertDialog = new AlertDialog.Builder(this).setView(dialogView).create();

            DatePicker datePicker = (DatePicker) dialogView.findViewById(R.id.date_picker);

            datePicker.init(myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH), null);


            initDatePicker(datePicker);

            dialogView.findViewById(R.id.date_time_set).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DatePicker datePicker = (DatePicker) dialogView.findViewById(R.id.date_picker);

                    myCalendar = new GregorianCalendar(datePicker.getYear(),
                            datePicker.getMonth(),
                            datePicker.getDayOfMonth());
                    String myFormat = "MMM-yyyy"; //In which you need put here
                    SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                    if (date_type.equalsIgnoreCase("start")) {

                        String endDate = exp_end.getText().toString();
                        if (!endDate.isEmpty()) {
                            Date end = null;
                            try {
                                end = sdf.parse(endDate);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            if (end != null && end.before(myCalendar.getTime())) {
                                Toast.makeText(AddUserDetail.this, getString(R.string.invalid_end_date), Toast.LENGTH_SHORT).show();
                            } else {
                                exp_start.setText(sdf.format(myCalendar.getTime()));
                            }
                        } else {
                            exp_start.setText(sdf.format(myCalendar.getTime()));
                        }

                    } else if (date_type.equalsIgnoreCase("end")) {

                        String startDate = exp_start.getText().toString();
                        if (!startDate.isEmpty()) {
                            Date start = null;
                            try {
                                start = sdf.parse(startDate);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            if (start != null && start.after(myCalendar.getTime())) {
                                Toast.makeText(AddUserDetail.this, getString(R.string.invalid_end_date), Toast.LENGTH_SHORT).show();
                            } else {
                                exp_end.setText(sdf.format(myCalendar.getTime()));
                            }
                        } else {
                            exp_end.setText(sdf.format(myCalendar.getTime()));
                        }
                    } else {
                        cert_date.setText(sdf.format(myCalendar.getTime()));
                    }


                    alertDialog.dismiss();
                }
            });
        } else {
            dialog = new DatePickerDialog(AddUserDetail.this, date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH));
        }

    }

    public void initDatePicker(DatePicker date_picker) {

        int year = date_picker.getYear();
        int month = date_picker.getMonth();
        int day = date_picker.getDayOfMonth();

        date_picker.init(year, month, day, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                //Add whatever you need to handle Date changes
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            int daySpinnerId = Resources.getSystem().getIdentifier("day", "id", "android");
            if (daySpinnerId != 0) {
                View daySpinner = date_picker.findViewById(daySpinnerId);
                if (daySpinner != null) {
                    daySpinner.setVisibility(View.GONE);
                }
            }

            int monthSpinnerId = Resources.getSystem().getIdentifier("month", "id", "android");
            if (monthSpinnerId != 0) {
                View monthSpinner = date_picker.findViewById(monthSpinnerId);
                if (monthSpinner != null) {
                    monthSpinner.setVisibility(View.VISIBLE);
                }
            }

            int yearSpinnerId = Resources.getSystem().getIdentifier("year", "id", "android");
            if (yearSpinnerId != 0) {
                View yearSpinner = date_picker.findViewById(yearSpinnerId);
                if (yearSpinner != null) {
                    yearSpinner.setVisibility(View.VISIBLE);
                }
            }
        } else { //Older SDK versions
            Field f[] = date_picker.getClass().getDeclaredFields();
            for (Field field : f) {
                if (field.getName().equals("mDayPicker") || field.getName().equals("mDaySpinner")) {
                    field.setAccessible(true);
                    Object dayPicker = null;
                    try {
                        dayPicker = field.get(date_picker);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                    ((View) dayPicker).setVisibility(View.GONE);
                }

                if (field.getName().equals("mMonthPicker") || field.getName().equals("mMonthSpinner")) {
                    field.setAccessible(true);
                    Object monthPicker = null;
                    try {
                        monthPicker = field.get(date_picker);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                    ((View) monthPicker).setVisibility(View.VISIBLE);
                }

                if (field.getName().equals("mYearPicker") || field.getName().equals("mYearSpinner")) {
                    field.setAccessible(true);
                    Object yearPicker = null;
                    try {
                        yearPicker = field.get(date_picker);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                    ((View) yearPicker).setVisibility(View.VISIBLE);
                }
            }
        }
    }

}
