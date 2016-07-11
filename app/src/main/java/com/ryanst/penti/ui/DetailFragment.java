package com.ryanst.penti.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ryanst.penti.R;
import com.ryanst.penti.util.WebViewUtil;
import com.ryanst.penti.widget.MyWebView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by zhengjuntong on 7/11/16.
 */
public class DetailFragment extends Fragment {

    @BindView(R.id.wb_detail)
    MyWebView wbDetail;
    private String url;

    public DetailFragment() {
    }

    public DetailFragment(String url) {
        this.url = url;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail, null);
        initWebView();
        ButterKnife.bind(this, view);
        return view;
    }

    private void initWebView() {
        WebViewUtil.setWebViewSettings(wbDetail, null, null);
        wbDetail.loadUrl(url);
    }
}
