package com.dilusense.faceplaydemo;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;

import com.dilusense.faceplaydemo.acticity.DeviceActivity;
import com.dilusense.faceplaydemo.utils.IntentUtils;
import com.dilusense.faceplaydemo.utils.MyConstants;
import com.dilusense.faceplaydemo.utils.PermissionsUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class SplashActivity extends Activity {
    @BindView(R.id.wifi_disconnect)
    ImageView wifi_disconnect;
    @BindView(R.id.wifi_connect)
    ImageView wifi_connect;
    private Unbinder bind;
    private String wifiId;
    private String[] permissions = new String[]{Manifest.permission.ACCESS_WIFI_STATE,
            Manifest.permission.CHANGE_WIFI_STATE,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.CHANGE_WIFI_MULTICAST_STATE,
            Manifest.permission.INTERNET};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        bind = ButterKnife.bind(this);
        PermissionsUtils.getInstance().chekPermissions(this, permissions, permissionsResult);
    }

    PermissionsUtils.IPermissionsResult permissionsResult = new PermissionsUtils.IPermissionsResult() {
        @Override
        public void passPermissons() {
            initView();
        }

        @Override
        public void forbitPermissons() {
        }
    };
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionsUtils.getInstance().onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }
    private void initView() {
        WifiManager wifiMgr = (WifiManager) this.getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifiMgr.getConnectionInfo();
        wifiId = info != null ? info.getSSID() : null;
        try {
            if (isWifi(SplashActivity.this) && MyConstants.isLockNessMonster(wifiId)) {
                wifi_connect.setVisibility(View.VISIBLE);
                wifi_disconnect.setVisibility(View.GONE);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        IntentUtils.entryActivity(SplashActivity.this, MainActivity.class);
                        finish();
                    }
                }, 2000);
            } else {
                wifi_connect.setVisibility(View.GONE);
                wifi_disconnect.setVisibility(View.VISIBLE);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        IntentUtils.entryActivity(SplashActivity.this, DeviceActivity.class);
                        finish();
                    }
                }, 2000);
            }
        } catch (Exception e) {
            e.getMessage();
        }
    }

    private static boolean isWifi(Context mContext) {
        ConnectivityManager connectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetInfo != null && activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI) {
            return true;
        }
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bind.unbind();
    }
}
