package com.hrapps.eBAT;

import java.io.Serializable;
import java.util.ArrayList;
/**
 * Created by Harshitha.bshekharap on 11/16/2017.
 */

public class KRAModel implements Serializable {
    private String goalName = "";
    private String weightage = "";
    private ArrayList<String> keyDeliverable = new ArrayList<>();
    private String achievementLow = "";
    private String achievementMax = "";
    private String achievementHigh = "";
    private String kraId = "";

    public String getGoalName() {
        return goalName;
    }

    public void setGoalName(String goalName) {
        this.goalName = goalName;
    }

    public String getWeightage() {
        return weightage;
    }

    public void setWeightage(String weightage) {
        this.weightage = weightage;
    }

    public ArrayList<String> getKeyDeliverable() {
        return keyDeliverable;
    }

    public void setKeyDeliverable(ArrayList<String> keyDeliverable) {
        this.keyDeliverable = keyDeliverable;
    }

    public String getAchievementLow() {
        return achievementLow;
    }

    public void setAchievementLow(String achievementLow) {
        this.achievementLow = achievementLow;
    }

    public String getAchievementMax() {
        return achievementMax;
    }

    public void setAchievementMax(String achievementMax) {
        this.achievementMax = achievementMax;
    }

    public String getAchievementHigh() {
        return achievementHigh;
    }

    public void setAchievementHigh(String achievementHigh) {
        this.achievementHigh = achievementHigh;
    }

    public String getKraId() {
        return kraId;
    }

    public void setKraId(String kraId) {
        this.kraId = kraId;
    }
}

