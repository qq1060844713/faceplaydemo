package com.dilusense.faceplaydemo.net.api;

import com.dilusense.faceplaydemo.network.response.BaseResponse;
import com.dilusense.faceplaydemo.network.result.PayInfoResult;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface PayAPI {
    @Multipart
    @POST("/face_payment/pay_result/result")
    Call<BaseResponse<List<PayInfoResult>>> payResult(
            @Part("wifi") RequestBody wifi,
            @Part("pay_money") RequestBody pay_money
    );
//    @Multipart
//    @POST("/face_payment/pay_result/result")
//    Call<BaseResponse<List<PayInfoResult>>> payResult(@Part("entity") RequestBody description);
}
