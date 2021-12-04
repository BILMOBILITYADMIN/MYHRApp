package com.hrapps.LMS_Britannia;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hrapps.BasicActivity;
import com.hrapps.R;
import com.hrapps.UserProfileActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import DB.DbUtil;
import Model.RolesModel;
import Utility.AsyncResponse;
import Utility.CWIncturePreferences;
import Utility.CWUrls;
import Utility.NetworkConnector;
import Utility.ProfileImageView;
import Utility.Util;

/**
 * Created by Nuzha on 31-01-2017.
 */

public class LMS_Dashboard extends BasicActivity implements AsyncResponse {
    private static final String TAG = "LMS_Dashboard";
    WebView webView;
    TextView title;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    Button logout;
    TextView name, email;
    ImageView side_menu, back, profileBg, home;
    ProfileImageView img;
    private ProgressDialog progressDialog;
    boolean checked = false;
    String cherrySelected = "";

    DbUtil db;
    ArrayList<RolesModel> mRolesList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.britannia_dashboard);
        webView = (WebView) findViewById(R.id.webview);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        db = new DbUtil();
        mRolesList = (ArrayList<RolesModel>) getIntent().getSerializableExtra("roles");
        Log.d("roleslist=", "" + mRolesList.toString());
        cherrySelected = getIntent().getStringExtra("cherrySelected");

        img = (ProfileImageView) findViewById(R.id.drawer_header_profile_pic);
        name = (TextView) findViewById(R.id.drawer_header_profile_name);
        email = (TextView) findViewById(R.id.drawer_header_profile_email);
        profileBg = (ImageView) findViewById(R.id.backgroundimage1);
        side_menu = (ImageView) findViewById(R.id.menu);
        back = (ImageView) findViewById(R.id.back);
        home = (ImageView) findViewById(R.id.home);

        logout = (Button) findViewById(R.id.logout1);

        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        title = (TextView) findViewById(R.id.title);

        name.setText(CWIncturePreferences.getFirstname() + CWIncturePreferences.getLastname());
        email.setText(CWIncturePreferences.getEmail());

        if (CWIncturePreferences.getAvatarurl() != null && !CWIncturePreferences.getAvatarurl().isEmpty()) {
            Util.loadAttachmentImageSquareForProfile(this, CWIncturePreferences.getAvatarurl(), img);
            Util.loadAttachmentImageLarge(this, CWIncturePreferences.getAvatarurl(), profileBg);
        }

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.closeDrawers();
            }
        });

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.closeDrawer(Gravity.LEFT);
                // To avoid lag
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent userProfile = new Intent(LMS_Dashboard.this, UserProfileActivity.class);
                        startActivity(userProfile);
                    }
                }, 200);

            }
        });

        startWebView(CWUrls.DOMAIN_WEB_BRIT + "/#/mobile/lms/lms-dashboard", 0);

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();

            }
        });

        final Menu menu = navigationView.getMenu();

        final SubMenu subMenu = menu.addSubMenu("Dashboard");
        subMenu.add(Menu.NONE, 30, Menu.NONE, "    " + "Training Plans").setCheckable(true).setChecked(false);
        subMenu.add(Menu.NONE, 22, Menu.NONE, "    " + "Configuration Settings").setCheckable(true).setChecked(false);
        subMenu.add(Menu.NONE, 23, Menu.NONE, "    " + "Requirements").setCheckable(true).setChecked(false);
//        subMenu.add(Menu.NONE, 24, Menu.NONE, "    " + "Individual Development Plans").setCheckable(true).setChecked(false);
//        subMenu.add(Menu.NONE, 25, Menu.NONE, "    " + "Task").setCheckable(true).setChecked(false);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(final MenuItem menuItem) {

                checked = true;

                switch (menuItem.getItemId()) {
                    //Approvals
                    case 21:
                        drawerLayout.closeDrawers();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                webView.clearHistory();
                                startWebView(CWUrls.DOMAIN_WEB_BRIT + "/#/mobile/lms/lms-dashboard", menuItem.getGroupId());
                                startWebView(CWUrls.DOMAIN_WEB_BRIT + "/#/mobile/lms/task-approvals", menuItem.getGroupId());
                            }
                        }, 200);
                        return true;
                    //Configuration
                    case 22:
                        drawerLayout.closeDrawers();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                webView.clearHistory();
                                startWebView(CWUrls.DOMAIN_WEB_BRIT + "/#/mobile/lms/lms-dashboard", menuItem.getGroupId());
                                startWebView(CWUrls.DOMAIN_WEB_BRIT + "/#/mobile/lms/configuration-settings", menuItem.getGroupId());
                            }
                        }, 200);
                        return true;
                    //Requirements
                    case 23:
                        drawerLayout.closeDrawers();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                webView.clearHistory();
                                startWebView(CWUrls.DOMAIN_WEB_BRIT + "/#/mobile/lms/lms-dashboard", menuItem.getGroupId());
                                startWebView(CWUrls.DOMAIN_WEB_BRIT + "/#/mobile/lms/requirements-gathering", menuItem.getGroupId());
                            }
                        }, 200);
                        return true;
                    //Individual Development Plan
                    case 24:
                        drawerLayout.closeDrawers();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                webView.clearHistory();
                                startWebView(CWUrls.DOMAIN_WEB_BRIT + "/#/mobile/lms/lms-dashboard", menuItem.getGroupId());
                                startWebView(CWUrls.DOMAIN_WEB_BRIT + "/#/mobile/lms/idp-index", menuItem.getGroupId());
                            }
                        }, 200);
                        return true;
                    //Tasks
                    case 25:
                        drawerLayout.closeDrawers();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                webView.clearHistory();
                                startWebView(CWUrls.DOMAIN_WEB_BRIT + "/#/mobile/lms/lms-dashboard", menuItem.getGroupId());
                                startWebView(CWUrls.DOMAIN_WEB_BRIT + "/#/mobile/lms/index", menuItem.getGroupId());
                            }
                        }, 200);
                        return true;
                    //Curricula
                    case 26:
                        drawerLayout.closeDrawers();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                webView.clearHistory();
                                startWebView(CWUrls.DOMAIN_WEB_BRIT + "/#/mobile/lms/lms-dashboard", menuItem.getGroupId());
                                startWebView(CWUrls.DOMAIN_WEB_BRIT + "/#/mobile/lms/list-of-curriculum", menuItem.getGroupId());
                            }
                        }, 200);
                        return true;
                    //Courses
                    case 27:
                        drawerLayout.closeDrawers();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                webView.clearHistory();
                                startWebView(CWUrls.DOMAIN_WEB_BRIT + "/#/mobile/lms/lms-dashboard", menuItem.getGroupId());
                                startWebView(CWUrls.DOMAIN_WEB_BRIT + "/#/mobile/lms/list-of-courses", menuItem.getGroupId());
                            }
                        }, 200);
                        return true;
                    //Trainings
                    case 28:
                        drawerLayout.closeDrawers();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                webView.clearHistory();
                                startWebView(CWUrls.DOMAIN_WEB_BRIT + "/#/mobile/lms/lms-dashboard", menuItem.getGroupId());
                                startWebView(CWUrls.DOMAIN_WEB_BRIT + "/#/mobile/lms/mass-lms-approvals", menuItem.getGroupId());
                            }
                        }, 200);
                        return true;
                    //My Learning History
                    case 29:
                        drawerLayout.closeDrawers();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                webView.clearHistory();
                                startWebView(CWUrls.DOMAIN_WEB_BRIT + "/#/mobile/lms/lms-dashboard", menuItem.getGroupId());
                                startWebView(CWUrls.DOMAIN_WEB_BRIT + "/#/mobile/lms/mass-lms-approvals", menuItem.getGroupId());
                            }
                        }, 200);
                        return true;
                    //Training Plan
                    case 30:
                        drawerLayout.closeDrawers();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                webView.clearHistory();
                                startWebView(CWUrls.DOMAIN_WEB_BRIT + "/#/mobile/lms/lms-dashboard", menuItem.getGroupId());
                                startWebView(CWUrls.DOMAIN_WEB_BRIT + "/#/mobile/lms/tp-index", menuItem.getGroupId());
                            }
                        }, 200);
                        return true;
                    //Employee Status
                    case 31:
                        drawerLayout.closeDrawers();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                webView.clearHistory();
                                startWebView(CWUrls.DOMAIN_WEB_BRIT + "/#/mobile/lms/lms-dashboard", menuItem.getGroupId());
                                startWebView(CWUrls.DOMAIN_WEB_BRIT + "/#/mobile/lms/list-of-employees", menuItem.getGroupId());
                            }
                        }, 200);
                        return true;
                    default:
                        return true;
                }
            }
        });

        side_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(Gravity.LEFT);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Util.isOnline(LMS_Dashboard.this)) {
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

                            Toast.makeText(LMS_Dashboard.this, "No internet connection.Try later", Toast.LENGTH_SHORT).show();
                        }
                    }, 200);
                }
            }
        });

    }

    public void logout() {
        drawerLayout.closeDrawers();
        AlertDialog.Builder dialog = new AlertDialog.Builder(LMS_Dashboard.this);
        dialog.setTitle("Logout");
        dialog.setMessage("Do you want to logout?");
        dialog.setCancelable(true);
        dialog.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        logoutAsync();

                    }
                });
        dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog alert = dialog.create();
        alert.setCanceledOnTouchOutside(true);
        alert.show();


    }

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

        NetworkConnector connect = new NetworkConnector(LMS_Dashboard.this, NetworkConnector.TYPE_POST, CWUrls.LOG_OUT, headers, null, LMS_Dashboard.this);
        if (connect.isAllowed()) {
            connect.execute();
        } else {
            Toast.makeText(LMS_Dashboard.this, getString(R.string.action_not_allowed), Toast.LENGTH_SHORT).show();
        }

    }


    private void startWebView(String url, int roleSelected) {

        //Create new webview Client to show progress dialog
        //When opening a url or click on link

        if (progressDialog == null) {
            // in standard case YourActivity.this
            progressDialog = new ProgressDialog(LMS_Dashboard.this);
            progressDialog.setMessage("Loading...");
            progressDialog.setCancelable(true);
            progressDialog.show();
        }

        webView.setWebViewClient(new WebViewClient() {


            //If you will not use this method url links are opeen in new brower not in webview
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                view.loadUrl(url);
                return true;
            }

            //Show loader on url load

            public void onPageFinished(WebView view, String url) {
                try {

                    String finalPath = "";
                    if (null != url && url.length() > 0) {
                        int endIndex = url.lastIndexOf("/");
                        if (endIndex != -1) {
                            finalPath = url.substring(endIndex + 1, url.length());
                        }
                    }

                    if (url.contains(CWUrls.DOMAIN_WEB_BRIT + "/#/mobile/lms/template-dashboard")) {
                        title.setText("Templates");
                    }

                    //if session is expired it logs out of the app.
                    if (url.contains(CWUrls.DOMAIN_WEB_BRIT + "/#/mobile/lms/task-approvals")) {
                        title.setText("Approvals");
                    } else if (url.contains(CWUrls.DOMAIN_WEB_BRIT + "/#/mobile/lms/configuration-settings")) {
                        title.setText("Configuration");
                        addConfigSubMenu();
                    } else if (url.contains(CWUrls.DOMAIN_WEB_BRIT + "/#/mobile/lms/list-of-courses")) {
                        title.setText("Courses");
                    } else if (url.contains(CWUrls.DOMAIN_WEB_BRIT + "/#/mobile/lms/list-of-curriculum")) {
                        title.setText("Curricula");
                    } else if (url.contains(CWUrls.DOMAIN_WEB_BRIT + "/#/mobile/lms/list-of-employees")) {
                        title.setText("Employee Status");
                    } else if (url.contains(CWUrls.DOMAIN_WEB_BRIT + "/#/mobile/lms/idp-index")) {
                        title.setText("Individual Development Plan");
                    } else if (url.contains(CWUrls.DOMAIN_WEB_BRIT + "/#/mobile/lms/lms-dashboard")) {
                        title.setText("LMS Dashboard");
                    } else if (url.contains(CWUrls.DOMAIN_WEB_BRIT + "/#/mobile/lms/lms-dashboard-")) {          //???
                        title.setText("My Learning History");
                    } else if (url.contains(CWUrls.DOMAIN_WEB_BRIT + "/#/mobile/lms/requirements-gathering")) {
                        title.setText("Requirements");
                    } else if (url.contains(CWUrls.DOMAIN_WEB_BRIT + "/#/mobile/lms/index")) {
                        title.setText("Tasks");
                    } else if (url.contains(CWUrls.DOMAIN_WEB_BRIT + "/#/mobile/lms/tp-index")) {
                        title.setText("Training Plan");
                    } else if (url.contains(CWUrls.DOMAIN_WEB_BRIT + "/#/mobile/lms/trainings")) {               //???
                        title.setText("Trainings");
                    }

                    if (!(url.contains(CWUrls.DOMAIN_WEB_BRIT + "/#/mobile/lms/configuration-settings")
//                            || url.contains(CWUrls.DOMAIN_WEB_BRIT + "/#/mobile/lms/list-of-courses")
//                            || url.contains(CWUrls.DOMAIN_WEB_BRIT + "/#/mobile/lms/list-of-curriculum")
//                            || url.contains(CWUrls.DOMAIN_WEB_BRIT + "/#/mobile/lms/list-of-employees")
                    )) {
                        resetSubMenu();
                    }
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }

        });


        webView.setWebChromeClient(new WebChromeClient() {

            public void onProgressChanged(WebView view, final int progress) {
                if (progress == 100 && progressDialog.isShowing()) {

                    progressDialog.dismiss();
                    // progressDialog = null;
                }
            }

        });


        // You can create external class extends with WebChromeClient
        // Taking WebViewClient as inner class
        // we will define openFileChooser for select file from camera or

        Log.d("roleData=", "" + CWIncturePreferences.getRoleRegion());
        int roleSel = roleSelected;

        webView.getSettings().setJavaScriptEnabled(true);
        CookieManager.getInstance().setCookie(CWUrls.DOMAIN_WEB_BRIT, "token=" + CWIncturePreferences.getAccessToken());
        CookieManager.getInstance().setCookie(CWUrls.DOMAIN_WEB_BRIT, "email=" + CWIncturePreferences.getEmail());
        CookieManager.getInstance().setCookie(CWUrls.DOMAIN_WEB_BRIT, "roles=" + CWIncturePreferences.getRoleRegion());
        CookieManager.getInstance().setCookie(CWUrls.DOMAIN_WEB_BRIT, "myid=" + CWIncturePreferences.getUserId());
        CookieManager.getInstance().setCookie(CWUrls.DOMAIN_WEB_BRIT, "roleSelected=" + roleSelected);
        CookieManager.getInstance().setCookie(CWUrls.DOMAIN_WEB_BRIT, "cherrySelected=" + cherrySelected);

        Log.d("1token=", "" + CWIncturePreferences.getAccessToken());
        Log.d("1email=", "" + CWIncturePreferences.getEmail());
        Log.d("1roleData=", "" + CWIncturePreferences.getRoleRegion());
        Log.d("1myid=", "" + CWIncturePreferences.getUserId());
        Log.d("1roleSelected=", "" + roleSelected);

        CookieSyncManager.getInstance().sync();

        webView.loadUrl(url);

    }

    private void resetSubMenu() {
        final Menu menu = navigationView.getMenu();
        final SubMenu subMenu = menu.getItem(0).getSubMenu();

        subMenu.clear();

        subMenu.add(Menu.NONE, 30, Menu.NONE, "    " + "Training Plans").setCheckable(true).setChecked(false);
        subMenu.add(Menu.NONE, 22, Menu.NONE, "    " + "Configuration Settings").setCheckable(true).setChecked(false);
        subMenu.add(Menu.NONE, 23, Menu.NONE, "    " + "Requirements").setCheckable(true).setChecked(false);

        invalidateOptionsMenu();
    }

    //
    private void addConfigSubMenu() {
        final Menu menu = navigationView.getMenu();
        final SubMenu subMenu = menu.getItem(0).getSubMenu();
//        Log.d("D -> ", subMenu.getItem(1).getTitle().toString());

        subMenu.clear();

        subMenu.add(Menu.NONE, 27, Menu.NONE, "    " + "Courses").setCheckable(true).setChecked(false);
        subMenu.add(Menu.NONE, 26, Menu.NONE, "    " + "Curriculum").setCheckable(true).setChecked(false);
        subMenu.add(Menu.NONE, 31, Menu.NONE, "    " + "Employees").setCheckable(true).setChecked(false);

        invalidateOptionsMenu();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (CWIncturePreferences.getAvatarurl() != null && !CWIncturePreferences.getAvatarurl().isEmpty()) {
            Util.loadAttachmentImageSquareForProfile(this, CWIncturePreferences.getAvatarurl(), img);
            Util.loadAttachmentImageSquareForProfile(this, CWIncturePreferences.getAvatarurl(), profileBg);
        }

    }

    @Override
    protected void onRestart() {
        super.onRestart();

    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            if (webView.canGoBack()) {
                webView.goBack();
            } else {
                super.onBackPressed();
            }
        }
    }

    @Override
    public void processFinish(String output, int status_code, String url, int type) {
        if (url.contains(CWUrls.LOG_OUT)) {
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
                util.logoutfunc(LMS_Dashboard.this);


            } else if (message != null && !message.isEmpty()) {
                Toast.makeText(LMS_Dashboard.this, message, Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void configurationUpdated(boolean configUpdated) {

    }
}
