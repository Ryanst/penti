package com.ryanst.penti.ui;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ryanst.penti.R;
import com.ryanst.penti.adapter.NewsAdapter;
import com.ryanst.penti.bean.News;
import com.ryanst.penti.constant.PentiConst;
import com.ryanst.penti.core.BaseFragment;
import com.ryanst.penti.databinding.FragmentNewsListBinding;
import com.ryanst.penti.network.GetListRequest;
import com.ryanst.penti.network.GetListResponse;
import com.ryanst.penti.network.NetClientAPI;
import com.ryanst.penti.widget.DividerItemDecoration;
import com.ryanst.penti.widget.recyclerview.EndlessRecyclerOnScrollListener;
import com.ryanst.penti.widget.recyclerview.HeaderAndFooterRecyclerViewAdapter;
import com.ryanst.penti.widget.recyclerview.LoadingFooter;
import com.ryanst.penti.widget.recyclerview.RecyclerViewStateUtils;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by zhengjuntong on 7/11/16.
 */
public class NewsListFragment extends BaseFragment {
    public static final String QUERY_CONTENT_LIST = "queryContentList";
    public static final int PAGE_SIZE = 24;
    public static final int REFRESH = 1;
    private static final int LOAD_MORE = 2;
    public static final int NETWORK_FAIL = -1;
    public static final int STOP_REFRESH = 0;
    public static final String TYPE = "type";

    private RecyclerView rvNewList;
    private SwipeRefreshLayout swipeRefresh;

    private FragmentNewsListBinding binding;

    private String pageToken;
    private List<News> newsList = new ArrayList<>();

    private NewsAdapter adapter;
    private HeaderAndFooterRecyclerViewAdapter loadMoreAdapter = null;
    private boolean more = true;
    private String typeId;
    private String type;

    public static NewsListFragment getInstance(String type) {
        NewsListFragment fragment = new NewsListFragment();
        Bundle bundle = new Bundle();
        bundle.putString(TYPE, type);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_news_list, container, false);
        initType();
        initView();
        initRecycleView();
        refresh("");
        return binding.getRoot();
    }

    private void initType() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            String type = bundle.getString(TYPE);
            this.type = type;
        }

        switch (type) {
            case PentiConst.TYPE_PENTI_WANG:
                typeId = PentiConst.PENTI_WANG_ID;
                break;
            case PentiConst.TYPE_TUGUA:
                typeId = PentiConst.TUGUA_ID;
                break;
            default:
                typeId = PentiConst.PENTI_WANG_ID;
                break;
        }
    }

    private void initRecycleView() {

        rvNewList.setLayoutManager(new LinearLayoutManager(getActivity()));

        adapter = new NewsAdapter(getActivity(), newsList, type);

        loadMoreAdapter = new HeaderAndFooterRecyclerViewAdapter(adapter);

        rvNewList.setAdapter(loadMoreAdapter);
        rvNewList.addOnScrollListener(new EndlessRecyclerOnScrollListener(new EndlessRecyclerOnScrollListener.OnListLoadNextPageListener() {
            @Override
            public void onLoadNextPage(View view) {
                LoadingFooter.State state = RecyclerViewStateUtils.getFooterViewState(rvNewList);
                if (state == LoadingFooter.State.Loading || !more) {
                    return;
                }
                RecyclerViewStateUtils.setFooterViewState(getActivity(), rvNewList, PAGE_SIZE, LoadingFooter.State.Loading, null);
                refresh(pageToken);
            }
        }));
    }

    public void setRefresh() {
        if (swipeRefresh != null) {
            swipeRefresh.setRefreshing(true);
            refresh("");
        }
    }

    private void initView() {
        rvNewList = binding.rvNewList;
        swipeRefresh = binding.swipeRefresh;
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh("");
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
                    rvNewList.smoothScrollToPosition(0);
                    break;
                case LOAD_MORE:
                    loadMoreAdapter.notifyDataSetChanged();
                    RecyclerViewStateUtils.setFooterViewState(rvNewList, LoadingFooter.State.Normal);
                    break;
                case NETWORK_FAIL:
                    RecyclerViewStateUtils.setFooterViewState(getActivity(), rvNewList, PAGE_SIZE, LoadingFooter.State.NetWorkError, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (more) {
                                RecyclerViewStateUtils.setFooterViewState(getActivity(), rvNewList, PAGE_SIZE, LoadingFooter.State.Loading, null);
                                refresh(pageToken);
                            } else {
                                Toast.makeText(getContext(), "没有更多了", Toast.LENGTH_SHORT);
                            }
                        }
                    });
                    break;
                case STOP_REFRESH:
                    RecyclerViewStateUtils.setFooterViewState(rvNewList, LoadingFooter.State.Normal);
                    break;
                default:
                    break;
            }
        }
    };

    public void refresh(final String pageToken) {
        final GetListRequest request = new GetListRequest();
        request.setOperation(QUERY_CONTENT_LIST);
        request.setId(typeId);
        request.setPageToken(pageToken);

        NetClientAPI.getNewsList(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<GetListResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        handler.sendEmptyMessage(NETWORK_FAIL);
                    }

                    @Override
                    public void onNext(GetListResponse response) {
                        if (response != null) {
                            NewsListFragment.this.pageToken = response.getMoreInfo().getNextPageToken();
                            NewsListFragment.this.more = response.getMoreInfo().isMore();

                            if (TextUtils.isEmpty(pageToken)) {
                                newsList.clear();
                                newsList.addAll(response.getNewsList());
                                handler.sendEmptyMessage(REFRESH);
                            } else {
                                newsList.addAll(response.getNewsList());
                                handler.sendEmptyMessage(LOAD_MORE);
                            }

                        } else {
                            handler.sendEmptyMessage(STOP_REFRESH);
                        }
                    }
                });
    }
}
