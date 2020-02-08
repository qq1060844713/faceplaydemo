package com.dilusense.faceplaydemo;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.dilusense.faceplaydemo.acticity.DeviceActivity;
import com.dilusense.faceplaydemo.acticity.PaymentActivity;
import com.dilusense.faceplaydemo.acticity.base.BaseTitleActivity;
import com.dilusense.faceplaydemo.adapter.WifiListRvAdapter;
import com.dilusense.faceplaydemo.adapter.WifiScanAdapterItemClickListener;
import com.dilusense.faceplaydemo.adapter.deviceAdapter;
import com.dilusense.faceplaydemo.databindings.SharedPrefUtility;
import com.dilusense.faceplaydemo.net.utils.WifiUtils;
import com.dilusense.faceplaydemo.network.result.PassPerson;
import com.dilusense.faceplaydemo.network.result.PayInfoResult;
import com.dilusense.faceplaydemo.presenter.PayPresenter;
import com.dilusense.faceplaydemo.utils.IntentUtils;
import com.dilusense.faceplaydemo.utils.MyConstants;
import com.dilusense.faceplaydemo.utils.WifiAdmin;
import com.dilusense.faceplaydemo.view.DisConnDialog;
import com.dilusense.faceplaydemo.view.PassWordDialog;
import com.dilusense.faceplaydemo.view.PayResultView;
import com.dilusense.faceplaydemo.view.wifiConnection;
import com.hb.dialog.dialog.LoadingDialog;
import com.hb.dialog.dialog.LoadingFragmentDialog;
import com.hb.dialog.myDialog.MyAlertDialog;
import com.hb.dialog.myDialog.MyAlertInputDialog;
import com.jflavio1.wificonnector.WifiConnector;
import com.jflavio1.wificonnector.interfaces.ConnectionResultListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends BaseTitleActivity implements PayResultView {

    @BindView(R.id.pay_result)
    Button pay_result;
    @BindView(R.id.pay_num)
    EditText pay_num;
    @BindView(R.id.wifi_list1)
    LinearLayout wifi_list;
    @BindView(R.id.listview)
    ListView listview;
    @BindView(R.id.wifiRv)
    RecyclerView rv;
    private PayPresenter payPresenter;
    private int flog = 1;
    private WifiConnector wifiConnector;
    private String wifiName;
    private WifiAdmin wifiAdmin;
    private MyAlertDialog myAlertDialog;
    private WifiListRvAdapter adapter;
    private WifiUtils mUtils;
    private WifiInfo currentWifiInfo;
    private String currentWifiSSID;
    private int wifiNetworkId;
    private PassWordDialog passWordDialog;
    private com.dilusense.faceplaydemo.adapter.deviceAdapter deviceAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        payPresenter = new PayPresenter(this);
        wifiAdmin = new WifiAdmin(this);
        wifiConnector = new WifiConnector(this);
        mUtils = new WifiUtils(this);
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        setMainTitle(1,"已连接" + "(" + wifiName + ")");
        wifi_list.setVisibility(View.GONE);
    }

    @OnClick(R.id.rel_main)
    public void checkWifi() {
        if (MyConstants.isEven(flog)) {
            wifi_list.setVisibility(View.GONE);
        } else {
            wifiAdmin.startScan();
            currentWifiInfo = mUtils.getCurrentWifiInfo();
            wifiNetworkId = currentWifiInfo.getNetworkId();
            currentWifiSSID = currentWifiInfo.getSSID().replace("\"","").replace("\"","");
            wifi_list.setVisibility(View.VISIBLE);
            scanWifiDevice();
        }
        flog++;
    }

    private void scanWifiDevice() {
        List<ScanResult> wifiList = wifiAdmin.getWifiList();
        deviceAdapter = new deviceAdapter(ctx, currentWifiSSID, wifiConnector, true, wifiAdmin.filterScanResult(wifiList), new WifiScanAdapterItemClickListener() {
            @Override
            public void onClick(final ScanResult crm) {
                openWifi(crm);
            }
        });
        listview.setAdapter(deviceAdapter);
    }

    private void openWifi(final ScanResult crm) {
        String ssid = (String) SharedPrefUtility.getParam(ctx, SharedPrefUtility.WIFI_INFO, "");
        if (ssid.equals(crm.SSID)) {
            disConnect(crm.SSID);
        } else {
            passWordDialog = new PassWordDialog(ctx);
            if (crm.capabilities.contains("WPA")) {
                passWordDialog.setTitle("请输入(" + crm.SSID + ")的密码");
                passWordDialog.setYesOnclickListener("", new PassWordDialog.onYesOnclickListener() {
                    @Override
                    public void onYesClick(String phone) {
                        showDialog_wifi();
                        passWordDialog.dismiss();
                        connectToWifi(crm, phone);
                    }
                });
                passWordDialog.show();
            } else {
                showDialog_wifi();
                connectToWifi(crm, "");
            }
        }
    }
    private void connectToWifi(final ScanResult scanResult, String password) {
        wifiAdmin.forgetWifi(currentWifiSSID);
        wifiAdmin.disconnectWifi(wifiNetworkId);
        deviceAdapter.changetShowDelImage(false);
        wifiConnector.setScanResult(scanResult, password);
        wifiConnector.setLog(true);
        progressDismiss();
        wifiConnector.connectToWifi(new ConnectionResultListener() {
            @Override
            public void successfulConnect(String SSID) {
                passWordDialog.dismiss();
                deviceAdapter.changetShowDelImage(true);
            }

            @Override
            public void errorConnect(int codeReason) {
                Log.d("wifi","连接失败");
                passWordDialog.dismiss();
                toastMessage(2504,"");
                loadingDialog.dismiss();
            }

            @Override
            public void onStateChange(SupplicantState supplicantState) {

            }
        });
    }

    private void disConnect(String msg) {
        DisConnDialog disConnDialog = new DisConnDialog(ctx);
        disConnDialog.setTitle("确定与"+"'"+"设备"+msg+"'"+"断开连接");
        disConnDialog.setYesOnclickListener("", new DisConnDialog.onYesOnclickListener() {
            @Override
            public void onYesClick() {
                wifiAdmin.disconnectWifi(wifiNetworkId);
                wifiAdmin.forgetWifi(currentWifiSSID);
                finish();
                IntentUtils.entryActivity(MainActivity.this, DeviceActivity.class);
            }
        });
        disConnDialog.show();
    }

    @OnClick(R.id.pay_result)
    public void pay_reslut() {
        loadingDialog.show(getSupportFragmentManager(), "msg");
        if (pay_num.getText().length() != 0) {
            payPresenter.payMoney(this, pay_num.getText().toString());
        } else {
            toastMessage(101,"金额不能为空");
        }
    }

    @Override
    public void showPayResult(PassPerson payResult) {
        if (payResult.getData().getData()!=null) {
            disdialog();
            Intent intent = new Intent(MainActivity.this, PaymentActivity.class);
            intent.putExtra("person_data", payResult);
            startActivity(intent);
        }
    }

    @Override
    public void showPayFailedResult(String errMsg) {
        disdialog();
        if (errMsg.equals("timeout")) {
            IntentUtils.entryActivity(MainActivity.this, PaymentActivity.class);
        } else if (errMsg.contains("Failed to connect")) {
            Toast.makeText(ctx, "未连接终端设备", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(ctx, MyConstants.MSG_NET_SERVER_INTERNAL_ERROR, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void showNoPayData(int errCode, String errMsg) {
        disdialog();
    }

    @Override
    public void showPayResultData(String errMsg) {
        Toast.makeText(ctx, errMsg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showErroePayResultData(int errMsg) {
        disdialog();
        Toast.makeText(ctx, MyConstants.codeMsg(errMsg), Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        registerBroadcastReceiver();
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
    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what != 100) {
                try {
                    WifiInfo currentWifiInfo = mUtils.getCurrentWifiInfo();
                    currentWifiSSID = currentWifiInfo.getSSID().replace("\"", "").replace("\"", "");
                    setWifiStatus(currentWifiSSID);
                    wifiAdmin.startScan();
                    wifiNetworkId = currentWifiInfo.getNetworkId();
                    SharedPrefUtility.setParam(ctx, SharedPrefUtility.WIFI_INFO, currentWifiSSID);
                    scanWifiDevice();
                    deviceAdapter.changetShowDelImage(true);
                    loadingDialog.dismiss();
                } catch (Exception e) {

                }
            }  else {
                SharedPrefUtility.setParam(ctx, SharedPrefUtility.WIFI_INFO, "");
                setDisWifiStatus();
                deviceAdapter.changetShowDelImage(false);
            }
        }
    };
}
