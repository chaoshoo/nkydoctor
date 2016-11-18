package com.wobo.nkydoctor;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.netease.nrtc.sdk.NRtc;
import com.wobo.nkydoctor.utils.Storage;
import com.wobo.nkydoctor.utils.Stream;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xuchun on 2016/10/29.
 */
public class LoginActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CODE = 10;

    private Handler mHandler = new Handler();

    private ProgressDialog mProgressDialog;
    private EditText mAccount,mPassword;

    private String mAccountStr;
    private String mPasswordStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.universal);


        mProgressDialog = new ProgressDialog(this);
        ((FrameLayout) findViewById(R.id.universal_content)).addView(LayoutInflater.from(this).inflate(R.layout.activity_login, null));
        mAccount = (EditText) findViewById(R.id.login_account);
        mPassword = (EditText) findViewById(R.id.login_password);
        findViewById(R.id.login_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                check();
            }
        });
        String account = Storage.getInstance().getAccountStr(this);
        String password = Storage.getInstance().getPasswordStr(this);
        if (account != null) {
            mAccount.setText(account);
        }
        if (password != null) {
            mPassword.setText(password);
        }
    }

   @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (NRtc.checkPermission(this).size() == 0) {
                    login(mAccountStr,mPasswordStr);
                } else {
                    Toast.makeText(LoginActivity.this, "有些权限没有打开", Toast.LENGTH_SHORT)
                            .show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }

    }

    private void checkPermissions() {
        final List<String> missed = NRtc.checkPermission(this);
        if (missed.size() != 0) {
            List<String> showRationale = new ArrayList<>();
            for (String permission : missed) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
                    showRationale.add(permission);
                }
            }

            if (showRationale.size() > 0) {
                new AlertDialog.Builder(LoginActivity.this)
                        .setMessage("您必须同意此项权限的使用")
                        .setPositiveButton("同意", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions(LoginActivity.this, missed.toArray(new String[missed.size()]),
                                        PERMISSION_REQUEST_CODE);
                            }
                        })
                        .setNegativeButton("不同意", null)
                        .create()
                        .show();
            } else {
                ActivityCompat.requestPermissions(this, missed.toArray(new String[missed.size()]), PERMISSION_REQUEST_CODE);
            }
            return;
        }
        login(mAccountStr,mPasswordStr);

    }

    private void check() {
        mAccountStr = mAccount.getText().toString();
        if (TextUtils.isEmpty(mAccountStr)) {
            mAccount.setError("请输入手机号");
            return;
        }

        mPasswordStr = mPassword.getText().toString();
        if (TextUtils.isEmpty(mPasswordStr)) {
            mPassword.setError("请输入密码");
            return;
        }
        checkPermissions();

    }

    private void login(final String account, final String password) {
        mProgressDialog.setMessage("正在登录");
        mProgressDialog.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    final JSONObject resultJson = Stream.getRemoteData(Stream.makeJsonParam("doctorlogin",
                            new String[]{"tel", "password", "android_tv_channel_id"},
                            new Object[]{account, password, ""}));
                    if ("1".equals(resultJson.getString("code"))) {
                        final JSONObject doctor = resultJson.getJSONObject("doctor_info");
                        Storage.getInstance().save(LoginActivity.this,"login","doctor",doctor.toString());
                        Storage.getInstance().save(LoginActivity.this, "login", "account", account);
                        Storage.getInstance().save(LoginActivity.this, "login", "password", password);
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                mProgressDialog.dismiss();
                                RemoteActivity.launch(LoginActivity.this);
                                finish();
                            }
                        });
                        return;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        loginError();
                    }
                });
            }
        }).start();
    }

    private void loginError() {
        if (!isFinishing())
            mProgressDialog.dismiss();
        Toast.makeText(this, "登录失败", Toast.LENGTH_SHORT).show();
    }
}
