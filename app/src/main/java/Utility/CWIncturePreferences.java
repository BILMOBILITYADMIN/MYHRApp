package Utility;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Arun on 11-09-2015.
 */
public class CWIncturePreferences {
    private static final String PREFERENCE_NAME = "CW_INCTURE_PREFS";
    public static final String EMPTY_STRING_DEFAULT_VALUE = "null";
    private static final String USER_ID = "USER_ID";
    private static final String ACCESS_TOKEN = "ACCESS_TOKEN";
    private static final String DEVICE_ID = "DEVICE_ID";
    private static final String USERS_ID = "USERS_ID";
    private static final String FIRSTNAME = "FIRSTNAME";
    private static final String LASTNAME = "LASTNAME";
    private static final String AVATARURL = "AVATARURL";
    private static final String EMAIL = "EMAIL";
    private static final String PIN = "PIN";
    private static final String PERSPECTIVE = "PERSPECTIVE";

    private static final String DESIGNATION = "DESIGNATION";
    private static final String ROLE = "ROLE";
    private static final String ROLE_REGION = "ROLE_REGION";

    private static final String CardConfigVersion = "version";

    private static final String ConfigUrl = "";
    private static final String LOGO = "logo";
    private static final String RIGHT_FILTER_TAB0 = "tab0_right";
    private static final String LEFT_FILTER_TAB0 = "tab0_left";

    private static final String RIGHT_FILTER_TAB1 = "tab1_right";
    private static final String LEFT_FILTER_TAB1 = "tab1_left";

    private static final String RIGHT_FILTER_TAB2 = "tab2_right";
    private static final String LEFT_FILTER_TAB2 = "tab2_left";

    //
    private static final String REGION = "region";
    private static final String HR_FUNCTION = "hr_function";

    private static final String MATRIXMANAGER = "ISMATRIXMANAGER";

    private static final String LOGIN_DATA = "logindata";

    private static final String POSITION = "position";

    public static Long getPOSITION() {
        return _sharedPreferences.getLong(POSITION,0);

    }

    public static void setPosition(Long position) {
        SharedPreferences.Editor _editor = _sharedPreferences.edit();
        _editor.putLong(POSITION, position);
        _editor.commit();
        _editor = null;
    }

    private static SharedPreferences _sharedPreferences;

    public static void init(Context context) {
        if (_sharedPreferences == null) {
            /** Get the shared preferences object for CWIncture. */
            _sharedPreferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);

        }
    }


    public static String getLOGO() {
        return _sharedPreferences.getString(LOGO, EMPTY_STRING_DEFAULT_VALUE);
    }

    public static void setRegion(String region) {
        SharedPreferences.Editor _editor = _sharedPreferences.edit();
        _editor.putString(REGION, region);
        _editor.commit();
        _editor = null;
    }
    public static void setLoginModelData(String loginModelData) {
        SharedPreferences.Editor _editor = _sharedPreferences.edit();
        _editor.putString(LOGIN_DATA, loginModelData);
        _editor.commit();
        _editor = null;
    }

    public static String getLoginModelData() {
        return _sharedPreferences.getString(LOGIN_DATA, EMPTY_STRING_DEFAULT_VALUE);
    }

    public static void setHrFunction(String hr_function) {
        SharedPreferences.Editor _editor = _sharedPreferences.edit();
        _editor.putString(HR_FUNCTION, hr_function);
        _editor.commit();
        _editor = null;
    }

    public static String getRegion() {
        return _sharedPreferences.getString(REGION, EMPTY_STRING_DEFAULT_VALUE);
    }

    public static String getHrFunction() {
        return _sharedPreferences.getString(HR_FUNCTION, EMPTY_STRING_DEFAULT_VALUE);
    }

    public static void setLogo(String logo) {
        SharedPreferences.Editor _editor = _sharedPreferences.edit();
        _editor.putString(LOGO, logo);
        _editor.commit();
        _editor = null;
    }

    public static void setDeviceToken(String deviceToken) {

        SharedPreferences.Editor _editor = _sharedPreferences.edit();
        _editor.putString(DEVICE_ID, deviceToken);
        _editor.commit();
        _editor = null;
    }

    public static void setCardConfigVersion(String version) {

        SharedPreferences.Editor _editor = _sharedPreferences.edit();
        _editor.putString(CardConfigVersion, version);
        _editor.commit();
        _editor = null;
    }

    public static void setAccessToken(String accessToken) {
        SharedPreferences.Editor _editor = _sharedPreferences.edit();
        _editor.putString(ACCESS_TOKEN, accessToken);
        _editor.commit();
        _editor = null;
    }

    public static void setUserId(String userid) {
        SharedPreferences.Editor _editor = _sharedPreferences.edit();
        _editor.putString(USER_ID, userid);
        _editor.commit();
        _editor = null;
    }

    public static void setUsersId(String usersid) {
        SharedPreferences.Editor _editor = _sharedPreferences.edit();
        _editor.putString(USERS_ID, usersid);
        _editor.commit();
        _editor = null;
    }

    public static void setFirstname(String firstname) {
        SharedPreferences.Editor _editor = _sharedPreferences.edit();
        _editor.putString(FIRSTNAME, firstname);
        _editor.commit();
        _editor = null;
    }

    public static void setLastname(String lastname) {
        SharedPreferences.Editor _editor = _sharedPreferences.edit();
        _editor.putString(LASTNAME, lastname);
        _editor.commit();
        _editor = null;
    }

    public static void setAvatarurl(String avatarurl) {
        SharedPreferences.Editor _editor = _sharedPreferences.edit();
        _editor.putString(AVATARURL, avatarurl);
        _editor.commit();
        _editor = null;
    }

    public static void setEmail(String email) {
        SharedPreferences.Editor _editor = _sharedPreferences.edit();
        _editor.putString(EMAIL, email);
        _editor.commit();
        _editor = null;
    }


    public static void setPerspective(String perspective) {
        SharedPreferences.Editor _editor = _sharedPreferences.edit();
        _editor.putString(PERSPECTIVE, perspective);
        _editor.commit();
        _editor = null;
    }


    public static String getDeviceToken() {
        return _sharedPreferences.getString(DEVICE_ID, EMPTY_STRING_DEFAULT_VALUE);
    }

    public static String getAccessToken() {
        return _sharedPreferences.getString(ACCESS_TOKEN, EMPTY_STRING_DEFAULT_VALUE);
    }

    public static String getUserId() {
        return _sharedPreferences.getString(USER_ID, EMPTY_STRING_DEFAULT_VALUE);
    }

    public static String getUsersId() {
        return _sharedPreferences.getString(USERS_ID, EMPTY_STRING_DEFAULT_VALUE);
    }

    public static String getCardConfigVersion() {
        return _sharedPreferences.getString(CardConfigVersion, EMPTY_STRING_DEFAULT_VALUE);
    }

    public static String getFirstname() {
        return _sharedPreferences.getString(FIRSTNAME, EMPTY_STRING_DEFAULT_VALUE);
    }

    public static String getLastname() {
        return _sharedPreferences.getString(LASTNAME, EMPTY_STRING_DEFAULT_VALUE);
    }

    public static String getAvatarurl() {
        return _sharedPreferences.getString(AVATARURL, EMPTY_STRING_DEFAULT_VALUE);
    }

    public static String getEmail() {

        return _sharedPreferences.getString(EMAIL, EMPTY_STRING_DEFAULT_VALUE);
    }


    public static String getDesignation() {
        return _sharedPreferences.getString(DESIGNATION, EMPTY_STRING_DEFAULT_VALUE);
    }

    public static String getRole() {
        return _sharedPreferences.getString(ROLE, EMPTY_STRING_DEFAULT_VALUE);
    }

    public static String getRoleRegion() {
        return _sharedPreferences.getString(ROLE_REGION, EMPTY_STRING_DEFAULT_VALUE);
    }

    public static String getPerspective() {
        return _sharedPreferences.getString(PERSPECTIVE, EMPTY_STRING_DEFAULT_VALUE);
    }


    public static void resetPreferences() {
        SharedPreferences.Editor _editor = _sharedPreferences.edit();
        _editor.clear();
        _editor.commit();

    }


    public static void setDesignation(String designation) {
        SharedPreferences.Editor _editor = _sharedPreferences.edit();
        _editor.putString(DESIGNATION, designation);
        _editor.commit();
        _editor = null;
    }

    public static void setRole(String role) {
        SharedPreferences.Editor _editor = _sharedPreferences.edit();
        _editor.putString(ROLE, role);
        _editor.commit();
        _editor = null;
    }

    public static void setRoleRegion(String roleRegion) {
        SharedPreferences.Editor _editor = _sharedPreferences.edit();
        _editor.putString(ROLE_REGION, roleRegion);
        _editor.commit();
        _editor = null;
    }


    public static void setConfigUrl(String configUrl) {
        SharedPreferences.Editor _editor = _sharedPreferences.edit();
        _editor.putString(ConfigUrl, configUrl);
        _editor.commit();
        _editor = null;
    }

    public static String getConfigUrl() {
        return _sharedPreferences.getString(ConfigUrl, EMPTY_STRING_DEFAULT_VALUE);
    }

    public static void setPin(String pin) {
        SharedPreferences.Editor _editor = _sharedPreferences.edit();
        _editor.putString(PIN, pin);
        _editor.commit();
    }

    public static String getPin() {
        return _sharedPreferences.getString(PIN, EMPTY_STRING_DEFAULT_VALUE);
    }

    public static String getRightFilterTab0() {
        return _sharedPreferences.getString(RIGHT_FILTER_TAB0, "");
    }

    public static String getLeftFilterTab0() {
        return _sharedPreferences.getString(LEFT_FILTER_TAB0, "");

    }

    public static String getRightFilterTab1() {
        return _sharedPreferences.getString(RIGHT_FILTER_TAB1, "");
    }

    public static String getRightFilterTab2() {
        return _sharedPreferences.getString(RIGHT_FILTER_TAB2, "");

    }

    public static String getLeftFilterTab1() {
        return _sharedPreferences.getString(LEFT_FILTER_TAB1, "");

    }

    public static String getLeftFilterTab2() {
        return _sharedPreferences.getString(LEFT_FILTER_TAB2, "");

    }

    public static void RightFilterTab0(String filterName) {
        SharedPreferences.Editor _editor = _sharedPreferences.edit();
        _editor.putString(RIGHT_FILTER_TAB0, filterName);
        _editor.commit();
    }

    public static void LeftFilterTab0(String filterName) {
        SharedPreferences.Editor _editor = _sharedPreferences.edit();
        _editor.putString(LEFT_FILTER_TAB0, filterName);
        _editor.commit();
    }

    public static void RightFilterTab1(String filterName) {
        SharedPreferences.Editor _editor = _sharedPreferences.edit();
        _editor.putString(RIGHT_FILTER_TAB1, filterName);
        _editor.commit();
    }

    public static void RightFilterTab2(String filterName) {
        SharedPreferences.Editor _editor = _sharedPreferences.edit();
        _editor.putString(RIGHT_FILTER_TAB2, filterName);
        _editor.commit();
    }

    public static void LeftFilterTab1(String filterName) {
        SharedPreferences.Editor _editor = _sharedPreferences.edit();
        _editor.putString(LEFT_FILTER_TAB1, filterName);
        _editor.commit();
    }

    public static void LeftFilterTab2(String filterName) {
        SharedPreferences.Editor _editor = _sharedPreferences.edit();
        _editor.putString(LEFT_FILTER_TAB2, filterName);
        _editor.commit();
    }

    public static boolean isMatrixManager() {
        return _sharedPreferences.getBoolean(MATRIXMANAGER, false);
    }

    public static void setIsMatrixManager(boolean isMatrixManager) {
        SharedPreferences.Editor _editor = _sharedPreferences.edit();
        _editor.putBoolean(MATRIXMANAGER, isMatrixManager);
        _editor.commit();
        _editor = null;
    }
}
