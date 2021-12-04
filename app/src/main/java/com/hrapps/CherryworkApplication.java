package com.hrapps;

import android.app.Application;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Deeksha on 23-02-2016.
 */
public class CherryworkApplication extends Application {

    ArrayList<String> attachmentList = new ArrayList<String>();
    TextView attachmentCount = null;
    TextView notificationCountView;
    int notificationCount = 0;


    public void setAttachments(ArrayList<String> attachments) {

        this.attachmentList = attachments;
        if (attachmentCount != null) {
            if (attachmentList.size() > 0) {
                attachmentCount.setVisibility(View.VISIBLE);
                attachmentCount.setText("" + attachmentList.size());
            } else {
                attachmentCount.setVisibility(View.GONE);
            }
        }
    }

    public ArrayList<String> getAttachments() {
        return this.attachmentList;
    }

    public void clearAttachments() {

        this.attachmentList.clear();
        if (attachmentCount != null) {
            attachmentCount.setVisibility(View.GONE);
        }
    }

    public void setAttachmentCountView(TextView count) {
        attachmentCount = count;
        if (attachmentList.size() > 0) {
            attachmentCount.setVisibility(View.VISIBLE);
            attachmentCount.setText("" + attachmentList.size());
        } else {
            attachmentCount.setVisibility(View.GONE);
        }
    }

    public void notifiationAdded() {
        notificationCount++;
        setNotificationCount();
    }

    public void notifiationAdded(int count) {
        notificationCount = count;
        setNotificationCount();
    }

    public void notifiationRemoved() {
        notificationCount--;
        setNotificationCount();
    }

    public void setNotificationCountView(TextView count) {
        notificationCountView = count;

    }

    public void setNotificationCount() {
        if (notificationCountView != null) {
            if (notificationCount > 0) {
                notificationCountView.setVisibility(View.VISIBLE);
                notificationCountView.setText("" + notificationCount);
            } else {
                notificationCountView.setVisibility(View.GONE);
            }
        }
    }
}
