package com.thl.gtwmob.helper;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import android.telephony.TelephonyManager;
import static android.content.Context.ACTIVITY_SERVICE;

public class SystemInfo {


    private static Handler handler;
    private TelephonyManager telephonyManager;

    public static int _signalStrength;
    private final static String LTE_SIGNAL_STRENGTH = "getLteSignalStrength";
    public static SignalStrength signalStrength;

    public static String getNetworkClass(Context context) {
        TelephonyManager mTelephonyManager = (TelephonyManager)
                context.getSystemService(Context.TELEPHONY_SERVICE);
        int networkType = mTelephonyManager.getNetworkType();

        Log.v("xxx", "NetworkType=" + Integer.toString(networkType));
        switch (networkType) {
            case TelephonyManager.NETWORK_TYPE_GPRS:
                return "GPRS";
            case TelephonyManager.NETWORK_TYPE_EDGE:
            case TelephonyManager.NETWORK_TYPE_CDMA:
            case TelephonyManager.NETWORK_TYPE_1xRTT:
            case TelephonyManager.NETWORK_TYPE_IDEN:
                return "2G";
            case TelephonyManager.NETWORK_TYPE_UMTS:
            case TelephonyManager.NETWORK_TYPE_EVDO_0:
            case TelephonyManager.NETWORK_TYPE_EVDO_A:
            case TelephonyManager.NETWORK_TYPE_HSDPA:
                return "3G/HSDPA";
            case TelephonyManager.NETWORK_TYPE_HSUPA:
                return "3G/HSUPA";
            case TelephonyManager.NETWORK_TYPE_HSPA:
                return "3G/HSPA";
            case TelephonyManager.NETWORK_TYPE_EVDO_B:
                return "3G/EVDO_B";
            case TelephonyManager.NETWORK_TYPE_EHRPD:
                return "3G/EHRPD";
            case TelephonyManager.NETWORK_TYPE_HSPAP:
                return "3G/HSPAP";
            case TelephonyManager.NETWORK_TYPE_LTE:
                return "4G/LTE";
            default:
                return "Unknown";
        }
    }

    public static int getSignalStrength(Context context,String netWorkType) {

        if (netWorkType != "4G/LTE"){

          return 0;
        }
        Looper.prepare();
        //***************
        Log.v("Looper", "##2---------------");
        handler = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                Log.v("Looper", "##6---------------" + msg.toString());
                throw new RuntimeException();

            }
        };
//***************

        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

        // Listener for the signal strength.
        PhoneStateListener mListener = new PhoneStateListener() {

            @Override
            public void onSignalStrengthsChanged(SignalStrength sStrength) {
                signalStrength = sStrength;
                Log.v("Looper","##3 onSignalStrength "+signalStrength.toString());
                _signalStrength=getLTEsignalStrength();
                //*******************
                Log.v("Looper", "##4--------------- "+signalStrength);
                handler.sendMessage(handler.obtainMessage());
                Log.v("Looper", "##5--------------- "+_signalStrength);
//*******************
            }
        };

        // Register the listener for the telephony manager
        telephonyManager.listen(mListener, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);

        //******************
        if (handler != null) {
            try {
                Log.v("Looper", "#3---------------");
                Looper.loop();
                Log.v("Looper", "#3333---------------");
            } catch (
                    RuntimeException e) {
            }
        }
//******************
        Log.v("Looper","##7 return");
        telephonyManager.listen(mListener, PhoneStateListener.LISTEN_NONE);

        return  _signalStrength;
    }


    public static int getLTEsignalStrength()
    {
        try
        {
            Method[] methods = android.telephony.SignalStrength.class.getMethods();

            for (Method mthd : methods)
            {
                if (mthd.getName().equals(LTE_SIGNAL_STRENGTH))
                {
                    int LTEsignalStrength = (Integer) mthd.invoke(signalStrength, new Object[] {});
                    Log.v("xxx", "XXXX signalStrength = " + LTEsignalStrength);
                    return LTEsignalStrength;
                }
            }

            return -1;
        }
        catch (Exception e)
        {
            Log.e("xxx", "Exception: " + e.toString());

        }

        return -1;
    }


    public static float batteryTemperature(Context context) {

        Intent intent = context.registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        float temp = ((float) intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, 0)) / 10;
        return temp;
    }
    public static float batteryLevel(Context context) {
        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = context.registerReceiver(null, ifilter);
        float level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);

        return level;

    }

    public static int getBatteryInfo(Context context) {

        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = context.registerReceiver(null, ifilter);
        int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

        float batteryPct = level / (float) scale;
        System.out.println("xxxxx " + Float.toString(batteryPct) + "  level=  " + Integer.toString(level) + "  scale= " + Integer.toString(scale));

        return level;

    }

    public static float cpuTemperature() {

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

    public static Integer[] getFreeRAM(Context context) {
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        ActivityManager activityManager = (ActivityManager)context.getSystemService(ACTIVITY_SERVICE);
        activityManager.getMemoryInfo(mi);
        Integer[] mem = {(int) (mi.availMem / 1048576L), (int) (mi.totalMem / 1048576L)};

        return mem;
    }

    public static Boolean[] getConnectionInfo(Context context) {

        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

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

            return new Boolean[]{isWiFi, isMobile};

        } else {

            return new Boolean[]{false, false};

        }

    }

    public static void statusCheck(Context context) {
        final LocationManager manager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps(context);

        }
    }

    public static void buildAlertMessageNoGps(Context _context) {

        final Context context=_context;
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        context.startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
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

    public static String ReadCPUinfo() {

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


    public static void getBuildInfo() {

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


}
