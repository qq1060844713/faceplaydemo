package com.dilusense.faceplaydemo.network.result;

/**
 * Created by Thinkpad on 2019/12/7.
 */

public class VersionResult {

    private String versionName;
    private String apkUrl;
    private int versionCode;

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public String getApkUrl() {
        return apkUrl;
    }

    public void setApkUrl(String apkUrl) {
        this.apkUrl = apkUrl;
    }

    public int getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

}
