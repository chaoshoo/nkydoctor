package com.wobo.nkydoctor;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.open.androidtvwidget.bridge.EffectNoDrawBridge;
import com.open.androidtvwidget.leanback.recycle.GridLayoutManagerTV;
import com.open.androidtvwidget.leanback.recycle.RecyclerViewTV;
import com.open.androidtvwidget.view.MainUpView;
import com.wobo.nkydoctor.adapter.RemoteAdapter;
import com.wobo.nkydoctor.bean.Doctor;
import com.wobo.nkydoctor.bean.Remote;
import com.wobo.nkydoctor.nrtc.MultiChatActivity;
import com.wobo.nkydoctor.utils.Storage;
import com.wobo.nkydoctor.utils.Stream;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by xuchun on 2016/10/29.
 */
public class RemoteActivity extends AppCompatActivity {

    private Handler mHandler = new Handler();
    private ProgressDialog mProgressDialog;

//    private Button mUpBtn,mNextBtn,mBackBtn;
    private RecyclerViewTV mRecyclerViewTV;
    private MainUpView mMainUpView;
    private RemoteAdapter mRemoteAdapter;
    private List<Remote> mRemoteList = new ArrayList<Remote>();

    private Doctor mDoctor;
    private Remote mSelectRemote;
    private String mChannelName;
    private String mToken;
    private String mPushUrl;
    private String mCid;

    public static void launch(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, RemoteActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.universal);
        mDoctor = Storage.getInstance().getLocalDoctor(this);

        ((FrameLayout) findViewById(R.id.universal_content)).addView(LayoutInflater.from(this).inflate(R.layout.activity_remote, null));
        mMainUpView = (MainUpView) findViewById(R.id.remote_upview);
        mMainUpView.setEffectBridge(new EffectNoDrawBridge());
        EffectNoDrawBridge effectNoDrawBridge = (EffectNoDrawBridge) mMainUpView.getEffectBridge();
        effectNoDrawBridge.setTranDurAnimTime(200);
        mMainUpView.setUpRectResource(R.drawable.test_rectangle); // 设置移动边框的图片.
        mMainUpView.setShadowResource(R.drawable.item_shadow); // 设置移动边框的阴影.

        mRecyclerViewTV = (RecyclerViewTV) findViewById(R.id.remote_rv);
        GridLayoutManagerTV gridLayoutManagerTV = new GridLayoutManagerTV(this,4);
        gridLayoutManagerTV.setOrientation(GridLayoutManagerTV.VERTICAL);
        mRecyclerViewTV.setLayoutManager(gridLayoutManagerTV);
        mRecyclerViewTV.setAdapter(mRemoteAdapter = new RemoteAdapter(mRemoteList));
        mRecyclerViewTV.setOnItemListener(new RecyclerViewTV.OnItemListener() {
            @Override
            public void onItemPreSelected(RecyclerViewTV parent, View itemView, int position) {
                mMainUpView.setUnFocusView(itemView);
            }

            @Override
            public void onItemSelected(RecyclerViewTV parent, View itemView, int position) {
                mMainUpView.setFocusView(itemView, 1.0f);
            }

            @Override
            public void onReviseFocusFollow(RecyclerViewTV parent, View itemView, int position) {
                mMainUpView.setFocusView(itemView, 1.0f);
            }
        });
        mRecyclerViewTV.setOnItemClickListener(new RecyclerViewTV.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerViewTV parent, View itemView, int position) {
//                Toast.makeText(RemoteActivity.this,"点我干嘛",Toast.LENGTH_SHORT).show();
                mSelectRemote = mRemoteList.get(position);
                if ("2".equals(mSelectRemote.getIszd())) {
                    Toast.makeText(RemoteActivity.this, "This request has been rejected.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if ("2".equals(mSelectRemote.getIsdeal())) {
                    Toast.makeText(RemoteActivity.this, "This request has been rejected.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if ("1".equals(mSelectRemote.getIsdeal())) {
                    Toast.makeText(RemoteActivity.this, "This video request has been processed", Toast.LENGTH_SHORT).show();
                    return;
                }
                getToken();
            }
        });

        mProgressDialog = new ProgressDialog(this);
        getRemoteList();
    }

    private void loadError() {
        mProgressDialog.dismiss();
        Toast.makeText(this,"Data loading failed",Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void getRemoteList() {
        mProgressDialog.setMessage("Retrieving data");
        mProgressDialog.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    final JSONObject resultJson = Stream.getRemoteData(Stream.makeJsonParam("doctorremotelist",
                            new String[]{"doctor_code"},
                            new Object[]{mDoctor.getCode()}));
                    if ("1".equals(resultJson.getString("code"))) {
                        Gson gson = new Gson();
                        Type type = new TypeToken<List<Remote>>() {}.getType();
                        List<Remote> tempList = gson.fromJson(resultJson.getString("remotelist"), type);
                        if (!tempList.isEmpty()) {
                            mRemoteList.clear();
                            mRemoteList.addAll(tempList);
                        }
                    }
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (!isFinishing()) {
                                mProgressDialog.dismiss();
                            }
                            if (mRemoteList.isEmpty()) {
                                Toast.makeText(RemoteActivity.this,"No remote consultation data",Toast.LENGTH_SHORT).show();
                                return;
                            }
                            mRemoteAdapter.notifyDataSetChanged();
                        }
                    });
                    return;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        loadError();
                    }
                });
            }
        }).start();
    }

    private void getToken() {
        mProgressDialog.setMessage("Connecting server");
        mProgressDialog.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject resultJson = Stream.getRemoteData(Stream.makeJsonParam("getvediotoken",
                            new String[]{"uid"},
                            new Object[]{mDoctor.getTel()}));
                    if ("1".equals(resultJson.getString("code"))) {
                        mToken = resultJson.getString("token");
                        createChannelName();
                        return;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mProgressDialog.dismiss();
                        Toast.makeText(RemoteActivity.this,"TokenAcquisition failed",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).start();
    }

    private void createChannelName() {
        try {
            JSONObject resultJson = Stream.getRemoteData(Stream.makeJsonParam("makechannelname",
                    new String[]{"tel","remote_inspect_id","dotorName"},
                    new Object[]{mDoctor.getTel(),mSelectRemote.getId(),mDoctor.getName()}));
            if ("1".equals(resultJson.getString("code"))) {
                mChannelName = resultJson.getString("channelname");
                mPushUrl = resultJson.getString("pushUrl");
                mCid = resultJson.getString("cid");
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mProgressDialog.dismiss();

                        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(RemoteActivity.this);
                        //打开互动直播模式
                        sharedPreferences.edit().putBoolean(getString(R.string.setting_live_status_key), true).commit();
                        //设置推流地址
                        sharedPreferences.edit().putString(getString(R.string.setting_live_url_key), mPushUrl).commit();
                        //清晰度
                        sharedPreferences.edit().putString(getString(R.string.setting_vie_quality_key), "3").commit();
                        //帧率
                        sharedPreferences.edit().putString(getString(R.string.setting_vie_frame_rate_key), "15").commit();
                        //去视频
                        MultiChatActivity.launch(RemoteActivity.this,
                                Long.valueOf(mDoctor.getTel()),
                                mChannelName,
                                mToken,
                                true,
                                true,
                                false,
                                mCid);
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
                mProgressDialog.dismiss();
                Toast.makeText(RemoteActivity.this,"Room creation failed",Toast.LENGTH_SHORT).show();
            }
        });
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//
//
//
//            }
//        }).start();
    }
}