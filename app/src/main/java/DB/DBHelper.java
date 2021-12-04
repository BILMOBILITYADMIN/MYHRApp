package DB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import Utility.Constants;


public class DBHelper extends SQLiteOpenHelper implements Constants {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "Cherrywork";


    String CREATE_TABLE_CONTENT = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME_FEEDS + "("
            + TAG_ID + " INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL  UNIQUE,"
            + KEY_CONTENT_ID + " VARCHAR," + TAG_COMMENT_EN + " VARCHAR,"
            + TAG_SHARE_EN + " VARCHAR," + TAG_ATTACHMENT_EN + " VARCHAR,"
            + TAG_TYPE + " VARCHAR," + TAG_CREATED_TIME + " VARCHAR,"
            + TAG_ITEM_TITLE + " VARCHAR," + TAG_ITEM_DESC + " VARCHAR,"
            + TAG_CREATOR_NAME + " VARCHAR," + TAG_ACTIVITY_TIME + " VARCHAR,"
            + TAG_ACTION_TEXT + " VARCHAR," + TAG_SECOND_FULLNAME + " VARCHAR,"
            + TAG_IMAGE + " VARCHAR" + ")";

    String CREATE_TABLE_COMMENT = "CREATE TABLE  IF NOT EXISTS " + TABLE_NAME_COMMENTS + "("
            + TAG_COMMENT_ID + " VARCHAR," + KEY_COMMENT_CONTENT_ID
            + " VARCHAR," + TAG_COMMENTS_TEXT + " VARCHAR," + TAG_COMMENTS_NAME
            + " VARCHAR," + TAG_COMMENT_TIME + " VARCHAR," + TAG_COMMENT_IMAGE
            + " VARCHAR"

            + ")";
    String CREATE_TABLE_ATTACHMENT = "CREATE TABLE  IF NOT EXISTS " + TABLE_NAME_ATTACHMENTS
            + "(" + TAG_ATTACHMENT_FILE + " VARCHAR,"
            + KEY_ATTACHMENT_CONTENT_ID + " VARCHAR," + TAG_ATTACHMENT_ID
            + " VARCHAR," + TAG_ATTACHMENT_NAME + " VARCHAR"

            + ")";


    String CREATE_TABLE_CARDS = "CREATE TABLE  IF NOT EXISTS " + TABLE_CARDS
            + "(" + KEY_CARD_ID + " VARCHAR,"
            + KEY_CARD_TYPE + " VARCHAR,"
            + KEY_CARD_SUBTYPE + " VARCHAR,"
            + KEY_CARD_FIELDS_MANDATE + " VARCHAR,"
            + KEY_CARD_FIELDS_OPTION + " VARCHAR," + KEY_CARD_ACTIONS + " VARCHAR"

            + ")";

    String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TASKS_TABLE_NAME + " ("
            + TASKS_TABLE_DATA + " TEXT"
            + ")";

    String CREATE_TABLE_SCROLL = "CREATE TABLE IF NOT EXISTS " + TASKS_TABLE_NAME_SCROLL + " ("
            + TASKS_TABLE_DATA_SCROLL + " TEXT"
            + ")";

    String CREATE_TABLE_DETAIL = "CREATE TABLE IF NOT EXISTS " + TASKS_TABLE_NAME_DETAIL + " ("
            + TASKS_TABLE_DATA_DETAIL + " TEXT," + TASKS_TABLE_DATA_DETAIL_ID + " TEXT"
            + ")";

    String CREATE_TABLE_TAB = "CREATE TABLE IF NOT EXISTS " + TAB_TABLE_NAME_TABS + " ("
            + TAB_TABLE_DATA_TAB_ID + " TEXT," + TAB_TABLE_DATA_TAB_DEFAULTVALUE + " TEXT"
            + ")";

    String CREATE_TABLE_TYPES = "CREATE TABLE IF NOT EXISTS " + TAB_TABLE_DATA_TAB_Type + " ("
            + TAB_TABLE_NAME_TYPES + " TEXT" + ")";

    String CREATE_TABLE_NAVIGATION = "CREATE TABLE IF NOT EXISTS " + NAVIGATION_TABLE_NAME + " ("
            + NAVIGATION_TABLE_DATA_ID + " TEXT," + NAVIGATION_TABLE_DATA_OPTIONS + " TEXT," + NAVIGATION_TABLE_DATA_DISPLAYNAME + " TEXT"
            + ")";

    String CREATE_TABLE_FILTERS = "CREATE TABLE IF NOT EXISTS " + FILTERS_TABLE_NAME + " ("
            + FILTERS_TABLE_DATA_ID + " TEXT," + FILTERS_TABLE_DATA_OPTIONS + " TEXT," + FILTERS_TABLE_DATA_DISPLAYNAME + " TEXT"
            + ")";

    String CREATE_TABLE_ROLES = "CREATE TABLE IF NOT EXISTS " + ROLES_TABLE_NAME + " ("
            + ROLES_TABLE_DATA_SLNO + " VARCHAR," + ROLES_TABLE_DATA_ROLENAME + " TEXT," + ROLES_TABLE_DATA_ROLEREGION + " TEXT"
            + ")";


    String CREATE_TABLE_NOTIFICATIONS = "CREATE TABLE IF NOT EXISTS " + NOTIFICATIONS_TABLE_NAME + " ("
            + NOTIFICATIONS_TABLE_DATA + " TEXT"
            + ")";
    String CREATE_TIMESHEET_DATABASE = "CREATE TABLE if not exists " + TIMESHEET_TABLE + "("
            + DATE + " VARCHAR,"
            + START_DATE + " VARCHAR," +
            END_DATE + " VARCHAR," +
            PROJECT + " VARCHAR," +
            PROJECT_CODE + " VARCHAR," +
            ACTIVITY + " VARCHAR," +
            ACTIVITY_CODE + " VARCHAR," +
            TASK_LEVEL_CODE + " VARCHAR," +
            TASK_TYPE_CODE + " VARCHAR," +

            HOURS + " FLOAT" + ")";

    String CREATE_TASK_DATABASE = "CREATE TABLE if not exists " + TASK_TABLE + "("
            + TASK_LEVEL + " VARCHAR," +
            TASK_LEVEL_CODE + " VARCHAR," +
            TASK_TYPE + " VARCHAR," +
            TASK_TYPE_CODE + " VARCHAR" +
            ")";

    String CREATE_USER_PERMISSION_DATABASE = "CREATE TABLE if not exists " + USER_PERMISSION_TABLE + "("
            + ENTITY + " VARCHAR," +
            CREATE + " VARCHAR," +
            READ + " VARCHAR," +
            UPDATE + " VARCHAR," +
            DELETE + " VARCHAR" +
            ")";
    String CREATE_OFFLINE_ACTIONS_TABLE = "CREATE TABLE if not exists " + PENDING_OFFLINE_ACTION + "("
            + ACTION_TYPE + " VARCHAR," +
            WORKITEM_ID + " VARCHAR," +
            TEXT + " VARCHAR" +

            ")";

    String CREATE_MASTER_ROLE_TABLE = "CREATE TABLE if not exists " + MASTER_ROLE_TABLE_NAME + "("
            + ROLE_TYPE + " VARCHAR," //Card Type
            + ROLE_NAME + " VARCHAR,"
            + ROLE_DISPLAY_NAME + " VARCHAR,"
            + ROLE_TEXT + " VARCHAR,"
            + ROLE_HR_FUNCTION + " VARCHAR,"
            + ROLE_REGION + " VARCHAR"
            + ")";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_CONTENT);
        db.execSQL(CREATE_TABLE_COMMENT);
        db.execSQL(CREATE_TABLE_ATTACHMENT);
        db.execSQL(CREATE_TABLE_CARDS);
        db.execSQL(CREATE_USER_PERMISSION_DATABASE);

        db.execSQL(CREATE_TABLE);
        db.execSQL(CREATE_TABLE_TAB);
        db.execSQL(CREATE_TABLE_NAVIGATION);
        db.execSQL(CREATE_TABLE_FILTERS);
        db.execSQL(CREATE_TABLE_SCROLL);
        db.execSQL(CREATE_TABLE_DETAIL);
        db.execSQL(CREATE_TABLE_NOTIFICATIONS);

        db.execSQL(CREATE_TABLE_ROLES);

        db.execSQL(CREATE_TIMESHEET_DATABASE);
        db.execSQL(CREATE_TASK_DATABASE);
        db.execSQL(CREATE_TABLE_TYPES);
        db.execSQL(CREATE_OFFLINE_ACTIONS_TABLE);

        db.execSQL(CREATE_MASTER_ROLE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


}