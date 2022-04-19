package com.example.systeminfo;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.BatteryManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_LOCATION = 1;
    private FusedLocationProviderClient mFusedLocationClient;
    protected static final int REQUEST_CHECK_SETTINGS = 0x1;

    private Task ll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                getBatteryInfo();
                getConnectionInfo();
            }
        });

        System.out.println("xxxxx ONCREATE");

        // tee luokka kuten edellisisätkin kyselyistä eli uudellenköäytettäviä ominaisuuksia
        //FusedLocationProviderClient
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

            //FusedLocationProviderClient mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);


                ll = mFusedLocationClient.getLastLocation();

                ll.addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {

                        System.out.println("xxxxx SUCCESS");
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {

                            System.out.println("xxxxx " + Double.toString(location.getLatitude()) + "   " + Double.toString(location.getLongitude()));
                            // Logic to handle location object
                        }
                        else{

                            System.out.println("xxxxx " + "LOCATION= NULL");

                        }
                    }
                });

                ll.addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        System.out.println("xxxxx "+e.getMessage());
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

    private void getBatteryInfo(){

        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = this.registerReceiver(null, ifilter);
        int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

        float batteryPct = level / (float)scale;
        System.out.println("xxxxx "+Float.toString(batteryPct)+"  level=  "+Integer.toString(level)+"  scale= "+Integer.toString(scale));
    }

    private void getConnectionInfo(){

        ConnectivityManager cm =
                (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        if(isConnected){
            boolean isWiFi = activeNetwork.getType() == ConnectivityManager.TYPE_WIFI;
           // if(isWiFi){

                System.out.println("xxxxx Wifi= "+Boolean.toString(isWiFi));
           // }

            Boolean isMobile = activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE;
            System.out.println("xxxxx Mobile= "+Boolean.toString(isMobile));
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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


    // tätä ei kutsuta...
    public void sendMessage(View view) {
        // Do something in response to button
        System.out.println("xxxxx");
    }
}
