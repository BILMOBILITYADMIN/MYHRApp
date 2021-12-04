package com.hrapps.Recruitment_Britannia;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import com.hrapps.BasicActivity;
import com.hrapps.Login;
import com.hrapps.R;

import Utility.CWIncturePreferences;
import Utility.CWUrls;

/**
 * Created by harshu on 1/19/2017.
 */

public class RecruitmentWorkitemDetail extends BasicActivity {

    private ProgressDialog progressDialog;
    String hrFunction = "";
    WebView webView;
    ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recruitment_detail);

        webView = (WebView) findViewById(R.id.webview);
        back = (ImageView) findViewById(R.id.back);


        CWIncturePreferences.init(RecruitmentWorkitemDetail.this);

        Intent intent = getIntent();

        String req_id = intent.getStringExtra("req_id");
        String workitem_id = intent.getStringExtra("workitem_id");
        String roleName = intent.getStringExtra("roleName");
        String regionName = intent.getStringExtra("regionName");
        hrFunction = intent.getStringExtra("hrFunction");
        String subType = intent.getStringExtra("subType");

        Log.d("roleName=", "" + roleName);
        Log.d("roleregion=", "" + regionName);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        if (hrFunction.equalsIgnoreCase("null") || hrFunction == null || hrFunction.isEmpty()) {
            hrFunction = "undefined";
        }

        if (CWIncturePreferences.getRoleRegion() == null && CWIncturePreferences.getUserId() == null && CWIncturePreferences.getEmail() == null && CWIncturePreferences.getAccessToken() == null) {
            Intent toLogin = new Intent(RecruitmentWorkitemDetail.this, Login.class);
            startActivity(toLogin);
        } else {
            if (subType.equalsIgnoreCase("requisition")) {
                startWebView(CWUrls.DOMAIN_WEB_BRIT + "/#/mobile/recruitment/workitemDetail?id=" + req_id + "&workitemId=" + workitem_id, roleName, regionName);
            } else if (subType.equalsIgnoreCase("updateRequisition")) {
                startWebView(CWUrls.DOMAIN_WEB_BRIT + "/#/mobile/recruitment/update-non-budgeted-requisition?id=" + req_id + "&workitemId=" + workitem_id + "&subtype=nonBudgeted", roleName, regionName);
            } else if (subType.equalsIgnoreCase("offerRollout")) {
                startWebView(CWUrls.DOMAIN_WEB_BRIT + "/#/mobile/recruitment/request-for-exception-detail?id=" + req_id + "&workitemId=" + workitem_id, roleName, regionName);
            } else if (subType.equalsIgnoreCase("jobApplicationException")) {
                startWebView(CWUrls.DOMAIN_WEB_BRIT + "/#/mobile/recruitment/exception-detail?id=" + req_id + "&workitemId=" + workitem_id, roleName, regionName);

            } else if (subType.equalsIgnoreCase("payRefferalBonus")) {
                startWebView(CWUrls.DOMAIN_WEB_BRIT + "/#/mobile/recruitment/exception-detail?id=" + req_id + "&workitemId=" + workitem_id, roleName, regionName);
            }
        }

    }

    private void startWebView(String url, String roleName, String regionName) {

        //Create new webview Client to show progress dialog
        //When opening a url or click on link


        if (progressDialog == null) {
            // in standard case YourActivity.this
            progressDialog = new ProgressDialog(RecruitmentWorkitemDetail.this);
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
        // we will define openFileChooser for select file from camera or sdcard

        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setJavaScriptEnabled(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true);
        }

        CookieManager.getInstance().setCookie(CWUrls.DOMAIN_WEB_BRIT, "token=" + CWIncturePreferences.getAccessToken());
        CookieManager.getInstance().setCookie(CWUrls.DOMAIN_WEB_BRIT, "email=" + CWIncturePreferences.getEmail());
        CookieManager.getInstance().setCookie(CWUrls.DOMAIN_WEB_BRIT, "roleName =" + roleName);
        CookieManager.getInstance().setCookie(CWUrls.DOMAIN_WEB_BRIT, "myid=" + CWIncturePreferences.getUserId());
        CookieManager.getInstance().setCookie(CWUrls.DOMAIN_WEB_BRIT, "regionName=" + regionName);
        CookieManager.getInstance().setCookie(CWUrls.DOMAIN_WEB_BRIT, "userhrFunction=" + hrFunction);

        CookieSyncManager.getInstance().sync();

        webView.loadUrl(url);
    }
}
