package com.hrapps.PMS_Britannia;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
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
 * Created by harshu on 1/20/2017.
 */

public class PMS_Dashboard extends BasicActivity implements AsyncResponse {

    WebView webView;
    TextView title, no_data;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    Button logout;
    TextView name, email;
    ImageView side_menu, back, profileBg, home;
    ProfileImageView img;
    private ProgressDialog progressDialog;
    boolean checked = false;
    String url = "";
    String cherrySelected = "";
    String roleName = "";
    String roleRegion = "";
    String hrFunction = "";

    DbUtil db;
    ArrayList<RolesModel> mRolesList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.britannia_dashboard);

        webView = (WebView) findViewById(R.id.webview);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        mRolesList = (ArrayList<RolesModel>) getIntent().getSerializableExtra("roles");
        if (!mRolesList.isEmpty() && mRolesList != null) {
            Log.d("roleslist=", "" + mRolesList.toString());
        }
        cherrySelected = getIntent().getStringExtra("cherrySelected");

        db = new DbUtil();
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
        title.setText("PMS Dashboard");

        no_data = (TextView) findViewById(R.id.no_data);

        if (mRolesList.size() == 0) {
            no_data.setVisibility(View.VISIBLE);
            webView.setVisibility(View.GONE);
        } else {
            no_data.setVisibility(View.GONE);
            webView.setVisibility(View.VISIBLE);
        }

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
                        Intent userProfile = new Intent(PMS_Dashboard.this, UserProfileActivity.class);
                        startActivity(userProfile);
                    }
                }, 200);

            }
        });


        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();

            }
        });

        Intent intent = getIntent();

        if (intent.getStringExtra("url") != null) {
            url = intent.getStringExtra("url");
        } else {
            url = "/#/mobile/pms/dashboard";

        }

        startWebView(CWUrls.DOMAIN_WEB_BRIT + url, 0);


        final Menu menu = navigationView.getMenu();

        String role = "";
        if (mRolesList.size() > 0) {
            if (mRolesList.size() == 1) {
                role = mRolesList.get(0).getRole_name();
                final SubMenu subMenu = menu.addSubMenu("Dashboard");

                switch (role.toUpperCase()) {
                    case "VPSA":
                    case "VPHR":
                        subMenu.add(0, 11, Menu.NONE, "Approvals").setCheckable(true).setChecked(false);
                        break;
                    case "ADMIN":
                        subMenu.add(Menu.NONE, 12, Menu.NONE, "    " + "Appraisals").setCheckable(true).setChecked(false);
                        subMenu.add(Menu.NONE, 13, Menu.NONE, "    " + "Templates").setCheckable(true).setChecked(false);
                        subMenu.add(Menu.NONE, 14, Menu.NONE, "    " + "Criterions").setCheckable(true).setChecked(false);
                        subMenu.add(Menu.NONE, 17, Menu.NONE, "    " + "Employee Status Report").setCheckable(true).setChecked(false);
                        subMenu.add(Menu.NONE, 18, Menu.NONE, "    " + "Scale settings").setCheckable(true).setChecked(false);
                        subMenu.add(Menu.NONE, 20, Menu.NONE, "    " + "Launch filter settings").setCheckable(true).setChecked(false);
                        break;
                    case "SA":
                    case "MANAGER":
                    case "RHRM":
                    case "RSM":
                    case "HRBP":
                        subMenu.add(Menu.NONE, 19, Menu.NONE, "    " + "Appraisals").setCheckable(true).setChecked(false);
                        break;
                    case "TSI":
                        subMenu.add(Menu.NONE, 21, Menu.NONE, "    " + "Appraisals").setCheckable(true).setChecked(false);
                        break;

                }

            }//end of roleslist size=1
            else {
                for (int i = 0; i < mRolesList.size(); i++) {

                    if (mRolesList.get(i).getRole_name().equalsIgnoreCase("VPSA")
                            || mRolesList.get(i).getRole_name().equalsIgnoreCase("VPHR")
                            || mRolesList.get(i).getRole_name().equalsIgnoreCase("ADMIN")
                            || mRolesList.get(i).getRole_name().equalsIgnoreCase("TSI")
                            || mRolesList.get(i).getRole_name().equalsIgnoreCase("manager")
                            || mRolesList.get(i).getRole_name().equalsIgnoreCase("RHRM")
                            || mRolesList.get(i).getRole_name().equalsIgnoreCase("RSM")
                            || mRolesList.get(i).getRole_name().equalsIgnoreCase("HRBP")
                            || mRolesList.get(i).getRole_name().equalsIgnoreCase("SA"))

                    {
                        final SubMenu subMenu;
                        if (mRolesList.get(i).getRole_region().equalsIgnoreCase("null") || mRolesList.get(i).getRole_region().isEmpty()) {
                            subMenu = menu.addSubMenu(mRolesList.get(i).getRole_name() + " - " + mRolesList.get(i).getRole_hr_function());

                        } else if (mRolesList.get(i).getRole_hr_function().equalsIgnoreCase("null") || mRolesList.get(i).getRole_hr_function().isEmpty()) {
                            subMenu = menu.addSubMenu(mRolesList.get(i).getRole_name() + " - " + mRolesList.get(i).getRole_region());

                        } else {
                            subMenu = menu.addSubMenu(mRolesList.get(i).getRole_name() + " - " + mRolesList.get(i).getRole_region() + " - " + mRolesList.get(i).getRole_region());

                        }

                        role = mRolesList.get(i).getRole_name();
                        switch (role.toUpperCase()) {
                            case "VPSA":
                            case "VPHR":
                                subMenu.add(0, 11, 0, "Approvals").setCheckable(true).setChecked(false);
                                break;
                            case "ADMIN":
                                subMenu.add(i, 12, i, "    " + "Appraisals").setCheckable(true).setChecked(false);
                                subMenu.add(i, 13, i, "    " + "Templates").setCheckable(true).setChecked(false);
                                subMenu.add(i, 14, i, "    " + "Criterions").setCheckable(true).setChecked(false);
                                subMenu.add(i, 17, i, "    " + "Employee Status Report").setCheckable(true).setChecked(false);
                                subMenu.add(i, 18, i, "    " + "Scale settings").setCheckable(true).setChecked(false);
                                subMenu.add(i, 20, i, "    " + "Launch filter settings").setCheckable(true).setChecked(false);
                                break;
                            case "SA":
                            case "MANAGER":
                            case "RSM":
                            case "HRBP":
                                subMenu.add(i, 19, i, "    " + "Appraisals").setCheckable(true).setChecked(false);
                                break;
                            case "TSI":
                                subMenu.add(i, 21, i, "    " + "Appraisals").setCheckable(true).setChecked(false);
                                break;
                        }
                    }
                }
            }

        }//end of roleslist size>0


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(final MenuItem menuItem) {

                checked = true;

                switch (menuItem.getItemId()) {
                    case 11:
                        drawerLayout.closeDrawers();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                webView.clearHistory();
                                startWebView(CWUrls.DOMAIN_WEB_BRIT + "/#/mobile/pms/dashboard", menuItem.getGroupId());
                                startWebView(CWUrls.DOMAIN_WEB_BRIT + "/#/mobile/pms/mass-pms-approvals", menuItem.getGroupId());
                            }
                        }, 200);

                        return true;

                    case 12:
                        drawerLayout.closeDrawers();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                webView.clearHistory();
                                startWebView(CWUrls.DOMAIN_WEB_BRIT + "/#/mobile/pms/dashboard", menuItem.getGroupId());
                                startWebView(CWUrls.DOMAIN_WEB_BRIT + "/#/mobile/pms/appraisal-dashboard", menuItem.getGroupId());

                            }
                        }, 200);

                        return true;

                    case 13:
                        drawerLayout.closeDrawers();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                webView.clearHistory();
                                startWebView(CWUrls.DOMAIN_WEB_BRIT + "/#/mobile/pms/dashboard", menuItem.getGroupId());
                                startWebView(CWUrls.DOMAIN_WEB_BRIT + "/#/mobile/pms/template-dashboard", menuItem.getGroupId());
                            }
                        }, 200);

                        return true;

                    case 14:
                        drawerLayout.closeDrawers();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                webView.clearHistory();
                                startWebView(CWUrls.DOMAIN_WEB_BRIT + "/#/mobile/pms/dashboard", menuItem.getGroupId());
                                startWebView(CWUrls.DOMAIN_WEB_BRIT + "/#/mobile/pms/criterions-dashboard", menuItem.getGroupId());
                            }
                        }, 200);

                        return true;

                    case 15:
                        drawerLayout.closeDrawers();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                webView.clearHistory();
                                startWebView(CWUrls.DOMAIN_WEB_BRIT + "/#/mobile/pms/dashboard", menuItem.getGroupId());
                                startWebView(CWUrls.DOMAIN_WEB_BRIT + "/#/mobile/pms/employee-annual-appraisal", menuItem.getGroupId());
                            }
                        }, 200);

                        return true;

                    case 16:
                        drawerLayout.closeDrawers();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                webView.clearHistory();
                                startWebView(CWUrls.DOMAIN_WEB_BRIT + "/#/mobile/pms/dashboard", menuItem.getGroupId());
                                startWebView(CWUrls.DOMAIN_WEB_BRIT + "/#/mobile/pms/annual-approvals", menuItem.getGroupId());

                            }
                        }, 200);

                        return true;

                    case 17:
                        drawerLayout.closeDrawers();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                webView.clearHistory();
                                startWebView(CWUrls.DOMAIN_WEB_BRIT + "/#/mobile/pms/dashboard", menuItem.getGroupId());
                                startWebView(CWUrls.DOMAIN_WEB_BRIT + "/#/mobile/pms/active-appraisal-list", menuItem.getGroupId());
                            }
                        }, 200);

                        return true;

                    case 18:
                        drawerLayout.closeDrawers();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                webView.clearHistory();
                                startWebView(CWUrls.DOMAIN_WEB_BRIT + "/#/mobile/pms/dashboard", menuItem.getGroupId());
                                startWebView(CWUrls.DOMAIN_WEB_BRIT + "/#/mobile/pms/scale-settings", menuItem.getGroupId());

                            }
                        }, 200);

                        return true;

                    case 19:
                        drawerLayout.closeDrawers();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                webView.clearHistory();
                                startWebView(CWUrls.DOMAIN_WEB_BRIT + "/#/mobile/pms/dashboard", menuItem.getGroupId());
                                startWebView(CWUrls.DOMAIN_WEB_BRIT + "/#/mobile/pms/appraisal-dashboard", menuItem.getGroupId());
                            }
                        }, 200);

                        return true;

                    case 20:
                        drawerLayout.closeDrawers();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                webView.clearHistory();
                                startWebView(CWUrls.DOMAIN_WEB_BRIT + "/#/mobile/pms/dashboard", menuItem.getGroupId());
                                startWebView(CWUrls.DOMAIN_WEB_BRIT + "/#/mobile/pms/launch-filter-settings", menuItem.getGroupId());
                            }
                        }, 200);

                        return true;

                    case 21:
                        drawerLayout.closeDrawers();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                webView.clearHistory();
                                startWebView(CWUrls.DOMAIN_WEB_BRIT + "/#/mobile/pms/dashboard", menuItem.getGroupId());
                                startWebView(CWUrls.DOMAIN_WEB_BRIT + "/#/mobile/pms/employee-annual-appraisal", menuItem.getGroupId());
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
                if (Util.isOnline(PMS_Dashboard.this)) {
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

                            Toast.makeText(PMS_Dashboard.this, "No internet connection.Try later", Toast.LENGTH_SHORT).show();
                        }
                    }, 200);
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();

        } else {
            super.onBackPressed();
        }

    }

    public void logout() {
        LayoutInflater li = LayoutInflater.from(this);
        final View promptsView = li.inflate(R.layout.logout_dialog, null);

        drawerLayout.closeDrawers();
        final AlertDialog.Builder dialog = new AlertDialog.Builder(PMS_Dashboard.this);
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

        NetworkConnector connect = new NetworkConnector(PMS_Dashboard.this, NetworkConnector.TYPE_POST, CWUrls.LOG_OUT, headers, null, PMS_Dashboard.this);
        if (connect.isAllowed()) {
            connect.execute();
        } else {
            Toast.makeText(PMS_Dashboard.this, getString(R.string.action_not_allowed), Toast.LENGTH_SHORT).show();
        }

    }

    private void startWebView(String url, int groupID) {

        //Create new webview Client to show progress dialog
        //When opening a url or click on link

        if (progressDialog == null) {
            // in standard case YourActivity.this
            progressDialog = new ProgressDialog(PMS_Dashboard.this);
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

                String cookies = CookieManager.getInstance().getCookie(url).toString();

                if (url.contains(CWUrls.DOMAIN_WEB_BRIT + "/#/mobile/pms/mass-pms-approvals")) {
                    title.setText("Approvals");
                } else if (url.contains(CWUrls.DOMAIN_WEB_BRIT + "/#/mobile/pms/appraisal-dashboard")) {
                    title.setText("Appraisals");
                } else if (url.contains(CWUrls.DOMAIN_WEB_BRIT + "/#/mobile/pms/template-dashboard")) {
                    title.setText("Templates");
                } else if (url.contains(CWUrls.DOMAIN_WEB_BRIT + "/#/mobile/pms/criterions-dashboard")) {
                    title.setText("Criterions");
                } else if (url.contains(CWUrls.DOMAIN_WEB_BRIT + "/#/mobile/pms/employee-annual-appraisal")) {
                    title.setText("Appraisals");
                } else if (url.contains(CWUrls.DOMAIN_WEB_BRIT + "/#/mobile/pms/active-appraisal-list")) {
                    title.setText("Employee Status Report");
                } else if (url.contains(CWUrls.DOMAIN_WEB_BRIT + "/#/mobile/pms/scale-settings")) {
                    title.setText("Scale settings");
                } else if (url.contains(CWUrls.DOMAIN_WEB_BRIT + "/#/mobile/pms/launch-filter-settings")) {
                    title.setText("Launch filter settings");
                } else if (url.contains(CWUrls.DOMAIN_WEB_BRIT + "/#/mobile/pms/dashboard")) {
                    title.setText("PMS Dashboard");
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

        if (mRolesList.size() > 0) {
            roleName = mRolesList.get(groupID).getRole_name();
            roleRegion = mRolesList.get(groupID).getRole_region();
            if (mRolesList.get(groupID).getRole_hr_function() == null || mRolesList.get(groupID).getRole_hr_function().isEmpty() || mRolesList.get(groupID).getRole_hr_function().equalsIgnoreCase("null")) {
                hrFunction = "undefined";
            } else {
                hrFunction = mRolesList.get(groupID).getRole_hr_function();
            }

        } else {
            roleName = CWIncturePreferences.getRole();
            roleRegion = CWIncturePreferences.getRegion();
            hrFunction = CWIncturePreferences.getHrFunction();
        }


        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setJavaScriptEnabled(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true);
        }


        CookieManager.getInstance().setCookie(CWUrls.DOMAIN_WEB_BRIT, "token=" + CWIncturePreferences.getAccessToken());
        CookieManager.getInstance().setCookie(CWUrls.DOMAIN_WEB_BRIT, "email=" + CWIncturePreferences.getEmail());
        CookieManager.getInstance().setCookie(CWUrls.DOMAIN_WEB_BRIT, "roleName=" + roleName);
        CookieManager.getInstance().setCookie(CWUrls.DOMAIN_WEB_BRIT, "myid=" + CWIncturePreferences.getUserId());
        CookieManager.getInstance().setCookie(CWUrls.DOMAIN_WEB_BRIT, "regionName=" + roleRegion);
        CookieManager.getInstance().setCookie(CWUrls.DOMAIN_WEB_BRIT, "cherrySelected=" + cherrySelected);
        CookieManager.getInstance().setCookie(CWUrls.DOMAIN_WEB_BRIT, "userhrFunction=" + hrFunction);

        Log.d("hrfucntion=", "" + hrFunction);
        Log.d("hrfucntion=", "" + CWIncturePreferences.getAccessToken());
        Log.d("hrfucntion=", "" + CWIncturePreferences.getEmail());
        Log.d("hrfucntion=", "" + CWIncturePreferences.getUserId());
        Log.d("hrfucntion=", "" + roleRegion);
        Log.d("hrfucntion=", "" + roleName);


        CookieSyncManager.getInstance().sync();

        webView.loadUrl(url);

        Log.d("urlloaded=", "" + webView.getUrl());

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
                util.logoutfunc(PMS_Dashboard.this);

            } else if (message != null && !message.isEmpty()) {
                Toast.makeText(PMS_Dashboard.this, message, Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void configurationUpdated(boolean configUpdated) {

    }
}
