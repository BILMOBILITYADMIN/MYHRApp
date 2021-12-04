package com.hrapps.receivers;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import Utility.Util;

/**
 * Created by Deeksha on 21-11-2015.
 */

public class NetworkChangeReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, final Intent intent) {

        boolean isOnline = Util.isOnline(context);

        if (isOnline) {
            new Syncher().startSynch(context);
        } else {
            LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(context);
            Intent localintent = new Intent("localIntent");
            //sending intent through BroadcastManager
            broadcastManager.sendBroadcast(localintent);
        }
    }
}
