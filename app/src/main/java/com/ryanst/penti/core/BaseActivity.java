package com.ryanst.penti.core;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;

import com.github.johnpersano.supertoasts.SuperToast;
import com.orhanobut.logger.Logger;
import com.ryanst.penti.R;
import com.ryanst.penti.widget.CloseAllActivityEvent;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;


/**
 * Created by zhengjuntong on 7/11/16.
 */

public class BaseActivity extends AppCompatActivity {

    private SuperToast toast;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(this.getClass().getSimpleName());
        MobclickAgent.onResume(this);
        Log.d("BaseActivity", getClass().getName());
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(this.getClass().getSimpleName());
        MobclickAgent.onPause(this);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.anim_come, R.anim.anim_gone);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessage(CloseAllActivityEvent closeEvent) {
        this.finish();
    }

    protected void toast(String message) {
        toast(message,2000);
    }

    protected void toastShort(String message) {
        toast(message,1000);
    }

    protected void toastLong(String message) {
        toast(message,4000);
    }

    protected void toast(String message, int duration ) {
        if (toast == null) {
            toast = SuperToast.create(this, message, duration);
            toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        }
        toast.setText(message);
        toast.show();
    }

    public void logger(String message) {
        Logger.d(message);
    }

    public void logger(String tag, String message) {
        Logger.t(tag).d(message);
    }

    public void log(String message) {
        Log.d(getClass().getName(), message);
    }
}
