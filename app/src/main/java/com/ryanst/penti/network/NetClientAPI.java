package com.ryanst.penti.network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.ryanst.penti.bean.News;
import com.ryanst.penti.core.MyApplication;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Callback;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by zhengjuntong on 16/5/7.
 */
public class NetClientAPI {
    public static final int OUT_TIME = 60;

    public static class NetConfig {
        public static final String HOST = "http://yuedu.163.com/";
    }

    private static RestInterface restService;
    private static RestInterface restHtmlService;

    static {
        // 设置拦截器
        Interceptor interceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                if (!NetWorkUtil.isConnected(MyApplication.getApplication())) {
                    request = request.newBuilder()
                            .cacheControl(CacheControl.FORCE_CACHE)
                            .build();
                }
                Response response = chain.proceed(request);
                if (!NetWorkUtil.isConnected(MyApplication.getApplication())) {
                    int maxAge = 0 * 60; // 有网络时 设置缓存超时时间0个小时
                    response.newBuilder()
                            .header("Cache-Control", "public, max-age=" + maxAge)
                            .removeHeader("Pragma")// 清除头信息，因为服务器如果不支持，
                            // 会返回一些干扰信息，不清除下面无法生效
                            .build();
                } else {
                    int maxStale = 60 * 60 * 24 * 28; // 无网络时，设置超时为4周
                    response.newBuilder()
                            .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                            .removeHeader("Pragma")
                            .build();
                }
                return response;
            }
        };
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        //设置缓存路径
        File httpCacheDirectory = new File("", "responses");
        //设置缓存 20M
        Cache cache = new Cache(httpCacheDirectory, 20 * 1024 * 1024);
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .addInterceptor(loggingInterceptor)
                .readTimeout(OUT_TIME, TimeUnit.SECONDS)//set out time
                .connectTimeout(OUT_TIME, TimeUnit.SECONDS)//set out time
                .cache(cache)
                .build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(NetConfig.HOST)
                .client(okHttpClient)
//                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(buildGsonConverter())
//                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Retrofit retrofitHtml = new Retrofit.Builder()
                .baseUrl(NetConfig.HOST)
                .client(okHttpClient)
                .addConverterFactory(StringConverterFactory.create())
                .build();

        restService = retrofit.create(RestInterface.class);

        restHtmlService = retrofitHtml.create(RestInterface.class);
    }

    public static class StringConverterFactory extends Converter.Factory {
        private final MediaType MEDIA_TYPE = MediaType.parse("text/plain");

        private static final StringConverterFactory INSTANCE = new StringConverterFactory();

        public static StringConverterFactory create() {
            return INSTANCE;
        }

        @Override
        public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
            if (String.class.equals(type)) {
                return new Converter<ResponseBody, String>() {
                    @Override
                    public String convert(ResponseBody value) throws IOException {
                        return value.string();
                    }
                };
            }
            return null;
        }
    }

    private static GsonConverterFactory buildGsonConverter() {
        GsonBuilder gsonBuilder = new GsonBuilder();

        gsonBuilder.registerTypeAdapter(GetListResponse.class, new DictionaryResponseDeserializer());
        Gson gson = gsonBuilder.create();

        return GsonConverterFactory.create(gson);
    }

    public static class DictionaryResponseDeserializer implements JsonDeserializer<GetListResponse> {

        @Override
        public GetListResponse deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            GetListResponse response = new GetListResponse();

            JsonArray array = (JsonArray) json;
            JsonObject moreInfoJson = (JsonObject) array.get(0);
            GetListResponse.MoreInfo moreInfo = new GetListResponse.MoreInfo();
            moreInfo.setMore(moreInfoJson.get("more").getAsBoolean());
            moreInfo.setUnsupported(moreInfoJson.get("unsupported").getAsBoolean());
            moreInfo.setNextPageToken(moreInfoJson.get("nextPageToken").getAsString());
            response.setMoreInfo(moreInfo);

            JsonArray newsList = (JsonArray) array.get(1);
            List<News> emptyNews = new ArrayList<>();
            response.setNewsList(emptyNews);

            int newsNum = newsList.size();
            for (int i = 0; i < newsNum; i++) {
                JsonObject newsJson = (JsonObject) newsList.get(i);
                News news = new News();
                news.setSourceID(newsJson.get("sourceID").getAsString());
                news.setTitle(newsJson.get("title").getAsString());
                news.setSource(newsJson.get("source").getAsString());
                news.setBreif(newsJson.get("breif").getAsString());
                news.setContentID(newsJson.get("contentID").getAsString());
                news.setPosttime(newsJson.get("posttime").getAsString());

                List<String> imageList = new ArrayList<>();
                JsonArray imageArray = newsJson.get("images").getAsJsonArray();
                int imageNum = imageArray.size();
                for (int j = 0; j < imageNum; j++) {
                    String imageUrl = imageArray.get(j).getAsString();
                    imageList.add(imageUrl);
                }

                news.setImages(imageList);
                News.SummaryImageBean summaryImageBean = new News.SummaryImageBean();
                JsonObject summaryImageJsonObject = newsJson.get("summaryImage").getAsJsonObject();
                summaryImageBean.setSummaryImageURL(summaryImageJsonObject.get("summaryImageURL").getAsString());
                news.setSummaryImage(summaryImageBean);

                response.getNewsList().add(news);
            }
            return response;
        }
    }

    public static void getNewsList(GetListRequest request, Callback<GetListResponse> callback) {
        if (!NetWorkUtil.isConnected(MyApplication.getApplication())) {
            callback.onResponse(null, null);
            return;
        }

        restService.getNewsList(request.getOperation(), request.getId(), request.getPageToken()).enqueue(callback);
    }

    public static void getNewsHtml(String operation, String tuguaId, String contentId, Callback<String> callback) {
        if (!NetWorkUtil.isConnected(MyApplication.getApplication())) {
            callback.onResponse(null, null);
            return;
        }

        restHtmlService.getNewsHtml(operation, tuguaId, contentId).enqueue(callback);
    }

}
