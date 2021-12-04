package Model;

public class NotificationsModel {

    public String id = "";
    public String message = "";
    public String workitem_id = "";
    public String sender_id = "";
    public String sender_displayName = "";
    public String sender_avatarurl = "";
    public String createdAt = "";
    public String workitem_status = "";
    public String applicationType = "";
    public String timesheetStartDate = "";
    public String timesheetendDate = "";
    public String time = "";
    public Object text = "";
    public String action = "";
    public String badgeCount = "";
    public String timesheetStatus = "";
    public String timesheetId = "";
    public String workitemId = "";
    public String notification_workitem_id;

    public String appraisalTemplate_id = "";
    public String appraisalTemplate_workitemId = "";
    public String appraisalTemplate_type = "";
    public String validity_from = "";
    public String validity_to = "";

    public Boolean read_status;
    public String receiver_avatarurl = "";
    public String receiver_displayName = "";
    public String updatedBy_avatarurl = "";
    public String updatedBy_displayName = "";
    public String appraisal_status = "";
    public String appraisalProcess_stage = "";
    public String appraisalProcess_type = "";
    public String appraisalProcess_id = "";
    public String appraisalname = "";
    public String sender_roleName = "";
    public String sender_roleRegion = "";
    public String sender_hrfunction = "";
    public String notification_cherryName = "";
    public String receiver_roleName = "";
    public String receiver_roleRegion = "";
    public String receiver_hrfunction = "";
    public String onBoarding_type = "";
    public String onBoarding_name = "";
    public String onBoarding_formId = "";
    public String onBoarding_evaluationPeriod = "";
    public String onBoarding_currentStage = "";
    public String applicationId = "";
    public String evaluationId = "";
    public String candidateName = "";
    public String generateOffer_avatar = "";
    public String generateOffer_reason = "";
    public String offerId = "";
    public String candidate_dob = "";
    public String candidate_lname = "";
    public String candidate_fname = "";
    public String candidate_email = "";

    public String leave_id = "";
    public String leave_evaluation_period = "";
    public String leave_ep_from = "";
    public String leave_ep_to = "";
    public String leave_name = "";
    public String leave_currentStage="";

    public String getLeave_id() {
        return leave_id;
    }

    public void setLeave_id(String leave_id) {
        this.leave_id = leave_id;
    }

    public String getLeave_evaluation_period() {
        return leave_evaluation_period;
    }

    public void setLeave_evaluation_period(String leave_evaluation_period) {
        this.leave_evaluation_period = leave_evaluation_period;
    }

    public String getLeave_ep_from() {
        return leave_ep_from;
    }

    public void setLeave_ep_from(String leave_ep_from) {
        this.leave_ep_from = leave_ep_from;
    }

    public String getLeave_ep_to() {
        return leave_ep_to;
    }

    public void setLeave_ep_to(String leave_ep_to) {
        this.leave_ep_to = leave_ep_to;
    }

    public String getLeave_name() {
        return leave_name;
    }

    public void setLeave_name(String leave_name) {
        this.leave_name = leave_name;
    }

    public String getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }

    public String getEvaluationId() {
        return evaluationId;
    }

    public void setEvaluationId(String evaluationId) {
        this.evaluationId = evaluationId;
    }

    public String getCandidateName() {
        return candidateName;
    }

    public void setCandidateName(String candidateName) {
        this.candidateName = candidateName;
    }

    public String getGenerateOffer_avatar() {
        return generateOffer_avatar;
    }

    public void setGenerateOffer_avatar(String generateOffer_avatar) {
        this.generateOffer_avatar = generateOffer_avatar;
    }

    public String getGenerateOffer_reason() {
        return generateOffer_reason;
    }

    public void setGenerateOffer_reason(String generateOffer_reason) {
        this.generateOffer_reason = generateOffer_reason;
    }

    public String getOfferId() {
        return offerId;
    }

    public void setOfferId(String offerId) {
        this.offerId = offerId;
    }

    public String getRequisition_type() {
        return requisition_type;
    }

    public void setRequisition_type(String requisition_type) {
        this.requisition_type = requisition_type;
    }

    public String requisition_type = "";

    public String getRequisitionId() {
        return requisitionId;
    }

    public void setRequisitionId(String requisitionId) {
        this.requisitionId = requisitionId;
    }

    public String getPositionName() {
        return positionName;
    }

    public void setPositionName(String positionName) {
        this.positionName = positionName;
    }

    public String getWorkLocation() {
        return workLocation;
    }

    public void setWorkLocation(String workLocation) {
        this.workLocation = workLocation;
    }

    public String getRequisition_workitemId() {
        return requisition_workitemId;
    }

    public void setRequisition_workitemId(String requisition_workitemId) {
        this.requisition_workitemId = requisition_workitemId;
    }

    public String requisitionId = "";
    public String positionName = "";
    public String workLocation = "";
    public String requisition_workitemId = "";

    public String getAppraisalTemplate_workitemId() {
        return appraisalTemplate_workitemId;
    }

    public void setAppraisalTemplate_workitemId(String appraisalTemplate_workitemId) {
        this.appraisalTemplate_workitemId = appraisalTemplate_workitemId;
    }

    public String getAppraisalTemplate_id() {
        return appraisalTemplate_id;
    }

    public void setAppraisalTemplate_id(String appraisalTemplate_id) {
        this.appraisalTemplate_id = appraisalTemplate_id;
    }

    public String getAppraisalTemplate_type() {
        return appraisalTemplate_type;
    }

    public void setAppraisalTemplate_type(String appraisalTemplate_type) {
        this.appraisalTemplate_type = appraisalTemplate_type;
    }

    public String getValidity_from() {
        return validity_from;
    }

    public void setValidity_from(String validity_from) {
        this.validity_from = validity_from;
    }

    public String getValidity_to() {
        return validity_to;
    }

    public void setValidity_to(String validity_to) {
        this.validity_to = validity_to;
    }

    public Boolean getRead_status() {
        return read_status;
    }

    public void setRead_status(Boolean read_status) {
        this.read_status = read_status;
    }

    public String getUpdatedBy_id() {
        return updatedBy_id;
    }

    public void setUpdatedBy_id(String updatedBy_id) {
        this.updatedBy_id = updatedBy_id;
    }

    public String getUpdatedBy_avatarurl() {
        return updatedBy_avatarurl;
    }

    public void setUpdatedBy_avatarurl(String updatedBy_avatarurl) {
        this.updatedBy_avatarurl = updatedBy_avatarurl;
    }

    public String getUpdatedBy_displayName() {
        return updatedBy_displayName;
    }

    public void setUpdatedBy_displayName(String updatedBy_displayName) {
        this.updatedBy_displayName = updatedBy_displayName;
    }

    public String updatedBy_id;

    public String getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(String updateAt) {
        this.updateAt = updateAt;
    }

    public String updateAt;

    public String getReceiver_id() {
        return receiver_id;
    }

    public void setReceiver_id(String receiver_id) {
        this.receiver_id = receiver_id;
    }

    public String getReceiver_avatarurl() {
        return receiver_avatarurl;
    }

    public void setReceiver_avatarurl(String receiver_avatarurl) {
        this.receiver_avatarurl = receiver_avatarurl;
    }

    public String getReceiver_displayName() {
        return receiver_displayName;
    }

    public void setReceiver_displayName(String receiver_displayName) {
        this.receiver_displayName = receiver_displayName;
    }

    public String receiver_id;

    public String getNotification_workitem_id() {
        return notification_workitem_id;
    }

    public void setNotification_workitem_id(String notification_workitem_id) {
        this.notification_workitem_id = notification_workitem_id;
    }


    public String getTimesheetId() {
        return timesheetId;
    }

    public void setTimesheetId(String timesheetId) {
        this.timesheetId = timesheetId;
    }

    public String getWorkitemId() {
        return workitemId;
    }

    public void setWorkitemId(String workitemId) {
        this.workitemId = workitemId;
    }

    public String getTimesheetStatus() {
        return timesheetStatus;
    }

    public void setTimesheetStatus(String timesheetStatus) {
        this.timesheetStatus = timesheetStatus;
    }

    public String getBadgeCount() {
        return badgeCount;
    }

    public void setBadgeCount(String badgeCount) {
        this.badgeCount = badgeCount;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Object getText() {
        return text;
    }

    public void setText(Object text) {
        this.text = text;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String displayName;
    public String avatarUrl;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


}
