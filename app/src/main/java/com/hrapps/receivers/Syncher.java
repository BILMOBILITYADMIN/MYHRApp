package com.hrapps.receivers;

import android.content.Context;

import java.util.ArrayList;

import DB.DbUtil;
import Model.OfflineAction;
import Utility.Actions;

/**
 * Created by Deeksha on 19-11-2015.
 */
public class Syncher {

    private ArrayList<OfflineAction> actions;
    Context _context;

    public void startSynch(Context context) {
        this._context = context;
        actions = DbUtil.getPendingActions(context);

        for (int count = 0; count < actions.size(); count++) {
            OfflineAction action = actions.get(count);
            Actions actiontoTake = new Actions(_context, action.getAction_type(), action.getWorkitem_id(), action.getText(), true);
            actiontoTake.onClick(null);
        }


    }


}
