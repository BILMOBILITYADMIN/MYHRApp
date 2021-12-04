package com.hrapps.Recruitment_Britannia;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import DB.DbUtil;
import Model.ConfigModel;
import Model.RolesModel;
import Utility.AsyncResponse;
import Utility.CWIncturePreferences;
import Utility.CWUrls;
import Utility.Constants;
import Utility.NetworkConnector;
import Utility.ProfileImageView;
import Utility.Util;

/**
 * Created by harshu on 1/18/2017.
 */

public class Recruitment_Dashboard extends BasicActivity implements AsyncResponse {

    WebView webView;
    TextView title, no_data;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    Button logout;
    TextView name, email;
    ImageView side_menu, back, profileBg, home;
    ProfileImageView img;
    private ProgressDialog progressDialog;
    String cherrySelected = "";
    String hrFunction = "";
    String roleName = "";
    String roleRegion = "";
    String globalUrl = "";

    DbUtil db;
    ArrayList<RolesModel> mRolesList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.britannia_dashboard);


        img = (ProfileImageView) findViewById(R.id.drawer_header_profile_pic);
        name = (TextView) findViewById(R.id.drawer_header_profile_name);
        email = (TextView) findViewById(R.id.drawer_header_profile_email);
        profileBg = (ImageView) findViewById(R.id.backgroundimage1);
        side_menu = (ImageView) findViewById(R.id.menu);
        back = (ImageView) findViewById(R.id.back);
        home = (ImageView) findViewById(R.id.home);
        logout = (Button) findViewById(R.id.logout1);
        webView = (WebView) findViewById(R.id.webview);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        title = (TextView) findViewById(R.id.title);
        title.setText("Recruitment Dashboard");

        db = new DbUtil();
        mRolesList = (ArrayList<RolesModel>) getIntent().getSerializableExtra("roles");
        if (mRolesList != null) {
            Log.d("roleslist=", "" + mRolesList.toString());

        }
        cherrySelected = getIntent().getStringExtra("cherrySelected");

        no_data = (TextView) findViewById(R.id.no_data);

        if (mRolesList != null) {
            if (mRolesList.size() == 0) {
                no_data.setVisibility(View.VISIBLE);
                webView.setVisibility(View.GONE);
            } else {
                no_data.setVisibility(View.GONE);
                webView.setVisibility(View.VISIBLE);
            }
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
                        Intent userProfile = new Intent(Recruitment_Dashboard.this, UserProfileActivity.class);
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


        side_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(Gravity.LEFT);
            }
        });

        final Menu menu = navigationView.getMenu();
        String role = "";

        if (mRolesList != null) {
            if (mRolesList.size() > 0) {

                if (mRolesList.size() == 1) {
                    role = mRolesList.get(0).getRole_name();
                    final SubMenu subMenu = menu.addSubMenu("Dashboard");

                    switch (role.toUpperCase()) {
                        case "HRBP":
                        case "HEADHR":
                        case "RECRUITER":
                        case "COMPOFFICER":
                        case "C & B HEAD":
                            subMenu.add(0, 1, Menu.NONE, "Requisitions").setCheckable(true).setChecked(false);
                            subMenu.add(0, 2, Menu.NONE, "Offered").setCheckable(true).setChecked(false);
                            subMenu.add(0, 3, Menu.NONE, "Turn Around Time").setCheckable(true).setChecked(false);
                            subMenu.add(0, 4, Menu.NONE, "My Tasks").setCheckable(true).setChecked(false);
                            subMenu.add(0, 5, Menu.NONE, "IJP & ER").setCheckable(true).setChecked(false);
                            break;
                        case "EMPLOYEE":
                            subMenu.add(0, 10, Menu.NONE, "Requisitions").setCheckable(true).setChecked(false);
                            subMenu.add(0, 4, Menu.NONE, "My Tasks").setCheckable(true).setChecked(false);
                            subMenu.add(0, 5, Menu.NONE, "IJP & ER").setCheckable(true).setChecked(false);
                            break;
                        case "RECADMIN":
                            subMenu.add(0, 1, Menu.NONE, "Requisitions").setCheckable(true).setChecked(false);
                            subMenu.add(0, 2, Menu.NONE, "Offered").setCheckable(true).setChecked(false);
                            subMenu.add(0, 3, Menu.NONE, "Turn Around Time").setCheckable(true).setChecked(false);
                            subMenu.add(0, 6, Menu.NONE, "Candidates").setCheckable(true).setChecked(false);
                            subMenu.add(0, 7, Menu.NONE, "Proxy-User-Access").setCheckable(true).setChecked(false);
                            subMenu.add(0, 8, Menu.NONE, "Upload and delete JD").setCheckable(true).setChecked(false);
                            subMenu.add(0, 9, Menu.NONE, "Placement Agencies").setCheckable(true).setChecked(false);
                            break;


                    }

                } else {
                    for (int i = 0; i < mRolesList.size(); i++) {
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
                            case "HRBP":
                            case "HEADHR":
                            case "RECRUITER":
                            case "COMPOFFICER":
                            case "C & B HEAD":
                                subMenu.add(i, 1, i, "Requisitions").setCheckable(true).setChecked(false);
                                subMenu.add(i, 2, i, "Offered").setCheckable(true).setChecked(false);
                                subMenu.add(i, 3, i, "Turn Around Time").setCheckable(true).setChecked(false);
                                subMenu.add(i, 4, i, "My Tasks").setCheckable(true).setChecked(false);
                                subMenu.add(i, 5, i, "IJP & ER").setCheckable(true).setChecked(false);
                                break;
                            case "EMPLOYEE":
                                subMenu.add(i, 10, i, "Requisitions").setCheckable(true).setChecked(false);
                                subMenu.add(i, 4, i, "My Tasks").setCheckable(true).setChecked(false);
                                subMenu.add(i, 5, i, "IJP & ER").setCheckable(true).setChecked(false);
                                break;
                            case "RECADMIN":
                                subMenu.add(i, 1, i, "Requisitions").setCheckable(true).setChecked(false);
                                subMenu.add(i, 2, i, "Offered").setCheckable(true).setChecked(false);
                                subMenu.add(i, 3, i, "Turn Around Time").setCheckable(true).setChecked(false);
                                subMenu.add(i, 6, i, "Candidates").setCheckable(true).setChecked(false);
                                subMenu.add(i, 7, i, "Proxy-User-Access").setCheckable(true).setChecked(false);
                                subMenu.add(i, 8, i, "Upload and delete JD").setCheckable(true).setChecked(false);
                                subMenu.add(i, 9, i, "Placement Agencies").setCheckable(true).setChecked(false);
                                break;
                        }
                    }
                }
            }//end of roleslist size > 0

        } //end of rolesList!=null

        webView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String s1, String s2, String s3, long l) {

            }
        });

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(final MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    //requisitions
                    case 1:
                        drawerLayout.closeDrawers();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                webView.clearHistory();
                                startWebView(CWUrls.DOMAIN_WEB_BRIT + "/#/mobile/recruitment/recruitment-dashboard/NAVIGATION_Recruitment", menuItem.getGroupId());
                                startWebView(CWUrls.DOMAIN_WEB_BRIT + "/#/mobile/recruitment/recruitment-requisitions", menuItem.getGroupId());
                            }
                        }, 200);

                        return true;

                    //offered
                    case 2:
                        drawerLayout.closeDrawers();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                webView.clearHistory();
                                startWebView(CWUrls.DOMAIN_WEB_BRIT + "/#/mobile/recruitment/recruitment-dashboard/NAVIGATION_Recruitment", menuItem.getGroupId());
                                startWebView(CWUrls.DOMAIN_WEB_BRIT + "/#/mobile/recruitment/recruitment-offered", menuItem.getGroupId());
                            }
                        }, 200);

                        return true;

                    //tat
                    case 3:
                        drawerLayout.closeDrawers();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                webView.clearHistory();
                                startWebView(CWUrls.DOMAIN_WEB_BRIT + "/#/mobile/recruitment/recruitment-dashboard/NAVIGATION_Recruitment", menuItem.getGroupId());
                                startWebView(CWUrls.DOMAIN_WEB_BRIT + "/#/mobile/recruitment/recruitment-tat", menuItem.getGroupId());
                            }
                        }, 200);

                        return true;

                    //my tasks
                    case 4:
                        drawerLayout.closeDrawers();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                webView.clearHistory();
                                startWebView(CWUrls.DOMAIN_WEB_BRIT + "/#/mobile/recruitment/recruitment-dashboard/NAVIGATION_Recruitment", menuItem.getGroupId());
                                startWebView(CWUrls.DOMAIN_WEB_BRIT + "/#/mobile/recruitment/recruitment-task-index", menuItem.getGroupId());
                            }
                        }, 200);

                        return true;

                    //IJP
                    case 5:
                        drawerLayout.closeDrawers();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                webView.clearHistory();
                                startWebView(CWUrls.DOMAIN_WEB_BRIT + "/#/mobile/recruitment/recruitment-dashboard/NAVIGATION_Recruitment", menuItem.getGroupId());
                                startWebView(CWUrls.DOMAIN_WEB_BRIT + "/#/mobile/recruitment/IJP", menuItem.getGroupId());
                            }
                        }, 200);

                        return true;

                    //candidates
                    case 6:
                        drawerLayout.closeDrawers();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                webView.clearHistory();
                                startWebView(CWUrls.DOMAIN_WEB_BRIT + "/#/mobile/recruitment/recruitment-dashboard/NAVIGATION_Recruitment", menuItem.getGroupId());
                                startWebView(CWUrls.DOMAIN_WEB_BRIT + "/#/mobile/recruitment/candidates", menuItem.getGroupId());
                            }
                        }, 200);

                        return true;

                    //proxy-user-access
                    case 7:
                        drawerLayout.closeDrawers();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                webView.clearHistory();
                                startWebView(CWUrls.DOMAIN_WEB_BRIT + "/#/mobile/recruitment/recruitment-dashboard/NAVIGATION_Recruitment", menuItem.getGroupId());
                                startWebView(CWUrls.DOMAIN_WEB_BRIT + "/#/mobile/recruitment/proxyUserAccess", menuItem.getGroupId());
                            }
                        }, 200);

                        return true;

                    //upload and delete JD
                    case 8:
                        drawerLayout.closeDrawers();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                webView.clearHistory();
                                startWebView(CWUrls.DOMAIN_WEB_BRIT + "/#/mobile/recruitment/recruitment-dashboard/NAVIGATION_Recruitment", menuItem.getGroupId());
                                startWebView(CWUrls.DOMAIN_WEB_BRIT + "/#/mobile/recruitment/deletejd", menuItem.getGroupId());
                            }
                        }, 200);

                        return true;

                    //placement agencies
                    case 9:
                        drawerLayout.closeDrawers();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                webView.clearHistory();
                                startWebView(CWUrls.DOMAIN_WEB_BRIT + "/#/mobile/recruitment/recruitment-dashboard/NAVIGATION_Recruitment", menuItem.getGroupId());
                                startWebView(CWUrls.DOMAIN_WEB_BRIT + "/#/mobile/recruitment/listPlacementAgency", menuItem.getGroupId());
                            }
                        }, 200);

                        return true;

                    //employee requisitions
                    case 10:
                        drawerLayout.closeDrawers();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                webView.clearHistory();
                                startWebView(CWUrls.DOMAIN_WEB_BRIT + "/#/mobile/recruitment/recruitment-dashboard/NAVIGATION_Recruitment", menuItem.getGroupId());
                                startWebView(CWUrls.DOMAIN_WEB_BRIT + "/#/mobile/recruitment/employee-requisitions", menuItem.getGroupId());
                            }
                        }, 200);

                        return true;

                    case 1011:
                        drawerLayout.closeDrawers();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                webView.clearHistory();
                                startWebView(CWUrls.DOMAIN_WEB_BRIT + "/#/mobile/recruitment/pa-dashboard", menuItem.getGroupId());
                                startWebView(CWUrls.DOMAIN_WEB_BRIT + "/#/mobile/recruitment/recruitment-requirements", menuItem.getGroupId());
                            }
                        }, 200);

                        return true;

                    case 1012:
                        drawerLayout.closeDrawers();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                webView.clearHistory();
                                startWebView(CWUrls.DOMAIN_WEB_BRIT + "/#/mobile/recruitment/pa-dashboard", menuItem.getGroupId());
                                startWebView(CWUrls.DOMAIN_WEB_BRIT + "/#/mobile/recruitment/patat", menuItem.getGroupId());
                            }
                        }, 200);

                        return true;


                    default:
                        return true;
                }
            }
        });

        if (CWIncturePreferences.getRole().equalsIgnoreCase("bgvTeam")) {
            startWebView(CWUrls.DOMAIN_WEB_BRIT + "/#/mobile/onboarding/onboarding-verification", 0);
        } else {
            startWebView(CWUrls.DOMAIN_WEB_BRIT + "/#/mobile/recruitment/recruitment-dashboard/NAVIGATION_Recruitment", 0);
        }


        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Util.isOnline(Recruitment_Dashboard.this)) {
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

                            Toast.makeText(Recruitment_Dashboard.this, "No internet connection.Try later", Toast.LENGTH_SHORT).show();
                        }
                    }, 200);
                }
            }
        });

    }

    private void startWebView(String url, int groupID) {

        //Create new webview Client to show progress dialog
        //When opening a url or click on link

        if (progressDialog == null) {
            // in standard case YourActivity.this
            progressDialog = new ProgressDialog(Recruitment_Dashboard.this);
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
                globalUrl = url;

                if (url.contains(CWUrls.DOMAIN_WEB_BRIT + "/#/mobile/recruitment/recruitment-requisitions")) {
                    title.setText("Requisitions");
                } else if (url.contains(CWUrls.DOMAIN_WEB_BRIT + "/#/mobile/recruitment/recruitment-offered")) {
                    title.setText("Offered");
                } else if (url.contains(CWUrls.DOMAIN_WEB_BRIT + "/#/mobile/recruitment/recruitment-tat")) {
                    title.setText("Turn Around Time");
                } else if (url.contains(CWUrls.DOMAIN_WEB_BRIT + "/#/mobile/recruitment/recruitment-task-index")) {
                    title.setText("My Tasks");
                } else if (url.contains(CWUrls.DOMAIN_WEB_BRIT + "/#/mobile/recruitment/IJP")) {
                    title.setText("IJP");
                } else if (url.contains(CWUrls.DOMAIN_WEB_BRIT + "/#/mobile/recruitment/candidates")) {
                    title.setText("Candidates");
                } else if (url.contains(CWUrls.DOMAIN_WEB_BRIT + "/#/mobile/recruitment/proxyUserAccess")) {
                    title.setText("Proxy User Access");
                } else if (url.contains(CWUrls.DOMAIN_WEB_BRIT + "/#/mobile/recruitment/deletejd")) {
                    title.setText("Upload and delete JD");
                } else if (url.contains(CWUrls.DOMAIN_WEB_BRIT + "/#/mobile/recruitment/listPlacementAgency")) {
                    title.setText("Placement Agencies");
                } else if (url.contains(CWUrls.DOMAIN_WEB_BRIT + "/#/mobile/recruitment/employee-requisitions")) {
                    title.setText("Requisitions");
                } else if (url.contains(CWUrls.DOMAIN_WEB_BRIT + "/#/mobile/recruitment/recruitment-dashboard")) {
                    title.setText("Recruitment Dashboard");
                } else if (url.contains("resumeDownload")) {
                    String decodedURL = "";
                    try {
                        decodedURL = URLDecoder.decode(url, "UTF-8");
                        Log.d("urldecoded=", "" + decodedURL);

                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }

                    if (ContextCompat.checkSelfPermission(Recruitment_Dashboard.this,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED) {


                        ActivityCompat.requestPermissions(Recruitment_Dashboard.this,
                                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                Constants.MY_PERMISSIONS_REQUEST_GALLERY);


                    } else {
                        DownloadManager.Request request = new DownloadManager.Request(
                                Uri.parse(url));

                        request.allowScanningByMediaScanner();
                        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED); //Notify client once download is completed!
                        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "");//add resume file name
                        DownloadManager dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                        dm.enqueue(request);
                        Toast.makeText(getApplicationContext(), "Downloading File", Toast.LENGTH_LONG).show(); //To notify the Client that the file is being downloaded

                    }

                    webView.goBack();

                }
            }

        });

        if (mRolesList != null) {
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

        } else {
            roleName = CWIncturePreferences.getRole();
            roleRegion = CWIncturePreferences.getRegion();
            hrFunction = CWIncturePreferences.getHrFunction();
        }
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


        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true);
        }

        CookieManager.getInstance().setCookie(CWUrls.DOMAIN_WEB_BRIT, "token=" + CWIncturePreferences.getAccessToken());
        CookieManager.getInstance().setCookie(CWUrls.DOMAIN_WEB_BRIT, "email=" + CWIncturePreferences.getEmail());
        CookieManager.getInstance().setCookie(CWUrls.DOMAIN_WEB_BRIT, "roleName =" + roleName);
        CookieManager.getInstance().setCookie(CWUrls.DOMAIN_WEB_BRIT, "myid=" + CWIncturePreferences.getUserId());
        CookieManager.getInstance().setCookie(CWUrls.DOMAIN_WEB_BRIT, "regionName=" + roleRegion);
        CookieManager.getInstance().setCookie(CWUrls.DOMAIN_WEB_BRIT, "cherrySelected=" + cherrySelected);
        CookieManager.getInstance().setCookie(CWUrls.DOMAIN_WEB_BRIT, "userhrFunction=" + hrFunction);


        Log.d("1token=", "" + CWIncturePreferences.getAccessToken());
        Log.d("1email=", "" + CWIncturePreferences.getEmail());
        Log.d("1myid=", "" + CWIncturePreferences.getUserId());
        Log.d("1roleName=", "" + roleName);
        Log.d("1roleRegion=", "" + roleRegion);
        Log.d("1cherryselected=", "" + cherrySelected);
        Log.d("1userhr=", "" + hrFunction);

        CookieSyncManager.getInstance().sync();

        webView.loadUrl(url);

    }

    //change logout

    public void logout() {
        LayoutInflater li = LayoutInflater.from(this);
        final View promptsView = li.inflate(R.layout.logout_dialog, null);

        drawerLayout.closeDrawers();
        final AlertDialog.Builder dialog = new AlertDialog.Builder(Recruitment_Dashboard.this);
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

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }

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

        NetworkConnector connect = new NetworkConnector(Recruitment_Dashboard.this, NetworkConnector.TYPE_POST, CWUrls.LOG_OUT, headers, null, Recruitment_Dashboard.this);
        if (connect.isAllowed()) {
            connect.execute();
        } else {
            Toast.makeText(Recruitment_Dashboard.this, getString(R.string.action_not_allowed), Toast.LENGTH_SHORT).show();
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
                util.logoutfunc(Recruitment_Dashboard.this);


            } else if (message != null && !message.isEmpty()) {
                Toast.makeText(Recruitment_Dashboard.this, message, Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void configurationUpdated(boolean configUpdated) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case Constants.MY_PERMISSIONS_REQUEST_GALLERY: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {


                    DownloadManager.Request request = new DownloadManager.Request(
                            Uri.parse(globalUrl));

                    request.allowScanningByMediaScanner();
                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED); //Notify client once download is completed!
                    request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "");//add resume file name
                    DownloadManager dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                    dm.enqueue(request);
                    Toast.makeText(getApplicationContext(), "Downloading File", Toast.LENGTH_LONG).show(); //To notify the Client that the file is being downloaded

                }
                return;
            }

        }
    }
}
