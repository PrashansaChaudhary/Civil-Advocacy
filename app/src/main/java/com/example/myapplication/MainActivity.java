package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.Manifest;
import android.location.Address;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import Util.Helper;
import civicinfo.CivilAdvocacy;
import civicinfo.NormalizedInput;
import civicinfo.Officials;

/*
 * Author : Prashansa Chaudhary
 * */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private  String civilAdvocacyURL = "https://www.googleapis.com/civicinfo/v2/representatives";
    private static final String TAG = "MainActivity";
    private RequestQueue queue;
    private CivilAdvocacy civilAdvocacy;
    String address ;
    TextView curLocation;
    RecyclerView recyclerView;
    NormalizedInput normalizedInput;
    ArrayList< Officials > officials;
    private FusedLocationProviderClient mFusedLocationClient;
    private static final int LOCATION_REQUEST = 111;
    private static String locationString = "Mountain View,CA";
    boolean checkGPS = false;
    Double latitude = 0.0 , longitude = 0.0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(getString(R.string.application_name));
        setContentView(R.layout.activity_main);
        curLocation = findViewById(R.id.currentLocation);
        recyclerView = findViewById(R.id.governList);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        address = determineLocation();
        getAdvocacyData(address);
    }

    @SuppressLint("MissingPermission")
    private String determineLocation() {
        if (checkAppPermissions()) {
            mFusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, location -> {
                        // Got last known location. In some situations this can be null.
                        if (location != null) {
                            locationString = getPlace(location);

                            Log.d(TAG, "determineLocation: Location String "+ locationString);
                        }
                        else{
                            locationString ="Mountain View,CA";
                        }
                    })
                    .addOnFailureListener(this, e -> Toast.makeText(MainActivity.this,
                            e.getMessage(), Toast.LENGTH_LONG).show());
        }
        Log.d(TAG, "determineLocation: "+locationString);
        return locationString;
    }

    private boolean checkAppPermissions() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED) {
            return true;
            }

        ActivityCompat.requestPermissions(this,
                new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION
                }, LOCATION_REQUEST);
        return false;
        }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == LOCATION_REQUEST) {
            if (permissions[0].equals(Manifest.permission.ACCESS_FINE_LOCATION)) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    determineLocation();
                    getAdvocacyData(address);
                }
            }
        }
    }
    private String getPlace(Location loc) {

        StringBuilder sb = new StringBuilder();

        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses;

        try {
            addresses = geocoder.getFromLocation(loc.getLatitude(), loc.getLongitude(), 1);
            String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            sb.append(String.format(
                    Locale.getDefault(),
                    "%s, %s", city, state));
            this.latitude = loc.getLatitude();
            this.longitude = loc.getLongitude();
            address = addresses.get(0).getAddressLine(0);
            getAdvocacyData(address);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    // Need to get data from API?
    private void getAdvocacyData(String address) {
        if(Helper.isNetworkConnected(this)){
            queue = Volley.newRequestQueue(getApplicationContext());
            // Building URL for API
            String url = Helper.buildURL(civilAdvocacyURL, address);
            JsonObjectRequest jsonObjectRequest =
                    new JsonObjectRequest(Request.Method.GET, url,
                            null, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            officials  = Helper.parseJSON(response.toString());
                            normalizedInput = Helper.normalizedInput(response.toString());
                            civilAdvocacy = new CivilAdvocacy(normalizedInput, null, null, officials);
                            setCivilAdvocacyData(civilAdvocacy);
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            //Toast.makeText(MainActivity.this, "" + error.toString(), Toast.LENGTH_LONG).show();
                        }
                    });
            // Add the request to the RequestQueue.
            queue.add(jsonObjectRequest);
        } else {
            curLocation.setText("No Data For Location");
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setIcon(R.drawable.error);
            builder.setTitle("No Network Connection");
            builder.setMessage("Data cannot be accessed/loaded without an internet connection.");
            AlertDialog dialog = builder.create();
            dialog.show();
            Toast.makeText(MainActivity.this, "No Network available" , Toast.LENGTH_LONG).show();
        }
    }

    private void setCivilAdvocacyData(CivilAdvocacy civilAdvocacy) {
        if(civilAdvocacy == null){
            Toast.makeText(this, "No data found for your location", Toast.LENGTH_LONG).show();

        } else{
            if(civilAdvocacy.getNormalizedInputObject().getLine1().isEmpty()){
                curLocation.setText(civilAdvocacy.getNormalizedInputObject().getCity() + ", " +civilAdvocacy.getNormalizedInputObject().getState());
            } else {
                String zipCode = civilAdvocacy.getNormalizedInputObject().getZip();
                curLocation.setText(civilAdvocacy.getNormalizedInputObject().getLine1()+", "
                        +civilAdvocacy.getNormalizedInputObject().getCity() + ", "
                        +civilAdvocacy.getNormalizedInputObject().getState()+" "+zipCode);
            }
            AdvocacyAdapter advocacyAdapter = new AdvocacyAdapter(MainActivity.this, null, civilAdvocacy.getOfficials());
            recyclerView.setAdapter(advocacyAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
            advocacyAdapter.notifyDataSetChanged();
        }

    }

    // Menu Items Functionality
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_items, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.aboutActivity){
            Intent aboutIntent = new Intent(this, AboutActivity.class);
            startActivity(aboutIntent);
            return true;
        }
        if(item.getItemId() == R.id.searchLocation){
            changeLocation();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void changeLocation() {
        if(Helper.isNetworkConnected(this)) {
            LayoutInflater inflater = LayoutInflater.from(this);
            final View view = inflater.inflate(R.layout.change_location, null);
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Enter Address");
            builder.setView(view);

            builder.setPositiveButton("OK", (dialog, id) -> {
                EditText alertDialogLocation = view.findViewById(R.id.changeLocation);
                address = alertDialogLocation.getText().toString();
                getAdvocacyData(address);
            });


            builder.setNegativeButton("CANCEL", (dialog, id) -> {
                Toast.makeText(MainActivity.this, "Don't want to change the place?",
                        Toast.LENGTH_SHORT).show();
            });
            AlertDialog dialog = builder.create();

            dialog.show();
        } else {
            Toast.makeText(this, "No Network Connection", Toast.LENGTH_SHORT).show();

        }
    }

    // On item click listener -> Recycler view items
    @Override
    public void onClick(View view) {
        int position = recyclerView.getChildAdapterPosition(view);
        Officials officials =  this.officials.get(position);
        Intent in = new Intent(this, IndividualOfficials.class);
        in.putExtra("officials", officials);
        in.putExtra("locationName", civilAdvocacy.getNormalizedInputObject());
        view.getContext().startActivity(in);
    }

}