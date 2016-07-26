package com.geekmenship.app;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;

import com.geekmenship.app.util.GlobalConstant;

public class SplashScreenActivity extends Activity {

    private static String TAG = SplashScreenActivity.class.getSimpleName();
    public SharedPreferences mSharedPreferences;

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splashscreen);

        mSharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(SplashScreenActivity.this);

        if (mSharedPreferences.contains(GlobalConstant.GEEK)) {
            startActivity(new Intent(SplashScreenActivity.this, MainActivity.class));
        } else {
            new DisplaySplash().execute();
        }

		finish();
	}
	
	private class DisplaySplash extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(Void... arg0) {
            try {
                Thread.sleep(4000); // Delays the thread (4 milliseconds)
            } catch (InterruptedException e) {
                Log.e(TAG, "Delay error");
            }
            return null;
		}

		@Override
		protected void onPostExecute(Void result) {
            startActivity(new Intent(SplashScreenActivity.this, SlideShowActivity.class));
		}
	}
}
