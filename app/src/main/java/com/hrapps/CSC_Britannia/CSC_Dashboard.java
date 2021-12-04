package com.hrapps.CSC_Britannia;

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
 * Created by harshu on 8/1/2016.
 */
public class CSC_Dashboard extends BasicActivity implements AsyncResponse {

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
    String cherrySelected = "";

    String roleName = "";
    String roleRegion = "";

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
        cherrySelected = getIntent().getStringExtra("cherrySelected");
        Log.d("roleslist=", "" + mRolesList.toString());

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
        title.setText("CSC Dashboard");

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
                        Intent userProfile = new Intent(CSC_Dashboard.this, UserProfileActivity.class);
                        startActivity(userProfile);
                    }
                }, 200);

            }
        });

        startWebView(CWUrls.DOMAIN_WEB_BRIT + "/#/mobile/csc/dashboard", 0);


        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();

            }
        });

        final Menu menu = navigationView.getMenu();


//        roles_list = db.getRolesList(CSC_Dashboard.this);

        String role = "";
        if (mRolesList.size() == 1) {
            role = mRolesList.get(0).getRole_name();
            final SubMenu subMenu = menu.addSubMenu("Dashboard");

            if (!role.equalsIgnoreCase("CSCMGR")) {
                subMenu.add(Menu.NONE, 14, Menu.NONE, "    " + "Appraisals").setCheckable(true).setChecked(false);
            }

            switch (role.toUpperCase()) {
                case "VPSA":
                case "VPHR":
                    menu.add(0, 11, Menu.NONE, "Approvals");
                    subMenu.add(Menu.NONE, 15, Menu.NONE, "    " + "Reports").setCheckable(true).setChecked(false);
                    break;
                case "SDM1":
                    subMenu.add(Menu.NONE, 12, Menu.NONE, "    " + "Templates").setCheckable(true).setChecked(false);
                    subMenu.add(Menu.NONE, 13, Menu.NONE, "    " + "Criterions").setCheckable(true).setChecked(false);
                case "SDM2":
                case "RSM":
                case "SA":
                case "HRO":
                case "RHRM":
                case "CSCMGR":
                case "HROPSM":
                case "HRBP-S":
                case "NSDM":
                    subMenu.add(Menu.NONE, 15, Menu.NONE, "    " + "Reports").setCheckable(true).setChecked(false);
                    break;

            }


        } else {
            for (int i = 0; i < mRolesList.size(); i++) {

                final SubMenu subMenu = menu.addSubMenu(mRolesList.get(i).getRole_name() + " - " + mRolesList.get(i).getRole_region());
                role = mRolesList.get(i).getRole_name();

                if (!role.equalsIgnoreCase("CSCMGR")) {
                    subMenu.add(i, 14, i, "    " + "Appraisals").setCheckable(true).setChecked(false);
                }

                switch (role.toUpperCase()) {
                    case "SDM1":
                        subMenu.add(i, 12, i, "    " + "Templates").setCheckable(true).setChecked(false);
                        subMenu.add(i, 13, i, "    " + "Criterions").setCheckable(true).setChecked(false);
                    case "SDM2":
                    case "RSM":
                    case "SA":
                    case "VPSA":
                    case "VPHR":
                    case "HRO":
                    case "RHRM":
                    case "CSCMGR":
                    case "HROPSM":
                    case "HRBP-S":
                    case "NSDM":
                        subMenu.add(i, 15, i, "    " + "Reports").setCheckable(true).setChecked(false);
                        break;
                    case "TSI":
                        subMenu.add(i, 16, i, "    " + "Score Card").setCheckable(true).setChecked(false);
                        break;

                }
            }
            if (role.equalsIgnoreCase("VPSA") || role.equalsIgnoreCase("VPHR")) {
                menu.add(0, 11, 0, "Approvals");
            }
            menu.add(0, 0, Menu.NONE, "");
        }


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
                                startWebView(CWUrls.DOMAIN_WEB_BRIT + "/#/mobile/csc/dashboard", menuItem.getGroupId());
                                startWebView(CWUrls.DOMAIN_WEB_BRIT + "/#/mobile/csc/mass-csc-approvals", menuItem.getGroupId());
                            }
                        }, 200);

                        return true;
                    case 12:
                        if (Util.isOnline(CSC_Dashboard.this)) {
                            drawerLayout.closeDrawers();
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    webView.clearHistory();
                                    startWebView(CWUrls.DOMAIN_WEB_BRIT + "/#/mobile/csc/dashboard", menuItem.getGroupId());
                                    startWebView(CWUrls.DOMAIN_WEB_BRIT + "/#/mobile/csc/template-dashboard", menuItem.getGroupId());

                                }
                            }, 200);

                        } else {
                            drawerLayout.closeDrawers();
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(CSC_Dashboard.this, "No internet Connection", Toast.LENGTH_SHORT).show();
                                }
                            }, 200);
                        }
                        return true;
                    case 13:
                        drawerLayout.closeDrawers();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                webView.clearHistory();
                                startWebView(CWUrls.DOMAIN_WEB_BRIT + "/#/mobile/csc/dashboard", menuItem.getGroupId());
                                startWebView(CWUrls.DOMAIN_WEB_BRIT + "/#/mobile/csc/criterions-dashboard", menuItem.getGroupId());

                            }
                        }, 200);

                        return true;

                    case 14:
                        drawerLayout.closeDrawers();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Log.d("menuinfo=", "" + menuItem.getGroupId());
                                Log.d("menuinfo1=", "" + menuItem.getTitleCondensed().toString());
                                webView.clearHistory();
                                startWebView(CWUrls.DOMAIN_WEB_BRIT + "/#/mobile/csc/dashboard", menuItem.getGroupId());
                                startWebView(CWUrls.DOMAIN_WEB_BRIT + "/#/mobile/csc/appraisal-dashboard", menuItem.getGroupId());
                            }
                        }, 200);

                        return true;

                    //REPORTS
                    case 15:
                        drawerLayout.closeDrawers();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                webView.clearHistory();
                                startWebView(CWUrls.DOMAIN_WEB_BRIT + "/#/mobile/csc/dashboard", menuItem.getGroupId());
                                startWebView(CWUrls.DOMAIN_WEB_BRIT + "/#/mobile/cscReports/closed-appraisals", menuItem.getGroupId());
                                Log.d("groupid=", "" + menuItem.getGroupId());
                            }
                        }, 200);

                        return true;
                    //SCORECARD
                    case 16:
                        drawerLayout.closeDrawers();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                webView.clearHistory();
                                startWebView(CWUrls.DOMAIN_WEB_BRIT + "/#/mobile/csc/dashboard", menuItem.getGroupId());
                                startWebView(CWUrls.DOMAIN_WEB_BRIT + "/#/mobile/csc/tsi-scorecard", menuItem.getGroupId());
                                Log.d("groupid=", "" + menuItem.getGroupId());
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
                if (Util.isOnline(CSC_Dashboard.this)) {
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

                            Toast.makeText(CSC_Dashboard.this, "No internet connection.Try later", Toast.LENGTH_SHORT).show();
                        }
                    }, 200);
                }
            }
        });

    }

    //change logout in all the dashboards

    public void logout() {
        LayoutInflater li = LayoutInflater.from(this);
        final View promptsView = li.inflate(R.layout.logout_dialog, null);

        drawerLayout.closeDrawers();
        final AlertDialog.Builder dialog = new AlertDialog.Builder(CSC_Dashboard.this);
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

        NetworkConnector connect = new NetworkConnector(CSC_Dashboard.this, NetworkConnector.TYPE_POST, CWUrls.LOG_OUT, headers, null, CSC_Dashboard.this);
        if (connect.isAllowed()) {
            connect.execute();
        } else {
            Toast.makeText(CSC_Dashboard.this, getString(R.string.action_not_allowed), Toast.LENGTH_SHORT).show();
        }

    }


    private void startWebView(String url, int groupID) {

        //Create new webview Client to show progress dialog
        //When opening a url or click on link

        if (progressDialog == null) {
            // in standard case YourActivity.this
            progressDialog = new ProgressDialog(CSC_Dashboard.this);
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

                    if (url.contains(CWUrls.DOMAIN_WEB_BRIT + "/#/mobile/csc/template-dashboard")) {
                        title.setText("Templates");
                    } else if (url.contains(CWUrls.DOMAIN_WEB_BRIT + "/#/mobile/csc/criterions-dashboard")) {
                        title.setText("Criterions");
                    } else if (url.contains(CWUrls.DOMAIN_WEB_BRIT + "/#/mobile/csc/tsi-scorecard")) {
                        title.setText("Score Card");
                    } else if (url.contains(CWUrls.DOMAIN_WEB_BRIT + "/#/mobile/csc/appraisal-dashboard")) {
                        title.setText("Appraisals");
                    } else if (url.contains(CWUrls.DOMAIN_WEB_BRIT + "/#/mobile/csc/dashboard")) {
                        title.setText("CSC Dashboard");
                    } else if (url.contains(CWUrls.DOMAIN_WEB_BRIT + "/#/mobile/cscReports/closed-appraisals")) {
                        title.setText("Reports");
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

        if (mRolesList.size() > 0) {
            roleName = mRolesList.get(groupID).getRole_name();
            roleRegion = mRolesList.get(groupID).getRole_region();
        } else {
            roleName = CWIncturePreferences.getRole();
            roleRegion = CWIncturePreferences.getRegion();
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
        CookieManager.getInstance().setCookie(CWUrls.DOMAIN_WEB_BRIT, "regionName=" + roleRegion);
        CookieManager.getInstance().setCookie(CWUrls.DOMAIN_WEB_BRIT, "cherrySelected=" + cherrySelected);


        Log.d("1token=", "" + CWIncturePreferences.getAccessToken());
        Log.d("1email=", "" + CWIncturePreferences.getEmail());
        Log.d("1roles=", "" + CWIncturePreferences.getRoleRegion());
        Log.d("1myid=", "" + CWIncturePreferences.getUserId());

        CookieSyncManager.getInstance().sync();

        webView.loadUrl(url);

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
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
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
                util.logoutfunc(CSC_Dashboard.this);


            } else if (message != null && !message.isEmpty()) {
                Toast.makeText(CSC_Dashboard.this, message, Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void configurationUpdated(boolean configUpdated) {

    }
}
