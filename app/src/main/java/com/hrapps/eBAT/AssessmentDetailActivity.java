package com.hrapps.eBAT;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hrapps.BasicActivity;
import com.hrapps.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import Utility.AsyncResponse;
import Utility.CWIncturePreferences;
import Utility.CWUrls;
import Utility.Constants;
import Utility.NetworkConnector;

/**
 * Created by Harshitha.bshekharap on 11/16/2017.
 */

public class AssessmentDetailActivity extends AppCompatActivity implements AsyncResponse {
    private String id = "";
    ArrayList<KRAModel> kras = new ArrayList<>();
    TextView name, week, proj_name;
    TextView tvStatus;
    RelativeLayout statuslayout;
    LinearLayout buttonLayout;
    ImageView back;
    Button reject, accept, bSubmit;
    private boolean isSelf;
    private FloatingActionButton fabAddKra;

    PerformanceKRAAdapter performanceKRAAdapter;
    ExpandableListView explv_kras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_details);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.my_primary_dark));
        }
        if (getIntent().getExtras() != null) {
            id = getIntent().getStringExtra(Constants.ASSESSMENT_ID);
            isSelf = getIntent().getExtras().getBoolean("ISSELF", false);
        }
        name = (TextView) findViewById(R.id.name);
        week = (TextView) findViewById(R.id.week);
        proj_name = (TextView) findViewById(R.id.proj_name);
        tvStatus = (TextView) findViewById(R.id.status);
        statuslayout = (RelativeLayout) findViewById(R.id.statuslayout);
        buttonLayout = (LinearLayout) findViewById(R.id.buttonLayout);
        reject = (Button) findViewById(R.id.reject);
        accept = (Button) findViewById(R.id.accept);
        bSubmit = (Button) findViewById(R.id.bSubmit);
        back = (ImageView) findViewById(R.id.back);
        explv_kras = (ExpandableListView) findViewById(R.id.task_list);
        fabAddKra = (FloatingActionButton) findViewById(R.id.fabAddKra);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final AlertDialog.Builder reject_dialog = new AlertDialog.Builder(AssessmentDetailActivity.this);
                LayoutInflater reject_inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View reject_view = reject_inflater.inflate(R.layout.textfield, null);
                final EditText reject_reason = (EditText) reject_view.findViewById(R.id.text);
                final TextView confirm = (TextView) reject_view.findViewById(R.id.confirm);
                confirm.setText("APPROVE");
                final TextView cancel = (TextView) reject_view.findViewById(R.id.cancel_approve_reject);
                reject_dialog.setTitle("Approve KRA");
                reject_dialog.setView(reject_view);


                final AlertDialog reject_alertDialog = reject_dialog.create();
                // show it
                reject_alertDialog.show();

                confirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        approve(reject_reason.getText().toString());
                        reject_alertDialog.dismiss();
                    }
                });

                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        reject_alertDialog.dismiss();
                    }
                });
            }
        });
        reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder reject_dialog = new AlertDialog.Builder(AssessmentDetailActivity.this);
                LayoutInflater reject_inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View reject_view = reject_inflater.inflate(R.layout.textfield, null);
                final EditText reject_reason = (EditText) reject_view.findViewById(R.id.text);
                final TextView confirm = (TextView) reject_view.findViewById(R.id.confirm);
                confirm.setText("REJECT");
                final TextView cancel = (TextView) reject_view.findViewById(R.id.cancel_approve_reject);
                reject_dialog.setTitle("Reject KRA");
                reject_dialog.setView(reject_view);

                final AlertDialog reject_alertDialog = reject_dialog.create();
                // show it
                reject_alertDialog.show();

                confirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        reject(reject_reason.getText().toString());
                        reject_alertDialog.dismiss();
                    }
                });

                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        reject_alertDialog.dismiss();
                    }
                });
            }
        });

        getAssessmentDetails();
    }

    public void getAssessmentDetails() {
        if (BasicActivity.isOnline(this)) {
            String url = CWUrls.GET_EBAT_TEAM_ASSESSMENT_DETAIL;

            String role = "";
            if (isSelf) {
                role = "employee";
            } else {
                role = "manager";
            }
            Map<String, String> headers = new HashMap<String, String>();
            headers.put("x-email-id", CWIncturePreferences.getEmail());
            headers.put("x-access-token", CWIncturePreferences.getAccessToken());
            headers.put("role", role);

            NetworkConnector connect = new NetworkConnector(this, NetworkConnector.TYPE_GET, url + id, headers, null, this);
            connect.execute();

        } else {
            Toast.makeText(this, "No Internet Connection,Try Later", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void processFinish(String output, int status_code, String url, int type) {
        if (url.contains(CWUrls.EBAT_TEAM_ASSESSMENT_APPROVE)) {

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
                String msg = ex.getMessage();
            }

            if (status.equals("success")) {
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
                Intent data = new Intent();
                data.putExtra("ID", id);
                data.putExtra("status", Constants.APPROVED);
                setResult(RESULT_OK);

            } else {
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            }
        } else if (url.contains(CWUrls.EBAT_TEAM_ASSESSMENT_REJECT)) {

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
                String msg = ex.getMessage();
            }

            if (status.equals("success")) {
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
                Intent data = new Intent();
                data.putExtra("ID", id);
                data.putExtra("status", Constants.PENDING_REWORK);
                setResult(RESULT_OK);

            } else {
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            }
        } else if (url.equals(CWUrls.GET_EBAT_TEAM_ASSESSMENT_DETAIL + id + "/submitKRA")) {

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
                String msg = ex.getMessage();
            }

            if (status.equals("success")) {
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
                onBackPressed();

            } else {
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            }
        } else if (url.contains(CWUrls.GET_EBAT_TEAM_ASSESSMENT_DETAIL)) {

            String status = "";
            String message = "";
            String statusText = "";

            try {
                JSONObject main = new JSONObject(output);
                if (main.has("data")) {

                    JSONObject obj = main.optJSONObject("data");

                    JSONObject appraiseeObj = obj.optJSONObject("appraisee");
                    name.setText(appraiseeObj.optString("displayName"));
                    week.setText(appraiseeObj.optString("email"));
                    proj_name.setText(appraiseeObj.optString("designation"));
                    statusText = obj.optString("status");
                    tvStatus.setText(statusText);
                    statuslayout.setVisibility(View.VISIBLE);
                    if (statusText.equalsIgnoreCase(Constants.PENDING_REWORK)) {
                        statuslayout.setBackgroundColor(getResources().getColor(R.color.taskRed));

                    } else if (statusText.equalsIgnoreCase(Constants.MATRIX_MANAGER_APPROVAL_PENDING) || statusText.equalsIgnoreCase(Constants.MANAGER_APPROVAL_PENDING)) {
                        statuslayout.setBackgroundColor(getResources().getColor(R.color.taskYellow));

                    } else if (statusText.equalsIgnoreCase(Constants.APPROVED)) {
                        statuslayout.setBackgroundColor(getResources().getColor(R.color.taskGreen));

                    }

                    if (isSelf && (statusText.equalsIgnoreCase(Constants.PENDING_REWORK) || statusText.equalsIgnoreCase(Constants.PENDING_FOR_INPUT))) {
                        buttonLayout.setVisibility(View.VISIBLE);
                        accept.setVisibility(View.GONE);
                        reject.setVisibility(View.GONE);
                        bSubmit.setVisibility(View.VISIBLE);
                        fabAddKra.setVisibility(View.VISIBLE);
                        bSubmit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                submit();
                            }
                        });
                        fabAddKra.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent editKRAActivity = new Intent(AssessmentDetailActivity.this, AddKraActivity.class);
                                editKRAActivity.putExtra(Constants.ASSESSMENT_ID, id);
                                startActivityForResult(editKRAActivity, Constants.EDIT_KRA);
                            }
                        });


                    } else if (!isSelf) {
                        if (statusText.equalsIgnoreCase(Constants.PENDING_REWORK)) {
                            buttonLayout.setVisibility(View.GONE);
                        } else if (statusText.equalsIgnoreCase(Constants.MATRIX_MANAGER_APPROVAL_PENDING) || statusText.equalsIgnoreCase(Constants.MANAGER_APPROVAL_PENDING)) {
                            buttonLayout.setVisibility(View.VISIBLE);
                        } else if (statusText.equalsIgnoreCase(Constants.APPROVED)) {
                            buttonLayout.setVisibility(View.GONE);
                        }
                    }
                    JSONObject appraisalData = obj.optJSONObject("appraisalData");
                    JSONArray kraArr = appraisalData.optJSONArray("kras");
                    if (kraArr != null && kraArr.length() > 0) {
                        for (int count = 0; count < kraArr.length(); count++) {
                            JSONObject kraObj = kraArr.getJSONObject(count);
                            KRAModel kra = new KRAModel();
                            kra.setKraId(kraObj.optString("_id"));
                            kra.setGoalName(kraObj.optString("name"));
                            kra.setWeightage(kraObj.optString("weightage"));
                            JSONArray deliverableArr = kraObj.optJSONArray("keyDeliverables");
                            if (deliverableArr != null && deliverableArr.length() > 0) {
                                for (int del = 0; del < deliverableArr.length(); del++) {
                                    kra.getKeyDeliverable().add(deliverableArr.getString(del));
                                }
                            }
                            JSONObject achievementMetrics = kraObj.optJSONObject("achievementMetrics");
                            kra.setAchievementLow(achievementMetrics.optString("low"));
                            kra.setAchievementMax(achievementMetrics.optString("medium"));
                            kra.setAchievementHigh(achievementMetrics.optString("high"));
                            kras.add(kra);

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
                String msg = ex.getMessage();
            }

            if (status.equals("success")) {
                performanceKRAAdapter = new PerformanceKRAAdapter(AssessmentDetailActivity.this, kras, explv_kras, isSelf, id, statusText);
                explv_kras.setAdapter(performanceKRAAdapter);
            } else {
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            }
        }


    }

    @Override
    public void configurationUpdated(boolean configUpdated) {

    }

    public void approve(String comment) {
        if (BasicActivity.isOnline(this)) {
            String url = CWUrls.EBAT_TEAM_ASSESSMENT_APPROVE;

            Map<String, String> headers = new HashMap<String, String>();
            headers.put("x-email-id", CWIncturePreferences.getEmail());
            headers.put("x-access-token",
                    CWIncturePreferences.getAccessToken());
            headers.put("role",
                    "manager");
            headers.put("Content-Type",
                    "application/json");

            JSONObject body;

            body = new JSONObject();
            try {
                body.put("appraisalId", id);
                body.put("managerComment", comment);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            NetworkConnector connect = new NetworkConnector(this, NetworkConnector.TYPE_PUT, url, headers, body.toString(), this);
            connect.execute();

        } else {
            Toast.makeText(this, "No Internet Connection,Try Later", Toast.LENGTH_SHORT).show();
        }
    }

    public void reject(String comment) {
        if (BasicActivity.isOnline(this)) {
            String url = CWUrls.EBAT_TEAM_ASSESSMENT_REJECT;

            Map<String, String> headers = new HashMap<String, String>();
            headers.put("x-email-id", CWIncturePreferences.getEmail());
            headers.put("x-access-token",
                    CWIncturePreferences.getAccessToken());
            headers.put("role",
                    "manager");
            headers.put("Content-Type",
                    "application/json");

            JSONObject body;

            body = new JSONObject();
            JSONArray ids = new JSONArray();
            ids.put(id);
            try {
                body.put("appraisalIds", ids);
                body.put("managerComment", comment);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            NetworkConnector connect = new NetworkConnector(this, NetworkConnector.TYPE_PUT, url, headers, body.toString(), this);
            connect.execute();

        } else {
            Toast.makeText(this, "No Internet Connection,Try Later", Toast.LENGTH_SHORT).show();
        }
    }

    public void submit() {
        if (BasicActivity.isOnline(this)) {
            String url = CWUrls.GET_EBAT_TEAM_ASSESSMENT_DETAIL + id + "/submitKRA";

            Map<String, String> headers = new HashMap<String, String>();
            headers.put("x-email-id", CWIncturePreferences.getEmail());
            headers.put("x-access-token",
                    CWIncturePreferences.getAccessToken());
            headers.put("role",
                    "manager");
            headers.put("Content-Type",
                    "application/json");


            NetworkConnector connect = new NetworkConnector(this, NetworkConnector.TYPE_POST, url, headers, null, this);
            connect.execute();

        } else {
            Toast.makeText(this, "No Internet Connection,Try Later", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == Constants.EDIT_KRA) {
            if (data != null) {
                String id = data.getStringExtra("KRA_ID");
                KRAModel kra = (KRAModel) data.getSerializableExtra("KRA_MODEL");
                if (id != null) {
                    for (int item = 0; item < kras.size(); item++) {
                        if (kras.get(item).getKraId().equals(id)) {
                            kras.remove(item);
                            kras.add(item, kra);
                            break;
                        }
                    }
                } else {
                    kras.add(kra);
                }
                performanceKRAAdapter.notifyDataSetChanged();

            }
        }
    }
}

