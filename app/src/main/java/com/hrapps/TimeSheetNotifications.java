package com.hrapps;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.hrapps.CSC_Britannia.AppraisalProcessDetail;
import com.hrapps.CSC_Britannia.TemplateDetail;
import com.hrapps.Leave_Britannia.LeaveDetail;
import com.hrapps.Recruitment_Britannia.RecruitmentDetail;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import DB.DbAdapter;
import DB.DbUtil;
import Model.NotificationsModel;
import Utility.AsyncResponse;
import Utility.CWIncturePreferences;
import Utility.CWUrls;
import Utility.NetworkConnector;
import Utility.Util;
import adapters.TimesheetNotificationsAdapter;


/**
 * Created by Ar on 08-03-2016.
 */
public class TimeSheetNotifications extends BasicActivity implements AsyncResponse {

    TimesheetNotificationsAdapter adapter;
    ListView lv;
    TextView noNotifications;
    ArrayList<NotificationsModel> notifications_list = new ArrayList<>();
    NotificationsModel notificationsModel;
    String message = "", status_code = "", response = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.timesheet_notifications);
        ImageView back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.my_primary_dark));

        }

        lv = (ListView) findViewById(R.id.notificationlist);
        noNotifications = (TextView) findViewById(R.id.nonotifications);

        if (Util.isOnline(this)) {
            Map<String, String> headers = new HashMap<>();
            headers.put("x-email-id", CWIncturePreferences.getEmail());
            headers.put("x-access-token",
                    CWIncturePreferences.getAccessToken());

            try {
                headers.put("android-version", getPackageManager().getPackageInfo(getPackageName(), 0).versionName);
            } catch (PackageManager.NameNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            Log.d("headers=", "" + headers.size());
            NetworkConnector connect = new NetworkConnector(TimeSheetNotifications.this, NetworkConnector.TYPE_GET, CWUrls.NOTIFICATIONS, headers, null, TimeSheetNotifications.this);
            connect.execute();
        } else {
            DbUtil db = new DbUtil();
            response = db.getInAppNotifications();
            String actualString = response;
            if (response.startsWith("null")) {
                actualString = response.substring(4);
            }
            parse(actualString);
        }
    }

    @Override
    public void processFinish(String output, int status_code, String url, int type) {

        if (url.equalsIgnoreCase(CWUrls.NOTIFICATIONS) && type == NetworkConnector.TYPE_GET) {
            notifications_list.clear();
            DbAdapter.getDbAdapterInstance().delete(DbUtil.NOTIFICATIONS_TABLE_NAME);
            DbUtil db = new DbUtil();
            db.addInAppNotifications(output);

            String actualString = output;
            if (output.startsWith("null")) {
                actualString = output.substring(4);
            }
            parse(actualString);

        } else if (url.contains(CWUrls.NOTIFICATION_STATUS_UPDATE) && type == NetworkConnector.TYPE_PUT) {
            try {
                JSONObject object = new JSONObject(output);
                if (object.getString("status").equalsIgnoreCase("success")) {
                    ((CherryworkApplication) getApplicationContext()).notifiationRemoved();

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }//end of elseif

    }

    public void parse(String output) {

        try {

            JSONObject notifications = new JSONObject(output);
            if (notifications.getString("status").equalsIgnoreCase("success")) {

                message = notifications.optString("message");
                status_code = notifications.optString("status_code");
                JSONObject data = (JSONObject) notifications.optJSONObject("data");
                JSONArray notificationsArray = data.optJSONArray("notifications");
                if (notificationsArray != null && notificationsArray.length() > 0) {
                    for (int n = 0; n < notificationsArray.length(); n++) {

                        notificationsModel = new NotificationsModel();
                        notificationsModel.badgeCount = data.optString("badgeCount");
                        JSONObject nObj = notificationsArray.optJSONObject(n);

                        notificationsModel.id = nObj.optString("_id");

                        if (nObj.has("cherryName")) {
                            notificationsModel.notification_cherryName = nObj.optString("cherryName");
                        }


                        JSONObject contentobj = nObj.optJSONObject("content");
                        if (contentobj.has("timesheet")) {
                            JSONObject timesheet = contentobj.optJSONObject("timesheet");

                            if (timesheet != null) {
                                notificationsModel.timesheetStartDate = timesheet.optString("startDate");
                                notificationsModel.timesheetendDate = timesheet.optString("endDate");
                                notificationsModel.timesheetStatus = timesheet.optString("status");
                                notificationsModel.timesheetId = timesheet.optString("_id");
                                notificationsModel.workitemId = timesheet.optString("workitemId");
                            }

                        }//end of timesheet

                        if (contentobj.has("leaveProcess")) {
                            JSONObject leave = contentobj.optJSONObject("leaveProcess");

                            if (leave != null) {
                                notificationsModel.leave_id = leave.optString("leaveID");
                                notificationsModel.leave_evaluation_period = leave.optString("evaluationPeriod");
                                notificationsModel.leave_ep_from = leave.optString("from");
                                notificationsModel.leave_ep_to = leave.optString("to");
                                notificationsModel.leave_name = leave.optString("name");
                            }
                        }//end of leave

                        if (contentobj.has("workitem")) {
                            JSONObject workitem = contentobj.optJSONObject("workitem");

                            if (workitem != null) {
                                notificationsModel.workitem_id = workitem.optString("_id");
                                notificationsModel.workitem_status = workitem.optString("status");
                                notificationsModel.timesheetStartDate = workitem.optString("startDate");
                                notificationsModel.timesheetendDate = workitem.optString("endDate");
                            }
                        }//end of workitem

                        if (contentobj.has("appraisalTemplate")) {

                            JSONObject appraisalTemplate = contentobj.optJSONObject("appraisalTemplate");

                            if (appraisalTemplate != null) {
                                notificationsModel.appraisalTemplate_id = appraisalTemplate.optString("_id");
                                notificationsModel.appraisalTemplate_type = appraisalTemplate.optString("type");
                                notificationsModel.appraisalTemplate_workitemId = appraisalTemplate.optString("workitemId");
                                notificationsModel.appraisal_status = appraisalTemplate.optString("status");

                                JSONObject validity = appraisalTemplate.optJSONObject("evaluationPeriod");

                                if (validity != null) {
                                    notificationsModel.validity_from = validity.optString("from");
                                    notificationsModel.validity_to = validity.optString("to");

                                }
                            }

                        }

                        JSONObject appraisalProcess = null;
                        if (contentobj.has("appraisalProcess")) {
                            appraisalProcess = contentobj.optJSONObject("appraisalProcess");

                            if (appraisalProcess != null) {
                                notificationsModel.appraisalProcess_id = appraisalProcess.optString("templateId");
                                notificationsModel.appraisalProcess_type = appraisalProcess.optString("type");
                                notificationsModel.appraisalProcess_stage = appraisalProcess.optString("currentStage");
                                notificationsModel.appraisalname = appraisalProcess.optString("name");

                                JSONObject validity = appraisalProcess.optJSONObject("evaluationPeriod");

                                if (validity != null) {

                                    notificationsModel.validity_from = validity.optString("from");
                                    notificationsModel.validity_to = validity.optString("to");

                                }
                            }
                        }//end of appraisal process


                        if (contentobj.has("onBoardingProcess")) {
                            JSONObject onBoardingProcess = contentobj.optJSONObject("onBoardingProcess");


                            notificationsModel.onBoarding_formId = onBoardingProcess.optString("formId");
                            notificationsModel.onBoarding_evaluationPeriod = onBoardingProcess.optString("evaluationPeriod");
                            notificationsModel.onBoarding_currentStage = onBoardingProcess.optString("currentStage");
                            notificationsModel.onBoarding_name = onBoardingProcess.optString("name");
                            notificationsModel.onBoarding_type = onBoardingProcess.optString("type");

                        }//end of onboarding process


                        if (contentobj.has("requisition")) {

                            JSONObject requisition = contentobj.optJSONObject("requisition");
                            if (requisition != null) {
                                notificationsModel.requisitionId = requisition.optString("requisitionId");
                                notificationsModel.positionName = requisition.optString("positionName");
                                notificationsModel.workLocation = requisition.optString("location");
                                notificationsModel.requisition_workitemId = requisition.optString("workitemId");
                            }
                        }//end of requisition

                        if (contentobj.has("updateRequisition")) {
                            JSONObject updateRequisition = contentobj.optJSONObject("updateRequisition");
                            if (updateRequisition != null) {
                                notificationsModel.requisitionId = updateRequisition.optString("requisitionId");
                                notificationsModel.positionName = updateRequisition.optString("positionName");
                                notificationsModel.workLocation = updateRequisition.optString("location");
                                notificationsModel.requisition_workitemId = updateRequisition.optString("workitemId");
                                notificationsModel.requisition_type = updateRequisition.optString("type");

                            }
                        }//end of update requisition

                        if (contentobj.has("generateOffer")) {
                            JSONObject generateOffer = contentobj.optJSONObject("generateOffer");
                            if (generateOffer != null) {
                                notificationsModel.applicationId = generateOffer.optString("applicationId");
                                notificationsModel.evaluationId = generateOffer.optString("evaluationId");
                                notificationsModel.requisitionId = generateOffer.optString("requisitionId");
                                notificationsModel.requisition_workitemId = generateOffer.optString("workitemId");
                                notificationsModel.offerId = generateOffer.optString("offerId");
                            }
                        }//end of generateOffer

                        if (contentobj.has("interviewFeedback")) {
                            JSONObject interviewFeedback = contentobj.optJSONObject("interviewFeedback");
                            if (interviewFeedback != null) {
                                notificationsModel.applicationId = interviewFeedback.optString("applicationId");
                                notificationsModel.evaluationId = interviewFeedback.optString("evaluationId");
                                notificationsModel.requisitionId = interviewFeedback.optString("requisitionId");

                                try {
                                    if (interviewFeedback.has("candidate")) {
                                        JSONObject candidateObject = interviewFeedback.optJSONObject("candidate");
                                        if (candidateObject != null) {
                                            notificationsModel.candidate_email = candidateObject.optString("emailId");
                                        }

                                    }
                                } catch (Exception e) {
                                }
                            }
                        }//end of interviewFeedback

                        if (contentobj.has("profileEvaluation")) {
                            JSONObject profileEvaluation = contentobj.optJSONObject("profileEvaluation");
                            if (profileEvaluation != null) {
                                notificationsModel.applicationId = profileEvaluation.optString("applicationId");
                                notificationsModel.requisitionId = profileEvaluation.optString("requisitionId");
                                notificationsModel.requisition_workitemId = profileEvaluation.optString("workitemId");


                                if (profileEvaluation.has("candidate")) {
                                    JSONObject candidate = profileEvaluation.optJSONObject("candidate");
                                    if (candidate != null) {
                                        notificationsModel.candidate_email = candidate.optString("emailId");
                                    }
                                }
                            }

                        }//end of profileEvaluation

                        if (contentobj.has("applicationEvaluation")) {
                            JSONObject applicationEvaluation = contentobj.optJSONObject("applicationEvaluation");
                            if (applicationEvaluation != null) {
                                notificationsModel.applicationId = applicationEvaluation.optString("applicationId");
                                notificationsModel.requisitionId = applicationEvaluation.optString("requisitionId");

                                if (applicationEvaluation.has("candidate")) {
                                    JSONObject candidate = applicationEvaluation.optJSONObject("candidate");
                                    if (candidate != null) {
                                        notificationsModel.candidate_email = candidate.optString("emailId");
                                    }
                                }
                            }

                        }//end of applicationEvaluation


                        if (contentobj.has("offerRollout")) {
                            JSONObject offerRollout = contentobj.optJSONObject("offerRollout");
                            if (offerRollout != null) {
                                notificationsModel.applicationId = offerRollout.optString("applicationId");
                                notificationsModel.requisitionId = offerRollout.optString("requisitionId");
                                notificationsModel.requisition_workitemId = offerRollout.optString("workitemId");
                            }

                        }//end of offerRollout

                        if (contentobj.has("sourceRequisition")) {
                            JSONObject sourceRequisition = contentobj.optJSONObject("sourceRequisition");
                            if (sourceRequisition != null) {
                                notificationsModel.requisitionId = sourceRequisition.optString("requisitionId");
                            }
                        }//end of sourceRequisition

                        if (contentobj.has("postIJP")) {
                            JSONObject postIJP = contentobj.optJSONObject("postIJP");
                            if (postIJP != null) {

                            }
                        }//end of postIJP

                        if (contentobj.has("postER")) {
                            JSONObject postER = contentobj.optJSONObject("postER");
                            if (postER != null) {

                            }
                        }//end of postER

                        if (contentobj.has("uploadDocuments")) {
                            JSONObject uploadDocuments = contentobj.optJSONObject("uploadDocuments");
                            if (uploadDocuments != null) {
                                notificationsModel.requisitionId = uploadDocuments.optString("requisitionId");
                                notificationsModel.candidate_email = uploadDocuments.optString("candidateEmail");
                                notificationsModel.applicationId = uploadDocuments.optString("applicationId");
                            }
                        }

                        notificationsModel.notification_workitem_id = nObj.optString("workitemId");
                        notificationsModel.message = nObj.optString("message");
                        notificationsModel.applicationType = nObj.optString("applicationType");
                        notificationsModel.read_status = nObj.optBoolean("read");

                        JSONObject sender = nObj.optJSONObject("sender");
                        notificationsModel.sender_id = sender.optString("_id");
                        notificationsModel.sender_displayName = sender.optString("displayName");
                        notificationsModel.sender_avatarurl = sender.optString("avatar");

                        if (sender.has("role")) {
                            JSONObject sender_role = sender.optJSONObject("role");
                            notificationsModel.sender_roleName = sender_role.optString("name");
                            notificationsModel.sender_roleRegion = sender_role.optString("region");
                            notificationsModel.sender_hrfunction = sender_role.optString("hrFunction");
                        }

                        if (nObj.has("receiver")) {

                            JSONObject receiver = nObj.optJSONObject("receiver");
                            if (receiver != null) {
                                notificationsModel.receiver_id = receiver.optString("_id");
                                notificationsModel.receiver_displayName = receiver.optString("displayName");
                                notificationsModel.receiver_avatarurl = receiver.optString("avatar");
                                if (receiver.has("role")) {
                                    JSONObject receiver_role = receiver.optJSONObject("role");
                                    notificationsModel.receiver_roleName = receiver_role.optString("name");
                                    String region = receiver_role.optString("region");
                                    if (region == null || region.isEmpty() || region.contains("null")) {
                                        notificationsModel.receiver_roleRegion = CWIncturePreferences.getRegion();
                                    } else {
                                        notificationsModel.receiver_roleRegion = region;
                                    }

                                    if (notificationsModel.appraisalProcess_stage.equalsIgnoreCase("HRBP Approval")) {
                                        notificationsModel.receiver_hrfunction = appraisalProcess.optString("hrFunction");
                                    } else {
                                        notificationsModel.receiver_hrfunction = receiver_role.optString("hrFunction");
                                    }

                                }

                            }
                        }

                        notificationsModel.createdAt = nObj.optString("createdAt");
                        notificationsModel.updateAt = nObj.optString("updateAt");

                        if (nObj.has("updatedBy")) {
                            JSONObject updatedBy = nObj.optJSONObject("updatedBy");
                            if (updatedBy != null) {
                                notificationsModel.updatedBy_id = updatedBy.optString("_id");
                                notificationsModel.updatedBy_displayName = updatedBy.optString("displayName");
                                notificationsModel.updatedBy_avatarurl = updatedBy.optString("avatar");
                            }
                        }

                        notifications_list.add(notificationsModel);
                    }
                }

                if (notifications_list.size() == 0) {
                    noNotifications.setVisibility(View.VISIBLE);
                } else {

                    adapter = new TimesheetNotificationsAdapter(TimeSheetNotifications.this, R.layout.timesheet_notifications_list, notifications_list);

                    lv.setAdapter(adapter);
                    lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                        @Override
                        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                                long arg3) {
                            // TODO Auto-generated method stub
                            if (Util.isOnline(TimeSheetNotifications.this)) {
                                NotificationsModel notification = notifications_list.get(arg2);

                                if (notification.applicationType.equals("appraisalTemplate")) {

                                    if (notification.getRead_status() == false) {
                                        updateStatus(notification);
                                    }
                                    Intent intent = new Intent(TimeSheetNotifications.this, TemplateDetail.class);
                                    intent.putExtra("applicationType", notification.applicationType);
                                    intent.putExtra("template_id", notification.appraisalTemplate_id);
                                    intent.putExtra("workitem_id", notification.appraisalTemplate_workitemId);
                                    intent.putExtra("status", notification.appraisal_status);
                                    intent.putExtra("roleName", notification.receiver_roleName);
                                    intent.putExtra("regionName", notification.receiver_roleRegion);
                                    intent.putExtra("type", notification.notification_cherryName);
                                    intent.putExtra("hrFunction", notification.receiver_hrfunction);
                                    startActivity(intent);
                                }//end of aprraisalTemplate
                                else if (notification.applicationType.equals("appraisalProcess")) {
                                    if (notification.getRead_status() == false) {
                                        updateStatus(notification);
                                    }
                                    Intent intent = new Intent(TimeSheetNotifications.this, AppraisalProcessDetail.class);
                                    intent.putExtra("applicationType", notification.applicationType);
                                    intent.putExtra("template_id", notification.appraisalProcess_id);
                                    intent.putExtra("stage", notification.appraisalProcess_stage);
                                    intent.putExtra("from", notification.validity_from);
                                    intent.putExtra("to", notification.validity_to);
                                    intent.putExtra("roleName", notification.receiver_roleName);
                                    intent.putExtra("regionName", notification.receiver_roleRegion);
                                    intent.putExtra("type", notification.notification_cherryName);
                                    intent.putExtra("hrFunction", notification.receiver_hrfunction);
                                    intent.putExtra("appraisalname", notification.appraisalname);
                                    startActivity(intent);
                                }//end of appraisalProcess
                                else if (notification.applicationType.equalsIgnoreCase("onBoardingProcess")) {
                                    if (notification.getRead_status() == false) {
                                        updateStatus(notification);
                                    }
                                    Intent intent = new Intent(TimeSheetNotifications.this, AppraisalProcessDetail.class);
                                    intent.putExtra("applicationType", notification.applicationType);
                                    intent.putExtra("template_id", notification.onBoarding_formId);
                                    intent.putExtra("type", notification.notification_cherryName);
                                    intent.putExtra("stage", notification.onBoarding_currentStage);
                                    intent.putExtra("roleName", notification.receiver_roleName);
                                    intent.putExtra("regionName", notification.receiver_roleRegion);
                                    intent.putExtra("hrFunction", notification.receiver_hrfunction);
                                    startActivity(intent);
                                }//end of onBoarding
                                else if (notification.applicationType.equalsIgnoreCase("requisition")) {
                                    if (notification.getRead_status() == false) {
                                        updateStatus(notification);
                                    }
                                    Intent intent = new Intent(TimeSheetNotifications.this, RecruitmentDetail.class);
                                    intent.putExtra("applicationType", notification.applicationType);
                                    intent.putExtra("req_id", notification.requisitionId);
                                    intent.putExtra("roleName", notification.receiver_roleName);
                                    intent.putExtra("regionName", notification.receiver_roleRegion);
                                    intent.putExtra("hrFunction", notification.receiver_hrfunction);
                                    startActivity(intent);
                                }//end of requisition
                                else if (notification.applicationType.equalsIgnoreCase("updateRequisition")) {
                                    if (notification.getRead_status() == false) {
                                        updateStatus(notification);
                                    }
                                    Intent intent = new Intent(TimeSheetNotifications.this, RecruitmentDetail.class);
                                    intent.putExtra("applicationType", notification.applicationType);
                                    intent.putExtra("req_id", notification.requisitionId);
                                    intent.putExtra("roleName", notification.receiver_roleName);
                                    intent.putExtra("regionName", notification.receiver_roleRegion);
                                    intent.putExtra("hrFunction", notification.receiver_hrfunction);
                                    startActivity(intent);

                                }//end of updateRequisition

                                else if (notification.applicationType.equalsIgnoreCase("sourceRequisition")) {
                                    if (notification.getRead_status() == false) {
                                        updateStatus(notification);
                                    }

                                    Intent intent = new Intent(TimeSheetNotifications.this, RecruitmentDetail.class);
                                    intent.putExtra("applicationType", notification.applicationType);
                                    intent.putExtra("req_id", notification.requisitionId);
                                    intent.putExtra("roleName", notification.receiver_roleName);
                                    intent.putExtra("regionName", notification.receiver_roleRegion);
                                    intent.putExtra("hrFunction", notification.receiver_hrfunction);
                                    startActivity(intent);
                                }//end of sourceRequisition

                                else if (notification.applicationType.equalsIgnoreCase("profileEvaluation")) {
                                    if (notification.getRead_status() == false) {
                                        updateStatus(notification);
                                    }
                                    Intent intent = new Intent(TimeSheetNotifications.this, RecruitmentDetail.class);
                                    intent.putExtra("applicationType", notification.applicationType);
                                    intent.putExtra("req_id", notification.requisitionId);
                                    intent.putExtra("applicationId", notification.applicationId);
                                    intent.putExtra("roleName", notification.receiver_roleName);
                                    intent.putExtra("regionName", notification.receiver_roleRegion);
                                    intent.putExtra("hrFunction", notification.receiver_hrfunction);
                                    intent.putExtra("email", notification.candidate_email);
                                    startActivity(intent);

                                }//end of profileEvaluation
                                else if (notification.applicationType.equalsIgnoreCase("postIJP")) {
                                    if (notification.getRead_status() == false) {
                                        updateStatus(notification);
                                    }
                                    Intent intent = new Intent(TimeSheetNotifications.this, RecruitmentDetail.class);
                                    intent.putExtra("applicationType", notification.applicationType);
                                    intent.putExtra("req_id", notification.requisitionId);
                                    intent.putExtra("roleName", notification.receiver_roleName);
                                    intent.putExtra("regionName", notification.receiver_roleRegion);
                                    intent.putExtra("hrFunction", notification.receiver_hrfunction);
                                    startActivity(intent);
                                }//end of postIJP

                                else if (notification.applicationType.equalsIgnoreCase("postER")) {
                                    if (notification.getRead_status() == false) {
                                        updateStatus(notification);
                                    }
                                    Intent intent = new Intent(TimeSheetNotifications.this, RecruitmentDetail.class);
                                    intent.putExtra("applicationType", notification.applicationType);
                                    intent.putExtra("req_id", notification.requisitionId);
                                    intent.putExtra("roleName", notification.receiver_roleName);
                                    intent.putExtra("regionName", notification.receiver_roleRegion);
                                    intent.putExtra("hrFunction", notification.receiver_hrfunction);
                                    startActivity(intent);
                                }//end of postER

                                else if (notification.applicationType.equalsIgnoreCase("interviewFeedback")) {
                                    if (notification.getRead_status() == false) {
                                        updateStatus(notification);
                                    }
                                    Intent intent = new Intent(TimeSheetNotifications.this, RecruitmentDetail.class);
                                    intent.putExtra("applicationType", notification.applicationType);
                                    intent.putExtra("req_id", notification.requisitionId);
                                    intent.putExtra("roleName", notification.receiver_roleName);
                                    intent.putExtra("regionName", notification.receiver_roleRegion);
                                    intent.putExtra("hrFunction", notification.receiver_hrfunction);
                                    intent.putExtra("applicationId", notification.applicationId);
                                    intent.putExtra("email", notification.candidate_email);
                                    startActivity(intent);
                                }//end of interviewFeedback
                                else if (notification.applicationType.equalsIgnoreCase("offerRollout")) {
                                    if (notification.getRead_status() == false) {
                                        updateStatus(notification);
                                    }
                                    Intent intent = new Intent(TimeSheetNotifications.this, RecruitmentDetail.class);
                                    intent.putExtra("applicationType", notification.applicationType);
                                    intent.putExtra("req_id", notification.requisitionId);
                                    intent.putExtra("roleName", notification.receiver_roleName);
                                    intent.putExtra("regionName", notification.receiver_roleRegion);
                                    intent.putExtra("hrFunction", notification.receiver_hrfunction);
                                    intent.putExtra("applicationId", notification.applicationId);
                                    startActivity(intent);
                                }//end of offerRollOut
                                else if (notification.applicationType.equalsIgnoreCase("generateOffer")) {
                                    if (notification.getRead_status() == false) {
                                        updateStatus(notification);
                                    }
                                    Intent intent = new Intent(TimeSheetNotifications.this, RecruitmentDetail.class);
                                    intent.putExtra("applicationType", notification.applicationType);
                                    intent.putExtra("req_id", notification.requisitionId);
                                    intent.putExtra("roleName", notification.receiver_roleName);
                                    intent.putExtra("regionName", notification.receiver_roleRegion);
                                    intent.putExtra("hrFunction", notification.receiver_hrfunction);
                                    intent.putExtra("offerId", notification.offerId);
                                    startActivity(intent);
                                }//end of generateOffer

                                else if (notification.applicationType.equalsIgnoreCase("applicationEvaluation")) {
                                    if (notification.getRead_status() == false) {
                                        updateStatus(notification);
                                    }

                                    Intent intent = new Intent(TimeSheetNotifications.this, RecruitmentDetail.class);
                                    intent.putExtra("applicationType", notification.applicationType);
                                    intent.putExtra("req_id", notification.requisitionId);
                                    intent.putExtra("applicationId", notification.applicationId);
                                    intent.putExtra("roleName", notification.receiver_roleName);
                                    intent.putExtra("regionName", notification.receiver_roleRegion);
                                    intent.putExtra("hrFunction", notification.receiver_hrfunction);
                                    intent.putExtra("email", notification.candidate_email);
                                    startActivity(intent);
                                }// end of applicationEvaluation

                                else if (notification.applicationType.equalsIgnoreCase("approveRequisition")) {
                                    if (notification.getRead_status() == false) {
                                        updateStatus(notification);
                                    }

                                    Intent intent = new Intent(TimeSheetNotifications.this, MainActivity.class);
                                    startActivity(intent);
                                }//end of approveRequisition
                                else if (notification.applicationType.equalsIgnoreCase("uploadDocuments")) {
                                    if (notification.getRead_status() == false) {
                                        updateStatus(notification);
                                    }
                                    Intent intent = new Intent(TimeSheetNotifications.this, RecruitmentDetail.class);
                                    intent.putExtra("applicationType", notification.applicationType);
                                    intent.putExtra("req_id", notification.requisitionId);
                                    intent.putExtra("applicationId", notification.applicationId);
                                    intent.putExtra("roleName", notification.receiver_roleName);
                                    intent.putExtra("regionName", notification.receiver_roleRegion);
                                    intent.putExtra("hrFunction", notification.receiver_hrfunction);
                                    intent.putExtra("email", notification.candidate_email);
                                    startActivity(intent);

                                }//end of uploadDcouments
                                else if (notification.applicationType.equalsIgnoreCase("Info process")) {
                                    if (notification.getRead_status() == false) {
                                        updateStatus(notification);
                                    }
//                                    Intent intent = new Intent(TimeSheetNotifications.this, LeaveDetail.class);
//                                    intent.putExtra("applicationType", notification.applicationType);
//                                    intent.putExtra("leaveId",notification.leave_id);
//
//                                    startActivity(intent);

                                }//end of Info process
                                else if (notification.applicationType.equalsIgnoreCase("Cancel Process")) {
                                    if (notification.getRead_status() == false) {
                                        updateStatus(notification);
                                    }
                                    Intent intent = new Intent(TimeSheetNotifications.this, LeaveDetail.class);
                                    intent.putExtra("applicationType", notification.applicationType);
                                    intent.putExtra("leaveId", notification.leave_id);

                                    startActivity(intent);


                                }//end of Cancel Process
                                else if (notification.applicationType.equalsIgnoreCase("Approval Process")) {
                                    if (notification.getRead_status() == false) {
                                        updateStatus(notification);
                                    }
                                    Intent intent = new Intent(TimeSheetNotifications.this, LeaveDetail.class);
                                    intent.putExtra("applicationType", notification.applicationType);
                                    intent.putExtra("leaveId", notification.leave_id);

                                    startActivity(intent);


                                }//end of Approval Process
                                else if (notification.applicationType.equalsIgnoreCase("Reject Process")) {
                                    if (notification.getRead_status() == false) {
                                        updateStatus(notification);
                                    }
                                    Intent intent = new Intent(TimeSheetNotifications.this, LeaveDetail.class);
                                    intent.putExtra("applicationType", notification.applicationType);
                                    intent.putExtra("leaveId", notification.leave_id);

                                    startActivity(intent);

                                }//end of Reject Process
                                else if (notification.applicationType.equalsIgnoreCase("Approval Response")) {
                                    if (notification.getRead_status() == false) {
                                        updateStatus(notification);
                                    }
                                    Intent intent = new Intent(TimeSheetNotifications.this, LeaveDetail.class);
                                    intent.putExtra("applicationType", notification.applicationType);
                                    intent.putExtra("leaveId", notification.leave_id);

                                    startActivity(intent);

                                }//end of Approval Response

                            } else {
                                Toast.makeText(TimeSheetNotifications.this, "Details not available in offline mode", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }
            }//end of if
        }//end of try
        catch (JSONException e) {
            e.printStackTrace();
        }

        if (status_code.equals(403)) {
            Toast.makeText(TimeSheetNotifications.this, message, Toast.LENGTH_LONG).show();
            Util util = new Util();
            util.logoutfunc(TimeSheetNotifications.this);
        }

    }

    private void updateStatus(NotificationsModel notificationModel) {
        Map<String, String> headers = new HashMap<>();
        headers.put("x-email-id", CWIncturePreferences.getEmail());
        headers.put("x-access-token",
                CWIncturePreferences.getAccessToken());

        try {
            headers.put("android-version", getPackageManager().getPackageInfo(getPackageName(), 0).versionName);
        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        NetworkConnector connect = new NetworkConnector(TimeSheetNotifications.this, NetworkConnector.TYPE_PUT, CWUrls.NOTIFICATION_STATUS_UPDATE + notificationModel.id, headers, null, TimeSheetNotifications.this);
        if (connect.isAllowed()) {
            connect.execute();

            notificationModel.setRead_status(true);
            adapter.notifyDataSetChanged();
        } else {
            Toast.makeText(TimeSheetNotifications.this, getString(R.string.action_not_allowed), Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void configurationUpdated(boolean configUpdated) {

    }
}
