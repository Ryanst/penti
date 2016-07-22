package com.ryanst.penti.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.ryanst.penti.R;
import com.ryanst.penti.adapter.NewsAdapter;
import com.ryanst.penti.bean.News;
import com.ryanst.penti.core.BaseFragment;
import com.ryanst.penti.network.GetListRequest;
import com.ryanst.penti.network.GetListResponse;
import com.ryanst.penti.network.NetClientAPI;
import com.ryanst.penti.util.AndroidScreenUtil;
import com.ryanst.penti.widget.DividerItemDecoration;
import com.ryanst.penti.widget.recyclerview.EndlessRecyclerOnScrollListener;
import com.ryanst.penti.widget.recyclerview.HeaderAndFooterRecyclerViewAdapter;
import com.ryanst.penti.widget.recyclerview.LoadingFooter;
import com.ryanst.penti.widget.recyclerview.RecyclerViewStateUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by zhengjuntong on 7/11/16.
 */
public class ListFragment extends BaseFragment {
    public static final String QUERY_CONTENT_LIST = "queryContentList";
    public static final int PAGE_SIZE = 24;
    public static final int REFRESH = 1;
    public static final int NO_MORE = 2;
    public static final int NETWORK_FAIL = -1;
    public static final int STOP_REFRESH = 0;
    @BindView(R.id.rv_new_list)
    RecyclerView rvNewList;

    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefresh;

    private String pageToken;
    private HandlerThread handlerThread;
    private List<News> newsList;
    private RecyclerView.Adapter<RecyclerView.ViewHolder> adapter;
    private HeaderAndFooterRecyclerViewAdapter loadMoreAdapter = null;
    private boolean more = true;
    private String tuguaId = "d1b01607-873f-400e-bd2f-332e5b9ce7f6_1";
    private String baseUrl = "http://yuedu.163.com/source.do?operation=queryContentHtml";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, null);
        ButterKnife.bind(this, view);
        initView();
        initData();
        initRecycleView();
        return view;
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            refresh("");
        }
    };

    private void initData() {
        newsList = new ArrayList<>();
        handlerThread = new HandlerThread("load_news_list");
        handlerThread.start();
        loadData();
    }

    private void loadData() {
        Handler handler = new Handler(handlerThread.getLooper());
        handler.post(runnable);
    }

    private void initRecycleView() {

        rvNewList.setLayoutManager(new LinearLayoutManager(getActivity()));

        rvNewList.addItemDecoration(new DividerItemDecoration(getActivity()));

        rvNewList.addItemDecoration(new RecyclerView.ItemDecoration() {
        });

        adapter = new NewsAdapter(getActivity(), newsList);

        loadMoreAdapter = new HeaderAndFooterRecyclerViewAdapter(adapter);

        rvNewList.setAdapter(loadMoreAdapter);
        rvNewList.addOnScrollListener(mOnScrollListener);
    }


    private EndlessRecyclerOnScrollListener mOnScrollListener = new EndlessRecyclerOnScrollListener(new EndlessRecyclerOnScrollListener.OnListLoadNextPageListener() {
        @Override
        public void onLoadNextPage(View view) {
            LoadingFooter.State state = RecyclerViewStateUtils.getFooterViewState(rvNewList);
            if (state == LoadingFooter.State.Loading || !more) {
                return;
            }

            RecyclerViewStateUtils.setFooterViewState(getActivity(), rvNewList, PAGE_SIZE, LoadingFooter.State.Loading, null);
            refresh(pageToken);
        }
    });


    private void initView() {
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData();
            }
        });
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            swipeRefresh.setRefreshing(false);
            switch (msg.what) {
                case REFRESH:
                    loadMoreAdapter.notifyDataSetChanged();
                case NO_MORE:
                    loadMoreAdapter.notifyDataSetChanged();
                    RecyclerViewStateUtils.setFooterViewState(rvNewList, LoadingFooter.State.TheEnd);
                    break;
                case NETWORK_FAIL:
                    RecyclerViewStateUtils.setFooterViewState(getActivity(), rvNewList, PAGE_SIZE, LoadingFooter.State.NetWorkError, mFooterClick);
                    break;
                case STOP_REFRESH:
                    break;
                default:
                    break;
            }
        }
    };

    private void refresh(final String pageToken) {
        GetListRequest request = new GetListRequest();
        request.setOperation(QUERY_CONTENT_LIST);
        request.setId(tuguaId);
        request.setPageToken(pageToken);

        NetClientAPI.getNewsList(request, new Callback<GetListResponse>() {
            @Override
            public void onResponse(Call<GetListResponse> call, Response<GetListResponse> response) {
                if (response != null && response.body() != null) {
                    GetListResponse body = response.body();
                    if (response != null && body != null) {
                        newsList.addAll(body.getNewsList());
                        ListFragment.this.pageToken = body.getMoreInfo().getNextPageToken();
                        ListFragment.this.more = body.getMoreInfo().isMore();
                        Log.d("CurrentThreadId", Thread.currentThread().getId() + "");

                        if (more) {
                            handler.sendEmptyMessage(REFRESH);
                        } else {
                            handler.sendEmptyMessage(NO_MORE);
                        }
                    }
                } else {
                    handler.sendEmptyMessage(STOP_REFRESH);
                }
            }

            @Override
            public void onFailure(Call<GetListResponse> call, Throwable t) {
                handler.sendEmptyMessage(NETWORK_FAIL);
            }
        });
    }

    private View.OnClickListener mFooterClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (more) {
                RecyclerViewStateUtils.setFooterViewState(getActivity(), rvNewList, PAGE_SIZE, LoadingFooter.State.Loading, null);
                refresh(pageToken);
            } else {
                Toast.makeText(getContext(), "没有更多了", Toast.LENGTH_SHORT);
            }
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (handlerThread != null && handlerThread.isAlive()) {
            handlerThread.quit();
        }
    }
}
