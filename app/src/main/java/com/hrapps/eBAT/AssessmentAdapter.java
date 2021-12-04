package com.hrapps.eBAT;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hrapps.R;

import java.util.List;

import Utility.Constants;
import Utility.TimeUtils;

/**
 * Created by Harshitha.bshekharap on 11/16/2017.
 */

public class AssessmentAdapter extends RecyclerView.Adapter<AssessmentAdapter.MyViewHolder> {

    private Context context;
    private List<AssessmentModel> employeeList;
    private boolean isSelf;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView ivMiniCardAccent, ivIcon, ivEdit, ivDelete;
        public TextView tvTitle, tvSubTitle, tvDescription;
        public CardView cvParent;

        public MyViewHolder(View view) {
            super(view);

            ivMiniCardAccent = (ImageView) view.findViewById(R.id.ivMiniCardAccent);
            ivIcon = (ImageView) view.findViewById(R.id.ivIcon);

            tvTitle = (TextView) view.findViewById(R.id.tvTitle);
            tvSubTitle = (TextView) view.findViewById(R.id.tvSubTitle);
            tvDescription = (TextView) view.findViewById(R.id.tvDescription);
            cvParent = (CardView) view.findViewById(R.id.cvParent);

//            ivEdit = (ImageView) view.findViewById(R.id.ivEdit);
//            ivDelete = (ImageView) view.findViewById(R.id.ivDelete);
        }
    }


    public AssessmentAdapter(Context context, List<AssessmentModel> employeeList, boolean isSelf) {
        this.context = context;
        this.employeeList = employeeList;
        this.isSelf = isSelf;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.mini_list_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final AssessmentModel model = employeeList.get(position);

        if (model.getStatus().equalsIgnoreCase(Constants.APPROVED)) {
            holder.ivMiniCardAccent.setBackgroundColor(context.getResources().getColor(R.color.taskGreen));
        } else if (model.getStatus().equalsIgnoreCase(Constants.PENDING_REWORK)) {
            holder.ivMiniCardAccent.setBackgroundColor(context.getResources().getColor(R.color.taskRed));
        } else if (model.getStatus().equalsIgnoreCase(Constants.PENDING_FOR_INPUT)) {
            holder.ivMiniCardAccent.setBackgroundColor(context.getResources().getColor(R.color.grey));
        }

        holder.tvTitle.setText(model.getAppraisee().getName());
        if (model.getAssessmentYear().isEmpty())
            holder.tvSubTitle.setText(model.getStatus());
        else
            holder.tvSubTitle.setText(model.getAssessmentYear());
        if (!model.getSubmittedDate().isEmpty())
            holder.tvDescription.setText("Submitted:" + TimeUtils.getDateForTitle(model.getSubmittedDate()));

        holder.cvParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isSelf || !model.getStatus().equalsIgnoreCase(Constants.PENDING_FOR_INPUT)) {
                    Intent assessmentDetail = new Intent(context, AssessmentDetailActivity.class);
                    assessmentDetail.putExtra("ID", employeeList.get(position).getId());
                    assessmentDetail.putExtra("ISSELF", isSelf);
                    ((Activity) context).startActivityForResult(assessmentDetail, 111);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return employeeList == null ? 0 : employeeList.size();
    }
}
