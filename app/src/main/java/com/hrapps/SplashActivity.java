package com.hrapps;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;

import com.crashlytics.android.Crashlytics;
import com.guardanis.applock.UnlockActivity;
import com.guardanis.applock.locking.LockingHelper;
import com.hrapps.Recruitment_Britannia.Recruitment_Dashboard;

import DB.DbAdapter;
import Utility.CWIncturePreferences;
import io.fabric.sdk.android.Fabric;


public class SplashActivity extends BasicActivity {
    // Button test;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

        // Intent intent = new Intent(this, SetPin.class);
        // startActivityForResult(intent, 1);

        // if(!ActionLockingHelper.unlockIfRequired(this)) {
        // doSomethingThatRequiresLockingIfEnabled();
        // }

        DbAdapter.getDbAdapterInstance().open(this);

        CWIncturePreferences.init(SplashActivity.this);

        IntentLauncher launcher = new IntentLauncher();
        launcher.start();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == LockingHelper.REQUEST_CODE_UNLOCK && resultCode == RESULT_OK) {
            //doSomethingThatRequiresLockingIfEnabled();
            if (CWIncturePreferences.getAccessToken().equalsIgnoreCase(CWIncturePreferences.EMPTY_STRING_DEFAULT_VALUE)) {

                String config_url = CWIncturePreferences.getConfigUrl();

                if (config_url.equalsIgnoreCase("null") || config_url.equalsIgnoreCase("")) {
                    Intent intent = new Intent(SplashActivity.this, Login.class);
                    SplashActivity.this.startActivity(intent);
                } else {
                    Intent intent = new Intent(SplashActivity.this, Login.class);
                    SplashActivity.this.startActivity(intent);
                }
            } else {
                String posValue = "";
                if (CWIncturePreferences.getRole().equalsIgnoreCase("vpsa") || CWIncturePreferences.getRole().equalsIgnoreCase("vphr") || CWIncturePreferences.getRole().equalsIgnoreCase("SDM1") || CWIncturePreferences.getRole().equalsIgnoreCase("SDM2")) {
                    posValue = "no";
                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    intent.putExtra("pos", posValue);
                    startActivity(intent);
                }
//                else if (CWIncturePreferences.getRole().equalsIgnoreCase("placementAgency")||CWIncturePreferences.getRole().equalsIgnoreCase("bgvTeam")){
//                    Intent intent = new Intent(SplashActivity.this, Recruitment_Dashboard.class);
//                    startActivity(intent);
//                }
                else {
                    posValue = "yes";
                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    intent.putExtra("pos", posValue);
                    startActivity(intent);
                }
            }


            SplashActivity.this.finish();
        }


    }


    private class IntentLauncher extends Thread {
        @Override
        /**
         * Sleep for some time and then start new activity.
         */
        public void run() {
            try {
                // Sleeping
                Thread.sleep(3 * 1000);
            } catch (Exception e) {
                Log.e("Exception", e.getMessage());
            }
            if (CWIncturePreferences.getPin().equalsIgnoreCase("locked")) {
                Intent intent = new Intent(SplashActivity.this, UnlockActivity.class);
                startActivityForResult(intent, LockingHelper.REQUEST_CODE_UNLOCK);
            } else {
                if (CWIncturePreferences.getAccessToken().equalsIgnoreCase(CWIncturePreferences.EMPTY_STRING_DEFAULT_VALUE)) {

                    String config_url = CWIncturePreferences.getConfigUrl();

                    if (config_url.equalsIgnoreCase("null") || config_url.equalsIgnoreCase("")) {
                        Intent intent = new Intent(SplashActivity.this, Login.class);
                        SplashActivity.this.startActivity(intent);
                    } else {
                        Intent intent = new Intent(SplashActivity.this, Login.class);
                        SplashActivity.this.startActivity(intent);
                    }
                } else {

                    String posValue = "";
                    if (CWIncturePreferences.getRole().equalsIgnoreCase("vpsa") || CWIncturePreferences.getRole().equalsIgnoreCase("vphr") || CWIncturePreferences.getRole().equalsIgnoreCase("SDM1") || CWIncturePreferences.getRole().equalsIgnoreCase("SDM2")) {
                        posValue = "no";
                        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                        intent.putExtra("pos", posValue);
                        startActivity(intent);
                    } else {
                        posValue = "yes";
                        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                        intent.putExtra("pos", posValue);
                        startActivity(intent);
                    }
                }


                SplashActivity.this.finish();
            }

        }
    }


}
