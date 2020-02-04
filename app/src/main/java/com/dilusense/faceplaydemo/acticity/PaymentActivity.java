package com.dilusense.faceplaydemo.acticity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.dilusense.faceplaydemo.R;
import com.dilusense.faceplaydemo.acticity.base.BaseTitleActivity;
import com.dilusense.faceplaydemo.databindings.SharedPrefUtility;
import com.dilusense.faceplaydemo.network.result.PayInfoResult;
import com.dilusense.faceplaydemo.view.PayResultView;

import java.util.List;

public class PaymentActivity extends BaseTitleActivity implements PayResultView {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
    }
    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        String wifiName = (String) SharedPrefUtility.getParam(ctx, SharedPrefUtility.WIFI_INFO, "");
        setMainTitle("已连接"+"("+wifiName+")");
    }

    @Override
    public void showPayResult(List<PayInfoResult> payResult) {
          
    }

    @Override
    public void showPayFailedResult(String errMsg) {

    }

    @Override
    public void showNoPayData(int errCode, String errMsg) {

    }
}
