package com.amuramarketing.callrecorder.models;

import java.io.Serializable;
import java.util.List;



public class CallList implements Serializable{

    public List<CallDetailsModel> getCallList() {
        return callList;
    }

    public void setCallList(List<CallDetailsModel> callList) {
        this.callList = callList;
    }

    private List<CallDetailsModel> callList;
}
