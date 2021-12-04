package com.hrapps;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import DB.DbAdapter;
import DB.DbUtil;
import Model.ConfigModel;
import Model.LoginModel;
import Utility.AsyncResponse;
import Utility.CWIncturePreferences;
import Utility.CWUrls;
import Utility.Constants;
import Utility.NetworkConnector;
import Utility.Util;

/**
 * Created by Arun on 02-09-2015.
 */
public class Login extends Activity implements AsyncResponse {
    private Button login;
    private ImageView app_logo;
    private RelativeLayout parent;
    private EditText et_username, et_password;
    private TextInputLayout user_name_layout, password_layout;

    private TextView reset_password;
    private String user_name_value, password_value, deviceId;
    private String _deviceToken = null;
    private TelephonyManager TelephonyMgr;
    private LoginModel loginModel;
    private static final String PROJECT_NUMBER = "636260679552";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.login);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            getWindow().setNavigationBarColor(getResources().getColor(R.color.my_primary_text));
        }

        DbAdapter.getDbAdapterInstance().open(this);
        CWIncturePreferences.init(this);


        login = (Button) findViewById(R.id.login);
        et_username = (EditText) findViewById(R.id.username);
        et_password = (EditText) findViewById(R.id.password);
        user_name_layout = (TextInputLayout) findViewById(R.id.uname);
        password_layout = (TextInputLayout) findViewById(R.id.pword);
        parent = (RelativeLayout) findViewById(R.id.parent);
        app_logo = (ImageView) findViewById(R.id.applogo);
        reset_password = (TextView) findViewById(R.id.copy);
        reset_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, ForgotPassword.class);
                startActivity(intent);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(et_password.getWindowToken(), 0);

                login();

            }
        });


        getDeviceGcmToken();


        String logo = CWIncturePreferences.getLOGO();
        //Util.loadAttachmentImageMedium(this, logo, app_logo);
        super.onCreate(savedInstanceState);

    }

    /**
     * Registers and gets the GCM Device token
     */
    private void getDeviceGcmToken() {

        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                GoogleCloudMessaging gcm;
                String deviceToken = "";
                try {

                    gcm = GoogleCloudMessaging
                            .getInstance(getApplicationContext());

                    deviceToken = gcm.register(PROJECT_NUMBER);


                } catch (IOException ex) {
                    Log.d("GCM Exception ", ex.getLocalizedMessage());
                }
                return deviceToken;
            }

            @Override
            protected void onPostExecute(String token) {
                _deviceToken = token;
                Log.d("DEVICE Token", "" + _deviceToken);
                if (token != null) {
                    CWIncturePreferences.init(Login.this);
                    CWIncturePreferences.setDeviceToken(token);
                }

            }
        }.execute(null, null, null);


    }

    /**
     * validation and Async call to login
     */
    private void login() {
        user_name_value = et_username.getText().toString().trim();
        password_value = et_password.getText().toString();
        if (Util.isOnline(this)) {
            if (user_name_value.isEmpty() && (password_value.isEmpty() || password_value.length() == 0)) {
                user_name_layout.setError("Username cannot be empty");
                password_layout.setError("Password cannot be empty");
            } else if (password_value.isEmpty()) {
                user_name_layout.setErrorEnabled(false);
                password_layout.setError("Password cannot be empty");
            } else if (user_name_value.isEmpty()) {
                user_name_layout.setError("Username cannot be empty");
                password_layout.setErrorEnabled(false);
            } else {
                loginAsync();
            }

        } else {
            Snackbar snack = Snackbar.make(parent, "No Internet Connection,Try Later", Snackbar.LENGTH_SHORT);

            View view = snack.getView();

            TextView tv = (TextView) view.findViewById(android.support.design.R.id.snackbar_text);
            tv.setTextColor(Color.WHITE);
            snack.show();

        }
    }

    private void loginAsync() {


        if (ContextCompat.checkSelfPermission(Login.this,
                Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            {

                ActivityCompat.requestPermissions(Login.this,
                        new String[]{Manifest.permission.READ_PHONE_STATE},
                        Constants.MY_PERMISSIONS_REQUEST_PHONE);

            }
        } else {
            TelephonyMgr = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
            deviceId = TelephonyMgr.getDeviceId();
            Log.d("deviceid=", "" + deviceId);
            {
                Map<String, String> headers = new HashMap<>();
                headers.put("x-device-id", deviceId);
                headers.put("x-device-type", "android");
                headers.put("x-device-token", CWIncturePreferences.getDeviceToken());
                headers.put("Content-Type", "application/json");

                if (!user_name_value.contains("britindia.com")) {
                    headers.put("type", "external");
                } else if (user_name_value.equalsIgnoreCase("bgv@britindia.com") ||
                        user_name_value.equalsIgnoreCase("medical@britindia.com") ||
                        user_name_value.equalsIgnoreCase("itsupport@britindia.com")) {
                    headers.put("type", "external");
                }

                try {
                    headers.put("android-version", getPackageManager().getPackageInfo(getPackageName(), 0).versionName);
                } catch (PackageManager.NameNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                try {
                    JSONObject obj = new JSONObject();
                    obj.put("email", user_name_value);
                    obj.put("password", password_value);
                    String message = obj.toString();
                    Log.d("DEVICE TOKEN", CWIncturePreferences.getDeviceToken());
                    NetworkConnector connect = new NetworkConnector(Login.this, NetworkConnector.TYPE_POST, CWUrls.LOGIN_URL, headers, message, Login.this);
                    if (connect.isAllowed()) {
                        connect.execute();
                    } else {
                        Toast.makeText(Login.this, getString(R.string.action_not_allowed), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

        }
    }


    @Override
    public void onBackPressed() {
        ActivityCompat.finishAffinity(this);
    }


    @Override
    public void processFinish(String output, int status_code, String url, int type) {
        if (url.contains(CWUrls.LOGIN_URL)) {
            loginModel = new LoginModel();

            DbUtil db = new DbUtil();
            DbUtil.delete(Login.this, Constants.ROLES_TABLE_NAME, null, null);

            try {

                JSONObject main_obj = new JSONObject(output);
                if (main_obj.has("data")) {
                    JSONObject data = main_obj.optJSONObject("data");
                    if (data != null) {
                        loginModel.accesstoken = data.optString("token");
                        loginModel.firstname = Util.capitalizeWords(data.optString("firstName"));
                        loginModel.lastName = Util.capitalizeWords(data.optString("lastName"));
                        loginModel.avatarUrl = data.optString("avatar");
                        loginModel.userId = data.optString("_id");
                        loginModel.email = data.optString("email");
                        loginModel.role = data.optString("role");

                        JSONObject officialInformation = data.optJSONObject("officialInformation");
                        if (officialInformation != null) {
                            loginModel.designation = officialInformation.optString("designation");
                            loginModel.official_region = officialInformation.optString("region");
                            loginModel.position = officialInformation.optLong("id");
                            CWIncturePreferences.setRegion(loginModel.official_region);
                            CWIncturePreferences.setPosition(loginModel.position);

                        }

                        //to check for matrix manager
                        boolean isMatrixManager = data.optBoolean("matrixManager");
                        CWIncturePreferences.setIsMatrixManager(isMatrixManager);

                        JSONArray rolesArray = data.optJSONArray("roles");
                        JSONArray web_roles_array = new JSONArray();
                        JSONObject jsonObject = null;

                        if (rolesArray != null) {
                            for (int r = 0; r < rolesArray.length(); r++) {
                                JSONObject rolesObject = rolesArray.optJSONObject(r);
                                loginModel.role_name = rolesObject.optString("name");
                                loginModel.role_region = rolesObject.optString("region");
                                loginModel.role_text = rolesObject.optString("roleText");
                                loginModel.hr_function = rolesObject.optString("hrFunction");

                                if (loginModel.hr_function.equalsIgnoreCase("null") || loginModel.hr_function.isEmpty()) {
                                    loginModel.hr_function = "undefined";
                                }

                                CWIncturePreferences.setHrFunction(loginModel.hr_function);

                                if (loginModel.role_region.equalsIgnoreCase("null") || loginModel.role_region.isEmpty()) {
                                    loginModel.role_region = loginModel.official_region;
                                }

                                jsonObject = new JSONObject();

                                try {
                                    jsonObject.put("name", loginModel.role_name);
                                    jsonObject.put("region", loginModel.role_region);
                                    jsonObject.put("selected", false);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                db.addRoles(r, loginModel.role_name, loginModel.role_region);
                                web_roles_array.put(jsonObject);
                            }
                        }

                        JSONObject cscReportsTo = data.optJSONObject("cscReportsTo");
                        if (cscReportsTo!=null){
                            loginModel.reportsTo_id= cscReportsTo.optString("_id");
                            loginModel.reportsTo_displayName = cscReportsTo.optString("displayName");
                            loginModel.reportsTo_avatar = cscReportsTo.optString("avatar");
                            loginModel.reportsTo_designation = cscReportsTo.optString("designation");
                            loginModel.reportsTo_mobile = cscReportsTo.optString("mobile");
                            loginModel.reportsTo_email = cscReportsTo.optString("email");
                            loginModel.reportsTo_deleted = cscReportsTo.optBoolean("deleted");
                            loginModel.reportsTo_employeeNumber = cscReportsTo.optString("employeeNumber");

                            JSONObject checkobj = new JSONObject();
                            checkobj.put("_id",loginModel.reportsTo_id);
                            checkobj.put("displayName",loginModel.reportsTo_displayName);
                            checkobj.put("avatar",loginModel.reportsTo_avatar);
                            checkobj.put("designation",loginModel.reportsTo_designation);
                            checkobj.put("mobile",loginModel.reportsTo_mobile);
                            checkobj.put("email",loginModel.reportsTo_email);
                            checkobj.put("deleted",loginModel.reportsTo_deleted);
                            checkobj.put("employeeNumber",loginModel.reportsTo_employeeNumber);
                            CWIncturePreferences.setLoginModelData(checkobj.toString());
                        }


                        // TODO: CHECKN
                        loginModel.rolesArray_web = web_roles_array.toString();
                    }
                }
                if (main_obj.has("status")) {
                    loginModel.status = main_obj.optString("status");
                }
                if (main_obj.has("message")) {
                    loginModel.message = main_obj.optString("message");
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }


            if (loginModel.status.equals("success")) {
                CWIncturePreferences.init(Login.this);
                CWIncturePreferences.setAccessToken(loginModel.getAccesstoken());
                CWIncturePreferences.setUserId(loginModel.getUserId());
                CWIncturePreferences.setFirstname(loginModel.getFirstname());
                CWIncturePreferences.setLastname(loginModel.getLastName());
                CWIncturePreferences.setAvatarurl(loginModel.getAvatarUrl());
                CWIncturePreferences.setEmail(loginModel.getEmail());
                CWIncturePreferences.setDesignation(loginModel.getDesignation());
                CWIncturePreferences.setRole(loginModel.getRole_name());
                CWIncturePreferences.setRoleRegion(loginModel.rolesArray_web);

                initialConfig();

            } else {

                Snackbar.make(parent, loginModel.getMessage(), Snackbar.LENGTH_SHORT).show();

            }
        }

    }

    @Override
    public void configurationUpdated(boolean configUpdated) {

    }

    /**
     * Async call to get configuration
     */
    public void initialConfig() {

        Map<String, String> headers = new HashMap<>();
        headers.put("x-email-id", CWIncturePreferences.getEmail());
        headers.put("x-access-token", CWIncturePreferences.getAccessToken());
        try {
            headers.put("android-version", getPackageManager().getPackageInfo(getPackageName(), 0).versionName);
        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


        NetworkConnector connect = new NetworkConnector(Login.this, NetworkConnector.TYPE_GET, CWUrls.CONFIG_URL, headers, null, Login.this);
        connect.setLogin(true);
        if (connect.isAllowed()) {
            connect.execute();
        } else {
            Toast.makeText(Login.this, getString(R.string.action_not_allowed), Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case Constants.MY_PERMISSIONS_REQUEST_PHONE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    TelephonyMgr = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    deviceId = TelephonyMgr.getDeviceId();
                    {
                        Map<String, String> headers = new HashMap<>();
                        headers.put("x-device-id", deviceId);
                        headers.put("x-device-type", "android");
                        headers.put("x-device-token", CWIncturePreferences.getDeviceToken());
                        headers.put("Content-Type", "application/json");

                        Log.d("deviceyoekn=", "" + CWIncturePreferences.getDeviceToken());

                        try {
                            JSONObject obj = new JSONObject();
                            obj.put("email", user_name_value);
                            obj.put("password", password_value);
                            String message = obj.toString();
                            Log.d("DEVICE TOKEN", CWIncturePreferences.getDeviceToken());
                            NetworkConnector connect = new NetworkConnector(Login.this, NetworkConnector.TYPE_POST, CWUrls.LOGIN_URL, headers, message, Login.this);
                            if (connect.isAllowed()) {
                                connect.execute();
                            } else {
                                Toast.makeText(Login.this, getString(R.string.action_not_allowed), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }

                }

            }


        }
    }

}
