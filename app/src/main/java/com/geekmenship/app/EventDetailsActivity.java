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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.geekmenship.adapter.EventDetailsAdapter;
import com.geekmenship.app.util.RequestPackage;
import com.geekmenship.dao.HttpManager;
import com.geekmenship.model.Event;
import com.geekmenship.parsers.EventJSONParser;

import java.util.List;


public class EventDetailsActivity extends ActionBarActivity {

    public ImageView eventImage;
    public TextView communityNameTextView;
    public TextView eventTitleTextView;
    public TextView eventDateTextView;
    public TextView eventTimeTextView;
    public TextView eventVenueTextView;
    public TextView eventDescriptionTextView;
    public TextView organizerNameTextView;
    public TextView organizerEmailTextView;
    public TextView organizerDescriptionTextView;

    private Button attendanceButton;
    private List<FetchData> tasks;
    private ProgressBar progressBar;
    public List<Event> eventList;
    public Event event;
    public int eventID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);
        View view;

        try {
            Intent intent = getIntent();
            eventID = (int) intent.getIntExtra("eventID", 0);
        } catch (Exception e) {
            e.printStackTrace();
        }



//        eventImage = (ImageView) findViewById(R.id.eventImageView);
//        communityNameTextView = (TextView) findViewById(R.id.communityNameTextView);
//        eventTitleTextView = (TextView) findViewById(R.id.eventTitleTextView);
//        eventDateTextView = (TextView) findViewById(R.id.eventDateTextView);
//        eventTimeTextView = (TextView) findViewById(R.id.eventTimeTextView);
//        eventVenueTextView = (TextView) findViewById(R.id.eventVenueTextView);
//        eventDescriptionTextView = (TextView) findViewById(R.id.eventDescriptionTextView);
//        organizerNameTextView = (TextView) findViewById(R.id.organizerNameTextView);
//        organizerEmailTextView = (TextView) findViewById(R.id.organizerEmailTextView);
//        organizerDescriptionTextView = (TextView) findViewById(R.id.organizerDescriptionTextView);



//        communityNameTextView.setText(event.getCommunityName());
//        eventTitleTextView.setText(event.getTitle());
//        eventDateTextView.setText("Date: " + event.getDate());
//        eventTimeTextView.setText("Time: " + event.getTime());
//        eventVenueTextView.setText("Venue: " + event.getVenue());
//        eventDescriptionTextView.setText(event.getDescription());
//        organizerNameTextView.setText(event.getCommunityName());
//        organizerEmailTextView.setText(event.getCommunityName());
//        organizerDescriptionTextView.setText(event.getCommunityName());

//        attendanceButton = (Button) findViewById(R.id.attendButton);


//        if (isOnline()) {
//            requestData(GlobalConstant.SELECT_DATA_URL);
//        } else {
//            // TODO: Fetch data from the sqlite database
//            Toast.makeText(this, "Internet connection isn't available", Toast.LENGTH_LONG).show();
//        }


//        attendanceButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                // Changes the state of the button
//                if (attendanceButton.getText().equals(getResources().getText(R.string.not_attending))) {
//                    attendanceButton.setText(getResources().getText(R.string.attending));
//                    attendanceButton.setBackgroundColor(getResources().getColor(R.color.orange));
//                } else {
//                    attendanceButton.setText(getResources().getText(R.string.not_attending));
//                    attendanceButton.setBackgroundColor(getResources().getColor(R.color.greenish_blue));
//                }
//            }
//        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_event_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_share) {

            Intent sharingIntent = new Intent(Intent.ACTION_SEND);

            sharingIntent.setType("text/plain");

            String shareBody = "GDG" + " - " + "Android Fundamentals" + "\n\n";
            shareBody += "Date: \t" + "19/02/2015" + "\n";
            shareBody += "Time: \t" + "09:00 - 16:00" + "\n";
            shareBody += "Venue: \t" + "Innovation Hub" + "\n\n";
            shareBody += "Lorem ipsum dolor sit amet, consectetuer adipiscing elit, sed diam nonummy nibh euismod tincidunt ut laoreet dolore magna aliquam erat volutpat. Ut wisi enim ad minim veniam, quis nostrud exerci tation ullamcorper suscipit lobortis nisl ut aliquip ex ea commodo consequat. \n\n Duis autem vel eum iriure dolor in hendrerit in vulputate velit esse molestie consequat, vel illum dolore eu feugiat nulla facilisis at vero eros et accumsan et iusto odio dignissim qui blandit praesent luptatum zzril delenit augue duis dolore te feugait nulla facilisi. Nam liber tempor cum soluta nobis eleifend option congue nihil imperdiet doming id quod mazim placerat facer possim assum. Typi non habent claritatem insitam; est usus legentis in iis qui facit eorum claritatem. Investigationes demonstraverunt lectores legere me lius quod ii legunt saepius. Claritas est etiam processus dynamicus, qui sequitur mutationem consuetudium lectorum. Mirum est notare quam littera gothica, quam nunc putamus parum claram, anteposuerit litterarum formas humanitatis per seacula quarta decima et quinta decima. Eodem modo typi, qui nunc nobis videntur parum clari, fiant sollemnes in futurum." + "\n\n";

            sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);

            startActivity(Intent.createChooser(sharingIntent, "Share via"));

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    protected boolean isOnline() {

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }

    void requestData(String uri) {

        RequestPackage requestPackage = new RequestPackage();
        requestPackage.setMethod("GET");
        requestPackage.setUri(uri);
        requestPackage.setParam("table", "EVENT");

        new FetchData().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, requestPackage);
    }

    public EventDetailsAdapter updateDisplay() {

        EventDetailsAdapter adapter = new EventDetailsAdapter(EventDetailsActivity.this, R.layout.activity_event_details, eventList);
        return adapter;
    }

    private class FetchData extends AsyncTask<RequestPackage, String, List<Event>> {

        @Override
        protected void onPreExecute() {

//            if (tasks.size() == 0) {
//                progressBar.setVisibility(View.VISIBLE);
//            }
//
//            tasks.add(this);
        }

        @Override
        protected List<Event> doInBackground(RequestPackage... params) {

            String content = HttpManager.getData(params[0]);
            eventList = EventJSONParser.parseFeed(content);

            return eventList;
        }

        @Override
        protected void onPostExecute(List<Event> result) {
            if (result == null) {
                Toast.makeText(EventDetailsActivity.this, "Can't connect to web service", Toast.LENGTH_LONG).show();
                return;
            } else {
                event = result.get(eventID);

                communityNameTextView.setText(event.getCommunityName());
                eventTitleTextView.setText(event.getTitle());
                eventDateTextView.setText("Date: " + event.getDate());
                eventTimeTextView.setText("Time: " + event.getTime());
                eventVenueTextView.setText("Venue: " + event.getVenue());
                eventDescriptionTextView.setText(event.getDescription());
                organizerNameTextView.setText(event.getCommunityName());
                organizerEmailTextView.setText(event.getCommunityName());
                organizerDescriptionTextView.setText(event.getCommunityName());

                eventImage.setImageBitmap(event.getBitmap());

                return;
            }

        }
    }
}
