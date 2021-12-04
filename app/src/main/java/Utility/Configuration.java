package Utility;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hrapps.CSC_Britannia.TemplateDetail;
import com.hrapps.R;
import com.hrapps.Recruitment_Britannia.RecruitmentWorkitemDetail;

import java.text.SimpleDateFormat;
import java.util.TimeZone;

import Model.ConfigModel;
import Model.FeedsDataModel;
import adapters.InboxAdapter;

/**
 * Created by Deeksha on 25-01-2016.
 */
public class Configuration {
    static Configuration config = null;
    Context _context = null;
    String perspective = "";
    private InboxAdapter adapter;
    private String approvalType = "";

    public static Configuration getConfigInstance() {
        if (null == config) {
            config = new Configuration();
        }
        return config;
    }

    public int getXml(String cardId) {
        int xml = 0;
        switch (cardId) {

            case "csc_approval":

                xml = R.layout.csc_approval;

                break;

            case "recruitment_requisition_approval":
                xml = R.layout.recruitment_requisition_approval;

                break;
            case "update_requisition":
                xml = R.layout.update_requisition;

                break;


            case "pms_goalSetting_approval":
                xml = R.layout.pms_goalsetting_approval;

                break;

            case "lms_curriculum_approval":
                xml = R.layout.lms_curriculum_approval;

                break;

            default:
                break;
        }
        return xml;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void configureXml(int xml_id, Context context, final View view, Object data, ConfigModel card, final InboxAdapter.ViewHolder holder) {
        _context = context;
        WindowManager wm = (WindowManager) _context.getSystemService(Context.WINDOW_SERVICE);
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        dateFormatter.setTimeZone(TimeZone.getTimeZone("Etc/UTC"));

        InboxAdapter.ViewHolder viewHolder = holder;
        switch (xml_id) {

            case R.layout.csc_approval: {

                final FeedsDataModel csc_approval = (FeedsDataModel) data;

                TextView userName = (TextView) view.findViewById(R.id.firstName);
                TextView title = (TextView) view.findViewById(R.id.title);
                TextView description = (TextView) view.findViewById(R.id.description);
                TextView created_at = (TextView) view.findViewById(R.id.date1);
                TextView appraisal_approve = (TextView) view.findViewById(R.id.approve);
                TextView appraisal_reject = (TextView) view.findViewById(R.id.reject);
                TextView status = (TextView) view.findViewById(R.id.status);
                MaterialImageView profilePic = (MaterialImageView) view.findViewById(R.id.quickContactBadge1);
                LinearLayout actionLayout = (LinearLayout) view.findViewById(R.id.actionlayout);
                RelativeLayout parent_layout = (RelativeLayout) view.findViewById(R.id.parent_layout);
                ImageView line = (ImageView) view.findViewById(R.id.imageView2);

                if (csc_approval.appraisal_creatoravatarurl != null && !csc_approval.appraisal_creatoravatarurl.isEmpty()) {
                    Util.loadAttachmentImageMaterial(_context, csc_approval.appraisal_creatoravatarurl, profilePic);
                }

                Log.d("type=", "" + approvalType);
                userName.setText(Util.capitalizeWords(csc_approval.appraisal_creatordisplayname));
                title.setText(Util.capitalizeWords(csc_approval.name));
                description.setText(Util.capitalizeSentence(csc_approval.description));
                created_at.setText(TimeUtils.getDate(csc_approval.createdAt));


                for (int a = 0; a < csc_approval.owners.size(); a++) {
                    if (csc_approval.owners.get(a).oemail.equalsIgnoreCase(CWIncturePreferences.getEmail())) {
                        if (csc_approval.owners.get(a).ostatus.equalsIgnoreCase("approved") || csc_approval.owners.get(a).ostatus.equalsIgnoreCase("rejected")) {
                            appraisal_approve.setVisibility(View.GONE);
                            appraisal_reject.setVisibility(View.GONE);
                            actionLayout.setVisibility(View.GONE);
                            if (csc_approval.owners.get(a).ostatus.equalsIgnoreCase("approved")) {
                                status.setVisibility(View.VISIBLE);
                                status.setText("Approved");
                                line.setVisibility(View.GONE);
                                status.setTextColor(_context.getResources().getColor(R.color.status_completed));
                            } else if (csc_approval.owners.get(a).ostatus.equalsIgnoreCase("rejected")) {
                                status.setVisibility(View.VISIBLE);
                                status.setText("Rejected");
                                line.setVisibility(View.GONE);
                                status.setTextColor(_context.getResources().getColor(R.color.status_rejected));
                            }

                        } else {
                            status.setVisibility(View.GONE);
                            appraisal_approve.setVisibility(View.VISIBLE);
                            appraisal_reject.setVisibility(View.VISIBLE);
                            line.setVisibility(View.VISIBLE);
                            actionLayout.setVisibility(View.VISIBLE);

                        }
                        break;
                    }

                }


                for (int b = 0; b < csc_approval.owners.size(); b++) {

                    if (csc_approval.owners.get(b).oemail.equals(CWIncturePreferences.getEmail())) {
                        final int finalB = b;
                        parent_layout.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(_context, TemplateDetail.class);
                                intent.putExtra("template_id", csc_approval.appraisal_id);
                                intent.putExtra("workitem_id", csc_approval.id);
                                intent.putExtra("status", csc_approval.owners.get(finalB).ostatus);
                                intent.putExtra("type", approvalType);
                                intent.putExtra("roleName", csc_approval.owners.get(finalB).oRoleName);
                                intent.putExtra("regionName", csc_approval.owners.get(finalB).oRoleRegion);
                                intent.putExtra("hrFunction", csc_approval.owners.get(finalB).oHrFunction);
                                _context.startActivity(intent);


                            }
                        });
                        break;
                    }
                }//end of assignedto arraylist


                if (csc_approval.appraisal_creatoremail.equals(CWIncturePreferences.getEmail())) {

                    parent_layout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            Intent intent = new Intent(_context, TemplateDetail.class);
                            intent.putExtra("template_id", csc_approval.appraisal_id);
                            intent.putExtra("workitem_id", csc_approval.id);
                            intent.putExtra("status", "");
                            intent.putExtra("type", approvalType);
                            intent.putExtra("roleName", csc_approval.appraisal_roleName);
                            intent.putExtra("regionName", csc_approval.appraisal_roleRegion);
                            intent.putExtra("hrFunction", csc_approval.appraisal_roleHrFunction);
                            _context.startActivity(intent);

                        }
                    });
                }

//                    else {
//
//                        parent_layout.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View view) {
//                                Intent intent = new Intent(_context, TemplateDetail.class);
//                                intent.putExtra("template_id", csc_approval.appraisal_id);
//                                intent.putExtra("workitem_id", csc_approval.id);
//                                intent.putExtra("status",csc_approval.status);
//                                intent.putExtra("type", approvalType);
//                                intent.putExtra("roleName",csc_approval.appraisal_roleName);
//                                intent.putExtra("regionName",csc_approval.appraisal_roleRegion);
//                                intent.putExtra("hrFunction",csc_approval.appraisal_roleHrFunction);
//                                _context.startActivity(intent);
//                            }
//                        });
//                        break;
//                    }


                for (int i = 0; i < csc_approval.subscribers.size(); i++) {
                    if (csc_approval.subscribers.get(i).semail.equalsIgnoreCase(CWIncturePreferences.getEmail())) {
                        appraisal_approve.setVisibility(View.GONE);
                        appraisal_reject.setVisibility(View.GONE);
                        actionLayout.setVisibility(View.GONE);
                        if (csc_approval.status.equalsIgnoreCase("Submitted")) {
                            status.setVisibility(View.VISIBLE);
                            status.setText("Submitted");
                            status.setTextColor(_context.getResources().getColor(R.color.status_pending));

                        } else if (csc_approval.status.equalsIgnoreCase("rejected")) {
                            status.setVisibility(View.VISIBLE);
                            status.setText("Rejected");
                            status.setTextColor(_context.getResources().getColor(R.color.status_rejected));
                        } else if (csc_approval.status.equalsIgnoreCase("approved")) {
                            status.setVisibility(View.VISIBLE);
                            status.setText("Approved");
                            status.setTextColor(_context.getResources().getColor(R.color.status_completed));
                        }
                        break;
                    }
                }

                appraisal_approve.setOnClickListener(new Actions(_context, "Approve_AppraisalTemplate", csc_approval.id, ""));
                appraisal_reject.setOnClickListener(new Actions(_context, "Reject_AppraisalTemplate", csc_approval.id, ""));

            }//end of csc_approval

            break;

            case R.layout.pms_goalsetting_approval: {
                final FeedsDataModel pms_goalSetting = (FeedsDataModel) data;


                TextView name = (TextView) view.findViewById(R.id.firstName);
                TextView requestedOn = (TextView) view.findViewById(R.id.date1);
                TextView title = (TextView) view.findViewById(R.id.title);
                TextView stage = (TextView) view.findViewById(R.id.stage_value);
                TextView dueDate = (TextView) view.findViewById(R.id.dueDate_value);
                RelativeLayout parent_layout = (RelativeLayout) view.findViewById(R.id.parent_layout);
                TextView status = (TextView) view.findViewById(R.id.status);

                TextView approve_goalSetting = (TextView) view.findViewById(R.id.approve);
                TextView reject_goalSetting = (TextView) view.findViewById(R.id.reject);
                TextView comment_goalSetting = (TextView) view.findViewById(R.id.comment);
                LinearLayout ActionLay = (LinearLayout) view.findViewById(R.id.ActionLay);


                dueDate.setText(TimeUtils.getDate(pms_goalSetting.dueDate));
                name.setText(pms_goalSetting.appraisee_displayname);
                stage.setText(pms_goalSetting.stage);


                approve_goalSetting.setOnClickListener(new Actions(_context, "Approve_AppraisalTemplate", pms_goalSetting.id, "goal setting"));
                reject_goalSetting.setOnClickListener(new Actions(_context, "Reject_AppraisalTemplate", pms_goalSetting.id, "goal setting"));
                comment_goalSetting.setOnClickListener(new Actions(_context, "Comment_GoalSetting", pms_goalSetting.id, ""));

                title.setText(pms_goalSetting.name);

                for (int b = 0; b < pms_goalSetting.owners.size(); b++) {

                    if (pms_goalSetting.owners.get(b).oemail.equals(CWIncturePreferences.getEmail())) {
                        final int finalB = b;
                        parent_layout.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(_context, TemplateDetail.class);
                                intent.putExtra("appId", pms_goalSetting.appraisal_id);
                                intent.putExtra("workitem_id", pms_goalSetting.id);
                                intent.putExtra("status", pms_goalSetting.owners.get(finalB).ostatus);
                                intent.putExtra("type", approvalType);
                                _context.startActivity(intent);

                            }
                        });
                        break;
                    }
                }

                for (int i = 0; i < pms_goalSetting.subscribers.size(); i++) {
                    if (pms_goalSetting.subscribers.get(i).semail.equalsIgnoreCase(CWIncturePreferences.getEmail())) {
                        approve_goalSetting.setVisibility(View.GONE);
                        reject_goalSetting.setVisibility(View.GONE);
                        ActionLay.setVisibility(View.GONE);
                        if (pms_goalSetting.status.equalsIgnoreCase("Submitted")) {
                            status.setVisibility(View.VISIBLE);
                            status.setText("Submitted");
                            status.setTextColor(_context.getResources().getColor(R.color.status_pending));

                        } else if (pms_goalSetting.status.equalsIgnoreCase("rejected")) {
                            status.setVisibility(View.VISIBLE);
                            status.setText("Rejected");
                            status.setTextColor(_context.getResources().getColor(R.color.status_rejected));
                        } else if (pms_goalSetting.status.equalsIgnoreCase("approved")) {
                            status.setVisibility(View.VISIBLE);
                            status.setText("Approved");
                            status.setTextColor(_context.getResources().getColor(R.color.status_completed));
                        }
                        break;
                    }
                }

                for (int a = 0; a < pms_goalSetting.owners.size(); a++) {
                    if (pms_goalSetting.owners.get(a).oemail.equalsIgnoreCase(CWIncturePreferences.getEmail())) {
                        if (pms_goalSetting.owners.get(a).ostatus.equalsIgnoreCase("approved") || pms_goalSetting.owners.get(a).ostatus.equalsIgnoreCase("rejected")) {
                            approve_goalSetting.setVisibility(View.GONE);
                            reject_goalSetting.setVisibility(View.GONE);
                            ActionLay.setVisibility(View.GONE);
                            if (pms_goalSetting.owners.get(a).ostatus.equalsIgnoreCase("approved")) {
                                status.setVisibility(View.VISIBLE);
                                status.setText("Approved");
                                // line.setVisibility(View.GONE);
                                status.setTextColor(_context.getResources().getColor(R.color.status_completed));
                            } else if (pms_goalSetting.owners.get(a).ostatus.equalsIgnoreCase("rejected")) {
                                status.setVisibility(View.VISIBLE);
                                status.setText("Rejected");
                                // line.setVisibility(View.GONE);
                                status.setTextColor(_context.getResources().getColor(R.color.status_rejected));
                            }

                        } else {
                            status.setVisibility(View.GONE);
                            approve_goalSetting.setVisibility(View.VISIBLE);
                            reject_goalSetting.setVisibility(View.VISIBLE);
                            //line.setVisibility(View.VISIBLE);
                            ActionLay.setVisibility(View.VISIBLE);

                        }
                        break;
                    }

                }
            }

            break;

            case R.layout.recruitment_requisition_approval: {
                final FeedsDataModel recruitment = (FeedsDataModel) data;

                TextView position_requestedBy = (TextView) view.findViewById(R.id.firstName);
                TextView position_requested = (TextView) view.findViewById(R.id.position_requested);
                TextView position_requestedOn = (TextView) view.findViewById(R.id.date1);
                TextView requisition_approve = (TextView) view.findViewById(R.id.approve);
                TextView requisition_reject = (TextView) view.findViewById(R.id.reject);
                final TextView comment_recruitment = (TextView) view.findViewById(R.id.comment);
                TextView description = (TextView) view.findViewById(R.id.description);
                TextView status = (TextView) view.findViewById(R.id.status);
                ImageView line = (ImageView) view.findViewById(R.id.imageView2);
                LinearLayout actionlayout = (LinearLayout) view.findViewById(R.id.actionlayout);
                MaterialImageView profilePic = (MaterialImageView) view.findViewById(R.id.quickContactBadge1);
                RelativeLayout parentLayout = (RelativeLayout) view.findViewById(R.id.parent_layout);
                RelativeLayout commentLayout = (RelativeLayout) view.findViewById(R.id.commentLayout);
                TextView commentedBy = (TextView) view.findViewById(R.id.FirstNameComment);
                TextView comment_text = (TextView) view.findViewById(R.id.SecondNameComment);
                MaterialImageView imageView1 = (MaterialImageView) view.findViewById(R.id.imageView1);
                View comment_action_divider = (View) view.findViewById(R.id.comment_action_divider);
                LinearLayout name_layout = (LinearLayout) view.findViewById(R.id.name_layout);

                if (recruitment.subtype.equalsIgnoreCase("requisition")) {
                    comment_recruitment.setVisibility(View.VISIBLE);
                } else {
                    comment_recruitment.setVisibility(View.GONE);
                }

                if (recruitment.recruitment_req_creatoravatarurl != null && !recruitment.recruitment_req_creatoravatarurl.isEmpty()) {
                    Util.loadAttachmentImageMaterial(_context, recruitment.recruitment_req_creatoravatarurl, profilePic);
                }

                position_requested.setText(recruitment.recruitment_position_requested);

                if (recruitment.subtype.equalsIgnoreCase("offerrollout")) {
                    profilePic.setVisibility(View.GONE);
                    position_requestedBy.setText("Offer Rollout");
                    position_requestedOn.setText("");
                } else {
                    profilePic.setVisibility(View.VISIBLE);

                    if (recruitment.recruitment_req_creatordisplayname.isEmpty()) {
                        position_requestedBy.setText(recruitment.creatordisplayname);
                    } else {
                        position_requestedBy.setText(recruitment.recruitment_req_creatordisplayname);
                    }
                }


                position_requestedOn.setText(TimeUtils.getDate(recruitment.createdAt));
                if (recruitment.subtype.equalsIgnoreCase("offerrollout")) {
                    description.setText("Offer letter approval for " + recruitment.candidate_fname + " " + recruitment.candidate_lname + " " + "for the position of " + recruitment.recruitment_position_requested);
                } else if (recruitment.subtype.equalsIgnoreCase("jobApplicationException")) {
                    description.setText(recruitment.candidate_fname + " " + recruitment.candidate_lname + " has requested for an exception " + recruitment.recruitment_position_requested);
                } else if (recruitment.subtype.equalsIgnoreCase("updateRequisition")) {
                    description.setText("Assign position number for a new Non-Budgeted requisition " + recruitment.recruitment_position_requested);
                } else {
                    description.setText("Created a new position requisition for " + recruitment.recruitment_position_requested);
                }
                line.setVisibility(View.VISIBLE);


                for (int b = 0; b < recruitment.owners.size(); b++) {

                    if (recruitment.owners.get(b).oemail.equals(CWIncturePreferences.getEmail())) {
                        final int finalB = b;
                        parentLayout.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(_context, RecruitmentWorkitemDetail.class);
                                intent.putExtra("req_id", recruitment.requestId);
                                intent.putExtra("workitem_id", recruitment.id);
                                intent.putExtra("subType", recruitment.subtype);
                                intent.putExtra("status", recruitment.owners.get(finalB).ostatus);
                                intent.putExtra("roleName", recruitment.owners.get(finalB).oRoleName);
                                intent.putExtra("regionName", recruitment.owners.get(finalB).oRoleRegion);
                                intent.putExtra("hrFunction", recruitment.owners.get(finalB).oHrFunction);
                                _context.startActivity(intent);

                            }
                        });
                        break;
                    }
//                    else {
//
//                        parentLayout.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View view) {
//                                Intent intent = new Intent(_context, RecruitmentWorkitemDetail.class);
//                                intent.putExtra("req_id", recruitment.requestId);
//                                intent.putExtra("workitem_id", recruitment.id);
//                                intent.putExtra("status",recruitment.status);
//                                intent.putExtra("subType",recruitment.subtype);
//                                intent.putExtra("roleName",recruitment.appraisal_roleName);
//                                intent.putExtra("regionName",recruitment.appraisal_roleRegion);
//                                intent.putExtra("hrFunction",recruitment.appraisal_roleHrFunction);
//                                _context.startActivity(intent);
//                            }
//                        });
//                        break;
//                    }
                }

                for (int a = 0; a < recruitment.owners.size(); a++) {
                    if (recruitment.owners.get(a).oemail.equalsIgnoreCase(CWIncturePreferences.getEmail())) {
                        if (recruitment.owners.get(a).ostatus.equalsIgnoreCase("approved") || recruitment.owners.get(a).ostatus.equalsIgnoreCase("rejected")) {
                            requisition_approve.setVisibility(View.GONE);
                            requisition_reject.setVisibility(View.GONE);
                            actionlayout.setVisibility(View.GONE);
                            if (recruitment.owners.get(a).ostatus.equalsIgnoreCase("approved")) {
                                status.setVisibility(View.VISIBLE);
                                status.setText("Approved");
                                line.setVisibility(View.GONE);
                                status.setTextColor(_context.getResources().getColor(R.color.status_completed));
                            } else if (recruitment.owners.get(a).ostatus.equalsIgnoreCase("rejected")) {
                                status.setVisibility(View.VISIBLE);
                                status.setText("Rejected");
                                line.setVisibility(View.GONE);
                                status.setTextColor(_context.getResources().getColor(R.color.status_rejected));
                            }

                        } else {
                            status.setVisibility(View.GONE);
                            requisition_approve.setVisibility(View.VISIBLE);
                            requisition_reject.setVisibility(View.VISIBLE);
                            line.setVisibility(View.VISIBLE);
                            actionlayout.setVisibility(View.VISIBLE);

                        }
                        break;
                    }

                }

                for (int i = 0; i < recruitment.subscribers.size(); i++) {
                    if (recruitment.subscribers.get(i).semail.equalsIgnoreCase(CWIncturePreferences.getEmail())) {
                        requisition_approve.setVisibility(View.GONE);
                        requisition_reject.setVisibility(View.GONE);
                        comment_recruitment.setVisibility(View.GONE);
                        actionlayout.setVisibility(View.GONE);
                        line.setVisibility(View.GONE);
                        if (recruitment.status.equalsIgnoreCase("Submitted")) {
                            status.setVisibility(View.VISIBLE);
                            status.setText("Submitted");
                            status.setTextColor(_context.getResources().getColor(R.color.status_pending));

                        } else if (recruitment.status.equalsIgnoreCase("rejected")) {
                            status.setVisibility(View.VISIBLE);
                            status.setText("Rejected");
                            status.setTextColor(_context.getResources().getColor(R.color.status_rejected));
                        } else if (recruitment.status.equalsIgnoreCase("approved")) {
                            status.setVisibility(View.VISIBLE);
                            status.setText("Approved");
                            status.setTextColor(_context.getResources().getColor(R.color.status_completed));
                        }
                        break;
                    }
                }

                comment_recruitment.setOnClickListener(new Actions(_context, "COMMENT", recruitment, ""));

                requisition_approve.setOnClickListener(new Actions(_context, "Approve_RecruitmentRequisition", recruitment.id, ""));
                requisition_reject.setOnClickListener(new Actions(_context, "Reject_RecruitmentRequisition", recruitment.id, ""));


                if (recruitment.comments.size() > 0) {
                    Util.loadAttachmentImageMaterial(_context, recruitment.comments.get(recruitment.comments.size() - 1).useravatarurl, imageView1);
                    commentedBy.setText(recruitment.comments.get(recruitment.comments.size() - 1).userdisplayname);
                    comment_text.setText(recruitment.comments.get(recruitment.comments.size() - 1).body);
                    commentLayout.setVisibility(View.VISIBLE);
                    comment_action_divider.setVisibility(View.VISIBLE);
                } else {
                    commentLayout.setVisibility(View.GONE);
                }


            }//end of recruitment requisition approval

            break;

            case R.layout.update_requisition: {
                final FeedsDataModel update_requisition = (FeedsDataModel) data;

                TextView description = (TextView) view.findViewById(R.id.description);
                TextView pos_requested = (TextView) view.findViewById(R.id.pos_requested);
                TextView status = (TextView) view.findViewById(R.id.status);
                RelativeLayout parent_layout = (RelativeLayout) view.findViewById(R.id.parent_layout);


                status.setText(update_requisition.status);
                // description.setText();
                pos_requested.setText(update_requisition.recruitment_position_requested);

                parent_layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent updateReq_intent = new Intent(_context, RecruitmentWorkitemDetail.class);
                        updateReq_intent.putExtra("req_id", update_requisition.requestId);
                        updateReq_intent.putExtra("workitem_id", update_requisition.id);
                        updateReq_intent.putExtra("status", update_requisition.status);
                        updateReq_intent.putExtra("subType", update_requisition.subtype);
                        updateReq_intent.putExtra("roleName", update_requisition.owners.get(0).oRoleName);
                        updateReq_intent.putExtra("regionName", update_requisition.owners.get(0).oRoleRegion);
                        updateReq_intent.putExtra("hrFunction", update_requisition.owners.get(0).oHrFunction);
                        _context.startActivity(updateReq_intent);

                    }
                });


            }//end of updateRequisition

            break;

            default:
                break;

        }
    }

    public void setperspective(String perspect) {

        perspective = perspect;
    }

    public void setAdapter(InboxAdapter adapter) {

        this.adapter = adapter;
    }

    public InboxAdapter getAdapter() {

        return this.adapter;
    }

    public void approvalType(String type) {
        approvalType = type;
    }

}

