package com.geekmenship.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.geekmenship.dao.EventContract.*;

public class EventDbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "event.db";

    public EventDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        final String SQL_CREATE_EVENT_TABLE =
                "CREATE TABLE " + EventEntry.TABLE_NAME + " (" +
                        EventEntry.COLUMN_EVENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        EventEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                        EventEntry.COLUMN_TIME + " TEXT NOT NULL, " +
                        EventEntry.COLUMN_VENUE + " TEXT NOT NULL, " +
                        EventEntry.COLUMN_DATE + " TEXT NOT NULL, " +
                        EventEntry.COLUMN_PHOTO + " TEXT NOT NULL, " +
                        EventEntry.COLUMN_DESCRIPTION + " TEXT NOT NULL, " +
                        EventEntry.COLUMN_COMMUNITY_NAME + " TEXT NOT NULL, " +

                        " UNIQUE (" + EventEntry.COLUMN_TITLE + ", " + EventEntry.COLUMN_DATE + ", " +
                        ", " + EventEntry.COLUMN_VENUE + ") ON CONFLICT REPLACE);";
                ;

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + EventEntry.TABLE_NAME);
        onCreate(db);
    }
}
