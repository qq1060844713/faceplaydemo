package com.dilusense.faceplaydemo;

import android.app.Application;
import android.content.IntentFilter;
import android.net.ConnectivityManager;

import com.dilusense.faceplaydemo.databindings.ActivityManager;
import com.dilusense.faceplaydemo.net.RestClient;


public class App extends Application {
    private ActivityManager activityManager = null;
    private static App instent = null;
    private static RestClient restClient = null;
    @Override
    public void onCreate() {
        super.onCreate();
        instent = this;
        activityManager = ActivityManager.getScreenManager();
        if (restClient == null) {
            restClient = new RestClient();
        }
    }

    public ActivityManager getActivityManager() {
        return activityManager;
    }

    /**
     * 获得程序application实例
     * @return 返回application实例对象
     */
    public static App getInstent() {

        if (instent == null) {
            synchronized (App.class) {
                if (instent == null) {
                    instent = new App();
                }
            }
        }
        return instent;
    }

    /**
     * 获取网络访问客户端实例
     * @return
     */
    public static RestClient getRestClient() {
        if (restClient == null) {
            synchronized (App.class) {
                if (restClient == null) {
                    restClient = new RestClient();
                }
            }
        }
        return restClient;
    }

    /**
     * 服务端ip地址
     * @return
     */
    public String getPayServIP(){
       return "192.168.43.119:8888";
    }
}

