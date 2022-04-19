package com.thl.gtwmob;

import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

public class TimerClass {

    private Timer timer;

    private Callback callback;
    private int id;

    public TimerClass(int id, int delay,Callback callback) {
        this.callback=callback;
        this.id=id;
        timer = new Timer();

        timer.schedule(new RemindTask(),0, delay);
    }

    public void stop(){

        timer.cancel();
    }

    class RemindTask extends TimerTask {
        public void run() {
            Log.v("xxx","callback from timerClass");
            callback.callback(id,"hello from TimerClass","xxxx");
            //timer.cancel(); //Terminate the timer thread

        }
    }
}
