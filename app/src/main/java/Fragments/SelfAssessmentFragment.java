package Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.hrapps.BasicActivity;
import com.hrapps.R;
import com.hrapps.eBAT.AssessmentAdapter;
import com.hrapps.eBAT.AssessmentModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Utility.AsyncResponse;
import Utility.CWIncturePreferences;
import Utility.CWUrls;
import Utility.NetworkConnector;
import Utility.TimeUtils;

/**
 * Created by Harshitha.bshekharap on 11/16/2017.
 */

public class SelfAssessmentFragment extends Fragment implements AsyncResponse {

    private List<AssessmentModel> employeeList = new ArrayList<>();
    private RecyclerView rvAppraisalInfo;
    private AssessmentAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.frag_self_assessment, container, false);

        rvAppraisalInfo = (RecyclerView) rootView.findViewById(R.id.rvSelfAssessmentInfo);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        rvAppraisalInfo.setLayoutManager(mLayoutManager);
        rvAppraisalInfo.setItemAnimator(new DefaultItemAnimator());
        getSelfAssessment();
        return rootView;
    }

    public void getSelfAssessment() {
        if (BasicActivity.isOnline(getActivity())) {
            String url = CWUrls.BASE_URL_CSC + "api/v1/performance/ebatAppraisals";

            Map<String, String> headers = new HashMap<String, String>();
            headers.put("x-email-id", CWIncturePreferences.getEmail());
            headers.put("x-access-token",
                    CWIncturePreferences.getAccessToken());
            headers.put("role",
                    "employee");

            NetworkConnector connect = new NetworkConnector(getActivity(), NetworkConnector.TYPE_GET, url, headers, null, this);
            connect.execute();

        } else {
            Toast.makeText(getActivity(), "No Internet Connection,Try Later", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void processFinish(String output, int status_code, String url, int type) {
        if (url.contains(CWUrls.BASE_URL_CSC + "api/v1/performance/ebatAppraisals")) {

            String status = "";
            String message = "";

            try {
                JSONObject main = new JSONObject(output);
                if (main.has("data")) {

                    JSONArray system_array = (JSONArray) main.opt("data");
                    if (system_array != null) {
                        for (int i = 0; i < system_array.length(); i++) {
                            JSONObject obj = system_array.getJSONObject(i);

                            AssessmentModel assessment = new AssessmentModel();
                            assessment.setId(obj.optString("_id"));
                            assessment.getAppraisee().setName(obj.optString("name"));
                            assessment.setCurrentStage(obj.optString("currentStage"));
                            assessment.setStatus(obj.optString("status"));
                            JSONArray stagesArr = obj.optJSONArray("stages");
                            JSONObject evalObj = obj.optJSONObject("evaluationPeriod");
                            String from = TimeUtils.getDateForAssessmentYear(evalObj.optString("from"));
                            String to = TimeUtils.getDateForAssessmentYear(evalObj.optString("to"));
                            assessment.setAssessmentYear(from + " - " + to);
                            if (stagesArr != null && stagesArr.length() > 0) {
                                JSONObject firstStage = stagesArr.getJSONObject(0);
                                assessment.setSubmittedDate(firstStage.optString("updatedAt"));
                            }
                            employeeList.add(assessment);

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
                mAdapter = new AssessmentAdapter(getActivity(), employeeList, true);

                rvAppraisalInfo.setAdapter(mAdapter);
            } else {
                Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void configurationUpdated(boolean configUpdated) {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //   super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == getActivity().RESULT_OK && requestCode == 111) {

            if (data != null) {
                String id = data.getStringExtra("ID");
                String status = data.getStringExtra("status");
                for (int item = 0; item < employeeList.size(); item++) {
                    if (employeeList.get(item).getId().equals(id)) {
                        employeeList.get(item).setStatus(status);
                        break;
                    }
                }
                mAdapter.notifyDataSetChanged();

            }
        }
    }
}
