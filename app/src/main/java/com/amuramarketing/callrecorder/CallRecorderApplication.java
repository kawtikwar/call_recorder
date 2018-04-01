package com.amuramarketing.callrecorder;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import com.amuramarketing.callrecorder.database.DatabaseManager;
import com.amuramarketing.callrecorder.database.SQLiteHelper;
import com.facebook.stetho.Stetho;

public class CallRecorderApplication extends Application {

    private static CallRecorderApplication instance = new CallRecorderApplication();

    /**
     * Current activity in the top stack.
     */
    private Activity currentActivity;

    @Override
    public void onCreate() {
        super.onCreate();

        DatabaseManager.initializeInstance(new SQLiteHelper(this));
        Stetho.initializeWithDefaults(this);
        registerActivityLifecycleCallbacks(new MyLifecycleHandler());
    }

    public CallRecorderApplication() {
        super();
        instance = this;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);

    }
}
