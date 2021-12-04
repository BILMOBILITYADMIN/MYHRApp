package com.hrapps.eBAT;

import java.util.ArrayList;

import Model.UserProfile;

/**
 * Created by Harshitha.bshekharap on 11/16/2017.
 */

public class AssessmentModel {
    private String id = "";
    private UserProfile appraisee;
    private String currentStage = "";
    private String status = "";
    private ArrayList<KRAModel> kraList;
    private String submittedDate = "";
    private String assessmentYear = "";


    public UserProfile getAppraisee() {
        if (appraisee == null) {
            appraisee = new UserProfile();
        }
        return appraisee;

    }

    public void setAppraisee(UserProfile appraisee) {
        this.appraisee = appraisee;
    }

    public String getCurrentStage() {
        return currentStage;
    }

    public void setCurrentStage(String currentStage) {
        this.currentStage = currentStage;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ArrayList<KRAModel> getKraList() {
        return kraList;
    }

    public void setKraList(ArrayList<KRAModel> kraList) {
        this.kraList = kraList;
    }

    public String getSubmittedDate() {
        return submittedDate;
    }

    public void setSubmittedDate(String submittedDate) {
        this.submittedDate = submittedDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAssessmentYear() {
        return assessmentYear;
    }

    public void setAssessmentYear(String assessmentYear) {
        this.assessmentYear = assessmentYear;
    }
}

