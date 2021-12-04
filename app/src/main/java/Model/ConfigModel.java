package Model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Arun on 28-01-2016.
 */
public class ConfigModel implements Serializable {
    public String id = "";
    public String Default = "";
    public ArrayList<ConfigModel> tabs = new ArrayList<>();
    public String status = "";
    public String message = "";
    public String tab_id = "";
    public String icon = "";
    public String displayName = "";
    public ArrayList<ConfigModel> noptions = new ArrayList<ConfigModel>();
    public ArrayList<ConfigModel> navigation = new ArrayList<ConfigModel>();
    public ArrayList<ConfigModel> foptions = new ArrayList<ConfigModel>();
    public ArrayList<ConfigModel> filters = new ArrayList<ConfigModel>();
    public ArrayList<String> types = new ArrayList<>();
    //
    public ArrayList<String> roles = new ArrayList<>();
    public ArrayList<String> roleNames = new ArrayList<>();
    public String roleType = "";
    public String roleName = "";
    public String roleDisplayName = "";
    public String roleText = "";
    public String roleHrFunction = "";
    public String roleRegion = "";
    //
    public ArrayList<String> subtypes = new ArrayList<>();
    public String type = "";
    public String subtype = "";
    public String metaCard = "";
    public ArrayList<String> mandatory = new ArrayList<String>();
    public ArrayList<String> optional = new ArrayList<String>();
    public ArrayList<String> actions = new ArrayList<String>();
    public ArrayList<ConfigModel> cards = new ArrayList<>();
    public String options = "";
    public String cardConfigVersion = "";
    public boolean isSubscribed = false;
    public boolean isMandatory = false;


    public String getMessage() {
        return message;
    }

    public boolean isSubscribed() {
        return isSubscribed;
    }

    public void setIsSubscribed(boolean isSubscribed) {
        this.isSubscribed = isSubscribed;
    }

    public boolean isMandatory() {
        return isMandatory;
    }

    public void setIsMandatory(boolean isMandatory) {
        this.isMandatory = isMandatory;
    }
}
