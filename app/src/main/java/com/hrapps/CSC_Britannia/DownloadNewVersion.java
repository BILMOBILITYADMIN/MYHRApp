package com.hrapps.CSC_Britannia;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.CookieSyncManager;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.hrapps.BasicActivity;
import com.hrapps.R;

/**
 * Created by harshu on 11/29/2016.
 */
public class DownloadNewVersion extends BasicActivity {

    WebView webView;
    private ProgressDialog progressDialog;

    //172.16.8.236:7007//QA build


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.download_version_ui);

        webView = (WebView) findViewById(R.id.webview);

        startWebView("http://myhr.britindia.com/files/a.txt");

    }

    private void startWebView(String url) {

        //Create new webview Client to show progress dialog
        //When opening a url or click on link


        if (progressDialog == null) {
            // in standard case YourActivity.this
            progressDialog = new ProgressDialog(DownloadNewVersion.this);
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

            @Override
            public void onLoadResource(WebView view, String url) {
                super.onLoadResource(view, url);

                webView.setDownloadListener(new DownloadListener() {
                    public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(url));
                        startActivity(i);
                    }
                });
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

        CookieSyncManager.getInstance().sync();

        webView.loadUrl(url);
    }
}
