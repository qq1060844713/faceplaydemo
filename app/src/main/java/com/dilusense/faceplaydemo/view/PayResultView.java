package com.dilusense.faceplaydemo.view;

import com.dilusense.faceplaydemo.network.result.PayInfoResult;

import java.util.List;

public interface PayResultView {
    void showPayResult(List<PayInfoResult> payResult);

    void showPayFailedResult(String errMsg);

    void showNoPayData(int errCode, String errMsg);
}
