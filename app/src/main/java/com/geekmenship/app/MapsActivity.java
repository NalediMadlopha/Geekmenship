package com.geekmenship.app;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.geekmenship.model.Event;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends ActionBarActivity {

    private ProgressDialog progressDialog;
    private GoogleMap mMap;
    private Location location = null;
    private List<Address> addresses = null;
    private final String TAG = MapsActivity.this.getClass().getSimpleName();
    private String errorMessage = "";
    public Event event;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.location_fragment);

        Intent intent = getIntent();
        event = (Event) intent.getSerializableExtra("newEvent");

        progressDialog = new ProgressDialog(MapsActivity.this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("Loading the map...");
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(true);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_map, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_accept_map) {

            if (addresses == null || addresses.size()  == 0) {
                Toast.makeText(this, getString(R.string.no_address_found),
                        Toast.LENGTH_LONG).show();
            } else {
                Intent locationSelectionIntent = new Intent();
                locationSelectionIntent.putExtra("newEvent", event);
                setResult(Activity.RESULT_OK, locationSelectionIntent);
                finish();
            }

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    @Override
    protected void onPause() { super.onPause(); }

    @Override
    protected void onDestroy() { super.onDestroy(); }

    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    private void setUpMap() {
       mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                getAddress(latLng.latitude, latLng.longitude);
            }
       });
    }

    public void markLocation(double latitude, double longitude) {

        mMap.clear(); // Clears the map

        // Creates the marker
        MarkerOptions marker = new MarkerOptions().position(
            new LatLng(latitude, longitude)).title(addresses.get(0).getAddressLine(0)
                + ", " + addresses.get(0).getLocality()
                + ", " + addresses.get(0).getCountryCode());

        // Changing marker icon
        marker.icon(BitmapDescriptorFactory
            .defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));

        // Adds a marker
        mMap.addMarker(marker);
        CameraPosition cameraPosition = new CameraPosition.Builder()
            .target(new LatLng(latitude, longitude)).zoom(16.0f).build();

        // Sets the camera position
        mMap.animateCamera(CameraUpdateFactory
            .newCameraPosition(cameraPosition));
    }

    public void getAddress(double lat, double lng) {
        // Declares a Geocoder object
        Geocoder geocoder = new Geocoder(MapsActivity.this, Locale.getDefault());

        try {
            // Determine whether a Geocoder is available.
            if (!Geocoder.isPresent()) {
                Toast.makeText(this, R.string.no_geocoder_available,
                        Toast.LENGTH_LONG).show();
            } else {
                addresses = geocoder.getFromLocation(lat, lng, 1);
            }
        } catch (IOException ioException) {
            // Catch network or other I/O problems.
            errorMessage = getString(R.string.service_not_available);
            Log.e(TAG, errorMessage, ioException);
        } catch (IllegalArgumentException illegalArgumentException) {
            // Catch invalid latitude or longitude values.
            errorMessage = getString(R.string.invalid_lat_long_used);
            Log.e(TAG, errorMessage + ". " +
                    "Latitude = " + location.getLatitude() +
                    ", Longitude = " +
                    location.getLongitude(), illegalArgumentException);
        }

        // Handle case where no address was found.
        if (addresses == null || addresses.size()  == 0) {
            Toast.makeText(this, getString(R.string.no_address_found),
                    Toast.LENGTH_LONG).show();
        } else if (addresses.get(0).getLocality() == null) {
            // Checks if there is a suburb name
            mMap.clear();
            addresses = null;
            Toast.makeText(this, getString(R.string.no_suburb_name),
                    Toast.LENGTH_LONG).show();
        } else {
            // Marks a selected location
            markLocation(lat, lng);

            event.setVenue(addresses.get(0).getLocality()
                + ", " + addresses.get(0).getCountryCode());

        }
    }
}
