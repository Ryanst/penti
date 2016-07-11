package com.ryanst.penti.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ryanst.penti.R;
import com.ryanst.penti.network.GetListRequest;
import com.ryanst.penti.network.GetListResponse;
import com.ryanst.penti.network.NetClientAPI;

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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, null);
        initView();
        ButterKnife.bind(this, view);
        return view;
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
                if(response != null && response.body() != null){


                }
            }

            @Override
            public void onFailure(Call<GetListResponse> call, Throwable t) {

            }
        });
    }


}
