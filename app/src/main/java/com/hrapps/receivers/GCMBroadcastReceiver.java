package com.hrapps.receivers;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;

import com.hrapps.CSC_Britannia.AppraisalProcessDetail;
import com.hrapps.CSC_Britannia.TemplateDetail;
import com.hrapps.CherryworkApplication;
import com.hrapps.Leave_Britannia.LeaveDetail;
import com.hrapps.MainActivity;
import com.hrapps.R;
import com.hrapps.Recruitment_Britannia.RecruitmentDetail;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import org.json.JSONException;
import org.json.JSONObject;

import Model.NotificationsModel;
import Utility.CWIncturePreferences;


/**
 * Created by Arun on 14-09-2015.
 */
public class GCMBroadcastReceiver extends BroadcastReceiver {

    NotificationsModel model;
    private String msg = "";

    @Override
    public void onReceive(Context context, Intent arg1) {
        String message2 = null;
        Bundle extras = arg1.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(context);
        String messageType = gcm.getMessageType(arg1);
        // String message=gcm.getMessageType(arg1);

        extras.putString("message", messageType);
        //extras.get(Config.MESSAGE_KEY);
        //Intent myIntent = new Intent(context.getApplicationContext(),InboxActivity.class);
        //my Intent.putExtras(extras);
        //context.getApplicationContext().startActivity(myIntent);
        Log.d("string", "" + messageType.toString());

        // Explicitly specify that GcmIntentService will handle the intent.
//        // Start the service, keeping the device awake while it is launching.
//        startWakefulService(context, (arg1));
//        setResultCode(Activity.RESULT_OK);

        if (!extras.isEmpty()) {

            if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)) {
                String message1 = extras.getString("body");
                ((CherryworkApplication) context.getApplicationContext()).notifiationAdded();
                //String message2=extras.getString("id");

                if (message1 != null) {
                    PendingIntent pIntent = null;
                    NotificationsModel notification = parse(message1);

                    if (notification.applicationType.equalsIgnoreCase("appraisalProcess")) {
                        Intent intent = new Intent(context, AppraisalProcessDetail.class);
                        intent.putExtra("applicationType", notification.applicationType);
                        intent.putExtra("template_id", notification.appraisalProcess_id);
                        intent.putExtra("stage", notification.appraisalProcess_stage);
                        intent.putExtra("from", notification.validity_from);
                        intent.putExtra("to", notification.validity_to);
                        intent.putExtra("appraisalname", notification.appraisalname);
                        intent.putExtra("type", notification.notification_cherryName);
                        intent.putExtra("roleName", notification.receiver_roleName);
                        intent.putExtra("regionName", notification.receiver_roleRegion);
                        intent.putExtra("hrFunction", notification.receiver_hrfunction);
                        pIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
                    } else if (notification.applicationType.equalsIgnoreCase("appraisalTemplate")) {
                        Intent intent = new Intent(context, TemplateDetail.class);
                        intent.putExtra("applicationType", notification.applicationType);
                        intent.putExtra("template_id", notification.appraisalTemplate_id);
                        intent.putExtra("workitem_id", notification.appraisalTemplate_workitemId);
                        intent.putExtra("status", notification.appraisal_status);
                        intent.putExtra("roleName", notification.receiver_roleName);
                        intent.putExtra("regionName", notification.receiver_roleRegion);
                        intent.putExtra("hrFunction", notification.receiver_hrfunction);
                        intent.putExtra("type", notification.notification_cherryName);
                        pIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
                    } else if (notification.applicationType.equalsIgnoreCase("onBoardingProcess")) {
                        Intent intent = new Intent(context, AppraisalProcessDetail.class);
                        intent.putExtra("applicationType", notification.applicationType);
                        intent.putExtra("template_id", notification.onBoarding_formId);
                        intent.putExtra("type", notification.notification_cherryName);
                        intent.putExtra("stage", notification.onBoarding_currentStage);
                        intent.putExtra("hrFunction", notification.receiver_hrfunction);
                        intent.putExtra("roleName", notification.receiver_roleName);
                        intent.putExtra("regionName", notification.receiver_roleRegion);
                        pIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
                    } else if (notification.applicationType.equalsIgnoreCase("requisition")) {
                        Intent intent = new Intent(context, RecruitmentDetail.class);
                        intent.putExtra("applicationType", notification.applicationType);
                        intent.putExtra("req_id", notification.requisitionId);
                        intent.putExtra("roleName", notification.receiver_roleName);
                        intent.putExtra("regionName", notification.receiver_roleRegion);
                        intent.putExtra("hrFunction", notification.receiver_hrfunction);
                        pIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
                    } else if (notification.applicationType.equalsIgnoreCase("updateRequisition")) {
                        Intent intent = new Intent(context, RecruitmentDetail.class);
                        intent.putExtra("applicationType", notification.applicationType);
                        intent.putExtra("req_id", notification.requisitionId);
                        intent.putExtra("roleName", notification.receiver_roleName);
                        intent.putExtra("regionName", notification.receiver_roleRegion);
                        intent.putExtra("hrFunction", notification.receiver_hrfunction);
                        pIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);

                    }//end of updateRequisition

                    else if (notification.applicationType.equalsIgnoreCase("profileEvaluation")) {
                        Intent intent = new Intent(context, RecruitmentDetail.class);
                        intent.putExtra("applicationType", notification.applicationType);
                        intent.putExtra("req_id", notification.requisitionId);
                        intent.putExtra("roleName", notification.receiver_roleName);
                        intent.putExtra("regionName", notification.receiver_roleRegion);
                        intent.putExtra("hrFunction", notification.receiver_hrfunction);
                        intent.putExtra("email", notification.candidate_email);
                        intent.putExtra("applicationId", notification.applicationId);
                        pIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
                    }//end of profileEvaluation
                    else if (notification.applicationType.equalsIgnoreCase("postIJP")) {
                        Intent intent = new Intent(context, RecruitmentDetail.class);
                        intent.putExtra("applicationType", notification.applicationType);
                        intent.putExtra("req_id", notification.requisitionId);
                        intent.putExtra("roleName", notification.receiver_roleName);
                        intent.putExtra("regionName", notification.receiver_roleRegion);
                        intent.putExtra("hrFunction", notification.receiver_hrfunction);
                        pIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
                    }//end of postIJP
                    else if (notification.applicationType.equalsIgnoreCase("postER")) {
                        Intent intent = new Intent(context, RecruitmentDetail.class);
                        intent.putExtra("applicationType", notification.applicationType);
                        intent.putExtra("req_id", notification.requisitionId);
                        intent.putExtra("roleName", notification.receiver_roleName);
                        intent.putExtra("regionName", notification.receiver_roleRegion);
                        intent.putExtra("hrFunction", notification.receiver_hrfunction);
                        pIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
                    }//end of postER
                    else if (notification.applicationType.equalsIgnoreCase("interviewFeedback")) {
                        Intent intent = new Intent(context, RecruitmentDetail.class);
                        intent.putExtra("applicationType", notification.applicationType);
                        intent.putExtra("req_id", notification.requisitionId);
                        intent.putExtra("roleName", notification.receiver_roleName);
                        intent.putExtra("regionName", notification.receiver_roleRegion);
                        intent.putExtra("hrFunction", notification.receiver_hrfunction);
                        intent.putExtra("applicationId", notification.applicationId);
                        intent.putExtra("email", notification.candidate_email);
                        pIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
                    }//end of interviewFeedback

                    else if (notification.applicationType.equalsIgnoreCase("offerRollout")) {
                        Intent intent = new Intent(context, RecruitmentDetail.class);
                        intent.putExtra("applicationType", notification.applicationType);
                        intent.putExtra("req_id", notification.requisitionId);
                        intent.putExtra("roleName", notification.receiver_roleName);
                        intent.putExtra("regionName", notification.receiver_roleRegion);
                        intent.putExtra("hrFunction", notification.receiver_hrfunction);
                        intent.putExtra("applicationId", notification.applicationId);
                        pIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
                    }//end of offerRollOut
                    else if (notification.applicationType.equalsIgnoreCase("generateOffer")) {
                        Intent intent = new Intent(context, RecruitmentDetail.class);
                        intent.putExtra("applicationType", notification.applicationType);
                        intent.putExtra("req_id", notification.requisitionId);
                        intent.putExtra("roleName", notification.receiver_roleName);
                        intent.putExtra("regionName", notification.receiver_roleRegion);
                        intent.putExtra("hrFunction", notification.receiver_hrfunction);
                        intent.putExtra("offerId", notification.offerId);
                        pIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
                    }//end of generateOffer

                    else if (notification.applicationType.equalsIgnoreCase("applicationEvaluation")) {
                        Intent intent = new Intent(context, RecruitmentDetail.class);
                        intent.putExtra("applicationType", notification.applicationType);
                        intent.putExtra("req_id", notification.requisitionId);
                        intent.putExtra("roleName", notification.receiver_roleName);
                        intent.putExtra("regionName", notification.receiver_roleRegion);
                        intent.putExtra("hrFunction", notification.receiver_hrfunction);
                        intent.putExtra("applicationId", notification.applicationId);
                        intent.putExtra("email", notification.candidate_email);
                        pIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
                    }// end of applicationEvaluation

                    else if (notification.applicationType.equalsIgnoreCase("approveRequisition")) {
                        Intent intent = new Intent(context, MainActivity.class);
                        pIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
                    }//end of approveRequisition

                    else if (notification.applicationType.equalsIgnoreCase("sourceRequisition")) {

                        Intent intent = new Intent(context, RecruitmentDetail.class);
                        intent.putExtra("applicationType", notification.applicationType);
                        intent.putExtra("req_id", notification.requisitionId);
                        intent.putExtra("roleName", notification.receiver_roleName);
                        intent.putExtra("regionName", notification.receiver_roleRegion);
                        intent.putExtra("hrFunction", notification.receiver_hrfunction);
                        pIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
                    } else if (notification.applicationType.equalsIgnoreCase("uploadDocuments")) {
                        Intent intent = new Intent(context, RecruitmentDetail.class);
                        intent.putExtra("applicationType", notification.applicationType);
                        intent.putExtra("req_id", notification.requisitionId);
                        intent.putExtra("applicationId", notification.applicationId);
                        intent.putExtra("email", notification.candidate_email);
                        intent.putExtra("roleName", notification.receiver_roleName);
                        intent.putExtra("regionName", notification.receiver_roleRegion);
                        intent.putExtra("hrFunction", notification.receiver_hrfunction);
                        pIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
                    } else if (notification.applicationType.equalsIgnoreCase("Info process")) {
                        Intent intent = new Intent(context, LeaveDetail.class);
                        intent.putExtra("applicationType", notification.applicationType);
                        intent.putExtra("leaveId",notification.leave_id);
                        pIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
                    }else if (notification.applicationType.equalsIgnoreCase("Cancel Process")) {
                        Intent intent = new Intent(context, LeaveDetail.class);
                        intent.putExtra("applicationType", notification.applicationType);
                        intent.putExtra("leaveId",notification.leave_id);
                        pIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
                    }else if (notification.applicationType.equalsIgnoreCase("Approval Process")) {
                        Intent intent = new Intent(context, LeaveDetail.class);
                        intent.putExtra("applicationType", notification.applicationType);
                        intent.putExtra("leaveId",notification.leave_id);
                        pIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
                    }else if (notification.applicationType.equalsIgnoreCase("Reject Process")) {
                        Intent intent = new Intent(context, LeaveDetail.class);
                        intent.putExtra("applicationType", notification.applicationType);
                        intent.putExtra("leaveId",notification.leave_id);
                        pIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
                    }else if (notification.applicationType.equalsIgnoreCase("Approval Response")) {
                        Intent intent = new Intent(context, LeaveDetail.class);
                        intent.putExtra("applicationType", notification.applicationType);
                        intent.putExtra("leaveId",notification.leave_id);
                        pIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
                    }

                    else {
                        String posValue = "";
                        Intent intent = new Intent(context, MainActivity.class);
                        intent.putExtra("pos", posValue);
                        pIntent = PendingIntent.getActivity(context, 0, intent, 0);
                    }
                    Notification noti = new Notification.Builder(context)
                            .setContentTitle("Britannia").setContentText(notification.message)
                            .setSmallIcon(R.mipmap.brit_app_icon_new)
                            .setColor(0xff0000)
                            .extend(new Notification.WearableExtender().setContentIcon(R.mipmap.brit_app_icon_new))
                            .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                            .setContentIntent(pIntent).getNotification();

                    NotificationManager notificationManager = (NotificationManager) context
                            .getSystemService(Context.NOTIFICATION_SERVICE);
                    noti.flags |= Notification.FLAG_AUTO_CANCEL;
                    notificationManager.notify(0, noti);
                }
            }
        }
    }//end of onRecieve()

    public NotificationsModel parse(String response) {
        try {
            JSONObject obj = new JSONObject(response);
            model = new NotificationsModel();
            model.applicationType = obj.optString("applicationType");
            model.message = obj.optString("message");
            JSONObject contentobj = obj.optJSONObject("content");

            JSONObject appraisalProcess = contentobj.optJSONObject("appraisalProcess");
            JSONObject appraisalTemplate = contentobj.optJSONObject("appraisalTemplate");
            JSONObject onBoardingProcess = contentobj.optJSONObject("onBoardingProcess");
            JSONObject leaveProcess = contentobj.optJSONObject("leaveProcess");


            if (appraisalProcess != null) {
                model.appraisalProcess_id = appraisalProcess.optString("templateId");
                model.appraisalProcess_type = appraisalProcess.optString("type");
                model.appraisalProcess_stage = appraisalProcess.optString("currentStage");
                model.appraisalname = appraisalProcess.optString("name");

                JSONObject validity = appraisalProcess.optJSONObject("evaluationPeriod");

                if (validity != null) {
                    model.validity_from = validity.optString("from");
                    model.validity_to = validity.optString("to");
                }
            }//end of appraisalProcess

            if (appraisalTemplate != null) {
                model.appraisalTemplate_id = appraisalTemplate.optString("_id");
                model.appraisalTemplate_workitemId = appraisalTemplate.optString("workitemId");
                model.appraisal_status = appraisalTemplate.optString("status");
            }//end of appraisalTemplate

            if (leaveProcess!=null){
                model.leave_id = leaveProcess.optString("leaveID");
                model.leave_name = leaveProcess.optString("name");
                model.leave_currentStage = leaveProcess.optString("currentStage");
            }
            if (onBoardingProcess != null) {
                model.onBoarding_formId = onBoardingProcess.optString("formId");
                model.onBoarding_evaluationPeriod = onBoardingProcess.optString("evaluationPeriod");
                model.onBoarding_currentStage = onBoardingProcess.optString("currentStage");
                model.onBoarding_name = onBoardingProcess.optString("name");
                model.onBoarding_type = onBoardingProcess.optString("type");
            }//end of onBoardingProcess


            if (contentobj.has("requisition")) {
                JSONObject requisition = contentobj.optJSONObject("requisition");
                if (requisition != null) {
                    model.requisitionId = requisition.optString("requisitionId");
                    model.positionName = requisition.optString("positionName");
                    model.workLocation = requisition.optString("location");
                    model.requisition_workitemId = requisition.optString("workitemId");
                }
            }//end of requisition

            if (contentobj.has("updateRequisition")) {
                JSONObject updateRequisition = contentobj.optJSONObject("updateRequisition");
                if (updateRequisition != null) {
                    model.requisitionId = updateRequisition.optString("requisitionId");
                    model.positionName = updateRequisition.optString("positionName");
                    model.requisition_workitemId = updateRequisition.optString("workitemId");
                    model.requisition_type = updateRequisition.optString("type");
                }
            }//end of updateRequisition


            if (contentobj.has("generateOffer")) {
                JSONObject generateOffer = contentobj.optJSONObject("generateOffer");

                if (generateOffer != null) {
                    model.applicationId = generateOffer.optString("applicationId");
                    model.evaluationId = generateOffer.optString("evaluationId");
                    model.requisitionId = generateOffer.optString("requisitionId");
                    model.requisition_workitemId = generateOffer.optString("workitemId");
                    model.offerId = generateOffer.optString("offerId");
                }
            }// end of generateOffer

            if (contentobj.has("offerRollout")) {
                JSONObject offerRollout = contentobj.optJSONObject("offerRollout");

                if (offerRollout != null) {
                    model.applicationId = offerRollout.optString("applicationId");
                    model.evaluationId = offerRollout.optString("evaluationId");
                    model.requisitionId = offerRollout.optString("requisitionId");
                    model.requisition_workitemId = offerRollout.optString("workitemId");
                }
            }// end of offerRollout


            if (contentobj.has("interviewFeedback")) {
                JSONObject interviewFeedback = contentobj.optJSONObject("interviewFeedback");
                if (interviewFeedback != null) {
                    model.applicationId = interviewFeedback.optString("applicationId");
                    model.evaluationId = interviewFeedback.optString("evaluationId");
                    model.requisitionId = interviewFeedback.optString("requisitionId");

                    try {
                        if (interviewFeedback.has("candidate")) {
                            JSONObject candidateObject = interviewFeedback.optJSONObject("candidate");
                            if (candidateObject != null) {
                                model.candidate_email = candidateObject.optString("emailId");
                            }
                        }
                    } catch (Exception e) {
                    }
                }
            }// end of interviewFeedbback

            if (contentobj.has("profileEvaluation")) {
                JSONObject profileEvaluation = contentobj.optJSONObject("profileEvaluation");
                if (profileEvaluation != null) {
                    model.applicationId = profileEvaluation.optString("applicationId");
                    model.requisitionId = profileEvaluation.optString("requisitionId");
                    model.requisition_workitemId = profileEvaluation.optString("workitemId");

                    if (profileEvaluation.has("candidate")) {
                        JSONObject candidate = profileEvaluation.optJSONObject("candidate");
                        if (candidate != null) {
                            model.candidate_email = candidate.optString("emailId");
                        }
                    }

                }
            }//end of profileEvaluation

            if (contentobj.has("applicationEvaluation")) {
                JSONObject applicationEvaluation = contentobj.optJSONObject("applicationEvaluation");
                if (applicationEvaluation != null) {
                    model.applicationId = applicationEvaluation.optString("applicationId");
                    model.requisitionId = applicationEvaluation.optString("requisitionId");

                    if (applicationEvaluation.has("candidate")) {
                        JSONObject candidate = applicationEvaluation.optJSONObject("candidate");
                        if (candidate != null) {
                            model.candidate_email = candidate.optString("emailId");
                        }
                    }
                }
            }//end of applicationEvaluation

            if (contentobj.has("sourceRequisition")) {
                JSONObject sourceRequisition = contentobj.optJSONObject("sourceRequisition");
                if (sourceRequisition != null) {
                    model.requisitionId = sourceRequisition.optString("requisitionId");
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
                    model.requisitionId = uploadDocuments.optString("requisitionId");
                    model.candidate_email = uploadDocuments.optString("candidateEmail");
                    model.applicationId = uploadDocuments.optString("applicationId");
                }
            }


            JSONObject metadata = contentobj.optJSONObject("metadata");
            if (obj.has("cherryName")) {
                model.notification_cherryName = obj.optString("cherryName");
            }

            JSONObject sender = obj.optJSONObject("sender");
            if (sender != null) {
                model.sender_id = sender.optString("_id");
                model.sender_displayName = sender.optString("displayName");
                model.sender_avatarurl = sender.optString("avatar");

                if (sender.has("role")) {
                    JSONObject sender_role = sender.optJSONObject("role");
                    model.sender_roleName = sender_role.optString("name");
                    model.sender_roleRegion = sender_role.optString("region");
                    model.sender_hrfunction = sender_role.optString("hrFunction");
                }

            }


            JSONObject receiver = obj.optJSONObject("receiver");

           if (receiver!=null){
               model.receiver_id = receiver.optString("_id");
               model.receiver_displayName = receiver.optString("displayName");
               model.receiver_avatarurl = receiver.optString("avatar");
                if (receiver.has("role")) {
                    JSONObject receiver_role = receiver.optJSONObject("role");
                    model.receiver_roleName = receiver_role.optString("name");
                    String region = receiver_role.optString("region");
                    if (region == null || region.isEmpty() || region.contains("null")) {
                        model.receiver_roleRegion = CWIncturePreferences.getRegion();
                    } else {
                        model.receiver_roleRegion = region;
                    }

                    if (model.appraisalProcess_stage.equalsIgnoreCase("HRBP Approval")) {
                        model.receiver_hrfunction = appraisalProcess.optString("hrFunction");
                    } else {
                        model.receiver_hrfunction = receiver_role.optString("hrFunction");
                    }


                }
            }


        } //end of try
        catch (JSONException e) {
            e.printStackTrace();
        }//end of catch
        return model;
    }

}
