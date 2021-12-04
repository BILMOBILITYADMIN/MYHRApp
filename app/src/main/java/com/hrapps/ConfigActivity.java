package com.hrapps;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import Utility.AsyncResponse;
import Utility.CWIncturePreferences;
import Utility.CWUrls;
import Utility.NetworkConnector;
import Utility.Util;

/**
 * Created by harshu on 12/14/2015.
 */
public class ConfigActivity extends BasicActivity implements AsyncResponse {
    EditText username;

    ImageView config_done;
    ArrayList<String> authTypes = new ArrayList<>();
    String config_url = "";
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    protected void onCreate(Bundle savedInstanceState) {

        // this.requestWindowFeature(Window.FEATURE_NO_TITLE); // Removes title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //
        super.onCreate(savedInstanceState);
        setContentView(R.layout.config_screen);


        username = (EditText) findViewById(R.id.username);
        config_done = (ImageView) findViewById(R.id.config_done);

        config_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String validEmail = username.getText().toString().trim();

                if (!validEmail.matches(emailPattern)) {
                    Toast.makeText(ConfigActivity.this, getString(R.string.invalid_email), Toast.LENGTH_SHORT).show();
                } else {
                    config_url = validEmail;

                    if (Util.isOnline(ConfigActivity.this)) {
                        NetworkConnector connector = new NetworkConnector(ConfigActivity.this, NetworkConnector.TYPE_GET, CWUrls.PRE_CONFIG_URL + config_url, null, null, ConfigActivity.this);
                        if (connector.isAllowed()) {
                            connector.execute();
                        } else {
                            Toast.makeText(ConfigActivity.this, getString(R.string.action_not_allowed), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(ConfigActivity.this, getString(R.string.no_internet), Toast.LENGTH_LONG).show();
                    }

                }


            }
        });
    }

    @Override
    public void processFinish(String output, int status_code, String url, int type) {
        String authType = "";
        if (url.contains(CWUrls.PRE_CONFIG_URL)) {
            try {
                JSONObject jsonObject = new JSONObject(output);

                if (jsonObject.optString("status").equalsIgnoreCase("success")) {
                    //authtypes to be parsed

                    JSONObject subObject = jsonObject.getJSONObject("data");
                    JSONArray authTypesArray = subObject.getJSONArray("authTypes");
                    for (int i = 0; i < authTypesArray.length(); i++) {
                        authType = authTypesArray.optString(i);
                    }
                    authTypes.add(authType);
                    String logo = subObject.optString("logo");

                    CWIncturePreferences.init(ConfigActivity.this);
                    CWIncturePreferences.setLogo(logo);
                    CWIncturePreferences.setConfigUrl(username.getText().toString());
                    Intent i = new Intent(ConfigActivity.this, Login.class);
                    i.putExtra("logo", logo);
                    startActivity(i);
                } else {
                    Toast.makeText(ConfigActivity.this, "Message :" + jsonObject.optString("message"), Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }

    @Override
    public void configurationUpdated(boolean configUpdated) {

    }
}
