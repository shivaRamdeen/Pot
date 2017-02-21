package com.diamondstudios.potholelocator;

import android.*;
import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.loopj.android.http.*;

import java.io.UnsupportedEncodingException;

import cz.msebera.android.httpclient.Header;

public class mainMapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    LocationManager locationManager;
    android.location.LocationListener locationListener;
    long LOCATION_UPDATE_TIME = 5000;
    double myLong = 0;
    double myLat = 0;
    FloatingActionButton fabbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //acquire reference to system location hardware
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //get button instance
        fabbtn = (FloatingActionButton) findViewById(R.id.floatingActionButton2);
       /*locationListener = new android.location.LocationListener(){
           @Override
           public void onLocationChanged(Location location){

           }

           @Override
           public void onStatusChanged(String provider, int status,Bundle extras){

           }

           @Override
           public void onProviderEnabled(String provider){

           }

           @Override
           public void onProviderDisabled(String provider){

           }
       };*/


        locationListener = new android.location.LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                myLat = location.getLatitude();
                myLong = location.getLongitude();
                //enable button when location is updated
                //fabbtn.setEnabled(true);
            }

            @Override
            public void onStatusChanged(String provider, int status,Bundle extras){

            }

            @Override
            public void onProviderEnabled(String provider){

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };



        //register the listener with the location manager to reciev location updates
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.LOCATION_HARDWARE}, 10);
            return;
        }else{
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        };





    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[],int[] grantResult){
        switch(requestCode) {
            case 10:
            {
                if (grantResult.length > 0 && grantResult[0] == PackageManager.PERMISSION_GRANTED) {
                    //we have permission to use location services
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, (android.location.LocationListener) locationListener);

                }else {
                    //permission denied
                    finish(); //or end the application
                }
                return;
            }
        }
    }




    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //check if gps location is valid
        if(myLat == 0 && myLong == 0)
        {

            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, (android.location.LocationListener) locationListener);
            LatLng myLocation = new LatLng(myLat, myLong);
            mMap.addMarker(new MarkerOptions().position(myLocation).title("You"));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(myLocation));

        }else {
            // Add a marker in Trinidad and move the camera
            LatLng trini = new LatLng(10.6918, -61.2225);
            mMap.addMarker(new MarkerOptions().position(trini).title("Marker in Trinidad"));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(trini));
        }
    }

    public void updateLocation(View view){
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, (android.location.LocationListener) locationListener);
        LatLng myLocation = new LatLng(myLat, myLong);
        mMap.addMarker(new MarkerOptions().position(myLocation).title("You"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(myLocation));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15.0f));
        //start form to submit information
        Intent intent = new Intent(this, newPtForm.class);
        startActivity(intent);
    };

    public void httpRequest(View view)
    {
        String URL = "http://ip-api.com/json";
        //String resp;
        final TextView tv_resp = (TextView) findViewById(R.id.tv_json);
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(URL, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String resp = new String(responseBody,"UTF-8");
                    tv_resp.setText(resp);
                } catch (UnsupportedEncodingException e) {
                    tv_resp.setText("Unsupported Encoding When Converting byte to string");
                    //e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                tv_resp.setText(String.format("Request failed,  %d",statusCode));

            }
        });

    };
}

