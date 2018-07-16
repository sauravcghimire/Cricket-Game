package com.appsinfinity.fingercricket.api;

import android.content.Context;

import com.squareup.okhttp.OkHttpClient;

import java.util.concurrent.TimeUnit;

import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.client.OkClient;

/**
 * Created on 6/22/2016.
 *
 * @author Nitu
 */
public class Api {
    private static Api ourInstance;
    private ApiService apiService;

    public Api(Context ctx, String baseUrl) {
        final OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.setReadTimeout(120, TimeUnit.SECONDS);
        okHttpClient.setConnectTimeout(120, TimeUnit.SECONDS);
        RestAdapter adapter = new RestAdapter.Builder()
                .setConverter(new JacksonConverter())
                .setClient(new OkClient(okHttpClient))
                .setEndpoint(baseUrl)
                .setRequestInterceptor(new RequestInterceptor() {
                    @Override
                    public void intercept(RequestFacade request) {
                        request.addHeader("Content-Type", "application/json");
                    }
                })
                .build();
        this.apiService = adapter.create(ApiService.class);
    }




    public static ApiService getService(Context ctx, String baseUrl) {
        ourInstance = new Api(ctx, baseUrl);
        return ourInstance.apiService;
    }



}
