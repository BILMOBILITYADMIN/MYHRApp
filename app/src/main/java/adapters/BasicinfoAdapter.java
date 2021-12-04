package adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hrapps.CSC_Britannia.BasicInfoModel;
import com.hrapps.R;

import java.util.ArrayList;

/**
 * Created by harshu on 10/4/2016.
 */

public class BasicinfoAdapter extends RecyclerView.Adapter<BasicinfoAdapter.ViewHolder> {

    ArrayList<BasicInfoModel> detail_list = new ArrayList<>();

    public BasicinfoAdapter(ArrayList<BasicInfoModel> detail_list) {
        this.detail_list = detail_list;

    }

    @Override
    public BasicinfoAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.basic_info_row, parent, false);
        return new ViewHolder(itemLayoutView);
    }

    @Override
    public void onBindViewHolder(BasicinfoAdapter.ViewHolder holder, int position) {


        if (detail_list.get(position).fname == null) {
            holder.fname.setText("");
        } else {
            holder.fname.setText(detail_list.get(position).fname);
        }
        if (detail_list.get(position).lname == null) {
            holder.lname.setText("");
        } else {
            holder.lname.setText(detail_list.get(position).lname);

        }

        if (detail_list.get(position).email == null) {
            holder.email.setText("");
        } else {
            holder.email.setText(detail_list.get(position).email);

        }


    }

    @Override
    public int getItemCount() {
        return this.detail_list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView fname, lname, role, email;


        public ViewHolder(View itemView) {
            super(itemView);
            fname = (TextView) itemView.findViewById(R.id.data1);
            lname = (TextView) itemView.findViewById(R.id.data2);
            role = (TextView) itemView.findViewById(R.id.data3);
            email = (TextView) itemView.findViewById(R.id.data4);
        }
    }
}
