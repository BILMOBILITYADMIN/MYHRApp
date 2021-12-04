package DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import Model.ConfigModel;
import Model.LoginModel;
import Model.OfflineAction;
import Model.RolesModel;
import Model.UserPermission;
import Utility.CWIncturePreferences;
import Utility.Constants;
import Utility.Util;

/**
 * Created by harshu on 12/21/2015.
 */
public class DbUtil implements Constants {

    private static final String TASKS_TABLE_DATA = "Data";
    public static final String TASKS_TABLE_NAME_SCROLL = "scroll";
    public static final String TASKS_TABLE_DATA_DETAIL_ID = "detail_id";

    public static final String TASKS_TABLE_NAME_DETAIL = "detail";
    public static final String TASKS_TABLE_NAME = "tasks";

    public static final String NOTIFICATIONS_TABLE_NAME = "notifications";
    private static final String NOTIFICATIONS_TABLE_DATA = "Data";

    public static final String TAB_TABLE_NAME_TABS = "tabs";
    private static final String TAB_TABLE_DATA_TAB_ID = "tab_id";
    private static final String TAB_TABLE_DATA_TAB_DEFAULTVALUE = "default_value";

    public static final String TAB_TABLE_NAME_TYPES = "type";
    private static final String TAB_TABLE_DATA_TAB_Type = "_type";

    public static final String NAVIGATION_TABLE_NAME = "navigation";
    private static final String NAVIGATION_TABLE_DATA_ID = "nav_id";
    private static final String NAVIGATION_TABLE_DATA_OPTIONS = "nav_options";


    public static final String FILTERS_TABLE_NAME = "filters";
    private static final String FILTERS_TABLE_DATA_ID = "filter_id";
    private static final String FILTERS_TABLE_DATA_OPTIONS = "filter_options";

    public static final String ROLES_TABLE_NAME = "roles";
    private static final String ROLES_TABLE_DATA_SLNO = "roles_slno";
    private static final String ROLES_TABLE_DATA_ROLENAME = "roles_name";
    private static final String ROLES_TABLE_DATA_ROLEREGION = "roles_region";


    public void addMember(String response) {
        ContentValues values = new ContentValues();
        values.put(TASKS_TABLE_DATA, response);
        DbAdapter.getDbAdapterInstance().insert(TASKS_TABLE_NAME, null, values);

    }

    public void addTabDetails(String id, String value) {

        ContentValues values = new ContentValues();
        values.put(TAB_TABLE_DATA_TAB_ID, id);
        values.put(TAB_TABLE_DATA_TAB_DEFAULTVALUE, value);
        DbAdapter.getDbAdapterInstance().insert(TAB_TABLE_NAME_TABS, null, values);

    }

    public void addTypes(String type) {

        ContentValues values = new ContentValues();
        values.put(TAB_TABLE_NAME_TYPES, type);
        DbAdapter.getDbAdapterInstance().insert(TAB_TABLE_DATA_TAB_Type, null, values);

    }

    public void addNavigation(String id, String options, String Display) {
        ContentValues values = new ContentValues();
        values.put(NAVIGATION_TABLE_DATA_ID, id);
        values.put(NAVIGATION_TABLE_DATA_OPTIONS, options);
        values.put(NAVIGATION_TABLE_DATA_DISPLAYNAME, Display);
        DbAdapter.getDbAdapterInstance().insert(NAVIGATION_TABLE_NAME, null, values);


    }

    public void addFilters(String id, String options, String Display) {
        ContentValues values = new ContentValues();
        values.put(FILTERS_TABLE_DATA_ID, id);
        values.put(FILTERS_TABLE_DATA_OPTIONS, options);
        values.put(FILTERS_TABLE_DATA_DISPLAYNAME, Display);
        DbAdapter.getDbAdapterInstance().insert(FILTERS_TABLE_NAME, null, values);


    }

    public void addRoles(int slno, String roleName, String roleRegion) {
        ContentValues values = new ContentValues();
        values.put(ROLES_TABLE_DATA_SLNO, slno);
        values.put(ROLES_TABLE_DATA_ROLENAME, roleName);
        values.put(ROLES_TABLE_DATA_ROLEREGION, roleRegion);
        DbAdapter.getDbAdapterInstance().insert(ROLES_TABLE_NAME, null, values);

    }

    public void addMasterRoles(String roleType, String roleName, String roleDisplayName, String roleText, String roleHrFunction, String roleRegion) {
        ContentValues values = new ContentValues();
        values.put(ROLE_TYPE, roleType);
        values.put(ROLE_NAME, roleName);
        values.put(ROLE_DISPLAY_NAME, roleDisplayName);
        values.put(ROLE_TEXT, roleText);
        values.put(ROLE_HR_FUNCTION, roleHrFunction);
        values.put(ROLE_REGION, roleRegion);
        DbAdapter.getDbAdapterInstance().insert(MASTER_ROLE_TABLE_NAME, null, values);

    }

    public static void checkAndOpenDataBase(Context context) {
        if (!DbAdapter.getDbAdapterInstance().isDataBaseOpen()) {
            DbAdapter.getDbAdapterInstance().open(context);
        }
    }

    public static long insert(String tableName, ContentValues contentValues) {

        return DbAdapter.getDbAdapterInstance().insert(tableName,
                contentValues);
    }

    public static int delete(Context context, String tableName, String whereClause,
                             String[] whereArgs) {
        checkAndOpenDataBase(context);
        return DbAdapter.getDbAdapterInstance().delete(tableName, whereClause,
                whereArgs);
    }


    public String getTabDetails(String id) {
        String selectQuery = "SELECT " + TAB_TABLE_DATA_TAB_DEFAULTVALUE + " FROM " + TAB_TABLE_NAME_TABS + " WHERE "
                + TAB_TABLE_DATA_TAB_ID + " = '" + id + "'";
        Cursor cursor = DbAdapter.getDbAdapterInstance().rawQuery(selectQuery, null);
        String response = null;
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            response = cursor.getString(0);
            cursor.close();

        }
        return response;
    }

    public String getNavDetails(String id) {
        String selectQuery = "SELECT " + NAVIGATION_TABLE_DATA_OPTIONS + " FROM " + NAVIGATION_TABLE_NAME + " WHERE "
                + NAVIGATION_TABLE_DATA_ID + " = '" + id + "'";
        Cursor cursor = DbAdapter.getDbAdapterInstance().rawQuery(selectQuery, null);
        String response = null;
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            response = cursor.getString(0);
            cursor.close();

        }
        return response;

    }


    public String getFiltersDetails(String id) {
        String selectQuery = "SELECT " + FILTERS_TABLE_DATA_OPTIONS + " FROM " + FILTERS_TABLE_NAME + " WHERE "
                + FILTERS_TABLE_DATA_ID + " = '" + id + "'";
        Cursor cursor = DbAdapter.getDbAdapterInstance().rawQuery(selectQuery, null);
        String response = null;
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            response = cursor.getString(0);
            cursor.close();

        }
        return response;

    }

    public ArrayList<LoginModel> getRolesList(Context c) {

        Cursor cursor = DbAdapter.getDbAdapterInstance().getResultSet(ROLES_TABLE_NAME, new String[]{ROLES_TABLE_DATA_SLNO, ROLES_TABLE_DATA_ROLENAME, ROLES_TABLE_DATA_ROLEREGION}, null, c);
        ArrayList<LoginModel> roles_list = new ArrayList<>();
        if (null != cursor) {

            if (cursor.moveToFirst()) {
                do {
                    LoginModel login = new LoginModel();

                    login.role_name = (cursor.getString(cursor
                            .getColumnIndexOrThrow(ROLES_TABLE_DATA_ROLENAME)));
                    login.role_region = (cursor.getString(cursor
                            .getColumnIndexOrThrow(ROLES_TABLE_DATA_ROLEREGION)));

                    roles_list.add(login);

                } while (cursor.moveToNext());

            }

            cursor.close();
        }
        return roles_list;
    }

    public ArrayList<RolesModel> getMasterRolesList(String roleType) {
        String SELECT_QUERY = "SELECT " + ROLE_NAME + ", " + ROLE_DISPLAY_NAME + ", " + ROLE_TEXT + ", " + ROLE_HR_FUNCTION + ", " + ROLE_REGION + " FROM " + MASTER_ROLE_TABLE_NAME + " WHERE " + ROLE_TYPE + " = '" + roleType + "'";
        ArrayList<RolesModel> rolesList = new ArrayList<>();
        Cursor cursor = DbAdapter.getDbAdapterInstance().rawQuery(SELECT_QUERY, null);
        if (cursor.moveToFirst()) {
            do {
                RolesModel rolesModel = new RolesModel();
                rolesModel.setRole_name(cursor.getString(0));
                rolesModel.setRole_display_name(cursor.getString(1));
                rolesModel.setRole_text(cursor.getString(2));
                rolesModel.setRole_hr_function(cursor.getString(3));
                String region = cursor.getString(4);
                if (region != null && region.length() > 0 && !region.equals("null"))
                    rolesModel.setRole_region(region);
                else
                    rolesModel.setRole_region(CWIncturePreferences.getRegion());
                rolesList.add(rolesModel);
            } while (cursor.moveToNext());
        }
        return rolesList;
    }


    public void addInAppNotifications(String response) {
        ContentValues values = new ContentValues();
        values.put(NOTIFICATIONS_TABLE_DATA, response);
        DbAdapter.getDbAdapterInstance().insert(NOTIFICATIONS_TABLE_NAME, null, values);
    }


    public String getResponseStructure() {
        String selectQuery = "SELECT  * FROM " + "tasks";
        Cursor cursor = DbAdapter.getDbAdapterInstance().rawQuery(selectQuery, null);
        if (cursor.moveToFirst())
            return cursor.getString(0);
        else
            return "";
    }


    public String getResponseOnDetail(String id) {
        String selectQuery = "SELECT  * FROM " + TASKS_TABLE_NAME_DETAIL + " WHERE "
                + TASKS_TABLE_DATA_DETAIL_ID + " = '" + id + "'";
        Cursor cursor = DbAdapter.getDbAdapterInstance().rawQuery(selectQuery, null);
        String response = null;
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            response = cursor.getString(0);
            cursor.close();
        }
        return response;
    }

    public String getInAppNotifications() {
        String selectQuery = "SELECT  * FROM " + NOTIFICATIONS_TABLE_NAME;
        Cursor cursor = DbAdapter.getDbAdapterInstance().rawQuery(selectQuery, null);
        cursor.moveToFirst();
        return cursor.getString(0);
    }


    public void insertCardDetails(ConfigModel card) {
        ContentValues values = new ContentValues();
        values.put(KEY_CARD_TYPE, card.type);
        values.put(KEY_CARD_SUBTYPE, card.subtype);
        values.put(KEY_CARD_ID, card.metaCard);
        values.put(KEY_CARD_FIELDS_MANDATE, Util.convertArrayToString(card.mandatory));
        values.put(KEY_CARD_FIELDS_OPTION, Util.convertArrayToString(card.optional));
        values.put(KEY_CARD_ACTIONS, Util.convertArrayToString(card.actions));
        DbAdapter.getDbAdapterInstance().insert(TABLE_CARDS, null, values);
    }

    public UserPermission getPermissionForEntity(String entity, Context c) {
        UserPermission permission = new UserPermission();
        Cursor cursor = DbAdapter.getDbAdapterInstance().getResultSet(USER_PERMISSION_TABLE, new String[]{CREATE, READ, UPDATE, DELETE}, null, ENTITY + " = '" + entity + "' ", c);

        if (null != cursor) {


            if (cursor.moveToFirst()) {
                permission.setCreate(cursor.getString(cursor
                        .getColumnIndexOrThrow(CREATE)));
                permission.setRead(cursor.getString(cursor
                        .getColumnIndexOrThrow(READ)));
                permission.setUpdate(cursor.getString(cursor
                        .getColumnIndexOrThrow(UPDATE)));
                permission.setDelete(cursor.getString(cursor
                        .getColumnIndexOrThrow(DELETE)));


            }

            cursor.close();
        }
        return permission;
    }

    public ConfigModel getConfiguredCard(String type, String subType, Context c) {
        ConfigModel card = new ConfigModel();
        Cursor cursor = DbAdapter.getDbAdapterInstance().getResultSet(TABLE_CARDS, new String[]{KEY_CARD_ID, KEY_CARD_FIELDS_MANDATE, KEY_CARD_FIELDS_OPTION, KEY_CARD_ACTIONS}, null, KEY_CARD_TYPE + " = '" + type + "' and " + KEY_CARD_SUBTYPE + " = '" + subType + "'", c);

        if (null != cursor) {


            if (cursor.moveToFirst()) {
                card = new ConfigModel();
                card.type = type;
                card.subtype = subType;
                card.id = cursor.getString(cursor
                        .getColumnIndexOrThrow(KEY_CARD_ID));
                card.mandatory = Util.convertStringToArray(cursor.getString(cursor
                        .getColumnIndexOrThrow(KEY_CARD_FIELDS_MANDATE)));
                card.optional = Util.convertStringToArray(cursor.getString(cursor
                        .getColumnIndexOrThrow(KEY_CARD_FIELDS_OPTION)));
                card.actions = Util.convertStringToArray(cursor.getString(cursor
                        .getColumnIndexOrThrow(KEY_CARD_ACTIONS)));


            }

            cursor.close();
        }
        return card;
    }


    public ArrayList<ConfigModel> getTabs(Context c) {
        Cursor cursor = DbAdapter.getDbAdapterInstance().getResultSet(TAB_TABLE_NAME_TABS, new String[]{TAB_TABLE_DATA_TAB_ID, TAB_TABLE_DATA_TAB_DEFAULTVALUE}, null, c);
        ArrayList<ConfigModel> tabs = new ArrayList<>();
        if (null != cursor) {

            if (cursor.moveToFirst()) {
                do {
                    ConfigModel tab = new ConfigModel();

                    tab.id = (cursor.getString(cursor
                            .getColumnIndexOrThrow(TAB_TABLE_DATA_TAB_ID)));
                    tab.Default = (cursor.getString(cursor
                            .getColumnIndexOrThrow(TAB_TABLE_DATA_TAB_DEFAULTVALUE)));
                    tabs.add(tab);

                } while (cursor.moveToNext());

            }

            cursor.close();
        }
        return tabs;
    }

    public ArrayList<ConfigModel> getTypes(Context c) {
        Cursor cursor = DbAdapter.getDbAdapterInstance().getResultSet(TAB_TABLE_DATA_TAB_Type, new String[]{TAB_TABLE_NAME_TYPES}, null, c);
        ArrayList<ConfigModel> types = new ArrayList<>();
        if (null != cursor) {

            if (cursor.moveToFirst()) {
                do {
                    ConfigModel type = new ConfigModel();

                    type.id = (cursor.getString(cursor.getColumnIndexOrThrow(TAB_TABLE_NAME_TYPES)));

                    types.add(type);

                } while (cursor.moveToNext());

            }

            cursor.close();
        }
        return types;
    }

    public ArrayList<ConfigModel> getNavigation(Context c) {
        Cursor cursor = DbAdapter.getDbAdapterInstance().getResultSet(NAVIGATION_TABLE_NAME, new String[]{NAVIGATION_TABLE_DATA_ID, NAVIGATION_TABLE_DATA_OPTIONS, NAVIGATION_TABLE_DATA_DISPLAYNAME}, null, c);

        ArrayList<ConfigModel> navigation_list = new ArrayList<>();
        if (null != cursor) {

            if (cursor.moveToFirst()) {
                do {
                    ConfigModel navigation = new ConfigModel();

                    navigation.id = (cursor.getString(cursor
                            .getColumnIndexOrThrow(NAVIGATION_TABLE_DATA_ID)));
                    navigation.options = (cursor.getString(cursor
                            .getColumnIndexOrThrow(NAVIGATION_TABLE_DATA_OPTIONS)));
                    navigation.displayName = (cursor.getString(cursor
                            .getColumnIndexOrThrow(NAVIGATION_TABLE_DATA_DISPLAYNAME)));
                    navigation_list.add(navigation);

                } while (cursor.moveToNext());

            }

            cursor.close();
        }
        return navigation_list;
    }

    public static boolean actionTakenForWorkitem(Context _context, String id) {
        Cursor cursor = DbAdapter.getDbAdapterInstance().getResultSet(PENDING_OFFLINE_ACTION, new String[]{WORKITEM_ID}, WORKITEM_ID + " = '" + id + "'", null, null, _context);
        if (null != cursor) {

            if (cursor.moveToFirst())
                return true;
            else
                return false;
        }
        return false;
    }

    public ArrayList<ConfigModel> getFilters(Context c) {

        Cursor cursor = DbAdapter.getDbAdapterInstance().getResultSet(FILTERS_TABLE_NAME, new String[]{FILTERS_TABLE_DATA_ID, FILTERS_TABLE_DATA_OPTIONS, FILTERS_TABLE_DATA_DISPLAYNAME}, null, c);
        ArrayList<ConfigModel> filterlist = new ArrayList<>();
        if (null != cursor) {

            if (cursor.moveToFirst()) {
                do {
                    ConfigModel filters = new ConfigModel();

                    filters.id = (cursor.getString(cursor
                            .getColumnIndexOrThrow(FILTERS_TABLE_DATA_ID)));
                    filters.options = (cursor.getString(cursor
                            .getColumnIndexOrThrow(FILTERS_TABLE_DATA_OPTIONS)));
                    filters.displayName = (cursor.getString(cursor
                            .getColumnIndexOrThrow(FILTERS_TABLE_DATA_DISPLAYNAME)));
                    filterlist.add(filters);

                } while (cursor.moveToNext());

            }

            cursor.close();
        }
        return filterlist;
    }

    public static String getFilterId(Context c, String filterName) {

        Cursor cursor = DbAdapter.getDbAdapterInstance().getResultSet(FILTERS_TABLE_NAME, new String[]{FILTERS_TABLE_DATA_OPTIONS}, null, null, c);
        String id = "";
        boolean idSet = false;
        if (null != cursor) {

            if (cursor.moveToFirst()) {
                do {
                    String options = (cursor.getString(cursor
                            .getColumnIndexOrThrow(FILTERS_TABLE_DATA_OPTIONS)));
                    try {
                        JSONArray json = new JSONArray(options);
                        if (json.length() > 0) {
                            for (int o = 0; o < json.length(); o++) {
                                JSONObject oobj = json.optJSONObject(o);
                                if (oobj.optString("displayName").equalsIgnoreCase(filterName)) {
                                    id = oobj.optString("id");
                                    idSet = true;
                                    break;
                                }


                            }

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                } while (cursor.moveToNext() && !idSet);

            }

            cursor.close();
        }
        return id;
    }


    public static ArrayList<OfflineAction> getPendingActions(Context c) {

        Cursor cursor = DbAdapter.getDbAdapterInstance().getResultSet(PENDING_OFFLINE_ACTION, new String[]{ACTION_TYPE, WORKITEM_ID, TEXT}, null, c);
        ArrayList<OfflineAction> actions = new ArrayList<>();
        if (null != cursor) {

            if (cursor.moveToFirst()) {
                do {
                    OfflineAction action = new OfflineAction();

                    action.setAction_type((cursor.getString(cursor
                            .getColumnIndexOrThrow(ACTION_TYPE))));

                    action.setWorkitem_id(cursor.getString(cursor
                            .getColumnIndexOrThrow(WORKITEM_ID)));
                    action.setText(cursor.getString(cursor
                            .getColumnIndexOrThrow(TEXT)));
                    actions.add(action);

                } while (cursor.moveToNext());

            }

            cursor.close();
        }
        return actions;
    }


}
