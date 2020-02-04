package com.dilusense.faceplaydemo.net;

import android.util.Log;

import com.dilusense.faceplaydemo.App;
import com.dilusense.faceplaydemo.net.api.PayAPI;
import com.ihsanbal.logging.Level;
import com.ihsanbal.logging.LoggingInterceptor;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.android.BuildConfig;

/**
 * Author :    kuangch
 * Date :      1/2/2016
 */
public class RestClient {

    public static String payHTTPProtocol = "http://";


    private Retrofit.Builder httpBuilt;

    public RestClient() {

        LoggingInterceptor logging = new LoggingInterceptor.Builder()
                .loggable(BuildConfig.DEBUG)
                .setLevel(Level.BASIC)
                .log(Log.INFO)
                .request("Request")
                .response("Response")
                .addHeader("version", BuildConfig.VERSION_NAME)
                .build();

        // okHttpClient
        OkHttpClient client = new OkHttpClient.Builder().readTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(20, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .addInterceptor(logging)
//                .retryOnConnectionFailure(true)
                .build();

        httpBuilt = new Retrofit.Builder()
                .client(client)
                .addConverterFactory(GsonConverterFactory.create());

    }

    public PayAPI getCompareService() {
        return httpBuilt.baseUrl(payHTTPProtocol + App.getInstent().getPayServIP())
                .build().create(PayAPI.class);
    }
}
