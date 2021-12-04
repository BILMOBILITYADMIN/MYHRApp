package adapters;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hrapps.EditExperienceCertActivity;
import com.hrapps.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import Model.UserProfileDetail;

/**
 * Created by Deeksha on 05-02-2016.
 */
public class InfoEditAdapter extends RecyclerView.Adapter<InfoEditAdapter.ViewHolder> {
    Context _context;
    Calendar myCalendar;
    int type = 0;
    DatePickerDialog dialog = null;

    String date_type = "";
    ArrayList<UserProfileDetail> details = new ArrayList<UserProfileDetail>();
    private AlertDialog alertDialog;

    public InfoEditAdapter(Context context, int type, ArrayList<UserProfileDetail> details) {
        this._context = context;
        this.type = type;
        this.details = details;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (type == 0) {
            View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.experience_layout, null);
            return new ViewHolder(itemLayoutView);
        } else {
            View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.certificate_layout, null);
            return new ViewHolder(itemLayoutView);
        }
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        myCalendar = Calendar.getInstance();

        if (type == 0) {
            holder.exp_title.setText(details.get(position).getTitle());
            holder.exp_title.addTextChangedListener(new MyCustomEditTextListener(holder.exp_title, position));
            holder.exp_company.setText(details.get(position).getDetail1());
            holder.exp_company.addTextChangedListener(new MyCustomEditTextListener(holder.exp_company, position));
            holder.exp_link.setText(details.get(position).getWeb_link());
            holder.exp_link.addTextChangedListener(new MyCustomEditTextListener(holder.exp_link, position));
            holder.exp_start.addTextChangedListener(new MyCustomEditTextListener(holder.exp_start, position));
            holder.exp_end.addTextChangedListener(new MyCustomEditTextListener(holder.exp_end, position));
            String[] dates = details.get(position).getDetail2().split(" to ");
            if (dates != null && dates.length > 1) {
                holder.exp_start.setText(dates[0]);
                holder.exp_end.setText(dates[1]);
            }
            holder.exp_start.setOnClickListener(new datePickerClickedForExperience(holder.exp_start, position));
            holder.exp_end.setOnClickListener(new datePickerClickedForExperience(holder.exp_end, position));
        } else {
            holder.cert_name.setText(details.get(position).getTitle());
            holder.cert_institute.setText(details.get(position).getDetail1());
            holder.cert_date.setText(details.get(position).getDetail2());
            int count = position + 1;
            holder.cert_count.setText("" + count + ".");
            holder.cert_date.setOnClickListener(new datePickerClicked(holder.cert_date));

            holder.cert_name.addTextChangedListener(new MyCustomEditTextListener(holder.cert_name, position));
            holder.cert_institute.addTextChangedListener(new MyCustomEditTextListener(holder.cert_institute, position));

            holder.cert_date.addTextChangedListener(new MyCustomEditTextListener(holder.cert_date, position));


        }

        holder.delete.setOnClickListener(new deleteClicked(position));

    }


    @Override
    public int getItemCount() {
        return details.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        EditText cert_name, cert_institute, cert_date;
        TextView cert_count;
        EditText exp_title, exp_company, exp_link, exp_start, exp_end;
        ImageView delete;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            //cert layout
            cert_name = (EditText) itemLayoutView.findViewById(R.id.cert_name);
            cert_institute = (EditText) itemLayoutView.findViewById(R.id.cert_institute);
            cert_date = (EditText) itemLayoutView.findViewById(R.id.cert_date);
            cert_count = (TextView) itemLayoutView.findViewById(R.id.count);

            //experience layout
            exp_title = (EditText) itemLayoutView.findViewById(R.id.exp_title);
            exp_company = (EditText) itemLayoutView.findViewById(R.id.exp_company);
            exp_link = (EditText) itemLayoutView.findViewById(R.id.exp_link);
            exp_start = (EditText) itemLayoutView.findViewById(R.id.exp_start);
            exp_end = (EditText) itemLayoutView.findViewById(R.id.exp_end);

            //delete button for both
            delete = (ImageView) itemLayoutView.findViewById(R.id.delete);
        }

    }

    class deleteClicked implements View.OnClickListener {
        int pos = 0;

        public deleteClicked(int position) {
            pos = position;
        }

        @Override
        public void onClick(View v) {

            AlertDialog.Builder delete_experience = new AlertDialog.Builder(
                    _context);
            delete_experience.setTitle("Confirm Delete");
            delete_experience.setMessage("Are you sure you want to delete ?");

            delete_experience.setPositiveButton("Confirm",
                    new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog,
                                            int which) {
                            if (details != null && details.size() > 0) {
                                details.remove(pos);
                                notifyDataSetChanged();
                            }
                        }

                    });

            delete_experience.setNegativeButton("No",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog deleteExp = delete_experience.create();

            // show it
            deleteExp.show();

        }
    }

    class datePickerClicked implements View.OnClickListener {

        EditText editText;

        public datePickerClicked(EditText et) {
            editText = et;
        }

        @Override
        public void onClick(View view) {

            ((EditExperienceCertActivity) InfoEditAdapter.this._context).setDatePicker(editText);

        }
    }

    class datePickerClickedForExperience implements View.OnClickListener {

        EditText editText;
        int position = 0;

        public datePickerClickedForExperience(EditText et, int position) {
            editText = et;
            this.position = position;
        }

        @Override
        public void onClick(View view) {
            {
                final View dialogView = View.inflate(_context, R.layout.dialog_date_picker, null);
                alertDialog = new AlertDialog.Builder(_context).setView(dialogView).create();

                DatePicker datePicker = (DatePicker) dialogView.findViewById(R.id.date_picker);

                datePicker.init(myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH), null);


                LinearLayout ll = (LinearLayout) datePicker.getChildAt(0);
                LinearLayout ll2 = (LinearLayout) ll.getChildAt(0);
                ll2.getChildAt(0).setVisibility(View.GONE);

                dialogView.findViewById(R.id.date_time_set).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        DatePicker datePicker = (DatePicker) dialogView.findViewById(R.id.date_picker);

                        myCalendar = new GregorianCalendar(datePicker.getYear(),
                                datePicker.getMonth(),
                                datePicker.getDayOfMonth());
                        String myFormat = "MMM-yyyy"; //In which you need put here
                        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                        if (editText.getId() == R.id.exp_end) {
                            String startDate = details.get(position).getExp_start();
                            Date start = null;
                            try {
                                start = sdf.parse(startDate);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            if (start != null && start.after(myCalendar.getTime())) {
                                Toast.makeText(_context, _context.getString(R.string.invalid_end_date), Toast.LENGTH_SHORT).show();
                            } else {
                                editText.setText(sdf.format(myCalendar.getTime()));
                            }
                        } else if (editText.getId() == R.id.exp_start) {
                            String endDate = details.get(position).getExp_end();
                            Date end = null;
                            try {
                                end = sdf.parse(endDate);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            if (end != null && end.before(myCalendar.getTime())) {
                                Toast.makeText(_context, _context.getString(R.string.invalid_end_date), Toast.LENGTH_SHORT).show();
                            } else {
                                editText.setText(sdf.format(myCalendar.getTime()));
                            }
                        } else {
                            editText.setText(sdf.format(myCalendar.getTime()));
                        }

                        alertDialog.dismiss();
                    }
                });

                alertDialog.show();
            }


        }
    }

    private class MyCustomEditTextListener implements TextWatcher {

        private int position;
        private EditText editText;

        public MyCustomEditTextListener(EditText ed, int position) {
            this.editText = ed;
            this.position = position;
        }

        public void updatePosition(int position) {
            this.position = position;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            if (position < details.size()) {
                if (charSequence.length() == 0) {
                    return;
                }
                switch (editText.getId()) {
                    case R.id.exp_title:
                        if (details.get(position).getTitle().equals(charSequence.toString())) {
                            return;
                        } else {
                            details.get(position).setTitle(charSequence.toString());

                        }
                        break;
                    case R.id.exp_company:
                        if (details.get(position).getDetail1().equals(charSequence.toString())) {
                            return;
                        } else {
                            details.get(position).setDetail1(charSequence.toString());
                        }
                        break;
                    case R.id.exp_link:
                        if (details.get(position).getWeb_link().equals(charSequence.toString())) {
                            return;
                        } else {
                            details.get(position).setWeb_link(charSequence.toString());
                        }
                        break;
                    case R.id.exp_start:
                        if (details.get(position).getExp_start().equals(charSequence.toString())) {
                            return;
                        } else {
                            details.get(position).setExp_start(charSequence.toString());
                        }

                        details.get(position).setDetail2(details.get(position).getExp_start() + " to " + details.get(position).getExp_end());

                        break;

                    case R.id.exp_end:
                        if (details.get(position).getExp_end().equals(charSequence.toString())) {
                            return;
                        } else {
                            details.get(position).setExp_end(charSequence.toString());
                        }

                        details.get(position).setDetail2(details.get(position).getExp_start() + " to " + details.get(position).getExp_end());

                        break;

                    case R.id.cert_name:
                        if (details.get(position).getTitle().equals(charSequence.toString())) {
                            return;
                        } else {
                            details.get(position).setTitle(charSequence.toString());

                        }
                        break;
                    case R.id.cert_institute:
                        if (details.get(position).getDetail1().equals(charSequence.toString())) {
                            return;
                        } else {
                            details.get(position).setDetail1(charSequence.toString());
                        }
                        break;

                    case R.id.cert_date:
                        if (details.get(position).getDetail2().equals(charSequence.toString())) {
                            return;
                        } else {
                            details.get(position).setDetail2(charSequence.toString());
                        }


                        break;

                    default:
                        break;
                }
            }

        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    }


    public ArrayList<UserProfileDetail> getEditedDetails() {
        return details;

    }

}
