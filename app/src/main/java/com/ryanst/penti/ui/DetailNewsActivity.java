package com.ryanst.penti.ui;

import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.Nullable;
import android.view.View;

import com.ryanst.penti.R;
import com.ryanst.penti.core.BaseActivity;
import com.ryanst.penti.network.NetClientAPI;
import com.ryanst.penti.util.WebViewUtil;
import com.ryanst.penti.widget.MyWebView;
import com.ryanst.penti.widget.NavigationBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by zhengjuntong on 7/12/16.
 */
public class DetailNewsActivity extends BaseActivity {
    @BindView(R.id.navBar)
    NavigationBar navBar;
    @BindView(R.id.wb_detail)
    MyWebView wbDetail;
    private String url;

    private String tuguaId = "d1b01607-873f-400e-bd2f-332e5b9ce7f6_1";
    private String pentiwang = "d1b01607-873f-400e-bd2f-332e5b9ce7f6_1";
    private String baseUrl = "http://yuedu.163.com/source.do?operation=queryContentHtml";
    private String operation = "queryContentHtml";
    private String contentId;
    private HandlerThread handlerThread;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_news);
        ButterKnife.bind(this);
        initData();
        initView();
        loadData();
    }

    private void loadData() {
        Handler handler = new Handler(handlerThread.getLooper());
        handler.post(runnable);
    }

    private void initData() {
        handlerThread = new HandlerThread("get_news_html");
        handlerThread.start();

        contentId = getIntent().getStringExtra("contentId");
    }

    private void getUrl() {
        url = baseUrl;
        url += "&" + "id=" + tuguaId;
        String contentId = getIntent().getStringExtra("contentId");
        url += "&" + "contentUuid=" + contentId;
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            loadNewsHtml(operation, tuguaId, contentId);
        }
    };

    private void loadNewsHtml(String operation, String tuguaId, String contentId) {
        NetClientAPI.getNewsHtml(operation, tuguaId, contentId, new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String htmlString = response.body();
                loadWebView(htmlString);
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }

    private void loadWebView(String htmlString) {
//        wbDetail.loadData(htmlString, "text/html; charset=UTF-8", null);
        wbDetail.loadDataWithBaseURL(null, htmlString, "text/html", "UTF-8", null);
    }

    private void initView() {
        navBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        WebViewUtil.setWebViewSettings(wbDetail, null, null);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (handlerThread != null && handlerThread.isAlive()) {
            handlerThread.quit();
        }
    }
}
