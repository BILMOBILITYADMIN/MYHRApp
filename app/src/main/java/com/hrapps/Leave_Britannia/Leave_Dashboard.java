package com.hrapps.Leave_Britannia;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.Nullable;
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
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hrapps.BasicActivity;
import com.hrapps.CSC_Britannia.CSC_Dashboard;
import com.hrapps.Login;
import com.hrapps.R;
import com.hrapps.Recruitment_Britannia.Recruitment_Dashboard;
import com.hrapps.UserProfileActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import Model.LoginModel;
import Model.RolesModel;
import Utility.AsyncResponse;
import Utility.CWIncturePreferences;
import Utility.CWUrls;
import Utility.Constants;
import Utility.NetworkConnector;
import Utility.ProfileImageView;
import Utility.Util;

public class Leave_Dashboard extends BasicActivity implements AsyncResponse {
    private final String dashboard_url = CWUrls.DOMAIN_WEB_BRIT+"/#/mobile/leave/dashboard";
    private final String approval_url = CWUrls.DOMAIN_WEB_BRIT+"/#/mobile/leave/approvals";

    WebView webView;
    private ProgressDialog progressDialog;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    ImageView side_menu, home, back, profileBg;
    ProfileImageView img;
    Button logout;
    TextView name, email,title,tv_app_version;

    private ValueCallback<Uri> mUploadMessage;
    public ValueCallback<Uri[]> uploadMessage;
    public static final int REQUEST_SELECT_FILE = 100;
    ArrayList<RolesModel> mRolesList = new ArrayList<>();
    String role ="";
    String globalUrl = "";



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.britannia_dashboard);
        webView = (WebView) findViewById(R.id.webview);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        side_menu = (ImageView) findViewById(R.id.menu);
        home = (ImageView) findViewById(R.id.home);
        back = (ImageView) findViewById(R.id.back);
        name = (TextView) findViewById(R.id.drawer_header_profile_name);
        email = (TextView) findViewById(R.id.drawer_header_profile_email);
        img = (ProfileImageView) findViewById(R.id.drawer_header_profile_pic);
        profileBg = (ImageView) findViewById(R.id.backgroundimage1);
        logout = (Button) findViewById(R.id.logout1);
        title = (TextView)findViewById(R.id.title);
        tv_app_version = (TextView)findViewById(R.id.tv_version);

        try {
            tv_app_version.setText("App Version " + getPackageManager().getPackageInfo(getPackageName(), 0).versionName);
        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


        mRolesList = (ArrayList<RolesModel>) getIntent().getSerializableExtra("roles");
        Log.d("roleslist=", "" + mRolesList.toString());

        if (mRolesList.size() == 1) {
            role = mRolesList.get(0).getRole_name();
            {
                Log.d("rolee=:", "" + role);
            }
        }

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.closeDrawers();
            }
        });

        name.setText(CWIncturePreferences.getFirstname() + CWIncturePreferences.getLastname());
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
                        Intent userProfile = new Intent(Leave_Dashboard.this, UserProfileActivity.class);
                        startActivity(userProfile);
                    }
                }, 200);

            }
        });

        logout.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v) {
                if (Util.isOnline(Leave_Dashboard.this)) {
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

                            Toast.makeText(Leave_Dashboard.this, "No internet connection.Try later", Toast.LENGTH_SHORT).show();
                        }
                    }, 200);
                }
            }
        });
        final Menu menu = navigationView.getMenu();
        final SubMenu subMenu = menu.addSubMenu("Leave");
        if (role.equalsIgnoreCase("retainee")){
            leaveWebview(approval_url, 1);
            subMenu.add(Menu.NONE, 2, Menu.NONE, "    " + "Approvals").setCheckable(true).setChecked(false);
            subMenu.add(Menu.NONE, 3, Menu.NONE, "    " + "Team Calendar").setCheckable(true).setChecked(false);
        }else{
            leaveWebview(dashboard_url, 1);
            subMenu.add(Menu.NONE, 1, Menu.NONE, "    " + "Leave").setCheckable(true).setChecked(false);
            subMenu.add(Menu.NONE, 2, Menu.NONE, "    " + "Approvals").setCheckable(true).setChecked(false);
            subMenu.add(Menu.NONE, 3, Menu.NONE, "    " + "Team Calendar").setCheckable(true).setChecked(false);
        }

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(final MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case 1:
                        drawerLayout.closeDrawers();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                webView.clearHistory();
                                leaveWebview(CWUrls.DOMAIN_WEB_BRIT + "/#/mobile/leave/dashboard", menuItem.getGroupId());
                            }
                        }, 200);

                        break;
                    case 2:
                        drawerLayout.closeDrawers();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                webView.clearHistory();
                                leaveWebview(CWUrls.DOMAIN_WEB_BRIT + "/#/mobile/leave/approvals", menuItem.getGroupId());
                                Log.d("groupid=", "" + menuItem.getGroupId());
                            }
                        }, 200);
                        break;
                    case 3:
                        drawerLayout.closeDrawers();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                webView.clearHistory();
                                leaveWebview(CWUrls.DOMAIN_WEB_BRIT + "/#/mobile/leave/team-calendar", menuItem.getGroupId());
                                Log.d("groupid=", "" + menuItem.getGroupId());
                            }
                        }, 200);

                        break;
                    default:
                        break;
                }
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

    protected void leaveWebview(String url, int groupID) {
        if (progressDialog == null) {
            // in standard case YourActivity.this
            progressDialog = new ProgressDialog(Leave_Dashboard.this);
            progressDialog.setMessage("Loading...");
            progressDialog.setCancelable(true);
            progressDialog.show();
        }


        webView.setWebViewClient(new WebViewClient() {


            //If you will not use this method url links are open in new brower not in webview
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                view.loadUrl(url);
                return true;
            }

            //Show loader on url load

            public void onPageFinished(WebView view, String url) {
                globalUrl = url;
                if (url.contains("/#/mobile/leave/dashboard")){
                    title.setText("Leave");
                }else  if (url.contains("/#/mobile/leave/approvals")){
                    title.setText("Leave Approvals");
                }else if(url.contains("/#/mobile/leave/team-calendar")){
                    title.setText("Team Calendar");
                }else if (url.contains("/leave/leavePolicy01Aug2018.pdf")) {
                    String decodedURL = "";
                    try {
                        decodedURL = URLDecoder.decode(url, "UTF-8");
                        Log.d("urldecoded=", "" + decodedURL);

                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }

//                    if (ContextCompat.checkSelfPermission(Leave_Dashboard.this,
//                            Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                            != PackageManager.PERMISSION_GRANTED) {
//
//
//                        ActivityCompat.requestPermissions(Leave_Dashboard.this,
//                                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
//                                Constants.MY_PERMISSIONS_REQUEST_GALLERY);
//
//
//                    } else {
//                        DownloadManager.Request request = new DownloadManager.Request(
//                                Uri.parse(url));
//
//                        request.allowScanningByMediaScanner();
//                        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED); //Notify client once download is completed!
//                        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "");//add resume file name
//                        DownloadManager dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
//                        dm.enqueue(request);
//                        Toast.makeText(getApplicationContext(), "Downloading File", Toast.LENGTH_LONG).show(); //To notify the Client that the file is being downloaded
//
//                    }

                    webView.goBack();

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



            // For Lollipop 5.0+ Devices
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            public boolean onShowFileChooser(WebView mWebView, ValueCallback<Uri[]> filePathCallback, WebChromeClient.FileChooserParams fileChooserParams) {
                if (uploadMessage != null) {
                    uploadMessage.onReceiveValue(null);
                    uploadMessage = null;
                }

                uploadMessage = filePathCallback;
                Intent intent = fileChooserParams.createIntent();
                try {
                    startActivityForResult(intent, REQUEST_SELECT_FILE);
                } catch (ActivityNotFoundException e) {
                    uploadMessage = null;
                    Toast.makeText(Leave_Dashboard.this, "Cannot Open File Chooser", Toast.LENGTH_LONG).show();
                    return false;
                }
                return true;
            }

        });


        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setJavaScriptEnabled(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true);
        }
        CookieManager.getInstance().setCookie(CWUrls.COOKIE_DOMAIN_BRIT, "email=" + CWIncturePreferences.getEmail());
        CookieManager.getInstance().setCookie(CWUrls.COOKIE_DOMAIN_BRIT, "token=" + CWIncturePreferences.getAccessToken());
        CookieManager.getInstance().setCookie(CWUrls.COOKIE_DOMAIN_BRIT, "id=" + CWIncturePreferences.getPOSITION());
        CookieManager.getInstance().setCookie(CWUrls.COOKIE_DOMAIN_BRIT, "name=" + CWIncturePreferences.getFirstname()+CWIncturePreferences.getLastname());

        Log.d("2token=", "" + CWIncturePreferences.getAccessToken());
        Log.d("2email=", "" + CWIncturePreferences.getEmail());
        Log.d("email=", "" + CWIncturePreferences.getPOSITION());
        Log.d("role=",""+CWIncturePreferences.getRole());

        String loginModel =  CWIncturePreferences.getLoginModelData();
        JSONObject managerObj=null;
        try {
            managerObj = new JSONObject(loginModel);
            Log.d("checkobj=",""+managerObj.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        CookieManager.getInstance().setCookie(CWUrls.COOKIE_DOMAIN_BRIT, "manager=" +managerObj.toString());


        CookieSyncManager.getInstance().sync();

        webView.loadUrl(url);


    }


    public void logout() {
        LayoutInflater li = LayoutInflater.from(this);
        final View promptsView = li.inflate(R.layout.logout_dialog, null);

        drawerLayout.closeDrawers();
        final AlertDialog.Builder dialog = new AlertDialog.Builder(Leave_Dashboard.this);
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

        NetworkConnector connect = new NetworkConnector(Leave_Dashboard.this, NetworkConnector.TYPE_POST, CWUrls.LOG_OUT, headers, null, Leave_Dashboard.this);
        if (connect.isAllowed()) {
            connect.execute();
        } else {
            Toast.makeText(Leave_Dashboard.this, getString(R.string.action_not_allowed), Toast.LENGTH_SHORT).show();
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

            if (status.equalsIgnoreCase("success")) {

                Util util = new Util();
                util.logoutfunc(Leave_Dashboard.this);


            } else if (message != null && !message.isEmpty()) {
                Toast.makeText(Leave_Dashboard.this, message, Toast.LENGTH_SHORT).show();
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (requestCode == REQUEST_SELECT_FILE) {
                if (uploadMessage == null)
                    return;
                uploadMessage.onReceiveValue(WebChromeClient.FileChooserParams.parseResult(resultCode, intent));
                uploadMessage = null;
            }
        } else
            Toast.makeText(Leave_Dashboard.this, "Failed to Upload Image", Toast.LENGTH_LONG).show();
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
