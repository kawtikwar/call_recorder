package com.amuramarketing.callrecorder.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.provider.CallLog;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import com.amuramarketing.callrecorder.Utils.DateUtil;
import com.amuramarketing.callrecorder.database.DatabaseUtils;
import com.amuramarketing.callrecorder.models.CallDetailsModel;
import com.amuramarketing.callrecorder.preferences.PreferenceHelper;
import com.amuramarketing.callrecorder.services.RecorderIntendService;

import static com.amuramarketing.callrecorder.services.RecorderIntendService.filePath;

public class PhonecallReceiver extends BroadcastReceiver {

    //The receiver will be recreated whenever android feels like it.  We need a static variable to remember data between instantiations
    protected static final String TAG = PhonecallReceiver.class.getName();
    private Context mContext;
    private static int lastState = TelephonyManager.CALL_STATE_IDLE;
    private static long callStartTime;
    private static boolean isIncoming;
    private static String savedNumber;  //because the passed incoming is only valid in ringing
    private static String mTenDigitNumber;
    private static String callType = "";
    private static String callStatus = PreferenceHelper.CALL_STATUS_MISSED;


    @Override
    public void onReceive(Context context, Intent intent) {
        this.mContext = context;

        detectSim(intent);

        PreferenceHelper.context = context;

        if (intent.getAction().equals("android.intent.action.NEW_OUTGOING_CALL")) {
            savedNumber = intent.getExtras().getString("android.intent.extra.PHONE_NUMBER");

            if (savedNumber != null && savedNumber.length() > 10 && savedNumber.startsWith("+91")) {
                mTenDigitNumber = savedNumber.substring(3, savedNumber.length());

            } else {
                mTenDigitNumber = savedNumber;
            }


        } else {
            String stateStr = intent.getExtras().getString(TelephonyManager.EXTRA_STATE);
            String number = intent.getExtras().getString(TelephonyManager.EXTRA_INCOMING_NUMBER);
            int state = 0;
            if (stateStr.equals(TelephonyManager.EXTRA_STATE_IDLE)) {
                state = TelephonyManager.CALL_STATE_IDLE;
            } else if (stateStr.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)) {
                state = TelephonyManager.CALL_STATE_OFFHOOK;
            } else if (stateStr.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
                state = TelephonyManager.CALL_STATE_RINGING;
            }


            onCallStateChanged(context, state, number);


        }


    }


    private void detectSim(Intent intent) {
        try {
            String callingSIM = "";
            Bundle bundle = intent.getExtras();
            callingSIM = String.valueOf(bundle.getInt("simId", -1));
            if (callingSIM == "0") {
                // Incoming call from SIM1
                Toast.makeText(mContext, "SIM 1", Toast.LENGTH_SHORT).show();
            } else if (callingSIM == "1") {
                // Incoming call from SIM2
                Toast.makeText(mContext, "SIM 1", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Derived classes should override these to respond to specific events of interest
    protected void onIncomingCallStarted(Context ctx, String number, long start) {
        callType = PreferenceHelper.CALL_TYPE_INCOMING;
        startRecording(ctx);
    }

    protected void onOutgoingCallStarted(Context ctx, String number, long start) {
        callType = PreferenceHelper.CALL_TYPE_OUTGOING;
        startRecording(ctx);
    }

    protected void onIncomingCallEnded(Context ctx, String number, String callType, long start, long end) {
        RecorderIntendService.stopRecording();
        stopRecording(ctx);
        Log.e("onIncomingCallEnded", "called");
        insertRecord(ctx, callType, number, filePath, start, end);

    }

    protected void onOutgoingCallEnded(Context ctx, String number, String callType, long start, long end) {

        RecorderIntendService.stopRecording();
        stopRecording(ctx);
        insertRecord(ctx, callType, number, filePath, start, end);
    }

    protected void onMissedCall(Context ctx, String number, String callType, long start) {
        RecorderIntendService.stopRecording();
        stopRecording(ctx);
        insertRecord(ctx, callType, number, filePath, start, start);

    }

    private void startRecording(Context context) {
        Intent intent = new Intent(context, RecorderIntendService.class);
        //intent.putExtra("receiver", mReceiver);
        //intent.putExtra("media_path", uri);
        // intent.putExtra("position", position);
        //intent.putExtra("message_details", message);
        context.startService(intent);
    }

    private void stopRecording(Context context) {
        Intent intent = new Intent(context, RecorderIntendService.class);
        context.stopService(intent);
    }


    private void insertRecord(final Context ctx, final String mtype, final String number,
                              final String mFilePath,
                              final long start, final long end) {

        Intent sendData = new Intent(PreferenceHelper.CALL_LOG_RECEIVER);

        sendData.putExtra(PreferenceHelper.EXTRA_PARAMS_RESULT_KEY, "END_CALL");
        ctx.sendBroadcast(sendData);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                Cursor managedCursor = ctx.getContentResolver().query(CallLog.Calls.CONTENT_URI, null, null, null, CallLog.Calls.DATE + " DESC limit 1;");
                //int mnumber = managedCursor.getColumnIndex(CallLog.Calls.NUMBER);
                // int type = managedCursor.getColumnIndex( CallLog.Calls.TYPE );
                // int date = managedCursor.getColumnIndex(CallLog.Calls.DATE);
                //int duration = managedCursor.getColumnIndex(CallLog.Calls.DURATION);

                String callDuration = "0";
                while (managedCursor.moveToNext()) {
                    //String phNumber = managedCursor.getString(mnumber);
                    // String callType = managedCursor.getString(type);
                    //String callDate = managedCursor.getString(date);
                    //Date callDayTime = new Date(Long.valueOf(callDate));
                    callDuration = managedCursor.getString(managedCursor.getColumnIndex(CallLog.Calls.DURATION));


                }
                managedCursor.close();

                CallDetailsModel callDetailsModel = new CallDetailsModel();
                callDetailsModel.setMobileNo(number);
                callDetailsModel.setCallType(mtype);
                if (Long.parseLong(callDuration) > 0) {
                    callDetailsModel.setCallStatus(PreferenceHelper.CALL_STATUS_SPOKE);

                } else {
                    callDetailsModel.setCallStatus(PreferenceHelper.CALL_STATUS_MISSED);

                }

                callDetailsModel.setStartTime("" + start);
                callDetailsModel.setEndTime("" + end);
                callDetailsModel.setDate(DateUtil.getDateString(start));
                callDetailsModel.setDuration("" + callDuration);
                callDetailsModel.setFilePath(mFilePath);

                boolean status = DatabaseUtils.insertCallDetails(ctx, callDetailsModel);

     /*           if (status) {

                    Intent sendData = new Intent(PreferenceHelper.CALL_LOG_RECEIVER);

                    sendData.putExtra(PreferenceHelper.EXTRA_PARAMS_RESULT_KEY, "updates");
                    ctx.sendBroadcast(sendData);

                    //webservice call
                    CallDetailsModel callDetailsToPost = new CallDetailsModel();

                    callDetailsToPost.setCallStatus(callDetailsModel.getCallStatus());
                    callDetailsToPost.setCallType(callDetailsModel.getCallType());
                    callDetailsToPost.setDate(callDetailsModel.getDate());
                    callDetailsToPost.setDuration(callDetailsModel.getDuration());
                    callDetailsToPost.setStartTime(callDetailsModel.getStartTime());
                    callDetailsToPost.setEndTime(callDetailsModel.getEndTime());

                    File file = new File(filePath);

                    String fileName = number + "_" + System.currentTimeMillis() + "_" + file.getName();

                    callDetailsToPost.setFilePath(fileName);

                    List<MultipartBody.Part> files = new ArrayList<MultipartBody.Part>();
                    List<CallDetailsModel> datas = new ArrayList<CallDetailsModel>();

                    RequestBody reqFile = RequestBody.create(MediaType.parse("audio"), file);
                    MultipartBody.Part body = MultipartBody.Part.createFormData("images[]",
                            fileName, reqFile);

                    files.add(body);
                    datas.add(callDetailsToPost);

                    final CallList callList = new CallList();
                    callList.setCallList(datas);


                    APIUtils.getAPIInterface().postCallDetail(files, callList).enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            Log.e("response", "" + response.message());
                            if (response!=null && response.message()!=null) {
                                try {
                                    Gson gson = new Gson();
                                    CallList responseCallList = gson.fromJson(response.message(), CallList.class);
                                    if (responseCallList != null && responseCallList.getCallList() != null && !responseCallList.getCallList().isEmpty()) {
                                        for (int i = 0; i < responseCallList.getCallList().size(); i++) {
                                            responseCallList.getCallList().get(i).setIs_sync(1);// set sync flag true
                                            DatabaseUtils.insertCallDetails(mContext, responseCallList.getCallList().get(i));
                                        }
                                    }
                                } catch (JsonSyntaxException e) {
                                    e.printStackTrace();
                                }catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        @Override


                        public void onFailure(Call<ResponseBody> call, Throwable t) {

                            Log.e("onFailure", "" + t.getCause());
                        }
                    });
                }
*/

            }
        }, 3000);

    }


    //Deals with actual events

    //Incoming call-  goes from IDLE to RINGING when it rings, to OFFHOOK when it's answered, to IDLE when its hung up
    //Outgoing call-  goes from IDLE to OFFHOOK when it dials out, to IDLE when hung up
    public void onCallStateChanged(Context context, int state, String number) {
        if (lastState == state) {
            //No change, debounce extras
            return;
        }
        switch (state) {
            case TelephonyManager.CALL_STATE_RINGING:
                isIncoming = true;
                callStartTime = System.currentTimeMillis();
                if (number != null && number.length() > 10 && number.startsWith("+91")) {

                    mTenDigitNumber = number.substring(3, number.length());

                } else {
                    mTenDigitNumber = number;
                }
                makeToast("IncomingCallStarted");
                onIncomingCallStarted(context, mTenDigitNumber, callStartTime);
                break;
            case TelephonyManager.CALL_STATE_OFFHOOK:
                //Transition of ringing->offhook are pickups of incoming calls.  Nothing done on them
                if (lastState != TelephonyManager.CALL_STATE_RINGING) {
                    isIncoming = false;
                    callStartTime = System.currentTimeMillis();
                    makeToast("OutgoingCallStarted");
                    onOutgoingCallStarted(context, mTenDigitNumber, callStartTime);
                } else {
                    lastState = TelephonyManager.CALL_STATE_OFFHOOK;
                }
                break;
            case TelephonyManager.CALL_STATE_IDLE:
                //Went to idle-  this is the end of a call.  What type depends on previous state(s)
                if (lastState == TelephonyManager.CALL_STATE_RINGING) {
                    //Ring but no pickup-  a miss
                    makeToast("MissedCall");
                    onMissedCall(context, mTenDigitNumber, callType, callStartTime);
                } else if (isIncoming) {
                    //rejected
                    makeToast("IncomingCallEnded");
                    onIncomingCallEnded(context, mTenDigitNumber, callType, callStartTime, System.currentTimeMillis());
                } else {
                    makeToast("OutgoingCallEnded");
                    onOutgoingCallEnded(context, mTenDigitNumber, callType, callStartTime, System.currentTimeMillis());
                }
                break;
        }
        lastState = state;
    }

    private void makeToast(String message) {
//        Toast.makeText(mContext, "" + message, Toast.LENGTH_SHORT).show();
        Log.i(message, message);
    }


}