package com.dilusense.faceplaydemo.presenter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
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
import java.util.LinkedHashMap;
import java.util.List;


public class wifiPresenter{
    private wifiConnection wifiConnection;
    private List<ScanResult> mWifiList = new ArrayList<>();
    private Context ctx;
    private WifiConnector wifiConnector;

    public wifiPresenter(wifiConnection view) {
        wifiConnection = view;
    }

    public void createWifiConnectorObject(Context context){
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
                        wifiConnection.showScanResult(wifiScanResult);
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
    public static List<ScanResult> filterScanResult(final List<ScanResult> list) {
        LinkedHashMap<String, ScanResult> linkedMap = new LinkedHashMap<>(list.size());
        for (ScanResult rst : list) {
            if (linkedMap.containsKey(rst.SSID)) {
                if (rst.level > linkedMap.get(rst.SSID).level) {
                    linkedMap.put(rst.SSID, rst);
                }
                continue;
            }
            linkedMap.put(rst.SSID, rst);
        }
        list.clear();
        list.addAll(linkedMap.values());
        return list;
    }
    public void destroyWifiConnectorListeners() {
        wifiConnector.unregisterWifiStateListener();
    }
}
