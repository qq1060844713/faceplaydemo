package com.dilusense.faceplaydemo.view;

import com.dilusense.faceplaydemo.network.result.PassPerson;
import com.dilusense.faceplaydemo.network.result.PayInfoResult;

import java.util.List;

public interface PayResultView {
    void showPayResult(PassPerson payResult);

    void showPayFailedResult(String errMsg);

    void showNoPayData(int errCode, String errMsg);

    void showPayResultData(String errMsg);

    void showErroePayResultData(int errMsg);
}
