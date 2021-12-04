package Fragments;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hrapps.CherryworkApplication;
import com.hrapps.DashboardActivity;
import com.hrapps.MainActivity;
import com.hrapps.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
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
import Utility.Constants;
import Utility.FloatingActionButton;
import Utility.MainActivityCustomFab;
import Utility.NetworkConnector;
import Utility.Util;
import adapters.InboxAdapter;

import static Utility.Util.isOnline;


/**
 * @author shridharjoshi
 */
public class InboxFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, AsyncResponse, Constants {
    private SwipeRefreshLayout refreshLayout;
    public static Boolean refreshInProgress = false;
    MainActivityCustomFab fabmain;
    FloatingActionButton feedfab, taskfab;


    ImageView perspective;
    String perspect = "";
    boolean allItemsDownloaded = false;
    String response = "";
    int click = 0;
    boolean mini = false;
    TabLayout tabLayout;
    FeedsMainModel fmodel = new FeedsMainModel();
    public ArrayList<FeedsDataModel> list = new ArrayList<>();
    ArrayList<CommentModel> subscriber = new ArrayList<CommentModel>();
    public ArrayList<FeedsDataModel> original_list = new ArrayList<>();
    public ArrayList<AttachmentModel> attachlist, attachlistwithcomments = new ArrayList<>();
    public ArrayList<LikeModel> likelist = new ArrayList<>();

    public ArrayList<CommentModel> commentlist = new ArrayList<>();
    ArrayList<CommentModel> owner = new ArrayList<>();
    InboxAdapter adapter;
    InboxAdapter miniadapater;
    MainActivity main;
    NavigationView navigationViewRight, navigationLeft;
    DrawerLayout drawerLayout;
    RecyclerView lv;

    int skip = 0;
    int limit = 10;
    boolean pagination = false;
    private MenuItem prevMenuItem;
    private MenuItem prevMenuItemLeft;
    RelativeLayout blur_back;
    boolean offline = true;
    private TextView empty;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        main = (MainActivity) getActivity();
        View v = LayoutInflater.from(getActivity()).inflate(R.layout.taskslistview, null);
        refreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.mainLayout);
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setColorSchemeColors(Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW);
        fabmain = (MainActivityCustomFab) v.findViewById(R.id.fab);
        fabmain.close(true);
        tabLayout = (TabLayout) getActivity().findViewById(R.id.tab_layout);
        perspective = (ImageView) getActivity().findViewById(R.id.perspective);
        navigationViewRight = (NavigationView) getActivity().findViewById(R.id.navigation_right);
        navigationLeft = (NavigationView) getActivity().findViewById(R.id.navigation_view);
        drawerLayout = (DrawerLayout) getActivity().findViewById(R.id.drawer_layout);
        blur_back = (RelativeLayout) v.findViewById(R.id.blur_back);
        empty = (TextView) v.findViewById(R.id.empty);
        blur_back.setClickable(true);
        fabmain.setBlurLayout(blur_back);

        blur_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fabmain.close(true);
            }
        });

        navClick();
        navLeftClick();
        CWIncturePreferences.LeftFilterTab0("");
        CWIncturePreferences.RightFilterTab0("");
        if (navigationViewRight.getMenu().size() > 0) {
            prevMenuItem = navigationViewRight.getMenu().getItem(0).getSubMenu().getItem(0);
        }
        if (navigationLeft.getMenu().size() > 0) {
            prevMenuItemLeft = navigationLeft.getMenu().getItem(0).getSubMenu().getItem(0);
        }

        taskfab = (FloatingActionButton) v.findViewById(R.id.menu_item);
        feedfab = (FloatingActionButton) v.findViewById(R.id.menu_item2);

        if (CWIncturePreferences.getPerspective().equalsIgnoreCase("mini")) {
            perspect = "mini";
            perspective.setImageResource(R.mipmap.ic_list_white_36dp);
            click = 1;
            mini = true;
        }

        refreshLayout.setRefreshing(false);

        perspective.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CWIncturePreferences.init(getActivity());
                if (tabLayout.getSelectedTabPosition() == 0) {
                    if (click == 0) {
                        perspective.setImageResource(R.mipmap.ic_list_white_36dp);
                        perspect = "mini";
                        setadapter(perspect);
                        refreshLayout.setRefreshing(false);
                        mini = true;
                        click = 1;
                        CWIncturePreferences.setPerspective("mini");
                    } else {
                        perspective.setImageResource(R.mipmap.ic_view_agenda_white_36dp);
                        perspect = "";
                        setadapter(perspect);
                        refreshLayout.setRefreshing(false);
                        mini = false;
                        CWIncturePreferences.setPerspective("large");
                        click = 0;
                    }
                }
            }

        });


        if (!Util.isConnectedFast(getActivity())) {
            perspective.setImageResource(R.mipmap.ic_list_white_36dp);
            perspect = "mini";
            mini = true;
            click = 1;
        }

        if (click == 0) {
            perspective.setImageResource(R.mipmap.ic_view_agenda_white_36dp);
        } else {
            perspective.setImageResource(R.mipmap.ic_list_white_36dp);
        }
        getalltasks();
        return v;

    }

    private void navLeftClick() {
        navigationLeft.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                if (menuItem.getItemId() != 99 && menuItem.getItemId() != 100 && menuItem.getItemId() != 101) {
                    if (prevMenuItemLeft != null) {
                        prevMenuItemLeft.setChecked(false);
                    }

                    prevMenuItemLeft = menuItem;
                    menuItem.setCheckable(true);
                    menuItem.setChecked(true);
                    CWIncturePreferences.LeftFilterTab0(menuItem.getTitle().toString().trim());
                }

                switch (menuItem.getItemId()) {
                    case 100:
                        if (Util.isOnline(getActivity())) {
                            drawerLayout.closeDrawers();
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {

                                    main.logout();
                                }
                            }, 200);

                        } else {
                            drawerLayout.closeDrawers();
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getActivity(), getString(R.string.no_internet), Toast.LENGTH_SHORT).show();

                                }
                            }, 200);
                        }
                        break;

                    case 101:
                        drawerLayout.closeDrawers();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent = new Intent(getActivity(), DashboardActivity.class);
                                startActivity(intent);
                            }
                        }, 200);

                        return true;
                    default:
                        break;
                }

                switch (menuItem.getTitle().toString().trim()) {
                    case MY_TASKS:
                        if (Util.isOnline(getActivity())) {
                            drawerLayout.closeDrawers();
                            callmyTasks();


                        } else {
                            drawerLayout.closeDrawers();
                            Toast.makeText(getActivity(), getString(R.string.offline_unavailable), Toast.LENGTH_SHORT).show();

                        }

                        break;
                    case SENT_ITEMS:
                        if (Util.isOnline(getActivity())) {
                            drawerLayout.closeDrawers();
                            callsentItems();

                        } else {
                            drawerLayout.closeDrawers();
                            Toast.makeText(getActivity(), getString(R.string.offline_unavailable), Toast.LENGTH_SHORT).show();
                        }
                        break;

                    case COMPLETED:
                        if (Util.isOnline(getActivity())) {
                            drawerLayout.closeDrawers();
                            callBPMTasks();


                        } else {
                            drawerLayout.closeDrawers();
                            Toast.makeText(getActivity(), getString(R.string.offline_unavailable), Toast.LENGTH_SHORT).show();

                        }

                        break;

                    case "All":
                        drawerLayout.closeDrawers();
                        list.clear();
                        original_list.clear();
                        getalltasks();
                        break;

                    default:
                        break;
                }
                return true;
            }
        });
    }


    private void navClick() {
        navigationViewRight.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                CWIncturePreferences.RightFilterTab0(menuItem.getTitle().toString().trim());
                if (prevMenuItem != null) {
                    prevMenuItem.setChecked(false);
                }

                prevMenuItem = menuItem;
                menuItem.setCheckable(true);
                menuItem.setChecked(true);
                if (menuItem.getItemId() == 10) {
                    if (Util.isOnline(getActivity())) {
                        drawerLayout.closeDrawers();
                        list.clear();
                        original_list.clear();
                        getalltasks();

                    } else {
                        drawerLayout.closeDrawers();
                        Toast.makeText(getActivity(), getString(R.string.offline_unavailable), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    if (Util.isOnline(getActivity())) {
                        drawerLayout.closeDrawers();
                        if (menuItem.getTitle().toString().contains("BPM"))
                            callBPMTasks();
                        else
                            callFilters(menuItem.getTitle().toString());


                    } else {
                        drawerLayout.closeDrawers();
                        Toast.makeText(getActivity(), getString(R.string.offline_unavailable), Toast.LENGTH_SHORT).show();

                    }
                }
                return true;
            }

        });
    }

    private void callmyTasks() {
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("x-email-id", CWIncturePreferences.getEmail());
        headers.put("x-access-token", CWIncturePreferences.getAccessToken());
        try {
            headers.put("android-version", getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0).versionName);
        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        NetworkConnector connect = new NetworkConnector(getActivity(), NetworkConnector.TYPE_GET, CWUrls.GET_WORKITEM + "?filter=myTasks&limit=10", headers, null, this);
        if (connect.isAllowed()) {
            list.clear();
            original_list.clear();
            connect.execute();
        } else {
            Toast.makeText(getActivity(), getString(R.string.action_not_allowed), Toast.LENGTH_SHORT).show();
        }
    }

    private void callBPMTasks() {
        String url = CWUrls.BASE_URL + "api/v1/bpmTasks";

        Map<String, String> headers = new HashMap<>();
        headers.put("x-email-id", CWIncturePreferences.getEmail());
        headers.put("x-access-token", CWIncturePreferences.getAccessToken());

        NetworkConnector connect = new NetworkConnector(getActivity(), NetworkConnector.TYPE_GET, url, headers, null, this);

        if (connect.isAllowed()) {
            list.clear();
            original_list.clear();
            connect.execute();
        } else {
            Toast.makeText(getActivity(), getString(R.string.action_not_allowed), Toast.LENGTH_SHORT).show();
        }
    }


    private void callsentItems() {
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("x-email-id", CWIncturePreferences.getEmail());
        headers.put("x-access-token", CWIncturePreferences.getAccessToken());
        try {
            headers.put("android-version", getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0).versionName);
        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        NetworkConnector connect = new NetworkConnector(getActivity(), NetworkConnector.TYPE_GET, CWUrls.GET_WORKITEM + "?filter=sentItems&limit=10", headers, null, this);
        if (connect.isAllowed()) {
            list.clear();
            original_list.clear();
            connect.execute();
        } else {
            Toast.makeText(getActivity(), getString(R.string.action_not_allowed), Toast.LENGTH_SHORT).show();
        }
    }


    private void callFilters(String filterName) {
        String filter_id = DbUtil.getFilterId(getActivity(), filterName);
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("x-email-id", CWIncturePreferences.getEmail());
        headers.put("x-access-token", CWIncturePreferences.getAccessToken());
        try {
            headers.put("android-version", getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0).versionName);
//            Log.d("TOK", getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0).versionName.toString());
//            Log.d("TOK", CWIncturePreferences.getAccessToken());
        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        NetworkConnector connect = new NetworkConnector(getActivity(), NetworkConnector.TYPE_GET, CWUrls.GET_WORKITEM + "?filter=" + URLEncoder.encode(filterName) + "&limit=10", headers, null, this);
        if (connect.isAllowed()) {
            connect.execute();
        } else {
            Toast.makeText(getActivity(), getString(R.string.action_not_allowed), Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onRefresh() {
        if (isOnline(getActivity())) {
            // list.clear();
            allItemsDownloaded = false;
            if (mini) {
                perspect = "mini";
                pagination = false;
                getalltasks();

            } else {
                perspect = "";
                pagination = false;
                getalltasks();
            }
            refreshInProgress = false;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    refreshLayout.setRefreshing(false);
                }
            }, 3000);

            getNotificationCount();
        } else {
            refreshLayout.setRefreshing(false);
            Toast.makeText(getActivity(), getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
        }
        if (refreshInProgress)
            return;
        refreshInProgress = false;

    }

    @Override
    public void onResume() {
        super.onResume();
/*
        if (click == 0) {
            perspective.setImageResource(R.mipmap.ic_view_agenda_white_36dp);
        } else {
            perspective.setImageResource(R.mipmap.ic_list_white_36dp);
        }
        if (!isOnline(getActivity())) {
            DbUtil db = new DbUtil();
            response = db.getResponseStructure();
            String actualString = response;
            if (response.startsWith("null")) {
                actualString = response.substring(4);
            }
            parse(actualString);

        }*/
    }

    @Override
    public void onStart() {
        super.onStart();
        fabmain = (MainActivityCustomFab) getActivity().findViewById(R.id.fab);
        fabmain.close(true);
        if (offline) {
            DbUtil db = new DbUtil();
            response = db.getResponseStructure();
            String actualString = response;
            if (response.startsWith("null")) {
                actualString = response.substring(4);
            }
            parse(actualString);
        }
    }


    public void getalltasks() {

        if (isOnline(getActivity())) {
            Map<String, String> headers = new HashMap<String, String>();
            headers.put("x-email-id", CWIncturePreferences.getEmail());
            headers.put("x-access-token", CWIncturePreferences.getAccessToken());
            try {
                headers.put("android-version", getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0).versionName);
            } catch (PackageManager.NameNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }


            NetworkConnector connect = new NetworkConnector(getActivity(), NetworkConnector.TYPE_GET, CWUrls.GET_WORKITEM + "?" + "limit=" + limit + "&skip=" + skip, headers, null, this);
            if (connect.isAllowed()) {
                connect.execute();
            } else {
                Toast.makeText(getActivity(), getString(R.string.action_not_allowed), Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void processFinish(String output, int status_code, String url, int type) {

        if (url.contains(CWUrls.NOTIFICATIONS_COUNT)) {
            String status = "";
            try {
                JSONObject object = new JSONObject(output);

                if (object.has("status")) {
                    status = object.optString("status");
                }

                if (status.equals("success")) {
                    int notif_count = object.optInt("data");
                    ((CherryworkApplication) getActivity().getApplicationContext()).notifiationAdded(notif_count);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


        } else {
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

    @Override
    public void configurationUpdated(boolean configUpdated) {
        if (configUpdated) {
            setadapter(perspect);

        }
    }

    public void setadapter(String s) {
        if (lv == null) {
            lv = (RecyclerView) getActivity().findViewById(R.id.tasklist);
            LinearLayoutManager manager = new LinearLayoutManager(getActivity());
            lv.setLayoutManager(manager);
            lv.setHasFixedSize(true);
            //  setUpItemTouchHelper();

            //   setUpAnimationDecoratorHelper();

        }


        perspect = s;
        if (list.size() == 0) {
            adapter = new InboxAdapter(getActivity(), list, "");
            adapter.setUndoOn(true);
            miniadapater = new InboxAdapter(getActivity(), list, "mini");
            miniadapater.setUndoOn(true);
            if (perspect.equals("large") || perspect.equals("null")) {
                lv.setAdapter(adapter);
            } else {
                lv.setAdapter(miniadapater);
            }

            empty.setVisibility(View.VISIBLE);
        } else {
            empty.setVisibility(View.GONE);
            if (lv.getAdapter() == null) {
                lv.setBackgroundColor(Color.parseColor("#cecece"));
                adapter = new InboxAdapter(getActivity(), list, "");
                miniadapater = new InboxAdapter(getActivity(), list, "mini");
                adapter.setUndoOn(true);
                miniadapater.setUndoOn(true);
                if (perspect.equals("large") || perspect.equals("null") || perspect.equals("")) {
                    lv.setAdapter(adapter);
                } else {
                    lv.setAdapter(miniadapater);
                }
            } else {

                if (perspect.equals("large") || perspect.equals("null") || perspect.equals("")) {
                    ((InboxAdapter) (lv.getAdapter())).setPerspective("");
                } else {
                    ((InboxAdapter) (lv.getAdapter())).setPerspective("mini");
                }

                ((InboxAdapter) (lv.getAdapter())).notifyDataSetChanged();
            }

        }
        lv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                int visibleItemCount = lv.getLayoutManager().getChildCount();
                int totalItemCount = lv.getLayoutManager().getItemCount();
                int currentFirstVisibleItem = ((LinearLayoutManager) lv.getLayoutManager()).findFirstVisibleItemPosition();

                int topRowVerticalPosition = (lv == null || lv.getLayoutManager().getChildCount() == 0) ? 0 : lv.getChildAt(0).getTop();
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

        if (isOnline(getActivity())) {
            pagination = true;
            Map<String, String> headers = new HashMap<String, String>();
            headers.put("x-email-id", CWIncturePreferences.getEmail());
            headers.put("x-access-token", CWIncturePreferences.getAccessToken());

            try {
                headers.put("android-version", getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0).versionName);
            } catch (PackageManager.NameNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            NetworkConnector connect = new NetworkConnector(getActivity(), NetworkConnector.TYPE_GET, CWUrls.GET_WORKITEM_WITH_PAGINATION + limit + "&skip=" + lastInScreen, headers, null, this, true);
            if (connect.isAllowed()) {
                connect.execute();
            } else {
                Toast.makeText(getActivity(), getString(R.string.action_not_allowed), Toast.LENGTH_SHORT).show();
            }
        }

    }


    public void parse(String output) {
        try {
            if (list == null) {
                list = new ArrayList<>();
            }


            fmodel = new FeedsMainModel();

            CommentModel ccmodel = new CommentModel();

            JSONObject mainobject = new JSONObject(output);
            fmodel.status = mainobject.optString("status");
            fmodel.message = mainobject.optString("message");
            if (fmodel.status.equals("success")) {
                if (!pagination) {
                    list.clear();
                    original_list.clear();
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
                                if (model.type.equalsIgnoreCase("Champion Score Card") && model.subtype.equalsIgnoreCase("Appraisal Template Approve")) {


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

                                    if (createdBy.has("role")) {
                                        JSONObject roleObject = createdBy.optJSONObject("role");

                                        model.appraisal_roleName = roleObject.optString("name");
                                        model.appraisal_roleRegion = roleObject.optString("region");
                                        model.appraisal_roleHrFunction = roleObject.optString("hrFunction");
                                    }


                                    model.createdAt = content.optString("createdAt");
                                    model.updatedAt = content.optString("updatedAt");


                                } else if (model.type.equalsIgnoreCase("Performance Management System") && model.subtype.equalsIgnoreCase("Appraisal Template Approve")) {


                                    model.appraisal_id = content.optString("_id");
                                    model.name = content.optString("name");

                                    JSONObject validity = content.optJSONObject("validity");
                                    model.validity_from = validity.optString("from");
                                    model.validity_to = validity.optString("to");

                                    model.description = content.optString("description");
                                    model.content_status = content.optString("status");

                                    JSONObject createdBy = content.optJSONObject("createdBy");
                                    model.appraisal_creatorid = createdBy.optString("_id");
                                    model.appraisal_creatordisplayname = createdBy.optString("displayName");
                                    model.appraisal_creatoravatarurl = createdBy.optString("avatar");
                                    model.appraisal_creatorDesignation = createdBy.optString("designation");
                                    model.appraisal_creatorphoneno = createdBy.optString("mobile");
                                    model.appraisal_creatoremail = createdBy.optString("email");
                                    model.appraisal_deleted = createdBy.optString("deleted");

                                    if (createdBy.has("role")) {
                                        JSONObject roleObject = createdBy.optJSONObject("role");

                                        model.appraisal_roleName = roleObject.optString("name");
                                        model.appraisal_roleRegion = roleObject.optString("region");
                                        model.appraisal_roleHrFunction = roleObject.optString("hrFunction");
                                    }

                                    model.createdAt = content.optString("createdAt");
                                    model.updatedAt = content.optString("updatedAt");


                                } else if (model.type.equalsIgnoreCase("Performance Management System") && model.subtype.equalsIgnoreCase("Goal Setting")) {

                                    model.appraisal_id = content.optString("_id");
                                    model.name = content.optString("name");

                                    model.stage = content.optString("currentStage");

                                    JSONArray stages_array = content.optJSONArray("stages");
                                    if (stages_array != null) {
                                        JSONObject stages_object = stages_array.optJSONObject(1);
                                        model.dueDate = stages_object.optString("dueDate");
                                    }


                                    JSONObject appraisee = content.optJSONObject("appraisee");
                                    model.appraisee_id = appraisee.optString("_id");
                                    model.appraisee_displayname = appraisee.optString("displayName");
                                    model.appraisee_avatar = appraisee.optString("avatar");
                                    model.appraisee_designation = appraisee.optString("designation");
                                    model.appraisee_phoneno = appraisee.optString("mobile");
                                    model.appraisee_email = appraisee.optString("email");

                                } else if (model.type.equalsIgnoreCase("Recruitment") && model.subtype.equalsIgnoreCase("requisition")) {

                                    model.recruitment_position_requested = content.optString("positionName");

                                    model.requestId = content.optString("_id");

                                    if (content.has("createdBy")) {
                                        JSONObject created_by = content.optJSONObject("createdBy");
                                        if (created_by != null) {
                                            model.recruitment_req_creatorid = created_by.optString("_id");
                                            model.recruitment_req_creatordisplayname = created_by.optString("displayName");
                                            model.recruitment_req_creatoravatarurl = created_by.optString("avatar");

                                        }
                                    }


                                } else if (model.type.equalsIgnoreCase("Recruitment") && model.subtype.equalsIgnoreCase("jobApplicationException")) {

                                    model.recruitment_position_requested = content.optString("positionName");

                                    model.requestId = content.optString("requisitionId");

                                    if (content.has("createdBy")) {
                                        JSONObject created_by = content.optJSONObject("createdBy");
                                        if (created_by != null) {
                                            model.recruitment_req_creatorid = created_by.optString("_id");
                                            model.recruitment_req_creatordisplayname = created_by.optString("displayName");
                                            model.recruitment_req_creatoravatarurl = created_by.optString("avatar");

                                        }
                                    }

                                } else if (model.type.equalsIgnoreCase("Recruitment") && model.subtype.equalsIgnoreCase("offerRollout")) {

                                    model.recruitment_position_requested = content.optString("positionName");

                                    model.requestId = content.optString("requisitionId");

                                    if (content.has("createdBy")) {
                                        JSONObject created_by = content.optJSONObject("createdBy");
                                        if (created_by != null) {
                                            model.recruitment_req_creatorid = created_by.optString("_id");
                                            model.recruitment_req_creatordisplayname = created_by.optString("displayName");
                                            model.recruitment_req_creatoravatarurl = created_by.optString("avatar");

                                        } else {

                                        }
                                    }
                                } else if (model.type.equalsIgnoreCase("Recruitment") && model.subtype.equalsIgnoreCase("updateRequisition")) {

                                    model.recruitment_position_requested = content.optString("positionName");

                                    model.requestId = content.optString("requisitionId");

                                    if (content.has("createdBy")) {
                                        JSONObject created_by = content.optJSONObject("createdBy");
                                        if (created_by != null) {
                                            model.recruitment_req_creatorid = created_by.optString("_id");
                                            model.recruitment_req_creatordisplayname = created_by.optString("displayName");
                                            model.recruitment_req_creatoravatarurl = created_by.optString("avatar");

                                        }

                                    }
                                }

                                if (content.has("candidate")) {
                                    JSONObject candidate_object = content.optJSONObject("candidate");
                                    model.candidate_fname = candidate_object.optString("firstName");
                                    model.candidate_lname = candidate_object.optString("lastName");
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
                            } else if (dataobject.optJSONArray("content") != null) {

                                JSONArray doContent = dataobject.optJSONArray("content");
//
                                if (model.type.equalsIgnoreCase("Curriculum Approval Template") && model.subtype.equalsIgnoreCase("Curriculum Approval Template")) {
                                    //LMS

                                    for (int cx = 0; cx < doContent.length(); cx++) {

                                        JSONObject contentObj = doContent.getJSONObject(cx);

                                        model.curriculum_id = contentObj.optString("curriculumId");
                                        model.curriculumName = contentObj.optString("curriculumName");
                                        model.description = contentObj.optString("description");
                                        model.preRequisite = contentObj.optString("preRequisite");
                                        model.mandatoryFunctions = contentObj.optJSONArray("mandatoryFunctions");
                                        model.recommendedFunctions = contentObj.optJSONArray("recommendedFunctions");
                                        model.complianceFor = contentObj.optJSONArray("complianceFor");
                                        model.managerApproval = contentObj.optString("managerApproval");
                                        model.workFlowApproval = contentObj.optString("workFlowApproval");
                                        model.workFlowApprovalUnit = contentObj.optString("workFlowApprovalUnit");
                                        model.costPerEmployee = contentObj.optString("costPerEmployee");
                                        model.costPerEmpUnit = contentObj.optString("costPerEmpUnit");
                                        model.certificateTemplate = contentObj.optString("certificateTemplate");
                                        model.certificateValidity = contentObj.optString("certificateValidity");
                                        model.validityInMonths = contentObj.optString("validityInMonths");
                                        model.certificationType = contentObj.optString("certificationType");
                                        model.evaluationForm = contentObj.optString("evaluationForm");
                                        model.feedbackForm = contentObj.optString("feedbackForm");
                                        model.emailTemplate = contentObj.optString("emailTemplate");
                                        model.manualMailTrigger = contentObj.optString("manualMailTrigger");
                                        model.courses = contentObj.optJSONArray("courses");
                                        model.createdBy = contentObj.optString("createdBy");
                                        model.createdAt = contentObj.optString("createdAt");
                                        model.deleted = contentObj.optString("deleted");
                                        model.curriculumId = contentObj.optString("curriculumId");
                                        model.totalTrainingHours = contentObj.optString("totalTrainingHours");
                                        model.coverPic = contentObj.optString("coverPic");
                                        model.updatedBy = contentObj.optString("updatedBy");
                                        model.updatedAt = contentObj.optString("updatedAt");

                                        //For courses
                                        JSONArray courseArray = model.courses;
                                        for (int jx = 0; jx < courseArray.length(); jx++) {

                                            JSONObject courseObj = courseArray.getJSONObject(jx);

                                            model.courseTitle = courseObj.optString("courseTitle");
                                            model.courseId = courseObj.optString("courseId");
                                            model.courses_id = courseObj.optString("_id");
                                            model.hours = courseObj.optString("hours");
                                            model.mode = courseObj.optString("mode");
                                            model.courseType = courseObj.optString("courseType");
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


                                JSONArray attachmentsarray = (JSONArray) commentobject.opt("attachments");
                                if (attachmentsarray != null && attachmentsarray.length() > 0) {
                                    for (int attachment = 0; attachment < attachmentsarray.length(); attachment++) {
                                        AttachmentModel attachmentmodel = new AttachmentModel();

                                        JSONObject attachmentobject = attachmentsarray.optJSONObject(attachment);
                                        attachmentmodel.time = attachmentobject.optString("time");
                                        attachmentmodel.url = attachmentobject.optString("url");
                                        attachmentmodel.displayname = attachmentobject.optString("displayName");
                                        attachmentmodel.type = attachmentobject.optString("type");
                                        attachmentmodel.id = attachmentobject.optString("id");
                                        attachmentmodel.createdAt = attachmentobject.optString("createdAt");
                                        attachlistwithcomments.add(attachmentmodel);
                                    }
                                }
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


                                    if (oobj.has("role")) {
                                        JSONObject role_object = oobj.optJSONObject("role");

                                        if (role_object != null) {
                                            citem.oRoleName = role_object.optString("name");
                                            citem.oRoleRegion = role_object.optString("region");
                                            citem.oHrFunction = role_object.optString("hrFunction");
                                        }
                                    }


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
                            original_list.add(model);
                        } catch (JSONException jEx) {
                            System.out.println(jEx.getLocalizedMessage());
                        }


                    }

                } else {
                    allItemsDownloaded = true;
                }
                setadapter(perspect);
            } else {
                Toast.makeText(getActivity(), fmodel.message, Toast.LENGTH_SHORT).show();
            }
        } catch (Exception ex) {
            System.out.println(ex.getLocalizedMessage());
        }

    }

    public void getNotificationCount() {
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("x-access-token", CWIncturePreferences.getAccessToken());
        headers.put("x-email-id", CWIncturePreferences.getEmail());
        try {
            headers.put("android-version", getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0).versionName);
        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        NetworkConnector connect = new NetworkConnector(getActivity(), NetworkConnector.TYPE_GET, CWUrls.NOTIFICATIONS_COUNT, headers, null, this);
        if (connect.isAllowed()) {
            connect.execute();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //   super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == getActivity().RESULT_OK && requestCode == 240) {
            if (data != null) {
                FeedsDataModel workItem = (FeedsDataModel) data.getSerializableExtra("WORKITEM");
                for (int item = 0; item < list.size(); item++) {
                    if (list.get(item).id.equals(workItem.id)) {
                        list.remove(item);
                        list.add(item, workItem);
                        break;
                    }
                }
                ((InboxAdapter) (lv.getAdapter())).notifyDataSetChanged();
            }
        }
    }


    private void setUpItemTouchHelper() {

        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {

            // we want to cache these and not allocate anything repeatedly in the onChildDraw method
            Drawable background;
            Drawable xMark;
            int xMarkMargin;
            boolean initiated;

            private void init() {
                background = new ColorDrawable(Color.RED);
                xMark = ContextCompat.getDrawable(getActivity(), R.mipmap.ic_action_content_clear);
                xMark.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
                xMarkMargin = 10;
                initiated = true;
            }

            // not important, we don't want drag & drop
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public int getSwipeDirs(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                int position = viewHolder.getAdapterPosition();
                InboxAdapter testAdapter = (InboxAdapter) recyclerView.getAdapter();
                if (testAdapter.isUndoOn() && testAdapter.isPendingRemoval(position)) {
                    return 0;
                }
                return super.getSwipeDirs(recyclerView, viewHolder);
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                int swipedPosition = viewHolder.getAdapterPosition();
                InboxAdapter adapter = (InboxAdapter) lv.getAdapter();
                boolean undoOn = adapter.isUndoOn();
                if (undoOn) {
                    adapter.pendingRemoval(swipedPosition);
                } else {
                    adapter.remove(swipedPosition);
                }


            }

       /*     @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                View itemView = viewHolder.itemView;

                // not sure why, but this method get's called for viewholder that are already swiped away
                if (viewHolder.getAdapterPosition() == -1) {
                    // not interested in those
                    return;
                }

                if (!initiated) {
                    init();
                }

                // draw red background
                background.setBounds(itemView.getRight() + (int) dX, itemView.getTop(), itemView.getRight(), itemView.getBottom());
                background.draw(c);

                // draw x mark
                int itemHeight = itemView.getBottom() - itemView.getTop();
                int intrinsicWidth = xMark.getIntrinsicWidth();
                int intrinsicHeight = xMark.getIntrinsicWidth();

                int xMarkLeft = itemView.getRight() - xMarkMargin - intrinsicWidth;
                int xMarkRight = itemView.getRight() - xMarkMargin;
                int xMarkTop = itemView.getTop() + (itemHeight - intrinsicHeight)/2;
                int xMarkBottom = xMarkTop + intrinsicHeight;
                xMark.setBounds(xMarkLeft, xMarkTop, xMarkRight, xMarkBottom);

                xMark.draw(c);

                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
*/
        };
        ItemTouchHelper mItemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        mItemTouchHelper.attachToRecyclerView(lv);


    }

    /**
     * We're gonna setup another ItemDecorator that will draw the red background in the empty space while the items are animating to thier new positions
     * after an item is removed.
     */
    private void setUpAnimationDecoratorHelper() {
        lv.addItemDecoration(new RecyclerView.ItemDecoration() {

            // we want to cache this and not allocate anything repeatedly in the onDraw method
            Drawable background;
            boolean initiated;

            private void init() {
                background = new ColorDrawable(Color.RED);
                initiated = true;
            }

            @Override
            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {

                if (!initiated) {
                    init();
                }

                // only if animation is in progress
                if (parent.getItemAnimator().isRunning()) {

                    // some items might be animating down and some items might be animating up to close the gap left by the removed item
                    // this is not exclusive, both movement can be happening at the same time
                    // to reproduce this leave just enough items so the first one and the last one would be just a little off screen
                    // then remove one from the middle

                    // find first child with translationY > 0
                    // and last one with translationY < 0
                    // we're after a rect that is not covered in recycler-view views at this point in time
                    View lastViewComingDown = null;
                    View firstViewComingUp = null;

                    // this is fixed
                    int left = 0;
                    int right = parent.getWidth();

                    // this we need to find out
                    int top = 0;
                    int bottom = 0;

                    // find relevant translating views
                    int childCount = parent.getLayoutManager().getChildCount();
                    for (int i = 0; i < childCount; i++) {
                        View child = parent.getLayoutManager().getChildAt(i);
                        if (child.getTranslationY() < 0) {
                            // view is coming down
                            lastViewComingDown = child;
                        } else if (child.getTranslationY() > 0) {
                            // view is coming up
                            if (firstViewComingUp == null) {
                                firstViewComingUp = child;
                            }
                        }
                    }

                    if (lastViewComingDown != null && firstViewComingUp != null) {
                        // views are coming down AND going up to fill the void
                        top = lastViewComingDown.getBottom() + (int) lastViewComingDown.getTranslationY();
                        bottom = firstViewComingUp.getTop() + (int) firstViewComingUp.getTranslationY();
                    } else if (lastViewComingDown != null) {
                        // views are going down to fill the void
                        top = lastViewComingDown.getBottom() + (int) lastViewComingDown.getTranslationY();
                        bottom = lastViewComingDown.getBottom();
                    } else if (firstViewComingUp != null) {
                        // views are coming up to fill the void
                        top = firstViewComingUp.getTop();
                        bottom = firstViewComingUp.getTop() + (int) firstViewComingUp.getTranslationY();
                    }

                    background.setBounds(left, top, right, bottom);
                    background.draw(c);

                }
                super.onDraw(c, parent, state);
            }

        });
    }

}
