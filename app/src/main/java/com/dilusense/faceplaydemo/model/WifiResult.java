package com.dilusense.faceplaydemo.model;


import java.io.Serializable;

/**
 * wifi信息
 */
public class WifiResult implements Serializable{
    //地址
    public String wifiAddress;
    //wifi名称
    public String wifiName;
    //加密方式
    public String encryptionWay;
    //信号强度
    public String signalStrength;
    //接入频率
    public String accessFrequency;

    public String getWifiAddress() {
        return wifiAddress;
    }

    public void setWifiAddress(String wifiAddress) {
        this.wifiAddress = wifiAddress;
    }

    public String getWifiName() {
        return wifiName;
    }

    public void setWifiName(String wifiName) {
        this.wifiName = wifiName;
    }

    public String getEncryptionWay() {
        return encryptionWay;
    }

    public void setEncryptionWay(String encryptionWay) {
        this.encryptionWay = encryptionWay;
    }

    public String getSignalStrength() {
        return signalStrength;
    }

    public void setSignalStrength(String signalStrength) {
        this.signalStrength = signalStrength;
    }

    public String getAccessFrequency() {
        return accessFrequency;
    }

    public void setAccessFrequency(String accessFrequency) {
        this.accessFrequency = accessFrequency;
    }
}
