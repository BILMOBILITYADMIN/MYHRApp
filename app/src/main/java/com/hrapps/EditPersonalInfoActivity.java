package com.hrapps;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import Utility.AsyncResponse;
import Utility.CWIncturePreferences;
import Utility.CWUrls;
import Utility.NetworkConnector;
import Utility.Util;

public class EditPersonalInfoActivity extends BasicActivity implements AsyncResponse {
    EditText mobile, email, location;
    ImageView done;
    CoordinatorLayout coordinatorLayout;
    Spinner maritalStatus;
    String[] status = new String[]{
            "Marital Status",
            "Married",
            "Single",
            "Divorced",
            "Widowed"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_personal_info);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.my_primary_dark));

        }
        ImageView cancel = (ImageView) findViewById(R.id.cancel);
        done = (ImageView) findViewById(R.id.done);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorlayout);
        maritalStatus = (Spinner) findViewById(R.id.marital_status);
        location = (EditText) findViewById(R.id.location);
        mobile = (EditText) findViewById(R.id.mobile);
        email = (EditText) findViewById(R.id.email);

        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_list_item_1, status) {
            @Override
            public boolean isEnabled(int position) {

                return position != 0;
            }

            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if (position == 0) {
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                } else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        maritalStatus.setAdapter(spinnerArrayAdapter);

        final String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";


        if (getIntent().getExtras() != null) {
            mobile.setText(getIntent().getExtras().getString("Mobile"));
            email.setText(getIntent().getExtras().getString("Email"));
            location.setText(getIntent().getExtras().getString("Location"));

            String ms = getIntent().getExtras().getString("maritalStatus");

            for (int i = 0; i < status.length; i++) {
                if (ms.equalsIgnoreCase(status[i])) {
                    maritalStatus.setSelection(i);
                }
            }

            Log.d("email==", "" + getIntent().getExtras().getString("Email"));
            Log.d("mobile==", "" + getIntent().getExtras().getString("Mobile"));
            Log.d("location==", "" + getIntent().getExtras().getString("Location"));
        }
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(EditPersonalInfoActivity.this);

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

                final String validEmail = email.getText().toString().trim();
                View view = EditPersonalInfoActivity.this.getCurrentFocus();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }

                if (mobile.length() == 0 || location.length() == 0 || email.length() == 0) {
                    Snackbar.make(v, "Please fill the empty fields", Snackbar.LENGTH_LONG).show();

                } else if (mobile.length() > 10 || mobile.length() < 10) {
                    Snackbar.make(v, "Please enter a valid mobile number !", Snackbar.LENGTH_LONG).show();

                } else if (!validEmail.matches(emailPattern)) {
                    Snackbar.make(v, "Please enter a valid email address !", Snackbar.LENGTH_LONG).show();

                } else {

                    if (Util.isOnline(EditPersonalInfoActivity.this)) {
                        updatePersonalInfo();
                    } else {
                        Snackbar.make(coordinatorLayout, "No internet", Snackbar.LENGTH_SHORT).show();
                    }

                }

            }
        });
    }

    /**
     * Async call to update experience/certification details
     */

    public void updatePersonalInfo() {


        Map<String, String> headers = new HashMap<>();

        headers.put("x-device-id",
                CWIncturePreferences.getDeviceToken());


        headers.put("x-email-id", CWIncturePreferences.getEmail());
        headers.put("x-access-token",
                CWIncturePreferences.getAccessToken());
        headers.put("Content-Type", "application/json");

        JSONObject data = new JSONObject();
        JSONObject personal = new JSONObject();
        try {
            personal.put("maritalStatus", maritalStatus.getSelectedItem());
            personal.put("mobile", mobile.getText().toString());
            personal.put("email", email.getText().toString());
            personal.put("location", location.getText().toString());

            data.put("personalInformation", personal);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        NetworkConnector connect = new NetworkConnector(this, NetworkConnector.TYPE_PUT, CWUrls.UPDATE_PROFILE + CWIncturePreferences.getUserId(), headers, data.toString(), this);
        if (connect.isAllowed()) {
            connect.execute();
        } else {
            Toast.makeText(this, getString(R.string.action_not_allowed), Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    public void processFinish(String output, int status_code, String url, int type) {
        String status = "";
        try {
            JSONObject response = new JSONObject(output);
            status = response.getString("status");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (status.equalsIgnoreCase("success")) {
            Intent result = new Intent();
            result.putExtra("Mobile", mobile.getText().toString());
            result.putExtra("Email", email.getText().toString());
            result.putExtra("Location", location.getText().toString());
            result.putExtra("maritalStatus", maritalStatus.getSelectedItem().toString());
            setResult(200, result);
            finish();
        } else {
            Snackbar.make(coordinatorLayout, status, Snackbar.LENGTH_SHORT).show();
        }

    }

    @Override
    public void configurationUpdated(boolean configUpdated) {

    }
}
