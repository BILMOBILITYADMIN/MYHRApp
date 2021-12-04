package adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hrapps.R;

import java.util.ArrayList;

import Model.UserProfileDetail;
import Utility.Util;

/**
 * Created by Deeksha on 11-01-2016.
 */
public class UserProfileDetailAdapter extends RecyclerView.Adapter<UserProfileDetailAdapter.ViewHolder> {
    ArrayList<UserProfileDetail> detail_list = new ArrayList<>();

    public UserProfileDetailAdapter(ArrayList<UserProfileDetail> detail_list) {
        this.detail_list = detail_list;

    }

    @Override
    public UserProfileDetailAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_profile_detail_row, parent, false);
        return new ViewHolder(itemLayoutView);
    }

    @Override
    public void onBindViewHolder(UserProfileDetailAdapter.ViewHolder holder, int position) {


        if (detail_list.get(position).getTitle().equalsIgnoreCase("transferdate")) {
            holder.title.setText("Date of Transfer");
        } else if (detail_list.get(position).getTitle().equals("joiningdate")) {
            holder.title.setText("Date of Joining");
        } else if (detail_list.get(position).getTitle().equalsIgnoreCase("id")) {
            holder.title.setText("EmpId");
        } else {
            holder.title.setText(Util.capitalizeWords(detail_list.get(position).getTitle()));
        }

        if (detail_list.get(position).getDetail1().equalsIgnoreCase("null") || detail_list.get(position).getDetail1().isEmpty()) {
            holder.detail1.setText("");
        } else {
            holder.detail1.setText(detail_list.get(position).getDetail1());
        }
        if (detail_list.get(position).getDetail2() == null || detail_list.get(position).getDetail2().isEmpty()) {
            holder.detail2.setText("");
        } else {
            holder.detail2.setText(detail_list.get(position).getDetail2());
        }

    }


    @Override
    public int getItemCount() {
        return this.detail_list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView detail1;
        TextView detail2;
        ImageView icon;

        public ViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            detail1 = (TextView) itemView.findViewById(R.id.detail1);
            detail2 = (TextView) itemView.findViewById(R.id.detail2);
            icon = (ImageView) itemView.findViewById(R.id.icon);
        }
    }
}
