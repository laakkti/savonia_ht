package gtw.mob.request_data;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import java.net.Socket;
import java.net.URISyntaxException;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    //    private Socket socket;
    //private String myUrl = "http://192.168.10.48:3001/test";
    private String myUrl = "http://127.0.0.1:3001/test";

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

                //toast(view);
                String data = null;
                try {
                    data = getWeather();
                } catch (ExecutionException e) {
                    //e.printStackTrace();
                    System.out.println("xxx" + e.getMessage());
                } catch (InterruptedException e) {
                    //e.printStackTrace();
                    System.out.println("xxx" + e.getMessage());
                }
                System.out.println("xxx" + data);


// tämä on testiä jatkossa token luetaan emailista
                //String token="bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VybmFtZSI6ImxhYWtrdGkiLCJpZCI6IjVjY2M0N2ZjOWU5MDVhMWNlODQ5YTVjOCIsImlhdCI6MTU1NjkxMjQ3NH0.grMREdX-_rzVqV5QzsUsN4xS2_Tiq7pADVzoMQVN-lM";
                String title="dataset_4.5.2019";
                String token="bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VybmFtZSI6ImxhYWtrdGkiLCJpZCI6IjVjY2M0N2ZjOWU5MDVhMWNlODQ5YTVjOCIsImlhdCI6MTU1NjkxMjQ3NH0.grMREdX-_rzVqV5QzsUsN4xS2_Tiq7pADVzoMQVN-lM";
                String result = send_data(data,token,title);
                Log.v("xxx", "RESULT= " + result);
            }
        });

/*
        try {
            socket = IO.socket(myUrl);
            socket.connect();
            socket.emit("join", Nickname);
        } catch (URISyntaxException e) {
            e.printStackTrace();

        }
*/

    }

    private String send_data(String data,String token,String title) {

        //      String data = "{\"coord\":{\"lon\":27.68,\"lat\":62.89},\"weather\":[{\"id\":520,\"main\":\"Rain\",\"description\":\"light intensity shower rain\",\"icon\":\"09d\"}],\"base\":\"stations\",\"main\":{\"temp\":274.15,\"pressure\":976,\"humidity\":96,\"temp_min\":274.15,\"temp_max\":274.15},\"visibility\":10000,\"wind\":{\"speed\":3.6,\"deg\":190},\"clouds\":{\"all\":90},\"dt\":1552107000,\"sys\":{\"type\":1,\"id\":1345,\"message\":0.0036,\"country\":\"FI\",\"sunrise\":1552106886,\"sunset\":1552146778},\"id\":650225,\"name\":\"Kuopio\",\"cod\":200}";

//data="[{\"name\":\"Page A\",\"uv\":4000,\"pv\":2400,\"amt\":2400}]
// ";

        //**********************
        //String data="[{\"name\":\"Page A\",\"uv\":4000,\"pv\":2400,\"amt\":2400},{\"name\":\"Page B\",\"uv\":3000,\"pv\":1398,\"amt\":2210},{\"name\":\"Page C\",\"uv\":2000,\"pv\":9800,\"amt\":2290}]";
//**********************

        //String data = "Androidista päivää...";
        //Some url endpoint that you may have
        //String myUrl = "http://192.168.10.48:3001/api/notes/test";

        //String myUrl = "http://192.168.10.50:3001/api/datas";
        String myUrl = "http://192.168.43.155:3001/api/datas";
        //String myUrl = "http://127.0.0.1:3001/api/datas";
        //Instantiate new instance of our class

        HttpPostRequest postRequest = new HttpPostRequest();
        //Perform the doInBackground method, passing in our url

        // määrittele jokin arvo
        String result = "Error";

        try {

         //   myUrl = myUrl;


// kuinka const config = {
//    headers: { Authorization: token },
//  }
//
//  const response = await axios.post(baseUrl, newObject, config)
            result = postRequest.execute(myUrl, data,token,title).get();
            //String result = postRequest.execute(myUrl,data).get();
            //String result = postRequest.execute(myUrl).get();

// rekursio
            //getWeather();

            //    return result;

        } catch (Exception ex) {

            Log.v("", "ERROR" + ex.getMessage());
        }

        return result;
    }

    private static String getWeather() throws ExecutionException, InterruptedException {

        String secretKey = "ea8a7cd6a76ab7136502dfe91fde6f7b";
        String coords="62.8376,27.6477";
        //String exclude="minutely,hourly,daily,alerts,flags";
        //String exclude="currently,hourly,daily,alerts,flags";
        String exclude="currently,minutely,daily,alerts,flags";

        String myUrl = "https://api.darksky.net/forecast/" + secretKey + "/"+coords+"?units=si&exclude="+exclude;

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


    private void toast(View view) {

        Snackbar.make(view, "Hyvvee päivöö", Snackbar.LENGTH_INDEFINITE)
                .setAction("Ok", new OkListener()).show();

    }

    public class OkListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {

            Log.v("xxx", "SnagBar action clicked");
            Snackbar.make(v, "Kiitos samoin", Snackbar.LENGTH_SHORT)
                    .setAction("Ok", null).show();
            /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();*/

            // Code to undo the user's last action
        }
    }

}
