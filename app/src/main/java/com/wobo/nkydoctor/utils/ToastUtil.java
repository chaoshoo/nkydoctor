package com.wobo.nkydoctor.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by xuchun on 2016/8/15.
 */
public class ToastUtil {

    private static Toast mToast;

    public static void show(Context context,String content) {
        if (mToast == null) {
            mToast = Toast.makeText(context,content,Toast.LENGTH_SHORT);
        } else {
            mToast.setText(content);
        }
        mToast.show();
    }

    /**
     * 释放创建Toast对象使用的Context
     */
    public static void destory() {
        if (mToast != null) {
            mToast = null;
        }
    }
}
