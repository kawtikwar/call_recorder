package com.amuramarketing.callrecorder.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class SQLiteHelper extends SQLiteOpenHelper {

    private static final String TAG = SQLiteHelper.class.getSimpleName();

    public SQLiteHelper(Context context) {
        super(context, DatabaseUtils.DATABASE_NAME, null, DatabaseUtils.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DatabaseUtils.CREATE_TB_CALL_INFO);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
