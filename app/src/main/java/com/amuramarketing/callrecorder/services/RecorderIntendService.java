package com.amuramarketing.callrecorder.services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.Environment;
import android.os.PowerManager;

import com.amuramarketing.callrecorder.Utils.DateUtil;

import java.io.File;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class RecorderIntendService extends IntentService {

    private static MediaRecorder recorder;
    private static volatile PowerManager.WakeLock wakeLock;
    private static volatile boolean isMounted = false;
    private static volatile boolean isInRecording = false;
    private  static final String AMR_DIR = "/CALL_RECORD/";
    private static final String IDLE = "app";
    private static volatile String fileNamePrefix = IDLE;
    public static String filePath="filePath";

    public RecorderIntendService() {
        super("RecorderIntendService");
    }


    private void startRecording() {
        if (!isMounted)
            return;
        stopRecording();
        try {
            File amr = new File(Environment.getExternalStorageDirectory()
                    .getAbsolutePath()
                    + AMR_DIR
                    + DateUtil.getDateTimeString()
                    + "_"
                    + fileNamePrefix + ".amr");
            log("Prepare recording in " + amr.getAbsolutePath());
            if (recorder==null)
            recorder = new MediaRecorder();
            recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            recorder.setOutputFile(amr.getAbsolutePath());
            recorder.prepare();
            recorder.start();
            isInRecording = true;
            acquireWakeLock();
            filePath=""+ amr.getAbsolutePath();
            //log("Recording in " +filePath);
        } catch (Exception e) {
            e.printStackTrace();
           // Log.w(TAG, e);
        }
    }

    public static void stopRecording() {

        try {
            if (isInRecording) {
                isInRecording = false;
                recorder.stop();
                recorder.release();
                recorder = null;
                releaseWakeLock();
                log("call recording stopped");
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }


    private void prepareAmrDir() {
        isMounted = Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);

        if (!isMounted)
            return;
        File amrRoot = new File(Environment.getExternalStorageDirectory()
                .getAbsolutePath() + AMR_DIR);
        if (!amrRoot.isDirectory())
            amrRoot.mkdir();
    }

    private void acquireWakeLock() {
        if (wakeLock == null) {
            log("Acquiring wake lock");
            PowerManager pm = (PowerManager)getSystemService(Context.POWER_SERVICE);
            wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, this
                    .getClass().getCanonicalName());
            wakeLock.acquire();
        }

    }

    private static void releaseWakeLock() {
        if (wakeLock != null && wakeLock.isHeld()) {
            wakeLock.release();
            wakeLock = null;
            log("Wake lock released");
        }

    }


    private static void log(String info) {
        if (isMounted) {
            File log = new File(Environment.getExternalStorageDirectory()
                    .getAbsolutePath()
                    + AMR_DIR
                    + "log_"
                    + DateUtil.getMonthString()
                    + ".txt");

        }
    }


    @Override
    protected void onHandleIntent(Intent intent) {

        prepareAmrDir();
        startRecording();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
       // stopRecording();
    }
}
