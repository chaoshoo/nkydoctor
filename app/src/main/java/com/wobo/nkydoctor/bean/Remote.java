package com.wobo.nkydoctor.bean;

import java.io.Serializable;

/**
 * 远程咨询(远程预约诊断)
 * Created by xuchun on 2016/8/26.
 */
public class Remote implements Serializable {

    private String id;
    private String code;
    private String vip_code;
    private String doctor_code;
    private String hospital_code;
    private String order_time;
    private String affirm_time;
    private String iszd;
    private String isdeal;
    private String zd_begin_time;
    private String zd_end_Time;
    private String create_time;
    private String remark;
    private String real_name;

    //医生名字
    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getVip_code() {
        return vip_code;
    }

    public void setVip_code(String vip_code) {
        this.vip_code = vip_code;
    }

    public String getDoctor_code() {
        return doctor_code;
    }

    public void setDoctor_code(String doctor_code) {
        this.doctor_code = doctor_code;
    }

    public String getHospital_code() {
        return hospital_code;
    }

    public void setHospital_code(String hospital_code) {
        this.hospital_code = hospital_code;
    }

    public String getOrder_time() {
        return order_time;
    }

    public void setOrder_time(String order_time) {
        this.order_time = order_time;
    }

    public String getAffirm_time() {
        return affirm_time;
    }

    public void setAffirm_time(String affirm_time) {
        this.affirm_time = affirm_time;
    }

    public String getIszd() {
        return iszd;
    }

    public void setIszd(String iszd) {
        this.iszd = iszd;
    }

    public String getIsdeal() {
        return isdeal;
    }

    public void setIsdeal(String isdeal) {
        this.isdeal = isdeal;
    }

    public String getZd_begin_time() {
        return zd_begin_time;
    }

    public void setZd_begin_time(String zd_begin_time) {
        this.zd_begin_time = zd_begin_time;
    }

    public String getZd_end_Time() {
        return zd_end_Time;
    }

    public void setZd_end_Time(String zd_end_Time) {
        this.zd_end_Time = zd_end_Time;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getReal_name() {
        return real_name;
    }

    public void setReal_name(String real_name) {
        this.real_name = real_name;
    }
}
