package com.example.yishafang.healthpro;

import com.squareup.okhttp.OkHttpClient;

import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.client.OkClient;

/**
 * Created by yellowstar on 12/5/15.
 */
public class ServiceFactory {
    public static final String API_BASE_URL = "https://healthpro295.herokuapp.com/";

    private static RestAdapter.Builder builder = new RestAdapter.Builder()
            .setEndpoint(API_BASE_URL)
            .setClient(new OkClient(new OkHttpClient()));

    public static <S> S createService(Class<S> serviceClass) {
        return createService(serviceClass, null);
    }

    public static <S> S createService(Class<S> serviceClass, final String authToken) {
        if (authToken != null) {
            builder.setRequestInterceptor(new RequestInterceptor() {
                @Override
                public void intercept(RequestInterceptor.RequestFacade request) {
                    request.addHeader("Authorization", authToken);
                }
            });
        }
        RestAdapter adapter = builder.build();
        return adapter.create(serviceClass);
    }
}
