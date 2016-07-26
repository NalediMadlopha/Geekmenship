package com.geekmenship.app;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.geekmenship.adapter.EventAdapter;
import com.geekmenship.adapter.EventListAdapter;
import com.geekmenship.app.util.RequestPackage;
import com.geekmenship.dao.HttpManager;
import com.geekmenship.model.Event;
import com.geekmenship.parsers.EventJSONParser;
import com.geekmenship.app.util.GlobalConstant;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends ActionBarActivity {

    private EventListAdapter eventListAdapter;
    public List<Event> eventList;
    private ListView eventsListView;

    List<FetchEvents> tasks;

    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_2);

        progressBar = (ProgressBar) findViewById(R.id.progressBar1);
        progressBar.setVisibility(View.INVISIBLE);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.ic_launcher);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        tasks = new ArrayList<>();

        if (isOnline()) {
            requestData(GlobalConstant.SELECT_DATA_URL);
        } else {
            // TODO: Fetch data from the sqlite database
            Toast.makeText(this, "Internet connection isn't available", Toast.LENGTH_LONG).show();
        }

        eventsListView = (ListView) findViewById(R.id.eventsListView);
        eventsListView.setOnItemClickListener(mEvent_OnItemClickListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    final AdapterView.OnItemClickListener mEvent_OnItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent = new Intent(MainActivity.this, EventDetailsActivity.class);

            try {
                intent.putExtra("eventID", position);
                startActivity(intent);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        if (1 == 1) {
            menu.getItem(0).setVisible(true);
        } else {
            menu.getItem(0).setVisible(false);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_overflow_main_menu) {
//            startActivity(new Intent(MainActivity.this, EventRegisterActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    void requestData(String uri) {

        RequestPackage requestPackage = new RequestPackage();
        requestPackage.setMethod("GET");
        requestPackage.setUri(uri);
        requestPackage.setParam("table", "EVENT");

        new FetchEvents().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, requestPackage);
    }

    void updateDisplay() {

        EventAdapter adapter = new EventAdapter(MainActivity.this, R.layout.item_event, eventList);
        ListView eventListView = (ListView) findViewById(R.id.eventsListView);
        eventListView.setAdapter(adapter);
    }

    protected boolean isOnline() {

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }

    private class FetchEvents extends AsyncTask<RequestPackage, String, List<Event>> {

        @Override
        protected void onPreExecute() {

            if (tasks.size() == 0) {
                progressBar.setVisibility(View.VISIBLE);
            }

            tasks.add(this);
        }

        @Override
        protected List<Event> doInBackground(RequestPackage... params) {

            String content = HttpManager.getData(params[0]);
            eventList = EventJSONParser.parseFeed(content);

            return eventList;
        }

        @Override
        protected void onPostExecute(List<Event> result) {

            tasks.remove(this);

            if (tasks.size() == 0) {
               progressBar.setVisibility(View.INVISIBLE);
            }

            if (result == null) {
                Toast.makeText(MainActivity.this, "Can't connect to web service", Toast.LENGTH_LONG).show();
                return;
            }

            updateDisplay();
        }
    }
}
