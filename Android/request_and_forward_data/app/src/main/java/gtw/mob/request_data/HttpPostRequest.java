package gtw.mob.request_data;

import android.os.AsyncTask;

import org.json.JSONException;
import android.util.Log;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import static android.content.ContentValues.TAG;
//import static com.google.api.client.http.HttpMethods.POST;

public class HttpPostRequest extends AsyncTask<String, Void, String> {
    public static final String REQUEST_METHOD = "GET";
    public static final int READ_TIMEOUT = 15000;
    public static final int CONNECTION_TIMEOUT = 15000;
    @Override
    protected String doInBackground(String... params){
        String stringUrl = params[0];
        //String postData=params[1];

        //String postData="{heippa:666}";


        //JSONObject postData=null;
        String postData=null;

        try {

            postData=params[1];



            //postData = new JSONObject(params[1]);
            //System.out.println("xxxxx ====="+postData.toString());
            //JSONObject postData2 = new JSONObject(params[1]);
            //postData = new JSONObject("{heippa:666}");
            //System.out.println("xxxxx ====="+postData.toString()+"="+postData2.toString());
        } catch (Exception e) {
            //} catch (JSONException e) {
            System.out.println("xxxxx Error"+e.getMessage());
            e.printStackTrace();
        }


        String result;
        String inputLine;
        try {
            //Create a URL object holding our url
            URL myUrl = new URL(stringUrl);

            //Create a connection
            HttpURLConnection connection =(HttpURLConnection)
                    myUrl.openConnection();
            //Set methods and timeouts
            connection.setReadTimeout(READ_TIMEOUT);
            connection.setConnectTimeout(CONNECTION_TIMEOUT);
            connection.setRequestMethod("POST");
            connection.setDoInput(true);
            connection.setDoOutput(true);
            // vois olla parametreissa mutta JSON usein käytetty tiedonvälitysformaatti
            connection.setRequestProperty("Content-Type", "application/json");

            if(params[2]!=null){
                connection.setRequestProperty("authorization", params[2]);
            }

            if(params[3]!=null){
                connection.setRequestProperty("Title", params[3]);
            }

            //connection.setRequestProperty("Content-Type", "text/plain");
            //Connect to our url
            //connection.connect();


            if (postData != null) {

                OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
// kokeeksi pois suraan nsireingionä parametri
                //                writer.write(postData.toString());
                writer.write(postData);
                writer.flush();
                System.out.println("xxxxx"+postData.toString());

                /*
                OutputStream os = connection.getOutputStream();

                os.write(postData.getBytes());

                os.flush();

                os.close();*/
            }

            int statusCode = connection.getResponseCode();
            System.out.println("xxx POST Response Code :  " + statusCode);

            System.out.println("xxx POST Response Message : " + connection.getResponseMessage());


            if (statusCode ==  200) {

                InputStream inputStream = new BufferedInputStream(connection.getInputStream());

                String response = convertInputStreamToString(inputStream);

                return response;

                // From here you can convert the string to JSON with whatever JSON parser you like to use
                // After converting the string to JSON, I call my custom callback. You can follow this process too, or you can implement the onPostExecute(Result) method
            } else {
                // Status code is not 200
                // Do something to handle the error
            }

        } catch (Exception e) {
            Log.d(TAG, e.getLocalizedMessage());
        }
        return null;
    }

    private String convertInputStreamToString(InputStream inputStream) {
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        StringBuilder sb = new StringBuilder();
        String line;
        try {
            while((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }


    protected void onPostExecute(String result){
        super.onPostExecute(result);
    }
}