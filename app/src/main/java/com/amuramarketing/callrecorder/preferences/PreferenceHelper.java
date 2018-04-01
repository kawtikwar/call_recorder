package com.amuramarketing.callrecorder.preferences;


import android.content.Context;


public class PreferenceHelper {

    final static String TAG = "PreferencesHelper";

    public static final String PREFERENCESNAME = "CALL_RECOREDER_APP";

    //preference keys
    public final static String KEY_IMEI = "device_id";
    public final static String KEY_LOGGED_IN = "logged_in";
    public static final String KEY_USER_DETAILS = "user_details";

    public static final String KEY_USER = "user";
    public static final String SERVER_URL = "http://testingurl.com/call_recording/";

    public static Context context;


    //URLS
    public final static String TESTING_URL = "services-uat2.avadh-tech.com/model.svc";
    public final static String APIHTTP_VAL = "http://";
    public final static String APISERVERHOST_VAL = TESTING_URL;
    public final static String GET_URL = TESTING_URL;


    // =============== API's ==================

    public final static String SERVICES_POST = "POST";
    public final static String SERVICES_GET = "GET";

    public static final String EXTRA_PARAMS_RESULT_KEY = "ExtraParams";

// =============== Profile ==================

    public static final String ACTION_CALL = "call";

    public static final String CALL_TYPE_INCOMING = "incoming";
    public static final String CALL_TYPE_OUTGOING = "outgoing";
    public static final String CALL_STATUS_MISSED = "missed";
    public static final String CALL_STATUS_SPOKE = "spoke";

    /* incoming but not spoked*/
    public static final String CALL_TYPE_MISSED = "missed";
    /* outgoing but not spoked*/
    public static final String CALL_TYPE_NOT_ATTENDED = "not_attended";

    public static final String CALL_LOG_RECEIVER = "call_log_receiver";

    public static final String REGISTRATION_TOKEN = "registration_token";
}