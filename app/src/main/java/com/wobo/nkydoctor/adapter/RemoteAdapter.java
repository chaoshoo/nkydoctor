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
        holder.doctor.setText("Reservation clilent：" + remote.getReal_name()+ "\nAppointment time：" + remote.getOrder_time());
        holder.remark.setText("Appointment notes：\n" + remote.getRemark());
        String isZd = remote.getIszd();
        if ("1".equals(isZd)) {
            isZd = "Answered";
        } else if ("2".equals(isZd)) {
            isZd = "Refused";
        } else {
            isZd = "Untreated";
        }
        String isDeal = remote.getIsdeal();
        if ("1".equals(isDeal)) {
            isDeal = "Chatted";
        } else if ("2".equals(isDeal)) {
            isDeal = "Refused";
        } else {
            isDeal = "Untreated";
        }
        holder.status.setText("Visit status：" + isZd + "\nVideo status：" + isDeal);
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