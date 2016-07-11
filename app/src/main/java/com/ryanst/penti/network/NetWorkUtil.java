package com.ryanst.penti.network;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetWorkUtil {

    public enum NetType {NO_NETWORK_CONNECTED, WIFI_CONNECTED, OTHER_NETWORK_CONNECTED}

    public static boolean isConnected(Context context) {

        if (context.checkCallingOrSelfPermission(Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
            return false;
        }

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    public static NetType getNetType(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();

        if (networkInfo == null || !networkInfo.isConnected()) {
            return NetType.NO_NETWORK_CONNECTED;
        }

        int networkType = networkInfo.getType();
        if (networkType == ConnectivityManager.TYPE_WIFI) {
            return NetType.WIFI_CONNECTED;
        } else {
            return NetType.OTHER_NETWORK_CONNECTED;
        }
    }

    public static String getNetTypeString(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();

        if (networkInfo == null || !networkInfo.isConnected()) {
            return "Net not connect";
        }

        int networkType = networkInfo.getType();
        if (networkType == ConnectivityManager.TYPE_WIFI) {
            return "Wifi";
        } else {
            return "4G";
        }
    }
}