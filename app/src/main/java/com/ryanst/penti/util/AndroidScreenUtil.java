package com.ryanst.penti.util;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.view.Window;

/**
 * Created by kevin on 16/3/11.
 */
public class AndroidScreenUtil {
    /**
     * 获取屏幕的长宽point
     *
     * @param activity
     * @return
     */
    public static Point screenDisplay(Context activity) {
        DisplayMetrics metrics = activity.getResources().getDisplayMetrics();
        Point point = new Point();
        point.x = metrics.widthPixels;
        return point;
    }

    /**
     * 获取屏幕
     *
     * @param activity
     * @return
     */
    public static int getScreenStatusBarHeight(Activity activity) {
        Rect frame = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        return frame.top;
    }

    /**
     * 获取屏幕的密度
     *
     * @param activity
     * @return
     */
    public static float getScreenDensity(Activity activity) {
        DisplayMetrics metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        return metrics.density;
    }

    /**
     * 获取屏幕密度dpi
     *
     * @param activity
     * @return
     */
    public static int getScreenDpi(Activity activity) {
        DisplayMetrics metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        return metrics.densityDpi;
    }

    /**
     * 获取标题栏高度
     *
     * @param activity
     * @return
     */
    public static int getTitleBarHeight(Activity activity) {
        Rect rect = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
        int sbh = rect.top;
        int contentTop = activity.getWindow().findViewById(Window.ID_ANDROID_CONTENT).getTop();
        int titleBarTop = contentTop - sbh;
        return titleBarTop;
    }

    /**
     * 获取屏幕的高宽比
     *
     * @param context
     * @return
     */
    public static float getScreenRate(Context context) {
        Point P = getScreenMetrics(context);
        float H = P.y;
        float W = P.x;
        return (H / W);
    }

    /**
     * 获取屏幕的宽高
     *
     * @param context
     * @return
     */
    public static Point getScreenMetrics(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        int w_screen = dm.widthPixels;
        int h_screen = dm.heightPixels;
        return new Point(w_screen, h_screen);

    }

    public static float getScreenDensity(Context context) {
        return context.getResources().getDisplayMetrics().density;
    }

    public static int dip2px(Context context, int dip) {
        return (int) (getScreenDensity(context) * dip);
    }

    public static int dimen2px(Context context, int dimRes) {
        return context.getResources().getDimensionPixelSize(dimRes);
    }

    /**
     * 获取通知栏的高度
     *
     * @return
     */
    public static int getStatusBarHeight() {
        return Resources.getSystem().getDimensionPixelSize(
                Resources.getSystem().getIdentifier("status_bar_height", "dimen", "android"));
    }
}
