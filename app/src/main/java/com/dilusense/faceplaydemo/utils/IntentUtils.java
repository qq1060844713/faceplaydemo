package com.dilusense.faceplaydemo.utils;

import android.app.Activity;
import android.content.Intent;

/**
 * Created by Thinkpad on 2017/3/6.
 */
public class IntentUtils {

    public static void entryActivity(Activity ctx, Class cls){
        Intent intent = new Intent();
        intent.setClass(ctx, cls);
        ctx.startActivity(intent);
    }
}
