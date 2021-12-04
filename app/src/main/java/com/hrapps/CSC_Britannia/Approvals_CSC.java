package com.hrapps.CSC_Britannia;

import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hrapps.BasicActivity;
import com.hrapps.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import DB.DbAdapter;
import DB.DbUtil;
import Model.AttachmentModel;
import Model.CommentModel;
import Model.FeedsDataModel;
import Model.FeedsMainModel;
import Model.LikeModel;
import Utility.AsyncResponse;
import Utility.CWIncturePreferences;
import Utility.CWUrls;
import Utility.NetworkConnector;
import Utility.Util;
import adapters.InboxAdapter;

import static Utility.Util.isOnline;

/**
 * Created by harshu on 9/29/2016.
 */
public class Approvals_CSC extends BasicActivity implements AsyncResponse, SwipeRefreshLayout.OnRefreshListener {

    RecyclerView approvals_list;
    String perspect = "";
    boolean allItemsDownloaded = false;
    FeedsMainModel fmodel = new FeedsMainModel();
    public ArrayList<FeedsDataModel> list = new ArrayList<>();
    public ArrayList<FeedsDataModel> original_list = new ArrayList<>();
    ArrayList<CommentModel> subscriber = new ArrayList<CommentModel>();
    public ArrayList<AttachmentModel> attachlist, attachlistwithcomments = new ArrayList<>();
    public ArrayList<LikeModel> likelist = new ArrayList<>();

    public ArrayList<CommentModel> commentlist = new ArrayList<>();
    public ArrayList<DataModel> dayslist = new ArrayList<>();
    public ArrayList<TimesheetTaskModel> taskslist = new ArrayList<>();
    ArrayList<CommentModel> owner = new ArrayList<>();
    InboxAdapter adapter;


    int skip = 0;
    int limit = 10;
    boolean pagination = false;
    private TextView empty;
    boolean offline = true;
    ImageView back;


    private SwipeRefreshLayout refreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.approvals_csc);

        //sets the status bar color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.my_primary_dark));
        }

        approvals_list = (RecyclerView) findViewById(R.id.tasklist);
        empty = (TextView) findViewById(R.id.noapproval);
        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.mainTaskLayout);
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setColorSchemeColors(Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW);

        back = (ImageView) findViewById(R.id.back);

        LinearLayoutManager manager = new LinearLayoutManager(this);
        approvals_list.setLayoutManager(manager);
        approvals_list.setHasFixedSize(true);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        getApprovals();
    }

    private void getApprovals() {
        if (isOnline(this)) {
            Map<String, String> headers = new HashMap<String, String>();
            headers.put("x-email-id", CWIncturePreferences.getEmail());
            headers.put("x-access-token", CWIncturePreferences.getAccessToken());

            NetworkConnector connect = new NetworkConnector(this, NetworkConnector.TYPE_GET, CWUrls.GET_WORKITEM + "?" + "limit=" + limit + "&skip=" + skip, headers, null, this);
            connect.setLogin(true);
            if (connect.isAllowed()) {
                connect.execute();
            } else {
                Toast.makeText(this, getString(R.string.action_not_allowed), Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onRefresh() {

        getApprovals();
    }

    @Override
    public void processFinish(String output, int status_code, String url, int type) {

        if (url.contains(CWUrls.GET_WORKITEM) || url.contains(CWUrls.GET_WORKITEM_WITH_PAGINATION)) {

            DbAdapter.getDbAdapterInstance().delete(DbUtil.TASKS_TABLE_NAME);
            DbAdapter.getDbAdapterInstance().delete(DbUtil.TASKS_TABLE_NAME_SCROLL);
            DbUtil db = new DbUtil();
            db.addMember(output);
            if (offline) {
                list.clear();
                original_list.clear();
                offline = false;
            }

            String actualString = output;
            if (output.startsWith("null")) {
                actualString = output.substring(4);
            }
            parse(actualString);
        }


    }

    public void parse(String output) {
        try {
            if (list == null) {
                list = new ArrayList<>();
            }

            fmodel = new FeedsMainModel();
            dayslist = new ArrayList<>();
            taskslist = new ArrayList<>();
            CommentModel ccmodel = new CommentModel();

            JSONObject mainobject = new JSONObject(output);
            fmodel.status = mainobject.optString("status");
            fmodel.message = mainobject.optString("message");
            if (fmodel.status.equals("success")) {
                if (!pagination) {
                    list.clear();
                }
                JSONArray data = (JSONArray) mainobject.opt("data");
                if (data != null && data.length() > 0) {
                    for (int i = 0; i < data.length(); i++) {
                        try {
                            JSONObject dataobject = data.getJSONObject(i);
                            JSONObject cardObject = dataobject.getJSONObject("card");
                            FeedsDataModel model = new FeedsDataModel();
                            model.type = cardObject.optString("type");
                            model.subtype = cardObject.optString("subtype");
                            model.title = Util.capitalizeSentence(dataobject.optString("title"));
                            model.description = Util.capitalizeSentence(dataobject.optString("description"));
                            model.createdAt = dataobject.optString("createdAt");
                            model.Public = dataobject.optString("public");
                            model.id = dataobject.optString("_id");
                            model.lastModified = dataobject.optString("updatedAt");
                            model.status = dataobject.optString("status");
                            if (dataobject.optJSONObject("lastActivity") != null) {
                                JSONObject activity = dataobject.optJSONObject("lastActivity");
                                if (activity.length() > 0) {

                                    model.activity = "<b> <font color='#000000'>" + Util.capitalizeWords(activity.optString("user")) + "</font></b>" + " " +
                                            activity.optString("activity");

                                    model.time = activity.optString("time");
                                    model.action = activity.optString("action");
                                }
                            } else {
                                model.activity = dataobject.optString("lastActivity");
                            }

                            if (dataobject.optJSONObject("content") != null) {
                                JSONObject content = dataobject.optJSONObject("content");
                                if (model.type.equalsIgnoreCase("Performance Management System") && model.subtype.equalsIgnoreCase("Appraisal Template Approve")) {

                                    model.appraisal_id = content.optString("_id");
                                    model.name = content.optString("name");
                                    model.active = content.optBoolean("active");
                                    model.workflow = content.optString("workflow");

                                    JSONObject validity = content.optJSONObject("validity");
                                    model.validity_from = validity.optString("from");
                                    model.validity_to = validity.optString("to");


                                    model.description = content.optString("description");
                                    model.content_status = content.optString("status");


                                    JSONArray sections = content.optJSONArray("sections");
                                    if (sections != null && sections.length() > 0) {
                                        for (int s = 0; s < sections.length(); s++) {
                                            JSONObject sectionObject = sections.optJSONObject(s);
                                            model.sections_name = sectionObject.optString("name");
                                            model.sections_description = sectionObject.optString("description");
                                            model.sections_measurement = sectionObject.optString("measurement");
                                            model.sections_overallScore = sectionObject.optBoolean("overallScore");

                                            JSONArray criteria = sectionObject.optJSONArray("criteria");
                                            if (criteria != null && criteria.length() > 0) {
                                                for (int c = 0; c < criteria.length(); c++) {
                                                    JSONObject criteriaObject = criteria.optJSONObject(c);
                                                    model.criteria_name = criteriaObject.optString("name");
                                                    model.criteria_description = criteriaObject.optString("description");

                                                }
                                            }//end of criteria array
                                        }
                                    }//end of sections array

                                    JSONObject createdBy = content.optJSONObject("createdBy");
                                    model.appraisal_creatorid = createdBy.optString("_id");
                                    model.appraisal_creatordisplayname = createdBy.optString("displayName");
                                    model.appraisal_creatoravatarurl = createdBy.optString("avatar");
                                    model.appraisal_creatorDesignation = createdBy.optString("designation");
                                    model.appraisal_creatorphoneno = createdBy.optString("mobile");
                                    model.appraisal_creatoremail = createdBy.optString("email");
                                    model.appraisal_deleted = createdBy.optString("deleted");

                                    model.createdAt = content.optString("createdAt");
                                    model.updatedAt = content.optString("updatedAt");


                                } else if (model.type.equals("feeds")) {
                                    if (model.subtype.equals("newjoinee")) {
                                        JSONObject newUser = content.getJSONObject("user");
                                        model.avatarurl = newUser.optString("avatar");
                                        model.displayname = Util.capitalizeWords(newUser.optString("displayName"));
                                    } else if (model.subtype.equals("news")) {
                                        for (int j = 0; j < content.length(); j++) {
                                            model.setSource(content.optString("source"));

                                        }
                                    }

                                }
                            } else {
                                model.content = dataobject.optString("content");
                            }


                            likelist = new ArrayList<LikeModel>();
                            JSONArray likearray = (JSONArray) dataobject.opt("likes");
                            if (likearray != null && likearray.length() > 0) {
                                for (int l = 0; l < likearray.length(); l++) {
                                    LikeModel likemodel = new LikeModel();
                                    JSONObject likeobject = likearray.optJSONObject(l);
                                    likemodel.likeid = likeobject.optString("id");
                                    likelist.add(likemodel);
                                }
                            }
                            model.likes = likelist;
                            model.setLikescount(dataobject.optInt("likeCount"));
                            int isliked = dataobject.optInt("likedByUser");
                            if (isliked == 0)
                                model.setLiked(false);
                            else
                                model.setLiked(true);

                            commentlist = new ArrayList<CommentModel>();
                            attachlistwithcomments = new ArrayList<AttachmentModel>();
                            attachlist = new ArrayList<AttachmentModel>();
                            JSONObject commentobject = dataobject.optJSONObject("comments");
                            if (commentobject != null) {
                                CommentModel commentmodel = new CommentModel();
                                commentmodel.time = commentobject.optString("time");
                                commentmodel.id = commentobject.optString("id");
                                commentmodel.text = commentobject.optString("text");
                                commentmodel.body = commentobject.optString("text");
                                commentmodel.deleted = commentobject.optString("deleted");


                                JSONObject createdBy = commentobject.optJSONObject("commentedBy");
                                commentmodel.userid = createdBy.optString("id");
                                commentmodel.useremail = createdBy.optString("email");
                                commentmodel.userphoneno = createdBy.optString("phoneNo");
                                commentmodel.userdisplayname = Util.capitalizeWords(createdBy.optString("displayName"));
                                commentmodel.useravatarurl = createdBy.optString("avatar");


                                commentlist.add(commentmodel);

                                model.comments = commentlist;
                                model.attachmentwithcomments = attachlistwithcomments;
                                model.setCommentsCount(dataobject.optInt("commentsCount"));

                            }

                            attachlist = new ArrayList<AttachmentModel>();
                            JSONArray atachments_array = (JSONArray) dataobject.opt("attachments");
                            if (atachments_array != null && atachments_array.length() > 0) {
                                for (int a = 0; a < atachments_array.length(); a++) {
                                    AttachmentModel attachmodel = new AttachmentModel();
                                    JSONObject attachobject = atachments_array.optJSONObject(a);
                                    attachmodel.id = attachobject.optString("id");
                                    attachmodel.createdAt = attachobject.optString("createdAt");
                                    attachmodel.url = attachobject.optString("url");
                                    attachmodel.displayname = attachobject.optString("displayName");
                                    attachmodel.type = attachobject.optString("type");

                                    JSONObject user = attachobject.optJSONObject("user");
                                    attachmodel.userid = user.optString("_id");
                                    attachmodel.userdisplayname = user.optString("displayName");
                                    attachmodel.useravatarurl = user.optString("avatar");
                                    attachlist.add(attachmodel);
                                }
                            }

                            model.attachments = attachlist;

                            owner = new ArrayList<CommentModel>();
                            JSONArray assigned = (JSONArray) dataobject.opt("assignedTo");
                            if (assigned != null && assigned.length() > 0) {
                                for (int o = 0; o < assigned.length(); o++) {
                                    CommentModel citem = new CommentModel();
                                    JSONObject oobj = assigned.getJSONObject(o);
                                    citem.oemail = oobj.optString("email");
                                    citem.oid = oobj.optString("_id");
                                    citem.odisplayName = Util.capitalizeWords(oobj.optString("displayName"));
                                    citem.ostatus = oobj.optString("status");
                                    citem.oavatarUrl = oobj.optString("avatar");
                                    citem.ophoneNo = oobj.optString("phoneNo");
                                    owner.add(citem);
                                    ccmodel = citem;
                                }
                            }
                            model.owners = owner;
                            model.assigned = ccmodel;


                            JSONArray subscribers = (JSONArray) dataobject.opt("subscribers");
                            if (subscribers != null && subscribers.length() > 0) {
                                for (int sb = 0; sb < subscribers.length(); sb++) {
                                    CommentModel item = new CommentModel();
                                    JSONObject sobj = subscribers.getJSONObject(sb);
                                    item.semail = sobj.optString("email");
                                    item.sid = sobj.optString("_id");
                                    item.sdisplayName = Util.capitalizeWords(sobj.optString("displayName"));
                                    item.savatarUrl = sobj.optString("avatar");
                                    item.sphoneNo = sobj.optString("phoneNo");
                                    subscriber.add(item);
                                }
                            }
                            model.subscribers = subscriber;

                            if (dataobject.optJSONObject("createdBy") != null) {
                                JSONObject creator = dataobject.optJSONObject("createdBy");
                                model.creatorid = creator.optString("_id");
                                model.creatoremail = creator.optString("email");
                                model.creatorphoneno = creator.optString("phoneNo");
                                model.creatordisplayname = Util.capitalizeWords(creator.optString("displayName"));
                                model.creatoravatarurl = creator.optString("avatar");
                            } else if (!dataobject.optString("createdBy").isEmpty()) {
                                model.creatordisplayname = Util.capitalizeWords(dataobject.optString("createdBy"));
                            }


                            if (dataobject.optJSONObject("updatedBy") != null) {
                                JSONObject updator = dataobject.optJSONObject("updatedBy");
                                model.ucreatorid = updator.optString("_id");
                                model.ucreatoremail = updator.optString("email");
                                model.ucreatorphoneno = updator.optString("phoneNo");
                                model.ucreatordisplayname = updator.optString("displayName");
                                model.ucreatoravatarurl = updator.optString("avatar");
                            } else if (!dataobject.optString("updatedBy").isEmpty()) {
                                model.ucreatordisplayname = dataobject.optString("updatedBy");
                            }
                            list.add(model);
                        } catch (JSONException jEx) {
                            System.out.println(jEx.getLocalizedMessage());
                        }


                    }

                } else {
                    allItemsDownloaded = true;
                }
                setadapter(perspect);
            } else {
                Toast.makeText(Approvals_CSC.this, fmodel.message, Toast.LENGTH_SHORT).show();
            }
        } catch (Exception ex) {
            System.out.println(ex.getLocalizedMessage());
        }

    }


    @Override
    public void configurationUpdated(boolean configUpdated) {
        if (configUpdated) {
            setadapter(perspect);

        }

    }

    public void setadapter(String s) {
        if (approvals_list == null) {
            approvals_list = (RecyclerView) findViewById(R.id.tasklist);
            LinearLayoutManager manager = new LinearLayoutManager(this);
            approvals_list.setLayoutManager(manager);
            approvals_list.setHasFixedSize(true);
            //  setUpItemTouchHelper();

            //   setUpAnimationDecoratorHelper();

        }


        perspect = s;
        if (list.size() == 0) {
            adapter = new InboxAdapter(Approvals_CSC.this, list, "");
            adapter.setUndoOn(true);
            if (perspect.equals("large") || perspect.equals("null")) {
                approvals_list.setAdapter(adapter);
            } else {
//                approvals_list.setAdapter(miniadapater);
            }

            empty.setVisibility(View.VISIBLE);
        } else {
            empty.setVisibility(View.GONE);
            if (approvals_list.getAdapter() == null) {
                approvals_list.setBackgroundColor(Color.parseColor("#cecece"));
                adapter = new InboxAdapter(Approvals_CSC.this, list, "");
                adapter.setUndoOn(true);
                if (perspect.equals("large") || perspect.equals("null") || perspect.equals("")) {
                    approvals_list.setAdapter(adapter);
                }
            } else {

                if (perspect.equals("large") || perspect.equals("null") || perspect.equals("")) {
                    ((InboxAdapter) (approvals_list.getAdapter())).setPerspective("");
                } else {
                    //  ((InboxAdapter) (approvals_list.getAdapter())).setPerspective("mini");
                }

                ((InboxAdapter) (approvals_list.getAdapter())).notifyDataSetChanged();
            }

        }
        approvals_list.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                int visibleItemCount = approvals_list.getLayoutManager().getChildCount();
                int totalItemCount = approvals_list.getLayoutManager().getItemCount();
                int currentFirstVisibleItem = ((LinearLayoutManager) approvals_list.getLayoutManager()).findFirstVisibleItemPosition();

                int topRowVerticalPosition = (approvals_list == null || approvals_list.getLayoutManager().getChildCount() == 0) ? 0 : approvals_list.getChildAt(0).getTop();
                refreshLayout.setEnabled(currentFirstVisibleItem == 0 && topRowVerticalPosition >= 0);
                if (dy > 0) //check for scroll down
                {


                    int lastInScreen = currentFirstVisibleItem + visibleItemCount;

                    if (totalItemCount > 0) {
                        if (lastInScreen == totalItemCount - 1 && !pagination) {
                            if (original_list.size() % 10 == 0 && !allItemsDownloaded)
                                pagination(lastInScreen);
                        }
                    }
                }
            }

        });


        pagination = false;

    }


    public void pagination(int lastInScreen) {

        if (isOnline(Approvals_CSC.this)) {
            pagination = true;
            Map<String, String> headers = new HashMap<String, String>();
            headers.put("x-email-id", CWIncturePreferences.getEmail());
            headers.put("x-access-token", CWIncturePreferences.getAccessToken());

            try {
                headers.put("android-version", getPackageManager().getPackageInfo(getPackageName(), 0).versionName);
            } catch (PackageManager.NameNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }


            NetworkConnector connect = new NetworkConnector(Approvals_CSC.this, NetworkConnector.TYPE_GET, CWUrls.GET_WORKITEM_WITH_PAGINATION + limit + "&skip=" + lastInScreen, headers, null, this, true);
            connect.setLogin(true);
            if (connect.isAllowed()) {
                connect.execute();
            } else {
                Toast.makeText(Approvals_CSC.this, getString(R.string.action_not_allowed), Toast.LENGTH_SHORT).show();
            }
        }

    }
}
