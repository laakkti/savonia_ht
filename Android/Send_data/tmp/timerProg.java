package androidmads.javamailwithgmailapi;


import android.os.Handler;

import java.util.concurrent.ExecutionException;




public class timerProg{ //implements Callback{




    private Callback cb;
    private int delay;
    public timerProg(Callback cb,int delay){

      this.cb=cb;
      this.delay=delay;
        //cb("HELLO");
        init();

        //cb.callback("xxx");
    }

    //private void init(final Callback cb){
        private void init(){

        final Handler handler = new Handler();

        Runnable runnableCode = new Runnable() {



            @Override
            public void run() {
                // Do something here on the main thread

                System.out.println("xxxxx before");
                boolean status=cb.callback("heippa from xtimerprog");
                System.out.println("xxxxx after " + status);
                    //String result = getWeather();
                    //System.out.println("xxxxx " + result);

                handler.postDelayed(this, delay);
            }
        };
// Run the above code block on the main thread after 2 seconds
        handler.post(runnableCode);


    }


}