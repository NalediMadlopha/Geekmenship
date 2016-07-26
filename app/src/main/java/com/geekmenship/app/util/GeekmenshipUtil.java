package com.geekmenship.app.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class GeekmenshipUtil {

    private Context mContext;

    public GeekmenshipUtil() {
    }

    public GeekmenshipUtil(Context mContext) {
        this.mContext = mContext;
    }

    protected boolean isOnline() {

        ConnectivityManager connectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }
}
