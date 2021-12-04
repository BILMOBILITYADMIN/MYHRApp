package com.hrapps;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import Model.UserProfileDetail;
import Utility.AsyncResponse;
import Utility.CWIncturePreferences;
import Utility.CWUrls;
import Utility.NetworkConnector;
import adapters.InfoEditAdapter;

public class EditExperienceCertActivity extends BasicActivity implements AsyncResponse {
    RecyclerView recycler_view;
    int type = 0;   //0 for experience and 1 for certification
    ArrayList<UserProfileDetail> details = null;
    TextView title;
    ImageView add, done, cancel;
    private CoordinatorLayout coordinatorLayout;
    TextInputLayout start_date;
    int mYear, mMonth, mDay;

    EditText date_editText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.my_primary_dark));

        }
        setContentView(R.layout.edit_experience_cert);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorlayout);
        recycler_view = (RecyclerView) findViewById(R.id.list);
        title = (TextView) findViewById(R.id.title);
        add = (ImageView) findViewById(R.id.add);
        done = (ImageView) findViewById(R.id.done);
        cancel = (ImageView) findViewById(R.id.cancel);
        start_date = (TextInputLayout) findViewById(R.id.start_date);

        final LinearLayoutManager manager = new LinearLayoutManager(this);

        if (getIntent().getExtras() != null) {
            details = (ArrayList<UserProfileDetail>) getIntent().getExtras().getSerializable("Details");
            type = getIntent().getExtras().getInt("Type");
            InfoEditAdapter edit_adapter = new InfoEditAdapter(this, type, details);
            recycler_view.setLayoutManager(manager);
            recycler_view.setHasFixedSize(true);
            recycler_view.setAdapter(edit_adapter);
            if (type == 0) {
                title.setText("Edit Experience");
            } else {
                title.setText("Edit Certification");
            }
        }

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addInfo = new Intent(EditExperienceCertActivity.this, AddUserDetail.class);
                addInfo.putExtra("Type", type);
                startActivityForResult(addInfo, 4);
            }
        });

        cancel.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder alert = new AlertDialog.Builder(EditExperienceCertActivity.this);

                        alert.setTitle("Confirm Cancel");
                        alert.setMessage("All changes will be discarded. Are you sure you want to cancel?");
                        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                onBackPressed();
                            }
                        });
                        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });

                        final AlertDialog alertDialog = alert.create();
                        alertDialog.show();

                    }
                });


        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean invalid = false;

                JSONObject data = new JSONObject();
                JSONArray array = new JSONArray();
                View view = EditExperienceCertActivity.this.getCurrentFocus();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                details = ((InfoEditAdapter) recycler_view.getAdapter()).getEditedDetails();
                int list_count = details.size();


                for (int i = 0; i < list_count; i++) {


                    if (type == 0) {
                        JSONObject exp = new JSONObject();
                        UserProfileDetail expDetails = details.get(i);

                        if (expDetails.getTitle().length() == 0 || expDetails.getDetail1().length() == 0 || expDetails.getExp_start().length() == 0 || expDetails.getExp_end().length() == 0) {
                            invalid = true;

                        }

                        try {
                            exp.put("designation", expDetails.getTitle());
                            exp.put("companyName", expDetails.getDetail1());
                            exp.put("website", expDetails.getWeb_link());
                            JSONObject date = new JSONObject();
                            date.put("from", details.get(i).getExp_start());
                            date.put("to", details.get(i).getExp_end());
                            exp.put("date", date);
                            array.put(exp);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    } else {
                        JSONObject cert = new JSONObject();
                        UserProfileDetail certDetails = details.get(i);
                        if (certDetails.getTitle().length() == 0 || certDetails.getDetail1().length() == 0 || certDetails.getDetail2().length() == 0) {
                            invalid = true;

                        }

                        try {
                            cert.put("name", certDetails.getTitle());
                            cert.put("institution", certDetails.getDetail1());
                            cert.put("date", certDetails.getDetail2());
                            array.put(cert);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }

                if (invalid) {
                    Snackbar.make(v, "Please fill the empty fields", Snackbar.LENGTH_LONG).show();

                } else {

                    if (type == 0) {
                        try {
                            data.put("experience", array);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    } else {
                        try {
                            data.put("certifications", array);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    Map<String, String> headers = new HashMap<>();

                    headers.put("x-device-id",
                            CWIncturePreferences.getDeviceToken());

                    headers.put("x-email-id", CWIncturePreferences.getEmail());
                    headers.put("x-access-token",
                            CWIncturePreferences.getAccessToken());
                    headers.put("Content-Type", "application/json");


                    NetworkConnector connect = new NetworkConnector(EditExperienceCertActivity.this, NetworkConnector.TYPE_PUT, CWUrls.UPDATE_PROFILE + CWIncturePreferences.getUserId(), headers, data.toString(), EditExperienceCertActivity.this);
                    if (connect.isAllowed()) {
                        connect.execute();
                    } else {
                        Toast.makeText(EditExperienceCertActivity.this, getString(R.string.action_not_allowed), Toast.LENGTH_SHORT).show();
                    }

                }
            }


        });


    }

    /**
     * Method sets the Edit text from where date picker is being called
     */
    public void setDatePicker(EditText editText) {
        date_editText = editText;
        showDialog(0);

    }


    @Override
    protected Dialog onCreateDialog(int id) {
        if (id == 0) {
            Calendar mCurrentDate = Calendar.getInstance();
            mYear = mCurrentDate.get(Calendar.YEAR);
            mMonth = mCurrentDate.get(Calendar.MONTH);
            mDay = mCurrentDate.get(Calendar.DAY_OF_MONTH);

            return new DatePickerDialog(EditExperienceCertActivity.this, datePickerListener, mYear, mMonth, mDay);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {
            if (selectedYear == mYear) {
                String date = selectedDay + " / " + (selectedMonth + 1) + " / " + selectedYear;
                date_editText.setText(date);
            }
        }
    };


    @Override
    public void processFinish(String output, int status_code, String url, int type) {
        {
            String status = "";
            try {
                JSONObject response = new JSONObject(output);
                status = response.getString("status");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if (status.equalsIgnoreCase("success")) {
                Intent result = new Intent();
                result.putExtra("Details", details);
                setResult(200, result);
                finish();

            }

            Snackbar.make(coordinatorLayout, status, Snackbar.LENGTH_SHORT).show();

        }
    }

    @Override
    public void configurationUpdated(boolean configUpdated) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 200) {
            UserProfileDetail detail = (UserProfileDetail) data.getSerializableExtra("Detail");
            details.add(detail);
            recycler_view.getAdapter().notifyDataSetChanged();
        }
    }

}
