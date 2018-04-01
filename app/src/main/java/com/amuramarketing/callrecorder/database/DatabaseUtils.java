package com.amuramarketing.callrecorder.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.amuramarketing.callrecorder.models.CallDetailsModel;

import java.util.ArrayList;


public class DatabaseUtils {

    public static final String DATABASE_NAME = "db_call_records";
    public static final int DATABASE_VERSION = 2;

    private static final String TAG = DatabaseUtils.class.getSimpleName();

    //TABLES
    public static final String TB_CALL_INFO = "call_info";

    // call_info
    public static final String ID = "id";
    public static final String MOBILE_NUMBER = "mobile_no";

    public static final String START_TIME = "start_time";
    public static final String END_TIME = "end_time";
    public static final String DURATION = "duration";
    public static final String CALL_TYPE = "call_type";
    public static final String CALL_STATUS = "call_status";
    public static final String AUDIO_FILE_PATH = "file_path";
    public static final String IS_SYNC = "is_sync";
    public static final String DATE = "date";


    // create table to store call details
    public static final String CREATE_TB_CALL_INFO = "CREATE TABLE IF NOT EXISTS  "
            + TB_CALL_INFO
            + "("
            + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + MOBILE_NUMBER + " TEXT , "
            + CALL_TYPE + " TEXT , "
            + CALL_STATUS + " TEXT , "
            + DATE + " TEXT , "
            + START_TIME + " TEXT , "
            + END_TIME + " TEXT , "
            + DURATION + " TEXT , "
            + AUDIO_FILE_PATH + " TEXT , "
            + IS_SYNC + " INTEGER, "
            + "UNIQUE( " + MOBILE_NUMBER + ", " + START_TIME + " , " + END_TIME + " ) ON CONFLICT REPLACE "
            + ");";


    public static SQLiteDatabase getDatabaseInstance(Context context) {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        return db;
    }

    public static boolean insertCallDetails(Context context, CallDetailsModel callDetailsModel) {

        boolean status = false;
        SQLiteDatabase db = getDatabaseInstance(context);

        ContentValues values = new ContentValues();

        values.put(MOBILE_NUMBER, callDetailsModel.getMobileNo());
        values.put(CALL_TYPE, callDetailsModel.getCallType());
        values.put(CALL_STATUS, callDetailsModel.getCallStatus());
        values.put(DATE, callDetailsModel.getDate());
        values.put(START_TIME, callDetailsModel.getStartTime());
        values.put(END_TIME, callDetailsModel.getEndTime());
        values.put(DURATION, callDetailsModel.getDuration());
        values.put(AUDIO_FILE_PATH, callDetailsModel.getFilePath());
        values.put(IS_SYNC, callDetailsModel.getIs_sync());

        try {
            long insertStatus = db.insertOrThrow(TB_CALL_INFO, null,
                    values);

            if (insertStatus > -1) {
                status = true;
                Log.i(TAG, "Inserted");

            } else {
                status = false;
                Log.i(TAG, "Not Inserted");
            }
        } catch (Exception e) {
            status = false;
            e.printStackTrace();
        }
        return status;
    }


    /*   Get All Call logs to send it to server       */
    public static ArrayList<CallDetailsModel> getCallDetails(Context context) {
        final ArrayList<CallDetailsModel> userDatas = new ArrayList<CallDetailsModel>();
        try {
            SQLiteDatabase db = getDatabaseInstance(context);

            // db.get
            String sql = "SELECT * FROM " + TB_CALL_INFO + " WHERE " + DURATION + " > 0 AND " + IS_SYNC + " =0";

            Cursor cursor = db.rawQuery(sql, null);
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    do {
                        CallDetailsModel data = new CallDetailsModel();
                        data.setId(cursor.getString(cursor.getColumnIndex(ID)));
                        data.setMobileNo(cursor.getString(cursor.getColumnIndex(MOBILE_NUMBER)));
                        data.setCallType(cursor.getString(cursor.getColumnIndex(CALL_TYPE)));
                        data.setCallStatus(cursor.getString(cursor.getColumnIndex(CALL_STATUS)));
                        data.setDate(cursor.getString(cursor.getColumnIndex(DATE)));
                        data.setStartTime(cursor.getString(cursor.getColumnIndex(START_TIME)));
                        data.setEndTime(cursor.getString(cursor.getColumnIndex(END_TIME)));
                        data.setIs_sync(cursor.getInt(cursor.getColumnIndex(IS_SYNC)));
                        data.setDuration(cursor.getString(cursor.getColumnIndex(DURATION)));
                        data.setFilePath(cursor.getString(cursor.getColumnIndex(AUDIO_FILE_PATH)));


                        userDatas.add(data);
                    } while (cursor.moveToNext());
                }
            }
            cursor.close();
            //db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return userDatas;
    }
}