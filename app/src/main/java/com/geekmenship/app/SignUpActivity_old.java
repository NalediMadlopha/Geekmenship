package com.geekmenship.app;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.geekmenship.app.util.validator.EmailValidator;
import com.geekmenship.app.util.validator.NumericRangeValidator;
import com.geekmenship.app.util.validator.PersonNameValidator;
import com.geekmenship.app.util.validator.SameValueValidator;
import com.geekmenship.app.util.widget.FormEditText;

import java.io.File;


public class SignUpActivity_old extends ActionBarActivity {

    // Image view declaration(s)
    private ImageView profilePicture;

    // Button declaration(s)
    private Button signUpButton;

    // Frame layout declaration(s)
    private FrameLayout firstNameFL;
    private FrameLayout lastNameFL;
    private FrameLayout ageFL;
    private FrameLayout emailFL;
    private FrameLayout descriptionFL;
    private FrameLayout passwordFL;
    private FrameLayout confirmPasswordFL;

    // Form edit text declaration(s)
    private FormEditText firstNameET;
    private FormEditText lastNameET;
    private FormEditText ageET;
    private FormEditText emailET;
    private FormEditText descriptionET;
    private FormEditText passwordET;
    private FormEditText confirmPasswordET;

    // Integer declaration(s)
    private final int RESULT_LOAD_IMAGE = 1;
    private final int RESULT_CROP = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        profilePicture = (ImageView) findViewById(R.id.newProfilePicButton);
        signUpButton = (Button) findViewById(R.id.signUpButton);

        // Occupation spinner mapping
        Spinner occupationSpinner = (Spinner) findViewById(R.id.occupationSpinner);
        String[] occupationArray = {"-- Occupation --", "Employee", "Entrepreneur", "Freelancer",
                "Researcher", "Student", "Other"};
        ArrayAdapter<String> occupationAdapter = new ArrayAdapter<String>(
                SignUpActivity_old.this,
                android.R.layout.simple_spinner_item, occupationArray);
        occupationSpinner.setAdapter(occupationAdapter);

        // Location spinner mapping
        Spinner locationSpinner = (Spinner) findViewById(R.id.locationSpinner);
        String[] locationArray = {"-- Location --", "Johannesburg", "Pretoria", "Witbank", "Welkom",
                "Vaal", "Soweto", "Braamfontein"};
        ArrayAdapter<String> locationAdapter = new ArrayAdapter<String>(
                SignUpActivity_old.this,
                android.R.layout.simple_spinner_item, locationArray);
        locationSpinner.setAdapter(locationAdapter);

        // The first name frame layout
        firstNameFL = (FrameLayout) findViewById(R.id.firstNameFrameLayout);
        firstNameFL.addView(LayoutInflater.from(this).inflate(
                R.layout.edit_text_first_name, firstNameFL, false));
        firstNameET = (FormEditText) findViewById(R.id.firstNameEditText);
        firstNameET.addValidator(new PersonNameValidator(getResources().getString(R.string.invalid_first_name)));

        // The last name frame layout
        lastNameFL = (FrameLayout) findViewById(R.id.lastNameFrameLayout);
        lastNameFL.addView(LayoutInflater.from(this).inflate(
                R.layout.edit_text_last_name, lastNameFL, false));
        lastNameET = (FormEditText) findViewById(R.id.lastNameEditText);
        lastNameET.addValidator(new PersonNameValidator(getResources().getString(R.string.invalid_last_name)));

        // The last age frame layout
        ageFL = (FrameLayout) findViewById(R.id.ageFrameLayout);
        ageFL.addView(LayoutInflater.from(this).inflate(
                R.layout.edit_text_age, ageFL, false));
        ageET = (FormEditText) findViewById(R.id.ageEditText);
        ageET.addValidator(new NumericRangeValidator("You are too young/old", 10, 100));

        // The email frame layout
        emailFL = (FrameLayout) findViewById(R.id.emailFrameLayout);
        emailFL.addView(LayoutInflater.from(this).inflate(
                R.layout.edit_text_email_2, emailFL, false));
        emailET = (FormEditText) findViewById(R.id.emailEditText);
        emailET.addValidator(new EmailValidator(getResources().getString(R.string.invalid_email)));

        // The description frame layout
        descriptionFL = (FrameLayout) findViewById(R.id.descriptionFrameLayout);
        descriptionFL.addView(LayoutInflater.from(this).inflate(
                R.layout.edit_text_description, descriptionFL, false));
        descriptionET = (FormEditText) findViewById(R.id.descriptionEditText);

        // The password frame layout
        passwordFL = (FrameLayout) findViewById(R.id.passwordFrameLayout);
        passwordFL.addView(LayoutInflater.from(this).inflate(
                R.layout.edit_text_password, passwordFL, false));
        passwordET = (FormEditText) findViewById(R.id.passwordEditText);

        // The confirm password frame layout
        confirmPasswordFL = (FrameLayout) findViewById(R.id.confirmPasswordFrameLayout);
        confirmPasswordFL.addView(LayoutInflater.from(this).inflate(
                R.layout.edit_text_confirm_password, confirmPasswordFL, false));
        confirmPasswordET = (FormEditText) findViewById(R.id.confirmPasswordEditText);
        confirmPasswordET.addValidator(new SameValueValidator(passwordET,
                getResources().getString(R.string.password_mismatch)));

        // Sets the on click listeners
        profilePicture.setOnClickListener(mGlobal_OnClickLister);
        signUpButton.setOnClickListener(mGlobal_OnClickLister);
    }

    final View.OnClickListener mGlobal_OnClickLister = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch(v.getId()) {
                case R.id.newProfilePicButton:
                    Intent i = new Intent(Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(i, RESULT_LOAD_IMAGE);
                    break;
                case R.id.signUpButton:
                    // Indicates if an invalid input has occurred
                    // 0 means at least one error has occurred
                    int errorIndicator = 0;

                    if (firstNameET.testValidity()) { errorIndicator++; }
                    if (lastNameET.testValidity()) { errorIndicator++; }
                    if (ageET.testValidity()) { errorIndicator++; }
                    if (emailET.testValidity()) { errorIndicator++; }
                    if (descriptionET.testValidity()) { errorIndicator++; }
                    if (passwordET.testValidity()) { errorIndicator++; }
                    if (confirmPasswordET.testValidity()) { errorIndicator++; }
                    break;

            }
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sign_up, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case RESULT_LOAD_IMAGE:
                if(resultCode == Activity.RESULT_OK){
                    Uri selectedImage = data.getData();
                    String[] filePathColumn = { MediaStore.Images.Media.DATA };

                    Cursor cursor = getContentResolver().query(selectedImage,
                            filePathColumn, null, null, null);
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String picturePath = cursor.getString(columnIndex);
                    cursor.close();

                    //perform Crop on the Image Selected from Gallery
                    performCrop(picturePath);
                }
                break;
            case RESULT_CROP:
                if(resultCode == Activity.RESULT_OK){
                    Bundle extras = data.getExtras();
                    Bitmap selectedBitmap = extras.getParcelable("data");

                    // Set The Bitmap Data To ImageView
                    profilePicture.setImageBitmap(selectedBitmap);
                    profilePicture.setScaleType(ImageView.ScaleType.FIT_XY);
                }
                break;
        }
    }

    // The image crop method
    private void performCrop(String picUri) {
        try {
            //Start Crop Activity
            Intent cropIntent = new Intent("com.android.camera.action.CROP");

            // indicate image type and Uri
            File f = new File(picUri);
            Uri contentUri = Uri.fromFile(f);

            cropIntent.setDataAndType(contentUri, "image/*");
            // set crop properties
            cropIntent.putExtra("crop", "true");
            // indicate aspect of desired crop
            cropIntent.putExtra("aspectX", 1);
            cropIntent.putExtra("aspectY", 1);
            // indicate output X and Y
            cropIntent.putExtra("outputX", 280);
            cropIntent.putExtra("outputY", 280);

            // retrieve data on return
            cropIntent.putExtra("return-data", true);
            // start the activity - we handle returning in onActivityResult
            startActivityForResult(cropIntent, RESULT_CROP);
        }
        // respond to users whose devices do not support the crop action
        catch (Exception e) {
            // display an error message
            Toast.makeText(this, getResources().getString(R.string.error_device_not_supported),
                    Toast.LENGTH_SHORT).show();
        }
    }
}
