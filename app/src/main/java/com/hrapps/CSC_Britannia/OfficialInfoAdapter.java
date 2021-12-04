package com.hrapps.CSC_Britannia;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hrapps.R;

import java.util.ArrayList;

/**
 * Created by harshu on 11/23/2016.
 */

public class OfficialInfoAdapter extends RecyclerView.Adapter<OfficialInfoAdapter.ViewHolder> {

    ArrayList<OfficialInfoModel> detail_list = new ArrayList<>();

    public OfficialInfoAdapter(ArrayList<OfficialInfoModel> detail_list) {
        this.detail_list = detail_list;

    }

    @Override
    public OfficialInfoAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.official_info_row, parent, false);
        return new OfficialInfoAdapter.ViewHolder(itemLayoutView);
    }

    @Override
    public void onBindViewHolder(OfficialInfoAdapter.ViewHolder holder, int position) {


        if (detail_list.get(position).emp_id == null) {
            holder.emp_id.setText("");
        } else {
            holder.emp_id.setText(detail_list.get(position).emp_id);
        }
        if (detail_list.get(position).sap_id == null) {
            holder.sap_id.setText("");
        } else {
            holder.sap_id.setText(detail_list.get(position).sap_id);

        }
        if (detail_list.get(position).department == null) {
            holder.department.setText("");
        } else {
            holder.department.setText(detail_list.get(position).department);
        }
        if (detail_list.get(position).designation == null) {
            holder.designation.setText("");
        } else {
            holder.designation.setText(detail_list.get(position).designation);

        }


    }

    @Override
    public int getItemCount() {
        return this.detail_list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView emp_id, sap_id, department, designation;


        public ViewHolder(View itemView) {
            super(itemView);
            emp_id = (TextView) itemView.findViewById(R.id.data1);
            sap_id = (TextView) itemView.findViewById(R.id.data2);
            department = (TextView) itemView.findViewById(R.id.data3);
            designation = (TextView) itemView.findViewById(R.id.data4);
        }
    }
}

