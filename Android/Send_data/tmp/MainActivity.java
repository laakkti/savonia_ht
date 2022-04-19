package androidmads.javamailwithgmailapi;

import android.Manifest;
import android.accounts.AccountManager;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.extensions.android.gms.auth.GooglePlayServicesAvailabilityIOException;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.repackaged.org.apache.commons.codec.binary.Base64;
import com.google.api.client.util.ExponentialBackOff;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.GmailScopes;
import com.google.api.services.gmail.model.ListMessagesResponse;
import com.google.api.services.gmail.model.Message;
import com.google.api.services.gmail.model.MessagePart;
import com.google.api.services.gmail.model.MessagePartHeader;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.Timer;
import java.util.concurrent.ExecutionException;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import androidmads.javamailwithgmailapi.helper.InternetDetector;
import androidmads.javamailwithgmailapi.helper.Utils;

// ehkei tarvita??????
import androidmads.javamailwithgmailapi.timerProg;

public class MainActivity extends AppCompatActivity implements Callback{

    FloatingActionButton sendFabButton;
    FloatingActionButton readMessageFabButton;
    EditText edtToAddress, edtSubject, edtMessage, edtAttachmentData;
    Toolbar toolbar;
    GoogleAccountCredential mCredential;
    ProgressDialog mProgress;
    private static final String PREF_ACCOUNT_NAME = "accountName";
    private static final String[] SCOPES = {
            GmailScopes.GMAIL_LABELS,
            GmailScopes.GMAIL_COMPOSE,
            GmailScopes.GMAIL_INSERT,
            GmailScopes.GMAIL_MODIFY,
            GmailScopes.GMAIL_READONLY,
            GmailScopes.MAIL_GOOGLE_COM
    };
    private InternetDetector internetDetector;
    private final int SELECT_PHOTO = 1;
    public String fileName = "";

// muuta funktioin metodin nimi
    private static String getLocalhost(String data) throws ExecutionException, InterruptedException {

        //Some url endpoint that you may have
        String myUrl = "http://192.168.10.48:3001/api/notes/test";

        //Instantiate new instance of our class
        HttpPostRequest postRequest = new HttpPostRequest();
        //Perform the doInBackground method, passing in our url
        //String result = postRequest.execute(myUrl,"{heippa:666999}").get();
        String result = postRequest.execute(myUrl,data).get();
        //String result = postRequest.execute(myUrl).get();

// rekursio
        //getWeather();

        return result;
    }



    private static String getWeather() throws ExecutionException, InterruptedException {

        //Some url endpoint that you may have
        String myUrl = "http://api.openweathermap.org/data/2.5/weather?q=Kuopio,FIN&APPID=7f43902114c06c1382d5f60733b098df";

        //Instantiate new instance of our class
        HttpGetRequest getRequest = new HttpGetRequest();
        //Perform the doInBackground method, passing in our url
        String result = getRequest.execute(myUrl).get();

// rekursio
        //getWeather();

        return result;
    }

    /*
    private void timerProg(){

        final Handler handler = new Handler();

        Runnable runnableCode = new Runnable() {

            @Override
            public void run() {
                // Do something here on the main thread
                //System.out.println("xxxx Called on main thread");

                try {
                    String result = getWeather();
                    System.out.println("xxxxx " + result);
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                handler.postDelayed(this, 2000);
            }
        };
// Run the above code block on the main thread after 2 seconds
        handler.post(runnableCode);


    }
*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();

        // väliaikaisesti pois
        timerProg tp=new timerProg(this,100);
/*
        String result= null;
        try {
            result = this.getLocalhost();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("xxxxx post data" + result);
*/
        findViewById(R.id.attachment).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Utils.checkPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                    photoPickerIntent.setType("image/*");
                    startActivityForResult(photoPickerIntent, SELECT_PHOTO);
                } else {
                    ActivityCompat.requestPermissions(MainActivity.this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, SELECT_PHOTO);
                }
            }
        });

        findViewById(R.id.changeAccount).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Utils.checkPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    startActivityForResult(mCredential.newChooseAccountIntent(), Utils.REQUEST_ACCOUNT_PICKER);
                } else {
                    ActivityCompat.requestPermissions(MainActivity.this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, SELECT_PHOTO);
                }
            }
        });

        sendFabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getResultsFromApi(view, 1);
            }
        });


        readMessageFabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getResultsFromApi(view, 2);
            }
        });

    }

    private void init() {
        // Initializing Internet Checker
        internetDetector = new InternetDetector(getApplicationContext());

        // Initialize credentials and service object.
        mCredential = GoogleAccountCredential.usingOAuth2(
                getApplicationContext(), Arrays.asList(SCOPES))
                .setBackOff(new ExponentialBackOff());

        // Initializing Progress Dialog
        mProgress = new ProgressDialog(this);
        mProgress.setMessage("Sending...");

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        sendFabButton = (FloatingActionButton) findViewById(R.id.fab);
        readMessageFabButton = (FloatingActionButton) findViewById(R.id.fab2);
        edtToAddress = (EditText) findViewById(R.id.to_address);
        edtSubject = (EditText) findViewById(R.id.subject);
        edtMessage = (EditText) findViewById(R.id.body);
        edtAttachmentData = (EditText) findViewById(R.id.attachmentData);

    }

    @Override
    public boolean callback(String str) {

        try {
            String data=getWeather();
            System.out.println("xxxxx " + data);
            String result=getLocalhost(data);
            System.out.println("xxxxx " + result);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // otetaanko dataa talteen vai yksittäin lähtetään expressille vai suoraan kantaan
        // toisaalta express voisi ilman Androida löueka suoraan mutta demo ytarkoituksessa gatewqay
        //
        //System.out.println("xxxx Callback täältä lähtetään tietokantaan "+str);
        return true;
    }

    private void showMessage(View view, String message) {
        Snackbar.make(view, message, Snackbar.LENGTH_LONG).show();
    }

    private void getResultsFromApi(View view, int task) {
        if (!isGooglePlayServicesAvailable()) {
            acquireGooglePlayServices();
        } else if (mCredential.getSelectedAccountName() == null) {
            chooseAccount(view);
        } else if (!internetDetector.checkMobileInternetConn()) {
            showMessage(view, "No network connection available.");
        } else if (!Utils.isNotEmpty(edtToAddress) && task == 1) {
            showMessage(view, "To address Required");
        } else if (!Utils.isNotEmpty(edtSubject) && task == 1) {
            showMessage(view, "Subject Required");
        } else if (!Utils.isNotEmpty(edtMessage) && task == 1) {
            showMessage(view, "Message Required");
        } else {
            if (task == 1) {
                new MakeRequestTask(this, mCredential).execute();
            } else {
                new ReadMessageTask(this, mCredential).execute();
            }
        }
    }

    // Method for Checking Google Play Service is Available
    private boolean isGooglePlayServicesAvailable() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int connectionStatusCode = apiAvailability.isGooglePlayServicesAvailable(this);
        return connectionStatusCode == ConnectionResult.SUCCESS;
    }

    // Method to Show Info, If Google Play Service is Not Available.
    private void acquireGooglePlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int connectionStatusCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (apiAvailability.isUserResolvableError(connectionStatusCode)) {
            showGooglePlayServicesAvailabilityErrorDialog(connectionStatusCode);
        }
    }

    // Method for Google Play Services Error Info
    void showGooglePlayServicesAvailabilityErrorDialog(final int connectionStatusCode) {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        Dialog dialog = apiAvailability.getErrorDialog(
                MainActivity.this,
                connectionStatusCode,
                Utils.REQUEST_GOOGLE_PLAY_SERVICES);
        dialog.show();
    }

    // Storing Mail ID using Shared Preferences
    private void chooseAccount(View view) {
        if (Utils.checkPermission(getApplicationContext(), Manifest.permission.GET_ACCOUNTS)) {
            String accountName = getPreferences(Context.MODE_PRIVATE).getString(PREF_ACCOUNT_NAME, null);
            if (accountName != null) {
                mCredential.setSelectedAccountName(accountName);
                getResultsFromApi(view, 1);
            } else {
                // Start a dialog from which the user can choose an account
                startActivityForResult(mCredential.newChooseAccountIntent(), Utils.REQUEST_ACCOUNT_PICKER);
            }
        } else {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.GET_ACCOUNTS}, Utils.REQUEST_PERMISSION_GET_ACCOUNTS);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case Utils.REQUEST_PERMISSION_GET_ACCOUNTS:
                chooseAccount(sendFabButton);
                break;
            case SELECT_PHOTO:
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, SELECT_PHOTO);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case Utils.REQUEST_GOOGLE_PLAY_SERVICES:
                if (resultCode != RESULT_OK) {
                    showMessage(sendFabButton, "This app requires Google Play Services. Please install " +
                            "Google Play Services on your device and relaunch this app.");
                } else {
                    getResultsFromApi(sendFabButton, 1);
                }
                break;
            case Utils.REQUEST_ACCOUNT_PICKER:
                if (resultCode == RESULT_OK && data != null && data.getExtras() != null) {
                    String accountName = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
                    if (accountName != null) {
                        SharedPreferences settings = getPreferences(Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putString(PREF_ACCOUNT_NAME, accountName);
                        editor.apply();
                        mCredential.setSelectedAccountName(accountName);
                        getResultsFromApi(sendFabButton, 1);
                    }
                }
                break;
            case Utils.REQUEST_AUTHORIZATION:
                if (resultCode == RESULT_OK) {
                    getResultsFromApi(sendFabButton, 1);
                }
                break;
            case SELECT_PHOTO:
                if (resultCode == RESULT_OK) {
                    final Uri imageUri = data.getData();
                    fileName = getPathFromURI(imageUri);
                    edtAttachmentData.setText(fileName);
                }
        }
    }

    public String getPathFromURI(Uri contentUri) {
        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, "", null, "");
        assert cursor != null;
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }
        cursor.close();
        return res;
    }



    // Async Task for readMail using GMail OAuth


    private class ReadMessageTask extends AsyncTask<Void, Void, String> {

        private com.google.api.services.gmail.Gmail mService = null;
        private Exception mLastError = null;
        private View view = readMessageFabButton;
        // mihin tarvitaan,tarvitaanko?
        private MainActivity activity;

        private GoogleAccountCredential mCredential = null;

        ReadMessageTask(MainActivity activity, GoogleAccountCredential credential) {
            HttpTransport transport = AndroidHttp.newCompatibleTransport();
            JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
            mService = new com.google.api.services.gmail.Gmail.Builder(
                    transport, jsonFactory, credential)
                    .setApplicationName(getResources().getString(R.string.app_name))
                    .build();

            this.activity = activity;

        }

        @Override
        protected String doInBackground(Void... params) {
            try {

                String query = "label:inbox from:laakkti@gmail.com is:unread"; // maxResults:4";
                //String query = "maxResults:4";
                List<Message> messages = listMessagesMatchingQuery(mService, "me", query, (long) 1);

                if (messages.size() > 0) {
                    System.out.println("xxxXXX" + messages.get(0).getId() + "   " + messages.size());
                } else {

                    System.out.println("xxxXXX" + "0-size");
                }

                Message message = getMessage(mService, "me", messages.get(0).getId());

                System.out.println("xxxXXX" + message.getLabelIds().toString());
                System.out.println("xxxXXX" + message.getSnippet());
                System.out.println("xxxXXX" + message.getPayload().getHeaders().get(0).getName());
                String subject = getMessageSubject(message);
                System.out.println("xxxXXX" + subject);

//                String jsonMessageData=getMessageData(mService,"me");

                return "";
            } catch (Exception e) {
                mLastError = e;

                cancel(true);
                return null;
            }
        }

        private String getMessageSubject(Message message) throws Exception {

//		System.out.println(message.toPrettyString());

            MessagePart msgpart = message.getPayload();


            List<MessagePartHeader> headers = message.getPayload().getHeaders();

            for (MessagePartHeader header : headers) {

                if (header.getName().toString().equals("Subject")) {

                    return header.getValue().toString();

                }

            }

            return "";

        }

        public List<Message> listMessagesMatchingQuery(Gmail service, String userId,
                                                       String query, long maxResults) throws IOException {

            //Gmail.Users.Messages.List search = service.users().messages().list(userId).setMaxResults((long) 1).setQ(query);
            //ListMessagesResponse response = search.execute(); //service.users().messages().list(userId).setQ(query).execute();
            ListMessagesResponse response = service.users().messages().list(userId).setMaxResults(maxResults).setQ(query).execute();
            List<Message> messages = new ArrayList<Message>();

            messages = (List<Message>) response.getMessages();

            return messages;
        }

        public Message getMessage(Gmail service, String userId, String messageId)
                throws IOException {
            Message message = service.users().messages().get(userId, messageId).execute();

            System.out.println("Message snippet: " + message.getSnippet());

            return message;
        }


    }

    // Async Task for sending Mail using GMail OAuth
    private class MakeRequestTask extends AsyncTask<Void, Void, String> {

        private com.google.api.services.gmail.Gmail mService = null;
        private Exception mLastError = null;
        private View view = sendFabButton;
        // mihin tarvitaan,tarvitaanko?
        private MainActivity activity;

        MakeRequestTask(MainActivity activity, GoogleAccountCredential credential) {
            HttpTransport transport = AndroidHttp.newCompatibleTransport();
            JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
            mService = new com.google.api.services.gmail.Gmail.Builder(
                    transport, jsonFactory, credential)
                    .setApplicationName(getResources().getString(R.string.app_name))
                    .build();
            this.activity = activity;
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                return getDataFromApi();
            } catch (Exception e) {
                mLastError = e;
                cancel(true);
                return null;
            }
        }

        private String getDataFromApi() throws IOException {
            // getting Values for to Address, from Address, Subject and Body
            String user = "me";
            String to = Utils.getString(edtToAddress);
            String from = mCredential.getSelectedAccountName();
            String subject = Utils.getString(edtSubject);
            String body = Utils.getString(edtMessage);
            MimeMessage mimeMessage;
            String response = "";
            try {
                mimeMessage = createEmail(to, from, subject, body);
                response = sendMessage(mService, user, mimeMessage);
            } catch (MessagingException e) {
                e.printStackTrace();
            }
            return response;
        }

        // Method to send email
        private String sendMessage(Gmail service,
                                   String userId,
                                   MimeMessage email)
                throws MessagingException, IOException {
            Message message = createMessageWithEmail(email);
            // GMail's official method to send email with oauth2.0

            message = service.users().messages().send(userId, message).execute();

            return message.getId();
        }


        // Method to create email Params
        private MimeMessage createEmail(String to,
                                        String from,
                                        String subject,
                                        String bodyText) throws MessagingException {
            Properties props = new Properties();
            Session session = Session.getDefaultInstance(props, null);

            MimeMessage email = new MimeMessage(session);
            InternetAddress tAddress = new InternetAddress(to);
            InternetAddress fAddress = new InternetAddress(from);

            email.setFrom(fAddress);
            email.addRecipient(javax.mail.Message.RecipientType.TO, tAddress);
            email.setSubject(subject);

            // Create Multipart object and add MimeBodyPart objects to this object
            Multipart multipart = new MimeMultipart();

            // Changed for adding attachment and text
            // email.setText(bodyText);

            BodyPart textBody = new MimeBodyPart();
            textBody.setText(bodyText);
            multipart.addBodyPart(textBody);

            if (!(activity.fileName.equals(""))) {
                // Create new MimeBodyPart object and set DataHandler object to this object
                MimeBodyPart attachmentBody = new MimeBodyPart();
                String filename = activity.fileName; // change accordingly
                DataSource source = new FileDataSource(filename);
                attachmentBody.setDataHandler(new DataHandler(source));
                attachmentBody.setFileName(filename);
                multipart.addBodyPart(attachmentBody);
            }

            //Set the multipart object to the message object
            email.setContent(multipart);
            return email;
        }

        private Message createMessageWithEmail(MimeMessage email)
                throws MessagingException, IOException {
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            email.writeTo(bytes);
            String encodedEmail = Base64.encodeBase64URLSafeString(bytes.toByteArray());
            Message message = new Message();
            message.setRaw(encodedEmail);
            return message;
        }

        @Override
        protected void onPreExecute() {
            mProgress.show();
        }

        @Override
        protected void onPostExecute(String output) {
            mProgress.hide();
            if (output == null || output.length() == 0) {
                showMessage(view, "No results returned.");
            } else {
                showMessage(view, output);
            }
        }

        @Override
        protected void onCancelled() {
            mProgress.hide();
            if (mLastError != null) {
                if (mLastError instanceof GooglePlayServicesAvailabilityIOException) {
                    showGooglePlayServicesAvailabilityErrorDialog(
                            ((GooglePlayServicesAvailabilityIOException) mLastError)
                                    .getConnectionStatusCode());
                } else if (mLastError instanceof UserRecoverableAuthIOException) {
                    startActivityForResult(
                            ((UserRecoverableAuthIOException) mLastError).getIntent(),
                            Utils.REQUEST_AUTHORIZATION);
                } else {
                    showMessage(view, "The following error occurred:\n" + mLastError);
                    Log.v("Error", mLastError + "");
                }
            } else {
                showMessage(view, "Request Cancelled.");
            }
        }
    }

}
