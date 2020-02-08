package com.dilusense.faceplaydemo.utils;

import android.support.v4.util.SimpleArrayMap;
import android.util.Log;

import com.dilusense.faceplaydemo.net.api.PayAPI;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.android.BuildConfig;

/**
 * Retrofit工具类
 * Created by mazhanzhu on 2019/1/28.
 */
public class RetrofitUtils {
    /**
     * 缓存文件
     */
    private static final String RESPONSE_CACHE_FILE = "responseCache";
    /**
     * 缓存大小
     */
    private static final long RESPONSE_CACHE_SIZE = 10 * 1024 * 1024L;
    /**
     * 连接超时时间
     */
    private static final long HTTP_CONNECT_TIMEOUT = 10L;
    /**
     * 读取超时时间
     */
    private static final long HTTP_READ_TIMEOUT = 10L;
    /**
     * 写入超时时间
     */
    private static final long HTTP_WRITE_TIMEOUT = 10L;
    private static SimpleArrayMap<String, List<Cookie>> map = new SimpleArrayMap<>();
    private static String baseUrl;
    private static PayAPI mPortraitService;

    public static PayAPI getUrls() {

        //-----------------------------------配置网络请求日志-----------------------------------------------------------
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {

            @Override
            public void log(String message) {
                //完整的日志打印标识
                Log.e("=====log======", message);
            }
        });
        //日志打印的最高级别，可以输出所有的请求返回的信息包括各种请求格式！
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        //-----------------------------------配置网络请求日志------------------------------------------------------------

        //-----------------------------------配置网络请求参数------------------------------------------------------------
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(BuildConfig.DEBUG ? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE);
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(HTTP_CONNECT_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(HTTP_WRITE_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(HTTP_READ_TIMEOUT, TimeUnit.SECONDS)
                .cookieJar(new CookieJar() {
                    @Override
                    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
                        map.put(url.toString(), cookies);
                    }

                    @Override
                    public List<Cookie> loadForRequest(HttpUrl url) {
                        List<Cookie> cookies = map.get(url.toString());
                        return cookies != null ? cookies : new ArrayList<Cookie>();
                    }
                })
                .addInterceptor(loggingInterceptor)
                .addInterceptor(logging);
        OkHttpClient client = builder.build();
        //-----------------------------------配置网络请求参数------------------------------------------------------------

        //-----------------------------------配置Retrofit----------------------------------------------------------------
        //BaseUrl 一般网络请求地址里面，总会有一样得前缀，就像是
        //http://192.168.20.241:11008****这就是那个基地址，基地址+NetInterface类里面的
        //post（）的地址就是一个完整的网络请求地址，这点要注意
        baseUrl = "http://192.168.43.119:8888/";
        Retrofit mRetrofit = new Retrofit.Builder()
                //添加网络请求的基地址
                .baseUrl(baseUrl)
                //增加返回值为String的支持
                //添加转换工厂，用于解析json并转化为javaBean
                .addConverterFactory(GsonConverterFactory.create())
                //设置参数
                //.client(DataLayer.getClient())
                .client(client)
                .build();
        //NetInterface为接口定义类
        mPortraitService = mRetrofit.create(PayAPI.class);
        return mPortraitService;
        //-----------------------------------配置Retrofit----------------------------------------------------------------
    }
}
