package adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hrapps.R;

import java.util.ArrayList;

import Model.ConfigModel;

/**
 * Created by Deeksha on 07-12-2015.
 */
public class CherriesAdapter extends RecyclerView.Adapter<CherriesAdapter.ViewHolder> {
    Context _context;
    ArrayList<ConfigModel> cherries = new ArrayList<>();
    boolean mini = false;
    private OnItemClickListener listener;

    public CherriesAdapter(Context context, ArrayList<ConfigModel> cherries, boolean min) {
        this.cherries = cherries;
        _context = context;
        mini = min;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
        return new ViewHolder(itemLayoutView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (cherries.get(position).id.equalsIgnoreCase("Timesheet")) {
            holder.image.setImageResource(R.mipmap.timesheet_xhdpi);
        } else if (cherries.get(position).id.equalsIgnoreCase("Leave Application")) {
            holder.image.setImageResource(R.drawable.ic_leave);
            holder.text.setText(cherries.get(position).id);

        } else if (cherries.get(position).id.equalsIgnoreCase("Gamify")) {
            holder.image.setImageResource(R.mipmap.gamify);
            holder.text.setText(cherries.get(position).id);
        } else if (cherries.get(position).id.equalsIgnoreCase("CP-rail")) {

            //  holder.image.setImageResource(R.mipmap.cp_rail);
        } else if (cherries.get(position).id.equalsIgnoreCase("Champion Score Card")) {

            holder.image.setImageResource(R.mipmap.csc_new);
            holder.text.setText(cherries.get(position).id);

        } else if (cherries.get(position).id.equalsIgnoreCase("Performance Management System")) {
            holder.image.setImageResource(R.mipmap.performance_new);
            holder.text.setText(cherries.get(position).id);
        } else if (cherries.get(position).id.trim().equalsIgnoreCase("Learning Management System")) {
            holder.image.setImageResource(R.mipmap.lms);
            holder.text.setText(cherries.get(position).id);
        } else if (cherries.get(position).id.trim().equalsIgnoreCase("recruitment")) {
            holder.image.setImageResource(R.mipmap.recruitment_new_icon);
            holder.text.setText(cherries.get(position).id);
        } else if (cherries.get(position).id.trim().equalsIgnoreCase("on boarding")) {
            holder.image.setImageResource(R.mipmap.offboarding);
            holder.text.setText(cherries.get(position).id);
        } else if (cherries.get(position).id.trim().equalsIgnoreCase("e-bat")) {
            holder.image.setImageResource(R.mipmap.ebat);
            holder.text.setText(cherries.get(position).id);
        } else if (cherries.get(position).id.trim().equalsIgnoreCase("Development Conversations")) {
            holder.image.setImageResource(R.mipmap.developement_conversation);
            holder.text.setText(cherries.get(position).id);
        } else if (cherries.get(position).id.trim().equalsIgnoreCase("Employee Reimbursements and Travel")) {
            holder.image.setImageResource(R.mipmap.reimbursement);
            holder.text.setText(cherries.get(position).id);
        } else if (cherries.get(position).id.trim().equalsIgnoreCase("Health check up")) {
            holder.image.setImageResource(R.mipmap.health_checkup);
            holder.text.setText(cherries.get(position).id);
        } else if (cherries.get(position).id.trim().equalsIgnoreCase("e-Exit")) {
            holder.image.setImageResource(R.mipmap.e_exit);
            holder.text.setText(cherries.get(position).id);
        }
//        else if (cherries.get(position).id.trim().equalsIgnoreCase("Leave_app")) {
//            holder.image.setImageResource(R.mipmap.e_exit);
//            holder.text.setText(cherries.get(position).id);
//        }
        else {
            //Recruitment//
            holder.image.setImageResource(R.mipmap.onboarding);
            holder.text.setText(cherries.get(position).id);
        }

    }

    @Override
    public int getItemCount() {
        return cherries.size();
    }

    /**
     * Defines the listener interface
     */
    public interface OnItemClickListener {
        void onItemClick(View itemView, int position);
    }

    // Define the method that allows the parent activity or fragment to define the listener
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView image;
        public TextView text;

        public ViewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.image);
            text = (TextView) itemView.findViewById(R.id.text);
            image.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // Triggers click upwards to the adapter on click
                    if (listener != null) {
                        listener.onItemClick(image, getPosition());
                    }

                }
            });
        }

    }

    @Override
    public int getItemViewType(int position) {
        if (mini) {
            return R.layout.ports_list_item;
        } else {
            return R.layout.ports_grid_item;
        }
    }
}
