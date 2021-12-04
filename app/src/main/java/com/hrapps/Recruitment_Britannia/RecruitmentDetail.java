package com.hrapps.Recruitment_Britannia;

import android.Manifest;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hrapps.BasicActivity;
import com.hrapps.Login;
import com.hrapps.MainActivity;
import com.hrapps.R;

import org.w3c.dom.Text;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import Utility.CWIncturePreferences;
import Utility.CWUrls;
import Utility.Constants;

/**
 * Created by harshu on 1/19/2017.
 */

public class RecruitmentDetail extends BasicActivity {

    private ProgressDialog progressDialog;
    String hrFunction = "";
    WebView webView;
    ImageView back;
    TextView title;

    String req_id = "";
    String applicationId = "";
    String roleName = "";
    String regionName = "";
    String email = "";
    String globalUrl = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recruitment_detail);

        webView = (WebView) findViewById(R.id.webview);
        back = (ImageView) findViewById(R.id.back);
        title = (TextView) findViewById(R.id.title);


        CWIncturePreferences.init(RecruitmentDetail.this);

        Intent intent = getIntent();

        req_id = intent.getStringExtra("req_id");
        String applicationType = intent.getStringExtra("applicationType");
        roleName = intent.getStringExtra("roleName");
        regionName = intent.getStringExtra("regionName");
        hrFunction = intent.getStringExtra("hrFunction");
        applicationId = intent.getStringExtra("applicationId");
        String offerId = intent.getStringExtra("offerId");
        email = intent.getStringExtra("email");

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
            Intent toLogin = new Intent(RecruitmentDetail.this, Login.class);
            startActivity(toLogin);
        } else {
            if (applicationType.equalsIgnoreCase("requisition")) {
                startWebView(CWUrls.DOMAIN_WEB_BRIT + "/#/mobile/recruitment/recruitment-description?id=" + req_id, roleName, regionName);
            } else if (applicationType.equalsIgnoreCase("updateRequisition")) {
                startWebView(CWUrls.DOMAIN_WEB_BRIT + "/#/mobile/recruitment/update-non-budgeted-requisition?id=" + req_id + "&subtype=nonBudgeted", roleName, regionName);

            } else if (applicationType.equalsIgnoreCase("profileEvaluation")) {
                startWebView(CWUrls.DOMAIN_WEB_BRIT + "/#/mobile/recruitment/recruitment-candidatedetailview?id=" + req_id + "&applicationId=" + applicationId + "&email=" + email, roleName, regionName);
            } else if (applicationType.equalsIgnoreCase("postIJP") || applicationType.equalsIgnoreCase("postER")) {
                startWebView(CWUrls.DOMAIN_WEB_BRIT + "/#/mobile/recruitment/IJP", roleName, regionName);
            } else if (applicationType.equalsIgnoreCase("interviewFeedback")) {
                startWebView(CWUrls.DOMAIN_WEB_BRIT + "/#/mobile/recruitment/shortlistdetails?id=" + req_id + "&applicationId=" + applicationId + "&email=" + email, roleName, regionName);
            } else if (applicationType.equalsIgnoreCase("offerRollout")) {

                if (roleName.equalsIgnoreCase("C & B HEAD") || roleName.equalsIgnoreCase("VPHR")) {
                    Intent i = new Intent(RecruitmentDetail.this, MainActivity.class);
                    startActivity(i);
                } else if (roleName.equalsIgnoreCase("HRBP")) {
                    startWebView(CWUrls.DOMAIN_WEB_BRIT + "/#/mobile/recruitment/offered", roleName, regionName);
                }

            } else if (applicationType.equalsIgnoreCase("generateOffer")) {
                startWebView(CWUrls.DOMAIN_WEB_BRIT + "/#/mobile/recruitment/recruitment-offereddetail?Cid=" + offerId, roleName, regionName);
            } else if (applicationType.equalsIgnoreCase("applicationEvaluation")) {
                startWebView(CWUrls.DOMAIN_WEB_BRIT + "/#/mobile/recruitment/shortlistdetails?id=" + req_id + "&applicationId=" + applicationId + "&email=" + email, roleName, regionName);
            } else if (applicationType.equalsIgnoreCase("approveRequisition")) {
                Intent inbox = new Intent(RecruitmentDetail.this, MainActivity.class);
                startActivity(inbox);
            }//end of approveRequisition

            else if (applicationType.equalsIgnoreCase("sourceRequisition")) {
                startWebView(CWUrls.DOMAIN_WEB_BRIT + "/#/mobile/recruitment/recruitment-sourcing?id=" + req_id, roleName, regionName);
            } else if (applicationType.equalsIgnoreCase("uploadDocuments")) {
                startWebView(CWUrls.DOMAIN_WEB_BRIT + "/#/mobile/recruitment/shortlistdetails?id=" + req_id + "&applicationId=" + applicationId + "&email=" + email, roleName, regionName);
            }
        }

    }

    private void startWebView(String url, final String roleName, final String regionName) {

        //Create new webview Client to show progress dialog
        //When opening a url or click on link


        if (progressDialog == null) {
            // in standard case YourActivity.this
            progressDialog = new ProgressDialog(RecruitmentDetail.this);
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

                globalUrl = url;

                try {

                    if (url.contains(CWUrls.DOMAIN_WEB_BRIT + "/#/mobile/recruitment/recruitment-description?id=")) {
                        title.setText("Requisition");
                    } else if (url.contains(CWUrls.DOMAIN_WEB_BRIT + "/#/mobile/recruitment/update-non-budgeted-requisition?id=")) {
                        title.setText("Update Requisition");
                    } else if (url.contains(CWUrls.DOMAIN_WEB_BRIT + "/#/mobile/recruitment/recruitment-candidatedetailview?id=")) {
                        title.setText("Profile Evaluation");
                    } else if (url.contains(CWUrls.DOMAIN_WEB_BRIT + "/#/mobile/recruitment/IJP")) {
                        title.setText("IJP");
                    } else if (url.contains(CWUrls.DOMAIN_WEB_BRIT + "/#/mobile/recruitment/shortlistdetails?id=")) {
                        title.setText("Feedback");
                    } else if (url.contains(CWUrls.DOMAIN_WEB_BRIT + "/#/mobile/recruitment/offered")) {
                        title.setText("Offer");
                    } else if (url.contains(CWUrls.DOMAIN_WEB_BRIT + "/#/mobile/recruitment/recruitment-offereddetail?Cid=")) {
                        title.setText("Offer");
                    } else if (url.contains(CWUrls.DOMAIN_WEB_BRIT + "/#/mobile/recruitment/recruitment-sourcing?id=")) {
                        title.setText("Requisition");
                    } else if (url.contains("resumeDownload")) {
                        String decodedURL = "";
                        try {
                            decodedURL = URLDecoder.decode(url, "UTF-8");
                            Log.d("urldecoded=", "" + decodedURL);

                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }

                        if (ContextCompat.checkSelfPermission(RecruitmentDetail.this,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                != PackageManager.PERMISSION_GRANTED) {


                            ActivityCompat.requestPermissions(RecruitmentDetail.this,
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

                    }//end of resumeDownload


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
