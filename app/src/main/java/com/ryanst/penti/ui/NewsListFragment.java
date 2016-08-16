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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
        fragment.setType(type);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_news_list, container, false);
        initView();
        initRecycleView();
        initData();
        return binding.getRoot();
    }

    private void initData() {
        initTypeId();
        refresh("");
    }

    private void initTypeId() {
        switch (type) {
            case PentiConst.FRAGMENT_TYPE_PENTI_WANG:
                typeId = PentiConst.PENTI_WANG_ID;
                break;
            case PentiConst.FRAGMENT_TYPE_TUGUA:
                typeId = PentiConst.TUGUA_ID;
                break;
            default:
                typeId = PentiConst.PENTI_WANG_ID;
                break;
        }
    }

    private void initRecycleView() {

        rvNewList.setLayoutManager(new LinearLayoutManager(getActivity()));

        rvNewList.addItemDecoration(new DividerItemDecoration(getActivity()));

        adapter = new NewsAdapter(getActivity(), newsList);

        loadMoreAdapter = new HeaderAndFooterRecyclerViewAdapter(adapter);

        rvNewList.setAdapter(adapter);
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
        swipeRefresh.setRefreshing(true);
        refresh("");
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
                    adapter.notifyDataSetChanged();
//                    rvNewList.smoothScrollToPosition(0);
                    break;
                case LOAD_MORE:
                    adapter.notifyDataSetChanged();
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
        GetListRequest request = new GetListRequest();
        request.setOperation(QUERY_CONTENT_LIST);
        request.setId(typeId);
        request.setPageToken(pageToken);

        NetClientAPI.getNewsList(request, new Callback<GetListResponse>() {
            @Override
            public void onResponse(Call<GetListResponse> call, Response<GetListResponse> response) {
                if (response != null && response.body() != null) {
                    GetListResponse body = response.body();
                    if (response != null && body != null) {

                        NewsListFragment.this.pageToken = body.getMoreInfo().getNextPageToken();
                        NewsListFragment.this.more = body.getMoreInfo().isMore();

                        if (TextUtils.isEmpty(pageToken)) {
                            newsList.clear();
                            newsList.addAll(body.getNewsList());
                            handler.sendEmptyMessage(REFRESH);
                        } else {
                            handler.sendEmptyMessage(LOAD_MORE);
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
