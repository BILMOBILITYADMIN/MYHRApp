package adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hrapps.R;

import java.util.ArrayList;

import Model.NotificationsModel;
import Utility.CWIncturePreferences;
import Utility.CWUrls;
import Utility.TimeUtils;
import Utility.Util;


public class TimesheetNotificationsAdapter extends ArrayAdapter<NotificationsModel> {

    private ArrayList<NotificationsModel> notificationList;
    private Context _context;

    class ViewHolder {
        TextView timesheet_status, week, description, act_for, notification_date;
        Utility.CircleImageView avatar;
        RelativeLayout notification_background;

    }


    public TimesheetNotificationsAdapter(Context context, int textViewResourceId,
                                         ArrayList<NotificationsModel> stateList) {
        super(context, textViewResourceId, stateList);
        this.notificationList = new ArrayList<>();
        this.notificationList.addAll(stateList);
        this._context = context;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {

            LayoutInflater vi = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = vi.inflate(R.layout.timesheet_notifications_list, null);
            holder = new ViewHolder();

            holder.timesheet_status = (TextView) convertView.findViewById(R.id.timesheetstatus);
            holder.week = (TextView) convertView.findViewById(R.id.timesheetweek);
            holder.description = (TextView) convertView.findViewById(R.id.timesheettext);

            holder.avatar = (Utility.CircleImageView) convertView.findViewById(R.id.status);
            // holder.act_for = (TextView)convertView.findViewById(R.id.act_for);
            holder.notification_date = (TextView) convertView.findViewById(R.id.notification_date);

            holder.notification_background = (RelativeLayout) convertView.findViewById(R.id.notification_background);

            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        NotificationsModel item = notificationList.get(position);


        holder.timesheet_status.setText(item.message);
        //  holder.act_for.setText(item.sender_displayName);
        holder.notification_date.setText(TimeUtils.getDate(item.createdAt));
        holder.week.setText("Week :" + TimeUtils.getDateFromWeeks(item.timesheetStartDate) + "  -  " + TimeUtils.getDateFromWeeks(item.timesheetendDate));

        if (item.sender_avatarurl != null && !item.sender_avatarurl.isEmpty()) {
            Util.loadImageWithPlaceHolder(_context, CWUrls.IMAGE_FETCH_URL + item.sender_avatarurl + "?email=" + CWIncturePreferences.getEmail(), holder.avatar, R.mipmap.defaultmedium);
        }

        if (item.read_status == true) {
            holder.notification_background.setBackgroundResource(R.color.my_accent);
        } else {
            holder.notification_background.setBackgroundResource(R.color.divider_color);
        }
        return convertView;
    }


}