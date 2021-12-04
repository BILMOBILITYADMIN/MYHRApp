package Model;


import org.json.JSONArray;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Arun on 28-09-2015.
 */
public class FeedsDataModel implements Serializable {

    public String name = "";
    public String message = "";
    public String content = "";
    public ArrayList<AttachmentModel> attachmentwithcomments = new ArrayList<>();
    public CommentModel assigned = new CommentModel();
    public String ucreatorid = "";
    public String ucreatoremail = "";
    public String ucreatorphoneno = "";
    public String ucreatordisplayname = "";
    public String ucreatoravatarurl = "";
    public FeedsDataModel all;
    public String type = "";
    public String title = "";
    public String description = "";
    public String source = "";
    public String createdAt = "";

    public String Public = "";
    public String id = "";
    public String lastModified = "";
    public String status = "";
    public String deleted = "";
    public String time = "";
    public String activity = "";

    public String displayname = "";
    public String avatarurl = "";
    public ArrayList<AttachmentModel> attachments = new ArrayList<AttachmentModel>();
    public ArrayList<CommentModel> comments = new ArrayList<>();
    public String creatorid = "";
    public String creatoremail = "";
    public String creatorphoneno = "";
    public String creatoravatarurl = "";
    public String creatordisplayname = "";
    public String date = "";
    public boolean isEdited = false;
    public String body = "";

    public String action = "";
    public ArrayList<LikeModel> likes = new ArrayList<LikeModel>();
    public ArrayList<MediaModel> media = new ArrayList<MediaModel>();

    public ArrayList<CommentModel> subscribers = new ArrayList<CommentModel>();
    public ArrayList<CommentModel> owners = new ArrayList<CommentModel>();
    public String subtype = "";
    public String value = "";
    public boolean liked = false;
    public String header = "";
    public String bpmCreatedTime = "";
    public String bpmId = "";
    public String bpmModelId = "";
    public String bpmName = "";
    public String bpmPriority = "";
    public String bpmStatus = "";
    public String bpmSubject = "";
    public String bpmTaskInitiator = "";
    public String bpmTaskType = "";
    public String requestId = "";
    public String vendor = "";
    public String companyCode = "";
    public String currency = "";


    public boolean active = false;
    public String workflow = "";
    public String validity_from = "";
    public String validity_to = "";
    public String sections_name = "";
    public String sections_description = "";
    public String sections_measurement = "";
    public boolean sections_overallScore = false;
    public String creatorDesignation = "";
    public String updatedAt = "";
    public String appraisal_id;
    public String appraisal_creatorid = "";
    public String appraisal_creatordisplayname = "";
    public String appraisal_creatoravatarurl = "";
    public String appraisal_creatorDesignation = "";
    public String appraisal_creatorphoneno = "";
    public String appraisal_creatoremail = "";
    public String appraisal_deleted = "";
    public String content_status = "";
    public String recruitment_req_creatorid = "";
    public String recruitment_req_creatordisplayname = "";
    public String recruitment_req_creatoravatarurl = "";
    public String recruitment_position_requested = "";
    public String dueDate = "";
    public String stage = "";
    public String appraisee_id = "";
    public String appraisee_displayname = "";
    public String appraisee_avatar = "";
    public String appraisee_designation = "";
    public String appraisee_phoneno = "";
    public String appraisee_email = "";

    //LMS - Curriculum Approval Template
    public String curriculum_id = "";
    public String curriculumName = "";
    public String preRequisite = "";
    public JSONArray mandatoryFunctions = new JSONArray();
    public JSONArray recommendedFunctions = new JSONArray();
    public JSONArray complianceFor = new JSONArray();
    public String managerApproval = "";
    public String workFlowApproval = "";
    public String workFlowApprovalUnit = "";
    public String costPerEmployee = "";
    public String costPerEmpUnit = "";
    public String certificateTemplate = "";
    public String certificateValidity = "";
    public String validityInMonths = "";
    public String certificationType = "";
    public String evaluationForm = "";
    public String feedbackForm = "";
    public String emailTemplate = "";
    public String manualMailTrigger = "";
    public JSONArray courses = new JSONArray();
    public String createdBy = "";
    public String curriculumId = "";
    public String totalTrainingHours = "";
    public String coverPic = "";
    public String updatedBy = "";

    public String courseTitle = "";
    public String courseId = "";
    public String courses_id = "";
    public String hours = "";
    public String mode = "";
    public String courseType = "";
    public String appraisal_roleName = "";
    public String appraisal_roleRegion = "";
    public String appraisal_roleHrFunction = "";
    public String candidate_fname = "";
    public String candidate_lname = "";


    /**
     * @return the body
     */
    public String getBody() {
        return body;
    }

    /**
     * @param body the body to set
     */
    public void setBody(String body) {
        this.body = body;
    }


    /**
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the source
     */
    public String getSource() {
        return source;
    }

    /**
     * @param source the source to set
     */
    public void setSource(String source) {
        this.source = source;
    }

    /**
     * @return the createdAt
     */
    public String getCreatedAt() {
        return createdAt;
    }

    /**
     * @param createdAt the createdAt to set
     */
    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }


    public String getPublic() {
        return Public;
    }

    /**
     * @param public1 the public to set
     */
    public void setPublic(String public1) {
        Public = public1;
    }

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }


    /**
     * @return the status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * @return the deleted
     */
    public String getDeleted() {
        return deleted;
    }

    /**
     * @param deleted the deleted to set
     */
    public void setDeleted(String deleted) {
        this.deleted = deleted;
    }

    /**
     * @return the time
     */
    public String getTime() {
        return time;
    }

    /**
     * @param time the time to set
     */
    public void setTime(String time) {
        this.time = time;
    }

    /**
     * @return the activity
     */
    public String getActivity() {
        return activity;
    }

    /**
     * @param activity the activity to set
     */
    public void setActivity(String activity) {
        this.activity = activity;
    }

    /**
     * @return the activityLogArrayId
     */

    public String getDisplayname() {
        return displayname;
    }

    /**
     * @param displayname the displayname to set
     */
    public void setDisplayname(String displayname) {
        this.displayname = displayname;
    }

    /**
     * @return the avatarurl
     */
    public String getAvatarurl() {
        return avatarurl;
    }

    /**
     * @param avatarurl the avatarurl to set
     */
    public void setAvatarurl(String avatarurl) {
        this.avatarurl = avatarurl;
    }

    /**
     * @return the attachments
     */
    public ArrayList<AttachmentModel> getAttachments() {
        return attachments;
    }

    /**
     * @param attachments the attachments to set
     */
    public void setAttachments(ArrayList<AttachmentModel> attachments) {
        this.attachments = attachments;
    }

    /**
     * @return the comments
     */
    public ArrayList<CommentModel> getComments() {
        return comments;
    }

    /**
     * @param comments the comments to set
     */
    public void setComments(ArrayList<CommentModel> comments) {
        this.comments = comments;
    }


    /**
     * @return the date
     */
    public String getDate() {
        return date;
    }

    /**
     * @param date the date to set
     */
    public void setDate(String date) {
        this.date = date;
    }


    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public boolean isLiked() {
        return liked;
    }

    public void setLiked(boolean liked) {
        this.liked = liked;
    }

    public void setShowlikes(boolean showlikes) {
        this.showlikes = showlikes;
    }

    public boolean showlikes = true;


    public int getLikescount() {
        return likescount;
    }

    public void setLikescount(int likescount) {
        this.likescount = likescount;
    }

    public int likescount = 0;

    public int getCommentsCount() {
        return commentsCount;
    }

    public void setCommentsCount(int commentsCount) {
        this.commentsCount = commentsCount;
    }

    public int commentsCount = 0;

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getWorkflow() {
        return workflow;
    }

    public void setWorkflow(String workflow) {
        this.workflow = workflow;
    }


    public String getValidity_to() {
        return validity_to;
    }

    public void setValidity_to(String validity_to) {
        this.validity_to = validity_to;
    }

    public String getValidity_from() {
        return validity_from;
    }

    public void setValidity_from(String validity_from) {
        this.validity_from = validity_from;
    }

    public String getSections_name() {
        return sections_name;
    }

    public void setSections_name(String sections_name) {
        this.sections_name = sections_name;
    }

    public String getSections_description() {
        return sections_description;
    }

    public void setSections_description(String sections_description) {
        this.sections_description = sections_description;
    }

    public String getCriteria_name() {
        return criteria_name;
    }

    public void setCriteria_name(String criteria_name) {
        this.criteria_name = criteria_name;
    }

    public String getCriteria_description() {
        return criteria_description;
    }

    public void setCriteria_description(String criteria_description) {
        this.criteria_description = criteria_description;
    }

    public String criteria_name = "";
    public String criteria_description = "";

    public String getSections_measurement() {
        return sections_measurement;
    }

    public void setSections_measurement(String sections_measurement) {
        this.sections_measurement = sections_measurement;
    }

    public boolean isSections_overallScore() {
        return sections_overallScore;
    }

    public void setSections_overallScore(boolean sections_overallScore) {
        this.sections_overallScore = sections_overallScore;
    }

    public String getCreatorDesignation() {
        return creatorDesignation;
    }

    public void setCreatorDesignation(String creatorDesignation) {
        this.creatorDesignation = creatorDesignation;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

}


