package com.dilusense.faceplaydemo.presenter;

import android.content.Context;

import com.dilusense.faceplaydemo.App;
import com.dilusense.faceplaydemo.databindings.SharedPrefUtility;
import com.dilusense.faceplaydemo.net.utils.MultipartUtils;
import com.dilusense.faceplaydemo.network.response.BaseResponse;
import com.dilusense.faceplaydemo.network.result.PayInfoResult;
import com.dilusense.faceplaydemo.utils.MyConstants;
import com.dilusense.faceplaydemo.utils.ResponseCodeUtils;
import com.dilusense.faceplaydemo.view.PayResultView;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PayPresenter {
    private PayResultView mPayResultView;

    public PayPresenter(PayResultView view){
        mPayResultView = view;

    }
    public void payMoney(Context context, String money){
        String wifiInfo = (String) SharedPrefUtility.getParam(context, SharedPrefUtility.WIFI_INFO, "");
        Gson gson = new Gson();
        JsonObject jObj = new JsonObject();
        jObj.addProperty("wifi",wifiInfo);
        jObj.addProperty("pay_monery",money);
        RequestBody bodyjson = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), gson.toJson(jObj));
        Call<BaseResponse<List<PayInfoResult>>> call = App.getRestClient()
                .getCompareService() .payResult(MultipartUtils.createPartFromString(wifiInfo),MultipartUtils.createPartFromString(money));
        call.enqueue(new Callback<BaseResponse<List<PayInfoResult>>>() {

            @Override
            public void onResponse(Call<BaseResponse<List<PayInfoResult>>> call, Response<BaseResponse<List<PayInfoResult>>> response) {
                List<PayInfoResult> Compare1vnResult = response.body().getData();

                if(response.body().getCode() == 0 && Compare1vnResult != null) {
                    mPayResultView.showPayResult(Compare1vnResult);
                }  else{
                    mPayResultView.showNoPayData(response.body().getCode(), ResponseCodeUtils.convertMsg(response.body()));
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<List<PayInfoResult>>> call, Throwable t) {
                   mPayResultView.showPayFailedResult(MyConstants.MSG_NET_REQUEST_UNKNOWN);
            }
        });
    }
}
