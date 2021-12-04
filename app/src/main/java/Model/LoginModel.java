package Model;

/**
 * Created by Arun on 14-09-2015.
 */
public class LoginModel {

    public String accesstoken = "";
    public String firstname = "";
    public String lastName = "";
    public String avatarUrl = "";
    public String email = "";
    public String userId = "";
    public String status = "";
    public String message = "";
    public String designation;
    public String image = "";
    public String role = "";
    public String role_name = "";
    public String role_type = "";
    public Long position;
    public String reportsTo_id="";
    public String reportsTo_displayName="";
    public String reportsTo_avatar="";
    public String reportsTo_designation="";
    public String reportsTo_mobile="";
    public String reportsTo_email="";
    public boolean reportsTo_deleted= false;
    public String reportsTo_employeeNumber="";

    public Long getPosition() {
        return position;
    }

    public void setPosition(Long position) {
        this.position = position;
    }

    public String getHr_function() {
        return hr_function;
    }

    public void setHr_function(String hr_function) {
        this.hr_function = hr_function;
    }

    public String hr_function = "";

    public String getRolesArray_web() {
        return rolesArray_web;
    }

    public void setRolesArray_web(String rolesArray_web) {
        this.rolesArray_web = rolesArray_web;
    }

    public String rolesArray_web;

    public String getOfficial_region() {
        return official_region;
    }

    public void setOfficial_region(String official_region) {
        this.official_region = official_region;
    }

    public String official_region;

    public String getRole_text() {
        return role_text;
    }

    public void setRole_text(String role_text) {
        this.role_text = role_text;
    }

    public String role_text;

    public String getRole_name() {
        return role_name;
    }

    public void setRole_name(String role_name) {
        this.role_name = role_name;
    }

    public String getRole_region() {
        return role_region;
    }

    public void setRole_region(String role_region) {
        this.role_region = role_region;
    }

    public String role_region;

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }


    public String getAccesstoken() {
        return accesstoken;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastName() {
        return lastName;
    }


    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getUserId() {
        return userId;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDesignation() {
        return designation;
    }
}
