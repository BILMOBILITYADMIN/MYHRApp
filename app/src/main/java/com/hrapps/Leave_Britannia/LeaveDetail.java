package com.hrapps.Leave_Britannia;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.Toast;

import com.hrapps.R;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import Utility.CWIncturePreferences;
import Utility.CWUrls;
import Utility.Constants;

public class LeaveDetail extends AppCompatActivity {

    WebView webView;
    private ProgressDialog progressDialog;
    ImageView back;
    String globalUrl = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.leave_detail);
        webView = (WebView) findViewById(R.id.webview);
        back = (ImageView) findViewById(R.id.back);
        Intent intent = getIntent();
        String id = intent.getStringExtra("leaveId");
//        Log.d("id=", "" + id);

        String applicationType = intent.getStringExtra("applicationType");
        if (applicationType.equalsIgnoreCase("Approval Process") || applicationType.equalsIgnoreCase("Cancel Process")) {
            leaveWebview(CWUrls.DOMAIN_WEB_BRIT + "/#/mobile/leave/approval?id=" + id);
        } else if (applicationType.equalsIgnoreCase("Reject Process")|| applicationType.equalsIgnoreCase("Approval Response")) {
            leaveWebview(CWUrls.DOMAIN_WEB_BRIT + "/#/mobile/leave/detail/" + id);
        }

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();

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
    }

    protected void leaveWebview(String url) {
        if (progressDialog == null) {
            // in standard case YourActivity.this
            progressDialog = new ProgressDialog(LeaveDetail.this);
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
                if (url.contains("/leave/leavePolicy01Aug2018.pdf")) {
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

        });


        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setJavaScriptEnabled(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true);
        }
        CookieManager.getInstance().setCookie(CWUrls.COOKIE_DOMAIN_BRIT, "email=" + CWIncturePreferences.getEmail());
        CookieManager.getInstance().setCookie(CWUrls.COOKIE_DOMAIN_BRIT, "token=" + CWIncturePreferences.getAccessToken());
        CookieManager.getInstance().setCookie(CWUrls.COOKIE_DOMAIN_BRIT, "position=" + CWIncturePreferences.getPOSITION());
        CookieManager.getInstance().setCookie(CWUrls.COOKIE_DOMAIN_BRIT, "name=" + CWIncturePreferences.getFirstname()+CWIncturePreferences.getLastname());
//        Log.d("2token=", "" + CWIncturePreferences.getAccessToken());
//        Log.d("2email=", "" + CWIncturePreferences.getEmail());
        Log.d("posemail=", "" + CWIncturePreferences.getPOSITION());
        CookieSyncManager.getInstance().sync();

        webView.loadUrl(url);

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
