package com.hrapps;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hrapps.CSC_Britannia.OfficialInfoModel;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import Fragments.InfoFragment;
import Model.UserProfile;
import Utility.Actions;
import Utility.AppBarStateChangeListener;
import Utility.AsyncResponse;
import Utility.Base64;
import Utility.CWIncturePreferences;
import Utility.CWUrls;
import Utility.Constants;
import Utility.NetworkConnector;
import Utility.Util;

public class UserProfileActivity extends BasicActivity implements AsyncResponse {

    FloatingActionMenu actionMenu;
    ImageView circularMenu, profileImage_edit;
    private Toolbar mToolbar;
    private TextView title_view, firstName, secondName;
    private AppBarLayout app_bar;

    private ImageView background_profile;
    UserProfile user = new UserProfile();
    private static final int SELECT_FILE = 1222;
    private static final int REQUEST_CAMERA = 1888;

    ImageLoader imageLoader;
    String userId, fname, lname;
    private String imageStream;

    private TabLayout tabLayout;
    InfoFragment infoFragment;
    ArrayList<OfficialInfoModel> officialinfoList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_profile);

        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Info"));

        Intent intent = getIntent();
        userId = intent.getStringExtra("userid");
        fname = intent.getStringExtra("firstname");
        lname = intent.getStringExtra("lastname");


        infoFragment = new InfoFragment();

        Bundle bundle = new Bundle();
        bundle.putString("userId", userId);

        infoFragment.setArguments(bundle);

        android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.frame_layout, infoFragment);
        ft.commit();

        imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(UserProfileActivity.this));


        background_profile = (ImageView) findViewById(R.id.background_profile);
        profileImage_edit = (ImageView) findViewById(R.id.profileImage_edit);
        title_view = (TextView) findViewById(R.id.title);
        firstName = (TextView) findViewById(R.id.firstName);
        secondName = (TextView) findViewById(R.id.secondName);

        firstName.setText(CWIncturePreferences.getFirstname());
        secondName.setText(CWIncturePreferences.getLastname());

        circularMenu = (ImageView) findViewById(R.id.arcmenu);

        ImageView image_new = (ImageView) findViewById(R.id.image_new);

        if (userId == null) {
            userId = CWIncturePreferences.getUserId();
            String fullName = CWIncturePreferences.getFirstname() + " " + CWIncturePreferences.getLastname();
            title_view.setText(fullName);

        } else {

            firstName.setText(Util.capitalizeWords(fname));
            secondName.setText(Util.capitalizeWords(lname));
            title_view.setText(fname);

            tabLayout.setVisibility(View.GONE);
            profileImage_edit.setVisibility(View.GONE);

            circularMenu.setVisibility(View.GONE);
            image_new.setVisibility(View.GONE);
            SubActionButton.Builder itemBuilder = new SubActionButton.Builder(this);

            ImageView call = new ImageView(this);
            call.setBackgroundResource(R.mipmap.call_purple);
            SubActionButton button1 = itemBuilder.setContentView(call).build();

            ImageView sms = new ImageView(this);
            sms.setBackgroundResource(R.mipmap.chat_purple);
            SubActionButton button2 = itemBuilder.setContentView(sms).build();

            ImageView send_email = new ImageView(this);
            send_email.setBackgroundResource(R.mipmap.email_purple);
            SubActionButton button3 = itemBuilder.setContentView(send_email).build();

            ImageView hangout = new ImageView(this);
            hangout.setBackgroundResource(R.mipmap.hangouts_purple);
            SubActionButton button4 = itemBuilder.setContentView(hangout).build();

            button1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (ContextCompat.checkSelfPermission(UserProfileActivity.this,
                            Manifest.permission.CALL_PHONE)
                            != PackageManager.PERMISSION_GRANTED) {

                        {
                            ActivityCompat.requestPermissions(UserProfileActivity.this,
                                    new String[]{Manifest.permission.CALL_PHONE},
                                    Constants.MY_PERMISSIONS_REQUEST_PHONE);

                        }
                    } else {
                        new Actions(UserProfileActivity.this, "CALL", user, "").onClick(null);
                    }

                }
            });

            button2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (ContextCompat.checkSelfPermission(UserProfileActivity.this,
                            Manifest.permission.SEND_SMS)
                            != PackageManager.PERMISSION_GRANTED) {

                        {
                            ActivityCompat.requestPermissions(UserProfileActivity.this,
                                    new String[]{Manifest.permission.SEND_SMS},
                                    Constants.MY_PERMISSIONS_REQUEST_PHONE);

                        }
                    } else {
                        new Actions(UserProfileActivity.this, "SMS", user, "").onClick(null);
                    }

                }
            });
            button3.setOnClickListener(new Actions(UserProfileActivity.this, "EMAIL", user, ""));
            button4.setOnClickListener(new Actions(UserProfileActivity.this, "HANGOUTS", user, ""));

            actionMenu = new FloatingActionMenu.Builder(this)
                    .addSubActionView(button1)
                    .addSubActionView(button2)
                    .addSubActionView(button3)
                    .addSubActionView(button4)
                    .attachTo(circularMenu)

                    .build();
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        app_bar = (AppBarLayout) findViewById(R.id.app_bar_layout);
        setSupportActionBar(mToolbar);

        toolbar.setNavigationIcon(getResources().getDrawable(R.mipmap.ic_arrow_back_white));


        mToolbar.setNavigationIcon(getResources().getDrawable(R.mipmap.ic_arrow_back_white));
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }


        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // onBackPressed();
                Intent intent1 = new Intent(UserProfileActivity.this, MainActivity.class);
                intent1.putExtra("pos", "no");
                startActivity(intent1);
            }
        });


        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {


            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                if (tab.getPosition() == 0) {

                    android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.show(infoFragment);

                    ft.commit();

                } else if (tab.getPosition() == 1) {

                    android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.hide(infoFragment);

                    ft.commit();
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        app_bar.addOnOffsetChangedListener(new AppBarStateChangeListener() {
            @Override
            public void onStateChanged(AppBarLayout appBarLayout, AppBarStateChangeListener.State state) {
                Log.d("STATE", state.name());
                if (state == State.COLLAPSED) {
                    title_view.setVisibility(View.VISIBLE);
                } else {
                    title_view.setVisibility(View.GONE);
                }
            }
        });

        if (Util.isOnline(this)) {
            Map<String, String> headers = new HashMap<>();


            headers.put("x-email-id", CWIncturePreferences.getEmail());
            headers.put("x-access-token",
                    CWIncturePreferences.getAccessToken());
            try {
                headers.put("android-version", getPackageManager().getPackageInfo(getPackageName(), 0).versionName);
            } catch (PackageManager.NameNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            NetworkConnector connect = new NetworkConnector(this, NetworkConnector.TYPE_GET, CWUrls.GET_USER_PROFILE + userId, headers, null, this);
            if (connect.isAllowed()) {
                connect.execute();
            } else {
                Toast.makeText(UserProfileActivity.this, getString(R.string.action_not_allowed), Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
            onBackPressed();
        }

        profileImage_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });
    }

    @Override
    public void processFinish(String output, int status_code, String url, int type) {

        if (url.contains(CWUrls.GET_USER_PROFILE)) {
            try {
                JSONObject responseObj = new JSONObject(output);
                if (responseObj.getString("status").equalsIgnoreCase("success")) {
                    if (type == NetworkConnector.TYPE_GET) {
                        JSONObject profile = new JSONObject(responseObj.getString("data"));
                        if (profile != null) {

                            user.setName(Util.capitalizeWords(profile.optString("firstName")) + " " + Util.capitalizeWords(profile.getString("lastName")));
                            user.setDesignation(Util.capitalizeWords(profile.optString("designation")));
                            user.setProfile_img(profile.optString("avatar"));
                            if (userId == CWIncturePreferences.getUserId()) {
                                CWIncturePreferences.init(UserProfileActivity.this);
                                CWIncturePreferences.setAvatarurl(profile.optString("avatar"));

                            }


                            JSONObject personalObj = profile.optJSONObject("officialInformation");
                            if (personalObj != null) {

                                OfficialInfoModel officialInfoModel = new OfficialInfoModel();

                                officialInfoModel.emp_id = personalObj.optString("id");
                                officialInfoModel.sap_id = personalObj.optString("sapId");
                                officialInfoModel.department = personalObj.optString("organizationalUnit");
                                officialInfoModel.designation = personalObj.optString("designation");

                                officialinfoList.add(officialInfoModel);

                                // user.getPersonalDetails().clear();
                                // Iterator<?> keys = personalObj.keys();

//                                while (keys.hasNext()) {
//                                    String key = (String) keys.next();
//                                    if (key.contains("designation")) {
//                                        if (!personalObj.getString(key).equalsIgnoreCase("null")) {
//                                            mobile_number = personalObj.getString(key);
//                                            user.setPhone(mobile_number);
//                                        }
//                                    }
//                                    else if (key.contains("sapId")) {
//                                        location = personalObj.getString(key);
//                                    } else if (key.contains("id")) {
//                                        maritalStatus = personalObj.getString(key);
//                                    }
//
//                                    UserProfileDetail personalDetails1 = new UserProfileDetail(personalObj.getString(key), key, "");
//                                    user.getPersonalDetails().add(personalDetails1);
//                                }
                            }
                            if (type == NetworkConnector.TYPE_GET) {
                                title_view.setText(user.getName());
                                if (user.getProfile_img() != null && !user.getProfile_img().isEmpty() && !user.getProfile_img().contains("null")) {
                                    Util.resizeAndLoadImageWithPlaceholder(this, user.getProfile_img(), background_profile, background_profile.getMeasuredWidth(), background_profile.getMeasuredHeight(), R.mipmap.defaultmedium);
                                }

                            }
                        }
                    } else {
                        JSONObject profile = new JSONObject(responseObj.getString("data"));
                        if (profile != null) {

                            CWIncturePreferences.init(UserProfileActivity.this);
                            CWIncturePreferences.setAvatarurl(profile.optString("avatar"));
                        }
                    }
                } else if (!responseObj.getString("message").isEmpty()) {

                    Toast.makeText(this, responseObj.getString("message"), Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


    }

    @Override
    public void configurationUpdated(boolean configUpdated) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == REQUEST_CAMERA) {

            Bundle extras = data.getExtras();
            Bitmap bmp = (Bitmap) extras.get("data");

            String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Cherrywork/";


            try {

                File imageDirectory = new File(path);
                imageDirectory.mkdirs();

                String Image_path = path + String.valueOf(System.currentTimeMillis()) + ".jpg";

                try {
                    FileOutputStream fOut = new FileOutputStream(Image_path);
                    bmp.compress(Bitmap.CompressFormat.JPEG, 85, fOut);
                    fOut.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                ExifInterface exif = new ExifInterface(Image_path);
                int exifOrientation = exif.getAttributeInt(
                        ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

                int rotationInDegrees = exifToDegrees(exifOrientation);
                Matrix matrix = new Matrix();
                if (exifOrientation != 0f) {
                    matrix.preRotate(rotationInDegrees);
                }

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                if (bmp != null)
                    bmp.compress(Bitmap.CompressFormat.JPEG, 85, stream);
                byte[] byteArray = stream.toByteArray();
                imageStream = Base64
                        .encodeBytes(byteArray);


                imageLoader.displayImage("file://" + Image_path, background_profile, new SimpleImageLoadingListener() {

                    @Override
                    public void onLoadingStarted(String imageUri, View view) {
                        background_profile
                                .setImageResource(R.color.light_grey);
                        super.onLoadingStarted(imageUri, view);
                    }
                });

                if (Util.isOnline(this)) {

                    Map<String, String> headers = new HashMap<>();

                    headers.put("x-device-id",
                            CWIncturePreferences.getDeviceToken());

                    headers.put("x-email-id", CWIncturePreferences.getEmail());
                    headers.put("x-access-token",
                            CWIncturePreferences.getAccessToken());
                    headers.put("Content-Type", "application/json");
                    try {
                        headers.put("android-version", getPackageManager().getPackageInfo(getPackageName(), 0).versionName);
                    } catch (PackageManager.NameNotFoundException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                    JSONObject image = new JSONObject();
                    image.put("avatar", imageStream);
                    //4
                    NetworkConnector connect = new NetworkConnector(UserProfileActivity.this, NetworkConnector.TYPE_PUT, CWUrls.UPDATE_PROFILE + CWIncturePreferences.getUserId(), headers, image.toString(), UserProfileActivity.this);
                    if (connect.isAllowed()) {
                        connect.execute();
                    } else {
                        Toast.makeText(UserProfileActivity.this, getString(R.string.action_not_allowed), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(UserProfileActivity.this, getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (resultCode == RESULT_OK && requestCode == SELECT_FILE) {

            Uri selectedImageUri = data.getData();

            String tempPath = getPath(selectedImageUri,
                    UserProfileActivity.this);

            Bitmap bm;

            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(tempPath, options);
            options.inSampleSize = calculateInSampleSize(options, 250, 250);

            // Decode bitmap with inSampleSize set
            options.inJustDecodeBounds = false;
            bm = BitmapFactory.decodeFile(tempPath, options);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();

            bm.compress(Bitmap.CompressFormat.JPEG, 85, stream);
            byte[] byteArray = stream.toByteArray();
            imageStream = Base64.encodeBytes(byteArray);

            imageLoader.displayImage(String.valueOf(selectedImageUri), background_profile, new SimpleImageLoadingListener() {

                @Override
                public void onLoadingStarted(String imageUri, View view) {

                    super.onLoadingStarted(imageUri, view);
                }
            });

            if (Util.isOnline(this)) {

                Map<String, String> headers = new HashMap<>();

                headers.put("x-device-id",
                        CWIncturePreferences.getDeviceToken());

                headers.put("x-email-id", CWIncturePreferences.getEmail());
                headers.put("x-access-token",
                        CWIncturePreferences.getAccessToken());
                headers.put("Content-Type", "application/json");
                try {
                    headers.put("android-version", getPackageManager().getPackageInfo(getPackageName(), 0).versionName);
                } catch (PackageManager.NameNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                JSONObject image = new JSONObject();
                try {
                    image.put("avatar", imageStream);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                NetworkConnector connect = new NetworkConnector(UserProfileActivity.this, NetworkConnector.TYPE_PUT, CWUrls.UPDATE_PROFILE + CWIncturePreferences.getUserId(), headers, image.toString(), UserProfileActivity.this);
                if (connect.isAllowed()) {
                    connect.execute();
                } else {
                    Toast.makeText(UserProfileActivity.this, getString(R.string.action_not_allowed), Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(UserProfileActivity.this, getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
            }

        }
    }

    private String tryingItout(Uri selectedImageUri) {
        InputStream is = null;
        if (selectedImageUri.getAuthority() != null) {
            try {
                is = getContentResolver().openInputStream(selectedImageUri);
                Bitmap bmp = BitmapFactory.decodeStream(is);
                return writeToTempImageAndGetPathUri(UserProfileActivity.this, bmp).toString();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } finally {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;


    }

    public static String writeToTempImageAndGetPathUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return path;
    }

    private static int exifToDegrees(int exifOrientation) {
        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) {
            return 90;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {
            return 180;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {
            return 270;
        } else if (exifOrientation == ExifInterface.ORIENTATION_NORMAL) {
            return 90;
        }
        return 0;
    }

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public String getPath(Uri uri, Activity activity) {
        String[] projection = {MediaStore.MediaColumns.DATA};
        Cursor cursor = activity
                .managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    private void selectImage() {
        final CharSequence[] items = {"Camera", "Choose from Gallery", "Remove",
                "Cancel"};


        LayoutInflater li = LayoutInflater.from(UserProfileActivity.this);
        final View image_edit = li.inflate(R.layout.profile_image_edit_dialog, null);

        TextView camera = (TextView) image_edit.findViewById(R.id.camera);
        TextView gallery = (TextView) image_edit.findViewById(R.id.gallery);
        TextView remove = (TextView) image_edit.findViewById(R.id.remove);
        TextView cancel = (TextView) image_edit.findViewById(R.id.cancel);


        final AlertDialog.Builder builder = new AlertDialog.Builder(UserProfileActivity.this);
        builder.setView(image_edit);

//        builder.setItems(items, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int item) {
//                if (items[item].equals("Camera"))  else if (items[item].equals("Choose from Gallery"))  else if (items[item].equals("Cancel")) {
//                    dialog.dismiss();
//                } else if (items[item].equals("Remove")) {
//
//                    AlertDialog alertDialog;
//                    AlertDialog.Builder alert = new AlertDialog.Builder(UserProfileActivity.this);
//
//                    alert.setTitle("Remove the Profile Image!");
//                    alert.setMessage("Are you sure you want to remove the Profile Image ?");
//
//                    alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
//
//                        public void onClick(DialogInterface dialog1, int id) {
//                            dialog1.cancel();
//
//                        }
//                    });
//
//                    alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog1, int id) {
//                                    background_profile.setImageResource(R.mipmap.incture_settings_img_gradient);
//
//                                    if (Util.isOnline(UserProfileActivity.this)) {
//
//                                        Map<String, String> headers = new HashMap<>();
//
//                                        headers.put("x-device-id",
//                                                CWIncturePreferences.getDeviceToken());
//
//                                        headers.put("x-email-id", CWIncturePreferences.getEmail());
//                                        headers.put("x-access-token",
//                                                CWIncturePreferences.getAccessToken());
//                                        headers.put("Content-Type", "application/json");
//                                        try {
//                                            headers.put("android-version",getPackageManager().getPackageInfo(getPackageName(), 0).versionName);
//                                        } catch (PackageManager.NameNotFoundException e) {
//                                            // TODO Auto-generated catch block
//                                            e.printStackTrace();
//                                        }
//
//                                        JSONObject image = new JSONObject();
//
//                                        imageStream = "";
//
//                                        try {
//                                            image.put("avatar", imageStream);
//                                        } catch (JSONException e) {
//                                            e.printStackTrace();
//                                        }
//                                        //for profile pic removed update
//                                        NetworkConnector connect = new NetworkConnector(UserProfileActivity.this, NetworkConnector.TYPE_PUT, CWUrls.UPDATE_PROFILE + CWIncturePreferences.getUserId(), headers, image.toString(), UserProfileActivity.this);
//                                        if (connect.isAllowed()) {
//                                            connect.execute();
//                                        } else {
//                                            Toast.makeText(UserProfileActivity.this,  getString(R.string.action_not_allowed), Toast.LENGTH_SHORT).show();
//                                        }
//                                        dialog1.cancel();
//                                    } else {
//                                        Toast.makeText(UserProfileActivity.this,  getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
//                                    }
//
//                                }
//                            }
//
//                    );
//
//                    alertDialog = alert.create();
//                    alertDialog.show();
//
//                }
//            }
//         });

        //   builder.show();

        final AlertDialog dialog = builder.create();
        dialog.show();

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                {

                    if (ContextCompat.checkSelfPermission(UserProfileActivity.this,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED) {


                        ActivityCompat.requestPermissions(UserProfileActivity.this,
                                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                Constants.MY_PERMISSIONS_REQUEST_CAMERA);

                    } else if (ContextCompat.checkSelfPermission(UserProfileActivity.this,
                            Manifest.permission.CAMERA)
                            != PackageManager.PERMISSION_GRANTED) {


                        ActivityCompat.requestPermissions(UserProfileActivity.this,
                                new String[]{Manifest.permission.CAMERA},
                                Constants.MY_PERMISSIONS_REQUEST_CAMERA);


                    } else {
                        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                        startActivityForResult(intent, REQUEST_CAMERA);


                    }

                }
            }

        });

        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                {


                    if (ContextCompat.checkSelfPermission(UserProfileActivity.this,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED) {


                        ActivityCompat.requestPermissions(UserProfileActivity.this,
                                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                Constants.MY_PERMISSIONS_REQUEST_GALLERY);


                    } else {
                        Intent intent = new Intent(
                                Intent.ACTION_PICK,
                                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        intent.setType("image/*");
                        intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                        startActivityForResult(
                                Intent.createChooser(intent, "Select File"),
                                SELECT_FILE);
                    }


                }
            }
        });


        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });


        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                LayoutInflater li = LayoutInflater.from(UserProfileActivity.this);
                final View image_remove = li.inflate(R.layout.profile_image_remove_dialog, null);

                TextView yes = (TextView) image_remove.findViewById(R.id.yes);
                TextView no = (TextView) image_remove.findViewById(R.id.no);

                final AlertDialog alertDialog;
                AlertDialog.Builder alert = new AlertDialog.Builder(UserProfileActivity.this);
                alert.setView(image_remove);

                alertDialog = alert.create();
                alertDialog.show();

                yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        background_profile.setImageResource(R.mipmap.incture_settings_img_gradient);

                        if (Util.isOnline(UserProfileActivity.this)) {

                            Map<String, String> headers = new HashMap<>();

                            headers.put("x-device-id",
                                    CWIncturePreferences.getDeviceToken());

                            headers.put("x-email-id", CWIncturePreferences.getEmail());
                            headers.put("x-access-token",
                                    CWIncturePreferences.getAccessToken());
                            headers.put("Content-Type", "application/json");
                            try {
                                headers.put("android-version", getPackageManager().getPackageInfo(getPackageName(), 0).versionName);
                            } catch (PackageManager.NameNotFoundException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }

                            JSONObject image = new JSONObject();

                            imageStream = "";

                            try {
                                image.put("avatar", imageStream);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            //for profile pic removed update
                            NetworkConnector connect = new NetworkConnector(UserProfileActivity.this, NetworkConnector.TYPE_PUT, CWUrls.UPDATE_PROFILE + CWIncturePreferences.getUserId(), headers, image.toString(), UserProfileActivity.this);
                            if (connect.isAllowed()) {
                                connect.execute();
                            } else {
                                Toast.makeText(UserProfileActivity.this, getString(R.string.action_not_allowed), Toast.LENGTH_SHORT).show();
                            }
                            alertDialog.cancel();
                        } else {
                            Toast.makeText(UserProfileActivity.this, getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
                        }

                    }
                });

                no.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.cancel();
                    }
                });

            }
        });
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case Constants.MY_PERMISSIONS_REQUEST_CAMERA: {
                // If request is cancelled, the result arrays are empty.

                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {


                    Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                    startActivityForResult(intent, REQUEST_CAMERA);
                }
                return;
            }

            case Constants.MY_PERMISSIONS_REQUEST_GALLERY:

                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Intent intent = new Intent(
                            Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(
                            Intent.createChooser(intent, "Select File"),
                            SELECT_FILE);

                }
                return;

            case Constants.MY_PERMISSIONS_REQUEST_PHONE:

                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    new Actions(UserProfileActivity.this, "CALL", user, "").onClick(null);

                }
                return;

            case Constants.MY_PERMISSIONS_REQUEST_SMS:

                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    new Actions(UserProfileActivity.this, "SMS", user, "").onClick(null);

                }
                return;


        }
    }


}
