package com.hrapps;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

/**
 * Created by harshu on 5/17/2016.
 */
public abstract class BasicActivity extends AppCompatActivity {

    private BroadcastReceiver broadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Crouton.makeText(BasicActivity.this, "No internet Connection", Style.ALERT).show();
            }
        };

    }

    @Override
    public void onStart() {
        super.onStart();

        LocalBroadcastManager.getInstance(this).registerReceiver((broadcastReceiver),
                new IntentFilter("localIntent")
        );
    }

    @Override
    public void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver);
    }


    public static boolean isOnline(Context _context) {
        ConnectivityManager cm = (ConnectivityManager) _context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}

