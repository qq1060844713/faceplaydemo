package com.dilusense.faceplaydemo;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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
    private static final int LOCATION_CODE = 1;
    /**
     * 位置权限
     */
    public void quanxian(final Activity context) {
        try {
            String wserviceName = Context.WIFI_SERVICE;
            WifiManager mWifiManager = (WifiManager) context.getSystemService(wserviceName);
            if (!isWifiAvailable()){
                mWifiManager.setWifiEnabled(true);
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(context,
                            new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_CODE);
                }
            }
        } catch (SecurityException e) {
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        bind = ButterKnife.bind(this);
        PermissionsUtils.getInstance().chekPermissions(this, permissions, permissionsResult);
        quanxian(this);
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
//            if (isWifi(SplashActivity.this) && MyConstants.isLockNessMonster(wifiId)) {
            if (isWifi(SplashActivity.this)) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        IntentUtils.entryActivity(SplashActivity.this, MainActivity.class);
                        finish();
                    }
                }, 2000);
            } else {
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

    public boolean isWifiAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected() && networkInfo.getType() == ConnectivityManager.TYPE_WIFI);
    }

}
