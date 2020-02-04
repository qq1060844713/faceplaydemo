package com.dilusense.faceplaydemo.utils;


import com.dilusense.faceplaydemo.network.response.BaseResponse;

/**
 * Created by Thinkpad on 2017/3/20.
 */
public class ResponseCodeUtils {
    public static String convertMsg(BaseResponse response){
        String noticeMsg = response.getMsg();
        switch (response.getCode()) {
            case 10001:
                noticeMsg = MyConstants.MSG_NET_SERVER_ERROR;
                break;
            case 10003:
                noticeMsg = MyConstants.MSG_NET_SERVER_BUSY;
                break;
            case 10005:
                noticeMsg = MyConstants.MSG_NET_SERVER_INTERNAL_ERROR;
                break;
        }

        return noticeMsg;
    }
}
