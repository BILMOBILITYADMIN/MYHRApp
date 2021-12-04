package Utility;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hrapps.Login;
import com.hrapps.R;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import DB.DbAdapter;

/**
 * Created by Deeksha on 25-01-2016.
 */
public class Util {
    Context ctx;
    public static String strSeparator = "__,__";

    public static boolean isOnline(Context _context) {
        ConnectivityManager cm = (ConnectivityManager) _context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }


    public static String convertArrayToString(ArrayList<String> array) {
        String str = "";
        for (int i = 0; i < array.size(); i++) {
            str = str + array.get(i);
            // Do not append comma at the end of last element
            if (i < array.size() - 1) {
                str = str + strSeparator;
            }
        }
        return str;
    }

    public static ArrayList<String> convertStringToArray(String str) {
        ArrayList<String> list = new ArrayList<String>();
        String[] array = str.split(strSeparator);
        for (int i = 0; i < array.length; i++) {
            list.add(array[i]);

        }
        return list;
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            return;

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(),
                View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth,
                        RelativeLayout.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (2 * (listAdapter.getCount() - 1)) + 50;
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    public void logoutfunc(Context context1) {

        CWIncturePreferences.init(context1);
        String logo = CWIncturePreferences.getLOGO();
        String configurl = CWIncturePreferences.getConfigUrl();
        CWIncturePreferences.resetPreferences();
        CWIncturePreferences.setAccessToken(null);
        CWIncturePreferences.setUsersId(null);
        CWIncturePreferences.setDeviceToken(null);
        CWIncturePreferences.setConfigUrl(configurl);
        CWIncturePreferences.setLogo(logo);
        DbAdapter.getDbAdapterInstance().delete(Constants.TAB_TABLE_NAME_TABS);
        DbAdapter.getDbAdapterInstance().delete(Constants.FILTERS_TABLE_NAME);
        DbAdapter.getDbAdapterInstance().delete(Constants.NAVIGATION_TABLE_NAME);
        DbAdapter.getDbAdapterInstance().delete(Constants.TASKS_TABLE_NAME_DETAIL);
        DbAdapter.getDbAdapterInstance().delete(Constants.TASKS_TABLE_NAME_SCROLL);
        DbAdapter.getDbAdapterInstance().delete(Constants.TASKS_TABLE_NAME);
        DbAdapter.getDbAdapterInstance().delete(Constants.TABLE_CARDS);

        Intent intent = new Intent(context1, Login.class);
        context1.startActivity(intent);
        ((Activity) context1).finish();
    }

    public static void loadImage(Context _context, String url, ImageView imageView) {
        if (isOnline(_context)) {
            Picasso.with(_context).load(url).fit().into(imageView);
        } else {
            Picasso.with(_context).load(url).fit().into(imageView);

        }
    }

    public static void loadImageWithPlaceHolder(Context _context, String url, ImageView imageView, int place_holder) {
        if (isOnline(_context)) {
            Picasso.with(_context).load(url).fit().placeholder(place_holder).into(imageView);
        } else {
            Picasso.with(_context).load(url).fit().placeholder(place_holder).into(imageView);

        }
    }


    public static void resizeAndLoadImage(Context _context, String url, ImageView imageView, int width, int height) {
        if (isOnline(_context)) {
            Picasso.with(_context).load(url).resize(width, height).into(imageView);
        } else {
            Picasso.with(_context).load(url).resize(width, height).into(imageView);

        }
    }


    public static void resizeAndLoadImageWithPlaceholder(Context _context, String url, ImageView imageView, int width, int height, int placeholder) {
        String URL = "";

        if (isOnline(_context)) {
            URL = CWUrls.IMAGE_FETCH_URL + url + "?email=" + CWIncturePreferences.getEmail();
            Picasso.with(_context).load(URL).resize(width, height).placeholder(placeholder).into(imageView);
        } else {
            Picasso.with(_context).load(url).resize(width, height).placeholder(placeholder).into(imageView);

        }
    }


    public static void loadImageForProfile(Context _context, String url, ImageView imageView) {
        if (isOnline(_context)) {
            Picasso.with(_context).load(url).fit().placeholder(R.mipmap.defaultmedium).into(imageView);
        } else {
            Picasso.with(_context).load(R.mipmap.defaultmedium).fit().into(imageView);
        }
    }

    public static void loadAttachmentImageLarge(Context _context, String url, ImageView imageView) {
        String URL = "";
        if (isConnectedFast(_context)) {
            URL = CWUrls.IMAGE_FETCH_URL + url + "?email=" + CWIncturePreferences.getEmail();
        } else {
            URL = CWUrls.IMAGE_FETCH_URL + url + "?email=" + CWIncturePreferences.getEmail();
        }
        if (url == null || url.isEmpty() || URL.contentEquals(CWUrls.IMAGE_FETCH_URL) || URL.contentEquals(CWUrls.IMAGE_FETCH_URL)) {
            imageView.setBackgroundResource(R.mipmap.incture_settings_img_gradient);
            imageView.setImageResource(R.mipmap.incture_settings_img_gradient);
        } else {
            loadImage(_context, URL, imageView);

        }
    }


    public static void loadAttachmentImageSquareForProfile(Context _context, String url, ImageView imageView) {

        String URL = CWUrls.IMAGE_FETCH_URL + url + "?email=" + CWIncturePreferences.getEmail();
        if (URL.contentEquals(CWUrls.IMAGE_FETCH_URL)) {
            imageView.setBackgroundResource(R.mipmap.incture_settings_img_gradient);
            imageView.setImageResource(R.mipmap.defaultmedium);
        } else {
            loadImageForProfile(_context, URL, imageView);
        }
    }

    public static void loadAttachmentImageMaterial(Context _context, String url, Utility.MaterialImageView imageView) {

        String URL = CWUrls.IMAGE_FETCH_URL + url + "?email=" + CWIncturePreferences.getEmail();
        if (URL.contentEquals(CWUrls.IMAGE_FETCH_URL)) {
            imageView.setBackgroundResource(R.mipmap.defaultmedium);
            imageView.setImageResource(R.mipmap.defaultmedium);
        } else {
            loadImageWithPlaceHolder(_context, URL, imageView, R.mipmap.defaultmedium);
        }
    }


    /**
     * Check if there is fast connectivity
     *
     * @param context
     * @return
     */
    public static boolean isConnectedFast(Context context) {
        NetworkInfo info = getNetworkInfo(context);
        return (info != null && info.isConnected() && isConnectionFast(info.getType(), info.getSubtype()));
    }

    /**
     * Check if the connection is fast
     *
     * @param type
     * @param subType
     * @return
     */
    public static boolean isConnectionFast(int type, int subType) {
        if (type == ConnectivityManager.TYPE_WIFI) {
            return true;
        } else if (type == ConnectivityManager.TYPE_MOBILE) {
            switch (subType) {
                case TelephonyManager.NETWORK_TYPE_1xRTT:
                    return false; // ~ 50-100 kbps
                case TelephonyManager.NETWORK_TYPE_CDMA:
                    return false; // ~ 14-64 kbps
                case TelephonyManager.NETWORK_TYPE_EDGE:
                    return false; // ~ 50-100 kbps
                case TelephonyManager.NETWORK_TYPE_EVDO_0:
                    return true; // ~ 400-1000 kbps
                case TelephonyManager.NETWORK_TYPE_EVDO_A:
                    return true; // ~ 600-1400 kbps
                case TelephonyManager.NETWORK_TYPE_GPRS:
                    return false; // ~ 100 kbps
                case TelephonyManager.NETWORK_TYPE_HSDPA:
                    return true; // ~ 2-14 Mbps
                case TelephonyManager.NETWORK_TYPE_HSPA:
                    return true; // ~ 700-1700 kbps
                case TelephonyManager.NETWORK_TYPE_HSUPA:
                    return true; // ~ 1-23 Mbps
                case TelephonyManager.NETWORK_TYPE_UMTS:
                    return true; // ~ 400-7000 kbps
                /*
                 * Above API level 7, make sure to set android:targetSdkVersion
                 * to appropriate level to use these
                 */
                case TelephonyManager.NETWORK_TYPE_EHRPD: // API level 11
                    return true; // ~ 1-2 Mbps
                case TelephonyManager.NETWORK_TYPE_EVDO_B: // API level 9
                    return true; // ~ 5 Mbps
                case TelephonyManager.NETWORK_TYPE_HSPAP: // API level 13
                    return true; // ~ 10-20 Mbps
                case TelephonyManager.NETWORK_TYPE_IDEN: // API level 8
                    return false; // ~25 kbps
                case TelephonyManager.NETWORK_TYPE_LTE: // API level 11
                    return true; // ~ 10+ Mbps
                // Unknown
                case TelephonyManager.NETWORK_TYPE_UNKNOWN:
                default:
                    return false;
            }
        } else {
            return false;
        }
    }

    /**
     * Get the network info
     *
     * @param context
     * @return
     */
    public static NetworkInfo getNetworkInfo(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo();
    }

    /**
     * initiate the imageloader
     */
    public static ImageLoader initImageLoader(Context _context) {
        ImageLoader imageLoader;
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheOnDisc().imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
                .bitmapConfig(Bitmap.Config.RGB_565).build();
        ImageLoaderConfiguration.Builder builder = new ImageLoaderConfiguration.Builder(
                _context).defaultDisplayImageOptions(defaultOptions).memoryCache(
                new WeakMemoryCache());

        ImageLoaderConfiguration config = builder.build();
        imageLoader = ImageLoader.getInstance();
        imageLoader.init(config);
        return imageLoader;
    }

    public static String capitalizeWords(String str) {
        if (str == null || str.isEmpty()) {
            return "";
        }
        StringBuilder b = new StringBuilder(str);
        int i = 0;
        do {
            if (b.length() > i + 1)
                b.replace(i, i + 1, b.substring(i, i + 1).toUpperCase());
            else {
                b.replace(i, i + 1, String.valueOf(Character.toUpperCase(b.charAt(i))));
            }
            i = b.indexOf(" ", i) + 1;
        } while (i > 0 && i < b.length());

        return b.toString();
    }

    public static String capitalizeSentence(String line) {
        if (line.length() > 1)
            return Character.toUpperCase(line.charAt(0)) + line.substring(1);
        else if (line.length() > 0) {
            return String.valueOf(Character.toUpperCase(line.charAt(0)));
        } else
            return "";
    }


    public void serverMaintenance(Context context) {

        ctx = context;
        AlertDialog.Builder maintenanceDialog = new AlertDialog.Builder(
                context);
        maintenanceDialog.setTitle("Sorry");
        maintenanceDialog.setMessage("Server is under maintenance");
        maintenanceDialog.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        ActivityCompat.finishAffinity((Activity) ctx);
                    }
                });

        AlertDialog server_maintain = maintenanceDialog.create();
        server_maintain.show();
    }

    public void appUpdate(Context context) {
        ctx = context;

        LayoutInflater li = LayoutInflater.from(ctx);
        final View updateView = li.inflate(R.layout.app_update_dialog, null);

        AlertDialog.Builder app_update = new AlertDialog.Builder(ctx);
        app_update.setView(updateView);

        TextView confirm_update = (TextView) updateView.findViewById(R.id.confirm_update);

        final AlertDialog update = app_update.create();
        update.setCanceledOnTouchOutside(false);
        update.show();

        confirm_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                update.cancel();
                Uri uri = Uri.parse("http://myhr.britindia.com/store/files/testing.apk");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                ctx.startActivity(intent);
            }
        });

    }
}
