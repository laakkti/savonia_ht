package com.thl.gtwmob;

import java.net.URISyntaxException;
import java.util.Timer;

import android.Manifest;
import android.accounts.AccountManager;
import android.app.AlertDialog;
import android.app.Dialog;
//import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Looper;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
//import android.view.Menu;
//import android.view.MenuItem;
//import android.widget.EditText;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.tasks.Task;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
//import com.google.api.client.googleapis.util.Utils;
import com.google.api.client.util.ExponentialBackOff;
import com.google.api.services.gmail.GmailScopes;
//import com.google.api.services.gmail.model.MessagePart;

//import java.util.ArrayList;
import java.util.Arrays;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;

import com.google.gson.Gson;
import com.thl.gtwmob.helper.InternetDetector;
import com.thl.gtwmob.helper.Utilsx;
import com.thl.gtwmob.helper.SystemInfo;

import org.json.JSONObject;

import static com.thl.gtwmob.helper.Utilsx.REQUEST_PERMISSION_ACCESS_FINE_LOCATION;
import static com.thl.gtwmob.helper.Utilsx.REQUEST_PERMISSION_GET_ACCOUNTS;
import static com.thl.gtwmob.helper.Utilsx.readJSONFromAsset;
import static java.lang.Thread.sleep;


import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;


public class MainActivity extends AppCompatActivity implements Callback {


    private Socket socket;

    //*************************
    private String token;
    private String socketId;
    private String sourceUrl;
    private String title;
    private String data;



    //*************************

    // tömä saadaan  emaililla
    private String myUrl = "http://192.168.43.155:3001";

    private Timer _timer;

    private int taskCnt = 0;
    private int taskTotal = 0;

    public static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 1004;
    private static Handler handler;
    FloatingActionButton sendFabButton;
    private GoogleAccountCredential mCredential;
    //private static final String PREF_ACCOUNT_NAME = "accountName";
    private String PREF_ACCOUNT_NAME; // = "gtw.mob@gmail.com";
    private static final String[] SCOPES = {
            GmailScopes.GMAIL_LABELS,
            GmailScopes.GMAIL_COMPOSE,
            GmailScopes.GMAIL_INSERT,
            GmailScopes.GMAIL_MODIFY,
            GmailScopes.GMAIL_READONLY,
            GmailScopes.MAIL_GOOGLE_COM
    };
    private InternetDetector internetDetector;
    private timerProg tp;
    private timerProg readyTimer;


    private SysInfo info = new SysInfo();
    private boolean working = false;

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    protected static final int REQUEST_CHECK_SETTINGS = 0x1;
    private static final int REQUEST_LOCATION = 1;
    //
    //private int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 0;
    //protected static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS=1;

    private Double[] locationVal;
    //private Timer timer;
    private FusedLocationProviderClient mFusedLocationClient;
    private Task ll;
    private TextView cpuTemp;
    private TextView batTemp;
    private TextView batLevel;
    private TextView locationFld;
    private TextView mem;
    private TextView strength;
    private TextView mobileType;
    private Switch wifi;
    private Switch mobile;
    //private GetLocationPOIS getLocation;

    //***************************
    private SignalStrength signalStrength;
    private TelephonyManager telephonyManager;
    private final static String LTE_TAG = "LTE_Tag";
    private final static String LTE_SIGNAL_STRENGTH = "getLteSignalStrength";

    private GetLocation getLocation;
    private GetLocationPOIS loc;
    //private Handler handler;
    public int _signalStrength;
    private View mainView;
    //***************************
    private TimerClass timer;
    final Handler myHandler = new Handler();

    private TaskQueue taskQueue;

    private void setTextViewReferences() {

        cpuTemp = findViewById(R.id.cpuTemp);
        batTemp = findViewById(R.id.batTemp);
        batLevel = findViewById(R.id.batLevel);
        locationFld = findViewById(R.id.location);
        mem = findViewById(R.id.mem);

        wifi = findViewById(R.id.wifi);
        mobile = findViewById(R.id.mobile);
        mobileType = findViewById(R.id.mobileType);
        strength = findViewById(R.id.strength);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setTextViewReferences();


         //Log.v("xxx","ÖÖÖÖÖÖÖÖÖÖÖÖÖÖÖÖ "+str);
        //loc=new GetLocationPOIS(findViewById(android.R.id.content),MainActivity.this);

        init();
        mainView = findViewById(android.R.id.content);
        getResultsFromApi(mainView);

        //getLocation = new GetLocation();


        FloatingActionButton fab = findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //        Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                //                .setAction("Action", null).show();

                //              msg(view);

                //tp.init(true);

                /*
                Log.v("xxx","Ready to get values");
                SysInfo values=getValues(view);
                Log.v("xxx","Ready , is values read???");
                Log.v("xxx","Update display");
                // kerkeekö arvot kaikki tulla jotta voidaan pöivittäää näyttö
                updateDisplay(values);
                Log.v("xxx","Display updated");
                */
                //String netType=SystemInfo.getNetworkClass(getApplicationContext());
                //int strengthValue=
                //        new GetSignalStrength(getApplicationContext(),netType,MainActivity.this); //.GetValue();
                //Log.v("xxx","(((((((((((((((((((((((((((((((((((( "+Integer.toString(_strengthValue));

                //loc.getStart(view);

                //getLocation.getLocation(view, MainActivity.this);

                //     getResultsFromApi(view);

                //info.cpuTemp=SystemInfo.cpuTemperature();

                //      startReadyTimer();
                //readyTimer.init(true);
                //  timer.stop();


                // völiaikaisesti pois
                tp.init(true);

                /*
                String coords="62.8376,27.6477";
                String sourceUrl="https://api.darksky.net/forecast/ea8a7cd6a76ab7136502dfe91fde6f7b/62.8376,27.6477?units=si&exclude=currently,minutely,daily,alerts,flags";
                Log.v("xxx","#1  "+sourceUrl);

                try {
                    String str=getWeather2(sourceUrl, coords);
                    //String str=getWeather();
                    Log.v("xxx",str);
                }catch(Exception ex){

                    Log.v("xxx","Error: "+ex.getMessage());
                }
               */
            }
        });

        try {
            socket = IO.socket(myUrl);
            socket.connect();
            // tämä saadaan sähköpostilla

            socket.emit("client", "Hyvvee päevee");

        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

            //timer=new TimerClass(88,2000,this);

            //test();

            //init();
            //getValues();

            //View view = findViewById(android.R.id.content);
            //getResultsFromApi(view);
            tp = new timerProg(0, this, 4000);
            readyTimer = new timerProg(666, this, 4000);
//
//        test2();

            //msg();

        /*
        //?????? JOSSAKIN KOODI joka laitaa myös locationin päälle jos ei jo ole
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            System.out.println("xxxxx CHECK PERMISSION");
            // Check Permissions Now
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION);
        }
        */
/*
        _timer = new Timer();
        _timer.schedule(new TimerTask() {
            @Override
            public void run() {UpdateGUI();}
        }, 0, 4000);
        //_timer.schedule(new RemindTask(),0, 2000);
*/
        }

    /*
    private Double[] getLocation() {

        GetLocation getLocation = new GetLocation();
        Double[] loc = getLocation.getLocation(mainView, this);
        return loc;
    }
    */

/*
    private void UpdateGUI() {

        try {
            Log.v("xxx", "#################");
            //cpuTemp.setText("1234567890");
        } catch (Exception ex) {

            Log.v("xxx", ex.getMessage());
        }

        myHandler.post(myRunnable);

    }

    final Runnable myRunnable = new Runnable() {
        public void run() {
            //cpuTemp.setText("1234567890");
            //SysInfo values = getValues(mainView);
            //updateDisplay(values);
        }
    };

*/
    /*
    private boolean finished() {

        taskCnt++;

        if (taskCnt == taskTotal)
            taskCnt = 0;

        return (taskCnt == taskTotal);

    }
    */
/*
    @Override
    public Boolean callback(int id, int inc,int total) {

        //
        if (inc==0) {
            taskCnt = 0;
        } else {
            taskCnt++;
        }

        return (taskCnt==total);
    }
*/


        private String taskName;

        @Override
        public void callback ( int id, String subject, String content){

            if (id == 0) {  // timerille/intervallille varattu

                //Log.v("xxx", "xxxxxx");
                if (!working) {

                    // muuta nimi
                    msg(1, findViewById(android.R.id.content));
                } else {
                    if(taskName=="getData"){

                        // koorinaateille oma step
                        String coords="62.8376,27.6477";
                        try {

                            data=null;
                            data=getWeather2(sourceUrl, coords);
                            //String str=getWeather();
                            //Log.v("xxx",str);
                        }catch(Exception ex){

                            Log.v("xxx","Error: "+ex.getMessage());
                        }
                        taskName = "sendData";

                }else if(taskName == "getInfo") {

                        Log.v("xxx", "getValues(mainView)");
                        getValues(mainView);
                        taskName = "getInfoReady";

                    } else if (taskName == "getInfoReady") {

                        if (taskQueue.status()) {
                            taskQueue.clear();
                            updateDisplay();
                            taskName = "sendInfo";
                        }

                    } else if (taskName == "sendInfo") {


                        //String _return = send_deviceinfo("", "", "");
                        //Log.v("xxx", "SEND_data return= " + _return);
                        //String client_id="Idn4BZdiK22A2SMAAAAC";
                        //socket.emit("gtwmob.notify", "Hyvvee päevee pässit",socketId); //client_id);

                        // muuteaan JSON stringiksi

                        if(info.conType[0]){
                            info.connectionType="Wifi";
                        }else{
                            info.connectionType="Mobile";
                        }

                        Gson gson = new Gson();
                        String data=gson.toJson(info);

                        socket.emit("gtwmob .notify", data,socketId); //client_id);

                        taskName = "";
                        working = false;
                    } else if (taskName == "sendData") {


                        // pitäisi vielä sockeId, jotta kyttjää voidan tiedottaa
                        if(data!=null){

                            Log.v("xxx","data is ready for sending");
                            String result = send_data(data,token,title,socketId);
                            Log.v("xxx","sendidng data return= " +result);
                            taskName = "";
                            working=false;

                        }else{

                            Log.v("xxx","Odotellaan dataa.." );
                        }

                    }

                }

                Log.v("xxx", "id==========> 0");

            } else if (id == 666) {

                Log.v("xxx", "tsekataan onko tehtvä suoritettu");
                Log.v("xxx", Boolean.toString(taskQueue.status()));
                //SysInfo values = getValues(mainView);
                if (taskQueue.status()) {
                    taskQueue.clear();
                    updateDisplay();
                    //              tp.init(true);
                    //getValues(mainView);

                }

            } else if (id == 88) {

                Log.v("xxx", "In callback form TimerClass");
                //SysInfo values = getValues(this.mainView);
                //updateDisplay(values);
            } else {

                if (subject == null) {

                    //***************
                    //SysInfo si=getValues(findViewById(android.R.id.content));
//                updateDisplay(si);
                    //***************

                    Log.v("xxx", "Ei löytynyt viestiä odotellaan... uusi kierros");

                } else {
                    // asetetaan muuttuja niin ettei kysellä uutta viestiä kysellään vasta kun tehtävä suoritettu mutta interval elossa
                    // toisaalta pitisikö asettaa uudellen päälle ja pois


                    Log.v("xxx", "¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤ " + Integer.toString(id));
                    //Log.v("xxx", subject);
                    //Log.v("xxx", content);

                    // content = content.replace("&quot;", "\"");

                    try {

                        Log.v("xxx", "CONTENT= " + content);
                        JSONObject contentJSON = new JSONObject(content);

                        Log.v("xxx", "TOKEN= " + contentJSON.getString("Token"));
                        token=contentJSON.getString("Token");
                        //Log.v("xxx","IP= "+contentJSON.getString("IP"));
                        Log.v("xxx", "SocketId= " + contentJSON.getString("SocketId"));
                        socketId=contentJSON.getString("SocketId");
                        //Log.v("xxx", "Source= " + contentJSON.getString("Source"));
                        sourceUrl=contentJSON.getString("Source");
                        title=contentJSON.getString("Title");
                        Log.v("xxx", "title= "+title);


                        Log.v("xxx","sourceUrl= "+sourceUrl);


                    } catch (Exception ex) {

                        Log.v("xxx", ex.getMessage());
                    }

                    // nämä väliaikaisesti pois
                    working = true;
                    //taskName = "getInfo";
                    taskName="getData";


                    //startReadyTimer();
                    //getValues(mainView);
                    // jos ei toimi niin asetetaan muuttuja ja katsotaan mihin tämö menee seuraavalla kierroksella
                    //timer=new TimerClass(666,2000,this);
/*
                Log.v("xxx", "¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤");
                working = true;
                SysInfo values = getValues(this.mainView);
                updateDisplay(values);
*/

                    //startReadyTimer();
                    //boolean ret = fakeProcess(4000);
                    //Log.v("xxx", "Process over= " + Boolean.toString(ret));
                    //working = false;
                }
            }
            //       tp.init(false);
        }

        // locationissa turha callback arvot palautetaan returnilla
        @Override
        public void callbackLocation (String subject, Double[]location){

            taskQueue.add();
            info.location = location;
            Log.v("xxx", "¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤" + Double.toString(location[0]));
        }

        @Override
        public void callbackSignalStrength ( int strength){

            taskQueue.add();
            info.signalStrength = strength;
            Log.v("xxx", "¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤" + Integer.toString(strength));

        }

    private String send_deviceinfo(String data, String token, String title){

        String myUrl = "http://192.168.43.155:3001/api/datas/device";


        HttpPostRequest postRequest = new HttpPostRequest();

        Log.v("xxx", "URL= " + myUrl);

        String result = "Error";

        try {
            result = postRequest.execute(myUrl, data, token, title).get();

        } catch (Exception ex) {

            Log.v("", "ERROR" + ex.getMessage());
        }

        return result;
    }


        private String send_data (String data, String token, String title,String socketId){

            // tämä saadaan luettua e,ailisyta jos kun pilvessä
            String myUrl = "http://192.168.43.155:3001/api/datas";
            HttpPostRequest postRequest = new HttpPostRequest();

            Log.v("xxx", "URL= " + myUrl);

            String result = "Error";

            try {
                result = postRequest.execute(myUrl, data, token, title,socketId).get();

            } catch (Exception ex) {

                Log.v("", "ERROR" + ex.getMessage());
            }

            return result;
        }


        private void startThread () {

            Toast.makeText(getApplicationContext(), "XXXXXXXXXXX", Toast.LENGTH_LONG).show();

        /*
        Thread thread = new Thread() {
            public void run() {
                Looper.prepare();//Call looper.prepare()

                Log.v("xxx","RUN");

                Handler mHandler = new Handler() {
                    public void handleMessage(Message msg) {
                        Toast.makeText(getApplicationContext(), "Finco is Daddy", Toast.LENGTH_LONG);
                    }
                };

                Looper.loop();
            }
        };
        thread.start();
        */
        }

        private void startReadyTimer () {


            readyTimer.init(true);
        }

        private void init () {
            // Initializing Internet Checker

            Context context = getApplicationContext();

            String str = readJSONFromAsset("settings.json", context);

            try {
                JSONObject reader = new JSONObject(str);
                String email = reader.getString("email");
                PREF_ACCOUNT_NAME = email;
                //Log.v("xxx", "Assetval= " + str + "      " + email);
            } catch (Exception ex) {

                Log.v("xxx", "###### " + ex.getMessage());
            }

            Log.v("xxx", "#1 init ");
            internetDetector = new InternetDetector(getApplicationContext());

            Log.v("xxx", "#2 init ");
            // Initialize credentials and service object.
            mCredential = GoogleAccountCredential.usingOAuth2(
                    getApplicationContext(), Arrays.asList(SCOPES))
                    .setBackOff(new ExponentialBackOff());

        }

        private void updateDisplay () {

            //mobileType.setText("HEIIIIII");

            Log.v("xxx", "UPDATE DISPLAY");
            //locationFld.setText(Integer.toString(taskCnt++));


            mobileType.setText(info.netWorkType);
            mem.setText(Integer.toString(info.memory[0]) + "/" + Integer.toString(info.memory[1]));
            strength.setText(Integer.toString(info.signalStrength));
            locationFld.setText(Double.toString(info.location[0]) + "\n" + Double.toString(info.location[1]));

            wifi.setChecked(info.conType[0]);
            mobile.setChecked(info.conType[1]);

            cpuTemp.setText(Float.toString(info.cpuTemp));
            batTemp.setText(Float.toString(info.batTemp));
            batLevel.setText(Float.toString(info.batLevel));

        }

    /*
    private boolean taskDone(int id,int inc,int total) {

        if (inc == 0) {
            taskCnt = 0;
        } else if(inc==-1) {
        }else{
            taskCnt++;
        }

        return (taskCnt == total);
    }*/

        private void getValues (View view){

            taskQueue = new TaskQueue(0, 8);

            Context context = getApplicationContext();
            taskQueue.add();

            //info.netWorkType=SystemInfo.getNetworkClass(getApplicationContext());

            info.memory = SystemInfo.getFreeRAM(context);
            taskQueue.add();
            //info.signalStrength=SystemInfo.getSignalStrength(context, info.netWorkType);
            //info.location = getLocation();
            info.conType = SystemInfo.getConnectionInfo(context);
            taskQueue.add();
            info.cpuTemp = SystemInfo.cpuTemperature();
            taskQueue.add();
            info.batTemp = SystemInfo.batteryTemperature(context);
            taskQueue.add();
            info.batLevel = SystemInfo.batteryLevel(context);
            taskQueue.add();
            info.netWorkType = SystemInfo.getNetworkClass(getApplicationContext());
            //int strengthValue=
            new GetSignalStrength(getApplicationContext(), info.netWorkType, MainActivity.this);
            new GetLocation().getLocation(view, MainActivity.this);

        }


        private void getPermission () {

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


        public boolean checkLocationPermission () {
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

    private static String getWeather() throws ExecutionException, InterruptedException {

        String secretKey = "ea8a7cd6a76ab7136502dfe91fde6f7b";
        String coords="62.8376,27.6477";
        //String exclude="minutely,hourly,daily,alerts,flags";
        //String exclude="currently,hourly,daily,alerts,flags";
        String exclude="currently,minutely,daily,alerts,flags";

        String myUrl = "https://api.darksky.net/forecast/" + secretKey + "/"+coords+"?units=si&exclude="+exclude;

        Log.v("xxx","#2    "+myUrl);
        //Some url endpoint that you may have
        //String myUrl = "http://api.openweathermap.org/data/2.5/weather?q=Kuopio,FIN&APPID=7f43902114c06c1382d5f60733b098df";

        //Instantiate new instance of our class
        HttpGetRequest getRequest = new HttpGetRequest();
        //Perform the doInBackground method, passing in our url
        String result = getRequest.execute(myUrl).get();

        Log.v("xxx",result);


// rekursio
        //getWeather();

        return result;
    }

    private static String getWeather2(String sourceUrl,String coords) throws ExecutionException, InterruptedException {

        //String secretKey = "ea8a7cd6a76ab7136502dfe91fde6f7b";
        //String coords="62.8376,27.6477";

        //String _str="https://api.darksky.net/forecast/ea8a7cd6a76ab7136502dfe91fde6f7b/%s?units=si&exclude=currently,minutely,daily,alerts,flags";

        sourceUrl= String.format(sourceUrl, coords);
        Log.v("xxx","sourceUrl=" +sourceUrl);


        //String exclude="minutely,hourly,daily,alerts,flags";
        //String exclude="currently,hourly,daily,alerts,flags";
        //String exclude="currently,minutely,daily,alerts,flags";

        //String myUrl = "https://api.darksky.net/forecast/" + secretKey + "/"+coords+"?units=si&exclude="+exclude;

        //Some url endpoint that you may have
        //String myUrl = "http://api.openweathermap.org/data/2.5/weather?q=Kuopio,FIN&APPID=7f43902114c06c1382d5f60733b098df";

        //Instantiate new instance of our class
        HttpGetRequest getRequest = new HttpGetRequest();
        //Perform the doInBackground method, passing in our url
        String result = getRequest.execute(sourceUrl).get();

        Log.v("xxx","RES="+result);


// rekursio
        //getWeather();

        return result;
    }

        private boolean fakeProcess ( int _sleep){

            try {
                Log.v("xxx", "fakeProcess starts");
                sleep(_sleep);
                Log.v("xxx", "fakeProcess ends");
                return true;
            } catch (Exception ex) {

                Log.v("xxx", ex.getMessage());
            }
            return false;
        }

        @Override
        public void onRequestPermissionsResult ( int requestCode,
        String[] permissions, int[] grantResults){
            switch (requestCode) {
                case REQUEST_PERMISSION_GET_ACCOUNTS: {
                    // If request is cancelled, the result arrays are empty.
                    if (grantResults.length > 0
                            && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                        Log.v("xxx", "requestCode=" + requestCode + "   " + "GRANTED");
                        // permission was granted, yay! Do the
                        // contacts-related task you need to do.
                    } else {
                        Log.v("xxx", "requestCode=" + requestCode + "   " + "DENIED");
                        // permission denied, boo! Disable the
                        // functionality that depends on this permission.
                    }
                    return;
                }
                case REQUEST_PERMISSION_ACCESS_FINE_LOCATION: {

                    // If request is cancelled, the result arrays are empty.
                    if (grantResults.length > 0
                            && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                        Log.v("xxx", "requestCode=" + requestCode + "   " + "GRANTED");
                        // permission was granted, yay! Do the
                        // contacts-related task you need to do.
                    } else {
                        Log.v("xxx", "requestCode=" + requestCode + "   " + "DENIED");
                        // permission denied, boo! Disable the
                        // functionality that depends on this permission.
                    }
                    //return;
                }

                // other 'case' lines to check for other
                // permissions this app might request.
            }
        }

        private void showMessage (View view, String message){
            Snackbar.make(view, message, Snackbar.LENGTH_LONG).show();
        }


        private void msg ( int id, View view){

            // pitäisi ensin tarkistaa onko kirjauduttu ja sitähän varten on ewimerkkikoodissa
            // getResultsFromApi
            //new ReadMessageTask(this,mCredential).execute();
            new ReadMessageTask(id, mCredential, getResources().getString(R.string.app_name), PREF_ACCOUNT_NAME, this).execute();


            //subFunc_1();

        }

        // pitöisikö olla ReadMesageTask-luokassa kaikki
        private void getResultsFromApi (View view){

            //Log.v("xxx", "#########");

            try {

/*
            if (!internetDetector.checkMobileInternetConn()) {
                Log.v("xxx", "#5");
                showMessage(view, "No network connection available.");
                Log.v("xxx", "#6");
            } else
  */
                if (!isGooglePlayServicesAvailable()) {

                    Log.v("xxx", "#1");
                    acquireGooglePlayServices();
                    Log.v("xxx", "#2");
                } else if (mCredential.getSelectedAccountName() == null) {
                    //showMessage(view,"cHOOSE account");
                    Log.v("xxx", "#3");
                    chooseAccount(view);
                    Log.v("xxx", "#4");
                }
            } catch (Exception ex) {

                Log.v("xxx", "ERROR= " + ex.getMessage());
            }
        }

        // siirrää seuraavat omaan luokkaansa
        // Method for Checking Google Play Service is Available
        private boolean isGooglePlayServicesAvailable () {
            GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
            int connectionStatusCode = apiAvailability.isGooglePlayServicesAvailable(this);
            return connectionStatusCode == ConnectionResult.SUCCESS;
        }

        // Method to Show Info, If Google Play Service is Not Available.
        private void acquireGooglePlayServices () {
            GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
            int connectionStatusCode = apiAvailability.isGooglePlayServicesAvailable(this);
            if (apiAvailability.isUserResolvableError(connectionStatusCode)) {
                showGooglePlayServicesAvailabilityErrorDialog(connectionStatusCode);
            }
        }

        // Method for Google Play Services Error Info
        void showGooglePlayServicesAvailabilityErrorDialog ( final int connectionStatusCode){
            GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
            Dialog dialog = apiAvailability.getErrorDialog(
                    MainActivity.this,
                    connectionStatusCode,
                    Utilsx.REQUEST_GOOGLE_PLAY_SERVICES);
            dialog.show();
        }

        // Storing Mail ID using Shared Preferences
        private void chooseAccount (View view){

            if (Utilsx.checkPermission(getApplicationContext(), Manifest.permission.GET_ACCOUNTS)) {

                Log.v("xxx", "PREF_ACCOUNT_NAME= " + PREF_ACCOUNT_NAME);
                String accountName = getPreferences(Context.MODE_PRIVATE).getString(PREF_ACCOUNT_NAME, null);
                if (accountName != null) {

                    Log.v("xxx", "#1#accountName= " + accountName);
                    mCredential.setSelectedAccountName(accountName);
                    Log.v("xxx", "#2#accountName= " + accountName);
                    //getResultsFromApi(view);
                    Log.v("xxx", "#3#accountName= " + accountName);
                } else {
                    // Start a dialog from which the user can choose an account
                    Log.v("xxx", "accountName will be asked");
                    startActivityForResult(mCredential.newChooseAccountIntent(), Utilsx.REQUEST_ACCOUNT_PICKER);
                }
            } else {
                Log.v("xxx", "Request for permission ");
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.GET_ACCOUNTS}, REQUEST_PERMISSION_GET_ACCOUNTS);
            }
        }


        @Override
        protected void onActivityResult ( int requestCode, int resultCode, Intent data){
            super.onActivityResult(requestCode, resultCode, data);

            Log.v("xxx", "resultCode=" + resultCode);
            switch (requestCode) {
                case Utilsx.REQUEST_GOOGLE_PLAY_SERVICES:
                    if (resultCode != RESULT_OK) {
                        showMessage(sendFabButton, "This app requires Google Play Services. Please install " +
                                "Google Play Services on your device and relaunch this app.");
                    } else {
                        getResultsFromApi(sendFabButton);
                    }
                    break;
                case Utilsx.REQUEST_ACCOUNT_PICKER:
                    if (resultCode == RESULT_OK && data != null && data.getExtras() != null) {
                        String accountName = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
                        if (accountName != null) {
                            SharedPreferences settings = getPreferences(Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = settings.edit();
                            editor.putString(PREF_ACCOUNT_NAME, accountName);
                            editor.apply();
                            mCredential.setSelectedAccountName(accountName);
                            getResultsFromApi(sendFabButton);
                        }
                    }
                    break;
                case Utilsx.REQUEST_AUTHORIZATION:
                    if (resultCode == RESULT_OK) {
                        getResultsFromApi(sendFabButton);
                    }
            }
        }

    }


/*
class RemindTask extends TimerTask {
    public void run() {
        Log.v("xxx","callback from timerClass");

        cpuTemp.setText(Float.toString(info.cpuTemp));
        //callback.callback(88,"hello from TimerClass","xxxx");
        //timer.cancel(); //Terminate the timer thread

    }
}*/