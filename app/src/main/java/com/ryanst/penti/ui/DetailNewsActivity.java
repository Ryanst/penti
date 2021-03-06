package com.ryanst.penti.ui;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.Nullable;
import android.view.View;
import android.webkit.WebView;
import android.widget.ProgressBar;

import com.ryanst.penti.R;
import com.ryanst.penti.constant.PentiConst;
import com.ryanst.penti.core.BaseActivity;
import com.ryanst.penti.databinding.ActivityDetailNewsBinding;
import com.ryanst.penti.network.NetClientAPI;
import com.ryanst.penti.util.WebViewUtil;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.ryanst.penti.constant.PentiConst.PENTI_WANG;
import static com.ryanst.penti.constant.PentiConst.TUGUA;

/**
 * Created by zhengjuntong on 7/12/16.
 */
public class DetailNewsActivity extends BaseActivity {
    WebView wbDetail;

    private String operation = "queryContentHtml";
    private String contentId;
    private HandlerThread handlerThread;

    private ActivityDetailNewsBinding binding;
    private ProgressBar webProgress;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail_news);
        setSupportActionBar(binding.toolbar);
        initView();
        initData();
        loadData();
    }

    private void initView() {

        wbDetail = binding.wbDetail;
        webProgress = binding.webProgress;

        binding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        WebViewUtil.setWebViewSettings(wbDetail, null, new WebViewUtil.ProgressBarListener() {
            @Override
            public void hideProgressBar(boolean hide, int progress) {
                if (webProgress != null) {
                    if (hide) {
                        if (webProgress.getVisibility() != View.GONE) {
                            webProgress.setVisibility(View.GONE);
                        }
                    } else {
                        if (webProgress.getVisibility() != View.VISIBLE) {
                            webProgress.setVisibility(View.VISIBLE);
                        }
                        webProgress.setProgress(progress);
                    }
                }
            }
        });
    }

    private void loadData() {
        Handler handler = new Handler(handlerThread.getLooper());
        handler.post(loadHtmlRunnable);
    }

    private void initData() {
        handlerThread = new HandlerThread("get_news_html");
        handlerThread.start();

        contentId = getIntent().getStringExtra("contentId");
        String type = getIntent().getStringExtra("type");
        if (PentiConst.TYPE_TUGUA.equals(type)) {
            setTitle(TUGUA);
        } else {
            setTitle(PENTI_WANG);
        }
    }

    private Runnable loadHtmlRunnable = new Runnable() {
        @Override
        public void run() {
            loadNewsHtml(operation, PentiConst.PENTI_WANG_ID, contentId);
        }
    };


    private void loadNewsHtml(final String operation, final String tuguaId, final String contentId) {

        NetClientAPI.getNewsHtmlR(operation, tuguaId, contentId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        toast(getString(R.string.network_error));
                    }

                    @Override
                    public void onNext(String s) {
                        loadWebView(s);
                    }
                });
    }

    private void loadWebView(String htmlString) {
//        wbDetail.loadData(htmlString, "text/html; charset=UTF-8", null);
        wbDetail.loadDataWithBaseURL(null, htmlString, "text/html", "UTF-8", null);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (handlerThread != null && handlerThread.isAlive()) {
            handlerThread.quit();
        }
    }
}
