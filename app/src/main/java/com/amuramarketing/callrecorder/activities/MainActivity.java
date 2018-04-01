package com.amuramarketing.callrecorder.activities;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.amuramarketing.callrecorder.R;
import com.amuramarketing.callrecorder.adapters.CallDetailsAdapter;
import com.amuramarketing.callrecorder.database.DatabaseUtils;
import com.amuramarketing.callrecorder.models.CallDetailsModel;
import com.amuramarketing.callrecorder.receivers.AutoStart;
import com.amuramarketing.callrecorder.services.TimeService;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView rvCallInfo;
    private TextView mTxtEmpty;
    private ProgressBar mProgressBar;
    private ArrayList<CallDetailsModel> callList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initUI();
        startService(new Intent(this, TimeService.class));
        registerBroadcastReceiver();
        registerBroadcastReceiverPhoneCall();
    }

    private void initUI() {

        mTxtEmpty = (TextView) findViewById(R.id.mTxtEmpty);
        mProgressBar = (ProgressBar) findViewById(R.id.mProgressBar);
        rvCallInfo = (RecyclerView) findViewById(R.id.rvCallInfo);
        mProgressBar.setVisibility(View.VISIBLE);

       callList = DatabaseUtils.getCallDetails(this);
        addContactNamesToList();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvCallInfo.setLayoutManager(linearLayoutManager);
        int resId = R.anim.layout_animation_fall_down;
        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(this, resId);
        rvCallInfo.setLayoutAnimation(animation);
        mProgressBar.setVisibility(View.GONE);


        if (null != callList && !callList.isEmpty()) {
            rvCallInfo.setVisibility(View.VISIBLE);
            mTxtEmpty.setVisibility(View.GONE);
            CallDetailsAdapter callDetailsAdapter = new CallDetailsAdapter(this, callList);
            rvCallInfo.setAdapter(callDetailsAdapter);
        } else {
            rvCallInfo.setVisibility(View.GONE);
            mTxtEmpty.setVisibility(View.VISIBLE);
        }

    }

    private void addContactNamesToList() {
        if (null != callList && !callList.isEmpty()) {
            for (int i = 0; i < callList.size(); i++) {
                String name = getContactName(callList.get(i).getMobileNo());
                callList.get(i).setName(name);
            }
        }
    }


    public String getContactName(final String phoneNumber) {
        Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phoneNumber));

        String[] projection = new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME};

        String contactName = phoneNumber;
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                contactName = cursor.getString(0);
            }
            cursor.close();
        }

        return contactName;
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
