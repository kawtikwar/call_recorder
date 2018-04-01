package com.amuramarketing.callrecorder.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.amuramarketing.callrecorder.preferences.PreferenceHelper;




public class APIUtils {

    private APIUtils() {

    }

    public static APIInterface getAPIInterface() {
            return APIClient.getClient(PreferenceHelper.SERVER_URL).create(APIInterface.class);
    }

    public static boolean isOnline(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
        return (netInfo != null && netInfo.isConnected());
    }
}
