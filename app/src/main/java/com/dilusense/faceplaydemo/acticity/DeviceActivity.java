package com.dilusense.faceplaydemo.acticity;

import android.Manifest;
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
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
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
import com.dilusense.faceplaydemo.utils.IntentUtils;
import com.dilusense.faceplaydemo.utils.MyConstants;
import com.dilusense.faceplaydemo.utils.PermissionUtil;
import com.dilusense.faceplaydemo.utils.PermissionsUtils;
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device);
        bind = ButterKnife.bind(this);
        handler = new WeakHandler();
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
    protected void onStart() {
        super.onStart();
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
                device_button.setVisibility(View.GONE);
                listview.setVisibility(View.VISIBLE);
                List<ScanResult> crms = new ArrayList<>();
                disdialog();
                for (ScanResult cr : wifiResult) {
                    crms.add(cr);
                }
                if (crms.size() < 1) {
                    return;
                }
                listview.setAdapter(new deviceAdapter(ctx, "",wifiConnector, false, crms, new WifiScanAdapterItemClickListener() {
                    @Override
                    public void onClick(final ScanResult crm) {
                        if (crm.capabilities.contains("WPA")) {
                            passWordDialog = new PassWordDialog(ctx);
                            passWordDialog.setTitle("请输入" + crm.SSID + "的密码");
                            passWordDialog.setYesOnclickListener("", new PassWordDialog.onYesOnclickListener() {
                                @Override
                                public void onYesClick(String phone) {
                                    showDialog_wifi();
                                    connectToWifiAccessPoint(crm, phone);
                                }
                            });
                            passWordDialog.show();
                        } else {
                            showDialog_wifi();
                            connectToWifiAccessPoint(crm, "");
                        }
                    }
                }));
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

    public void connectToWifiAccessPoint(final ScanResult scanResult, String password) {
        wifiConnector.setScanResult(scanResult, password);
        wifiConnector.setLog(true);
        wifiConnector.connectToWifi(new ConnectionResultListener() {
            @Override
            public void successfulConnect(String SSID) {
                progressDismiss();
                SharedPrefUtility.setParam(ctx, SharedPrefUtility.WIFI_INFO, scanResult.SSID);
                SharedPrefUtility.setParam(ctx, SharedPrefUtility.WIFI_INFO_ID, scanResult.centerFreq0);
                IntentUtils.entryActivity(DeviceActivity.this, MainActivity.class);
            }

            @Override
            public void errorConnect(int codeReason) {
                progressDismiss();
                toastMessage(codeReason);
                if (codeReason == 2503) {
                    SharedPrefUtility.setParam(ctx, SharedPrefUtility.WIFI_INFO, scanResult.SSID);
                    SharedPrefUtility.setParam(ctx, SharedPrefUtility.WIFI_INFO_ID, scanResult.centerFreq0);
                    IntentUtils.entryActivity(DeviceActivity.this, MainActivity.class);
                }
            }

            @Override
            public void onStateChange(SupplicantState supplicantState) {

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bind.unbind();
        wifiPresenter.destroyWifiConnectorListeners();
    }
}
