package com.amuramarketing.callrecorder.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class CallDetailsModel implements Serializable{

    private String id;

    @SerializedName("Mobile_No")
    private String mobileNo;

    @SerializedName("callType")
    private String callType;

    @SerializedName("callStatus")
    private String callStatus;

    @SerializedName("startTime")
    private String startTime;

    @SerializedName("endTime")
    private String endTime;

    @SerializedName("duration")
    private String duration;

    @SerializedName("filePath")
    private String filePath;

    @SerializedName("call_date")
    private String date;

    private String name;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @SerializedName("is_sync")
    private int is_sync;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getCallStatus() {
        return callStatus;
    }

    public void setCallStatus(String callStatus) {
        this.callStatus = callStatus;
    }

    public String getCallType() {
        return callType;
    }

    public void setCallType(String callType) {
        this.callType = callType;
    }

    public int getIs_sync() {
        return is_sync;
    }

    public void setIs_sync(int is_sync) {
        this.is_sync = is_sync;
    }
}
