package com.wenh.baselibrary.network;


import com.google.gson.Gson;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


public class HttpModule {
    private final String address, port, sessionId;

    public HttpModule(String sessionId, String address, String port) {
        this.sessionId = sessionId;
        this.address = address;
        this.port = port;
    }

    Retrofit.Builder provideRetrofitBuilder() {
        return new Retrofit.Builder();
    }


    OkHttpClient.Builder provideOkHttpBuilder() {
        return new OkHttpClient.Builder();
    }

    BasicParamsInterceptor.Builder baseParamsBuilder;

    public void setHeader(String name, String value) {
        if (baseParamsBuilder != null) {
            baseParamsBuilder.addHeaderParam(name, value);
        }
    }

    OkHttpClient provideClient(OkHttpClient.Builder builder) {
        HttpLogInterceptor loggingInterceptor = new HttpLogInterceptor();
        loggingInterceptor.setLevel(HttpLogInterceptor.Level.BODY);
        builder.addInterceptor(loggingInterceptor);

        baseParamsBuilder = new BasicParamsInterceptor.Builder();
        baseParamsBuilder.addQueryParam("sessionId", sessionId);
        baseParamsBuilder.addParam("sessionId", sessionId);
//        setHeader("lang", Ins.prefers().language());
        builder.addInterceptor(baseParamsBuilder.build());

        //设置缓存
        //builder.addNetworkInterceptor(cacheInterceptor);
        //builder.addInterceptor(cacheInterceptor);
        //builder.cache(cache);
        //设置超时
        builder.connectTimeout(10, TimeUnit.SECONDS);
        builder.readTimeout(20, TimeUnit.SECONDS);
        builder.writeTimeout(20, TimeUnit.SECONDS);
        //错误重连
        builder.retryOnConnectionFailure(true);
        return builder.build();
    }


    public Apis provideService() {
        Retrofit retrofit = createRetrofit();
        return retrofit.create(Apis.class);
    }

    private Retrofit createRetrofit() {
        Retrofit.Builder builder = provideRetrofitBuilder();
        OkHttpClient.Builder httpBuilder = provideOkHttpBuilder();
        OkHttpClient client = provideClient(httpBuilder);
        String url = "http://" + address + ":" + port;
        return builder
                .baseUrl(url)
                .client(client)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }


    public static RequestBody toJson(Object object) {
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(object));
        return body;
    }
}
