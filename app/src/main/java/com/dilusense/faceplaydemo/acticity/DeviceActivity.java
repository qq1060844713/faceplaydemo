package com.dilusense.faceplaydemo.acticity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.badoo.mobile.util.WeakHandler;
import com.dilusense.faceplaydemo.MainActivity;
import com.dilusense.faceplaydemo.R;
import com.dilusense.faceplaydemo.acticity.base.BaseTitleActivity;
import com.dilusense.faceplaydemo.adapter.WifiScanAdapterItemClickListener;
import com.dilusense.faceplaydemo.adapter.deviceAdapter;
import com.dilusense.faceplaydemo.databindings.SharedPrefUtility;
import com.dilusense.faceplaydemo.model.WifiResult;
import com.dilusense.faceplaydemo.presenter.wifiPresenter;
import com.dilusense.faceplaydemo.service.NetBroadcastReceiver;
import com.dilusense.faceplaydemo.utils.IntentUtils;
import com.dilusense.faceplaydemo.utils.MyConstants;
import com.dilusense.faceplaydemo.utils.PermissionUtil;
import com.dilusense.faceplaydemo.utils.PermissionsUtils;
import com.dilusense.faceplaydemo.utils.WifiAdmin;
import com.dilusense.faceplaydemo.view.PassWordDialog;
import com.dilusense.faceplaydemo.view.wifiConnection;
import com.hb.dialog.dialog.LoadingDialog;
import com.hb.dialog.dialog.LoadingFragmentDialog;
import com.hb.dialog.myDialog.MyAlertInputDialog;
import com.jflavio1.wificonnector.WifiConnector;
import com.jflavio1.wificonnector.interfaces.ConnectionResultListener;
import com.mingle.widget.LoadingView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class DeviceActivity extends BaseTitleActivity implements wifiConnection {
    @BindView(R.id.device_button)
    Button device_button;
    @BindView(R.id.listview)
    ListView listview;
    private wifiPresenter wifiPresenter;
    private Unbinder bind;
    WeakHandler handler;
    private MyAlertInputDialog myAlertInputDialog;
    private WifiConnector wifiConnector;
    private PassWordDialog passWordDialog;
    WifiAdmin wifiAdmin;
    private WifiManager mWifiManager;
    private Message message;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device);
        bind = ButterKnife.bind(this);
        handler = new WeakHandler();
        wifiAdmin = new WifiAdmin(this);
        initView();
    }


    private void initView() {
        myAlertInputDialog = new MyAlertInputDialog(this).builder().setTitle("请输入密码").setEditText("");
        wifiConnector = new WifiConnector(this);
        wifiPresenter = new wifiPresenter(this);
        wifiPresenter.createWifiConnectorObject(DeviceActivity.this);
        showWifiDialog();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        setNewTitle("未连接设备终端");
    }

    @Override
    public void showScanResult(final List<ScanResult> wifiResult) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                try {
                    device_button.setVisibility(View.GONE);
                    listview.setVisibility(View.VISIBLE);
                    disdialog();
                    listview.setAdapter(new deviceAdapter(ctx, "", wifiConnector, false, wifiAdmin.filterScanResult(wifiResult), new WifiScanAdapterItemClickListener() {
                        @Override
                        public void onClick(final ScanResult crm) {
                            if (crm.capabilities.contains("WPA")) {
                                passWordDialog = new PassWordDialog(ctx);
                                passWordDialog.setTitle("请输入" + crm.SSID + "的密码");
                                passWordDialog.setYesOnclickListener("", new PassWordDialog.onYesOnclickListener() {
                                    @Override
                                    public void onYesClick(String phone) {
                                        showDialog_wifi();
                                        connectWifi(crm, phone);
//                                    connectToWifiAccessPoint(crm, phone);
                                    }
                                });
                                passWordDialog.show();
                            } else {
                                showDialog_wifi();
                                connectWifi(crm, "");
                            }
                        }
                    }));
                } catch (Exception e) {
                    Log.e("TAG", e.getMessage());
                }
            }
        });
    }

    @Override
    public void showScanNoResult(String msg) {
    }

    @Override
    public void showScanFailedResult(String errMsg) {
    }

    @Override
    public void showNoCompareData(int errCode, String errMsg) {
    }


    public int connectWifi(ScanResult crm, String password) {
        try {
            if (password != "") {
                passWordDialog.dismiss();
            }
            String wserviceName = Context.WIFI_SERVICE;
            mWifiManager = (WifiManager) this.getSystemService(wserviceName);
            int wifiId = mWifiManager.addNetwork(CreateWifiInfo(crm, password));
            if (wifiId == -1) {
                disdialog();
                toastMessage(2504);
                return -1;
            }
            return wifiId;
        } catch (Exception e){
            Log.e("wifi",e.getMessage());
        }
       return 0;
    }

    public WifiConfiguration CreateWifiInfo(ScanResult scan, String Password) {
        WifiConfiguration config = new WifiConfiguration();
        config.hiddenSSID = false;
        config.SSID = scan.SSID;
        config.status = WifiConfiguration.Status.ENABLED;
        if (scan.capabilities.contains("WEP")) {
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
            config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
            config.wepTxKeyIndex = 0;
            config.wepKeys[0] = Password;
        } else if (scan.capabilities.contains("PSK")) {
            config.preSharedKey = "\"" + Password + "\"";
        } else if (scan.capabilities.contains("EAP")) {
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_EAP);
            config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
            config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
            config.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
            config.preSharedKey = "\"" + Password + "\"";
        } else {
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
            config.preSharedKey = null;
            config.wepKeys[0] = "\"" + "\"";
            config.wepTxKeyIndex = 0;
        }
        return config;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bind.unbind();
        wifiPresenter.destroyWifiConnectorListeners();
    }

    @Override
    public void onNetChange(boolean netStatus) {
        this.netStatus = netStatus;
        isNetConnect();
    }

    public void isNetConnect() {
        message = new Message();
        if (netStatus) {
            message.what = 99;
            handler1.sendMessage(message);
        } else {
            message.what = 100;
            handler1.sendMessage(message);
        }
    }

    @SuppressLint("HandlerLeak")
    public Handler handler1 = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what != 100) {
                try {
                    loadingDialog.dismiss();
                    IntentUtils.entryActivity(DeviceActivity.this, MainActivity.class);
                    finish();
                }catch (Exception e){
                    e.getMessage();
                }
            }
        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        ;
        unregisterReceiver(netBroadcastReceiver);
    }
}
