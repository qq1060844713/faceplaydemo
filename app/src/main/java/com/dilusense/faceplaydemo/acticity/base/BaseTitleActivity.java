package com.dilusense.faceplaydemo.acticity.base;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dilusense.faceplaydemo.MainActivity;
import com.dilusense.faceplaydemo.R;
import com.dilusense.faceplaydemo.databindings.AbstractTemplateActivity;
import com.dilusense.faceplaydemo.databindings.CommonAction;
import com.dilusense.faceplaydemo.databindings.SharedPrefUtility;
import com.dilusense.faceplaydemo.net.utils.WifiUtils;
import com.dilusense.faceplaydemo.service.NetBroadcastReceiver;
import com.dilusense.faceplaydemo.utils.CustomToast;
import com.dilusense.faceplaydemo.utils.MyConstants;
import com.hb.dialog.dialog.LoadingFragmentDialog;

import java.util.Timer;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by Thinkpad on 2017/3/8.
 */
public class BaseTitleActivity extends AbstractTemplateActivity implements NetBroadcastReceiver.NetStatusMonitor {
    public NetBroadcastReceiver  netBroadcastReceiver;
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
    public boolean netStatus;
    public LoadingFragmentDialog loadingDialog;
    private ProgressDialog progressdlg = null;
    private WifiUtils mUtils;
    private CustomToast toast;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadingDialog = new LoadingFragmentDialog();
        loadingDialog.setCancelable(false);
        loadingDialog.setMessage("仿作伪验证中");
        mUtils = new WifiUtils(this);
    }


    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        unbinder = ButterKnife.bind(this);
        ctx = this;
        CommonAction.getInstance().addActivity(this);
    }

    public void showWifiDialog() {
        loadingDialog.setMessage("搜索中");
        loadingDialog.show(getSupportFragmentManager(), "msg");
    }

    public void disdialog() {
        loadingDialog.dismiss();
    }
    public void toastMessage(int content,String msg) {
        if (toast != null) {
            toast.hide();
        }
        toast = new CustomToast(BaseTitleActivity.this, (ViewGroup) this.findViewById(R.id.toast_custom_parent));
        if (content == 101) {
            toast.show(msg, 500);
        } else {
            toast.show(MyConstants.codeMsg(content), 500);
        }
    }
    @Override
    protected void onStart() {
        super.onStart();
        //注册网络状态监听的广播
        registerBroadcastReceiver();
    }

    public void registerBroadcastReceiver() {
        if (netBroadcastReceiver == null) {
            netBroadcastReceiver = new NetBroadcastReceiver();
            IntentFilter filter = new IntentFilter();
            filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
            registerReceiver(netBroadcastReceiver, filter);
            //设置监听
            netBroadcastReceiver.setStatusMonitor((NetBroadcastReceiver.NetStatusMonitor) this);
        }
    }

    public void setNewTitle(String title) {
        rel_splash.setVisibility(View.VISIBLE);
        rel_main.setVisibility(View.GONE);
        title_left.setVisibility(View.GONE);
        tv_title_txt.setText(title);
    }
    public void setMainTitle(int code,String title) {
        if (code == 0) {
            title_left_main.setVisibility(View.GONE);
        }
        rel_splash.setVisibility(View.GONE);
        rel_main.setVisibility(View.VISIBLE);
        tv_title_txt_main.setText(title);
    }

    public void setWifiStatus(String wifiName){
        tv_title_txt_main.setText("已连接"+"("+wifiName+")");
    }

    public void setDisWifiStatus() {
        tv_title_txt_main.setText("未连接终端设备");
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

    public void isNetConnect() {
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
   public Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what != 100){
                WifiInfo currentWifiInfo = mUtils.getCurrentWifiInfo();
                String currentWifiSSID = currentWifiInfo.getSSID().replace("\"","").replace("\"","");
                setWifiStatus(currentWifiSSID);
                getWifiStatus(currentWifiSSID);
                SharedPrefUtility.setParam(ctx, SharedPrefUtility.IS_WIFI, true);
                SharedPrefUtility.setParam(ctx, SharedPrefUtility.WIFI_INFO, currentWifiSSID);
            }else {
                setDisWifiStatus();
                SharedPrefUtility.setParam(ctx, SharedPrefUtility.IS_WIFI, false);
                SharedPrefUtility.setParam(ctx, SharedPrefUtility.WIFI_INFO, "暂无wifi");
            }
        }
    };

    public String getWifiStatus(String currentWifiSSID) {
           return currentWifiSSID;
    }

    public void showDialog_wifi() {
        loadingDialog.setMessage("连接中");
        loadingDialog.show(getSupportFragmentManager(), "msg");
    }
    public void progressDismiss() {
        if (progressdlg != null) {
            progressdlg.dismiss();
        }
    }
}
