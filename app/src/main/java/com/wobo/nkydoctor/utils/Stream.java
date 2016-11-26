package com.wobo.nkydoctor.utils;

import android.text.TextUtils;
import android.util.Log;

import com.wobo.nkydoctor.Config;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by xuchun on 2016/10/29.
 */
public class Stream {

    private static final String TAG = "Stream";

    public static JSONObject getRemoteData(JSONObject paramsJson) throws Exception {
        URL url = new URL(Config.URL + paramsJson.toString());
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setConnectTimeout(Config.CONNECT_TIMEOUT);
        connection.setReadTimeout(Config.READ_TIMEOUT);

        if (connection.getResponseCode() == 200) {
            String result = readInputStream(connection.getInputStream());
            if (TextUtils.isEmpty(result)) {
                return null;
            }
            return new JSONObject(result);
        }
        return null;
    }

    public static JSONObject makeJsonParam(String type,String[] names,Object[] values) throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("type",type);
        for(int i = 0;i < names.length;i ++) {
            jsonObject.put(names[i],values[i]);
        }
        return jsonObject;
    }


    public static String readInputStream(InputStream inputStream) throws IOException {
        if (inputStream == null) {
            return "";
        }

        BufferedInputStream bufferedInputStream = null;
        ByteArrayOutputStream byteArrayOutputStream = null;

        try {
            bufferedInputStream = new BufferedInputStream(inputStream);
            byteArrayOutputStream = new ByteArrayOutputStream();

            byte[] buffer = new byte[1024];
            int available = 0;
            while ((available = bufferedInputStream.read(buffer)) >= 0) {
                byteArrayOutputStream.write(buffer,0,available);
            }
            String result = byteArrayOutputStream.toString();
            Log.d(TAG,"result：" + result);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bufferedInputStream != null) {
                bufferedInputStream.close();
            }
        }
        return "";
    }
}