package com.dilusense.faceplaydemo.databindings;
import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 *  Activity管理类
 * author BULISILI
 * create at 2016/11/7 16:23
 */

/**
 * Created by ASUS on 2018/3/19.
 */

public class CommonAction {
    private List<Activity> AllActivitites = new ArrayList<Activity>();
    private static CommonAction instance;

    public CommonAction() {

    }

    public synchronized static CommonAction getInstance() {
        if (null == instance) {
            instance = new CommonAction();
        }
        return instance;
    }

    //在Activity基类的onCreate()方法中执行
    public void addActivity(Activity activity) {
        AllActivitites.add(activity);
    }

    //注销是销毁所有的Activity
    public void OutSign() {
        for (Activity activity : AllActivitites) {
            if (activity != null) {
                activity.finish();
            }
        }
    }
}
