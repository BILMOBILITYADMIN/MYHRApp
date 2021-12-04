package Utility;

/**
 * Created by Arun on 14-09-2015.
 */
public class CWUrls {

    public static final String DOMAIN_BRIT_QA = "hrapps.britindia.com";
    public static final String DOMAIN_BRIT_PRODUCTION = "myhr.britindia.com/api/";

    public static final String DOMAIN = DOMAIN_BRIT_QA;

    public static final String BASE_URL = "http://" + DOMAIN + ":4003/";


    //dev
//     public static final String BASE_URL_CSC = "http://dev.cherrywork.in:4013/";
//     public static final String DOMAIN_WEB_BRIT = "http://dev.cherrywork.in:7023";
//    public static final String COOKIE_DOMAIN_BRIT = "dev.cherrywork.in";



    //QA
    public static final String BASE_URL_CSC = "http://" + DOMAIN + "/platform/";
    public static final String DOMAIN_WEB_BRIT = "http://hrapps.britindia.com";
    public static final String COOKIE_DOMAIN_BRIT = "hrapps.britindia.com";
    public static final String IMAGE_FETCH_URL = "http://hrapps.britindia.com/platform";


    //for production
//    public static final String BASE_URL_CSC = "http://" + DOMAIN ;
//    public static final String DOMAIN_WEB_BRIT = "http://myhr.britindia.com";
//    public static final String COOKIE_DOMAIN_BRIT = "myhr.britindia.com";
//    public static final String IMAGE_FETCH_URL = "http://myhr.britindia.com/api";


    public static final String LOGIN_URL = BASE_URL_CSC + "login";
    public static final String CONFIG_URL = BASE_URL_CSC + "api/v1/configuration";
    public static final String GET_WORKITEM = BASE_URL_CSC + "api/v1/workitems";
    public static final String GET_WORKITEM_DETAIL = BASE_URL_CSC + "api/v1/workitem/";
    public static final String GET_WORKITEM_WITH_PAGINATION = BASE_URL_CSC + "api/v1/workitems?limit=";
    public static final String GET_USER_PROFILE = BASE_URL_CSC + "api/v1/user/";
    public static final String UPDATE_PROFILE = BASE_URL_CSC + "api/v1/user/";
    public static final String LOG_OUT = BASE_URL_CSC + "api/v1/logout";
    public static final String NOTIFICATIONS = BASE_URL_CSC + "api/v1/notifications";
    public static final String NOTIFICATIONS_COUNT = BASE_URL_CSC + "api/v1/notifications/count";
    public static final String GROUPS_SEARCH = BASE_URL_CSC + "api/v1/search/user?text=";
    public static final String PRE_CONFIG_URL = BASE_URL_CSC + "configuration?email=";
    public static final String NOTIFICATION_STATUS_UPDATE = BASE_URL_CSC + "api/v1/notifications/read/";
    public static final String ADD_COMMENT = BASE_URL_CSC + "api/v1/comment/workitem/";


    public static final String GET_EBAT_TEAM_ASSESSMENT = BASE_URL_CSC + "api/v1/performance/ebatAppraisals/employees";
    public static final String GET_EBAT_TEAM_ASSESSMENT_DETAIL = BASE_URL_CSC + "api/v1/performance/ebatAppraisals/";
    public static final String EBAT_TEAM_ASSESSMENT_APPROVE = BASE_URL_CSC + "api/v1/performance/ebatAppraisals/approveKRA";
    public static final String EBAT_TEAM_ASSESSMENT_REJECT = BASE_URL_CSC + "api/v1/performance/ebatAppraisals/rejectKRA";
    public static final String ADD_KRA = BASE_URL_CSC + "api/v1/performance/ebatAppraisals/kra";
    public static final String SUBMIT_KRA = BASE_URL_CSC + "api/v1/performance/ebatAppraisals/submitKRA";
}
