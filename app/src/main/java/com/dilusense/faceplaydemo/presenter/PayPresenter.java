package com.dilusense.faceplaydemo.presenter;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.dilusense.faceplaydemo.App;
import com.dilusense.faceplaydemo.databindings.SharedPrefUtility;
import com.dilusense.faceplaydemo.net.RestClient;
import com.dilusense.faceplaydemo.net.utils.MultipartUtils;
import com.dilusense.faceplaydemo.network.response.BaseResponse;
import com.dilusense.faceplaydemo.network.result.PassPerson;
import com.dilusense.faceplaydemo.network.result.PayInfoResult;
import com.dilusense.faceplaydemo.utils.HttpUtils;
import com.dilusense.faceplaydemo.utils.MyConstants;
import com.dilusense.faceplaydemo.utils.ResponseCodeUtils;
import com.dilusense.faceplaydemo.utils.RetrofitUtils;
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
//        Call<BaseResponse<List<PayInfoResult>>> call = App.getRestClient()
//                .getCompareService() .getPayResult();
//        call.enqueue(new Callback<BaseResponse<List<PayInfoResult>>>() {
//
//            @Override
//            public void onResponse(Call<BaseResponse<List<PayInfoResult>>> call, Response<BaseResponse<List<PayInfoResult>>> response) {
//                List<PayInfoResult> Compare1vnResult = response.body().getData();
//
//                if(response.body().getCode() == 0 && Compare1vnResult != null) {
//                    mPayResultView.showPayResult(Compare1vnResult);
//                }  else{
//                    mPayResultView.showNoPayData(response.body().getCode(), ResponseCodeUtils.convertMsg(response.body()));
//                }
//            }
//
//            @Override
//            public void onFailure(Call<BaseResponse<List<PayInfoResult>>> call, Throwable t) {
//                   mPayResultView.showPayFailedResult(MyConstants.MSG_NET_REQUEST_UNKNOWN);
//            }
//        });
//        HttpUtils.doGetAsyn(RestClient.payHTTPProtocol+App.getInstent().getPayServIP()+"/face_play/living/living", new HttpUtils.CallBack() {
//            @Override
//            public void onRequestComplete(String result) {
//                Gson gson = new Gson();
//                PassPerson passPerson = gson.fromJson(result, PassPerson.class);
//                if (passPerson !=null){
//                    mPayResultView.showPayResult(passPerson);
//                }
//            }
//
//            @Override
//            public void onRequestError(String result) {
//                mPayResultView.showPayFailedResult(result);
//            }
//        });

        RetrofitUtils.getUrls().getPayResult().enqueue(new Callback<PassPerson>() {
            @Override
            public void onResponse(Call<PassPerson> call, Response<PassPerson> response) {
                PassPerson body = response.body();
                if (body.getCode() == 0) {
                    if (body.getData() != null) {
                        mPayResultView.showPayResult(body);
                    } else {
                        mPayResultView.showNoPayData(0, "");
                    }
                }else {
                        mPayResultView.showErroePayResultData(body.getCode());
                }

            }

            @Override
            public void onFailure(Call<PassPerson> call, Throwable t) {
                mPayResultView.showPayFailedResult(t.getMessage());
            }
        });
    }

    public void payResult(int num) {
        HttpUtils.doGetAsyn(RestClient.payHTTPProtocol+App.getInstent().getPayServIP()+"/face_play/living/living?success="+num, new HttpUtils.CallBack() {
            @Override
            public void onRequestComplete(String result) {
                mPayResultView.showPayResultData(result);
            }

            @Override
            public void onRequestError(String result) {
                mPayResultView.showPayFailedResult(result);
            }
        });
    }
}
