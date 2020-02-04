package com.dilusense.faceplaydemo.databindings;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.dilusense.faceplaydemo.App;


/**
 * Created by ASUS on 2018/3/19.
 */

public abstract class AbstractTemplateActivity extends FragmentActivity {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App application = (App) this.getApplication();
        application.getActivityManager().pushActivity(this);
//        this.setContentView(this.getLayoutId());//缺少这一行
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        App application = (App) getApplication();
        application.getActivityManager().popActivity(this);
    }
    /**
     * 获取布局id
     *
     * @return
     */
//    protected abstract int getLayoutId();
}
