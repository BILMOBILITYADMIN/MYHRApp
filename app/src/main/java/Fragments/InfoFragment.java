package Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hrapps.AddUserDetail;
import com.hrapps.CSC_Britannia.BasicInfoModel;
import com.hrapps.CSC_Britannia.OfficialInfoAdapter;
import com.hrapps.CSC_Britannia.OfficialInfoModel;
import com.hrapps.EditExperienceCertActivity;
import com.hrapps.EditPersonalInfoActivity;
import com.hrapps.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import Model.UserProfile;
import Model.UserProfileDetail;
import Utility.AsyncResponse;
import Utility.CWIncturePreferences;
import Utility.CWUrls;
import Utility.NetworkConnector;
import Utility.Util;
import adapters.BasicinfoAdapter;
import adapters.UserProfileDetailAdapter;

/**
 * Created by harshu on 5/9/2016.
 */
public class InfoFragment extends Fragment implements AsyncResponse {

    private RecyclerView personal;
    private RecyclerView experience;
    private RecyclerView certifications;
    private CardView personal_card;
    private CardView experience_card;
    private CardView certification_card;
    ImageView personal_edit, exp_edit, cert_edit, add, addImage_cert;
    TextView add_exp, add_cert, experience_title, add_exp_new, add_cert_new, cert_title, no_experience, no_certifications;
    View line_exp, line_cert, experience_border, cert_border;

    String userId;
    String location, maritalStatus = "";
    String mobile_number = "";
    String email = "";

    UserProfile user = new UserProfile();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = LayoutInflater.from(getActivity()).inflate(R.layout.info_fragment, null);


        userId = getArguments().getString("userId");

        personal = (RecyclerView) v.findViewById(R.id.person_info_list);
        experience = (RecyclerView) v.findViewById(R.id.experience_list);
        certifications = (RecyclerView) v.findViewById(R.id.certification_list);
        personal_card = (CardView) v.findViewById(R.id.personal_card);
        experience_card = (CardView) v.findViewById(R.id.experience_card);
        certification_card = (CardView) v.findViewById(R.id.certification_card);
        personal_edit = (ImageView) v.findViewById(R.id.personal_edit);
        exp_edit = (ImageView) v.findViewById(R.id.experience_edit);
        cert_edit = (ImageView) v.findViewById(R.id.cert_edit);
        add_exp = (TextView) v.findViewById(R.id.add_exp);
        add_cert = (TextView) v.findViewById(R.id.add_cert);
        experience_title = (TextView) v.findViewById(R.id.experience_title);
        line_exp = (View) v.findViewById(R.id.line_exp);
        add_exp_new = (TextView) v.findViewById(R.id.add_exp_new);
        add = (ImageView) v.findViewById(R.id.add);
        addImage_cert = (ImageView) v.findViewById(R.id.addImage_cert);
        add_cert_new = (TextView) v.findViewById(R.id.add_cert_new);
        cert_title = (TextView) v.findViewById(R.id.cert_title);
        line_cert = (View) v.findViewById(R.id.line_cert);
        no_experience = (TextView) v.findViewById(R.id.no_experience);
        experience_border = (View) v.findViewById(R.id.experience_border);
        no_certifications = (TextView) v.findViewById(R.id.no_certifications);
        cert_border = (View) v.findViewById(R.id.cert_border);

        experience_card.setVisibility(View.GONE);
        certification_card.setVisibility(View.GONE);

        if (userId == null) {
            userId = CWIncturePreferences.getUserId();
        } else {
            cert_edit.setVisibility(View.GONE);
            personal_edit.setVisibility(View.GONE);
            exp_edit.setVisibility(View.GONE);
            add_exp.setVisibility(View.GONE);
            add_exp_new.setVisibility(View.GONE);
            add.setVisibility(View.GONE);
            add_cert.setVisibility(View.GONE);
            add_cert_new.setVisibility(View.GONE);
            addImage_cert.setVisibility(View.GONE);
        }


        LinearLayoutManager llm_personal = new org.solovyev.android.views.llm.LinearLayoutManager(getActivity());
        personal.setHasFixedSize(true);
        personal.setNestedScrollingEnabled(false);
        llm_personal.setOrientation(LinearLayoutManager.VERTICAL);
        personal.setLayoutManager(llm_personal);

        LinearLayoutManager llm_experience = new org.solovyev.android.views.llm.LinearLayoutManager(getActivity());
        experience.setHasFixedSize(true);
        experience.setNestedScrollingEnabled(false);
        llm_experience.setOrientation(LinearLayoutManager.VERTICAL);
        experience.setLayoutManager(llm_experience);

        LinearLayoutManager llm_certification = new org.solovyev.android.views.llm.LinearLayoutManager(getActivity());
        certifications.setHasFixedSize(true);
        certifications.setNestedScrollingEnabled(false);
        llm_certification.setOrientation(LinearLayoutManager.VERTICAL);
        certifications.setLayoutManager(llm_certification);


        personal_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent edit = new Intent(getActivity(), EditPersonalInfoActivity.class);
                edit.putExtra("Mobile", mobile_number);
                edit.putExtra("Email", email);
                edit.putExtra("Location", location);
                edit.putExtra("maritalStatus", maritalStatus);
                startActivityForResult(edit, 1);

            }
        });

        cert_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent edit = new Intent(getActivity(), EditExperienceCertActivity.class);
                edit.putExtra("Type", 1);
                edit.putExtra("Details", user.getCertificationDetails());
                startActivityForResult(edit, 2);
            }
        });

        exp_edit.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Intent edit = new Intent(getActivity(), EditExperienceCertActivity.class);
                edit.putExtra("Type", 0);
                edit.putExtra("Details", user.getExperienceDetails());
                startActivityForResult(edit, 3);


            }

        });

        add_exp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent edit = new Intent(getActivity(), AddUserDetail.class);
                edit.putExtra("Type", 0);
                startActivityForResult(edit, 4);
            }
        });

        add_exp_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent edit = new Intent(getActivity(), AddUserDetail.class);
                edit.putExtra("Type", 0);
                startActivityForResult(edit, 4);
            }
        });
        add_cert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent edit = new Intent(getActivity(), AddUserDetail.class);
                edit.putExtra("Type", 1);
                startActivityForResult(edit, 5);
            }
        });

        add_cert_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent edit = new Intent(getActivity(), AddUserDetail.class);
                edit.putExtra("Type", 1);
                startActivityForResult(edit, 5);
            }
        });

        if (Util.isOnline(getActivity())) {
            Map<String, String> headers = new HashMap<>();


            headers.put("x-email-id", CWIncturePreferences.getEmail());
            headers.put("x-access-token",
                    CWIncturePreferences.getAccessToken());
            NetworkConnector connect = new NetworkConnector(getActivity(), NetworkConnector.TYPE_GET, CWUrls.GET_USER_PROFILE + userId, headers, null, this);
            if (connect.isAllowed()) {
                connect.execute();
            } else {
                Toast.makeText(getActivity(), getString(R.string.action_not_allowed), Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getActivity(), getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
            getActivity().onBackPressed();
        }

        return v;

    }

    @Override
    public void processFinish(String output, int status_code, String url, int type) {

        ArrayList<BasicInfoModel> data_list = new ArrayList<>();
        ArrayList<OfficialInfoModel> officialinfoList = new ArrayList<>();
        try {
            JSONObject responseObj = new JSONObject(output);
            if (responseObj.getString("status").equalsIgnoreCase("success")) {
                if (type == NetworkConnector.TYPE_GET) {
                    JSONObject profile = new JSONObject(responseObj.getString("data"));
                    if (profile != null) {

                        BasicInfoModel basicInfoModel = new BasicInfoModel();

                        basicInfoModel.fname = profile.optString("firstName");
                        basicInfoModel.lname = profile.optString("lastName");
                        basicInfoModel.role = profile.optString("role");
                        basicInfoModel.email = profile.optString("email");

                        data_list.add(basicInfoModel);

                        JSONObject personalObj = profile.optJSONObject("officialInformation");
                        if (personalObj != null) {

                            OfficialInfoModel officialInfoModel = new OfficialInfoModel();

                            officialInfoModel.emp_id = personalObj.optString("id");
                            officialInfoModel.sap_id = personalObj.optString("sapId");
                            officialInfoModel.department = personalObj.optString("organizationalUnit");
                            officialInfoModel.designation = personalObj.optString("designation");

                            officialinfoList.add(officialInfoModel);


//                            user.getExperienceDetails().clear();
//                            Iterator<?> keys = personalObj.keys();
//                            while (keys.hasNext()) {
//                                String key = (String) keys.next();
//                                if (key.contains("mobile")) {
//                                    if (!personalObj.getString(key).equalsIgnoreCase("null")) {
//                                        mobile_number = personalObj.getString(key);
//                                        user.setPhone(mobile_number);
//                                    }
//                                } else if (key.contains("location")) {
//                                    location = personalObj.getString(key);
//                                } else if (key.contains("maritalStatus")) {
//                                    maritalStatus = personalObj.getString(key);
//                                }
//                                else if (key.contains("email")){
//                                    email = personalObj.getString(key);
//                                }
//
//                                UserProfileDetail personalDetails1 = new UserProfileDetail(key,personalObj.getString(key), "");
//                                user.getExperienceDetails().add(personalDetails1);
//                            }
                        }

                        JSONArray experienceArray = profile.optJSONArray("experience");
//                        if (experienceArray != null) {
//                            user.getExperienceDetails().clear();
//                            for (int i = 0; i < experienceArray.length(); i++) {
//                                JSONObject experienceObject = experienceArray.optJSONObject(i);
//                                if (experienceObject.optJSONObject("date") != null) {
//                                    UserProfileDetail expDetails = new UserProfileDetail(experienceObject.optString("designation"), experienceObject.optString("companyName"),
//                                            experienceObject.optJSONObject("date").optString("from") + " to " + experienceObject.optJSONObject("date").optString("to"));
//                                    expDetails.setWeb_link(experienceObject.optString("website"));
//                                    expDetails.setExp_start(experienceObject.optJSONObject("date").optString("from"));
//                                    expDetails.setExp_end(experienceObject.optJSONObject("date").optString("to"));
//                                    user.getExperienceDetails().add(expDetails);
//                                }
//
//                            }
//                        }

                        JSONArray certificationArray = profile.optJSONArray("certifications");
                        if (certificationArray != null) {
                            user.getCertificationDetails().clear();
                            for (int i = 0; i < certificationArray.length(); i++) {
                                JSONObject certificationObject = certificationArray.optJSONObject(i);
                                UserProfileDetail certDetails = new UserProfileDetail(certificationObject.optString("name"), certificationObject.optString("institution"), certificationObject.optString("date"));
                                user.getCertificationDetails().add(certDetails);

                            }
                        }

                        if (data_list.size() > 0) {
                            personal_card.setVisibility(View.VISIBLE);
                            personal.setAdapter(new BasicinfoAdapter(data_list));
                        } else {
                            personal_card.setVisibility(View.GONE);
                        }

                        if (officialinfoList.size() > 0) {

                            experience_card.setVisibility(View.VISIBLE);
                            experience.setAdapter(new OfficialInfoAdapter(officialinfoList));
                        } else {
                            experience_card.setVisibility(View.GONE);
                        }
//                        if (user.getPersonalDetails().size() > 0) {
//                            personal_card.setVisibility(View.VISIBLE);
//                            personal.setAdapter(new UserProfileDetailAdapter(user.getPersonalDetails()));
//                        } else {
//                            personal_card.setVisibility(View.GONE);
//                        }
//                        if (user.getExperienceDetails().size() > 0) {
//                            experience_card.setVisibility(View.VISIBLE);
//                            experience.setAdapter(new UserProfileDetailAdapter(user.getExperienceDetails()));
//                        }
//                        else if ((user.getExperienceDetails().size()== 0) && userId!=CWIncturePreferences.getUserId() ){
//                            exp_edit.setVisibility(View.GONE);
//                            experience_title.setVisibility(View.GONE);
//                            line_exp.setVisibility(View.GONE);
//                            add_exp_new.setVisibility(View.GONE);
//                            add.setVisibility(View.GONE);
//                            add_exp.setVisibility(View.GONE);
//                            no_experience.setVisibility(View.VISIBLE);
//                            experience_border.setVisibility(View.GONE);
//                        }
//                        else if ((user.getExperienceDetails().size()== 0) && userId==CWIncturePreferences.getUserId() ){
//                            exp_edit.setVisibility(View.GONE);
//                            experience_title.setVisibility(View.GONE);
//                            line_exp.setVisibility(View.GONE);
//                            add_exp.setVisibility(View.GONE);
//                            add_exp_new.setVisibility(View.VISIBLE);
//                            add.setVisibility(View.VISIBLE);
//                        }

                        if (user.getCertificationDetails().size() > 0) {
                            certification_card.setVisibility(View.VISIBLE);
                            certifications.setAdapter(new UserProfileDetailAdapter(user.getCertificationDetails()));
                        } else if ((user.getCertificationDetails().size() == 0) && userId != CWIncturePreferences.getUserId()) {
                            cert_edit.setVisibility(View.GONE);
                            cert_title.setVisibility(View.GONE);
                            line_cert.setVisibility(View.GONE);
                            add_cert.setVisibility(View.GONE);
                            add_cert_new.setVisibility(View.GONE);
                            addImage_cert.setVisibility(View.GONE);
                            no_certifications.setVisibility(View.VISIBLE);
                            cert_border.setVisibility(View.GONE);
                        } else if ((user.getCertificationDetails().size() == 0) && userId == CWIncturePreferences.getUserId()) {
                            cert_edit.setVisibility(View.GONE);
                            cert_title.setVisibility(View.GONE);
                            line_cert.setVisibility(View.GONE);
                            add_cert.setVisibility(View.GONE);
                            add_cert_new.setVisibility(View.VISIBLE);
                            addImage_cert.setVisibility(View.VISIBLE);
                        }


                    } else {
                        personal_card.setVisibility(View.GONE);
                        experience_card.setVisibility(View.GONE);
                        certification_card.setVisibility(View.GONE);
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //Personal data edited
        if (requestCode == 1 && resultCode == 200) {
            mobile_number = data.getStringExtra("Mobile");
            email = data.getStringExtra("Email");
            location = data.getStringExtra("Location");
            maritalStatus = data.getStringExtra("maritalStatus");
            user.getPersonalDetails().clear();

            UserProfileDetail personalDetails4 = new UserProfileDetail(maritalStatus, "maritalstatus", "");
            user.getPersonalDetails().add(personalDetails4);
            UserProfileDetail personalDetails1 = new UserProfileDetail(mobile_number, "mobile", "");
            user.getPersonalDetails().add(personalDetails1);
            UserProfileDetail personalDetails2 = new UserProfileDetail(email, "email", "");
            user.getPersonalDetails().add(personalDetails2);
            UserProfileDetail personalDetails3 = new UserProfileDetail(location, "location", "");
            user.getPersonalDetails().add(personalDetails3);

            personal.setAdapter(new UserProfileDetailAdapter(user.getPersonalDetails()));

        } else if (requestCode == 2 && resultCode == 200) {
            ArrayList<UserProfileDetail> certifications_list = (ArrayList<UserProfileDetail>) data.getSerializableExtra("Details");
            user.setCertificationDetails(certifications_list);
            if (user.getCertificationDetails().size() == 0) {
                cert_edit.setVisibility(View.GONE);
                cert_title.setVisibility(View.GONE);
                addImage_cert.setVisibility(View.VISIBLE);
                add_cert_new.setVisibility(View.VISIBLE);
                line_cert.setVisibility(View.GONE);
                add_cert.setVisibility(View.GONE);
            } else {
                cert_edit.setVisibility(View.VISIBLE);
                cert_title.setVisibility(View.VISIBLE);
                addImage_cert.setVisibility(View.GONE);
                add_cert_new.setVisibility(View.GONE);
                line_cert.setVisibility(View.VISIBLE);
                add_cert.setVisibility(View.VISIBLE);
            }
            certifications.setAdapter(new UserProfileDetailAdapter(user.getCertificationDetails()));

        } else if (requestCode == 3 && resultCode == 200) {
            ArrayList<UserProfileDetail> exp_list = (ArrayList<UserProfileDetail>) data.getSerializableExtra("Details");
            user.setExperienceDetails(exp_list);

            if (user.getExperienceDetails().size() == 0) {
                exp_edit.setVisibility(View.GONE);
                experience_title.setVisibility(View.GONE);
                line_exp.setVisibility(View.GONE);
                add_exp.setVisibility(View.GONE);
                add_exp_new.setVisibility(View.VISIBLE);
                add.setVisibility(View.VISIBLE);
            } else {
                exp_edit.setVisibility(View.VISIBLE);
                experience_title.setVisibility(View.VISIBLE);
                line_exp.setVisibility(View.VISIBLE);
                add_exp.setVisibility(View.VISIBLE);
                add_exp_new.setVisibility(View.GONE);
                add.setVisibility(View.GONE);
            }
            experience.setAdapter(new UserProfileDetailAdapter(user.getExperienceDetails()));
        }
        //add experience
        else if (requestCode == 4 && resultCode == 200) {
            UserProfileDetail detail = (UserProfileDetail) data.getSerializableExtra("Detail");
            user.getExperienceDetails().add(detail);
            if (user.getExperienceDetails().size() == 1) {
                experience.setAdapter(new UserProfileDetailAdapter(user.getExperienceDetails()));
                exp_edit.setVisibility(View.VISIBLE);
                experience_title.setVisibility(View.VISIBLE);
                line_exp.setVisibility(View.VISIBLE);
                add_exp.setVisibility(View.VISIBLE);
                add_exp_new.setVisibility(View.GONE);
                add.setVisibility(View.GONE);
            } else {
                experience.getAdapter().notifyDataSetChanged();
            }
            JSONArray array = new JSONArray();
            JSONObject request = new JSONObject();
            for (int i = 0; i < user.getExperienceDetails().size(); i++) {

                JSONObject exp = new JSONObject();

                try {
                    exp.put("designation", user.getExperienceDetails().get(i).getTitle());
                    exp.put("companyName", user.getExperienceDetails().get(i).getDetail1());
                    String[] dateSplit = user.getExperienceDetails().get(i).getDetail2().split(" to ");
                    if (dateSplit.length > 1) {
                        JSONObject date = new JSONObject();
                        date.put("from", dateSplit[0]);
                        date.put("to", dateSplit[1]);
                        exp.put("date", date);
                    }
                    array.put(exp);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            try {
                request.put("experience", array);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if (Util.isOnline(getActivity())) {

                Map<String, String> headers = new HashMap<>();

                headers.put("x-device-id",
                        CWIncturePreferences.getDeviceToken());

                headers.put("x-email-id", CWIncturePreferences.getEmail());
                headers.put("x-access-token",
                        CWIncturePreferences.getAccessToken());
                headers.put("Content-Type", "application/json");


                NetworkConnector connect = new NetworkConnector(getActivity(), NetworkConnector.TYPE_PUT, CWUrls.UPDATE_PROFILE + CWIncturePreferences.getUserId(), headers, request.toString(), this);
                if (connect.isAllowed()) {
                    connect.execute();
                } else {
                    Toast.makeText(getActivity(), getString(R.string.action_not_allowed), Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getActivity(), getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
            }

        }

        //add certification
        else if (requestCode == 5 && resultCode == 200) {
            UserProfileDetail detail = (UserProfileDetail) data.getSerializableExtra("Detail");
            user.getCertificationDetails().add(detail);
            if (user.getCertificationDetails().size() == 1) {
                certifications.setAdapter(new UserProfileDetailAdapter(user.getCertificationDetails()));
                cert_edit.setVisibility(View.VISIBLE);
                cert_title.setVisibility(View.VISIBLE);
                addImage_cert.setVisibility(View.GONE);
                add_cert_new.setVisibility(View.GONE);
                line_cert.setVisibility(View.VISIBLE);
                add_cert.setVisibility(View.VISIBLE);
            } else {
                certifications.getAdapter().notifyDataSetChanged();
            }
            JSONArray array = new JSONArray();
            JSONObject request = new JSONObject();
            for (int i = 0; i < user.getCertificationDetails().size(); i++) {
                JSONObject cert = new JSONObject();


                try {
                    cert.put("name", user.getCertificationDetails().get(i).getTitle());
                    cert.put("institution", user.getCertificationDetails().get(i).getDetail1());
                    cert.put("date", user.getCertificationDetails().get(i).getDetail2());
                    array.put(cert);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            try {
                request.put("certifications", array);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if (Util.isOnline(getActivity())) {
                Map<String, String> headers = new HashMap<>();

                headers.put("x-device-id",
                        CWIncturePreferences.getDeviceToken());

                headers.put("x-email-id", CWIncturePreferences.getEmail());
                headers.put("x-access-token",
                        CWIncturePreferences.getAccessToken());
                headers.put("Content-Type", "application/json");


                NetworkConnector connect = new NetworkConnector(getActivity(), NetworkConnector.TYPE_PUT, CWUrls.UPDATE_PROFILE + CWIncturePreferences.getUserId(), headers, request.toString(), this);
                if (connect.isAllowed()) {
                    connect.execute();
                } else {
                    Toast.makeText(getActivity(), getString(R.string.action_not_allowed), Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getActivity(), getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
            }

        }
    }

    @Override
    public void configurationUpdated(boolean configUpdated) {

    }
}
