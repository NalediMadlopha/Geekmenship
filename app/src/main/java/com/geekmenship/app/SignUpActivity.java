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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.geekmenship.adapter.SignUpAdapter;
import com.geekmenship.app.util.RequestPackage;
import com.geekmenship.dao.HttpManager;
import com.geekmenship.model.Geek;
import com.geekmenship.serializers.GeekJSONSerializer;
import com.geekmenship.app.util.GlobalConstant;
import com.geekmenship.app.util.mImageCropper;
import com.geekmenship.app.util.widget.FormEditText;

import java.io.ByteArrayOutputStream;

public class SignUpActivity extends ActionBarActivity {

    // Image button declaration(s)
    public ImageButton profilePictureImageButton;

    // Dialog declaration(s)
    private Dialog eventOrganiserDialog;

    // Progress bar dialog declaration(s)
    private ProgressDialog progressDialog;

    // List view declaration(s)
    public ListView listView;

    // Object declaration(s)
    public Geek newGeek = new Geek();
    private SignUpAdapter signUpAdapter;

    // Array declaration(s)
    public final Geek[] items = new Geek[7];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_2);

        getSupportActionBar().setSubtitle(getString(R.string.sign_Up));

        // Declares a interface components
        listView = (ListView) findViewById(R.id.eventRegisterListView);
        profilePictureImageButton = (ImageButton) findViewById(R.id.profilePictureImageButton);

        progressDialog = new ProgressDialog(SignUpActivity.this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("Geeking Up...");
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(true);

        // Adds the Geek objects to an array
        items[0] = new Geek(GlobalConstant.FIRST_NAME);
        items[1] = new Geek(GlobalConstant.LAST_NAME);
        items[2] = new Geek(GlobalConstant.EMAIL);
        items[3] = new Geek(GlobalConstant.DESCRIPTION);
        items[4] = new Geek(GlobalConstant.PASSWORD);
        items[5] = new Geek(GlobalConstant.CONFIRM_PASSWORD);
        items[6] = new Geek(GlobalConstant.EVENT_ORGANIZER);

        signUpAdapter = new SignUpAdapter(this, R.id.firstNameEditText, items);

        listView.setItemsCanFocus(true);
        listView.setAdapter(signUpAdapter);
        profilePictureImageButton.setOnClickListener(mGlobal_OnClickLister);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sign_up, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_accept_sign_up:

                EditText firstNameEditText = (FormEditText) findViewById(R.id.firstNameEditText);
                EditText lastNameEditText = (FormEditText) findViewById(R.id.lastNameEditText);
                EditText emailEditText = (FormEditText) findViewById(R.id.emailEditText);
                EditText descriptionEditText = (FormEditText) findViewById(R.id.descriptionEditText);
                EditText passwordEditText = (FormEditText) findViewById(R.id.passwordEditText);
                EditText confirmPasswordEditText = (FormEditText) findViewById(R.id.confirmPasswordEditText);
                CheckBox eventOrganiserCheckBox = (CheckBox) findViewById(R.id.eventOrganiserCheckBox);

                newGeek.setFirstName(firstNameEditText.getText().toString());
                newGeek.setLastName(lastNameEditText.getText().toString());
                newGeek.setEmail(emailEditText.getText().toString());
                newGeek.setDescription(descriptionEditText.getText().toString());
                newGeek.setPassword(passwordEditText.getText().toString());

                if (eventOrganiserCheckBox.isChecked()) {
                    newGeek.setEventOrganiser(true);
                    displayEventOrganiserDialog();
                } else {
                    newGeek.setEventOrganiser(false);

                    if (isOnline()) {
                        postData(GlobalConstant.INSERT_DATA_URL);
                    } else {
                        Toast.makeText(SignUpActivity.this, getString(R.string.error_no_internet), Toast.LENGTH_LONG).show();
                    }
                }

                break;
            default:
                return super.onOptionsItemSelected(item);
        }

        return true;
    }

    final View.OnClickListener mGlobal_OnClickLister = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch(v.getId()) {
                case R.id.profilePictureImageButton:
                    Intent intent = new Intent(Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, GlobalConstant.RESULT_LOAD_IMAGE);
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
                    newGeek.setEncodedString(Base64.encodeToString(byte_arr, Base64.DEFAULT));

                    newGeek.setBitmap(selectedBitmap); // Sets the geek's image bitmap

                    // Sets the bitmap data to image view
                    profilePictureImageButton.setImageBitmap(newGeek.getBitmap());
                    profilePictureImageButton.setScaleType(ImageView.ScaleType.CENTER_CROP);
                }
                break;
        }
    }

    // Gets the picture URI
    private String getPictureURI(Intent data) {
        Uri selectedImage = data.getData();
        String[] filePathColumn = { MediaStore.Images.Media.DATA };

        Cursor cursor = getContentResolver().query(selectedImage,
                filePathColumn, null, null, null);
        cursor.moveToFirst();

        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String pictureURI = cursor.getString(columnIndex);

        newGeek.setProfilePicture(
                pictureURI.substring(pictureURI.lastIndexOf('/') + 1)
        ); // Sets the event photo

        cursor.close();

        return pictureURI;
    }

    public void displayEventOrganiserDialog() {
        eventOrganiserDialog = new Dialog(SignUpActivity.this);

        eventOrganiserDialog.setContentView(R.layout.dialog_community_register);
        eventOrganiserDialog.setTitle(getString(R.string.event_organiser));

        final EditText geekCommunity = (EditText) eventOrganiserDialog.findViewById(R.id.geekCommunityNameEditText);
        final EditText geekCommunityDescription = (EditText) eventOrganiserDialog.findViewById(R.id.geekCommunityDescriptionEditText);
        Button eventOrganiserDoneButton = (Button) eventOrganiserDialog.findViewById(R.id.eventOrganiserDoneButton);
        Button eventOrganiserCancelButton = (Button) eventOrganiserDialog.findViewById(R.id.eventOrganiserCancelButton);

        eventOrganiserDoneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newGeek.setCommunityName(geekCommunity.getText().toString());
                newGeek.setCommunityDescription(geekCommunityDescription.getText().toString());

                if (isOnline()) {
                    postData(GlobalConstant.INSERT_DATA_URL);
                    eventOrganiserDialog.dismiss();
                } else {
                    Toast.makeText(SignUpActivity.this, getString(R.string.error_no_internet), Toast.LENGTH_LONG).show();
                }
            }
        });

        eventOrganiserCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eventOrganiserDialog.dismiss();
            }
        });

        eventOrganiserDialog.show();
    }

    void postData(String uri) {

        RequestPackage requestPackage = new RequestPackage();
        requestPackage.setMethod("POST");
        requestPackage.setUri(uri);
        requestPackage.setParams(new GeekJSONSerializer().serializeFeed(newGeek));

        new PushData().execute(requestPackage);
    }

    protected boolean isOnline() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnectedOrConnecting()) {
            return true;
        } else {
            return false;
        }
    }

    private class PushData extends AsyncTask<RequestPackage, String, String> {

        @Override
        protected void onPreExecute() {
            progressDialog.show();
        }

        @Override
        protected String doInBackground(RequestPackage... params) {
            String content = HttpManager.getData(params[0]);
            return content;
        }

        @Override
        protected void onPostExecute(String result) {

            progressDialog.dismiss();

            if (result == null) {
                Toast.makeText(SignUpActivity.this, "Can't connect to web service", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(SignUpActivity.this, "Welcome buddy, you are now a geek.", Toast.LENGTH_LONG).show();
            }

            Intent intent;
            intent = new Intent(SignUpActivity.this, MainActivity.class);
            startActivity(intent); // Starts a new activity

            finish(); // Close this activity
        }
    }
}