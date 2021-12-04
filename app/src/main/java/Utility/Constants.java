package Utility;

/**
 * Created by Deeksha on 15-12-2015.
 */
public interface Constants {

    String TAG_PUBLIC = "public";
    String TAG_STATUS = "status";

    // TABLE NAME CONTENT and respective columns;
    String TABLE_NAME_FEEDS = "feeds";
    String TAG_ID = "id";
    String TAG_MESSAGE = "message";
    String KEY_CONTENT_ID = "content_id";
    String TAG_COMMENT_EN = "commentsDisabled";
    String TAG_SHARE_EN = "shareDisabled";
    String TAG_ATTACHMENT_EN = "attachmentsDisabled";
    String TAG_TYPE = "type";
    String TAG_CREATED_TIME = "createdAt";
    String TAG_ITEM_TITLE = "title";
    String TAG_ITEM_DESC = "description";
    String TAG_CREATOR_NAME = "displayName";
    String TAG_ACTIVITY_TIME = "time";
    String TAG_ACTION_TEXT = "actionText";
    String TAG_SECOND_FULLNAME = "secondFullName";
    String TAG_IMAGE = "image";

    String TABLE_NAME_COMMENTS = "comments";
    String TAG_COMMENT_ID = "comment_id";
    String KEY_COMMENT_CONTENT_ID = "comment_content_id";
    String TAG_COMMENTS_TEXT = "comment_text";
    String TAG_COMMENTS_NAME = "comment_name";
    String TAG_COMMENT_TIME = "comment_time";
    String TAG_COMMENT_IMAGE = "comment_image";

    String TABLE_NAME_ATTACHMENTS = "attachments";
    String TAG_ATTACHMENT_FILE = "attachment_id";
    String KEY_ATTACHMENT_CONTENT_ID = "attachment_content_id";
    String TAG_ATTACHMENT_ID = "attachment_text";
    String TAG_ATTACHMENT_NAME = "attachment_name";


    String TABLE_CARDS = "cards_table";
    String KEY_CARD_ID = "card_id";
    String KEY_CARD_TYPE = "card_type";
    String KEY_CARD_SUBTYPE = "card_subtype";
    String KEY_CARD_FIELDS_MANDATE = "card_fields_m";
    String KEY_CARD_FIELDS_OPTION = "card_fields_o";
    String KEY_CARD_ACTIONS = "card_actions";

    String TASKS_TABLE_NAME = "tasks";
    String NOTIFICATIONS_TABLE_NAME = "notifications";

    String TASKS_TABLE_DATA = "Data";
    String NOTIFICATIONS_TABLE_DATA = "Data";


    String TASKS_TABLE_NAME_SCROLL = "scroll";
    String TASKS_TABLE_DATA_SCROLL = "Data";

    String TASKS_TABLE_NAME_DETAIL = "detail";
    String TASKS_TABLE_DATA_DETAIL = "Data";
    String TASKS_TABLE_DATA_DETAIL_ID = "detail_id";

    String TAB_TABLE_NAME_TABS = "tabs";
    String TAB_TABLE_DATA_TAB_ID = "tab_id";
    String TAB_TABLE_DATA_TAB_DEFAULTVALUE = "default_value";

    String TAB_TABLE_NAME_TYPES = "type";
    String TAB_TABLE_DATA_TAB_Type = "_type";

    String NAVIGATION_TABLE_NAME = "navigation";
    String NAVIGATION_TABLE_DATA_ID = "nav_id";
    String NAVIGATION_TABLE_DATA_OPTIONS = "nav_options";
    String NAVIGATION_TABLE_DATA_DISPLAYNAME = "display";

    String FILTERS_TABLE_NAME = "filters";
    String FILTERS_TABLE_DATA_ID = "filter_id";
    String FILTERS_TABLE_DATA_OPTIONS = "filter_options";
    String FILTERS_TABLE_DATA_DISPLAYNAME = "display";

    String ROLES_TABLE_NAME = "roles";
    String ROLES_TABLE_DATA_SLNO = "roles_slno";
    String ROLES_TABLE_DATA_ROLENAME = "roles_name";
    String ROLES_TABLE_DATA_ROLEREGION = "roles_region";

    String TASK_TABLE = "task_table";
    String ENTITY = "entity";
    String CREATE = "create_permission";
    String READ = "read_permission";
    String UPDATE = "update_permission";
    String DELETE = "delete_permission";

    String USER_PERMISSION_TABLE = "user_permission_table";
    String TASK_LEVEL = "task_level";
    String TASK_LEVEL_CODE = "task_level_code";
    String TASK_TYPE = "task_type";
    String TASK_TYPE_CODE = "task_type_code";

    String PENDING_OFFLINE_ACTION = "offline_actions_pending";
    String ACTION_TYPE = "action_type";
    String WORKITEM_ID = "workitem";
    String TEXT = "reason_comment";


    String TIMESHEET_TABLE = "timesheet_table";
    String DATE = "date";
    String START_DATE = "start_date";
    String END_DATE = "end_date";
    String PROJECT = "project_name";
    String PROJECT_CODE = "project_name_code";
    String ACTIVITY = "activity_name";
    String ACTIVITY_CODE = "activity_name_code";
    String HOURS = "hours";

    int MY_PERMISSIONS_REQUEST_CAMERA = 111;
    int MY_PERMISSIONS_REQUEST_GALLERY = 222;
    int MY_PERMISSIONS_REQUEST_PHONE = 13;
    int MY_PERMISSIONS_REQUEST_SMS = 144;

    String CONFIG_UPDATED = "configuration_update";

    String MY_TASKS = "My Tasks";
    String SENT_ITEMS = "Sent Items";
    String COMPLETED = "Completed";

    String MASTER_ROLE_TABLE_NAME = "MASTER_ROLE_TABLE";
    String ROLE_NAME = "ROLE_NAME";
    String ROLE_TYPE = "ROLE_TYPE";
    String ROLE_DISPLAY_NAME = "ROLE_DISPLAY_NAME";
    String ROLE_TEXT = "ROLE_TEXT";
    String ROLE_HR_FUNCTION = "ROLE_HR_FUNCTION";
    String ROLE_REGION = "ROLE_REGION";


    //Assessment Status
    String PENDING_REWORK = "Pending for KRA Rework";
    String MANAGER_APPROVAL_PENDING = "Pending for Manager Approval";
    String MATRIX_MANAGER_APPROVAL_PENDING = "Pending for Matrix Manager Approval";
    String APPROVED = "Approved";
    String PENDING_FOR_INPUT = "Pending for KRA Input";

    //KRA
    int EDIT_KRA = 1301;

    String KRA_MODEL = "KRA_MODEL";
    String ASSESSMENT_ID = "ID";

    String DOWNLOAD_PDF = "downloadPDF";
}
