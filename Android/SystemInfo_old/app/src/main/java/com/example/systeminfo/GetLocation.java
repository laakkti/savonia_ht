package com.example.systeminfo;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;

import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import static com.example.systeminfo.MainActivity.REQUEST_CHECK_SETTINGS;

public class GetLocation {

    private static final int REQUEST_LOCATION = 1;
    private Task ll;
    private FusedLocationProviderClient mFusedLocationClient;


    public GetLocation(View view){

        //Log.v(view);
        //Context context=view.getContext();
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(view.getContext());
        //context.toString());
        //getApplicationContext()

        if (ActivityCompat.checkSelfPermission(view.getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            System.out.println("xxxxx CHECK PERMISSION");
            // Check Permissions Now
            ActivityCompat.requestPermissions((Activity)view.getContext(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION);
        } else {
            System.out.println("xxxxx ELSE");

            //Context context=getContext();
            //FusedLocationProviderClient mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

            ll = mFusedLocationClient.getLastLocation();

            ll.addOnSuccessListener((Activity)view.getContext(), new OnSuccessListener<android.location.Location>() {
                @Override
                public void onSuccess(android.location.Location location) {

                    System.out.println("xxxxx SUCCESS");
                    // Got last known location. In some rare situations this can be null.
                    if (location != null) {

                        System.out.println("xxxxx " + "lat= "+Double.toString(location.getLatitude()) + "   " + "long= "+Double.toString(location.getLongitude()));
                        // Logic to handle location object
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

        }

    }

    /*
    private void getLocation() {

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            System.out.println("xxxxx CHECK PERMISSION");
            // Check Permissions Now
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION);
        } else {
            System.out.println("xxxxx ELSE");

            Context context=getContext();
            //FusedLocationProviderClient mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

            ll = mFusedLocationClient.getLastLocation();

            ll.addOnSuccessListener(this, new OnSuccessListener<android.location.Location>() {
                @Override
                public void onSuccess(android.location.Location location) {

                    System.out.println("xxxxx SUCCESS");
                    // Got last known location. In some rare situations this can be null.
                    if (location != null) {

                        System.out.println("xxxxx " + "lat= "+Double.toString(location.getLatitude()) + "   " + "long= "+Double.toString(location.getLongitude()));
                        // Logic to handle location object
                    } else {

                        System.out.println("xxxxx " + "LOCATION= NULL");
                    }
                }
            });

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


