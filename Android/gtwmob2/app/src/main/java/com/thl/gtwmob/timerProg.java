package com.thl.gtwmob;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;


// nimi vois olla interval tai jotain
public class timerProg { //implements Callback{

    private Callback cb;
    private int delay;
    private int id;

    private Handler handler;
    private Runnable runnableCode;

    public timerProg(int id, Callback cb, int delay) {

        this.cb = cb;
        this.delay = delay;
        this.id = id;
        //cb("HELLO");
//        init(true);
        //cb.callback("xxx");
    }

    //private void init(final Callback cb){
    public void init(boolean p) {

        if(p) {


            handler = new Handler();

            runnableCode = new Runnable() {

                @Override
                public void run() {
                    // Do something here on the main thread

                    //System.out.println("xxxxx before");
                    //boolean status=c
                    cb.callback(id, "xxx", "");
                    //System.out.println("xxxxx after " + status);
                    //String result = getWeather();
                    //System.out.println("xxxxx " + result);

                    handler.postDelayed(this, delay);
                }
            };
// Run the above code block on the main thread after delay
            handler.post(runnableCode);

        }else{
            Log.v("xxx", "STOP timer");
                    handler.removeCallbacks(runnableCode);
            handler=null;
            runnableCode=null;
        }
    }
}