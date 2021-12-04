package Utility;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.hrapps.CherryworkApplication;
import com.hrapps.MainActivity;
import com.hrapps.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import DB.DbUtil;
import Model.FeedsDataModel;
import Model.UserProfile;
import adapters.InboxAdapter;

import static Utility.Util.isOnline;

/**
 * Created by Deeksha on 04-02-2016.
 */
public class Actions implements View.OnClickListener, AsyncResponse, View.OnLongClickListener {

    private boolean likeShown = true;
    private ListView _listToUpdate;
    Context _context = null;
    String _actionId = "";
    Object _workitem = null;
    String ID = "";
    String timesheet_type = "";
    TextView likescount, liketext;
    ImageView likeimage;
    int type = 0;
    boolean isBackground = false;
    EditText _comment = null;
    ArrayList<String> attachements = new ArrayList<String>();
    AlertDialog alertDialog;
    String reasonOrComment = "";
    String commentText = "";
    private InboxAdapter adapter;

    public Actions(Context context, String actionId, Object workitem, String workitem_id) {
        _context = context;
        _actionId = actionId;
        _workitem = workitem;
        ID = workitem_id;
    }

    //For background action
    public Actions(Context context, String actionId, String workitem_id, String reasonOrComment, boolean isBackground) {
        _context = context;
        _actionId = actionId;
        ID = workitem_id;
        this.reasonOrComment = reasonOrComment;
        _workitem = new FeedsDataModel();
        ((FeedsDataModel) _workitem).id = ID;
        this.isBackground = isBackground;
    }

    //Constructor for Like
    public Actions(Context context, String actionId, Object workitem, TextView likescount, TextView liketext, ImageView likeimage, int type, boolean isShown) {
        _context = context;
        _actionId = actionId;
        _workitem = workitem;
        this.likescount = likescount;
        this.liketext = liketext;
        this.likeimage = likeimage;
        this.type = type;
        this.likeShown = isShown;
    }

    //Constructor for comments in detail screen
    public Actions(Context context, String actionId, Object workitem, EditText comment, ListView listToUpdate) {
        _context = context;
        _actionId = actionId;
        _workitem = workitem;
        _comment = comment;
        _listToUpdate = listToUpdate;
    }

    //For timesheet Approval/Rejection

    public Actions(Context context, String actionId, String timesheetId, String type) {
        _context = context;
        _actionId = actionId;
        ID = timesheetId;
        this.timesheet_type = type;

    }

    public void setAdapter(InboxAdapter adapter) {
        this.adapter = adapter;
    }


    @Override
    public void onClick(View v) {
        switch (_actionId) {

            case "Approve_AppraisalTemplate":
                if (!isBackground) {

                    LayoutInflater li = LayoutInflater.from(_context);
                    final View dialog_view = li.inflate(R.layout.approve_reject_dialog, null);

                    TextView dialog_confirmation = (TextView) dialog_view.findViewById(R.id.dialog_confirmation);
                    TextView confirm_approval = (TextView) dialog_view.findViewById(R.id.confirm_approval);
                    TextView cancel_approval = (TextView) dialog_view.findViewById(R.id.cancel_approval);
                    if (timesheet_type.equalsIgnoreCase("goal setting")) {
                        dialog_confirmation.setText("Are you sure you want to approve this Goal Setting ?");
                    } else {
                        dialog_confirmation.setText("Are you sure you want to approve this Appraisal Template ?");
                    }

                    AlertDialog.Builder approve_dialog = new AlertDialog.Builder(_context);
                    approve_dialog.setView(dialog_view);

                    final AlertDialog appraisal_approve = approve_dialog.create();
                    appraisal_approve.show();

                    confirm_approval.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (isOnline(_context)) {
                                Map<String, String> approve_headers = new HashMap<String, String>();

                                approve_headers.put("x-email-id", CWIncturePreferences.getEmail());
                                approve_headers.put("x-access-token",
                                        CWIncturePreferences.getAccessToken());
                                approve_headers.put("Content-Type",
                                        "application/json");
                                NetworkConnector approve_connect = new NetworkConnector(_context, NetworkConnector.TYPE_POST, CWUrls.BASE_URL_CSC + "api/v1/approve/approvals/" + ID, approve_headers, null, Actions.this);
                                if (approve_connect.isAllowed()) {
                                    approve_connect.execute();
                                } else {
                                    Toast.makeText(_context, _context.getString(R.string.action_not_allowed), Toast.LENGTH_SHORT).show();
                                }
                            } else {

                                Toast.makeText(_context, "No internet Connection", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });

                    cancel_approval.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            appraisal_approve.dismiss();
                        }
                    });
                }

                break;

            case "Reject_AppraisalTemplate":

                if (!isBackground) {

                    LayoutInflater li = LayoutInflater.from(_context);
                    final View dialog_view = li.inflate(R.layout.approve_reject_dialog, null);

                    TextView dialog_confirmation = (TextView) dialog_view.findViewById(R.id.dialog_confirmation);
                    TextView confirm_approval = (TextView) dialog_view.findViewById(R.id.confirm_approval);
                    TextView cancel_approval = (TextView) dialog_view.findViewById(R.id.cancel_approval);
                    TextView dialog_title = (TextView) dialog_view.findViewById(R.id.dialog_title);

                    dialog_title.setText("Confirm Rejection");

                    dialog_confirmation.setText("Are you sure you want to reject this Appraisal Template ?");

                    AlertDialog.Builder reject_dialog = new AlertDialog.Builder(
                            _context);
                    reject_dialog.setView(dialog_view);

                    final AlertDialog appraisal_reject = reject_dialog.create();

                    appraisal_reject.show();

                    confirm_approval.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            appraisal_reject.dismiss();

                            LayoutInflater li = LayoutInflater.from(_context);
                            final View promptsView = li.inflate(R.layout.prompts, null);

                            TextView confirm_Reject = (TextView) promptsView.findViewById(R.id.confirm_reject);
                            TextView cancel_Reject = (TextView) promptsView.findViewById(R.id.cancel_reject);

                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(_context);
                            alertDialogBuilder.setView(promptsView);

                            final EditText reason_to_reject = (EditText) promptsView.findViewById(R.id.editText_reason);

                            // create alert dialog
                            final AlertDialog alertDialog = alertDialogBuilder.create();
                            alertDialog.show();

                            confirm_Reject.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    if (reason_to_reject.getText().toString().isEmpty()) {


                                        InputMethodManager imm = (InputMethodManager) _context.getSystemService(Context.INPUT_METHOD_SERVICE);
                                        imm.hideSoftInputFromWindow(promptsView.getWindowToken(), 0);
                                        Toast.makeText(_context, "Please enter a reason to reject", Toast.LENGTH_LONG).show();

                                    } else {
                                        if (isOnline(_context)) {
                                            InputMethodManager imm = (InputMethodManager) _context.getSystemService(Context.INPUT_METHOD_SERVICE);
                                            imm.hideSoftInputFromWindow(promptsView.getWindowToken(), 0);

                                            Map<String, String> approve_headers = new HashMap<String, String>();

                                            approve_headers.put("x-email-id", CWIncturePreferences.getEmail());
                                            approve_headers.put("x-access-token",
                                                    CWIncturePreferences.getAccessToken());
                                            approve_headers.put("Content-Type",
                                                    "application/json");

                                            JSONObject obj = new JSONObject();
                                            try {
                                                obj.put("reason", reason_to_reject.getText().toString());
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                            String reason = obj.toString();

                                            NetworkConnector approve_connect = new NetworkConnector(_context, NetworkConnector.TYPE_POST, CWUrls.BASE_URL_CSC + "api/v1/reject/approvals/" + ID, approve_headers, reason, Actions.this);
                                            if (approve_connect.isAllowed()) {
                                                approve_connect.execute();
                                            } else {
                                                Toast.makeText(_context, _context.getString(R.string.action_not_allowed), Toast.LENGTH_SHORT).show();
                                            }

                                        } else {

                                            Toast.makeText(_context, "No internet Connection", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }
                            });

                            cancel_Reject.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    alertDialog.cancel();
                                    InputMethodManager imm = (InputMethodManager) _context.getSystemService(Context.INPUT_METHOD_SERVICE);
                                    imm.hideSoftInputFromWindow(promptsView.getWindowToken(), 0);
                                }
                            });

                        }
                    });

                    cancel_approval.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            appraisal_reject.dismiss();
                        }
                    });
                }
                break;

            case "Approve_RecruitmentRequisition":

                if (!isBackground) {

                    LayoutInflater li = LayoutInflater.from(_context);
                    final View dialog_view = li.inflate(R.layout.approve_reject_dialog, null);

                    TextView dialog_confirmation = (TextView) dialog_view.findViewById(R.id.dialog_confirmation);
                    TextView confirm_approval = (TextView) dialog_view.findViewById(R.id.confirm_approval);
                    TextView cancel_approval = (TextView) dialog_view.findViewById(R.id.cancel_approval);
                    TextView dialog_title = (TextView) dialog_view.findViewById(R.id.dialog_title);

                    dialog_title.setText("Confirm Approval");

                    dialog_confirmation.setText("Are you sure you want to approve this position requisition ?");

                    AlertDialog.Builder approve_dialog = new AlertDialog.Builder(_context);
                    approve_dialog.setView(dialog_view);

                    final AlertDialog appraisal_approve = approve_dialog.create();
                    appraisal_approve.show();

                    confirm_approval.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (isOnline(_context)) {
                                Map<String, String> approve_headers = new HashMap<String, String>();

                                approve_headers.put("x-email-id", CWIncturePreferences.getEmail());
                                approve_headers.put("x-access-token",
                                        CWIncturePreferences.getAccessToken());
                                approve_headers.put("Content-Type",
                                        "application/json");
                                NetworkConnector approve_connect = new NetworkConnector(_context, NetworkConnector.TYPE_POST, CWUrls.BASE_URL_CSC + "api/v1/approve/approvals/" + ID, approve_headers, null, Actions.this);
                                if (approve_connect.isAllowed()) {
                                    approve_connect.execute();
                                } else {
                                    Toast.makeText(_context, _context.getString(R.string.action_not_allowed), Toast.LENGTH_SHORT).show();
                                    Toast.makeText(_context, _context.getString(R.string.action_not_allowed), Toast.LENGTH_SHORT).show();
                                }
                            } else {

                                Toast.makeText(_context, "No internet Connection", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });

                    cancel_approval.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            appraisal_approve.dismiss();
                        }
                    });
                }

                break;

            case "Reject_RecruitmentRequisition":

                if (!isBackground) {

                    LayoutInflater li = LayoutInflater.from(_context);
                    final View dialog_view = li.inflate(R.layout.approve_reject_dialog, null);

                    TextView confirm_approval = (TextView) dialog_view.findViewById(R.id.confirm_approval);
                    TextView cancel_approval = (TextView) dialog_view.findViewById(R.id.cancel_approval);
                    TextView dialog_confirmation = (TextView) dialog_view.findViewById(R.id.dialog_confirmation);
                    TextView dialog_title = (TextView) dialog_view.findViewById(R.id.dialog_title);

                    dialog_title.setText("Confirm Rejection");
                    dialog_confirmation.setText("Are you sure you want to reject this position requisition ?");

                    AlertDialog.Builder reject_dialog = new AlertDialog.Builder(
                            _context);
                    reject_dialog.setView(dialog_view);

                    final AlertDialog appraisal_reject = reject_dialog.create();

                    appraisal_reject.show();

                    confirm_approval.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            appraisal_reject.dismiss();

                            LayoutInflater li = LayoutInflater.from(_context);
                            final View promptsView = li.inflate(R.layout.prompts, null);

                            TextView confirm_Reject = (TextView) promptsView.findViewById(R.id.confirm_reject);
                            TextView cancel_Reject = (TextView) promptsView.findViewById(R.id.cancel_reject);

                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(_context);
                            alertDialogBuilder.setView(promptsView);

                            final EditText reason_to_reject = (EditText) promptsView.findViewById(R.id.editText_reason);

                            // create alert dialog
                            final AlertDialog alertDialog = alertDialogBuilder.create();
                            alertDialog.show();

                            confirm_Reject.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    if (reason_to_reject.getText().toString().isEmpty()) {


                                        InputMethodManager imm = (InputMethodManager) _context.getSystemService(Context.INPUT_METHOD_SERVICE);
                                        imm.hideSoftInputFromWindow(promptsView.getWindowToken(), 0);
                                        Toast.makeText(_context, "Please enter a reason to reject", Toast.LENGTH_LONG).show();

                                    } else {
                                        if (isOnline(_context)) {
                                            InputMethodManager imm = (InputMethodManager) _context.getSystemService(Context.INPUT_METHOD_SERVICE);
                                            imm.hideSoftInputFromWindow(promptsView.getWindowToken(), 0);

                                            Map<String, String> approve_headers = new HashMap<String, String>();

                                            approve_headers.put("x-email-id", CWIncturePreferences.getEmail());
                                            approve_headers.put("x-access-token",
                                                    CWIncturePreferences.getAccessToken());
                                            approve_headers.put("Content-Type",
                                                    "application/json");

                                            JSONObject obj = new JSONObject();
                                            try {
                                                obj.put("reason", reason_to_reject.getText().toString());
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                            String reason = obj.toString();

                                            NetworkConnector approve_connect = new NetworkConnector(_context, NetworkConnector.TYPE_POST, CWUrls.BASE_URL_CSC + "api/v1/reject/approvals/" + ID, approve_headers, reason, Actions.this);
                                            if (approve_connect.isAllowed()) {
                                                approve_connect.execute();
                                            } else {
                                                Toast.makeText(_context, _context.getString(R.string.action_not_allowed), Toast.LENGTH_SHORT).show();
                                            }

                                        } else {

                                            Toast.makeText(_context, "No internet Connection", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }
                            });

                            cancel_Reject.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    alertDialog.cancel();
                                    InputMethodManager imm = (InputMethodManager) _context.getSystemService(Context.INPUT_METHOD_SERVICE);
                                    imm.hideSoftInputFromWindow(promptsView.getWindowToken(), 0);
                                }
                            });

                        }
                    });

                    cancel_approval.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            appraisal_reject.dismiss();
                        }
                    });
                }

                break;

            case "APPROVE":
                if (!isBackground) {
                    AlertDialog.Builder approve_dialog = new AlertDialog.Builder(
                            _context);
                    approve_dialog.setTitle("Confirm Approval");
                    approve_dialog.setMessage("Are you sure you want to approve this leave?");
                    approve_dialog.setPositiveButton("Confirm",
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    if (isOnline(_context)) {
                                        Map<String, String> approve_headers = new HashMap<String, String>();

                                        approve_headers.put("x-email-id", CWIncturePreferences.getEmail());
                                        approve_headers.put("x-access-token",
                                                CWIncturePreferences.getAccessToken());
                                        approve_headers.put("Content-Type",
                                                "application/json");
                                        NetworkConnector approve_connect = new NetworkConnector(_context, NetworkConnector.TYPE_POST, CWUrls.BASE_URL + "api/v1/approve/approvals/" + ((FeedsDataModel) _workitem).id, approve_headers, null, Actions.this);
                                        if (approve_connect.isAllowed()) {
                                            approve_connect.execute();
                                        } else {
                                            Toast.makeText(_context, _context.getString(R.string.action_not_allowed), Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        ContentValues offline = new ContentValues();
                                        offline.put(Constants.ACTION_TYPE, "APPROVE");
                                        offline.put(Constants.WORKITEM_ID, ((FeedsDataModel) _workitem).id);
                                        offline.put(Constants.TEXT, "");
                                        if (!DbUtil.actionTakenForWorkitem(_context, ((FeedsDataModel) _workitem).id)) {
                                            DbUtil.insert(Constants.PENDING_OFFLINE_ACTION, offline);
                                            Toast.makeText(_context, "No internet. Timesheet will be approved once online", Toast.LENGTH_LONG).show();

                                        } else {
                                            Toast.makeText(_context, _context.getString(R.string.action_already_taken), Toast.LENGTH_LONG).show();

                                        }
                                    }

                                }
                            });
                    approve_dialog.setNegativeButton("No",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
                    AlertDialog leave_approve = approve_dialog.create();

                    // show it
                    leave_approve.show();
                } else {
                    Map<String, String> approve_headers = new HashMap<String, String>();

                    approve_headers.put("x-email-id", CWIncturePreferences.getEmail());
                    approve_headers.put("x-access-token",
                            CWIncturePreferences.getAccessToken());
                    approve_headers.put("Content-Type",
                            "application/json");
                    NetworkConnector approve_connect = new NetworkConnector(_context, NetworkConnector.TYPE_POST, CWUrls.BASE_URL + "api/v1/approve/approvals/" + ((FeedsDataModel) _workitem).id, approve_headers, null, Actions.this, true);
                    if (approve_connect.isAllowed()) {
                        approve_connect.execute();
                    }
                }

                break;
            case "REJECT":
                if (!isBackground) {
                    AlertDialog.Builder reject_dialog = new AlertDialog.Builder(
                            _context);
                    LayoutInflater reject_inflater = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View reject_view = reject_inflater.inflate(R.layout.textfield, null);
                    final EditText reject_reason = (EditText) reject_view.findViewById(R.id.text);
                    reject_dialog.setTitle("Reject Approval");
                    reject_dialog.setView(reject_view);
                    reject_dialog.setMessage("Are you sure you want to reject this leave?");
                    reject_dialog.setPositiveButton("Confirm",
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    if (isOnline(_context)) {
                                        JSONObject mainJson = new JSONObject();
                                        try {
                                            mainJson.put("reason", reject_reason.getText().toString());
                                        } catch (JSONException jEx) {

                                        }
                                        Map<String, String> reject_headers = new HashMap<String, String>();

                                        reject_headers.put("x-email-id", CWIncturePreferences.getEmail());
                                        reject_headers.put("x-access-token",
                                                CWIncturePreferences.getAccessToken());
                                        reject_headers.put("Content-Type",
                                                "application/json");
                                        NetworkConnector reject_connect = new NetworkConnector(_context, NetworkConnector.TYPE_POST, CWUrls.BASE_URL + "api/v1/reject/approvals/" + ((FeedsDataModel) _workitem).id, reject_headers, mainJson.toString(), Actions.this);
                                        if (reject_connect.isAllowed()) {
                                            reject_connect.execute();
                                        } else {
                                            Toast.makeText(_context, _context.getString(R.string.action_not_allowed), Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        ContentValues offline = new ContentValues();
                                        offline.put(Constants.ACTION_TYPE, "REJECT");
                                        offline.put(Constants.WORKITEM_ID, ((FeedsDataModel) _workitem).id);
                                        offline.put(Constants.TEXT, reject_reason.getText().toString());
                                        if (!DbUtil.actionTakenForWorkitem(_context, ((FeedsDataModel) _workitem).id)) {
                                            DbUtil.insert(Constants.PENDING_OFFLINE_ACTION, offline);
                                            Toast.makeText(_context, "No internet. Rejection will be completed once online", Toast.LENGTH_LONG).show();
                                        } else {
                                            Toast.makeText(_context, _context.getString(R.string.action_already_taken), Toast.LENGTH_LONG).show();

                                        }
                                    }

                                }
                            });
                    reject_dialog.setNegativeButton("No",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    // if this button is clicked, just close
                                    // the dialog box and do nothing
                                    dialog.cancel();
                                }
                            });
                    AlertDialog reject_alertDialog = reject_dialog.create();

                    // show it
                    reject_alertDialog.show();
                } else {
                    JSONObject mainJson = new JSONObject();
                    try {
                        mainJson.put("reason", reasonOrComment);
                    } catch (JSONException jEx) {

                    }
                    Map<String, String> reject_headers = new HashMap<String, String>();

                    reject_headers.put("x-email-id", CWIncturePreferences.getEmail());
                    reject_headers.put("x-access-token",
                            CWIncturePreferences.getAccessToken());
                    reject_headers.put("Content-Type",
                            "application/json");
                    NetworkConnector reject_connect = new NetworkConnector(_context, NetworkConnector.TYPE_POST, CWUrls.BASE_URL + "api/v1/reject/approvals/" + ((FeedsDataModel) _workitem).id, reject_headers, mainJson.toString(), Actions.this);
                    if (reject_connect.isAllowed()) {
                        reject_connect.execute();
                    }
                }

                break;

            case "COMMENT": {

                if (!isBackground) {

                    LayoutInflater inflater = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    final View vi = inflater.inflate(R.layout.comments_dialog, null);

                    AlertDialog.Builder alert = new AlertDialog.Builder(_context);

                    alert.setView(vi);

                    alertDialog = alert.create();
                    alertDialog.show();

                    final EditText commentsEd = (EditText) vi.findViewById(R.id.commentsEd);


                    final TextView count = (TextView) vi.findViewById(R.id.attach_count);
                    ((CherryworkApplication) _context.getApplicationContext()).setAttachmentCountView(count);

                    Button send = (Button) vi.findViewById(R.id.post_comment);

                    send.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            View current_view = ((Activity) _context).getCurrentFocus();
                            if (view != null) {
                                InputMethodManager imm = (InputMethodManager) _context.getSystemService(Context.INPUT_METHOD_SERVICE);
                                imm.hideSoftInputFromWindow(current_view.getWindowToken(), 0);
                            }
                            if (commentsEd == null) {
                                commentText = reasonOrComment;
                            } else {
                                commentText = commentsEd.getText().toString();

                            }

                            if (commentText.length() == 0) {
                                Toast.makeText(_context, "Nothing to post!!!!!", Toast.LENGTH_SHORT).show();
                            } else {

                                Map<String, String> headers = new HashMap<String, String>();

                                headers.put("x-email-id", CWIncturePreferences.getEmail());
                                headers.put("x-access-token",
                                        CWIncturePreferences.getAccessToken());
                                headers.put("Content-Type",
                                        "application/json");

                                JSONObject object = new JSONObject();
                                try {
                                    object.put("text", commentText);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                String comment = object.toString();


                                NetworkConnector connect = new NetworkConnector(_context, NetworkConnector.TYPE_POST, CWUrls.ADD_COMMENT + ((FeedsDataModel) _workitem).id, headers, comment, Actions.this);
                                if (connect.isAllowed()) {
                                    if (Util.isOnline(_context)) {
                                        connect.execute();
                                    } else if (!isBackground) {
                                        Toast.makeText(_context, "Comment will be posted once online", Toast.LENGTH_SHORT).show();

                                        ContentValues offline = new ContentValues();
                                        offline.put(Constants.ACTION_TYPE, "COMMENT");
                                        offline.put(Constants.WORKITEM_ID, ((FeedsDataModel) _workitem).id);
                                        offline.put(Constants.TEXT, commentText);
                                        DbUtil.insert(Constants.PENDING_OFFLINE_ACTION, offline);


                                    }
                                } else {
                                    Toast.makeText(_context, _context.getString(R.string.action_not_allowed), Toast.LENGTH_SHORT).show();
                                }

                                alertDialog.dismiss();


                            }


                        }
                    });

                    ImageView imv = (ImageView) vi.findViewById(R.id.attachicon);
                    imv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
//                            Intent intent = new Intent(_context, AttachUI.class);
//                            attachements = ((CherryworkApplication) _context.getApplicationContext()).getAttachments();
//                            intent.putExtra("ATTACHMENTS", attachements);
//                            ((Activity) _context).startActivityForResult(intent, 100);


                        }
                    });

                } else {
                    Map<String, String> headers = new HashMap<String, String>();

                    headers.put("x-email-id", CWIncturePreferences.getEmail());
                    headers.put("x-access-token",
                            CWIncturePreferences.getAccessToken());
                    headers.put("Content-Type",
                            "application/json");

                    JSONObject object = new JSONObject();
                    try {
                        object.put("text", reasonOrComment);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    String comment = object.toString();

                    NetworkConnector connect = new NetworkConnector(_context, NetworkConnector.TYPE_POST, CWUrls.ADD_COMMENT + ((FeedsDataModel) _workitem).id, headers, comment, Actions.this, isBackground);
                    if (connect.isAllowed()) {

                        connect.execute();

                    }
                }
                break;
            }

            //Add comment from workitem detail screen
            case "COMMENT_DETAIL": {

                if (_comment == null) {
                    commentText = reasonOrComment;
                } else {
                    commentText = _comment.getText().toString();
                }
                if (!isBackground) {
                    View view = ((Activity) _context).getCurrentFocus();
                    if (view != null) {
                        InputMethodManager imm = (InputMethodManager) _context.getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }
                    attachements = ((CherryworkApplication) _context.getApplicationContext()).getAttachments();

                    if (commentText.length() == 0 && attachements.size() == 0) {
                        Toast.makeText(_context, "Nothing to post!!!!!", Toast.LENGTH_SHORT).show();
                    } else if (commentText.length() == 0) {
                        AlertDialog alertDialog;
                        AlertDialog.Builder alert1 = new AlertDialog.Builder(_context);

                        alert1.setTitle("Post Comment");
                        alert1.setMessage("Are you sure you want to post without any comment ?");

                        alert1.setNegativeButton("No", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                        alert1.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {

                                Map<String, String> headers = new HashMap<String, String>();

                                headers.put("x-email-id", CWIncturePreferences.getEmail());
                                headers.put("x-access-token",
                                        CWIncturePreferences.getAccessToken());
                                headers.put("Content-Type",
                                        "application/json");

                                JSONObject object = new JSONObject();
                                try {
                                    object.put("text", commentText);

                                    if (attachements != null && attachements.size() > 0) {
                                        JSONArray images = new JSONArray();

                                        for (int i = 0; i < attachements.size(); i++) {
                                            JSONObject imageMap = new JSONObject();

                                            String filepath = attachements.get(i);
                                            File imageFile = new File(filepath);
                                            String name = imageFile.getName();

                                            Bitmap bm = BitmapFactory.decodeFile(filepath);

                                            ByteArrayOutputStream stream = new ByteArrayOutputStream();
                                            bm.compress(Bitmap.CompressFormat.JPEG, 50, stream);
                                            byte[] byteArray = stream.toByteArray();
                                            String encodedImage = Base64.encodeBytes(byteArray);

                                            imageMap.put(name, encodedImage);

                                            images.put(imageMap);
                                            bm.recycle();
                                            System.gc();
                                        }


                                        object.put("files", images);
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                String comment = object.toString();

                                NetworkConnector connect = new NetworkConnector(_context, NetworkConnector.TYPE_POST, CWUrls.ADD_COMMENT + ((FeedsDataModel) _workitem).id, headers, comment, Actions.this);
                                if (connect.isAllowed()) {
                                    connect.execute();
                                } else {
                                    Toast.makeText(_context, _context.getString(R.string.action_not_allowed), Toast.LENGTH_SHORT).show();
                                }

                                dialog.cancel();
                            }
                        });
                        if (Util.isOnline(_context)) {
                            alertDialog = alert1.create();
                            alertDialog.show();
                        } else if (!isBackground) {
                            Toast.makeText(_context, "Cannot add attachments when offline", Toast.LENGTH_SHORT).show();
                        }
                    }//end of (_comment.length==0)


                    else {

                        Map<String, String> headers = new HashMap<String, String>();

                        headers.put("x-email-id", CWIncturePreferences.getEmail());
                        headers.put("x-access-token",
                                CWIncturePreferences.getAccessToken());
                        headers.put("Content-Type",
                                "application/json");

                        JSONObject object = new JSONObject();
                        try {
                            object.put("text", commentText);

                            if (attachements != null && attachements.size() > 0) {
                                JSONArray images = new JSONArray();

                                for (int i = 0; i < attachements.size(); i++) {
                                    JSONObject imageMap = new JSONObject();

                                    String filepath = attachements.get(i);
                                    File imageFile = new File(filepath);
                                    String name = imageFile.getName();

                                    Bitmap bm = BitmapFactory.decodeFile(filepath);

                                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                                    bm.compress(Bitmap.CompressFormat.JPEG, 50, stream);
                                    byte[] byteArray = stream.toByteArray();
                                    String encodedImage = Base64.encodeBytes(byteArray);

                                    imageMap.put(name, encodedImage);

                                    images.put(imageMap);
                                    bm.recycle();
                                    System.gc();
                                }
                                object.put("files", images);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        String comment = object.toString();

                        NetworkConnector connect = new NetworkConnector(_context, NetworkConnector.TYPE_POST, CWUrls.ADD_COMMENT + ((FeedsDataModel) _workitem).id, headers, comment, Actions.this);
                        if (connect.isAllowed()) {
                            if (Util.isOnline(_context)) {
                                connect.execute();
                            } else if (!isBackground) {
                                if (_comment != null) {
                                    _comment.setText("");
                                }
                                if (attachements != null && attachements.size() > 0) {
                                    Toast.makeText(_context, "Comment will be posted once online without the attachments", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(_context, "Comment will be posted once online", Toast.LENGTH_SHORT).show();

                                }

                                ContentValues offline = new ContentValues();
                                offline.put(Constants.ACTION_TYPE, "COMMENT_DETAIL");
                                offline.put(Constants.WORKITEM_ID, ((FeedsDataModel) _workitem).id);
                                offline.put(Constants.TEXT, commentText);
                                DbUtil.insert(Constants.PENDING_OFFLINE_ACTION, offline);


                            }
                        } else {
                            Toast.makeText(_context, _context.getString(R.string.action_not_allowed), Toast.LENGTH_SHORT).show();
                        }

                    }

                } else {
                    Map<String, String> headers = new HashMap<String, String>();

                    headers.put("x-email-id", CWIncturePreferences.getEmail());
                    headers.put("x-access-token",
                            CWIncturePreferences.getAccessToken());
                    headers.put("Content-Type",
                            "application/json");

                    JSONObject object = new JSONObject();
                    try {
                        object.put("text", commentText);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    String comment = object.toString();

                    NetworkConnector connect = new NetworkConnector(_context, NetworkConnector.TYPE_POST, CWUrls.ADD_COMMENT + ((FeedsDataModel) _workitem).id, headers, comment, Actions.this, true);
                    if (connect.isAllowed()) {

                        connect.execute();

                    }
                }
            }
            break;


            case "Comment_GoalSetting": {
                if (!isBackground) {

                    LayoutInflater inflater = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    final View vi = inflater.inflate(R.layout.alert_dialog1, null);

                    AlertDialog.Builder alert = new AlertDialog.Builder(_context);

                    alert.setView(vi);

                    alertDialog = alert.create();
                    alertDialog.show();

                    ((CherryworkApplication) _context.getApplicationContext()).clearAttachments();
                    final EditText commentsEd = (EditText) vi.findViewById(R.id.commentsEd);


                    final TextView count = (TextView) vi.findViewById(R.id.attach_count);
                    ((CherryworkApplication) _context.getApplicationContext()).setAttachmentCountView(count);

                    Button send = (Button) vi.findViewById(R.id.post_comment);

                    send.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            View current_view = ((Activity) _context).getCurrentFocus();
                            if (view != null) {
                                InputMethodManager imm = (InputMethodManager) _context.getSystemService(Context.INPUT_METHOD_SERVICE);
                                imm.hideSoftInputFromWindow(current_view.getWindowToken(), 0);
                            }
                            if (commentsEd == null) {
                                commentText = reasonOrComment;
                            } else {
                                commentText = commentsEd.getText().toString();

                            }
                            if (commentText.length() == 0) {
                                AlertDialog alertDialog;
                                AlertDialog.Builder alert = new AlertDialog.Builder(_context);

                                alert.setTitle("Post Comment");
                                alert.setMessage("Are you sure you want to post without any comment ?");

                                alert.setNegativeButton("No", new DialogInterface.OnClickListener() {

                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });

                                alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int id) {

                                        Map<String, String> headers = new HashMap<String, String>();

                                        headers.put("x-email-id", CWIncturePreferences.getEmail());
                                        headers.put("x-access-token",
                                                CWIncturePreferences.getAccessToken());
                                        headers.put("Content-Type",
                                                "application/json");

                                        JSONObject object = new JSONObject();
                                        try {
                                            object.put("text", commentText);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                        String comment = object.toString();
                                        NetworkConnector connect = new NetworkConnector(_context, NetworkConnector.TYPE_POST, CWUrls.ADD_COMMENT + ((FeedsDataModel) _workitem).id, headers, comment, Actions.this);
                                        if (connect.isAllowed()) {
                                            if (Util.isOnline(_context)) {
                                                connect.execute();
                                            }
                                        } else {
                                            Toast.makeText(_context, _context.getString(R.string.action_not_allowed), Toast.LENGTH_SHORT).show();
                                        }
                                        dialog.cancel();

                                    }
                                });
                                if (Util.isOnline(_context)) {
                                    alertDialog = alert.create();
                                    alertDialog.show();
                                }
                            } else {

                                Map<String, String> headers = new HashMap<String, String>();

                                headers.put("x-email-id", CWIncturePreferences.getEmail());
                                headers.put("x-access-token",
                                        CWIncturePreferences.getAccessToken());
                                headers.put("Content-Type",
                                        "application/json");

                                JSONObject object = new JSONObject();
                                try {
                                    object.put("text", commentText);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                String comment = object.toString();
                                NetworkConnector connect = new NetworkConnector(_context, NetworkConnector.TYPE_POST, CWUrls.ADD_COMMENT + ((FeedsDataModel) _workitem).id, headers, comment, Actions.this);
                                if (connect.isAllowed()) {
                                    if (Util.isOnline(_context)) {
                                        connect.execute();
                                    } else if (!isBackground) {
                                        Toast.makeText(_context, "Comment will be posted once online", Toast.LENGTH_SHORT).show();

                                        ContentValues offline = new ContentValues();
                                        offline.put(Constants.ACTION_TYPE, "COMMENT");
                                        offline.put(Constants.WORKITEM_ID, ((FeedsDataModel) _workitem).id);
                                        offline.put(Constants.TEXT, commentText);
                                        DbUtil.insert(Constants.PENDING_OFFLINE_ACTION, offline);
                                    }
                                } else {
                                    Toast.makeText(_context, _context.getString(R.string.action_not_allowed), Toast.LENGTH_SHORT).show();
                                }

                                alertDialog.dismiss();


                            }


                        }
                    });

                } else {
                    Map<String, String> headers = new HashMap<String, String>();

                    headers.put("x-email-id", CWIncturePreferences.getEmail());
                    headers.put("x-access-token",
                            CWIncturePreferences.getAccessToken());
                    headers.put("Content-Type",
                            "application/json");

                    JSONObject object = new JSONObject();
                    try {
                        object.put("text", reasonOrComment);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    String comment = object.toString();


                    NetworkConnector connect = new NetworkConnector(_context, NetworkConnector.TYPE_POST, CWUrls.ADD_COMMENT + ((FeedsDataModel) _workitem).id, headers, comment, Actions.this, isBackground);
                    if (connect.isAllowed()) {

                        connect.execute();

                    }
                }
                break;
            }

            //Delete a work item
            case "DELETE":

            {

                Map<String, String> headers = new HashMap<String, String>();

                headers.put("x-email-id", CWIncturePreferences.getEmail());
                headers.put("x-access-token",
                        CWIncturePreferences.getAccessToken());
                headers.put("Content-Type",
                        "application/json");


                NetworkConnector connect = new NetworkConnector(_context, NetworkConnector.TYPE_DELETE, CWUrls.GET_WORKITEM_DETAIL + ((FeedsDataModel) _workitem).id, headers, null, Actions.this);
                if (connect.isAllowed()) {
                    connect.execute();
                } else {
                    Toast.makeText(_context, _context.getString(R.string.action_not_allowed), Toast.LENGTH_SHORT).show();
                }

            }


            break;

            case "EMAIL":

                //To send an email

                Intent email = new Intent(Intent.ACTION_SEND);
                email.putExtra(Intent.EXTRA_EMAIL, new String[]{((UserProfile) _workitem).getEmail()});

                //need this to prompts email client only
                email.setType("message/rfc822");
                _context.startActivity(Intent.createChooser(email, "Choose an Email client :"));

                break;


            case "SMS":

                _context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.fromParts("sms", ((UserProfile) _workitem).getPhone(), null)));

                break;


            case "HANGOUTS":

                PackageManager pm = _context.getPackageManager();


                try {
                    Intent waIntent = new Intent(Intent.ACTION_SEND);
                    waIntent.setType("text/plain");
                    String text = "YOUR TEXT HERE";

                    PackageInfo info = pm.getPackageInfo("com.google.android.talk", PackageManager.GET_META_DATA);
                    //Check if package exists or not. If not then code
                    //in the catch block will be called
                    waIntent.setPackage("com.google.android.talk");

                    waIntent.putExtra(Intent.EXTRA_TEXT, text);
                    _context.startActivity(Intent.createChooser(waIntent, "Share with"));
                } catch (PackageManager.NameNotFoundException e) {
                    Toast.makeText(_context, "Not installed", Toast.LENGTH_SHORT).show();
                }

                break;

            case "CALL":

                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + ((UserProfile) _workitem).getPhone()));

                if (ContextCompat.checkSelfPermission(_context,
                        Manifest.permission.CALL_PHONE)
                        != PackageManager.PERMISSION_GRANTED) {

                    {
                        ActivityCompat.requestPermissions((Activity) _context,
                                new String[]{Manifest.permission.CALL_PHONE},
                                Constants.MY_PERMISSIONS_REQUEST_PHONE);

                    }
                }
                _context.startActivity(intent);

                break;


        }


    }

    @Override
    public void processFinish(String output, int status_code, String url, int type) {

        if (url.contains(CWUrls.ADD_COMMENT) && type == NetworkConnector.TYPE_POST) {

            ((CherryworkApplication) _context.getApplicationContext()).clearAttachments();
            try {
                JSONObject object = new JSONObject(output);
                if (object.getString("status").equalsIgnoreCase("success")) {
                    if (alertDialog != null) {
                        alertDialog.dismiss();
                    }
                    if (!isBackground) {
                        Intent intent = new Intent(_context, MainActivity.class);
                        _context.startActivity(intent);
                    } else {
                        DbUtil.delete(_context, Constants.PENDING_OFFLINE_ACTION, Constants.ACTION_TYPE + "= '" + _actionId + "' and " + Constants.WORKITEM_ID + "= '" + ID + "' and " + Constants.TEXT + " = '" + reasonOrComment + "'", null);
                    }

                } else if (!object.optString("message").isEmpty()) {
                    Toast.makeText(_context, object.optString("message"), Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        } else if (url.contains(CWUrls.GET_WORKITEM_DETAIL) && type == NetworkConnector.TYPE_DELETE) {

            try {
                JSONObject object = new JSONObject(output);
                if (object.getString("status").equalsIgnoreCase("success")) {


                    Intent intent = new Intent(_context, MainActivity.class);
                    _context.startActivity(intent);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        } else if (url.contains("api/v1/approve/approvals")) {

            try {
                JSONObject object = new JSONObject(output);
                if (object.getString("status").equalsIgnoreCase("success")) {
                    if (!isBackground) {

                        String message = object.optString("message");

                        Toast.makeText(_context, message, Toast.LENGTH_SHORT)
                                .show();
                        Intent intent = new Intent(_context, MainActivity.class);
                        intent.putExtra("pos", "no");
                        _context.startActivity(intent);
                    }
                } else if (object.getString("message") != null && !object.getString("message").isEmpty()) {
                    Toast.makeText(_context, object.getString("message"), Toast.LENGTH_SHORT)
                            .show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        } else if (url.contains("api/v1/reject/approvals")) {

            try {
                JSONObject object = new JSONObject(output);
                if (object.getString("status").equalsIgnoreCase("success")) {
                    if (!isBackground) {

                        String message = object.optString("message");

                        Toast.makeText(_context, message, Toast.LENGTH_SHORT)
                                .show();
                        Intent intent = new Intent(_context, MainActivity.class);
                        intent.putExtra("pos", "no");
                        _context.startActivity(intent);
                    }
                } else if (object.getString("message") != null && !object.getString("message").isEmpty()) {
                    Toast.makeText(_context, object.getString("message"), Toast.LENGTH_SHORT)
                            .show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void configurationUpdated(boolean configUpdated) {

    }

    @Override
    public boolean onLongClick(View v) {

        {
            AlertDialog.Builder alert = new AlertDialog.Builder(_context);

            alert.setTitle("Confirm Delete");
            if (((FeedsDataModel) _workitem).type.contains("task"))
                alert.setMessage("Do you want to delete this task?");
            else
                alert.setMessage("Do you want to delete this feed?");
            alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Map<String, String> headers = new HashMap<String, String>();

                    headers.put("x-email-id", CWIncturePreferences.getEmail());
                    headers.put("x-access-token",
                            CWIncturePreferences.getAccessToken());
                    headers.put("Content-Type",
                            "application/json");

                    NetworkConnector connect = new NetworkConnector(_context, NetworkConnector.TYPE_DELETE, CWUrls.GET_WORKITEM_DETAIL + ((FeedsDataModel) _workitem).id, headers, null, Actions.this);
                    if (connect.isAllowed()) {
                        connect.execute();
                    } else {
                        Toast.makeText(_context, _context.getString(R.string.action_not_allowed), Toast.LENGTH_SHORT).show();
                    }
                    dialog.dismiss();

                }
            });
            alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            final AlertDialog alertDialog = alert.create();
            alertDialog.show();
            return false;
        }


    }

}

