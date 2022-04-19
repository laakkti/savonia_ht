package com.thl.gtwmob;

import android.os.AsyncTask;
import android.util.Log;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.repackaged.org.apache.commons.codec.binary.StringUtils;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.Message;
import com.google.api.services.gmail.model.ListMessagesResponse;
import com.google.api.services.gmail.model.MessagePart;
import com.google.api.services.gmail.model.MessagePartHeader;
import com.google.api.services.gmail.model.ModifyMessageRequest;
import com.google.api.client.repackaged.org.apache.commons.codec.binary.Base64;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class ReadMessageTask extends AsyncTask<String, Void, String> {

    private Gmail mService;
    private String ACCOUNT_NAME;
    private Callback callback;
    private int id;

    ReadMessageTask(int id,GoogleAccountCredential credential, String app_name, String account_name, Callback callback) {

        HttpTransport transport = AndroidHttp.newCompatibleTransport();
        JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
        mService = new Gmail.Builder(
                transport, jsonFactory, credential)
                .setApplicationName(app_name)
                .build();

        ACCOUNT_NAME = account_name;
        this.callback = callback;
        this.id=id;
    }


    @Override
    protected String doInBackground(String... params) {

        //String stringUrl = params[0];

        String query = "label:inbox from:" + ACCOUNT_NAME + " is:unread"; // maxResults:4";

        //Log.v("xxx", "====" + query);

        try {
            // HUOM userId on muttujassa  kelpaako "me"

            Log.v("xxx", "====" + query);
            List<Message> messages = listMessagesMatchingQuery(mService, ACCOUNT_NAME, query, (long) 100);

            Log.v("xxx", "====" + "after query");

            //Log.v("xxx", "====" + query);

            if (messages != null) {

                // vanhin query-ehdon täyttävä eli tässä tapauksessa venhin lukematon
                String messageId = messages.get(messages.size() - 1).getId();
          //      Log.v("xxx", "xxxXXX" + messageId + "   " + messages.size());

                Message message = getMessage(mService, "me", messageId);

            //    Log.v("xxx", "####### " + message.getLabelIds().toString());

                /*
                System.out.println("#1 xxxXXX" + message.getLabelIds().toString());
                System.out.println("#2 xxxXXX" + message.getSnippet());
                System.out.println("#3 xxxXXX" + message.getPayload().getHeaders().get(0).getName());
                */
                String subject = getMessageSubject(message);
                //String content = message. //.getSnippet();


                //System.out.println(StringUtils.newStringUtf8(Base64.decodeBase64 (message.getRaw())));
                //System.out.println("#4 xxxXXX " + subject);
                MessagePart part = message.getPayload();
                String content=StringUtils.newStringUtf8(Base64.decodeBase64(part.getBody().getData()));
                 //String content=message.getRaw(); //StringUtils.newStringUtf8(Base64.decodeBase64(message.getRaw()));
                // merkitään viesti luetuksi TEE funktio
                 //Log.v("xxx","%%%%%%%%%%% "+ content);

                this.markMessageAsRead(messageId);

                //return message.getSnippet();
                this.callback.callback(this.id,subject,content);
            }else{

                this.callback.callback(this.id,null,null);
            }

        } catch (Exception ex) {

            Log.v("xxx", "ERRORxxxx "+ex.getMessage());
        }
//                String jsonMessageData=getMessageData(mService,"me");

        return "";

        /*
            } catch (Exception e) {
                //mLastError = e;

                cancel(true);
                return null;
            }*/
    }

    //return null;
    public void markMessageAsRead(String messageId) {

        try {
            ModifyMessageRequest modifyMessageRequest = new ModifyMessageRequest().setRemoveLabelIds(Collections.singletonList("UNREAD"));

            String username = "me";
            mService.users().messages().modify(username, messageId, modifyMessageRequest).execute();
        } catch (Exception ex) {

            Log.v("xxx", ex.getMessage());
        }
    }

    public Message getMessage(Gmail service, String userId, String messageId)
            throws IOException {
        Message message = service.users().messages().get(userId, messageId).execute();

//        System.out.println("Message snippet: " + message.getSnippet());

        return message;
    }

    private String getMessageSubject(Message message) throws Exception {

//		System.out.println(message.toPrettyString());

        //MessagePart msgpart = message.getPayload();


        List<MessagePartHeader> headers = message.getPayload().getHeaders();

        for (MessagePartHeader header : headers) {

            if (header.getName().equals("Subject")) {

                return header.getValue();

            }

        }

        return "";

    }


    public List<Message> listMessagesMatchingQuery(Gmail service, String userId,
                                                   String query, long maxResults) throws IOException {

        //Log.v("xxx", "#19");
        //Gmail.Users.Messages.List search = service.users().messages().list(userId).setMaxResults((long) 1).setQ(query);
        //ListMessagesResponse response = search.execute(); //service.users().messages().list(userId).setQ(query).execute();
        ListMessagesResponse response = service.users().messages().list(userId).setMaxResults(maxResults).setQ(query).execute();
        //Log.v("xxx", "#20");
        List<Message> messages = response.getMessages();
        //Log.v("xxx", "#21");
        return messages;
    }

    protected void onPostExecute(String result) {
        super.onPostExecute(result);

    }
}

