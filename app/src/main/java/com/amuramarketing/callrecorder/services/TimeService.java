package com.amuramarketing.callrecorder.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.amuramarketing.callrecorder.receivers.Alarm;

/**
 * Created by pooja on 7/24/2017.
 */

public class TimeService extends Service {
    Alarm alarm = new Alarm();

    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        alarm.setAlarm(this);
        return START_STICKY;
    }

    @Override
    public void onStart(Intent intent, int startId) {
        alarm.setAlarm(this);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}