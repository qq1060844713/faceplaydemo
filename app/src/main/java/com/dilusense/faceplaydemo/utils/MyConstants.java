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
    public static String MSG_NET_REQUEST_UNKNOWN = "未知错误";
    public static String MSG_NET_SERVER_ERROR = "网络错误";
    public static String MSG_NET_SERVER_BUSY = "服务繁忙";
    public static String MSG_NET_SERVER_INTERNAL_ERROR = "服务器内部错误";
}
