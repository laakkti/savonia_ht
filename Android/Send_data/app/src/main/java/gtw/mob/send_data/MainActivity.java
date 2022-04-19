package gtw.mob.send_data;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;


import com.github.nkzawa.emitter.Emitter;
    import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import java.net.URISyntaxException;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    private Socket socket;
    private String myUrl = "http://192.168.10.48:3001";

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

                toast(view);
            }
        });

        FloatingActionButton fab3 = findViewById(R.id.fab3);
        fab3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String result = send_data();
                Log.v("xxx", "RESULT= " + result);

                // voisikohan olla tuo sockeid send-.datan yhteydessä tais sitten ei

                String id="Idn4BZdiK22A2SMAAAAC";
                socket.emit("gtwmob.notify", "Hyvvee päevee pässit",id);
            }
        });

        try {
            socket = IO.socket(myUrl);
            socket.connect();
            // tämä saadaan sähköpostilla

            socket.emit("client", "Hyvvee päevee fuck you");

        } catch (URISyntaxException e) {
            e.printStackTrace();

        }


    }

    private String send_data() {

  //      String data = "{\"coord\":{\"lon\":27.68,\"lat\":62.89},\"weather\":[{\"id\":520,\"main\":\"Rain\",\"description\":\"light intensity shower rain\",\"icon\":\"09d\"}],\"base\":\"stations\",\"main\":{\"temp\":274.15,\"pressure\":976,\"humidity\":96,\"temp_min\":274.15,\"temp_max\":274.15},\"visibility\":10000,\"wind\":{\"speed\":3.6,\"deg\":190},\"clouds\":{\"all\":90},\"dt\":1552107000,\"sys\":{\"type\":1,\"id\":1345,\"message\":0.0036,\"country\":\"FI\",\"sunrise\":1552106886,\"sunset\":1552146778},\"id\":650225,\"name\":\"Kuopio\",\"cod\":200}";

//data="[{\"name\":\"Page A\",\"uv\":4000,\"pv\":2400,\"amt\":2400}]
// ";

        //**********************
        //String data="[{\"name\":\"Page A\",\"uv\":4000,\"pv\":2400,\"amt\":2400},{\"name\":\"Page B\",\"uv\":3000,\"pv\":1398,\"amt\":2210},{\"name\":\"Page C\",\"uv\":2000,\"pv\":9800,\"amt\":2290}]";
//**********************

        //Log.v("xxx",data);

        String data="   {\"name\":\"Hellooooooooo\"}";
        //Some url endpoint that you may have
        //String myUrl = "http://192.168.10.48:3001/api/notes/test";



        //Instantiate new instance of our class

        HttpPostRequest postRequest = new HttpPostRequest();
        //Perform the doInBackground method, passing in our url

        // määrittele jokin arvo
        String result = "Error";

        try {

            String myUrlx=myUrl+"/message";

            // tämä siis datan lähetykseen pelää url socketia varten
            //String myUrlx=myUrl+"/test";

            result = postRequest.execute(myUrlx, data).get();
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


    private void toast(View view){

        Snackbar.make(view, "Hyvvee päivöö", Snackbar.LENGTH_INDEFINITE)
                .setAction(   "Ok",new OkListener()).show();

    }

    public class OkListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {

            Log.v("xxx","SnagBar action clicked");
            Snackbar.make(v, "Kiitos samoin", Snackbar.LENGTH_SHORT)
                    .setAction(   "Ok",null).show();
            /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();*/

            // Code to undo the user's last action
        }
    }

}
