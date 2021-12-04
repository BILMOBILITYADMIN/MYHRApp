package com.hrapps.eBAT;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TableRow;
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

public class AddKraActivity extends AppCompatActivity implements View.OnClickListener, AsyncResponse {

    private ImageView ivBack;
    private Button bSave, bCancel;
    private EditText etGoalName, etWeightage, etKeyDeliverables, etLow, etFullAchievement, etHigh,
            etKeyDeliverables1, etKeyDeliverables2, etKeyDeliverables3;
    private ImageView ivAddKD, ivDelete1, ivDelete2, ivDelete3;
    private TableRow tr1, tr2, tr3;

    private KRAModel kraModel;
    private String assessmentId;
    private ArrayList<TableRow> trKDList = new ArrayList<>(3);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_kra);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.my_primary_dark));
        }

        ivBack = (ImageView) findViewById(R.id.ivBack);
        bSave = (Button) findViewById(R.id.bSave);
        bCancel = (Button) findViewById(R.id.bCancel);
        etGoalName = (EditText) findViewById(R.id.etGoalName);
        etWeightage = (EditText) findViewById(R.id.etWeightage);
        etKeyDeliverables = (EditText) findViewById(R.id.etKeyDeliverables);
        etLow = (EditText) findViewById(R.id.etLow);
        etFullAchievement = (EditText) findViewById(R.id.etFullAchievement);
        etHigh = (EditText) findViewById(R.id.etHigh);

        tr1 = (TableRow) findViewById(R.id.tr1);
        tr2 = (TableRow) findViewById(R.id.tr2);
        tr3 = (TableRow) findViewById(R.id.tr3);
        ivAddKD = (ImageView) findViewById(R.id.ivAddKD);
        ivDelete1 = (ImageView) findViewById(R.id.ivDelete1);
        ivDelete2 = (ImageView) findViewById(R.id.ivDelete2);
        ivDelete3 = (ImageView) findViewById(R.id.ivDelete3);
        etKeyDeliverables1 = (EditText) findViewById(R.id.etKeyDeliverables1);
        etKeyDeliverables2 = (EditText) findViewById(R.id.etKeyDeliverables2);
        etKeyDeliverables3 = (EditText) findViewById(R.id.etKeyDeliverables3);

        trKDList.add(tr1);
        trKDList.add(tr2);
        trKDList.add(tr3);

        if (getIntent() != null) {
            kraModel = (KRAModel) getIntent().getSerializableExtra(Constants.KRA_MODEL);
            assessmentId = getIntent().getStringExtra(Constants.ASSESSMENT_ID);
            if (kraModel != null) {
                setItems();
            }
        }

        ivBack.setOnClickListener(this);
        bSave.setOnClickListener(this);
        bCancel.setOnClickListener(this);

        ivAddKD.setOnClickListener(this);
        ivDelete1.setOnClickListener(this);
        ivDelete2.setOnClickListener(this);
        ivDelete3.setOnClickListener(this);
    }

    private void setItems() {
        etGoalName.setText(kraModel.getGoalName());
        etWeightage.setText(kraModel.getWeightage());

        ArrayList<String> keyDeliverables = kraModel.getKeyDeliverable();
        for (int kx = 1; kx < keyDeliverables.size(); kx++) {
            if (kx < 4) {
                trKDList.get(kx).setVisibility(View.VISIBLE);
                ((EditText) ((TextInputLayout) ((TableRow) trKDList.get(kx)).getChildAt(0)).getChildAt(0)).setText(keyDeliverables.get(kx));
            } else {
                break;
            }
        }
        etKeyDeliverables.setText(Html.fromHtml(keyDeliverables.get(0)));
        etLow.setText(Html.fromHtml(kraModel.getAchievementLow()));
        etFullAchievement.setText(Html.fromHtml(kraModel.getAchievementMax()));
        etHigh.setText(Html.fromHtml(kraModel.getAchievementHigh()));
        if (kraModel != null) {
            bSave.setText("Update");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivBack: {
                onBackPressed();
            }
            break;
            case R.id.bSave: {
                if (!etGoalName.getText().toString().isEmpty() && !etWeightage.getText().toString().isEmpty() && !etKeyDeliverables.getText().toString().isEmpty() && !etLow.getText().toString().isEmpty() && !etFullAchievement.getText().toString().isEmpty() && !etHigh.getText().toString().isEmpty()) {
                    if (kraModel != null)
                        addEditKra(kraModel.getKraId());
                    else
                        addEditKra("");
                } else {
                    Toast.makeText(this, "Please fill in all the fields", Toast.LENGTH_SHORT).show();
                }
            }
            break;
            case R.id.bCancel: {
                onBackPressed();
            }
            break;
            case R.id.ivAddKD: {
                for (int tx = 0; tx < trKDList.size(); tx++) {
                    if (trKDList.get(tx).getVisibility() == View.GONE) {
                        trKDList.get(tx).setVisibility(View.VISIBLE);
                        break;
                    }
                }
            }
            break;
            case R.id.ivDelete1: {
                tr1.setVisibility(View.GONE);
                etKeyDeliverables1.setText("");
            }
            break;
            case R.id.ivDelete2: {
                tr2.setVisibility(View.GONE);
                etKeyDeliverables2.setText("");
            }
            break;
            case R.id.ivDelete3: {
                tr3.setVisibility(View.GONE);
                etKeyDeliverables3.setText("");
            }
            break;
        }
    }

  /*  @Override
    public void onBackPressed() {
        final Dialog dialog = new Dialog(AddKraActivity.this);
        dialog.setContentView(R.layout.dialog_leave_activity);

        TextView tvCancel = (TextView) dialog.findViewById(R.id.tvCancel);
        TextView tvOk = (TextView) dialog.findViewById(R.id.tvOk);

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        tvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddKraActivity.super.onBackPressed();
            }
        });

        dialog.show();
    }*/

    private void addEditKra(String kraId) {
        if (BasicActivity.isOnline(this)) {
            String url = CWUrls.GET_EBAT_TEAM_ASSESSMENT_DETAIL + assessmentId + "/kra";

            Map<String, String> headers = new HashMap<String, String>();
            headers.put("x-email-id", CWIncturePreferences.getEmail());
            headers.put("x-access-token",
                    CWIncturePreferences.getAccessToken());
            headers.put("role",
                    "employee");
            headers.put("Content-Type",
                    "application/json");

            JSONObject body;
            JSONObject mainObj = new JSONObject();
            body = new JSONObject();

            try {
                if (!kraId.isEmpty()) {
                    body.put("_id", kraId);
                }
                body.put("name", etGoalName.getText().toString());
                body.put("weightage", etWeightage.getText().toString());
                JSONArray deliverableArr = new JSONArray();
                deliverableArr.put(etKeyDeliverables.getText().toString());
                JSONObject achievementMet = new JSONObject();
                achievementMet.put("low", etLow.getText().toString());
                achievementMet.put("medium", etFullAchievement.getText().toString());
                achievementMet.put("high", etHigh.getText().toString());
                body.put("achievementMetrics", achievementMet);
                body.put("keyDeliverables", deliverableArr);
                mainObj.put("kra", body);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            NetworkConnector connect = new NetworkConnector(this, NetworkConnector.TYPE_POST, url, headers, mainObj.toString(), this);
            connect.execute();

        } else {
            Toast.makeText(this, "No Internet Connection,Try Later", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void processFinish(String output, int status_code, String url, int type) {
        if (url.equals(CWUrls.GET_EBAT_TEAM_ASSESSMENT_DETAIL + assessmentId + "/kra")) {

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
                if (kraModel != null) {
                    data.putExtra("KRA_ID", kraModel.getKraId());
                } else {
                    kraModel = new KRAModel();
                }
                kraModel.setGoalName(etGoalName.getText().toString());
                kraModel.setWeightage(etWeightage.getText().toString());
                kraModel.getKeyDeliverable().clear();
                kraModel.getKeyDeliverable().add(etKeyDeliverables.getText().toString());
                kraModel.setAchievementLow(etLow.getText().toString());
                kraModel.setAchievementMax(etFullAchievement.getText().toString());
                kraModel.setAchievementHigh(etHigh.getText().toString());

                data.putExtra("KRA_MODEL", kraModel);

                setResult(RESULT_OK, data);
                onBackPressed();

            } else {
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            }
        }

    }

    @Override
    public void configurationUpdated(boolean configUpdated) {

    }
}

