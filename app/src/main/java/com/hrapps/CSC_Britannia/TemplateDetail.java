package com.hrapps.CSC_Britannia;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebChromeClient;
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
 * Created by harshu on 9/10/2016.
 */
public class TemplateDetail extends BasicActivity {
    WebView webView;
    private ProgressDialog progressDialog;
    ImageView back;
    TextView title ;
    String hrFunction="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.appraisal_template);

        title = (TextView)findViewById(R.id.title);
        webView = (WebView) findViewById(R.id.webview);
        back = (ImageView) findViewById(R.id.back);


        CWIncturePreferences.init(TemplateDetail.this);

        Intent intent = getIntent();
        String template_id = intent.getStringExtra("template_id");
        String workitem_id = intent.getStringExtra("workitem_id");
        String template_status = intent.getStringExtra("status");
        String type = intent.getStringExtra("type");
        String roleName = intent.getStringExtra("roleName");
        String regionName = intent.getStringExtra("regionName");
        hrFunction = intent.getStringExtra("hrFunction");

        if (hrFunction.equalsIgnoreCase("null")||hrFunction == null||hrFunction.isEmpty()){
            hrFunction = "undefined";
        }

        if (CWIncturePreferences.getRoleRegion() == null && CWIncturePreferences.getUserId() == null && CWIncturePreferences.getEmail() == null && CWIncturePreferences.getAccessToken() == null) {
            Intent toLogin = new Intent(TemplateDetail.this, Login.class);
            startActivity(toLogin);
        } else {
            if (type.equalsIgnoreCase("Pms")) {
                startWebView(CWUrls.DOMAIN_WEB_BRIT + "/#/mobile/pms/template-detail-approval?templateid=" + template_id + "&workitemid=" + workitem_id + "&status=" + template_status,roleName,regionName);
            }
            else if (type.equalsIgnoreCase("csc")){
                startWebView(CWUrls.DOMAIN_WEB_BRIT + "/#/mobile/csc/template-detail-approval?templateid=" + template_id + "&workitemid=" + workitem_id + "&status=" + template_status,roleName,regionName);
            }

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


        if (progressDialog == null) {
            // in standard case YourActivity.this
            progressDialog = new ProgressDialog(TemplateDetail.this);
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

        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true);
        }

        CookieManager.getInstance().setCookie(CWUrls.DOMAIN_WEB_BRIT, "token=" + CWIncturePreferences.getAccessToken());
        CookieManager.getInstance().setCookie(CWUrls.DOMAIN_WEB_BRIT, "email=" + CWIncturePreferences.getEmail());
        CookieManager.getInstance().setCookie(CWUrls.DOMAIN_WEB_BRIT, "roleName =" + roleName);
        CookieManager.getInstance().setCookie(CWUrls.DOMAIN_WEB_BRIT, "myid=" + CWIncturePreferences.getUserId());
        CookieManager.getInstance().setCookie(CWUrls.DOMAIN_WEB_BRIT, "regionName=" + regionName);
        CookieManager.getInstance().setCookie(CWUrls.DOMAIN_WEB_BRIT,"userhrFunction="+hrFunction);

        CookieSyncManager.getInstance().sync();

        webView.loadUrl(url);
    }
}
