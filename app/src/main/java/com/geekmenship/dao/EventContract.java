package com.geekmenship.dao;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

public class EventContract {

    public static final String CONTENT_AUTORITY = "com.geekmenship.app";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTORITY);

    public static final String PATH_EVENT = "event";

    public static final class EventEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_EVENT).build();

        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/" + CONTENT_AUTORITY + "/" + PATH_EVENT;

        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/" + CONTENT_AUTORITY + "/" + PATH_EVENT;

        public static final String TABLE_NAME = "event";

        public static final String COLUMN_EVENT_ID = "id";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_TIME = "time";
        public static final String COLUMN_VENUE = "venue";
        public static final String COLUMN_DATE = "date";
        public static final String COLUMN_PHOTO = "photo";
        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_COMMUNITY_NAME = "community_name";

        public static Uri buildWeatherUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }
}
