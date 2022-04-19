package com.example.systeminfo;

import android.Manifest;
import android.app.ActivityManager;
import android.app.admin.DevicePolicyManager;
//import android.os.CpuUsageInfo;
//import android.os.HardwarePropertiesManager;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import android.location.Location;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Timer;
import java.util.TimerTask;




public class MainActivity extends AppCompatActivity {

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    protected static final int REQUEST_CHECK_SETTINGS = 0x1;
    private static final int REQUEST_LOCATION = 1;
    //
    private int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 0;
    //protected static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS=1;

    //private Timer timer;
    private FusedLocationProviderClient mFusedLocationClient;
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
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                //        .setAction("Action", null).show();

                //getBatteryInfo();
                //            getConnectionInfo();
                //boolean ret=checkLocationPermission();
                //statusCheck();
                //getBuildInfo();
                //String ret = ReadCPUinfo();

                //float cpuT = cpuTemperature();
                //Log.v("xxx", "cpuTemperature= " + Float.toString(cpuT));
                //float batT = batteryTemperature(getApplicationContext());
                //Log.v("xxx", " batteryTemperature= " + );

                Integer[] _val = getFreeRAM();
                Log.v("xxx", "mem avail= " + Integer.toString(_val[0]));
                Log.v("xxx", "mem total= " + Integer.toString(_val[1]));


                //            getPermission();

                new GetLocation(view);
                //getLocation();
                getBatteryInfo();
                getConnectionInfo();

                final TextView cpuTemp = findViewById(R.id.cpuTemp);
                final TextView batTemp = findViewById(R.id.batTemp);
                final TextView batLevel = findViewById(R.id.batLevel);

/*
                Timer timer = new Timer();
                timer.scheduleAtFixedRate(new TimerTask() {

                                              @Override
                                              public void run() {


                                                  try {

                                                      runOnUiThread(new Runnable() {
                                                          public void run() {
                                                              String cpuTempString = Float.toString(cpuTemperature());
                                                              String batTempString = Float.toString(batteryTemperature());
                                                              String batLevelString = Float.toString(batteryLevel());
                                                              cpuTemp.setText(cpuTempString);
                                                              batTemp.setText(batTempString);
                                                              batLevel.setText(batLevelString);
                                                              Log.v("xxx", cpuTempString + "   " + batTempString + "   " + batLevelString);
                                                          }
                                                      });


                                                  } catch (Exception ex) {
                                                      Log.v("error", "Error: " + ex.getMessage());
                                                  }
                                              }
                                          },
                        0, 1000);
*/
            }
        });


//*************************************************
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
                for (Location location : locationResult.getLocations()) {
                    if (location != null) {
                        //TODO: UI updates.
                    }
                }
            }
        };
        //Context context=activi
        FusedLocationProviderClient mFusedLocationClient;
        mFusedLocationClient=LocationServices.getFusedLocationProviderClient(this); //.requestLocationUpdates(mLocationRequest, mLocationCallback, null);
        mFusedLocationClient.requestLocationUpdates(mLocationRequest,mLocationCallback,null);
//*************************************************
        getLocation();



        System.out.println("xxxxx ONCREATE____________");

        // tee luokka kuten edellisisätkin kyselyistä eli uudellenköäytettäviä ominaisuuksia
        //FusedLocationProviderClient


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            System.out.println("xxxxx CHECK PERMISSION");
            // Check Permissions Now
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION);
        }

    }

    private float batteryTemperature() {

        Context context = getApplicationContext();
        Intent intent = context.registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        float temp = ((float) intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, 0)) / 10;
        return temp; ///String.valueOf(temp) + "*C";
    }

    private float cpuTemperature() {
        Process process;
        try {
            process = Runtime.getRuntime().exec("cat sys/class/thermal/thermal_zone0/temp");
            //process = Runtime.getRuntime().exec("cat /sys/devices/virtual/thermal/*/type");
            process.waitFor();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line = reader.readLine();
            if (line != null) {
                float temp = Float.parseFloat(line);
                return temp / 1000.0f;
            } else {
                return 51.0f;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return 0.0f;
        }
    }


    private String ReadCPUinfo() {
        ProcessBuilder cmd;
        String result = "";

        try {

            Log.v("xxx", "ReadCPUINFO");
            String[] args = {"/system/bin/cat", "/proc/cpuinfo"};
            cmd = new ProcessBuilder(args);

            Process process = cmd.start();
            InputStream in = process.getInputStream();
            byte[] re = new byte[1024];
            while (in.read(re) != -1) {
                Log.v("xxx", new String(re));
                result = result + new String(re);
            }
            in.close();
        } catch (IOException ex) {
            Log.v("xxx", ex.getMessage());
            ex.printStackTrace();
        }
        return result;
    }


    private void getBuildInfo() {

        Log.i("xxx", "SERIAL: " + Build.SERIAL);
        Log.i("xxx", "MODEL: " + Build.MODEL);
        Log.i("xxx", "ID: " + Build.ID);
        Log.i("xxx", "Manufacture: " + Build.MANUFACTURER);
        Log.i("xxx", "brand: " + Build.BRAND);
        Log.i("xxx", "type: " + Build.TYPE);
        Log.i("xxx", "user: " + Build.USER);
        Log.i("xxx", "BASE: " + Build.VERSION_CODES.BASE);
        Log.i("xxx", "INCREMENTAL " + Build.VERSION.INCREMENTAL);
        Log.i("xxx", "SDK  " + Build.VERSION.SDK);
        Log.i("xxx", "BOARD: " + Build.BOARD);
        Log.i("xxx", "BRAND " + Build.BRAND);
        Log.i("xxx", "HOST " + Build.HOST);
        Log.i("xxx", "FINGERPRINT: " + Build.FINGERPRINT);
        Log.i("xxx", "Version Code: " + Build.VERSION.RELEASE);

    }


    public void statusCheck() {
        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();

        }
    }

        private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    private void getPermission() {

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_CONTACTS)) {

                Log.v("xxx", "#1");
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_READ_CONTACTS);

                Log.v("xxx", "#2");
                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            // Permission has already been granted
        }


    }


    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            Log.v("xxx", "#1");
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                Log.v("xxx", "#2");
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(this)
                        .setTitle("heippa")
                        .setMessage("hola hola")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(MainActivity.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();


            } else {

                Log.v("xxx", "heipparallaa");
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            Log.v("xxx", "#3");
            return true;
        }
    }

    public Integer[] getFreeRAM() {
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        activityManager.getMemoryInfo(mi);
        Integer[] mem = {(int) (mi.availMem / 1048576L), (int) (mi.totalMem / 1048576L)};

        return mem;
    }


    //
    /*
    public String getCPUUsage(int pid) {
        Process p;
        try {
            String[] cmd = {
                    "sh",
                    "-c",
                    "top -m 1000 -d 1 -n 1 | grep \"" + pid + "\" "};
            p = Runtime.getRuntime().exec(cmd);
            String line = reader.readLine();

            return line;
            // line contains the process info
        } catch (Exception ex) {

            return "";
        }

    }*/

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



        private void xxxgetLocation() {

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


        private float batteryLevel () {
            IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
            Intent batteryStatus = this.registerReceiver(null, ifilter);
            float level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);

            return level;

        }
    private int getBatteryInfo () {

        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = this.registerReceiver(null, ifilter);
        int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

        float batteryPct = level / (float) scale;
        System.out.println("xxxxx " + Float.toString(batteryPct) + "  level=  " + Integer.toString(level) + "  scale= " + Integer.toString(scale));

        return level;

    }


        private void getConnectionInfo () {

            ConnectivityManager cm =
                    (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            boolean isConnected = activeNetwork != null &&
                    activeNetwork.isConnectedOrConnecting();

            if (isConnected) {
                boolean isWiFi = activeNetwork.getType() == ConnectivityManager.TYPE_WIFI;
                // if(isWiFi){

                System.out.println("xxxxx Wifi= " + Boolean.toString(isWiFi));
                // }

                Boolean isMobile = activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE;
                System.out.println("xxxxx Mobile= " + Boolean.toString(isMobile));
            }

        }

        @Override
        public boolean onCreateOptionsMenu (Menu menu){
            // Inflate the menu; this adds items to the action bar if it is present.
            getMenuInflater().inflate(R.menu.menu_main, menu);
            return true;
        }

        @Override
        public boolean onOptionsItemSelected (MenuItem item){
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
        public void sendMessage (View view){
            // Do something in response to button
            System.out.println("xxxxx");
        }
    }
