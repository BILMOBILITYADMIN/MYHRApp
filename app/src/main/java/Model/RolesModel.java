package Model;

import java.io.Serializable;

/**
 * Created by Nuzha on 07-03-2017.
 */

public class RolesModel implements Serializable {
    private String role_type = "";
    private String role_name = "";
    private String role_display_name = "";
    private String role_text = "";
    private String role_hr_function = "";
    private String role_region = "";

    public String getRole_type() {
        return role_type;
    }

    public void setRole_type(String role_type) {
        this.role_type = role_type;
    }

    public String getRole_name() {
        return role_name;
    }

    public void setRole_name(String role_name) {
        this.role_name = role_name;
    }


    public String getRole_display_name() {
        return role_display_name;
    }

    public void setRole_display_name(String role_display_name) {
        this.role_display_name = role_display_name;
    }

    public String getRole_text() {
        return role_text;
    }

    public void setRole_text(String role_text) {
        this.role_text = role_text;
    }

    public String getRole_hr_function() {
        return role_hr_function;
    }

    public void setRole_hr_function(String role_hr_function) {
        this.role_hr_function = role_hr_function;
    }

    public String getRole_region() {
        return role_region;
    }

    public void setRole_region(String role_region) {
        this.role_region = role_region;
    }
}
