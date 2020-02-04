package com.dilusense.faceplaydemo.network.response;

/**
 * Created by Thinkpad on 2017/3/2.
 */
public class SimpleResponse{


    public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	private int code = -1;
    private String msg;

}
