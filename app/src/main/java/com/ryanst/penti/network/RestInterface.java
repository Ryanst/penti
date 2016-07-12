package com.ryanst.penti.network;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by zhengjuntong on 7/11/16.
 */
public interface RestInterface {
    @GET("source.do")
    Call<GetListResponse> getNewsList(@Query("operation") String operation, @Query("id") String id, @Query("pageToken") String pageToken);

    @GET("source.do")
    Call<String> getNewsHtml(@Query("operation") String operation, @Query("id") String tuguaId, @Query("contentUuid") String contentId);
}
