package Model;

import java.io.Serializable;
import java.util.ArrayList;

public class CommentModel implements Serializable {

    public String time = "";
    public String body = "";
    public String approval = "";
    public String ostatus = "";

    public String getoRoleName() {
        return oRoleName;
    }

    public void setoRoleName(String oRoleName) {
        this.oRoleName = oRoleName;
    }

    public String getoRoleRegion() {
        return oRoleRegion;
    }

    public void setoRoleRegion(String oRoleRegion) {
        this.oRoleRegion = oRoleRegion;
    }

    public String getoHrFunction() {
        return oHrFunction;
    }

    public void setoHrFunction(String oHrFunction) {
        this.oHrFunction = oHrFunction;
    }

    public String oRoleName = "";
    public String oRoleRegion = "";
    public String oHrFunction = "";

    public String getDeleted() {
        return deleted;
    }

    public void setDeleted(String deleted) {
        this.deleted = deleted;
    }

    public String deleted = "";
    public String userid = "";
    public String useremail = "";
    public String userphoneno = "";
    public String userdisplayname = "";
    public String useravatarurl = "";


    public ArrayList<AttachmentModel> getAttachments() {
        return attachments;
    }

    public void setAttachments(ArrayList<AttachmentModel> attachments) {
        this.attachments = attachments;
    }

    ArrayList<AttachmentModel> attachments = new ArrayList<AttachmentModel>();

    public String getUserdisplayname() {
        return userdisplayname;
    }

    public void setUserdisplayname(String userdisplayname) {
        this.userdisplayname = userdisplayname;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String id = "";
    public String oemail = "";
    public String oid = "";
    public String odisplayName = "";
    public String oavatarUrl = "";
    public String ophoneNo = "";
    public String semail = "";
    public String sid = "";
    public String sdisplayName = "";
    public String savatarUrl = "";
    public String sphoneNo = "";
    public String USER_TEXT = "";
    public String text = "";
}
