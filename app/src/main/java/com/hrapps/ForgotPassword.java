package com.hrapps;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import Model.LoginModel;
import Utility.AsyncResponse;
import Utility.CWUrls;
import Utility.NetworkConnector;
import Utility.Util;

/**
 * Created by Arun on 19-01-2016.
 */
public class ForgotPassword extends Activity implements AsyncResponse {
    Button send;
    ForgotPassword context;
    LoginModel model;
    TextInputLayout email_layout;
    EditText email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.forgot_password);

        send = (Button) findViewById(R.id.login);
        email = (EditText) findViewById(R.id.password);
        email_layout = (TextInputLayout) findViewById(R.id.pword);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email_value = email.getText().toString().trim();
                if (email_value.isEmpty()) {
                    email_layout.setError("Email cannot be empty");
                } else {
                    email_layout.setErrorEnabled(false);
                    if (Util.isOnline(ForgotPassword.this)) {

                        forgotPassword(email_value);

                    } else {
                        Toast.makeText(ForgotPassword.this, getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
                    }


                }

            }
        });


        super.onCreate(savedInstanceState);

    }

    /**
     * Asynch call for Forgot password
     *
     * @param emailId
     */

    private void forgotPassword(String emailId) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");


        try {
            JSONObject obj = new JSONObject();
            obj.put("email", emailId);
            String message = obj.toString();
            NetworkConnector connect = new NetworkConnector(ForgotPassword.this, NetworkConnector.TYPE_POST, CWUrls.BASE_URL + "forgotPassword", headers, message, ForgotPassword.this);
            if (connect.isAllowed()) {
                connect.execute();
            } else {
                Toast.makeText(ForgotPassword.this, getString(R.string.action_not_allowed), Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    /**
     * Called on successful response from server.
     */
    public void onSuccess() {
        Toast.makeText(ForgotPassword.this, "Reset instructions sent successfully", Toast.LENGTH_SHORT).show();
        onBackPressed();
    }

    @Override
    public void processFinish(String output, int status_code, String url, int type) {
        model = new LoginModel();
        try {

            JSONObject mainobj = new JSONObject(output);
            if (mainobj.has("data")) {
                JSONObject data = mainobj.optJSONObject("data");
                if (data != null) {
                    model.accesstoken = data.optString("token");
                    model.firstname = data.optString("firstName");
                    model.lastName = data.optString("lastName");
                    model.avatarUrl = data.optString("avatarUrl");
                    model.userId = data.optString("_id");
                    model.email = data.optString("email");
                }
            }
            if (mainobj.has("status")) {
                model.status = mainobj.optString("status");
            }
            if (mainobj.has("message")) {
                model.message = mainobj.optString("message");
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


        if (model.status.equals("success")) {
            onSuccess();

        } else {
            RelativeLayout parent = (RelativeLayout) findViewById(R.id.parent);
            Snackbar.make(parent, model.getMessage(), Snackbar.LENGTH_SHORT).show();

        }
    }

    @Override
    public void configurationUpdated(boolean configUpdated) {

    }
}
