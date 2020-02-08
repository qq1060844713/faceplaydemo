package com.dilusense.faceplaydemo.acticity;

import android.annotation.SuppressLint;
import android.net.wifi.ScanResult;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiInfo;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.badoo.mobile.util.WeakHandler;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.dilusense.faceplaydemo.MainActivity;
import com.dilusense.faceplaydemo.R;
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
import com.dilusense.faceplaydemo.view.GlideCircleTransform;
import com.dilusense.faceplaydemo.view.PassWordDialog;
import com.dilusense.faceplaydemo.view.PayResultView;
import com.dilusense.faceplaydemo.view.PwdEditText;
import com.hb.dialog.dialog.LoadingDialog;
import com.hb.dialog.myDialog.MyAlertDialog;
import com.jflavio1.wificonnector.WifiConnector;
import com.jflavio1.wificonnector.interfaces.ConnectionResultListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class PaymentActivity extends BaseTitleActivity implements PayResultView{
    @BindView(R.id.wifi_list1)
    LinearLayout wifi_list;
    @BindView(R.id.img_url)
    ImageView img_url;
    @BindView(R.id.person_check)
    TextView person_check;
    @BindView(R.id.pay_result_error)
    ImageView pay_result_error;
    @BindView(R.id.pay_ment_error)
    ImageView pay_ment_error;
    @BindView(R.id.pay_ment_right)
    ImageView pay_ment_right;
    @BindView(R.id.pay_result_right)
    ImageView pay_result_right;
    @BindView(R.id.show_pay)
    LinearLayout show_pay;
    @BindView(R.id.pay_password)
    LinearLayout pay_password;
    @BindView(R.id.show_pay_result)
    LinearLayout show_pay_result;
    @BindView(R.id.pay_ment_zhifu_error)
    LinearLayout pay_ment_zhifu_error;
    @BindView(R.id.pay_ment_zhifu_succeed)
    LinearLayout pay_ment_zhifu_succeed;
    @BindView(R.id.listview)
    ListView listview;
    @BindView(R.id.pay_ment)
    Button pay_ment;
    @BindView(R.id.password)
    PwdEditText password;
    @BindView(R.id.wifiRv)
    RecyclerView rv;
    WeakHandler handler;
    private PassPerson person;
    private PayPresenter payPresenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        handler = new WeakHandler();
        payPresenter = new PayPresenter(this);
    }

    private void getPersonData() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (person.getData().getData().getImg() != null) {
                    if (img_url != null) {
                        Glide.with(getApplicationContext())
                                .load(Base64.decode(person.getData().getData().getImg(), Base64.DEFAULT))
                                .centerCrop()
                                .transform(new GlideCircleTransform(ctx,2,ctx.getResources().getColor(R.color.white)))
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .error(R.mipmap.bg_photo).into(img_url);
                    } 
                    if (person.getData().getData().getIsLiving() == 1) {
                        pay_result_right.setVisibility(View.VISIBLE);
                        person_check.setText("活体检测成功");
                    } else {
                        pay_result_error.setVisibility(View.VISIBLE);
                        person_check.setText("活体检测失败");
                        pay_password.setVisibility(View.GONE);
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                show_pay.setVisibility(View.GONE);
                                show_pay_result.setVisibility(View.VISIBLE);
                                pay_result_error.setVisibility(View.VISIBLE);
                                pay_result_right.setVisibility(View.GONE);
                                pay_ment_error.setVisibility(View.VISIBLE);
                                pay_ment_zhifu_succeed.setVisibility(View.GONE);
                                pay_ment_zhifu_error.setVisibility(View.VISIBLE);
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        payPresenter.payResult(1);
                                        finish();
                                    }
                                }, 1000 * 2);
                            }
                        }, 1000 * 2);
                    }
                }
            }
        });
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        String wifiName = (String) SharedPrefUtility.getParam(ctx, SharedPrefUtility.WIFI_INFO, "");
        setMainTitle(0,"已连接" + "(" + wifiName + ")");
        person = (PassPerson) getIntent().getSerializableExtra("person_data");
        if (person != null) {
            getPersonData();
        }else {
            getNoResult();
        }
    }

    private void getNoResult() {
        Glide.with(getApplicationContext()).load(R.mipmap.bg_photo).centerCrop().diskCacheStrategy(DiskCacheStrategy.ALL).error(R.mipmap.bg_photo).into(img_url);
        pay_result_error.setVisibility(View.VISIBLE);
        person_check.setText("活体检测失败");
        pay_password.setVisibility(View.GONE);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                show_pay.setVisibility(View.GONE);
                show_pay_result.setVisibility(View.VISIBLE);
                pay_result_error.setVisibility(View.VISIBLE);
                pay_result_right.setVisibility(View.GONE);
                pay_ment_error.setVisibility(View.VISIBLE);
                pay_ment_zhifu_succeed.setVisibility(View.GONE);
                pay_ment_zhifu_error.setVisibility(View.VISIBLE);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        payPresenter.payResult(1);
                        finish();
                    }
                }, 1000 * 2);
            }
        }, 1000 * 2);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @OnClick(R.id.pay_ment)
    public void pay() {
        if (password.getText().toString().length() == 6) {
            show_pay.setVisibility(View.GONE);
            show_pay_result.setVisibility(View.VISIBLE);
            pay_ment_right.setVisibility(View.VISIBLE);
            pay_ment_error.setVisibility(View.GONE);
            pay_ment_zhifu_succeed.setVisibility(View.VISIBLE);
            pay_ment_zhifu_error.setVisibility(View.GONE);
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    payPresenter.payResult(0);
                    finish();
                }
            }, 1000 * 2);
        } else {
            toastMessage(101,"请输入六位数字");
        }
    }

    @Override
    public void showPayResult(PassPerson payResult) {

    }

    @Override
    public void showPayFailedResult(String errMsg) {

    }

    @Override
    public void showNoPayData(int errCode, String errMsg) {

    }

    @Override
    public void showPayResultData(String errMsg) {

    }

    @Override
    public void showErroePayResultData(int errMsg) {

    }
}
