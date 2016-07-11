package com.ryanst.penti.core;

import android.app.Application;
import android.content.Context;

import com.ryanst.penti.BuildConfig;
import com.ryanst.penti.widget.UCEHandler;

import butterknife.ButterKnife;

/**
 * Created by zhengjuntong on 7/11/16.
 */
public class MyApplication extends Application {
    private static Context application;

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        ButterKnife.setDebug(BuildConfig.DEBUG);
//        initUncaughtException();
    }

    public static Context getApplication() {
        return application;
    }


    public void initUncaughtException() {
        //设置该CrashHandler为程序的默认处理器
        UCEHandler uceHandler = new UCEHandler(this);
        Thread.setDefaultUncaughtExceptionHandler(uceHandler);
    }
}
