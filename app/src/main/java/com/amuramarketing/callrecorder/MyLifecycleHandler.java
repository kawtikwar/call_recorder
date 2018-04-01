package com.amuramarketing.callrecorder;

import android.app.Activity;
import android.app.Application.ActivityLifecycleCallbacks;
import android.os.Bundle;

import com.amuramarketing.callrecorder.preferences.PreferenceHelper;

public class MyLifecycleHandler implements ActivityLifecycleCallbacks {


    private Activity activity;


    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        this.activity = activity;
    }

    @Override
    public void onActivityStarted(Activity activity) {
        // TODO Auto-generated method stub
        this.activity = activity;

    }

    @Override
    public void onActivityResumed(Activity activity) {
        // TODO Auto-generated method stub
        this.activity = activity;
        PreferenceHelper.context = activity;
    }


    @Override
    public void onActivityPaused(Activity activity) {
        // TODO Auto-generated method stub
        this.activity = activity;

    }

    @Override
    public void onActivityStopped(Activity activity) {
        // TODO Auto-generated method stub
        this.activity = activity;
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
        // TODO Auto-generated method stub
        this.activity = activity;
    }


    @Override
    public void onActivityDestroyed(Activity activity) {
        // TODO Auto-generated method stub
        this.activity = activity;
    }
}
