package com.erick.hotweather.network;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by erickxhyang on 2018/9/18.
 */

public class NetworkChangeReceiver extends BroadcastReceiver {

    private NetworkChangeListener mListener;
    private boolean hasRegistered = false;

    public NetworkChangeReceiver(NetworkChangeListener listener) {
        mListener = listener;
    }

    public interface NetworkChangeListener {
        void onNetworkChange(Intent intent, boolean isConnected);
    }

    public void register(Context context) {
        if (!hasRegistered && context != null) {
            context.registerReceiver(this, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
            hasRegistered = true;
        }
    }

    public void unregister(Context context) {
        if (hasRegistered && context != null) {
            context.unregisterReceiver(this);
            hasRegistered = false;
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = null;
        if (connectivityManager != null) {
            networkInfo = connectivityManager.getActiveNetworkInfo();
        }
        if (networkInfo == null || !networkInfo.isConnected()) {
            mListener.onNetworkChange(intent, false);
        } else {
            mListener.onNetworkChange(intent, true);
        }
    }

}
