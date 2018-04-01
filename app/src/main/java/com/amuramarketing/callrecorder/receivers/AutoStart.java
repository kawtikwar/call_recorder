package com.amuramarketing.callrecorder.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AutoStart extends BroadcastReceiver {
    Alarm alarm = new Alarm();

    @Override
    public void onReceive(Context context, Intent intent) {
//        Toast.makeText(context, "", Toast.LENGTH_SHORT).show();
        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            alarm.setAlarm(context);
        }
    }
}