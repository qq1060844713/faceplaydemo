package com.dilusense.faceplaydemo;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.dilusense.faceplaydemo.acticity.PaymentActivity;
import com.dilusense.faceplaydemo.acticity.base.BaseTitleActivity;
import com.dilusense.faceplaydemo.databindings.SharedPrefUtility;
import com.dilusense.faceplaydemo.network.result.PayInfoResult;
import com.dilusense.faceplaydemo.presenter.PayPresenter;
import com.dilusense.faceplaydemo.utils.IntentUtils;
import com.dilusense.faceplaydemo.utils.MyConstants;
import com.dilusense.faceplaydemo.view.PayResultView;
import com.hb.dialog.dialog.LoadingDialog;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends BaseTitleActivity implements PayResultView{

    @BindView(R.id.pay_result)
    Button pay_result;
    @BindView(R.id.pay_num)
    EditText pay_num;
    @BindView(R.id.wifi_list1)
    LinearLayout wifi_list;
    @BindView(R.id.listview)
    ListView listview;
    private PayPresenter payPresenter;
    final Timer t = new Timer();
    private int flog=1;
    private LoadingDialog loadingDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        payPresenter = new PayPresenter(this);
        loadingDialog = new LoadingDialog(this);
        loadingDialog.setMessage("正在请求刷脸终端检测......");

    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        String wifiName = (String) SharedPrefUtility.getParam(ctx, SharedPrefUtility.WIFI_INFO, "");
        setMainTitle("已连接"+"("+wifiName+")");
    }

    @OnClick(R.id.rel_main)
    public void checkWifi() {
        if (MyConstants.isEven(flog)) {
            wifi_list.setVisibility(View.GONE);
        } else {
            loadingDialog.show();
            wifi_list.setVisibility(View.VISIBLE);
        }
        flog++;
    }

    @OnClick(R.id.pay_result)
    public void pay_reslut() {
        Boolean isWiFi=(Boolean) SharedPrefUtility.getParam(ctx, SharedPrefUtility.IS_WIFI, false);
        if (isWiFi){
            if (pay_num.getText().length()!=0) {
                showDialog();
                payPresenter.payMoney(this, pay_num.getText().toString());
            }else {
                Toast.makeText(ctx, "金额不能为空", Toast.LENGTH_SHORT).show();
            }
        }else {
//            IntentUtils.entryActivity(MainActivity.this, DeviceActivity.class);
            Toast.makeText(ctx, "当前未连接指定wifi", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void showPayResult(List<PayInfoResult> payResult) {
        disdialog();
        if (payResult.size() > 0) {
            IntentUtils.entryActivity(MainActivity.this, PaymentActivity.class);
        }
    }

    @Override
    public void showPayFailedResult(String errMsg) {
        disdialog();
        Toast.makeText(ctx, errMsg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showNoPayData(int errCode, String errMsg) {
        disdialog();
        t.schedule(new TimerTask() {
            public void run() {
                loadingDialog.dismiss();
                IntentUtils.entryActivity(MainActivity.this, PaymentActivity.class);
            }
        }, 2000);
    }
}
