package com.hrapps.PMS_Britannia;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.hrapps.BasicActivity;
import com.hrapps.R;

import Utility.CWIncturePreferences;
import Utility.CWUrls;

/**
 * Created by harshu on 1/25/2017.
 */

public class GoalSettingDetail extends BasicActivity {

    TextView title;
    WebView webview;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.appraisal_template);

        title = (TextView) findViewById(R.id.title);
        webview = (WebView) findViewById(R.id.webview);
        title.setText("Goal Setting Detail");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.my_primary_dark));

        }

        Intent intent = getIntent();
        String appId = intent.getStringExtra("appId");
        String workitem_id = intent.getStringExtra("workitem_id");
        String template_status = intent.getStringExtra("status");

        startWebView(CWUrls.DOMAIN_WEB_BRIT + "/#/mobile/pms/annual-review-by-manager?appid=" + appId + "&workitemid=" + workitem_id + "&status=" + template_status, 0);
        Log.d("url=", "" + CWUrls.DOMAIN_WEB_BRIT + "/#/mobile/pms/annual-review-by-manager?appid=" + appId + "&workitemid=" + workitem_id + "&status=" + template_status);
    }

    private void startWebView(String url, int roleSelected) {

        //Create new webview Client to show progress dialog
        //When opening a url or click on link

        if (progressDialog == null) {
            // in standard case YourActivity.this
            progressDialog = new ProgressDialog(GoalSettingDetail.this);
            progressDialog.setMessage("Loading...");
            progressDialog.setCancelable(true);
            progressDialog.show();
        }

        webview.setWebViewClient(new WebViewClient() {


            //If you will not use this method url links are opeen in new brower not in webview
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                view.loadUrl(url);
                return true;
            }

            //Show loader on url load

            public void onPageFinished(WebView view, String url) {

                if (url.contains(CWUrls.DOMAIN_WEB_BRIT + "/#/mobile/pms/mass-pms-approvals")) {
                    title.setText("Approvals");
                } else if (url.contains(CWUrls.DOMAIN_WEB_BRIT + "/mobile/pms/appraisal-dashboard")) {
                    title.setText("Appraisals");
                } else if (url.contains(CWUrls.DOMAIN_WEB_BRIT + "/#/mobile/pms/template-dashboard")) {
                    title.setText("Templates");
                } else if (url.contains(CWUrls.DOMAIN_WEB_BRIT + "/#/mobile/pms/criterions-dashboard")) {
                    title.setText("Criterions");
                } else if (url.contains(CWUrls.DOMAIN_WEB_BRIT + "/#/mobile/pms/employee-annual-appraisal")) {
                    title.setText("Appraisals");
                }
            }

        });


        webview.setWebChromeClient(new WebChromeClient() {

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


        webview.getSettings().setDomStorageEnabled(true);
        webview.getSettings().setJavaScriptEnabled(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true);
        }
        CookieManager.getInstance().setCookie(CWUrls.DOMAIN_WEB_BRIT, "token=" + CWIncturePreferences.getAccessToken());
        CookieManager.getInstance().setCookie(CWUrls.DOMAIN_WEB_BRIT, "email=" + CWIncturePreferences.getEmail());
        CookieManager.getInstance().setCookie(CWUrls.DOMAIN_WEB_BRIT, "roleData=" + CWIncturePreferences.getRoleRegion());
        CookieManager.getInstance().setCookie(CWUrls.DOMAIN_WEB_BRIT, "myid=" + CWIncturePreferences.getUserId());
        CookieManager.getInstance().setCookie(CWUrls.DOMAIN_WEB_BRIT, "roleSelected=" + roleSelected);

        CookieSyncManager.getInstance().sync();

        webview.loadUrl(url);

    }

}
