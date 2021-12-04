package Model;

import java.util.ArrayList;

/**
 * Created by Deeksha on 11-01-2016.
 */
public class UserProfile {

    String name = "";
    String email = "";
    String designation = "";
    String profile_img = "";
    String phone = "";

    ArrayList<UserProfileDetail> personalDetails = new ArrayList<UserProfileDetail>();
    ArrayList<UserProfileDetail> experienceDetails = new ArrayList<UserProfileDetail>();
    ArrayList<UserProfileDetail> certificationDetails = new ArrayList<UserProfileDetail>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getProfile_img() {
        return profile_img;
    }

    public void setProfile_img(String profile_img) {
        this.profile_img = profile_img;
    }

    public ArrayList<UserProfileDetail> getPersonalDetails() {
        return personalDetails;
    }


    public ArrayList<UserProfileDetail> getExperienceDetails() {
        return experienceDetails;
    }

    public void setExperienceDetails(ArrayList<UserProfileDetail> experienceDetails) {
        this.experienceDetails = experienceDetails;
    }

    public ArrayList<UserProfileDetail> getCertificationDetails() {
        return certificationDetails;
    }

    public void setCertificationDetails(ArrayList<UserProfileDetail> certificationDetails) {
        this.certificationDetails = certificationDetails;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhone() {
        return phone;
    }
}
