package com.amuramarketing.callrecorder.activities;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.amuramarketing.callrecorder.R;
import com.amuramarketing.callrecorder.receivers.AutoStart;
import com.amuramarketing.callrecorder.services.TimeService;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startService(new Intent(this, TimeService.class));
        registerBroadcastReceiver();
        registerBroadcastReceiverPhoneCall();
    }

    private void registerBroadcastReceiver() {
        try {
            ComponentName receiver = new ComponentName(this, AutoStart.class);
            PackageManager pm = getPackageManager();

            pm.setComponentEnabledSetting(receiver,
                    PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                    PackageManager.DONT_KILL_APP);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void registerBroadcastReceiverPhoneCall() {
        try {
            ComponentName receiver = new ComponentName(this, AutoStart.class);
            PackageManager pm = getPackageManager();

            pm.setComponentEnabledSetting(receiver,
                    PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                    PackageManager.DONT_KILL_APP);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void getMobileNumbers() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP_MR1) {
            SubscriptionManager subscriptionManager = SubscriptionManager.from(getApplicationContext());
            List<SubscriptionInfo> subsInfoList = subscriptionManager.getActiveSubscriptionInfoList();

            Log.d("Test", "Current list = " + subsInfoList);

            for (SubscriptionInfo subscriptionInfo : subsInfoList) {

                String number = subscriptionInfo.getNumber();

                Log.d("Test", " Number is  " + number);
            }
        }
    }

    public String getMobileNumber() {

        TelephonyManager telephonyManager = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);

        String strMobileNumber = telephonyManager.getLine1Number();

// Note : If the phone is dual sim, get the second number using :

       // telephonyManager.getLine2Number();

        return strMobileNumber;
    }
}
