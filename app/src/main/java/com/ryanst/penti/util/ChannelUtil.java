package com.ryanst.penti.util;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.telephony.TelephonyManager;

/**
 * Created by zhengjuntong on 16/3/22.
 */
public class ChannelUtil {

    /**
     * 获取设备的imei信息
     *
     * @param context the context
     * @return imei
     */
    public static String getImei(Context context) {
        String imei = "";
        try {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            imei = telephonyManager.getDeviceId();
        } catch (Exception e) {
        }
        return imei;
    }

    /**
     *
     * Gets meta channel.
     *
     * @param activity    the activity
     * @param channelName just like "UMENG_CHANNEL"
     * @return meta channel
     */
    public static String getMetaChannel(Context activity, String channelName) {
        String channel = "";
        try {
            ApplicationInfo info = activity.getPackageManager()
                    .getApplicationInfo(activity.getPackageName(),
                            PackageManager.GET_META_DATA);
            channel = "" + info.metaData.get(channelName);
        } catch (Exception e) {
            e.printStackTrace();
        }
//        Toast.makeText(activity, "channel is " + channel, Toast.LENGTH_LONG).show();
        return channel;

    }

    /**
     * Gets channel.
     *
     * @param context the context
     * @return the channel
     */
    public static String getChannel(Context context) {
        String channel = getMetaChannel(context, "UMENG_CHANNEL");

        return channel;
    }
}
