package com.hrapps.CSC_Britannia;
/**
 * Created by harshu on 11/23/2016.
 */
public class OfficialInfoModel {

    public String getEmp_id() {
        return emp_id;
    }

    public void setEmp_id(String emp_id) {
        this.emp_id = emp_id;
    }

    public String getSap_id() {
        return sap_id;
    }

    public void setSap_id(String sap_id) {
        this.sap_id = sap_id;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String emp_id = "";
    public String sap_id = "";
    public String department = "";
    public String designation = "";
}
