package com.ryanst.penti.ui;

import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.ryanst.penti.R;
import com.ryanst.penti.bean.News;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by zhengjuntong on 7/11/16.
 */
public class ListFragment extends Fragment {
    public static final String QUERY_CONTENT_LIST = "queryContentList";
    public static final int PAGE_SIZE = 24;
    @BindView(R.id.rv_new_list)
    RecyclerView rvNewList;

    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefresh;

    private String pageToken;
    private String tuguaId = "d1b01607-873f-400e-bd2f-332e5b9ce7f6_1";
    private HandlerThread handlerThread;
    private List<News> newsList;
    private RecyclerView.Adapter<RecyclerView.ViewHolder> adapter;
    private HeaderAndFooterRecyclerViewAdapter loadMoreAdapter = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, null);
        ButterKnife.bind(this, view);
        initView();
        initRecycleView();
        initData();
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

        adapter = new RecyclerView.Adapter<RecyclerView.ViewHolder>() {
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                return new RecyclerView.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_news, parent, false)) {
                };
            }

            @Override
            public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
                ((TextView) holder.itemView.findViewById(R.id.tv_title)).setText(newsList.get(position).getTitle());
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dealItemOnClick(position);
                    }
                });
            }

            @Override
            public int getItemCount() {
                return newsList.size();
            }
        };

        loadMoreAdapter = new HeaderAndFooterRecyclerViewAdapter(adapter);
        rvNewList.setAdapter(loadMoreAdapter);
        rvNewList.addOnScrollListener(mOnScrollListener);
    }


    private EndlessRecyclerOnScrollListener mOnScrollListener = new EndlessRecyclerOnScrollListener(new EndlessRecyclerOnScrollListener.OnListLoadNextPageListener() {
        @Override
        public void onLoadNextPage(View view) {
            LoadingFooter.State state = RecyclerViewStateUtils.getFooterViewState(rvNewList);
            if (state == LoadingFooter.State.Loading) {
                return;
            }

            RecyclerViewStateUtils.setFooterViewState(getActivity(), rvNewList, PAGE_SIZE, LoadingFooter.State.Loading, null);
            refresh(pageToken);
        }
    });

    private void dealItemOnClick(int position) {
        Toast.makeText(getActivity(), "you press position: " + position, Toast.LENGTH_SHORT).show();
    }


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
            loadMoreAdapter.notifyDataSetChanged();
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
                swipeRefresh.setRefreshing(false);
                if (response != null && response.body() != null) {
                    GetListResponse body = response.body();
                    if (response != null && body != null) {
                        newsList.addAll(body.getNewsList());
                        ListFragment.this.pageToken = body.getMoreInfo().getNextPageToken();
                        Log.d("CurrentThreadId", Thread.currentThread().getId() + "");
                        handler.sendEmptyMessage(0);
                    }
                }
            }

            @Override
            public void onFailure(Call<GetListResponse> call, Throwable t) {
                swipeRefresh.setRefreshing(false);
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (handlerThread != null && handlerThread.isAlive()) {
            handlerThread.quit();
        }
    }
}
