package com.ryanst.penti.widget;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Looper;
import android.widget.Toast;

import com.facebook.stetho.common.LogUtil;


/**
 * Created by zhengjuntong on 15/11/19.
 */
public class CrashHandler implements Thread.UncaughtExceptionHandler {
    private Thread.UncaughtExceptionHandler mDefaultHandler;

    Context applicationContext;

    public CrashHandler(Context applicationContext) {
        //获取系统默认的UncaughtException处理器
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        this.applicationContext = applicationContext;
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        if (!handleException(ex) && mDefaultHandler != null) {
            //如果用户没有处理则让系统默认的异常处理器来处理
            mDefaultHandler.uncaughtException(thread, ex);
        } else {
            Intent intent = applicationContext.getPackageManager().getLaunchIntentForPackage(applicationContext.getPackageName());
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent restartIntent = PendingIntent.getActivity(applicationContext.getApplicationContext(), 100, intent, PendingIntent.FLAG_ONE_SHOT);

            //退出程序
            AlarmManager mgr = (AlarmManager) applicationContext.getSystemService(Context.ALARM_SERVICE);
            mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 1000, restartIntent); // 1秒钟后重启应用
            System.exit(0);

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                LogUtil.e("error : ", e);
            }
        }
    }

    /**
     * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成.
     *
     * @param ex
     * @return true:如果处理了该异常信息;否则返回false.
     */
    private boolean handleException(Throwable ex) {
        if (ex == null) {
            return false;
        }
        //使用Toast来显示异常信息
        new Thread() {
            @Override
            public void run() {
                Looper.prepare();
                Toast.makeText(applicationContext.getApplicationContext(), "异常退出", Toast.LENGTH_LONG).show();
                Looper.loop();
            }
        }.start();
        return true;
    }
}
