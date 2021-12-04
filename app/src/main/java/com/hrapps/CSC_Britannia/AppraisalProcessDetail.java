package com.hrapps.CSC_Britannia;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.hrapps.BasicActivity;
import com.hrapps.Login;
import com.hrapps.R;

import Utility.CWIncturePreferences;
import Utility.CWUrls;

/**
 * Created by harshu on 10/7/2016.
 */
public class AppraisalProcessDetail extends BasicActivity {

    WebView webView;
    TextView title;
    ImageView back;
    String cherryName = "";
    String hrFunction = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.appraisal_process);


        webView = (WebView) findViewById(R.id.webview);
        title = (TextView) findViewById(R.id.title);
        back = (ImageView) findViewById(R.id.back);

        Intent intent = getIntent();
        String template_id = intent.getStringExtra("template_id");
        String from = intent.getStringExtra("from");
        String to = intent.getStringExtra("to");
        String stage = intent.getStringExtra("stage");
        String appraisalname = intent.getStringExtra("appraisalname");
        String roleName = intent.getStringExtra("roleName");
        String regionName = intent.getStringExtra("regionName");
        cherryName = intent.getStringExtra("type");//cherryname
        hrFunction = intent.getStringExtra("hrFunction");

        CWIncturePreferences.init(AppraisalProcessDetail.this);

        if (hrFunction.equalsIgnoreCase("null") || hrFunction == null || hrFunction.isEmpty()) {
            hrFunction = "undefined";
        }

        if (CWIncturePreferences.getRoleRegion() == null && CWIncturePreferences.getUserId() == null && CWIncturePreferences.getEmail() == null && CWIncturePreferences.getAccessToken() == null) {
            Intent toLogin = new Intent(AppraisalProcessDetail.this, Login.class);
            startActivity(toLogin);
        } else {

            if (cherryName.equalsIgnoreCase("csc")) {
                if (CWIncturePreferences.getRole().equalsIgnoreCase("tsi")) {
                    title.setText("Dashboard");
                    startWebView(CWUrls.DOMAIN_WEB_BRIT + "/#/mobile/csc/dashboard", roleName, regionName);
                } else {

                    switch (stage.toUpperCase()) {

                        case "TARGET SETTING":
                            title.setText("Targets");
                            startWebView(CWUrls.DOMAIN_WEB_BRIT + "/#/mobile/csc/employee-target-fill-list?appname=" + appraisalname + "&tempid=" + template_id + "&from=" + from + "&to=" + to, roleName, regionName);
                            break;

                        case "TARGET APPROVAL":
                            title.setText("Approvals");
                            startWebView(CWUrls.DOMAIN_WEB_BRIT + "/#/mobile/csc/mass-target-approvals?appname=" + appraisalname + "&tempid=" + template_id + "&from=" + from + "&to=" + to, roleName, regionName);
                            break;

                        case "ACHIEVEMENT UPDATE BY RSA":
                            title.setText("Achievements");
                            startWebView(CWUrls.DOMAIN_WEB_BRIT + "/#/mobile/csc/employee-achievement-fill-list?appname=" + appraisalname + "&tempid=" + template_id + "&from=" + from + "&to=" + to, roleName, regionName);
                            break;

                        case "ACHIEVEMENT REVIEW BY MANAGER":
                            title.setText("Approvals");
                            startWebView(CWUrls.DOMAIN_WEB_BRIT + "/#/mobile/csc/mass-achievement-approvals?appname=" + appraisalname + "&tempid=" + template_id + "&from=" + from + "&to=" + to, roleName, regionName);
                            break;

                        case "REVIEW AND UPDATE BY CAPABILITY TEAM":
                            title.setText("Scores");
                            startWebView(CWUrls.DOMAIN_WEB_BRIT + "/#/mobile/csc/employee-scores?appname=" + appraisalname + "&tempid=" + template_id + "&from=" + from + "&to=" + to, roleName, regionName);
                            break;

                        case "REVIEW BY HR OFFICER":
                            startWebView(CWUrls.DOMAIN_WEB_BRIT + "/#/mobile/csc/employee-scores-approval-byhrofficer?appname=" + appraisalname + "&tempid=" + template_id + "&from=" + from + "&to=" + to, roleName, regionName);
                            break;
                        case "REVIEW BY RHRM":
                            startWebView(CWUrls.DOMAIN_WEB_BRIT + "/#/mobile/csc/employee-scores-approval-byrhrm?appname=" + appraisalname + "&tempid=" + template_id + "&from=" + from + "&to=" + to, roleName, regionName);
                            break;

                    }//end of switch

                }
            }//end od csc cherry

            else if (cherryName.equalsIgnoreCase("pms")) {

                switch (stage.toUpperCase()) {
                    case "KRA INPUTS BY SALES ADMIN":
                        title.setText("Target Setting");
                        startWebView(CWUrls.DOMAIN_WEB_BRIT + "/#/mobile/pms/employee-goals-upload?appname=" + appraisalname + "&tempid=" + template_id + "&from=" + from + "&to=" + to + "&hrFunction=" + hrFunction, roleName, regionName);
                        break;

                    case "KRA APPROVAL BY MANAGER":
                        title.setText("Target Approval");
                        startWebView(CWUrls.DOMAIN_WEB_BRIT + "/#/mobile/pms/mass-target-approvals?appname=" + appraisalname + "&tempid=" + template_id + "&from=" + from + "&to=" + to + "&hrFunction=" + hrFunction, roleName, regionName);
                        break;

                    case "ACHIEVEMENT INPUT BY SALES ADMIN":
                        title.setText("Achievement Update");
                        startWebView(CWUrls.DOMAIN_WEB_BRIT + "/#/mobile/pms/employee-achievements-upload?appname=" + appraisalname + "&tempid=" + template_id + "&from=" + from + "&to=" + to + "&hrFunction=" + hrFunction, roleName, regionName);
                        break;

                    case "SELF ASSESSMENT BY EMPLOYEE":
                        title.setText("Self Assessment");
                        startWebView(CWUrls.DOMAIN_WEB_BRIT + "/#/mobile/pms/tsi-annual-detail?appname=" + appraisalname + "&tempid=" + template_id + "&from=" + from + "&to=" + to + "&hrFunction=" + hrFunction, roleName, regionName);
                        break;

                    case "MANAGER ASSESSMENT":
                        title.setText("Manager Assessment");
                        startWebView(CWUrls.DOMAIN_WEB_BRIT + "/#/mobile/pms/employee-assesment-appovals?appname=" + appraisalname + "&tempid=" + template_id + "&from=" + from + "&to=" + to + "&hrFunction=" + hrFunction, roleName, regionName);
                        break;

                    case "HRBP APPROVAL":
                        title.setText("Approval");
                        startWebView(CWUrls.DOMAIN_WEB_BRIT + "/#/mobile/pms/employee-drilldown-tree-data?appname=" + appraisalname + "&tempid=" + template_id + "&from=" + from + "&to=" + to + "&hrFunction=" + hrFunction + "&currentStage=" + stage, roleName, regionName);
                        break;
                    case "CLOSED":
                        if (roleName.equalsIgnoreCase("TSI")) {
                            title.setText("Annual Scores");
                            startWebView(CWUrls.DOMAIN_WEB_BRIT + "/#/mobile/pms/tsi-annual-scores?appname=" + appraisalname + "&tempid=" + template_id + "&from=" + from + "&to=" + to + "&hrFunction=" + hrFunction, roleName, regionName);
                        } else {
                            title.setText("Reports");
                            startWebView(CWUrls.DOMAIN_WEB_BRIT + "/#/mobile/pms/pms-report?appname=" + appraisalname + "&tempid=" + template_id + "&from=" + from + "&to=" + to + "&hrFunction=" + hrFunction, roleName, regionName);
                        }
                        break;
                }

            }//end of pms cherry
            else if (cherryName.equalsIgnoreCase("onBoarding")) {

                switch (stage.toUpperCase()) {

                    case "MEDICALFORM":
                        title.setText("Medical");
                        startWebView(CWUrls.DOMAIN_WEB_BRIT + "/#/mobile/onboarding/onboarding-medicalObt?id=" + template_id, roleName, regionName);
                        Log.d("url=", "" + CWUrls.DOMAIN_WEB_BRIT + "/#/mobile/onboarding/onboarding-medicalObt?id=" + template_id);
                        break;
                    case "BGVREPORTSUBMISSION":
                        title.setText("Background Verification");
                        startWebView(CWUrls.DOMAIN_WEB_BRIT + "/#/mobile/onboarding/onboarding-employeeinfo?id=" + template_id, roleName, regionName);
                        break;
                    case "FORMSUBMISSION":
                        title.setText("Checklist");
                        startWebView(CWUrls.DOMAIN_WEB_BRIT + "/#/mobile/onboarding/onboarding-checklist?id=" + template_id, roleName, regionName);
                        break;
                    case "HIRE":
                        title.setText("Hire Joiners");
                        startWebView(CWUrls.DOMAIN_WEB_BRIT + "/#/mobile/onboarding/onboarding-allList", roleName, regionName);
                        break;
                }

            }//end of onBoarding cherry
        }
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void startWebView(String url, String roleName, String regionName) {

        //Create new webview Client to show progress dialog
        //When opening a url or click on link

        webView.setWebViewClient(new WebViewClient() {

            ProgressDialog progressDialog;

            //If you will not use this method url links are opeen in new brower not in webview
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                view.loadUrl(url);
                return true;
            }

            //Show loader on url load
            public void onLoadResource(WebView view, String url) {
                if (progressDialog == null) {
                    // in standard case YourActivity.this
                    progressDialog = new ProgressDialog(AppraisalProcessDetail.this);
                    progressDialog.setMessage("Loading...");
                    progressDialog.setCancelable(true);
                    progressDialog.show();
                }
            }

            public void onPageFinished(WebView view, String url) {
                try {

                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                        // progressDialog = null;
                    }

                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
        });

        // You can create external class extends with WebChromeClient
        // Taking WebViewClient as inner class
        // we will define openFileChooser for select file from camera or sdcard

        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setJavaScriptEnabled(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true);
        }


        CookieManager.getInstance().setCookie(CWUrls.DOMAIN_WEB_BRIT, "token=" + CWIncturePreferences.getAccessToken());
        CookieManager.getInstance().setCookie(CWUrls.DOMAIN_WEB_BRIT, "email=" + CWIncturePreferences.getEmail());
        CookieManager.getInstance().setCookie(CWUrls.DOMAIN_WEB_BRIT, "roleName =" + roleName);
        CookieManager.getInstance().setCookie(CWUrls.DOMAIN_WEB_BRIT, "regionName=" + regionName);
        CookieManager.getInstance().setCookie(CWUrls.DOMAIN_WEB_BRIT, "myid=" + CWIncturePreferences.getUserId());
        CookieManager.getInstance().setCookie(CWUrls.DOMAIN_WEB_BRIT, "cherrySelected=" + cherryName);
        CookieManager.getInstance().setCookie(CWUrls.DOMAIN_WEB_BRIT, "userhrFunction=" + hrFunction);


        CookieSyncManager.getInstance().sync();

        webView.loadUrl(url);
    }
}
