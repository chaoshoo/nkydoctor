package com.wobo.nkydoctor.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.wobo.nkydoctor.bean.Doctor;

/**
 * Created by xuchun on 2016/10/30.
 */
public class Storage {

    private static Storage mShared;

    public static synchronized Storage getInstance() {
        if (mShared == null) {
            mShared = new Storage();
        }
        return mShared;
    }

    //保存数据
    public void save(Context context,String name,String key,String value) {
        context.getSharedPreferences(name,0).edit().putString(key,value).commit();
    }

    /**
     * 得到登录的vip
     * @param context
     * @return
     */
    public Doctor getLocalDoctor(Context context) {
        SharedPreferences sp = context.getSharedPreferences("login", 0);
        String doctor = sp.getString("doctor",null);
        if (doctor == null) {
            return null;
        }
        return JsonUtil.mGson.fromJson(doctor,Doctor.class);
    }



    /**
     * 得到登录的账号
     * @param context
     * @return
     */
    public String getAccountStr(Context context) {
        SharedPreferences sp = context.getSharedPreferences("login", 0);
        return sp.getString("account", null);
    }

    /**
     * 得到登录的密码
     * @param context
     * @return
     */
    public String getPasswordStr(Context context) {
        SharedPreferences sp = context.getSharedPreferences("login", 0);
        return sp.getString("password",null);
    }
}