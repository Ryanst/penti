package com.ryanst.penti.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ryanst.penti.R;
import com.ryanst.penti.bean.News;
import com.ryanst.penti.network.GetListRequest;
import com.ryanst.penti.network.GetListResponse;
import com.ryanst.penti.network.NetClientAPI;

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
    @BindView(R.id.rv_new_list)
    RecyclerView rvNewList;

    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefresh;
    private String pageToken;
    private String tuguaId = "d1b01607-873f-400e-bd2f-332e5b9ce7f6_1";

    private List<News> newsList;

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

    private void initData() {
        newsList = new ArrayList<>();
        refresh("");
    }

    private void initRecycleView() {

        rvNewList.setLayoutManager(new LinearLayoutManager(getActivity()));

        rvNewList.setAdapter(new RecyclerView.Adapter<RecyclerView.ViewHolder>() {
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                return new RecyclerView.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_news, parent, false)) {
                };
            }

            @Override
            public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
                ((TextView) holder.itemView.findViewById(R.id.tv_title)).setText(newsList.get(position).getTitle());
            }

            @Override
            public int getItemCount() {
                return newsList.size();
            }
        });
    }


    private void initView() {
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh("");
            }
        });
    }

    private void refresh(String pageToken) {
        GetListRequest request = new GetListRequest();
        request.setOperation(QUERY_CONTENT_LIST);
        request.setId(tuguaId);
        NetClientAPI.getNewsList(request, new Callback<GetListResponse>() {
            @Override
            public void onResponse(Call<GetListResponse> call, Response<GetListResponse> response) {
                if (response != null && response.body() != null) {


                }
            }

            @Override
            public void onFailure(Call<GetListResponse> call, Throwable t) {

            }
        });
    }


}
