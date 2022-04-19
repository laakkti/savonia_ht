package com.thl.gtwmob.backup;

import android.content.Context;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.thl.gtwmob.Callback;

import java.lang.reflect.Method;

public class GetSignalStrength extends PhoneStateListener{

    static String LTE_SIGNAL_STRENGTH = "getLteSignalStrength";
    private TelephonyManager telephonyManager;
    public static int _signalStrength=-1;
    public static SignalStrength signalStrength;
    //private Handler handler;
    //private Runnable runnableCode;

    private Callback callback;

    private PhoneStateListener mListener = new PhoneStateListener() {

        @Override
        public void onSignalStrengthsChanged(SignalStrength sStrength) {

            signalStrength = sStrength;
            //Log.v("Looper","##3 onSignalStrength "+signalStrength.toString());
            _signalStrength=getLTEsignalStrength();
            //*******************
            Log.v("Looper", "##4--------------- "+signalStrength);
            //handler.sendMessage(handler.obtainMessage());
            Log.v("Looper", "##5--------------- "+_signalStrength);
            telephonyManager.listen(mListener, PhoneStateListener.LISTEN_NONE);
            callback.callbackSignalStrength(_signalStrength);
//*******************
        }
    };

    public GetSignalStrength(Context context, String netWorkType, Callback callback){

        this.callback=callback;
        telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        telephonyManager.listen(mListener, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);
    }

    public int GetValue(){

        /*
        handler = new Handler();
        runnableCode = new Runnable() {

            @Override
            public void run() {
                // Do something here on the main thread

                if(_signalStrength!=-1){
                    handler.removeCallbacks(runnableCode);
                    handler=null;
                    runnableCode=null;
                    return _signalStrength;
                }
                //System.out.println("xxxxx before");
                //boolean status=c
//                cb.callback(id, "heippa from xtimerprog", "");
                //System.out.println("xxxxx after " + status);
                //String result = getWeather();
                //System.out.println("xxxxx " + result);

                handler.postDelayed(this, 1000);
            }
        };
// Run the above code block on the main thread after delay
        handler.post(runnableCode);
*/


/*
        do {

            Log.v("xxx",Integer.toString(_signalStrength));
        }while(_signalStrength==-1);
*/
        return _signalStrength;
    }

    //PhoneStateListener mListener = new PhoneStateListener()

    // Register the listener for the telephony manager
    public static int getLTEsignalStrength()
    {
        try
        {
            Method[] methods = SignalStrength.class.getMethods();

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

}
