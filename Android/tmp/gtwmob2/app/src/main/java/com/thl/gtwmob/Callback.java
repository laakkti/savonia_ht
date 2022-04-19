package com.thl.gtwmob;

public interface Callback {

    void callback(int id,String subject,String content);

    void callbackLocation(String subject,Double[] location);
}
