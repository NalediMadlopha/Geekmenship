package com.geekmenship.app;


import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;
import android.util.Log;

import com.geekmenship.dao.EventContract;
import com.geekmenship.dao.EventDbHelper;

import com.geekmenship.dao.EventContract.*;

public class TestDb extends AndroidTestCase {

    public static final String LOG_TAG = TestDb.class.getSimpleName();

    public void testCreateDb() throws Throwable {
        mContext.deleteDatabase((EventDbHelper.DATABASE_NAME));
        SQLiteDatabase db = new EventDbHelper(
                this.mContext).getWritableDatabase();

        assertEquals(true, db.isOpen());
        db.close();
    }

    public void testInsertReadDb() {

        int testId = 1;
        String testTitle = "GDG - Soweto";
        String testTime = "09:00 - 16:00";
        String testVenue = "Soweto, ZA";
        String testDate = "01 June 2015";
        String testPhoto = "EVENT13.jpg";
        String testDescription = "Description";
        String testCommunity = "Community Name";

        EventDbHelper dbHelper = new EventDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(EventEntry.COLUMN_EVENT_ID, testId);
        values.put(EventEntry.COLUMN_TITLE, testTitle);
        values.put(EventEntry.COLUMN_TIME, testTime);
        values.put(EventEntry.COLUMN_VENUE, testVenue);
        values.put(EventEntry.COLUMN_DATE, testDate);
        values.put(EventEntry.COLUMN_PHOTO, testPhoto);
        values.put(EventEntry.COLUMN_DESCRIPTION, testDescription);
        values.put(EventEntry.COLUMN_COMMUNITY_NAME, testCommunity);

        long eventRowId;
        eventRowId = db.insert(EventEntry.TABLE_NAME, null, values);

        assertTrue(eventRowId != -1);
        Log.e(LOG_TAG, "New row id: " + eventRowId);

        String[] columns = {
            EventEntry.COLUMN_EVENT_ID,
            EventEntry.COLUMN_TITLE,
            EventEntry.COLUMN_TIME,
            EventEntry.COLUMN_VENUE,
            EventEntry.COLUMN_DATE,
            EventEntry.COLUMN_PHOTO,
            EventEntry.COLUMN_DESCRIPTION,
            EventEntry.COLUMN_COMMUNITY_NAME,

        };

        Cursor cursor = db.query(
            EventEntry.TABLE_NAME,
                columns,
                null,
                null,
                null,
                null,
                null
        );

        if (cursor.moveToFirst()) {

            int titleIndex = cursor.getColumnIndex(EventEntry.COLUMN_TITLE);
            String title = cursor.getString(titleIndex);

            int timeIndex = cursor.getColumnIndex(EventEntry.COLUMN_TIME);
            String time = cursor.getString(timeIndex);

            int venueIndex = cursor.getColumnIndex(EventEntry.COLUMN_VENUE);
            String venue = cursor.getString(venueIndex);

            int dateIndex = cursor.getColumnIndex(EventEntry.COLUMN_DATE);
            String date = cursor.getString(dateIndex);

            int photoIndex = cursor.getColumnIndex(EventEntry.COLUMN_PHOTO);
            String photo = cursor.getString(photoIndex);

            int descriptionIndex = cursor.getColumnIndex(EventEntry.COLUMN_DESCRIPTION);
            String description = cursor.getString(descriptionIndex);

        } else {
            fail("No values returned : (");
        }

    }
}
