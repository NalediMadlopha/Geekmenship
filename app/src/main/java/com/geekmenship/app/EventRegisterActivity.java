package com.geekmenship.app;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.geekmenship.adapter.EventRegistrationAdapter;
import com.geekmenship.app.util.RequestPackage;
import com.geekmenship.dao.HttpManager;
import com.geekmenship.model.Event;
import com.geekmenship.serializers.EventJSONSerializer;
import com.geekmenship.app.util.GlobalConstant;
import com.geekmenship.app.util.mImageCropper;
import com.geekmenship.app.util.widget.FormEditText;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EventRegisterActivity extends ActionBarActivity {

    // Image button declaration(s)
    public ImageButton eventImageButton;

    // Dialog declaration(s)
    private Dialog startDateDialog;
    private Dialog endDateDialog;
    private Dialog startTimeDialog;
    private Dialog endTimeDialog;

    // Button declaration(s)
    private Button setButton;
    private Button cancelButton;

    // Number picker declaration(s)
    private NumberPicker ageNumberPicker;

    // Radio group declaration(s)
    private RadioGroup occupationRadioGroup;
    private RadioGroup genderRadioGroup;

    // Radio button declaration(s)
    private RadioButton selectedOccupation;
    private RadioButton selectedGender;

    // Text view declaration(s)
    private TextView eventVenueDescriptionTextView;
    private TextView eventDateDescriptionTextView;
    private TextView eventTimeDescriptionTextView;

    private DatePicker datePicker;
    private TimePicker timePicker;

    private String startDate;
    private String endDate;
    private String startTime;
    private String endTime;
    private String currentHour;
    private String currentMinute;

    // Progress bar dialog declaration(s)
    private ProgressDialog mapProgressDialog;
    private ProgressDialog eventRegistrationProgressDialog;

    // List view declaration(s)
    private ListView listView;

    // Object declaration(s)
    public Event newEvent = new Event();
    private EventRegistrationAdapter eventRegistrationAdapter;

    // Array declaration(s)
    private final Event[] items = new Event[5];
    List<Event> eventList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_register_2);

        // Declares a interface components
        listView = (ListView) findViewById(R.id.eventRegisterListView);
        eventImageButton = (ImageButton) findViewById(R.id.eventPictureImageButton);

        mapProgressDialog = new ProgressDialog(EventRegisterActivity.this);
        eventRegistrationProgressDialog = new ProgressDialog(EventRegisterActivity.this);

        mapProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mapProgressDialog.setMessage("Loading map...");
        mapProgressDialog.setIndeterminate(true);
        mapProgressDialog.setCancelable(true);

        eventRegistrationProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        eventRegistrationProgressDialog.setMessage("Registering an event...");
        eventRegistrationProgressDialog.setIndeterminate(true);
        eventRegistrationProgressDialog.setCancelable(true);

        // Adds the Geek objects to an array
        eventList.add(new Event(GlobalConstant.EVENT_TITLE));
        eventList.add(new Event(GlobalConstant.EVENT_DESCRIPTION));
        eventList.add(new Event(GlobalConstant.EVENT_LOCATION));
        eventList.add(new Event(GlobalConstant.EVENT_DATE));
        eventList.add(new Event(GlobalConstant.EVENT_TIME));

        updateDisplay();

        listView.setItemsCanFocus(true);
        listView.setOnItemClickListener(mEventRegister_OnItemClickListener);
        eventImageButton.setOnClickListener(mEventRegister_OnClickLister);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_event_register, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_accept_event_register:

                EditText eventTitle = (FormEditText) findViewById(R.id.eventTitleEditText);
                EditText eventDescription = (FormEditText) findViewById(R.id.eventDescriptionEditText);
                TextView eventLocation = (TextView) findViewById(R.id.eventVenueDescriptionTextView);

                newEvent.setTitle(eventTitle.getText().toString());
                newEvent.setDescription(eventDescription.getText().toString());
                newEvent.setVenue(eventLocation.getText().toString());

                if (isOnline()) {
                    postData(GlobalConstant.INSERT_DATA_URL);
                } else {
                    Toast.makeText(EventRegisterActivity.this, getString(R.string.error_no_internet), Toast.LENGTH_LONG).show();
                }

                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    final View.OnClickListener mEventRegister_OnClickLister = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            switch(v.getId()) {
                case R.id.eventPictureImageButton:
                    loadEventImage();
                    break;
                case R.id.eventDateSetButton:
                    displayEndDateDialog();
                    break;
                case R.id.eventTimeSetButton:
                    displayEndTimeDialog();
                    break;
            }
        }
    };

    final AdapterView.OnItemClickListener mEventRegister_OnItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        // Checks the selected list item
        switch(position) {
            case 2:
                displayLocationActivity();
                break;
            case 3:
                displayStartDateDialog();
                break;
            case 4:
                displayStartTimeDialog();
                break;
        }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case GlobalConstant.RESULT_LOAD_IMAGE:
                // Executes when an image is loaded
                if(resultCode == Activity.RESULT_OK){
                    Bundle extras = data.getExtras();

                    // Declares a new image cropper object
                    mImageCropper mImageCropper = new mImageCropper(getPictureURI(data));

                    // Checks if there is an exception
                    if (mImageCropper.exceptionExists) {
                        Toast.makeText(this, getString(R.string.error_device_not_supported),
                                Toast.LENGTH_LONG).show();
                    } else {
                        // Start the activity - handle returning in on activity result
                        startActivityForResult(mImageCropper.crop(),
                                GlobalConstant.RESULT_CROP);
                    }
                }
                break;
            case GlobalConstant.RESULT_CROP:
                // Executes when an image is cropped
                if(resultCode == Activity.RESULT_OK){
                    Bundle extras = data.getExtras();
                    Bitmap selectedBitmap = extras.getParcelable("data");

                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    // Must compress the Image to reduce image size to make upload easy
                    selectedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                    byte[] byte_arr = stream.toByteArray();

                    // Encode Image to String
                    newEvent.setEncodedString(Base64.encodeToString(byte_arr, Base64.DEFAULT));

                    newEvent.setBitmap(selectedBitmap); // Sets the event's image bitmap

                    // Sets the bitmap data to image view
                    eventImageButton.setImageBitmap(newEvent.getBitmap());
                    eventImageButton.setScaleType(ImageView.ScaleType.CENTER_CROP);
                }
                break;
            case GlobalConstant.RESULT_MAP_LOCATION:
                // Executes when a map location is selected
                Bundle extras = data.getExtras();
                if(extras.containsKey("newEvent")){
                    Event newEvent = (Event) extras.getSerializable("newEvent");
                    eventVenueDescriptionTextView = (TextView) listView.findViewById(R.id.eventVenueDescriptionTextView);
                    eventVenueDescriptionTextView.setText(newEvent.getVenue());
                }
                break;
        }
    }

    public void displayLocationActivity() {
        if (isOnline()) {
            new OpenMap().execute();
        } else {
            Toast.makeText(EventRegisterActivity.this, getResources().getString(R.string.error_no_internet), Toast.LENGTH_LONG).show();
        }
    }

    public void displayStartDateDialog() {
        // Executes when the date items is selected
        startDateDialog = new Dialog(EventRegisterActivity.this);

        startDateDialog.setContentView(R.layout.dialog_date_picker);
        startDateDialog.setTitle(getString(R.string.event_start_date));

        datePicker = (DatePicker) startDateDialog.findViewById(R.id.eventDatePicker);
        setButton = (Button) startDateDialog.findViewById(R.id.eventDateSetButton);
        cancelButton = (Button) startDateDialog.findViewById(R.id.eventDateCancelButton);

        setButton.setOnClickListener(mEventRegister_OnClickLister);

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startDateDialog.dismiss();
            }
        });

        startDateDialog.show();
    }

    public void displayEndDateDialog() {
        // Formats the date from the date picker
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(getString(R.string.date_format));
        startDate = simpleDateFormat.format(new Date(datePicker.getYear() - 1900, datePicker.getMonth(), datePicker.getDayOfMonth()));

        endDateDialog = new Dialog(EventRegisterActivity.this);

        endDateDialog.setContentView(R.layout.dialog_date_picker);
        endDateDialog.setTitle(getString(R.string.event_end_date));

        eventDateDescriptionTextView = (TextView) findViewById(R.id.eventDateDescriptionTextView);
        datePicker = (DatePicker) endDateDialog.findViewById(R.id.eventDatePicker);
        setButton = (Button) endDateDialog.findViewById(R.id.eventDateSetButton);
        cancelButton = (Button) endDateDialog.findViewById(R.id.eventDateCancelButton);

        setButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Formats the date from the date picker
                SimpleDateFormat df = new SimpleDateFormat(getString(R.string.date_format));
                endDate = df.format(new Date(datePicker.getYear() - 1900, datePicker.getMonth(), datePicker.getDayOfMonth()));

                eventDateDescriptionTextView.setText(startDate + " - " + endDate);
                newEvent.setDate(startDate + " - " + endDate); // Sets the event's start and end dates
                endDateDialog.dismiss();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                endDateDialog.dismiss();
            }
        });
        startDateDialog.dismiss();
        endDateDialog.show();
    }

    public void displayStartTimeDialog() {
        // Executes when the time items is selected
        startTimeDialog = new Dialog(EventRegisterActivity.this);

        startTimeDialog.setContentView(R.layout.dialog_time_picker);
        startTimeDialog.setTitle(getString(R.string.event_start_time));

        timePicker = (TimePicker) startTimeDialog.findViewById(R.id.eventTimePicker);
        setButton = (Button) startTimeDialog.findViewById(R.id.eventTimeSetButton);
        cancelButton = (Button) startTimeDialog.findViewById(R.id.eventTimeCancelButton);

        setButton.setOnClickListener(mEventRegister_OnClickLister);

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startTimeDialog.dismiss();
            }
        });

        startTimeDialog.show();
    }

    public void displayEndTimeDialog() {
        /* Adds a leading zero to the hour
                     *  If it is less than ten
                     */
        currentHour = (timePicker.getCurrentHour() < 10)
                ? "0" + String.valueOf(timePicker.getCurrentHour())
                : String.valueOf(timePicker.getCurrentHour());

                    /* Adds a leading zero to the minute
                     *  If it is less than ten
                     */
        currentMinute = (timePicker.getCurrentMinute() < 10)
                ? "0" + String.valueOf(timePicker.getCurrentMinute())
                : String.valueOf(timePicker.getCurrentMinute());

        startTime = currentHour + ":" + currentMinute;

        endTimeDialog = new Dialog(EventRegisterActivity.this);

        endTimeDialog.setContentView(R.layout.dialog_time_picker);
        endTimeDialog.setTitle(getString(R.string.event_end_time));

        eventTimeDescriptionTextView = (TextView) findViewById(R.id.eventTimeDescriptionTextView);
        timePicker = (TimePicker) endTimeDialog.findViewById(R.id.eventTimePicker);
        setButton = (Button) endTimeDialog.findViewById(R.id.eventTimeSetButton);
        cancelButton = (Button) endTimeDialog.findViewById(R.id.eventTimeCancelButton);

        setButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                            /* Adds a leading zero to the hour
                             *  If it is less than ten
                             */
                currentHour = (timePicker.getCurrentHour() < 10)
                        ? "0" + String.valueOf(timePicker.getCurrentHour())
                        : String.valueOf(timePicker.getCurrentHour());

                            /* Adds a leading zero to the minute
                             *  If it is less than ten
                             */
                currentMinute = (timePicker.getCurrentMinute() < 10)
                        ? "0" + String.valueOf(timePicker.getCurrentMinute())
                        : String.valueOf(timePicker.getCurrentMinute());

                endTime = currentHour + ":" + currentMinute;

                eventTimeDescriptionTextView.setText(startTime + " - " + endTime);
                newEvent.setTime(startTime + " - " + endTime); // Sets the event's start and end time
                endTimeDialog.dismiss();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                endTimeDialog.dismiss();
            }
        });

        startTimeDialog.dismiss();
        endTimeDialog.show();
    }

    public void loadEventImage() {
        Intent intent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, GlobalConstant.RESULT_LOAD_IMAGE);
    }

    private String getPictureURI(Intent data) {
        Uri selectedImage = data.getData();
        String[] filePathColumn = { MediaStore.Images.Media.DATA };

        Cursor cursor = getContentResolver().query(selectedImage,
                filePathColumn, null, null, null);
        cursor.moveToFirst();

        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String pictureURI = cursor.getString(columnIndex);

        newEvent.setPhoto(
            pictureURI.substring(pictureURI.lastIndexOf('/') + 1)
        ); // Sets the event photo

        cursor.close();

        return pictureURI;
    }

    void postData(String uri) {

        RequestPackage requestPackage = new RequestPackage();
        requestPackage.setMethod("POST");
        requestPackage.setUri(uri);
        requestPackage.setParams(new EventJSONSerializer().serializeFeed(newEvent));

        new PushData().execute(requestPackage);
    }

    void updateDisplay() {

        eventRegistrationAdapter = new EventRegistrationAdapter(this,
                R.layout.activity_event_register_2, eventList);
        listView.setAdapter(eventRegistrationAdapter);
    }

    protected boolean isOnline() {

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }

    private class PushData extends AsyncTask<RequestPackage, String, String> {

        @Override
        protected void onPreExecute() {
            eventRegistrationProgressDialog.show();
        }

        @Override
        protected String doInBackground(RequestPackage... params) {
            String content = HttpManager.getData(params[0]);
            return content;
        }

        @Override
        protected void onPostExecute(String result) {

            if (result == null) {
                Toast.makeText(EventRegisterActivity.this, "Can't connect to web service", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(EventRegisterActivity.this, "The event is registered successfully", Toast.LENGTH_LONG).show();
            }

            eventRegistrationProgressDialog.dismiss();
        }
    }

    public class OpenMap extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            mapProgressDialog.show();
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            Intent intent = new Intent(EventRegisterActivity.this, MapsActivity.class);

            intent.putExtra("newEvent", newEvent);
            startActivityForResult(intent, GlobalConstant.RESULT_MAP_LOCATION);

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            mapProgressDialog.dismiss();
            super.onPostExecute(aVoid);
        }
    }
}