package adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hrapps.R;

import java.util.ArrayList;

import Model.ContactsModel;
import Utility.CWUrls;
import Utility.Util;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by harshu on 5/27/2016.
 */
public class BasicSearchAdapter extends RecyclerView.Adapter<BasicSearchAdapter.ViewHolder> {

    ArrayList<ContactsModel> contactsModelArrayList = new ArrayList<>();
    Context _context;
    private int textColor = Color.BLACK;

    public BasicSearchAdapter(Context context,
                              ArrayList<ContactsModel> resource) {
        // TODO Auto-generated constructor stub
        this._context = context;
        this.contactsModelArrayList = resource;
    }


    @Override
    public BasicSearchAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.groupcontact_row, parent, false);
        return new ViewHolder(itemLayoutView);
    }

    @Override
    public void onBindViewHolder(BasicSearchAdapter.ViewHolder holder, int position) {

        final ContactsModel contactsModel = contactsModelArrayList.get(position);
        holder.firstname.setText(Util.capitalizeWords(contactsModel.getFirstname()));
        holder.lastname.setText(Util.capitalizeWords(contactsModel.getLastname()));
        holder.email.setText(contactsModel.getEmail());
        holder.firstname.setTextColor(textColor);
        holder.lastname.setTextColor(textColor);
        holder.email.setTextColor(textColor);

        Util.loadImageForProfile(_context, CWUrls.IMAGE_FETCH_URL + "thumbnail/" + contactsModel.getsecName(), holder.image);
    }

    @Override
    public int getItemCount() {
        return contactsModelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView firstname, lastname, email;
        CircleImageView image;

        public ViewHolder(View itemView) {
            super(itemView);
            firstname = (TextView) itemView.findViewById(R.id.textView1);
            lastname = (TextView) itemView.findViewById(R.id.textView2);
            email = (TextView) itemView.findViewById(R.id.emailid);
            image = (CircleImageView) itemView.findViewById(R.id.ImageView1);
        }
    }

    public void setTextColor(int color) {
        this.textColor = color;
    }

}
