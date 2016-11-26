package com.wobo.nkydoctor.utils;

import android.util.Log;

/**
 * Created by xuchun on 2016/8/15.
 */
public class LogUtil {

    private static boolean isDeBug = true;

    public static void d(String tag,String msg) {
        if (isDeBug) {
            Log.d(tag,msg);
        }
    }

}