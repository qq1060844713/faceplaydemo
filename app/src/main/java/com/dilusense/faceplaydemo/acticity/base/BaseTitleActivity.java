package com.dilusense.faceplaydemo.acticity.base;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dilusense.faceplaydemo.MainActivity;
import com.dilusense.faceplaydemo.R;
import com.dilusense.faceplaydemo.databindings.AbstractTemplateActivity;
import com.dilusense.faceplaydemo.databindings.CommonAction;
import com.dilusense.faceplaydemo.databindings.SharedPrefUtility;
import com.dilusense.faceplaydemo.service.NetBroadcastReceiver;
import com.hb.dialog.dialog.LoadingDialog;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by Thinkpad on 2017/3/8.
 */
public class BaseTitleActivity extends AbstractTemplateActivity implements NetBroadcastReceiver.NetStatusMonitor {
    NetBroadcastReceiver  netBroadcastReceiver;
    public Context ctx = null;

    @BindView(R.id.tv_title_txt)
    TextView tv_title_txt;
    private Unbinder unbinder;
    @BindView(R.id.tv_title_txt1)
    TextView tv_title_txt1;
    @BindView(R.id.title_left)
    RelativeLayout title_left;
    @BindView(R.id.wifi_disconnect)
    ImageView wifi_disconnect;
    @BindView(R.id.wifi_connect)
    ImageView wifi_connect;
    @BindView(R.id.tv_title_txt_main)
    TextView tv_title_txt_main;
    @BindView(R.id.tv_title_txt1_main)
    TextView tv_title_txt1_main;
    @BindView(R.id.title_left_main)
    RelativeLayout title_left_main;
    @BindView(R.id.wifi_disconnect_main)
    ImageView wifi_disconnect_main;
    @BindView(R.id.wifi_connect_main)
    ImageView wifi_connect_main;
    @BindView(R.id.rel_main)
    RelativeLayout rel_main;
    @BindView(R.id.rel_splash)
    RelativeLayout rel_splash;
    private boolean netStatus;
    final Timer t = new Timer();
    private LoadingDialog loadingDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadingDialog = new LoadingDialog(this);
        loadingDialog.setMessage("正在请求刷脸终端检测......");
    }


    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        unbinder = ButterKnife.bind(this);
        ctx = this;
        initBigImageShowResource();
        //把Activity添加到集合里面
        CommonAction.getInstance().addActivity(this);
        init();
    }

    public void init() {
    }

    public void initBigImageShowResource(){
    }

    public void showDialog(){
        loadingDialog.show();
    }

    public void disdialog(){
        t.schedule(new TimerTask() {
            public void run() {
                loadingDialog.dismiss();
            }
        }, 2000);
    }

    @Override
    protected void onStart() {
        super.onStart();
        //注册网络状态监听的广播
        registerBroadcastReceiver();
    }

    private void registerBroadcastReceiver() {
        if (netBroadcastReceiver == null) {
            netBroadcastReceiver = new NetBroadcastReceiver();
            IntentFilter filter = new IntentFilter();
            filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
            registerReceiver(netBroadcastReceiver, filter);
            //设置监听
            netBroadcastReceiver.setStatusMonitor((NetBroadcastReceiver.NetStatusMonitor) this);
        }
    }


    public void setTitle(String title) {

        tv_title_txt.setText(title);
    }

    public void setNewTitle(String title) {
        rel_splash.setVisibility(View.VISIBLE);
        rel_main.setVisibility(View.GONE);
        title_left.setVisibility(View.GONE);
        tv_title_txt.setText(title);
    }
    public void setMainTitle(String title) {
        rel_splash.setVisibility(View.GONE);
        rel_main.setVisibility(View.VISIBLE);
        title_left_main.setVisibility(View.GONE);
        tv_title_txt_main.setText(title);
    }

    public void setWifiStatus(){
        wifi_connect.setVisibility(View.VISIBLE);
        wifi_disconnect.setVisibility(View.GONE);
    }

    private void setDisWifiStatus() {
        wifi_connect.setVisibility(View.GONE);
        wifi_disconnect.setVisibility(View.VISIBLE);
    }

    public void setTitle1() {
        tv_title_txt1.setVisibility(View.VISIBLE);
        tv_title_txt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPrefUtility.setParam(BaseTitleActivity.this, SharedPrefUtility.IS_LOGIN, false);
                SharedPrefUtility.removeParam(BaseTitleActivity.this, SharedPrefUtility.LOGIN_DATA);

                Intent intent=new Intent(BaseTitleActivity.this, MainActivity.class);
                startActivity(intent);
                CommonAction.getInstance().OutSign();
            }
        });
    }

    @OnClick(R.id.title_left)
    public void iv_title_leftOnClick() {
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            if (netBroadcastReceiver != null) {
                //注销广播
                unregisterReceiver(netBroadcastReceiver);
            }
            unbinder.unbind();
        } catch (Exception e) {
            Log.i("ButterKnife", "unbind failed");
        }

    }

    @Override
    public void onNetChange(boolean netStatus) {
        this.netStatus = netStatus;
        isNetConnect();
    }

    private void isNetConnect() {
        Message message=new Message();
        if (netStatus){
            message.what=99;
            handler.sendMessage(message);
        }else {
            message.what=100;
            handler.sendMessage(message);
        }
    }
    @SuppressLint("HandlerLeak")
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what != 100){
                SharedPrefUtility.setParam(ctx, SharedPrefUtility.IS_WIFI, true);
            }else {
                SharedPrefUtility.setParam(ctx, SharedPrefUtility.IS_WIFI, false);
            }
        }
    };
}
