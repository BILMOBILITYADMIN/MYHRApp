package Utility;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import com.hrapps.MainActivity;
import com.hrapps.R;


import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import DB.DbUtil;
import Model.ConfigModel;
import Model.LoginModel;
import Model.UserPermission;

/**
 * Created by Deeksha on 14-12-2015.
 */
public class NetworkConnector extends AsyncTask<String, Void, String> {
    Context _context;
    public static int TYPE_PUT = 0;
    public static int TYPE_GET = 1;
    public static int TYPE_POST = 2;
    public static int TYPE_DELETE = 3;
    String headerValue;

    private ProgressDialog dialog;
    int type;
    Map<String, String> headers;
    String entity;
    String url;
    private int status_code;
    boolean login = false;
    AsyncResponse listener;
    String cardConfigValue = "";
    private ConfigModel configModel;
    boolean isAllowed = true;
    private ArrayList<ConfigModel> configList, tabList, navigationList, filterList, navigationOptions, filterOptions, cardsList;
    boolean isBackground = false;

    public NetworkConnector(Context context, int type, String url, Map<String, String> headers, String entity, AsyncResponse listener) {
        this._context = context;
        this.type = type;
        this.url = url;
        this.entity = entity;
        this.headers = headers;
        this.listener = listener;

        String module = "";
        if (url.contains("timesheet")) {
            module = "timesheet";
        } else if (url.contains("user")) {
            module = "users";
        } else if (url.contains("group")) {
            module = "groups";
        } else if (url.contains("workitem")) {
            module = "workitems";
        }

        UserPermission perm = new DbUtil().getPermissionForEntity(module, _context);
        switch (type) {
            case 0:
                if (perm.getUpdate().equals("none")) {
                    isAllowed = false;
                }
                break;
            case 1:
                if (perm.getRead().equals("none")) {
                    isAllowed = false;
                }
                break;
            case 2:
                if (perm.getCreate().equals("none")) {
                    isAllowed = false;
                }
                break;
            case 3:
                if (perm.getDelete().equals("none")) {
                    isAllowed = false;
                }
                break;
        }

    }


    public NetworkConnector(Context context, int type, String url, Map<String, String> headers, String entity, AsyncResponse listener, boolean isBackground) {
        this.isBackground = isBackground;
        this._context = context;
        this.type = type;
        this.url = url;
        this.entity = entity;
        this.headers = headers;
        this.listener = listener;

        String module = "";
        if (url.contains("timesheet")) {
            module = "timesheet";
        } else if (url.contains("user")) {
            module = "users";
        } else if (url.contains("group")) {
            module = "groups";
        } else if (url.contains("workitem")) {
            module = "workitems";
        }

        UserPermission perm = new DbUtil().getPermissionForEntity(module, _context);
        switch (type) {
            case 0:
                if (perm.getUpdate().equals("none")) {
                    isAllowed = false;
                }
                break;
            case 1:
                if (perm.getRead().equals("none")) {
                    isAllowed = false;
                }
                break;
            case 2:
                if (perm.getCreate().equals("none")) {
                    isAllowed = false;
                }
                break;
            case 3:
                if (perm.getDelete().equals("none")) {
                    isAllowed = false;
                }
                break;
        }
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (!isBackground) {
            dialog = new ProgressDialog(_context, R.style.AppDialogTheme);
            dialog.setCancelable(true);
            dialog.show();
        }
    }

    @Override
    protected String doInBackground(String[] params) {
        HttpClient httpClient = new DefaultHttpClient();
        HttpResponse httpresponse = null;
        String response = "";
        String actualString = "";
        switch (type) {
            case 0:
                HttpPut httpput = new HttpPut(url);

//                if (!url.contains(CWUrls.LOGIN_URL)){
//                    if (!CWIncturePreferences.getEmail().contains("britindia.com")){
//                        headers.put("type","external");
//                    }
//                }


                if (headers != null) {
                    for (String key : headers.keySet()) {
                        httpput.addHeader(key, headers.get(key));
                    }
                    if (!headers.containsKey("Content-Type")) {
                        httpput.addHeader("Content-Type", "application/json");
                    }
                }


                if (entity != null && !entity.isEmpty()) {
                    try {
                        httpput.setEntity(new StringEntity(entity, "UTF8"));
                        httpresponse = httpClient.execute(httpput);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    } catch (ClientProtocolException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        status_code = 404;
                        actualString = "Connection error";
                    }

                }
                if (httpresponse == null) {

                    try {

                        httpresponse = httpClient.execute(httpput);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    } catch (ClientProtocolException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        status_code = 404;
                        actualString = "Connection error";
                    }
                }
                break;
            case 1:
                HttpGet httpGet = new HttpGet(url);

                if (!url.contains(CWUrls.LOGIN_URL)) {

                    if (!(CWIncturePreferences.getEmail().equalsIgnoreCase("bgv@britindia.com") ||
                            CWIncturePreferences.getEmail().equalsIgnoreCase("medical@britindia.com") ||
                            CWIncturePreferences.getEmail().equalsIgnoreCase("itsupport@britindia.com"))) {
                        if (!CWIncturePreferences.getEmail().contains("britindia.com")) {
                            headers.put("type", "external");
                        }

                    } else {
                        headers.put("type", "external");
                    }
                }

                if (headers != null) {
                    for (String key : headers.keySet()) {
                        httpGet.addHeader(key, headers.get(key));
                    }
                }
                try {
                    httpresponse = httpClient.execute(httpGet);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (ClientProtocolException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    status_code = 404;
                    actualString = "Connection error";
                }

                break;
            case 2:
                HttpPost httppost = new HttpPost(url);

                if (!url.contains(CWUrls.LOGIN_URL)) {

                    if (!(CWIncturePreferences.getEmail().equalsIgnoreCase("bgv@britindia.com") ||
                            CWIncturePreferences.getEmail().equalsIgnoreCase("medical@britindia.com") ||
                            CWIncturePreferences.getEmail().equalsIgnoreCase("itsupport@britindia.com"))) {
                        if (!CWIncturePreferences.getEmail().contains("britindia.com")) {
                            headers.put("type", "external");
                        }

                    } else {
                        headers.put("type", "external");
                    }
                }

                if (headers != null) {
                    for (String key : headers.keySet()) {
                        httppost.addHeader(key, headers.get(key));
                    }
                    if (!headers.containsKey("Content-Type")) {
                        httppost.addHeader("Content-Type", "application/json");
                    }
                }

                if (entity != null && !entity.isEmpty()) {
                    try {
                        httppost.setEntity(new StringEntity(entity, "UTF8"));
                        httpresponse = httpClient.execute(httppost);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    } catch (ClientProtocolException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        status_code = 404;
                        actualString = "Connection error";
                    }
                }

                if (httpresponse == null) {
                    try {
                        httpresponse = httpClient.execute(httppost);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    } catch (ClientProtocolException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        status_code = 404;
                        actualString = "Connection error";
                    }
                }
                break;

            case 3:
                HttpDelete httpDelete = new HttpDelete(url);

                if (!url.contains(CWUrls.LOGIN_URL)) {

                    if (!(CWIncturePreferences.getEmail().equalsIgnoreCase("bgv@britindia.com") ||
                            CWIncturePreferences.getEmail().equalsIgnoreCase("medical@britindia.com") ||
                            CWIncturePreferences.getEmail().equalsIgnoreCase("itsupport@britindia.com"))) {
                        if (!CWIncturePreferences.getEmail().contains("britindia.com")) {
                            headers.put("type", "external");
                        }

                    } else {
                        headers.put("type", "external");
                    }
                }


                if (headers != null) {
                    for (String key : headers.keySet()) {
                        httpDelete.addHeader(key, headers.get(key));
                    }
                }

                if (httpresponse == null) {
                    try {
                        httpresponse = httpClient.execute(httpDelete);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    } catch (ClientProtocolException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        status_code = 404;
                        actualString = "Connection error";
                    }
                }
                break;

            default:
                break;
        }
        if (httpresponse != null) {
            status_code = httpresponse.getStatusLine().getStatusCode();

            HttpEntity httpEntity = httpresponse.getEntity();
            InputStream stream = null;
            try {
                stream = httpEntity.getContent();


                if (httpresponse != null) {
                    Header[] headers = httpresponse.getAllHeaders();
                    if (headers != null) {
                        for (int i = 0; i < headers.length; i++) {
                            headerValue = headers[i].getName();
                            if (headerValue.equalsIgnoreCase("cardConfigVersion")) {
                                cardConfigValue = httpresponse.getFirstHeader("cardConfigVersion").getValue();
                            }
                        }
                    }
                }


                BufferedReader buffer = new BufferedReader(new InputStreamReader(
                        stream));

                String s = "";
                while ((s = buffer.readLine()) != null) {
                    response += s;
                }
                Log.d("result", response);
                actualString = response;

                if (response.startsWith("null")) {
                    actualString = response.substring(4);
                }


            } catch (IOException e) {
                status_code = 404;
                actualString = "Connection error";
            }
        }
        return actualString;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }

        if (status_code == 403) {
            String message = _context.getString(R.string.invalid_credential);
            if (result != null) {
                try {
                    JSONObject mainobj = new JSONObject(result);
                    if (mainobj != null) {
                        message = mainobj.optString("message");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            Toast.makeText(_context, message, Toast.LENGTH_SHORT).show();
            Util util = new Util();
            util.logoutfunc(_context);

        } else if (status_code == 503) {
            Util util = new Util();
            util.serverMaintenance(_context);
        } else if (status_code == 426) {
            Util util = new Util();
            util.appUpdate(_context);
        } else if (status_code == 200 || status_code == 201) {


            if (url.equalsIgnoreCase(CWUrls.CONFIG_URL)) {
                configList = new ArrayList<ConfigModel>();
                configModel = new ConfigModel();

                DbUtil db = new DbUtil();
                DbUtil.delete(_context, Constants.TABLE_CARDS, null, null);
                DbUtil.delete(_context, Constants.TAB_TABLE_DATA_TAB_Type, null, null);
                DbUtil.delete(_context, Constants.USER_PERMISSION_TABLE, null, null);
                DbUtil.delete(_context, Constants.MASTER_ROLE_TABLE_NAME, null, null);


                try {

                    JSONObject mainobj = new JSONObject(result);
                    if (mainobj.has("data")) {
                        JSONObject data = mainobj.optJSONObject("data");
                        if (data != null) {
                            configModel.cardConfigVersion = data.optString("cardConfigVersion");
                            CWIncturePreferences.init(_context);
                            CWIncturePreferences.setCardConfigVersion(configModel.cardConfigVersion);
                            if (data.has("tabs")) {
                                tabList = new ArrayList<ConfigModel>();
                                JSONArray tabs = data.optJSONArray("tabs");
                                if (tabs.length() > 0) {

                                    for (int t = 0; t < tabs.length(); t++) {
                                        ConfigModel model = new ConfigModel();
                                        JSONObject tobj = tabs.optJSONObject(t);
                                        model.id = tobj.optString("id");
                                        model.Default = tobj.optString("default");
                                        tabList.add(model);
                                        db.addTabDetails(model.id, model.Default);
                                        db.getTabDetails(model.id);
                                        tabList.add(model);
                                        db.addTabDetails(model.id, model.Default);
                                        db.getTabDetails(model.id);

                                    }

                                }
                            }
                            configModel.tabs = tabList;

                            if (data.has("navigation")) {
                                navigationList = new ArrayList<ConfigModel>();
                                JSONArray navigation = data.optJSONArray("navigation");
                                if (navigation.length() > 0) {
                                    for (int n = 0; n < navigation.length(); n++) {
                                        JSONObject nobj = navigation.optJSONObject(n);
                                        ConfigModel model = new ConfigModel();
                                        model.id = nobj.optString("id");
                                        model.tab_id = nobj.optString("tab_id");
                                        model.icon = nobj.optString("icon");
                                        model.displayName = nobj.optString("displayName");

                                        navigationOptions = new ArrayList<ConfigModel>();
                                        JSONArray options = nobj.optJSONArray("options");
                                        if (options.length() > 0) {
                                            for (int o = 0; o < options.length(); o++) {
                                                JSONObject oobj = options.optJSONObject(o);
                                                ConfigModel omodel = new ConfigModel();
                                                omodel.id = oobj.optString("id");
                                                omodel.icon = oobj.optString("icon");
                                                omodel.displayName = oobj.optString("displayName");
                                                navigationOptions.add(omodel);

                                            }
                                        }
                                        configModel.noptions = navigationOptions;
                                        navigationList.add(model);
                                        db.addNavigation(model.id, options.toString(), model.displayName);
                                        db.getNavDetails(model.id);
                                    }
                                }
                            }
                            configModel.navigation = navigationList;

                            if (data.has("filters")) {
                                filterList = new ArrayList<ConfigModel>();
                                JSONArray filters = data.optJSONArray("filters");
                                if (filters.length() > 0) {
                                    for (int f = 0; f < filters.length(); f++) {
                                        JSONObject fobj = filters.optJSONObject(f);
                                        ConfigModel model = new ConfigModel();
                                        model.id = fobj.optString("id");
                                        model.tab_id = fobj.optString("tab_id");
                                        model.icon = fobj.optString("icon");
                                        model.displayName = fobj.optString("displayName");

                                        filterOptions = new ArrayList<ConfigModel>();
                                        JSONArray options = fobj.optJSONArray("options");
                                        if (options != null) {
                                            if (options.length() > 0) {
                                                for (int o = 0; o < options.length(); o++) {
                                                    JSONObject oobj = options.optJSONObject(o);
                                                    ConfigModel omodel = new ConfigModel();
                                                    omodel.id = oobj.optString("id");
                                                    omodel.icon = oobj.optString("icon");
                                                    omodel.displayName = oobj.optString("displayName");
                                                    filterOptions.add(omodel);
                                                }
                                            }

                                            db.addFilters(model.id, options.toString(), model.displayName);

                                        }
                                        configModel.foptions = filterOptions;
                                        filterList.add(model);
                                        db.getFiltersDetails(model.id);

                                    }
                                }

                            }
                            configModel.filters = filterList;
                            if (data.has("user")) {
                                JSONObject user = data.optJSONObject("user");
                                if (user != null) {

                                    JSONObject user_permissions = user.optJSONObject("permissions");
                                    if (user_permissions != null) {

                                        Iterator<?> keys = user_permissions.keys();

                                        while (keys.hasNext()) {
                                            String key = (String) keys.next();
                                            JSONObject permObj = user_permissions.optJSONObject(key);
                                            ContentValues user_perm_values = new ContentValues();
                                            user_perm_values.put(Constants.ENTITY, key);
                                            user_perm_values.put(Constants.CREATE, permObj.optString("create"));
                                            user_perm_values.put(Constants.READ, permObj.optString("read"));
                                            user_perm_values.put(Constants.UPDATE, permObj.optString("update"));
                                            user_perm_values.put(Constants.DELETE, permObj.optString("delete"));

                                            DbUtil.insert(Constants.USER_PERMISSION_TABLE, user_perm_values);


                                        }

                                    }
                                }
                            }

                            if (data.has("cardConfig")) {
                                JSONObject cardConfig = data.optJSONObject("cardConfig");

                                if (cardConfig != null) {

                                    JSONArray types = cardConfig.optJSONArray("types");
                                    ArrayList<String> typeList = new ArrayList<>();
                                    if (types.length() > 0 && types != null) {
                                        for (int t = 0; t < types.length(); t++) {
                                            JSONObject typeObj = types.optJSONObject(t);
                                            typeList.add(typeObj.optString("name"));

                                            JSONArray roles = types.optJSONObject(t).optJSONArray("roles");
                                            ArrayList<ConfigModel> rolesList = new ArrayList();
                                            if (roles != null) {
                                                if (roles.length() > 0) {
                                                    for (int r = 0; r < roles.length(); r++) {
                                                        ConfigModel rModel = new ConfigModel();
                                                        rModel.roleType = typeObj.optString("name"); //Name of card type
                                                        rModel.roleName = roles.optJSONObject(r).optString("name"); //Name of role
                                                        rModel.roleDisplayName = roles.optJSONObject(r).optString("displayName");
                                                        rModel.roleText = roles.optJSONObject(r).optString("roleText");
                                                        rModel.roleHrFunction = roles.optJSONObject(r).optString("hrFunction");
                                                        rModel.roleRegion = roles.optJSONObject(r).optString("region");
                                                        rolesList.add(rModel);
                                                        db.addMasterRoles(rModel.roleType, rModel.roleName, rModel.roleDisplayName, rModel.roleText, rModel.roleHrFunction, rModel.roleRegion); //RoleType will be the card type
                                                    }
                                                }
                                            }

                                            db.addTypes(typeObj.optString("name"));
                                        }
                                    }
                                    configModel.types = typeList;


                                    if (cardConfig.has("subTypes")) {
                                        JSONArray subtypes = cardConfig.optJSONArray("subTypes");
                                        ArrayList<String> subtypeslist = new ArrayList<String>();
                                        if (subtypes.length() > 0) {
                                            for (int st = 0; st < subtypes.length(); st++) {
                                                subtypeslist.add(subtypes.getString(st));

                                            }
                                        }
                                        configModel.subtypes = subtypeslist;
                                    }

                                    cardsList = new ArrayList<>();
                                    JSONArray cards = cardConfig.optJSONArray("cards");
                                    if (cards.length() > 0) {
                                        for (int c = 0; c < cards.length(); c++) {
                                            JSONObject cobj = cards.optJSONObject(c);
                                            ConfigModel model = new ConfigModel();
                                            model.type = cobj.optString("type");
                                            model.subtype = cobj.optString("subtype");
                                            model.metaCard = cobj.optString("templateId");

                                            JSONObject fields = cobj.optJSONObject("fields");
                                            JSONArray actions = cobj.optJSONArray("actions");
                                            if (fields != null) {

                                                JSONArray mandatory = fields.optJSONArray("mandatory");
                                                ArrayList<String> mandatorylist = new ArrayList<String>();
                                                if (mandatory.length() > 0) {
                                                    for (int m = 0; m < mandatory.length(); m++) {
                                                        mandatorylist.add(mandatory.getString(m));
                                                    }
                                                }
                                                model.mandatory = mandatorylist;

                                                JSONArray optional = fields.optJSONArray("optional");
                                                ArrayList<String> optionallist = new ArrayList<String>();
                                                if (optional.length() > 0) {
                                                    for (int op = 0; op < optional.length(); op++) {
                                                        optionallist.add(optional.getString(op));
                                                    }
                                                }
                                                model.optional = optionallist;
                                            }

                                            if (actions != null) {
                                                ArrayList<String> actionlist = new ArrayList<String>();
                                                if (actions.length() > 0) {
                                                    for (int m = 0; m < actions.length(); m++) {
                                                        actionlist.add(actions.getString(m));
                                                    }
                                                }
                                                model.actions = actionlist;
                                            }

                                            db.insertCardDetails(model);
                                            cardsList.add(model);

                                        }
                                    }

                                }
                            }

                            /*----------Position-------------*/

                            if(data.has("officialInformation")){
                                JSONObject officialInformation = data.optJSONObject("officialInformation");
                                if (officialInformation!=null){
                                    LoginModel model = new LoginModel();
                                    model.setPosition(officialInformation.optLong("position"));

                                }

                            }

                            configModel.cards = cardsList;


                        }
                    }
                    if (mainobj.has("status")) {
                        configModel.status = mainobj.optString("status");
                    }
                    if (mainobj.has("message")) {
                        configModel.message = mainobj.optString("message");
                    }
                    configList.add(configModel);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                if (configModel.status.equals("success")) {
                    if (login) {

                        Log.d("rolee=", "" + CWIncturePreferences.getRole());
                        String posValue = "";
                        if (CWIncturePreferences.getRole().equalsIgnoreCase("vpsa") || CWIncturePreferences.getRole().equalsIgnoreCase("vphr") || CWIncturePreferences.getRole().equalsIgnoreCase("SDM1") || CWIncturePreferences.getRole().equalsIgnoreCase("SDM2")) {
                            posValue = "no";
                            Intent intent = new Intent(_context, MainActivity.class);
                            intent.putExtra("pos", posValue);
                            _context.startActivity(intent);
                            ((Activity) _context).finish();
                        } else {
                            posValue = "yes";
                            Intent intent = new Intent(_context, MainActivity.class);
                            intent.putExtra("pos", posValue);
                            _context.startActivity(intent);
                            ((Activity) _context).finish();
                        }

                    }
                    if (listener != null) {
                        listener.configurationUpdated(true);
                        LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(_context);
                        Intent intent = new Intent(Constants.CONFIG_UPDATED);
                        //sending intent through BroadcastManager
                        broadcastManager.sendBroadcast(intent);
                    }
                } else if (configModel.getMessage() != null && !configModel.getMessage().isEmpty()) {
                    Toast.makeText(_context, configModel.getMessage(), Toast.LENGTH_SHORT).show();
                }


            } else {
                CWIncturePreferences.init(_context);
                if (CWIncturePreferences.getCardConfigVersion() != null && (!CWIncturePreferences.getCardConfigVersion().equalsIgnoreCase("null"))) {
                    if (!cardConfigValue.equalsIgnoreCase(CWIncturePreferences.getCardConfigVersion())) {
                        Log.d("Cherrywork", "Configuration Changed");
                        cardConfig();
                    }
                }
                if (listener != null)
                    listener.processFinish(result, status_code, url, type);
            }

        } else if (result != null && !result.isEmpty()) {

            String message = _context.getString(R.string.invalid_credential);
            if (result != null) {
                try {
                    JSONObject mainobj = new JSONObject(result);
                    if (mainobj != null) {
                        message = mainobj.optString("message");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            Toast.makeText(_context, result, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Calls service to get confgurations.
     */
    public void cardConfig() {
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("x-email-id", CWIncturePreferences.getEmail());
        headers.put("x-access-token", CWIncturePreferences.getAccessToken());
        NetworkConnector connect = null;
        connect = new NetworkConnector(_context, NetworkConnector.TYPE_GET, CWUrls.CONFIG_URL, headers, null, listener, true);

        connect.setLogin(false);
        if (connect.isAllowed()) {
            connect.execute();
        } else {
            Toast.makeText(_context, _context.getString(R.string.action_not_allowed), Toast.LENGTH_SHORT).show();
        }
    }

    public void setLogin(boolean b) {
        login = b;
    }

    public boolean isAllowed() {
        return isAllowed;
    }
}
