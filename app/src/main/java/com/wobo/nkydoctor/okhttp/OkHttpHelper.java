package com.wobo.nkydoctor.okhttp;

import com.wobo.nkydoctor.utils.LogUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by xuchun on 2016/8/17.
 */
public class OkHttpHelper {

    private static final String TAG = "OkHttpHelper";

    private static final OkHttpClient mOkHttpClient = new OkHttpClient.Builder().connectTimeout(60, TimeUnit.SECONDS).readTimeout(60,TimeUnit.SECONDS).writeTimeout(120,TimeUnit.SECONDS).build();

//    private static final String URL = "http://192.168.110.23:8080/nkyapi/mobile/interface.do?content=";

//    private static final String URL = "http://192.168.5.100:8080/nkyapi/mobile/interface.do?content=";

    //外网
//    private static final String URL = "http://114.55.228.245:84/nkyapi/mobile/interface.do?content=";

    private static final String URL = "http://api.nbrobo.com/mobile/interface.do?content=";
    /**
     * get请求
     * @param params
     * @param callback
     * @return
     */
    public static Call get(JSONObject params,Callback callback) {
        String requestUrl = URL + params.toString();
        Call call1 = mOkHttpClient.newCall(new Request.Builder().url(requestUrl).build());
        call1.enqueue(callback);
        LogUtil.d(TAG, "request：" + call1.request().toString());
        return call1;
    }

    /**
     *
     * @param url
     * @param callback
     * @return
     */
    public static Call download(String url,Callback callback) {
        Call call = mOkHttpClient.newCall(new Request.Builder().url(url).build());
        call.enqueue(callback);
        LogUtil.d(TAG, "request：" + call.request().toString());
        return call;
    }

    /**
     * 构造json参数
     * @param names
     * @param values
     * @return
     * @throws JSONException
     */
    public static JSONObject makeJsonParams(String type,String[] names,Object[] values) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("type",type);
            for(int i = 0;i < names.length;i ++) {
                LogUtil.d(TAG,"name：" + names[i] + "  value：" + values[i]);
                jsonObject.put(names[i],values[i]);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtil.d(TAG,"params：" + jsonObject.toString());
        return jsonObject;
    }
}
