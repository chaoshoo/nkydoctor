package com.wobo.nkydoctor.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by xuchun on 2016/8/15.
 */
public class NetworkUtil {

    //判断当前是否有网络
    public static boolean isHaveNet(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager == null) {
            return false;
        }
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo == null) {
            return false;
        }
        if (networkInfo.getState() == NetworkInfo.State.CONNECTED) {
            return true;
        }
        return false;
    }
}