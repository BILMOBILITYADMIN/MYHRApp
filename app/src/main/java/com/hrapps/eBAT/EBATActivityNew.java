package com.hrapps.eBAT;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
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
import android.webkit.DownloadListener;
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

import Model.RolesModel;
import Utility.AsyncResponse;
import Utility.CWIncturePreferences;
import Utility.CWUrls;
import Utility.Constants;
import Utility.NetworkConnector;
import Utility.ProfileImageView;
import Utility.Util;

public class EBATActivityNew extends BasicActivity implements AsyncResponse {

    private Context context;
    private WebView webView;
    private ProgressDialog progressDialog;
    private DrawerLayout drawerLayout;
    private TextView title;
    private ArrayList<RolesModel> mRolesList = new ArrayList<>();

    private boolean bRecheck = false;
    private boolean aRecheck = false;
    private boolean bIsManager = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.britannia_dashboard);

        context = EBATActivityNew.this;

        webView = findViewById(R.id.webview);
        drawerLayout = findViewById(R.id.drawer_layout);
        title = findViewById(R.id.title);

        NavigationView navigationView = findViewById(R.id.navigation_view);
        ImageView side_menu = findViewById(R.id.menu);
        ImageView home = findViewById(R.id.home);
        ImageView iback = findViewById(R.id.iback);
        ImageView back = findViewById(R.id.back);
        TextView name = findViewById(R.id.drawer_header_profile_name);
        TextView email = findViewById(R.id.drawer_header_profile_email);
        ProfileImageView img = findViewById(R.id.drawer_header_profile_pic);
        ImageView profileBg = findViewById(R.id.backgroundimage1);
        Button logout = findViewById(R.id.logout1);
        TextView tv_app_version = findViewById(R.id.tv_version);

        try {
            tv_app_version.setText(("App Version " + getPackageManager().getPackageInfo(getPackageName(), 0).versionName));
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        mRolesList = (ArrayList<RolesModel>) getIntent().getSerializableExtra("roles");

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        iback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.closeDrawers();
            }
        });

        name.setText((CWIncturePreferences.getFirstname() + CWIncturePreferences.getLastname()));
        email.setText(CWIncturePreferences.getEmail());

        if (CWIncturePreferences.getAvatarurl() != null && !CWIncturePreferences.getAvatarurl().isEmpty()) {
            Util.loadAttachmentImageSquareForProfile(this, CWIncturePreferences.getAvatarurl(), img);
            Util.loadAttachmentImageLarge(this, CWIncturePreferences.getAvatarurl(), profileBg);
        }
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.closeDrawer(Gravity.LEFT);
                // To avoid lag
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent userProfile = new Intent(context, UserProfileActivity.class);
                        startActivity(userProfile);
                    }
                }, 200);

            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Util.isOnline(context)) {
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

                            Toast.makeText(context, R.string.no_internet, Toast.LENGTH_SHORT).show();
                        }
                    }, 200);
                }
            }
        });
        final Menu menu = navigationView.getMenu();

        if (mRolesList != null) {
            if (mRolesList.size() > 0) {
                for (int i = 0; i < mRolesList.size(); i++) {
                    RolesModel currentRole = mRolesList.get(i);
                    String role = currentRole.getRole_name();

                    if (role.equalsIgnoreCase(EBATActivityNew.ROLES.MANAGER))
                        bIsManager = true;

                    switch (role.toUpperCase()) {
                        case EBATActivityNew.ROLES.ADMIN: {
                            final SubMenu subMenu = addSubMenu(currentRole, menu);
                            subMenu.add(i, MenuId.E_BAT_ADMIN, i, "    " + getString(R.string.eBat)).setCheckable(true).setChecked(false);
                        }
                        break;
                        case EBATActivityNew.ROLES.HRBP: {
                            final SubMenu subMenu = addSubMenu(currentRole, menu);
                            subMenu.add(i, MenuId.E_BAT_HRBP, i, "    " + getString(R.string.eBat)).setCheckable(true).setChecked(false);
                            subMenu.add(i, MenuId.HRBP_ARF, i, "    " + getString(R.string.additional_reviewer_feedback)).setCheckable(true).setChecked(false);
                        }
                        break;
                        case EBATActivityNew.ROLES.EMPLOYEE: {
                            final SubMenu subMenu = addSubMenu(currentRole, menu);
                            subMenu.add(i, MenuId.E_BAT_EMP, i, "    " + getString(R.string.eBat)).setCheckable(true).setChecked(false);
                            subMenu.add(i, MenuId.E_BAT_PH, i, "    " + getString(R.string.performance_history)).setCheckable(true).setChecked(false);
                            subMenu.add(i, MenuId.EMP_ARF, i, "    " + getString(R.string.additional_reviewer_feedback)).setCheckable(true).setChecked(false);
                        }
                        break;
                    }
                }
            }
        }
        dcWebView(CWUrls.DOMAIN_WEB_BRIT + DC_URLS.EMP_EBAT + DC_URLS.EMP_EBAT_SA, 0);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(final MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case MenuId.E_BAT_EMP:
                        drawerLayout.closeDrawers();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                webView.clearHistory();
                                dcWebView(CWUrls.DOMAIN_WEB_BRIT + DC_URLS.EMP_EBAT + DC_URLS.EMP_EBAT_SA, menuItem.getGroupId());
                            }
                        }, 200);

                        break;
                    case MenuId.E_BAT_PH:
                        drawerLayout.closeDrawers();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                webView.clearHistory();
                                dcWebView(CWUrls.DOMAIN_WEB_BRIT + DC_URLS.EMP_PH + DC_URLS.EMP_EBAT_SA, menuItem.getGroupId());
                            }
                        }, 200);
                        break;

                    case MenuId.EMP_ARF:
                        drawerLayout.closeDrawers();
                        if (aRecheck)
                            dcWebView(CWUrls.DOMAIN_WEB_BRIT + DC_URLS.EMP_EBAT + DC_URLS.EMP_EBAT_SA, menuItem.getGroupId());
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                webView.clearHistory();
                                dcWebView(CWUrls.DOMAIN_WEB_BRIT + DC_URLS.EMP_ARF, menuItem.getGroupId());
                            }
                        }, 200);
                        break;
                    case MenuId.HRBP_ARF:
                        drawerLayout.closeDrawers();
                        if (aRecheck)
                            dcWebView(CWUrls.DOMAIN_WEB_BRIT + DC_URLS.EMP_EBAT + DC_URLS.EMP_EBAT_SA, menuItem.getGroupId());
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                webView.clearHistory();
                                dcWebView(CWUrls.DOMAIN_WEB_BRIT + DC_URLS.EMP_ARF, menuItem.getGroupId());
                            }
                        }, 200);
                        break;

                    case MenuId.E_BAT_ADMIN:
                        drawerLayout.closeDrawers();
                        if (bRecheck)
                            dcWebView(CWUrls.DOMAIN_WEB_BRIT + DC_URLS.EMP_EBAT + DC_URLS.EMP_EBAT_SA, menuItem.getGroupId());
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                webView.clearHistory();
                                dcWebView(CWUrls.DOMAIN_WEB_BRIT + DC_URLS.HRBP_EBAT, menuItem.getGroupId());
                            }
                        }, 200);

                        break;
                    case MenuId.E_BAT_HRBP:
                        drawerLayout.closeDrawers();
                        if (bRecheck)
                            dcWebView(CWUrls.DOMAIN_WEB_BRIT + DC_URLS.EMP_EBAT + DC_URLS.EMP_EBAT_SA, menuItem.getGroupId());
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                webView.clearHistory();
                                dcWebView(CWUrls.DOMAIN_WEB_BRIT + DC_URLS.HRBP_EBAT, menuItem.getGroupId());
                            }
                        }, 200);
                        break;

                    default:
                        break;
                }

                // Since both have the same URL,
                // the webView doesn't detect that the page has to be reloaded
                // (with different role cookies)
                // Hence check is added to ensure page reload
                bRecheck = (menuItem.getItemId() == MenuId.E_BAT_HRBP || menuItem.getItemId() == MenuId.E_BAT_ADMIN);
                bRecheck = (menuItem.getItemId() == MenuId.EMP_ARF || menuItem.getItemId() == MenuId.HRBP_ARF);
                return true;
            }
        });

        side_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(Gravity.LEFT);
            }
        });

        webView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String s1, String s2, String s3, long l) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });
    }// end of onCreate()

    private SubMenu addSubMenu(RolesModel currentRole, Menu menu) {
        String roleName = currentRole.getRole_name();
        if (currentRole.getRole_region().equalsIgnoreCase("null") || currentRole.getRole_region().isEmpty()) {
            return menu.addSubMenu(capitalizeFirstLetter(roleName) + " - " + currentRole.getRole_hr_function());
        } else if (currentRole.getRole_hr_function().equalsIgnoreCase("null") || currentRole.getRole_hr_function().isEmpty()) {
            return menu.addSubMenu(capitalizeFirstLetter(roleName) + " - " + currentRole.getRole_region());
        } else {
            return menu.addSubMenu(capitalizeFirstLetter(roleName) + " - " + currentRole.getRole_region() + " - " + currentRole.getRole_region());
        }
    }

    private void dcWebView(String url, int groupID) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("Loading...");
            progressDialog.setCancelable(true);
            progressDialog.show();
        }

        webView.setWebViewClient(new WebViewClient() {
            //If you will not use this method url links are open in new browser not in webView
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onLoadResource(WebView view, String url) {
                if (url.contains(Constants.DOWNLOAD_PDF)) {
                    downloadPdf(url);
                } else super.onLoadResource(view, url);
            }

            //Show loader on url load
            public void onPageFinished(WebView view, String url) {
                if (url.equals(CWUrls.DOMAIN_WEB_BRIT + DC_URLS.EMP_EBAT)) { // Incorrect rendering onBackPress()
                    dcWebView(CWUrls.DOMAIN_WEB_BRIT + DC_URLS.EMP_EBAT + DC_URLS.EMP_EBAT_SA, 0);
                } else if (url.contains(DC_URLS.EMP_EBAT)) {
                    setTitle(R.string.eBat);
                } else if (url.contains(DC_URLS.EMP_PH)) {
                    setTitle(R.string.performance_history);
                } else if (url.contains(DC_URLS.EMP_ARF)) {
                    setTitle(R.string.additional_reviewer_feedback);
                } else if (url.contains(DC_URLS.HRBP_EBAT)) {
                    setTitle(R.string.dashboard);
                } else if (url.contains(CWUrls.BASE_URL_CSC)) {
                    setTitle("");
                }
            }

            private void setTitle(String sTitle) {
                title.setText(sTitle);
            }

            private void setTitle(int resTitle) {
                setTitle(getResources().getString(resTitle));
            }
        });

        String roleName, roleRegion;

        if (mRolesList != null) {
            if (mRolesList.size() > 0) {
                roleName = mRolesList.get(groupID).getRole_name();
                roleRegion = mRolesList.get(groupID).getRole_region();
            } else {
                roleName = CWIncturePreferences.getRole();
                roleRegion = CWIncturePreferences.getRegion();
            }
        } else {
            roleName = CWIncturePreferences.getRole();
            roleRegion = CWIncturePreferences.getRegion();
        }
        webView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, final int progress) {
                if (progress == 100 && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
            }
        });

        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setAllowFileAccess(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true);
        }


        CookieManager.getInstance().setCookie(CWUrls.COOKIE_DOMAIN_BRIT, "email=" + CWIncturePreferences.getEmail());
        CookieManager.getInstance().setCookie(CWUrls.COOKIE_DOMAIN_BRIT, "token=" + CWIncturePreferences.getAccessToken());
        CookieManager.getInstance().setCookie(CWUrls.COOKIE_DOMAIN_BRIT, "id=0" + CWIncturePreferences.getPOSITION());
        CookieManager.getInstance().setCookie(CWUrls.COOKIE_DOMAIN_BRIT, "name=" + CWIncturePreferences.getFirstname() + CWIncturePreferences.getLastname());
        CookieManager.getInstance().setCookie(CWUrls.COOKIE_DOMAIN_BRIT, "roleName=" + roleName);
        CookieManager.getInstance().setCookie(CWUrls.COOKIE_DOMAIN_BRIT, "regionName=" + roleRegion);
        CookieManager.getInstance().setCookie(CWUrls.COOKIE_DOMAIN_BRIT, "isManager=" + bIsManager);
        CookieManager.getInstance().setCookie(CWUrls.COOKIE_DOMAIN_BRIT, "adminUSER=" + (roleName.equalsIgnoreCase("Admin") ? "true" : "false"));
        CookieManager.getInstance().setCookie(CWUrls.COOKIE_DOMAIN_BRIT, "adminUSERLoginId=" + CWIncturePreferences.getUserId());
        CookieManager.getInstance().setCookie(CWUrls.COOKIE_DOMAIN_BRIT, "matrixManager=" + CWIncturePreferences.isMatrixManager());

        Log.d("2token=", "" + CWIncturePreferences.getAccessToken());
        Log.d("2email=", "" + CWIncturePreferences.getEmail());
        Log.d("email=", "" + CWIncturePreferences.getPOSITION());
        Log.d("role=", "" + CWIncturePreferences.getRole());

        String loginModel = CWIncturePreferences.getLoginModelData();
        JSONObject managerObj = null;

        try {
            managerObj = new JSONObject(loginModel);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (managerObj != null) {
            CookieManager.getInstance().setCookie(CWUrls.COOKIE_DOMAIN_BRIT, "manager=" + managerObj.toString());
        }

        CookieSyncManager.getInstance().sync();

        webView.loadUrl(url);
    }

    private void downloadPdf(String url) {
        if (Util.isOnline(context)) {
            Map<String, String> headers = new HashMap<>();

            headers.put("x-email-id", CWIncturePreferences.getEmail());
            headers.put("x-access-token", CWIncturePreferences.getAccessToken());

            try {
                headers.put("android-version", getPackageManager().getPackageInfo(getPackageName(), 0).versionName);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }

            NetworkConnector connect = new NetworkConnector(context, NetworkConnector.TYPE_GET, url, headers, null, EBATActivityNew.this);
            if (connect.isAllowed()) {
                connect.execute();
            } else {
                Toast.makeText(context, getString(R.string.action_not_allowed), Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(context, getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
        }
    }

    private void logout() {
        LayoutInflater li = LayoutInflater.from(this);
        final View promptsView = li.inflate(R.layout.logout_dialog, null);

        drawerLayout.closeDrawers();
        final AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setView(promptsView);

        TextView confirm_logout = promptsView.findViewById(R.id.confirm_logout);
        TextView cancel_logout = promptsView.findViewById(R.id.cancel_logout);

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
        Map<String, String> headers = new HashMap<>();

        headers.put("x-access-token", CWIncturePreferences.getAccessToken());
        headers.put("x-email-id", CWIncturePreferences.getEmail());

        try {
            headers.put("android-version", getPackageManager().getPackageInfo(getPackageName(), 0).versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        NetworkConnector connect = new NetworkConnector(context, NetworkConnector.TYPE_POST, CWUrls.LOG_OUT, headers, null, EBATActivityNew.this);
        if (connect.isAllowed()) {
            connect.execute();
        } else {
            Toast.makeText(context, getString(R.string.action_not_allowed), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void processFinish(String output, int status_code, String url, int type) {
        if (url.contains(Constants.DOWNLOAD_PDF)) {
            try {
                JSONObject response = new JSONObject(output);

                if (response.has("data")) {
                    String path = response.optString("data");

                    dcWebView(CWUrls.BASE_URL_CSC + path, 0);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (url.contains(CWUrls.LOG_OUT)) {
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

            if (status.equalsIgnoreCase("success")) {
                Util util = new Util();
                util.logoutfunc(context);
            } else if (message != null && !message.isEmpty()) {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            }
        }
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
    public void configurationUpdated(boolean configUpdated) {
    }

    private String capitalizeFirstLetter(String word) {
        return word.substring(0, 1).toUpperCase() + word.substring(1);
    }

    interface DC_URLS {
        String EMP_EBAT = "/#/mobile/kra/target-approval?";
        String EMP_PH = "/#/mobile/cherry/kra/performance-history";
        String EMP_ARF = "/#/mobile/kra/feedback-request";
        String HRBP_EBAT = "/#/mobile/kra/dashboard";
        String EMP_EBAT_SA = "?activetab={\"name\":\"Self Assessment\",\"active\":true,\"id\":\"tab0\",\"emp\":false}&tabsOpen=[{\"name\":\"Self Assessment\",\"active\":true,\"id\":\"tab0\",\"emp\":false},{\"name\":\"Team Assessment\",\"active\":false,\"id\":\"tab1\",\"emp\":false}]&code=Self Assessment";
        String EMP_EBAT_TA = "activetab={\"name\":\"Self Assessment\",\"active\":true,\"id\":\"tab0\",\"emp\":false}&tabsOpen=[{\"name\":\"Self Assessment\",\"active\":true,\"id\":\"tab0\",\"emp\":false},{\"name\":\"Team Assessment\",\"active\":false,\"id\":\"tab1\",\"emp\":false}]&code=Self Assessment";

    }

    interface MenuId {
        int E_BAT_EMP = 0;
        int E_BAT_ADMIN = 2;
        int E_BAT_HRBP = 4;
        int E_BAT_PH = 1;
        int EMP_ARF = 5;
        int HRBP_ARF = 3;
    }

    interface ROLES {
        String EMPLOYEE = "EMPLOYEE";
        String HRBP = "HRBP";
        String ADMIN = "ADMIN";
        String MANAGER = "MANAGER";
    }
}
