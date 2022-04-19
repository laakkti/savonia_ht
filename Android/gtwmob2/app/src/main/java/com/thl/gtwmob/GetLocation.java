package com.thl.gtwmob;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;


public class GetLocation {

    private static final int REQUEST_LOCATION = 1;
    private Task ll;
    private FusedLocationProviderClient mFusedLocationClient;

    private Double[] location=null;
    private Callback callback;

    //private Handler handler = null;


    public GetLocation(){

//        Looper.prepare();
    }
    //public Double[] getLocation(View view, Callback callback) {


    public Double[] getLocation(View view, Callback callback) {

//***************
        /*
        Log.v("Looper", "#2---------------");
        handler = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                Log.v("Looper", "#7---------------" + msg.toString());
                throw new RuntimeException();

            }
        };
        */
//***************
        this.callback = callback;
        init(view.getContext());
        //Log.v(view);
        //Context context=view.getContext();
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(view.getContext());
        //context.toString());
        //getApplicationContext()

        if (ActivityCompat.checkSelfPermission(view.getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            System.out.println("xxxxx CHECK PERMISSION");
            // Check Permissions Now
            ActivityCompat.requestPermissions((Activity) view.getContext(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION);
        } else {
            System.out.println("xxxxx ELSE");

            //Context context=getContext();
            //FusedLocationProviderClient mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

            ll = mFusedLocationClient.getLastLocation();

            ll.addOnSuccessListener((Activity) view.getContext(), new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {

                    System.out.println("xxxxx SUCCESS");
                    // Got last known location. In some rare situations this can be null.
                    if (location != null) {

                        ready(new Double[]{location.getLatitude(), location.getLongitude()});

//*******************
                      /*
                        Log.v("Looper", "#6---------------");
                        handler.sendMessage(handler.obtainMessage());
                        Log.v("Looper", "#8---------------");
                        */
//*******************


                    } else {

                        System.out.println("xxxxx " + "LOCATION= NULL");
                    }
                }
            });

            ll.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                    System.out.println("xxxxx " + e.getMessage());

                }
            });

//******************
            /*
            if (handler != null) {
                try {
                    Log.v("Looper", "#3---------------");
                    Looper.loop();
                    Log.v("Looper", "#3333---------------");
                } catch (
                        RuntimeException e) {
                }

            }
            */
//******************

        }

        return location;
    }

        private void ready (Double[] _location){

            location=_location;

            Log.v("Looper", "#4---------------");
            //Log.v("###", "XXXXXXXXXXXXXXXXXXXXXXX " + location[0].toString());
            this.callback.callbackLocation("location", location);
        }

        private void init (Context context){

            LocationRequest mLocationRequest = LocationRequest.create();
            mLocationRequest.setInterval(60000);
            mLocationRequest.setFastestInterval(5000);
            mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            LocationCallback mLocationCallback = new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult) {
                    if (locationResult == null) {
                        return;
                    }
                    int cnt = 0;

                    for (Location location : locationResult.getLocations()) {
                        if (location != null) {
                            //TODO: UI updates.

                            Log.v("LocationInit", "location=" + location.toString());
                        }
                        cnt++;
                        Log.v("LocationInit", "cnt=" + cnt);
                    }
                }
            };

            FusedLocationProviderClient mFusedLocationClient;
            mFusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
            //mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, null);

        }

        public void heippa () {

            Log.v("xxx", "¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤");
            //return true;
        }


    /*
            ll.addOnFailureListener(this, new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                    System.out.println("xxxxx " + e.getMessage());
                    if (e instanceof ResolvableApiException) {
                        // Location settings are not satisfied, but this can be fixed
                        // by showing the user a dialog.
                        try {
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().
                            ResolvableApiException resolvable = (ResolvableApiException) e;
                            resolvable.startResolutionForResult(MainActivity.this,
                                    REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException sendEx) {
                            // Ignore the error.
                        }
                    }
                }
            });
        }
    }
*/


}


