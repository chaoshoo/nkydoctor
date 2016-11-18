package com.wobo.nkydoctor.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wobo.nkydoctor.R;
import com.wobo.nkydoctor.bean.Remote;

import java.util.List;

/**
 * 远程咨询适配器
 * Created by xuchun on 2016/9/21.
 */
public class RemoteAdapter extends RecyclerView.Adapter<RemoteAdapter.MyViewHolder> {

    private List<Remote> mRemoteList;

    public RemoteAdapter(List<Remote> remoteList) {
        this.mRemoteList = remoteList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from((parent.getContext())).inflate(R.layout.remote_item,parent,false));
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Remote remote = mRemoteList.get(position);
        holder.doctor.setText("预约用户：" + remote.getReal_name()+ "\n预约时间：" + remote.getOrder_time());
        holder.remark.setText("预约备注：\n" + remote.getRemark());
        String isZd = remote.getIszd();
        if ("1".equals(isZd)) {
            isZd = "已应答";
        } else if ("2".equals(isZd)) {
            isZd = "已拒绝";
        } else {
            isZd = "未处理";
        }
        String isDeal = remote.getIsdeal();
        if ("1".equals(isDeal)) {
            isDeal = "已视频";
        } else if ("2".equals(isDeal)) {
            isDeal = "已拒绝";
        } else {
            isDeal = "未处理";
        }
        holder.status.setText("应诊状态：" + isZd + "\n视频状态：" + isDeal);
    }

    @Override
    public int getItemCount() {
        return mRemoteList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView doctor,remark,status;

        public MyViewHolder(View itemView) {
            super(itemView);
            doctor = (TextView) itemView.findViewById(R.id.remote_item_doctor);
            remark = (TextView) itemView.findViewById(R.id.remote_item_remark);
            status = (TextView) itemView.findViewById(R.id.remote_item_status);
        }
    }
}
