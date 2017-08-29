package com.ryanst.penti.core;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.johnpersano.supertoasts.SuperToast;
import com.umeng.analytics.MobclickAgent;

/**
 * Created by zhengjuntong on 7/11/16.
 */
public class BaseFragment extends Fragment {

    private SuperToast toast;

    protected Context context;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
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
            toast = SuperToast.create(MyApplication.getApplication(), message, duration);
            toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        }
        toast.setText(message);
        toast.show();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(this.getClass().getSimpleName());
        Log.d("BaseFragment", getClass().getName());
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(this.getClass().getSimpleName());
    }
}
