package com.hrapps.eBAT;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hrapps.BasicActivity;
import com.hrapps.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import Model.ContactsModel;
import Utility.AsyncResponse;
import Utility.CWIncturePreferences;
import Utility.CWUrls;
import Utility.Constants;
import Utility.NetworkConnector;
import Utility.Util;

/**
 * Created by Harshitha.bshekharap on 11/16/2017.
 */

public class PerformanceKRAAdapter extends BaseExpandableListAdapter implements AsyncResponse {


    private final String assessmentId;
    private final String status;
    private Context _context;
    ArrayList<KRAModel> kra_list = new ArrayList<>();
    ExpandableListView explv_kras;
    private boolean isSelf;
    private ArrayList<String> searched_user_list = new ArrayList<>();
    private ArrayList<String> searched_id_list = new ArrayList<>();
    private ArrayAdapter<String> autoCompleteAdapter;
    private AutoCompleteTextView actvRecos;
    private String reviewerName = "";
    private Dialog dialog;

    public PerformanceKRAAdapter(Context _context, ArrayList<KRAModel> kra_list, ExpandableListView expandableListView, boolean isSelf, String assessmentId, String status) {
        this._context = _context;
        this.kra_list = kra_list;
        this.explv_kras = expandableListView;
        this.isSelf = isSelf;
        this.status = status;
        this.assessmentId = assessmentId;

    }

    @Override
    public int getGroupCount() {
        return this.kra_list.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.kra_list.get(groupPosition).getWeightage();
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return this.kra_list.get(groupPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(final int groupPosition, boolean b, View view, ViewGroup viewGroup) {

        LayoutInflater infalInflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = infalInflater.inflate(R.layout.group_view_layout, null);
        //explv_kras.expandGroup(0);
        TextView weightage_value = (TextView) view.findViewById(R.id.weightage_value);
        TextView weightage_title = (TextView) view.findViewById(R.id.weightage_title);
        View group_divider = (View) view.findViewById(R.id.group_divider);
        TextView bAskForReco = (TextView) view.findViewById(R.id.bAskForReco);
        ImageView ivEdit = (ImageView) view.findViewById(R.id.ivEdit);
        ImageView ivDelete = (ImageView) view.findViewById(R.id.ivDelete);

        weightage_value.setText(Html.fromHtml(kra_list.get(groupPosition).getGoalName()));
        int kraVal = groupPosition + 1;
        weightage_title.setText("KRA" + kraVal + " - Weightage " + kra_list.get(groupPosition).getWeightage() + "%");

        if (explv_kras.isGroupExpanded(groupPosition)) {
            group_divider.setVisibility(View.GONE);
        } else {
            group_divider.setVisibility(View.VISIBLE);
        }
        if (isSelf && (status.equalsIgnoreCase(Constants.PENDING_REWORK) || status.equalsIgnoreCase(Constants.PENDING_FOR_INPUT))) {
            bAskForReco.setVisibility(View.VISIBLE);
            ivEdit.setVisibility(View.VISIBLE);
            ivDelete.setVisibility(View.VISIBLE);

            bAskForReco.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showRecoDialog(groupPosition);
                }
            });
            ivEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent editKRAActivity = new Intent(_context, AddKraActivity.class);
                    editKRAActivity.putExtra("KRA_MODEL", kra_list.get(groupPosition));
                    editKRAActivity.putExtra(Constants.ASSESSMENT_ID, assessmentId);
                    ((Activity) _context).startActivityForResult(editKRAActivity, Constants.EDIT_KRA);
                }
            });
            ivDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showConfirmDeleteDialog(groupPosition);
                }
            });
        }

        return view;
    }

    private void showRecoDialog(final int position) {

        dialog = new Dialog(_context);
        dialog.setContentView(R.layout.dialog_show_reco);

        TextView tvCancel = (TextView) dialog.findViewById(R.id.tvCancel);
        TextView tvSubmit = (TextView) dialog.findViewById(R.id.tvSubmit);


        // Get a reference to the AutoCompleteTextView in the layout
        actvRecos = (AutoCompleteTextView) dialog.findViewById(R.id.actvRecos);
        // Create the adapter and set it to the AutoCompleteTextView
        TextWatcher searchTw = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (!s.toString().equals("") && !searched_user_list.contains(s.toString())) {

                    if (Util.isOnline(_context)) {
                        String url = null;
                        try {
                            url = CWUrls.GROUPS_SEARCH + URLEncoder.encode(s.toString(), "UTF-8");
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }

                        Map<String, String> headers = new HashMap<>();
                        headers.put("x-email-id", CWIncturePreferences.getEmail());
                        headers.put("x-access-token",
                                CWIncturePreferences.getAccessToken());
                        if (s.length() >= 3) {
                            NetworkConnector connect = new NetworkConnector(_context, NetworkConnector.TYPE_GET, url, headers, null, PerformanceKRAAdapter.this);
                            connect.execute();
                        }
                    } else {
                        Toast.makeText(_context, "No Internet Connection,Try Later", Toast.LENGTH_SHORT).show();
                    }

                }


            }


        };
        actvRecos.addTextChangedListener(searchTw);
        actvRecos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                reviewerName = searched_id_list.get(i);
            }
        });


        /*actvRecos.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                    actvRecos.showDropDown();
            }
        });*/

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        tvSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!reviewerName.isEmpty()) {
                    submitForReview(kra_list.get(position).getKraId());
                } else {
                    Toast.makeText(_context, "Please select reviwer from list", Toast.LENGTH_LONG).show();
                }
            }
        });

        dialog.show();
    }

    private void showConfirmDeleteDialog(final int groupPosition) {

        final Dialog dialog = new Dialog(_context);
        dialog.setContentView(R.layout.dialog_leave_activity);

        TextView tvMessage = (TextView) dialog.findViewById(R.id.tvMessage);
        TextView tvCancel = (TextView) dialog.findViewById(R.id.tvCancel);
        TextView tvOk = (TextView) dialog.findViewById(R.id.tvOk);

        tvMessage.setText("Are you sure you want to delete this KRA?");
        tvOk.setText("Delete");

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        tvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                kra_list.remove(groupPosition);
                notifyDataSetChanged();
            }
        });

        dialog.show();
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View view, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.child_view_layout, null);

        TextView key_deliv_value = (TextView) view.findViewById(R.id.key_deliv_value);
        TextView low_value = (TextView) view.findViewById(R.id.low_value);
        TextView achievement_value = (TextView) view.findViewById(R.id.achievement_value);
        TextView high_value = (TextView) view.findViewById(R.id.high_value);

        for (int k = 0; k < kra_list.get(groupPosition).getKeyDeliverable().size(); k++) {
            key_deliv_value.setText("\n " + Html.fromHtml(kra_list.get(groupPosition).getKeyDeliverable().get(k)));
        }

        low_value.setText(Html.fromHtml(kra_list.get(groupPosition).getAchievementLow()));
        achievement_value.setText(Html.fromHtml(kra_list.get(groupPosition).getAchievementMax()));
        high_value.setText(Html.fromHtml(kra_list.get(groupPosition).getAchievementHigh()));


        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return false;
    }

    @Override
    public void processFinish(String output, int status_code, String url, int type) {
        if (url.contains(CWUrls.GROUPS_SEARCH)) {

            String status = "";
            String message = "";
            searched_user_list.clear();
            searched_id_list.clear();
            reviewerName = "";

            try {
                JSONObject main = new JSONObject(output);
                if (main.has("data")) {

                    JSONArray system_array = (JSONArray) main.opt("data");
                    if (system_array != null) {
                        for (int i = 0; i < system_array.length(); i++) {
                            Log.d("SystemArray", "" + system_array.length());
                            JSONObject obj = system_array.getJSONObject(i);
                            ContactsModel item = new ContactsModel();
                            item.setEmail(obj.optString("email"));
                            item.setFirstname(obj.optString("firstName"));
                            item.setLastname(obj.optString("lastName"));
                            item.setUserId(obj.optString("_id"));

                            searched_user_list.add(item.getFirstname() + " " + item.getLastname());
                            searched_id_list.add(item.getUserId());
                        }

                    }
                }
                if (main.has("status")) {
                    status = main.optString("status");
                }
                if (main.has("message")) {
                    message = main.optString("message");
                }
            } catch (Exception ex) {
                Log.d("EXCEPTION", ex.getMessage());
            }

            if (status.equals("success") && searched_user_list.size() > 0) {
                actvRecos.setAdapter(null);
                autoCompleteAdapter = new ArrayAdapter<String>(_context, android.R.layout.simple_list_item_1, searched_user_list);
                actvRecos.setAdapter(autoCompleteAdapter);
                actvRecos.showDropDown();
            } else {
                Toast.makeText(_context, message, Toast.LENGTH_LONG).show();
            }
        } else if (url.equals(CWUrls.GET_EBAT_TEAM_ASSESSMENT_DETAIL + assessmentId + "/requestRecommendation")) {

            String status = "";
            String message = "";


            try {
                JSONObject main = new JSONObject(output);

                if (main.has("status")) {
                    status = main.optString("status");
                }
                if (main.has("message")) {
                    message = main.optString("message");
                }
            } catch (Exception ex) {
                Log.d("EXCEPTION", ex.getMessage());
            }

            if (status.equals("success")) {
                Toast.makeText(_context, message, Toast.LENGTH_LONG).show();
                if (dialog != null) {
                    dialog.dismiss();
                }
            } else {
                Toast.makeText(_context, message, Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void configurationUpdated(boolean configUpdated) {

    }

    public void submitForReview(String kraID) {

        if (BasicActivity.isOnline(_context)) {
            String url = CWUrls.GET_EBAT_TEAM_ASSESSMENT_DETAIL + assessmentId + "/requestRecommendation";

            Map<String, String> headers = new HashMap<String, String>();
            headers.put("x-email-id", CWIncturePreferences.getEmail());
            headers.put("x-access-token",
                    CWIncturePreferences.getAccessToken());
            headers.put("role",
                    "employee");
            headers.put("Content-Type",
                    "application/json");

            JSONObject body;
            body = new JSONObject();

            try {

                body.put("kraId", kraID);

                body.put("requesterId", CWIncturePreferences.getEmail());
                JSONArray reviewerArr = new JSONArray();
                reviewerArr.put(reviewerName);
                body.put("reviewers", reviewerArr);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            NetworkConnector connect = new NetworkConnector(_context, NetworkConnector.TYPE_PUT, url, headers, body.toString(), this);
            connect.execute();

        } else {
            Toast.makeText(_context, "No Internet Connection,Try Later", Toast.LENGTH_SHORT).show();
        }
    }

}
