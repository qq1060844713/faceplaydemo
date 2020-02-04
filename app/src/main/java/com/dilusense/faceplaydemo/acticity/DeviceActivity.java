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
import com.dilusense.faceplaydemo.view.wifiConnection;
import com.hb.dialog.dialog.LoadingDialog;
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
    private static final String TAG = "Wifi";
    WeakHandler handler;
    private MyAlertInputDialog myAlertInputDialog;
    private WifiConnector wifiConnector;
    private LoadingDialog loadingDialog;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device);
        bind = ButterKnife.bind(this);
        handler = new WeakHandler();
        loadingDialog = new LoadingDialog(this);
        loadingDialog.setMessage("搜索wifi。。。。");
        initView();
    }


    private void initView() {
        myAlertInputDialog = new MyAlertInputDialog(this).builder().setTitle("请输入密码").setEditText("");
        wifiConnector = new WifiConnector(this);
        wifiPresenter = new wifiPresenter(this);
        loadingDialog.show();
        wifiPresenter.createWifiConnectorObject(DeviceActivity.this);
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
        List<WifiResult> crms = new ArrayList<WifiResult>();
        handler.post(new Runnable() {
            @Override
            public void run() {
                device_button.setVisibility(View.GONE);
                listview.setVisibility(View.VISIBLE);
                loadingDialog.dismiss();
                List<ScanResult> crms = new ArrayList<>();
                for (ScanResult cr : wifiResult) {
                    crms.add(cr);
                }
                if (crms.size() < 1) {
                    return;
                }
                listview.setAdapter(new deviceAdapter(ctx, crms, new WifiScanAdapterItemClickListener() {
                    @Override
                    public void onClick(final ScanResult crm) {
                        if (crm.capabilities != "") {
                            myAlertInputDialog.setPositiveButton("确认", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    connectToWifiAccessPoint(crm, myAlertInputDialog.getResult());
                                    myAlertInputDialog.dismiss();
                                }
                            }).setNegativeButton("取消", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    myAlertInputDialog.dismiss();
                                }
                            });
                            myAlertInputDialog.show();
                        }   else {
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
                setWifiStatus();
                SharedPrefUtility.setParam(ctx, SharedPrefUtility.WIFI_INFO, SSID);
                IntentUtils.entryActivity(DeviceActivity.this,MainActivity.class);
            }

            @Override
            public void errorConnect(int codeReason) {
                Toast.makeText(ctx, "Error on connecting to wifi: " + scanResult.SSID + "\nError code: " + codeReason, Toast.LENGTH_SHORT).show();
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
