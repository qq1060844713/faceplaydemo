package com.dilusense.faceplaydemo.network.response;

/**
 * Created by Thinkpad on 2017/3/3.
 */
public class BaseResponse<T> extends SimpleResponse{
    private T data;

    public T getData() {

        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
