package com.erick.hotweather.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.URLUtil;

/**
 * Created by erickxhyang on 2018/9/23.
 */

public class NetworkUtil {

    private final static String TAG = "NetworkUtil";

    private NetworkUtil() {
        // static use.
    }

    // ------------------ common -------------------
    public static boolean isNetworkAvailable(Context context) {
        NetworkInfo info = getActiveNetworkInfo(context);
        // 这里必须用isConnected,不能用avaliable，因为有网络的情况isAvailable也可能是false
        return info != null && info.isConnected();
    }

    public static boolean isWifiConnected(Context context) {
        if (context == null) {
            return false;
        }
        NetworkInfo activeNetworkInfo = getActiveNetworkInfo(context);
        return activeNetworkInfo != null && activeNetworkInfo.getType() == ConnectivityManager.TYPE_WIFI;
    }

    public static boolean isMobileConnected(Context context) {
        if (context == null) {
            return false;
        }
        NetworkInfo activeNetworkInfo = getActiveNetworkInfo(context);
        return activeNetworkInfo != null && activeNetworkInfo.getType() == ConnectivityManager.TYPE_MOBILE;
    }

    private static NetworkInfo getActiveNetworkInfo(Context context) {
        try {
            ConnectivityManager connMgr = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            return connMgr.getActiveNetworkInfo();
        } catch (Throwable e) {
            Log.e(TAG, "fail to get active network info", e);
            return null;
        }
    }

    public static boolean isNetworkUrl(String url) {
        //avoid spawn objects in URLUtil.isNetworkUrl
        if (TextUtils.isEmpty(url))
            return false;
        if (url.startsWith("http://") || url.startsWith("https://")) {
            return true;
        } else
            return !URLUtil.isFileUrl(url) && URLUtil.isNetworkUrl(url);
    }

}
