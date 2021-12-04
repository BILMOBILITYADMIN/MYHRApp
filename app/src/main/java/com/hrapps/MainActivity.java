package com.hrapps;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HeaderViewListAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.guardanis.applock.locking.LockingHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import DB.DbUtil;
import Model.ConfigModel;
import Model.ContactsModel;
import Utility.AsyncResponse;
import Utility.CWIncturePreferences;
import Utility.CWUrls;
import Utility.CustomViewPager;
import Utility.NetworkConnector;
import Utility.ProfileImageView;
import Utility.RecyclerItemClickListener;
import Utility.Util;
import adapters.BasicSearchAdapter;
import adapters.PagerAdapter;


/**
 * Created by Mallan
 */
public class MainActivity extends BasicActivity implements AsyncResponse, SwipeRefreshLayout.OnRefreshListener {

    private TabLayout tabLayout;
    private CustomViewPager viewPager;
    private EditText search;
    private SubMenu subMenu;
    Button logoff;

    private CoordinatorLayout coordinatorLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private ProfileImageView img;
    private TextView name, email, people, logout, dashboard, badgeCount;
    private ImageView back;
    private NavigationView navigationView, navigationView_right;
    private String tab_position;
    String pos = "";
    private DrawerLayout drawerLayout;
    RelativeLayout.LayoutParams layoutParams;
    int search_on; //Check to change the tool bar
    ImageView settings, cancel_search, profileBg, filter, notify, perspective, menu;
    RelativeLayout tool_bar;
    TextView empty,tv_app_version;
    RecyclerView search_list;
    RelativeLayout search_layout;
    ArrayList<ConfigModel> tabs = new ArrayList<>();
    ArrayList<ConfigModel> navigation = new ArrayList<>();
    ArrayList<ConfigModel> filters = new ArrayList<>();
    ArrayList<ContactsModel> searched_user_list = new ArrayList<ContactsModel>();

    DbUtil db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.my_primary_dark));
        }

        Intent intent = getIntent();
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        img = (ProfileImageView) findViewById(R.id.drawer_header_profile_pic);
        profileBg = (ImageView) findViewById(R.id.backgroundimage1);
        settings = (ImageView) findViewById(R.id.settings);
        back = (ImageView) findViewById(R.id.back);
        name = (TextView) findViewById(R.id.drawer_header_profile_name);
        email = (TextView) findViewById(R.id.drawer_header_profile_email);
        people = (TextView) findViewById(R.id.people);
        logout = (TextView) findViewById(R.id.logout);
        logoff = (Button) findViewById(R.id.logout1);
        dashboard = (TextView) findViewById(R.id.dashboard);
        people.setOnClickListener(new onClickListener(0));
        logout.setOnClickListener(new onClickListener(0));
        dashboard.setVisibility(View.GONE);
        dashboard.setOnClickListener(new onClickListener(0));
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.base_frame);
        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView_right = (NavigationView) findViewById(R.id.navigation_right);
        layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        menu = (ImageView) findViewById(R.id.menu);
        filter = (ImageView) findViewById(R.id.filter);
        notify = (ImageView) findViewById(R.id.notify);
        badgeCount = (TextView) findViewById(R.id.notify_count);
        tv_app_version = (TextView)findViewById(R.id.tv_version);


        perspective = (ImageView) findViewById(R.id.perspective);
        search = (EditText) findViewById(R.id.search1);
        tool_bar = (RelativeLayout) findViewById(R.id.tool_bar);
        cancel_search = (ImageView) findViewById(R.id.cancel_search);

        search_list = (RecyclerView) findViewById(R.id.listView1);
        empty = (TextView) findViewById(R.id.empty);
        search_layout = (RelativeLayout) findViewById(R.id.search_layout);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        search_list.setHasFixedSize(true);
        search_list.setNestedScrollingEnabled(false);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        search_list.setLayoutManager(llm);

        try {
            tv_app_version.setText("App Version " + getPackageManager().getPackageInfo(getPackageName(), 0).versionName);
        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        ((CherryworkApplication) getApplicationContext()).setNotificationCountView(badgeCount);
        getNotificationCount();
        search.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    {
                        search.setBackgroundResource(R.drawable.frame_2);
                        search.setTextColor(getResources().getColor(R.color.black));
                        menu.setImageResource(R.mipmap.icon_back_red);
                        filter.setVisibility(View.GONE);
                        cancel_search.setVisibility(View.VISIBLE);
                        perspective.setVisibility(View.GONE);
                        notify.setVisibility(View.GONE);
                        search_layout.setVisibility(View.VISIBLE);
                        badgeCount.setVisibility(View.GONE);

                        search_on = 1;


                        TextWatcher SearchTw = new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                            }

                            @Override
                            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                            }

                            @Override
                            public void afterTextChanged(Editable s) {

                                searched_user_list.clear();

                                if (s.toString().equals("")) {
                                    if (search_list.getAdapter() != null)
                                        search_list.getAdapter().notifyDataSetChanged();
                                    empty.setVisibility(View.GONE);

                                } else {


                                    if (Util.isOnline(MainActivity.this)) {
                                        String url = CWUrls.GROUPS_SEARCH + s.toString().trim();

                                        Map<String, String> headers = new HashMap<>();
                                        headers.put("x-email-id", CWIncturePreferences.getEmail());
                                        headers.put("x-access-token", CWIncturePreferences.getAccessToken());
                                        try {
                                            headers.put("android-version", getPackageManager().getPackageInfo(getPackageName(), 0).versionName);
                                        } catch (PackageManager.NameNotFoundException e) {
                                            // TODO Auto-generated catch block
                                            e.printStackTrace();
                                        }

                                        //  if (s.toString().trim().length() >= 3 && (!s.toString().contains(" "))) {

                                        NetworkConnector connect = new NetworkConnector(MainActivity.this, NetworkConnector.TYPE_GET, url, headers, null, MainActivity.this);
                                        if (connect.isAllowed()) {
                                            connect.execute();
                                        } else {
                                            Toast.makeText(MainActivity.this, getString(R.string.action_not_allowed), Toast.LENGTH_SHORT).show();
                                        }
//                                        } else {
//                                            if (search_list.getAdapter() != null)
//                                                search_list.getAdapter().notifyDataSetChanged();
//                                        }
                                    } else {
                                        Toast.makeText(MainActivity.this, getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }

                        };

                        search.addTextChangedListener(SearchTw);

                    }
                } else {
                    search.setBackgroundResource(R.drawable.frame);
                    search.setTextColor(getResources().getColor(R.color.background_color));
                    cancel_search.setVisibility(View.GONE);
                    filter.setVisibility(View.VISIBLE);
                }

            }
        });

        search_list.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(search.getWindowToken(), 0);
            }
        });


        search_list.addOnItemTouchListener(new RecyclerItemClickListener(MainActivity.this, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                        Intent intent = new Intent(MainActivity.this, UserProfileActivity.class);
                        intent.putExtra("userid", searched_user_list.get(position).userId);
                        intent.putExtra("firstname", searched_user_list.get(position).firstname);
                        intent.putExtra("lastname", searched_user_list.get(position).lastname);
                        startActivity(intent);

                    }
                })
        );


        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(Gravity.RIGHT);
            }
        });

        cancel_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(search.getWindowToken(), 0);
                menu.setImageResource(R.mipmap.menu_red);
                tabLayout.setVisibility(View.VISIBLE);
                search_layout.setVisibility(View.GONE);
                search.setText("");
                search.clearFocus();
                tool_bar.requestFocus();


                if (tabLayout.getSelectedTabPosition() == 0) {

                    perspective.setVisibility(View.GONE);
                    filter.setVisibility(View.VISIBLE);
                    notify.setVisibility(View.VISIBLE);

                } else if (tabLayout.getSelectedTabPosition() == 1) {

                    perspective.setVisibility(View.GONE);
                    filter.setVisibility(View.GONE);
                    notify.setVisibility(View.VISIBLE);
                } else if (tabLayout.getSelectedTabPosition() == 2) {
                    perspective.setVisibility(View.GONE);
                    filter.setVisibility(View.GONE);

                    notify.setVisibility(View.VISIBLE);
                }

            }
        });

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (search_on == 0) {
                    drawerLayout.openDrawer(Gravity.LEFT);
                } else {
                    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(search.getWindowToken(), 0);
                    menu.setImageResource(R.mipmap.menu_red);
                    filter.setImageResource(R.mipmap.ic_action_content_filter_list_red);
                    tabLayout.setVisibility(View.VISIBLE);
                    notify.setVisibility(View.VISIBLE);
                    search_layout.setVisibility(View.GONE);
                    search.setText("");
                    search_on = 0;
                    search.clearFocus();
                    tool_bar.requestFocus();

                    if (tabLayout.getSelectedTabPosition() == 0) {

                        perspective.setVisibility(View.GONE);
                        filter.setVisibility(View.VISIBLE);

                    } else if (tabLayout.getSelectedTabPosition() == 1) {

                        perspective.setVisibility(View.GONE);
                        filter.setVisibility(View.GONE);
                    } else if (tabLayout.getSelectedTabPosition() == 2) {
                        perspective.setVisibility(View.GONE);
                        filter.setVisibility(View.GONE);

                    }

                }
            }
        });

        notify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, TimeSheetNotifications.class);
                startActivity(intent);
            }
        });

        logoff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Util.isOnline(MainActivity.this)) {
                    drawerLayout.closeDrawers();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            logout();
                        }
                    }, 200);

                } else {
                    drawerLayout.closeDrawers();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Snackbar.make(coordinatorLayout, "No Internet Connection,Try Later", Snackbar.LENGTH_SHORT).show();

                        }
                    }, 200);
                }
            }
        });

        tab_position = intent.getStringExtra("Position");

        setupTabView();

        setupDrawerLayout();

        setupViewPager();

        setupNav();

        moreInfo();

        defaultFilterandNavigation();


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.closeDrawers();
            }
        });

        if (!Util.isOnline(this)) {
        }
    }


    /**
     * Display filters and side navigation items based on configuration.
     */

    private void defaultFilterandNavigation() {

        Log.d("email;=", "" + CWIncturePreferences.getEmail());
        Log.d("rolee;=", "" + CWIncturePreferences.getRole());

        ArrayList<ConfigModel> nav_items = db.getNavigation(MainActivity.this);

        if (nav_items != null) {
            if (nav_items.size() > 0) {
                final Menu menu = navigationView.getMenu();
                final SubMenu subMenu = menu.addSubMenu(nav_items.get(0).displayName.toUpperCase());
                subMenu.add(Menu.NONE, 10, Menu.NONE, "    " + "All").setCheckable(true).setChecked(true);
                subMenu.findItem(10).setIcon(R.mipmap.approveall_red);
            }
        }

        for (int i = 0, count = navigationView.getChildCount(); i < count; i++) {
            final View child = navigationView.getChildAt(i);
            if (child != null && child instanceof ListView) {
                final ListView menuView = (ListView) child;
                final HeaderViewListAdapter adapter = (HeaderViewListAdapter) menuView.getAdapter();
                final BaseAdapter wrapped = (BaseAdapter) adapter.getWrappedAdapter();
                wrapped.notifyDataSetChanged();
            }
        }

        Menu menuNavRight = navigationView_right.getMenu();
        final SubMenu sub = menuNavRight.addSubMenu("FILTERS");

        sub.add(Menu.NONE, 10, Menu.NONE, "All Workitems").setCheckable(true).setChecked(true);
        ArrayList<ConfigModel> filter_items = db.getTypes(MainActivity.this);
        try {
            ArrayList<ConfigModel> filter_options = new ArrayList<>();

            for (int i = 0; i < filter_items.size(); i++) {
                ConfigModel filter = new ConfigModel();
                filter.id = filter_items.get(i).id;
                filter.displayName = filter_items.get(i).displayName;
                filter_options.add(filter);
                sub.add(Menu.NONE, i, Menu.NONE, (filter_items.get(i)).id);
            }


            // ArrayList<ConfigModel> filter_options = new ArrayList<>();
            if (filter_items.size() > 0) {
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED, Gravity.RIGHT);

                //   JSONArray options = new JSONArray(filter_items.get(0).options);
                //  if (options.length() > 0) {
//                    for (int o = 0; o < filter_items.length(); o++) {
//                        JSONObject json_obj = options.optJSONObject(o);
//                        ConfigModel filter = new ConfigModel();
//                        filter.id = Util.capitalizeSentence(json_obj.optString("id"));
//                        filter.icon = json_obj.optString("icon");
//                        filter.displayName = Util.capitalizeSentence(json_obj.optString("displayName"));
//                        filter_options.add(filter);
//
//                        sub.add(Menu.NONE, o, Menu.NONE, (filter.id));
//
//
//                    }
                // }
//            else {
//                    drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, Gravity.RIGHT);
//
//                    filter.setVisibility(View.GONE);
//                }
            } else {
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, Gravity.RIGHT);
                filter.setVisibility(View.GONE);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        for (int i = 0, count = navigationView_right.getChildCount(); i < count; i++) {
            final View child = navigationView_right.getChildAt(i);
            if (child != null && child instanceof ListView) {
                final ListView menuView = (ListView) child;
                final HeaderViewListAdapter adapter = (HeaderViewListAdapter) menuView.getAdapter();
                final BaseAdapter wrapped = (BaseAdapter) adapter.getWrappedAdapter();
                wrapped.notifyDataSetChanged();
            }
        }

    }

    /**
     * Navigation item clicks for people and logout
     */
    private void setupNav() {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                switch (menuItem.getItemId()) {
                    case 100:
                        if (Util.isOnline(MainActivity.this)) {
                            drawerLayout.closeDrawers();
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    logout();
                                }
                            }, 200);

                        } else {
                            drawerLayout.closeDrawers();
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Snackbar.make(coordinatorLayout, "No Internet Connection,Try Later", Snackbar.LENGTH_SHORT).show();

                                }
                            }, 200);
                        }
                        return true;
                    case 101:
                        drawerLayout.closeDrawers();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent = new Intent(MainActivity.this, DashboardActivity.class);
                                startActivity(intent);
                            }
                        }, 200);

                        return true;
                    default:
                        return true;
                }
            }
        });
    }

    /**
     * Show confirmation alert for logout
     */
    public void logout() {
        LayoutInflater li = LayoutInflater.from(this);
        final View promptsView = li.inflate(R.layout.logout_dialog, null);

        drawerLayout.closeDrawers();
        final AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
        dialog.setView(promptsView);

        TextView confirm_logout = (TextView) promptsView.findViewById(R.id.confirm_logout);
        TextView cancel_logout = (TextView) promptsView.findViewById(R.id.cancel_logout);

        dialog.setCancelable(true);

        final AlertDialog alert = dialog.create();
        alert.setCanceledOnTouchOutside(true);
        alert.show();


        cancel_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                alert.dismiss();
            }
        });

        confirm_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alert.dismiss();
                logoutAsync();
            }
        });


    }

    /**
     * Async call for logout
     */

    private void logoutAsync() {
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("x-access-token", CWIncturePreferences.getAccessToken());
        headers.put("x-email-id", CWIncturePreferences.getEmail());
        try {
            headers.put("android-version", getPackageManager().getPackageInfo(getPackageName(), 0).versionName);
        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        NetworkConnector connect = new NetworkConnector(MainActivity.this, NetworkConnector.TYPE_POST, CWUrls.LOG_OUT, headers, null, MainActivity.this);
        if (connect.isAllowed()) {
            connect.execute();
        } else {
            Toast.makeText(MainActivity.this, getString(R.string.action_not_allowed), Toast.LENGTH_SHORT).show();
        }

    }


    /**
     * Set up and display tabs based on configuration of tabs saved in DB
     */

    private void setupTabView() {

        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        db = new DbUtil();
        tabs = db.getTabs(MainActivity.this);
        if (tabs != null) {
            if (tabs.size() > 0) {
                tabLayout.addTab(tabLayout.newTab().setText(tabs.get(0).id));
                tabLayout.addTab(tabLayout.newTab().setText(tabs.get(1).id));
                tabLayout.addTab(tabLayout.newTab().setText(tabs.get(2).id));
                tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
            }
        }
    }


    /**
     * Set up view page and tab selections
     */


    private void setupViewPager() {

        viewPager = (CustomViewPager) findViewById(R.id.pager);

        Intent intent = getIntent();

//        if (intent!=null){
//            pos = intent.getStringExtra("pos");
//        }
//        else {
//            pos="no";
//        }


        final PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(2);
        viewPager.setCurrentItem(0);



       /* viewPager.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                return true;
            }
        });*/


        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {


                if (tab.getPosition() == 0) {

                    perspective.setVisibility(View.GONE);
                    filter.setVisibility(View.VISIBLE);
                    viewPager.setCurrentItem(tab.getPosition());
                    ArrayList<ConfigModel> nav_items = db.getNavigation(MainActivity.this);
                    final Menu menu = navigationView.getMenu();
                    menu.clear();

                    if (nav_items.size() > 0) {
                        subMenu = menu.addSubMenu(nav_items.get(0).displayName.toUpperCase());
                    } else {
                        subMenu = menu.addSubMenu("INBOX");
                    }
                    MenuItem all = subMenu.add(Menu.NONE, 10, Menu.NONE, "    " + "All");
                    subMenu.findItem(10).setIcon(R.mipmap.approveall_red);
                    if (CWIncturePreferences.getLeftFilterTab0() != null && (CWIncturePreferences.getLeftFilterTab0().equalsIgnoreCase("All") || CWIncturePreferences.getLeftFilterTab0().isEmpty())) {
                        all.setChecked(true).setChecked(true);

                    }

                    Menu menuNavright = navigationView_right.getMenu();

                    menuNavright.clear();
                    final SubMenu sub = menuNavright.addSubMenu("FILTERS");
                    MenuItem allWorkItems = sub.add(Menu.NONE, 10, Menu.NONE, "All Workitems");
                    if (CWIncturePreferences.getRightFilterTab0() != null && (CWIncturePreferences.getRightFilterTab0().equalsIgnoreCase("All Workitems") || CWIncturePreferences.getRightFilterTab0().isEmpty())) {
                        allWorkItems.setChecked(true).setChecked(true);
                    }


                    ArrayList<ConfigModel> filter_items = db.getTypes(MainActivity.this);
                    JSONArray options = new JSONArray();


                    ArrayList<ConfigModel> filter_options = new ArrayList<>();

                    for (int i = 0; i < filter_items.size(); i++) {
                        ConfigModel filter = new ConfigModel();
                        filter.id = filter_items.get(i).id;
                        filter.displayName = filter_items.get(i).displayName;
                        filter_options.add(filter);
                        sub.add(Menu.NONE, i, Menu.NONE, (filter_items.get(i)).id);
                    }


                    if (filter_items.size() > 0) {
                        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED, Gravity.RIGHT);
//                        try {
//                            ArrayList<ConfigModel> filteroptions = new ArrayList<ConfigModel>();
//                           // options = new JSONArray(filter_items.get(0).options);
//
////                            if (options.length() > 0) {
////                                for (int o = 0; o < options.length(); o++) {
////                                    JSONObject oobj = options.optJSONObject(o);
////                                    ConfigModel omodel = new ConfigModel();
////                                    omodel.id = oobj.optString("id");
////                                    omodel.icon = oobj.optString("icon");
////                                    omodel.displayName = oobj.optString("displayName");
////                                    filteroptions.add(omodel);
////                                    MenuItem item = sub.add(Menu.NONE, o, Menu.NONE, (omodel.displayName));
////                                    if (CWIncturePreferences.getRightFilterTab0() != null && CWIncturePreferences.getRightFilterTab0().equalsIgnoreCase(omodel.displayName.trim())) {
////                                        item.setChecked(true).setChecked(true);
////                                    }
////                                }
////                                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED, Gravity.RIGHT);
////                            } else {
//                                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, Gravity.RIGHT);
//                                filter.setVisibility(View.GONE);
//                            }
//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
                    } else {
                        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, Gravity.RIGHT);
                        filter.setVisibility(View.GONE);
                    }


                    for (int i = 0, count = navigationView_right.getChildCount(); i < count; i++) {
                        final View child = navigationView_right.getChildAt(i);
                        if (child != null && child instanceof ListView) {
                            final ListView menuView = (ListView) child;
                            final HeaderViewListAdapter adapter = (HeaderViewListAdapter) menuView.getAdapter();
                            final BaseAdapter wrapped = (BaseAdapter) adapter.getWrappedAdapter();
                            wrapped.notifyDataSetChanged();
                        }
                    }


                }
                if (tab.getPosition() == 1) {

                    perspective.setVisibility(View.GONE);
                    filter.setVisibility(View.GONE);
                    viewPager.setCurrentItem(tab.getPosition());
                    ArrayList<ConfigModel> navitems = db.getNavigation(MainActivity.this);
                    final Menu menu = navigationView.getMenu();
                    menu.clear();

                    if (navitems.size() > 1) {
                        subMenu = menu.addSubMenu(navitems.get(1).displayName.toUpperCase());
                    } else {
                        subMenu = menu.addSubMenu("REPORTS");
                    }

                    try {
                        ArrayList<ConfigModel> navigationoptions = new ArrayList<ConfigModel>();

                        JSONArray options = new JSONArray();

                        if (navitems.size() > 1) {
                            options = new JSONArray(navitems.get(1).options);
                        }
                        if (options.length() > 0) {
                            for (int o = 0; o < options.length(); o++) {
                                JSONObject oobj = options.optJSONObject(o);
                                ConfigModel omodel = new ConfigModel();
                                omodel.id = oobj.optString("id");
                                omodel.icon = oobj.optString("icon");
                                omodel.displayName = oobj.optString("displayName");
                                navigationoptions.add(omodel);
                                MenuItem item = subMenu.add("     " + omodel.displayName);
                                if (CWIncturePreferences.getLeftFilterTab1() != null && CWIncturePreferences.getLeftFilterTab1().equalsIgnoreCase(omodel.displayName.trim())) {
                                    item.setChecked(true).setChecked(true);
                                }
                            }

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    Menu menuNavRight = navigationView_right.getMenu();

                    menuNavRight.clear();
                    final SubMenu sub = menuNavRight.addSubMenu("FILTERS");

                    ArrayList<ConfigModel> filter_items = db.getFilters(MainActivity.this);
                    if (filter_items.size() > 1) {
                        try {

                            JSONArray options = new JSONArray();

                            if (filter_items.size() > 1) {
                                options = new JSONArray(filter_items.get(1).options);
                                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED, Gravity.RIGHT);

                            } else {
                                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, Gravity.RIGHT);

                                filter.setVisibility(View.GONE);
                            }
                            if (options.length() > 0) {
                                for (int o = 0; o < options.length(); o++) {
                                    JSONObject json_obj = options.optJSONObject(o);
                                    ConfigModel filter_config = new ConfigModel();
                                    filter_config.id = json_obj.optString("id");
                                    filter_config.icon = json_obj.optString("icon");
                                    filter_config.displayName = json_obj.optString("displayName");

                                    MenuItem item = sub.add((filter_config.displayName));

                                    if (CWIncturePreferences.getRightFilterTab1() != null && CWIncturePreferences.getRightFilterTab1().equalsIgnoreCase(filter_config.displayName.trim())) {
                                        item.setChecked(true).setChecked(true);
                                    }
                                }
                            } else {
                                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, Gravity.RIGHT);

                                filter.setVisibility(View.GONE);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, Gravity.RIGHT);
                        filter.setVisibility(View.GONE);
                    }

                    for (int i = 0, count = navigationView_right.getChildCount(); i < count; i++) {
                        final View child = navigationView_right.getChildAt(i);
                        if (child != null && child instanceof ListView) {
                            final ListView menuView = (ListView) child;
                            final HeaderViewListAdapter adapter = (HeaderViewListAdapter) menuView.getAdapter();
                            final BaseAdapter wrapped = (BaseAdapter) adapter.getWrappedAdapter();
                            wrapped.notifyDataSetChanged();
                        }
                    }


                }
                if (tab.getPosition() == 2) {
                    perspective.setVisibility(View.GONE);
                    filter.setVisibility(View.GONE);
                    viewPager.setCurrentItem(tab.getPosition());
                    ArrayList<ConfigModel> nav_items = db.getNavigation(MainActivity.this);
                    final Menu menu = navigationView.getMenu();
                    menu.clear();

                    if (nav_items.size() > 2) {
                        subMenu = menu.addSubMenu(nav_items.get(2).displayName.toUpperCase());
                    } else {
                        subMenu = menu.addSubMenu("CHERRIES");
                    }

                    try {
                        ArrayList<ConfigModel> navigationoptions = new ArrayList<ConfigModel>();

                        JSONArray options = new JSONArray();

                        if (nav_items.size() > 2) {
                            options = new JSONArray(nav_items.get(2).options);

                        }
                        if (options.length() > 0) {
                            for (int o = 0; o < options.length(); o++) {
                                JSONObject json_obj = options.optJSONObject(o);
                                ConfigModel navigation_config = new ConfigModel();
                                navigation_config.id = json_obj.optString("id");
                                navigation_config.icon = json_obj.optString("icon");
                                navigation_config.displayName = json_obj.optString("displayName");
                                navigationoptions.add(navigation_config);
                                MenuItem item = subMenu.add(navigation_config.displayName);
                                if (CWIncturePreferences.getLeftFilterTab2() != null && CWIncturePreferences.getLeftFilterTab2().equalsIgnoreCase(navigation_config.displayName.trim())) {
                                    item.setChecked(true).setChecked(true);
                                }
                            }

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    Menu menuNavRight = navigationView_right.getMenu();


                    menuNavRight.clear();
                    final SubMenu sub = menuNavRight.addSubMenu("FILTERS");

                    ArrayList<ConfigModel> filter_items = db.getFilters(MainActivity.this);

                    try {

                        ArrayList<ConfigModel> filter_options = new ArrayList<ConfigModel>();
                        JSONArray options = new JSONArray();

                        if (filter_items.size() > 2) {
                            options = new JSONArray(filter_items.get(2).options);
                            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED, Gravity.RIGHT);
                        } else {
                            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, Gravity.RIGHT);
                            filter.setVisibility(View.GONE);
                        }
                        if (options.length() > 0) {
                            for (int o = 0; o < options.length(); o++) {
                                JSONObject json_obj = options.optJSONObject(o);
                                ConfigModel filter_config = new ConfigModel();
                                filter_config.id = json_obj.optString("id");
                                filter_config.icon = json_obj.optString("icon");
                                filter_config.displayName = json_obj.optString("displayName");
                                filter_options.add(filter_config);
                                MenuItem item = sub.add((filter_config.displayName));
                                if (CWIncturePreferences.getRightFilterTab2() != null && CWIncturePreferences.getRightFilterTab2().equalsIgnoreCase(filter_config.displayName.trim())) {
                                    item.setChecked(true).setChecked(true);
                                }
                            }
                        } else {
                            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, Gravity.RIGHT);
                            filter.setVisibility(View.GONE);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    for (int i = 0, count = navigationView_right.getChildCount(); i < count; i++) {
                        final View child = navigationView_right.getChildAt(i);
                        if (child != null && child instanceof ListView) {
                            final ListView menuView = (ListView) child;
                            final HeaderViewListAdapter adapter = (HeaderViewListAdapter) menuView.getAdapter();
                            final BaseAdapter wrapped = (BaseAdapter) adapter.getWrappedAdapter();
                            wrapped.notifyDataSetChanged();
                        }
                    }

                }

                invalidateOptionsMenu();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }
        });
//        if (tab_position != null) {
//            if (tab_position.contains("one")) {
//                viewPager.setCurrentItem(0);
//            } else if (tab_position.contains("two")) {
//                viewPager.setCurrentItem(1);
//            } else {
//                viewPager.setCurrentItem(0);
//            }
//        }


    }


    /**
     * Display a snack bar if no sim found
     */
    private void moreInfo() {
        TelephonyManager tManager = (TelephonyManager) getBaseContext()
                .getSystemService(TELEPHONY_SERVICE);

        TelephonyManager tm = (TelephonyManager) this.getSystemService(TELEPHONY_SERVICE);  //gets the current TelephonyManager
        if (tm.getSimState() != TelephonyManager.SIM_STATE_ABSENT) {
            String carrierName = tManager.getNetworkOperatorName();
            Log.d("Network", "" + carrierName);
        } else {
            Snackbar.make(coordinatorLayout, "No sim card", Snackbar.LENGTH_SHORT).show();
        }


        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;

        Log.d("Manufacturer", "" + manufacturer);
        Log.d("Model", "" + model);

        StringBuilder builder = new StringBuilder();
        builder.append("android : ").append(Build.VERSION.RELEASE);

        Field[] fields = Build.VERSION_CODES.class.getFields();
        for (Field field : fields) {
            String fieldName = field.getName();
            int fieldValue = -1;

            try {
                fieldValue = field.getInt(new Object());
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (NullPointerException e) {
                e.printStackTrace();
            }

            if (fieldValue == Build.VERSION.SDK_INT) {
                builder.append(" : ").append(fieldName).append(" : ");
                builder.append("sdk=").append(fieldValue);
            }
        }


    }

    @Override
    public void processFinish(String output, int status_code, String Url, int type) {

        if (Url.contains(CWUrls.GROUPS_SEARCH)) {

            String status = "";
            String message = "";
            searched_user_list.clear();

            try {
                JSONObject main = new JSONObject(output);
                if (main.has("data")) {

                    JSONArray system_array = (JSONArray) main.opt("data");
                    if (system_array != null) {
                        for (int i = 0; i < system_array.length(); i++) {
                            Log.d("SystemArray", "" + system_array.length());
                            JSONObject obj = system_array.getJSONObject(i);
                            ContactsModel item = new ContactsModel();
                            item.setEmail(obj.optString("email"));
                            item.setFirstname(obj.optString("firstName"));
                            item.setLastname(obj.optString("lastName"));
                            item.setUserId(obj.optString("_id"));

                            searched_user_list.add(item);

                        }

                    }
                }
                if (main.has("status")) {
                    status = main.optString("status");
                }
                if (main.has("message")) {
                    message = main.optString("message");
                }
            } catch (Exception ex) {
                Log.d("EXCEPTION", ex.getMessage());
            }

            if (status.equals("success")) {

                if (searched_user_list.size() > 0) {
                    empty.setVisibility(View.GONE);
                    if (search_list.getAdapter() == null) {
                        BasicSearchAdapter adapter = new BasicSearchAdapter(MainActivity.this, searched_user_list);
                        search_list.setAdapter(adapter);
                    } else {
                        search_list.getAdapter().notifyDataSetChanged();
                    }
                } else {
                    empty.setVisibility(View.VISIBLE);
                }


            } else if (message != null && !message.isEmpty()) {
                Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG).show();
            }
        } else if (Url.contains(CWUrls.LOG_OUT)) {
            String status = "", message = "";
            try {
                JSONObject object = new JSONObject(output);

                if (object.has("status")) {
                    status = object.optString("status");
                }
                if (object.has("message")) {
                    message = object.optString("message");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if (status.equals("success")) {

                Util util = new Util();
                util.logoutfunc(MainActivity.this);


            } else if (message != null && !message.isEmpty()) {
                Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        } else if (Url.contains(CWUrls.NOTIFICATIONS_COUNT)) {
            String status = "", message = "";
            try {
                JSONObject object = new JSONObject(output);

                if (object.has("status")) {
                    status = object.optString("status");
                }

                if (status.equals("success")) {
                    int notif_count = object.optInt("data");
                    ((CherryworkApplication) getApplicationContext()).notifiationAdded(notif_count);

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
    public void onRefresh() {

    }


    /**
     * On click for logout and people navigation options
     */

    private class onClickListener implements View.OnClickListener {
        int Position;

        public onClickListener(int position) {
            this.Position = position;
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.logout) {
                if (Util.isOnline(MainActivity.this)) {
                    drawerLayout.closeDrawers();
                    logout();
                } else {
                    drawerLayout.closeDrawers();
                    Snackbar.make(coordinatorLayout, "No Internet Connection,Try Later", Snackbar.LENGTH_SHORT).show();
                }
            }

            if (v.getId() == R.id.dashboard) {
                drawerLayout.closeDrawers();
                Intent intent = new Intent(MainActivity.this, DashboardActivity.class);
                startActivity(intent);
            }
        }
    }

    /**
     * Set up navigation drawer for display of user data
     */
    private void setupDrawerLayout() {

        if (CWIncturePreferences.getAvatarurl() != null && !CWIncturePreferences.getAvatarurl().isEmpty()) {
            Util.loadAttachmentImageSquareForProfile(this, CWIncturePreferences.getAvatarurl(), img);
            Util.loadAttachmentImageLarge(this, CWIncturePreferences.getAvatarurl(), profileBg);
        }

        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, Gravity.RIGHT);
        navigationView_right.setItemIconTintList(null);
        String lname = "";

        if (CWIncturePreferences.getLastname().equalsIgnoreCase("Null")) {
            lname = "";
        } else {
            lname = CWIncturePreferences.getLastname();
        }
        name.setText(CWIncturePreferences.getFirstname() + " " + lname);
        email.setText(CWIncturePreferences.getEmail());
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.closeDrawer(Gravity.LEFT);
                // To avoid lag
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent userProfile = new Intent(MainActivity.this, UserProfileActivity.class);
                        startActivity(userProfile);
                    }
                }, 200);

            }
        });

        DrawerLayout mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(
                MainActivity.this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.string.drawer_open,  /* "open drawer" description */
                R.string.drawer_close  /* "close drawer" description */
        ) {

            /**
             * Called when a drawer has settled in a completely closed state.
             */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);

            }

            /**
             * Called when a drawer has settled in a completely open state.
             */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);

            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerToggle.setDrawerIndicatorEnabled(true);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.notify) {
            Intent intent = new Intent(MainActivity.this, TimeSheetNotifications.class);
            startActivity(intent);

        }
        if (id == R.id.filter) {
            if (drawerLayout.isDrawerOpen(Gravity.RIGHT)) {
                drawerLayout.closeDrawer(Gravity.RIGHT);
            } else {
                drawerLayout.openDrawer(Gravity.RIGHT);
            }

        }

        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onBackPressed() {
        ActivityCompat.finishAffinity(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {


        super.onCreateOptionsMenu(menu);


        if (viewPager.getCurrentItem() == 0 || viewPager.getCurrentItem() == 2 || viewPager.getCurrentItem() == 3) {
            menu.findItem(R.id.filter).setVisible(false);
        } else {
            menu.findItem(R.id.filter).setVisible(true);
        }
        return true;

    }


    @Override
    protected void onResume() {
        super.onResume();


        if (CWIncturePreferences.getAvatarurl() != null && !CWIncturePreferences.getAvatarurl().isEmpty()) {
            Util.loadAttachmentImageSquareForProfile(this, CWIncturePreferences.getAvatarurl(), img);
            Util.loadAttachmentImageSquareForProfile(this, CWIncturePreferences.getAvatarurl(), profileBg);
        }


        db = new DbUtil();
        tabs = db.getTabs(MainActivity.this);
        navigation = db.getNavigation(MainActivity.this);
        filters = db.getFilters(MainActivity.this);

    }

    @Override
    protected void onRestart() {
        super.onRestart();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == LockingHelper.REQUEST_CODE_UNLOCK && resultCode == RESULT_OK) {
            onResume();
        }
        super.onActivityResult(requestCode, resultCode, data);
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    /**
     * Get the notification count to show badge
     */
    public void getNotificationCount() {
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("x-access-token", CWIncturePreferences.getAccessToken());
        headers.put("x-email-id", CWIncturePreferences.getEmail());
        try {
            headers.put("android-version", getPackageManager().getPackageInfo(getPackageName(), 0).versionName);
        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        NetworkConnector connect = new NetworkConnector(MainActivity.this, NetworkConnector.TYPE_GET, CWUrls.NOTIFICATIONS_COUNT, headers, null, MainActivity.this);
        if (connect.isAllowed()) {
            connect.execute();
        }
    }
}
