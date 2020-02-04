package com.dilusense.faceplaydemo.view;


import android.net.wifi.ScanResult;

import com.dilusense.faceplaydemo.model.WifiResult;

import java.util.ArrayList;
import java.util.List;

/**
 * wifi连接处理
 */
public interface wifiConnection {

    void showScanResult(List<ScanResult> wifiResult);

    void showScanNoResult(String msg);

    void showScanFailedResult(String errMsg);

    void showNoCompareData(int errCode, String errMsg);
}
