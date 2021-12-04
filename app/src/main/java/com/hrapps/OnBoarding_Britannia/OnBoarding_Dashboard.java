package com.hrapps.OnBoarding_Britannia;

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
 * Created by harshu on 3/16/2017.
 */

public class OnBoarding_Dashboard extends BasicActivity implements AsyncResponse {

    WebView webView;
    TextView title, no_data;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    Button logout;
    TextView name, email;
    ImageView side_menu, back, profileBg, home;
    ProfileImageView img;
    private ProgressDialog progressDialog;
    String url = "";
    String cherrySelected = "";

    String roleName = "";
    String roleRregion = "";

    DbUtil db;
    ArrayList<RolesModel> mRolesList = new ArrayList<>();
    boolean checked = false;


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
        title.setText("OnBoarding Dashboard");


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

        final Menu menu = navigationView.getMenu();

        String role = "";
        if (mRolesList.size() == 1) {
            role = mRolesList.get(0).getRole_name();
            final SubMenu subMenu = menu.addSubMenu("");

            switch (role.toUpperCase()) {
                case "CANDIDATE-DASHBOARD":
                case "ADMIN":
                case "HRBP":
                case "itTeam":
                    subMenu.add(Menu.NONE, 1, Menu.NONE, "    " + "Dashboard").setCheckable(true).setChecked(false);
                    break;
                case "OBTEAM":
                case "OBADMIN":
                    subMenu.add(Menu.NONE, 1, Menu.NONE, "    " + "Dashboard").setCheckable(true).setChecked(false);
                    subMenu.add(Menu.NONE, 2, Menu.NONE, "    " + "Onboarding-SLA").setCheckable(true).setChecked(false);
                    subMenu.add(Menu.NONE, 3, Menu.NONE, "    " + "Candidate-List").setCheckable(true).setChecked(false);
                    subMenu.add(Menu.NONE, 4, Menu.NONE, "    " + "Hire Joiner").setCheckable(true).setChecked(false);
                    break;


            }

        } else {
            for (int i = 0; i < mRolesList.size(); i++) {

                if (mRolesList.get(i).getRole_name().equalsIgnoreCase("CANDIDATE-DASHBOARD")
                        || mRolesList.get(i).getRole_name().equalsIgnoreCase("ADMIN")
                        || mRolesList.get(i).getRole_name().equalsIgnoreCase("IT")
                        || mRolesList.get(i).getRole_name().equalsIgnoreCase("HRBP-Team")
                        || mRolesList.get(i).getRole_name().equalsIgnoreCase("OBT") || mRolesList.get(i).getRole_name().equalsIgnoreCase("OBTEAM") || mRolesList.get(i).getRole_name().equalsIgnoreCase("OBADMIN")) {
                    final SubMenu subMenu = menu.addSubMenu(mRolesList.get(i).getRole_name() + " - " + mRolesList.get(i).getRole_region());
                    role = mRolesList.get(i).getRole_name();

                    switch (role.toUpperCase()) {
                        case "CANDIDATE-DASHBOARD":
                        case "ADMIN":
                        case "HRBP":
                        case "itTeam":
                            subMenu.add(i, 1, i, "    " + "Dashboard").setCheckable(true).setChecked(false);
                            break;
                        case "OBTEAM":
                        case "OBADMIN":
                            subMenu.add(i, 1, i, "    " + "Dashboard").setCheckable(true).setChecked(false);
                            subMenu.add(i, 2, i, "    " + "Onboarding-SLA").setCheckable(true).setChecked(false);
                            subMenu.add(i, 3, i, "    " + "Candidate-List").setCheckable(true).setChecked(false);
                            subMenu.add(i, 4, i, "    " + "Hire Joiner").setCheckable(true).setChecked(false);
                            break;
                    }
                }
            }
            final SubMenu subMenu = menu.addSubMenu(" ");
        }


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(final MenuItem menuItem) {

                checked = true;

                switch (menuItem.getItemId()) {
                    case 1:
                        drawerLayout.closeDrawers();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                webView.clearHistory();
                                startWebView(CWUrls.DOMAIN_WEB_BRIT + "/#/mobile/onboarding/onboarding-landing", menuItem.getGroupId());
                            }
                        }, 200);

                        return true;

                    case 2:
                        drawerLayout.closeDrawers();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                webView.clearHistory();
                                startWebView(CWUrls.DOMAIN_WEB_BRIT + "/#/mobile/onboarding/onboarding-landing", menuItem.getGroupId());
                                startWebView(CWUrls.DOMAIN_WEB_BRIT + "/#/mobile/onboarding/onboarding-SLA", menuItem.getGroupId());

                            }
                        }, 200);

                        return true;

                    case 3:
                        drawerLayout.closeDrawers();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                webView.clearHistory();
                                startWebView(CWUrls.DOMAIN_WEB_BRIT + "/#/mobile/onboarding/onboarding-landing", menuItem.getGroupId());
                                startWebView(CWUrls.DOMAIN_WEB_BRIT + "/#/mobile/onboarding/onboarding-allList", menuItem.getGroupId());
                            }
                        }, 200);

                        return true;

                    case 4:
                        drawerLayout.closeDrawers();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                webView.clearHistory();
                                startWebView(CWUrls.DOMAIN_WEB_BRIT + "/#/mobile/onboarding/onboarding-landing", menuItem.getGroupId());
                                startWebView(CWUrls.DOMAIN_WEB_BRIT + "/#/mobile/onboarding/onboarding-hire", menuItem.getGroupId());
                            }
                        }, 200);

                        return true;

                    default:
                        return true;
                }
            }
        });


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
                        Intent userProfile = new Intent(OnBoarding_Dashboard.this, UserProfileActivity.class);
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
            url = "/#/mobile/onboarding/onboarding-landing";
        }

        startWebView(CWUrls.DOMAIN_WEB_BRIT + url, 0);

        side_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(Gravity.LEFT);
            }
        });


        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Util.isOnline(OnBoarding_Dashboard.this)) {
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

                            Toast.makeText(OnBoarding_Dashboard.this, "No internet connection.Try later", Toast.LENGTH_SHORT).show();
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
        final AlertDialog.Builder dialog = new AlertDialog.Builder(OnBoarding_Dashboard.this);
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

        NetworkConnector connect = new NetworkConnector(OnBoarding_Dashboard.this, NetworkConnector.TYPE_POST, CWUrls.LOG_OUT, headers, null, OnBoarding_Dashboard.this);
        if (connect.isAllowed()) {
            connect.execute();
        } else {
            Toast.makeText(OnBoarding_Dashboard.this, getString(R.string.action_not_allowed), Toast.LENGTH_SHORT).show();
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
                util.logoutfunc(OnBoarding_Dashboard.this);

            } else if (message != null && !message.isEmpty()) {
                Toast.makeText(OnBoarding_Dashboard.this, message, Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void configurationUpdated(boolean configUpdated) {
    }

    private void startWebView(String url, int groupID) {

        //Create new webview Client to show progress dialog
        //When opening a url or click on link

        if (progressDialog == null) {
            // in standard case YourActivity.this
            progressDialog = new ProgressDialog(OnBoarding_Dashboard.this);
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
            roleRregion = mRolesList.get(groupID).getRole_region();
        } else {
            roleName = CWIncturePreferences.getRole();
            roleRregion = CWIncturePreferences.getRegion();
        }

        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setJavaScriptEnabled(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true);
        }


        CookieManager.getInstance().setCookie(CWUrls.DOMAIN_WEB_BRIT, "token=" + CWIncturePreferences.getAccessToken());
        CookieManager.getInstance().setCookie(CWUrls.DOMAIN_WEB_BRIT, "email=" + CWIncturePreferences.getEmail());
        CookieManager.getInstance().setCookie(CWUrls.DOMAIN_WEB_BRIT, "roleName =" + roleName);
        CookieManager.getInstance().setCookie(CWUrls.DOMAIN_WEB_BRIT, "myid=" + CWIncturePreferences.getUserId());
        CookieManager.getInstance().setCookie(CWUrls.DOMAIN_WEB_BRIT, "regionName=" + roleRregion);
        CookieManager.getInstance().setCookie(CWUrls.DOMAIN_WEB_BRIT, "cherrySelected=" + cherrySelected);

        CookieSyncManager.getInstance().sync();

        webView.loadUrl(url);

    }
}
