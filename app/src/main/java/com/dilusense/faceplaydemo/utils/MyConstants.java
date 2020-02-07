package com.dilusense.faceplaydemo.utils;

import java.util.regex.Pattern;

import android.os.Environment;

/**
 * Created by Thinkpad on 2019/12/10.
 */

public class MyConstants {

    public static String apkCheckUpdateUrl = "http://202.108.31.90:9001/apk/latest";
    public static int compareTimeout = 10; // 单位：s
    public static final int wifi = 1;
    public final static int HANDLER_FILE_COPY_FINISH = 8011;

    public static boolean isLockNessMonster(String s) {
        return Pattern.matches(".*(Dilusense).*", s);
    }

    public static boolean isEven(int num) {
        if (num % 2 == 0) {
            return true;
        } else {
            return false;
        }
    }

    public static String MSG_NET_REQUEST_UNKNOWN = "网络请求异常";
    public static String MSG_NET_SERVER_ERROR = "网络错误";
    public static String MSG_NET_SERVER_BUSY = "服务繁忙";
    public static String MSG_NET_SERVER_INTERNAL_ERROR = "服务器内部错误";

    public static String codeMsg(int code) {
        if (code == 2501) {
            return "授权错误";
        } else if (code == 2502) {
            return "连接异常";
        } else if (code == 2503) {
            return "wifi已连接";
        } else if (code == 2504) {
            return "连接失败";
        } else if (code == 2505) {
            return "未知错误";
        } else if (code == 2600) {
            return "发现wifi网络";
        } else if (code == -1) {
            return "未知错误";
        } else if (code == 10001) {
            return "参数错误";
        } else if (code == 10002) {
            return "刷脸终端正忙";
        }
        return "未知错误";
    }
}
