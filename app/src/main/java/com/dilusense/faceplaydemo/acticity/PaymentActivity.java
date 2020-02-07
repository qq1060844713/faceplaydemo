package com.dilusense.faceplaydemo.acticity;

import android.annotation.SuppressLint;
import android.net.wifi.ScanResult;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiInfo;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.dilusense.faceplaydemo.MainActivity;
import com.dilusense.faceplaydemo.R;
import com.dilusense.faceplaydemo.acticity.base.BaseTitleActivity;
import com.dilusense.faceplaydemo.adapter.WifiListRvAdapter;
import com.dilusense.faceplaydemo.adapter.WifiScanAdapterItemClickListener;
import com.dilusense.faceplaydemo.adapter.deviceAdapter;
import com.dilusense.faceplaydemo.databindings.SharedPrefUtility;
import com.dilusense.faceplaydemo.net.utils.WifiUtils;
import com.dilusense.faceplaydemo.network.result.PayInfoResult;
import com.dilusense.faceplaydemo.presenter.PayPresenter;
import com.dilusense.faceplaydemo.utils.IntentUtils;
import com.dilusense.faceplaydemo.utils.MyConstants;
import com.dilusense.faceplaydemo.utils.WifiAdmin;
import com.dilusense.faceplaydemo.view.PassWordDialog;
import com.dilusense.faceplaydemo.view.PayResultView;
import com.hb.dialog.dialog.LoadingDialog;
import com.hb.dialog.myDialog.MyAlertDialog;
import com.jflavio1.wificonnector.WifiConnector;
import com.jflavio1.wificonnector.interfaces.ConnectionResultListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class PaymentActivity extends BaseTitleActivity implements PayResultView {
    String TAG="result";
    private int flog=1;
    private LoadingDialog loadingDialog;
    @BindView(R.id.wifi_list1)
    LinearLayout wifi_list;
    @BindView(R.id.show_pay)
    LinearLayout show_pay;
    @BindView(R.id.show_pay_result)
    LinearLayout show_pay_result;
    @BindView(R.id.listview)
    ListView listview;
    @BindView(R.id.wifiRv)
    RecyclerView rv;
    Button pay;
    private WifiConnector wifiConnector;
    private WifiAdmin wifiAdmin;
    private WifiListRvAdapter adapter;
    private WifiUtils mUtils;
    private WifiInfo currentWifiInfo;
    private String currentWifiSSID;
    private int wifiNetworkId;
    private PassWordDialog passWordDialog;
    private MyAlertDialog myAlertDialog;
    private com.dilusense.faceplaydemo.adapter.deviceAdapter deviceAdapter;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        loadingDialog = new LoadingDialog(this);
        loadingDialog.setMessage("正在请求搜索wifi......");
        wifiConnector = new WifiConnector(this);
        wifiAdmin = new WifiAdmin(this);
        wifiConnector = new WifiConnector(this);
        mUtils = new WifiUtils(this);
    }
    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        String wifiName = (String) SharedPrefUtility.getParam(ctx, SharedPrefUtility.WIFI_INFO, "");
        setMainTitle("已连接"+"("+wifiName+")");
    }


//    private void openWifi(final ScanResult crm) {
//        String ssid = (String) SharedPrefUtility.getParam(ctx, SharedPrefUtility.WIFI_INFO, "");
//        if (ssid.equals(crm.SSID)) {
//            disConnect();
//        } else {
//            passWordDialog = new PassWordDialog(ctx);
//            if (crm.capabilities.contains("WPA")) {
//                passWordDialog.setTitle("请输入" + crm.SSID + "的密码");
//                passWordDialog.setYesOnclickListener("", new PassWordDialog.onYesOnclickListener() {
//                    @Override
//                    public void onYesClick(String phone) {
//                        showDialog_wifi();
//                        wifiAdmin.forgetWifi(currentWifiSSID);
//                        wifiAdmin.disconnectWifi(wifiNetworkId);
//                        deviceAdapter.changetShowDelImage(false);
//                        connectToWifi(crm, phone);
//                    }
//                });
//                passWordDialog.show();
//            } else {
//                showDialog_wifi();
//                connectToWifi(crm, "");
//            }
//        }
//    }
//
//    private void connectToWifi(final ScanResult scanResult, String password) {
//        wifiConnector.setScanResult(scanResult, password);
//        wifiConnector.setLog(true);
//        progressDismiss();
//        wifiConnector.connectToWifi(new ConnectionResultListener() {
//            @Override
//            public void successfulConnect(String SSID) {
//                passWordDialog.dismiss();
//                deviceAdapter.changetShowDelImage(true);
//            }
//
//            @Override
//            public void errorConnect(int codeReason) {
//                Log.d("wifi", "连接失败");
//                toastMessage(codeReason);
//            }
//
//            @Override
//            public void onStateChange(SupplicantState supplicantState) {
//
//            }
//        });
//    }
//    private void disConnect() {
//        myAlertDialog = new MyAlertDialog(this).builder().setTitle("确定断开连接？").setPositiveButton("确认", new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                wifiAdmin.disconnectWifi(wifiNetworkId);
//                wifiAdmin.forgetWifi(currentWifiSSID);
//                finish();
//                IntentUtils.entryActivity(PaymentActivity.this, DeviceActivity.class);
//            }
//        }).setNegativeButton("取消", new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//            }
//        });
//        myAlertDialog.show();
//    }
    @OnClick(R.id.pay)
    public void pay(){
        show_pay.setVisibility(View.GONE);
        show_pay_result.setVisibility(View.VISIBLE);
    }

    @Override
    public void showPayResult(List<PayInfoResult> payResult) {

    }

    @Override
    public void showPayFailedResult(String errMsg) {
        Log.d(TAG,errMsg);
    }

    @Override
    public void showNoPayData(int errCode, String errMsg) {
        Log.d(TAG,errMsg);
    }

//    @OnClick(R.id.rel_main)
//    public void checkWifi() {
//        if (MyConstants.isEven(flog)) {
//            wifi_list.setVisibility(View.GONE);
//        } else {
//            wifiAdmin.startScan();
//            currentWifiInfo = mUtils.getCurrentWifiInfo();
//            wifiNetworkId = currentWifiInfo.getNetworkId();
//            currentWifiSSID = currentWifiInfo.getSSID().replace("\"","").replace("\"","");
//            wifi_list.setVisibility(View.VISIBLE);
//            scanWifiDevice();
//        }
//        flog++;
//    }

//    private void scanWifiDevice() {
//        List<ScanResult> wifiList = wifiAdmin.getWifiList();
//        deviceAdapter = new deviceAdapter(ctx, currentWifiSSID, wifiConnector, true, wifiAdmin.filterScanResult(wifiList), new WifiScanAdapterItemClickListener() {
//            @Override
//            public void onClick(final ScanResult crm) {
//                openWifi(crm);
//            }
//        });
//        listview.setAdapter(deviceAdapter);
//    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//        registerBroadcastReceiver();
//    }
//
//    @Override
//    public void onNetChange(boolean netStatus) {
//        this.netStatus = netStatus;
//        isNetConnect();
//    }
//
//    public void isNetConnect() {
//        Message message = new Message();
//        if (netStatus) {
//            message.what = 99;
//            handler.sendMessage(message);
//        } else {
//            message.what = 100;
//            handler.sendMessage(message);
//        }
//    }
//
//    @SuppressLint("HandlerLeak")
//    public Handler handler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            if (msg.what != 100) {
//                WifiInfo currentWifiInfo = mUtils.getCurrentWifiInfo();
//                currentWifiSSID = currentWifiInfo.getSSID().replace("\"", "").replace("\"", "");
//                setWifiStatus(currentWifiSSID);
//                wifiAdmin.startScan();
//                wifiNetworkId = currentWifiInfo.getNetworkId();
//                scanWifiDevice();
//                deviceAdapter.changetShowDelImage(true);
//            } else {
//                setDisWifiStatus();
//            }
//        }
//    };
}
