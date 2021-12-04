package Model;

/**
 * Created by Harshitha on 05-02-2016.
 */
public class OfflineAction {

    String action_type = "";
    String workitem_id = "";
    String text = "";

    public String getAction_type() {
        return action_type;
    }

    public void setAction_type(String action_type) {
        this.action_type = action_type;
    }

    public String getWorkitem_id() {
        return workitem_id;
    }

    public void setWorkitem_id(String workitem_id) {
        this.workitem_id = workitem_id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
