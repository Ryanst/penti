package com.ryanst.penti.core;

import android.app.Application;
import android.content.Context;

import com.facebook.stetho.Stetho;
import com.github.moduth.blockcanary.BlockCanary;
import com.ryanst.penti.BuildConfig;
import com.ryanst.penti.util.StrictModeUtil;
import com.ryanst.penti.widget.AppBlockCanaryContext;
import com.ryanst.penti.widget.CrashHandler;

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

        StrictModeUtil.init();

        ButterKnife.setDebug(BuildConfig.DEBUG);

        BlockCanary.install(this, new AppBlockCanaryContext()).start();

        Stetho.initialize(
                Stetho.newInitializerBuilder(this)
                        .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                        .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(this))
                        .build());

//        initUncaughtException();
    }

    public static Context getApplication() {
        return application;
    }


    public void initUncaughtException() {
        //设置该CrashHandler为程序的默认处理器
        CrashHandler uceHandler = new CrashHandler(this);
        Thread.setDefaultUncaughtExceptionHandler(uceHandler);
    }
}
