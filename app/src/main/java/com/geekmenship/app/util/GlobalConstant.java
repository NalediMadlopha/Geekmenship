package com.geekmenship.app.util;

public final class GlobalConstant {
    // Geek string
    public static final String GEEK = "geek";

    // Results integer declaration(s)
    public static final int RESULT_LOAD_IMAGE = 0;
    public static final int RESULT_CROP = 1;
    public static final int RESULT_MAP_LOCATION = 2;

    // Geek attributes integer declaration(s)
    public static final int FIRST_NAME = 0;
    public static final int LAST_NAME = 1;
    public static final int EMAIL = 2;
    public static final int DESCRIPTION = 3;
    public static final int PASSWORD = 4;
    public static final int CONFIRM_PASSWORD = 4;
    public static final int EVENT_ORGANIZER = 6;

    // Event attributes integer declaration(s)
    public static final int EVENT_TITLE = 0;
    public static final int EVENT_DESCRIPTION = 1;
    public static final int EVENT_LOCATION = 2;
    public static final int EVENT_DATE = 3;
    public static final int EVENT_TIME = 4;

    // Memory calculation and cache size
    public static final int maxMemory = (int) Runtime.getRuntime().maxMemory() / 1024;
    public static final int cacheSize = maxMemory / 8;

    // Event attributes string declaration(s)
    public static final String SELECT_DATA_URL = "http://192.168.43.252/geekmenship/select.php";
    public static final String INSERT_DATA_URL = "http://192.168.43.252/geekmenship/insert.php";
    public static final String PHOTOS_BASE_URL = "http://192.168.43.252/geekmenship/photos/";
    public static final String PROFILE_PICTURE_BASE_URL = "http://192.168.43.24/geekmenship/profile_pictures/";
}
