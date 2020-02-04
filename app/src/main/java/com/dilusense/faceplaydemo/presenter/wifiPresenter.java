package com.dilusense.faceplaydemo.presenter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.util.Log;
import android.widget.Toast;

import com.dilusense.faceplaydemo.MainActivity;
import com.dilusense.faceplaydemo.utils.IntentUtils;
import com.dilusense.faceplaydemo.utils.MyConstants;
import com.dilusense.faceplaydemo.view.wifiConnection;
import com.jflavio1.wificonnector.WifiConnector;
import com.jflavio1.wificonnector.interfaces.ConnectionResultListener;
import com.jflavio1.wificonnector.interfaces.ShowWifiListener;
import com.jflavio1.wificonnector.interfaces.WifiStateListener;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;


public class wifiPresenter{
    private wifiConnection wifiConnection;
    private List<ScanResult> mWifiList = new ArrayList<>();
    private Context ctx;
    private WifiConnector wifiConnector;

    public wifiPresenter(wifiConnection view) {
        wifiConnection = view;
    }

    public void getWifiScanResult(Context context) {
        String wserviceName = Context.WIFI_SERVICE;
        WifiManager mWifiManager = (WifiManager) context.getSystemService(wserviceName);

        mWifiManager.setWifiEnabled(true);
        mWifiManager.startScan();

        mWifiList.clear();
        mWifiList = mWifiManager.getScanResults();

        if (mWifiList != null && mWifiList.size() > 0) {
            wifiConnection.showScanResult(mWifiList);
        } else {
            wifiConnection.showScanNoResult("暂无wifi数据");
        }
    }
    public void createWifiConnectorObject(Context context){
        String wserviceName = Context.WIFI_SERVICE;
        WifiManager mWifiManager = (WifiManager) context.getSystemService(wserviceName);
        mWifiManager.setWifiEnabled(true);
        wifiConnector = new WifiConnector(context);
        wifiConnector.setLog(true);
        wifiConnector.registerWifiStateListener(new WifiStateListener() {
            @Override
            public void onStateChange(int wifiState) {

            }

            @Override
            public void onWifiEnabled() {
                wifiConnector.showWifiList(new ShowWifiListener() {
                    @Override
                    public void onNetworksFound(WifiManager wifiManager, List<ScanResult> wifiScanResult) {
                        List<ScanResult> wifiNum = new ArrayList<>();
                        for (ScanResult scanResult:wifiScanResult) {
                             if (MyConstants.isLockNessMonster(scanResult.SSID)){
                                 wifiNum.add(scanResult);
                             }
                        }
                        wifiConnection.showScanResult(wifiNum);
                    }

                    @Override
                    public void onNetworksFound(JSONArray wifiList) {

                    }

                    @Override
                    public void errorSearchingNetworks(int errorCode) {
                        wifiConnection.showScanFailedResult(errorCode+"获取失败");
                    }
                });
            }

            @Override
            public void onWifiEnabling() {

            }

            @Override
            public void onWifiDisabling() {

            }

            @Override
            public void onWifiDisabled() {
                wifiConnection.showScanResult(new ArrayList<ScanResult>());
            }
        });
    }

    public void destroyWifiConnectorListeners() {
        wifiConnector.unregisterWifiStateListener();
    }
}
